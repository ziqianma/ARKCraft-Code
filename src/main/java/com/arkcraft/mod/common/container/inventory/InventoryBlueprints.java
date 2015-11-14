package com.arkcraft.mod.common.container.inventory;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.player.ARKPlayer;
import com.arkcraft.mod.common.handlers.ARKCraftingManager;
import com.arkcraft.mod.common.handlers.IARKRecipe;
import com.arkcraft.mod.common.lib.LogHelper;
import com.arkcraft.mod.common.network.UpdatePlayerCrafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.List;

/***
 * 
 * @author wildbill22
 *
 */
public class InventoryBlueprints extends InventoryBasic {
	ARKCraftingManager craftingManager;
	InventoryPlayerCrafting invCrafting;
//	IInventory invCrafting;
	
	public InventoryBlueprints(String title, boolean customName, int slotCount) {
		super(title, customName, slotCount);
	}

	public InventoryBlueprints(String title, boolean customName, int slotCount, ARKCraftingManager craftingManager,
			InventoryPlayerCrafting invCrafting, short craftTimeForItem) {
		super(title, customName, slotCount);
		this.craftingManager = craftingManager; 
		setNumBlueprints(craftingManager.getNumRecipes());
		fillInventoryWithRecipes();
		this.invCrafting = invCrafting;
		CRAFT_TIME_FOR_ITEM = craftTimeForItem;
	}

	// We do not save the blueprint inventory to NBT, it is loaded from the Crafting Manager

    /**
     * Do not give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        return false;
    }

    public void openInventory(EntityPlayer player)  {
        super.openInventory(player);
    }

    public void closeInventory(EntityPlayer player) {
        super.closeInventory(player);
    }

	//----------------- End of normal inventory function, rest for crafting -----------------

    // Variables to sync from client to server
	private boolean craftOne = false;
	private short blueprintPressed = 0; // Button pressed
	
	private void sendUpdateToServer(){
		ARKCraft.modChannel.sendToServer(new UpdatePlayerCrafting(craftOne, blueprintPressed));
	}
	
	public void setCraftOnePressed(boolean craftOnePressed, int i, boolean andUpdateServer) {
//		LogHelper.info("InventoryBlueprints: Set craftOne to " + craftOnePressed + " on " + FMLCommonHandler.instance().getEffectiveSide());
		this.craftOne = craftOnePressed;
		blueprintPressed = (short) i;
		if (andUpdateServer)
			sendUpdateToServer();
	}
	
	/** The number of blueprints (number of recipes defined) */
	private int numBlueprints;
	public int getNumBlueprints() {
		return numBlueprints;
	}
	public void setNumBlueprints(int num){
		this.numBlueprints = num;
	}

	public boolean isCrafting() {
		return craftOne;
	}

	/** Returns double between 0 and 1 representing % done */
	public double fractionCraftingRemainingForItem() {
		if (craftingTime < 0)
			return 0.0D;
		double fraction = craftingTime / (double)CRAFT_TIME_FOR_ITEM;
		return MathHelper.clamp_double(fraction, 0.0, 1.0);
	}

	/** The number of items that can be crafted */
	private short numThatCanBeCrafted = 0;

	private final int craftTickRefreshRate = 5;
	private int canCraftTick = craftTickRefreshRate;
	// Warning: numThatCanBeCrafted is only set on client when this is called, but crafting logic takes this into account
    @SideOnly(Side.CLIENT)
	public int getNumToBeCrafted(int i) {
		if (canCraftTick >= 0){
			canCraftTick--;
		} else {
			canCraftTick = craftTickRefreshRate;
		}
		if (canCraftTick == craftTickRefreshRate){
			blueprintPressed = (short) i;
			canCraft();
		}
		return numThatCanBeCrafted;
	}

	/** The number of seconds required to craft an item */
	private static short CRAFT_TIME_FOR_ITEM;

	/** The number of seconds the current item has been crafting 
	 *  Logic:
	 *  -1 when none are being crafted
	 *   0 when the items is to be crafted
	 *   n seconds until it will be crafted  
	 */
	private short craftingTime = -1;
	/** Time to craft current item being crafted */
	public int craftingTimeRemainingOnItem() {
		return (int) craftingTime;
	}

	private int tick = 20;
	
	// This method is called every tick to update the tile entity (called from PlayerTickEvent)
	// It runs both on the server and the client.
	public void update() {
		if (tick >= 0){
			tick--;
			return;			
		} else {
			tick = 20;
		}

//		LogHelper.info("InventoryBlueprints: Update called on " + FMLCommonHandler.instance().getEffectiveSide());
		
		// If not crafting an item, return
		if (!craftOne)
			return;
		
		// Reset crafting time if it reaches -1 (is true after crafting one of multiple, or after pushing button in GUI)
		if (craftingTime < 0){
			craftingTime = CRAFT_TIME_FOR_ITEM;
			// See if an item can be crafted
			if (!craftItem(false)){
				craftOne = false;
				craftingTime = -1;
				return;
			}
		}
		else
			craftingTime--;
		
		// If craftingTime has reached -1, try and craft the item
		if (craftingTime < 0) {
			LogHelper.info("InventoryBlueprints: About to craft the item on " + FMLCommonHandler.instance().getEffectiveSide());
			craftItem();
			craftOne = false;
		}
	}

	/**
	 * Check if the item is craftable and there is sufficient space in the output slots
	 * @return true if crafting the item is possible
	 */
	private boolean canCraft() {
		return craftItem(false);
	}

	/**
	 * Craft an item, if possible
	 */
	private boolean craftItem() { return craftItem(true); }

	/**
	 * checks that there are enough items to craft the item and that there is room for the result in the output slots
	 * If desired, crafts the item
	 * @param doCraftItem - If true, craft the item. If false, check whether crafting is possible, but don't change the inventory
	 * @return false if no item can be crafted, true otherwise
	 */
	private boolean craftItem(boolean doCraftItem){
		Integer firstSuitableOutputSlot = null;
		ItemStack result = getStackInSlot(blueprintPressed);

		// No recipes?
		if (result == null)
			return false;
		
		// find the first suitable output slot, 1st check for identical item that has enough space
		for (int outputSlot = ARKPlayer.LAST_INVENTORY_SLOT; outputSlot > ARKPlayer.FIRST_INVENTORY_SLOT; outputSlot--) {
			ItemStack outputStack = invCrafting.getStackInSlot(outputSlot);
			if (outputStack != null && outputStack.getItem() == result.getItem() && 
					(!result.getHasSubtypes() || outputStack.getMetadata() == result.getMetadata())
					&& ItemStack.areItemStackTagsEqual(outputStack, result)) {
				int combinedSize = invCrafting.getStackInSlot(outputSlot).stackSize + result.stackSize;
				if (combinedSize <= invCrafting.getInventoryStackLimit() 
						&& combinedSize	<= invCrafting.getStackInSlot(outputSlot).getMaxStackSize()) {
					firstSuitableOutputSlot = outputSlot;
					break;
				}
			}
		}
		if (firstSuitableOutputSlot == null) {
			// 2nd look for empty slot if no partially filled slots are found
			for (int outputSlot = ARKPlayer.LAST_INVENTORY_SLOT; outputSlot > ARKPlayer.FIRST_INVENTORY_SLOT; outputSlot--) {
				ItemStack outputStack = invCrafting.getStackInSlot(outputSlot);
				if (outputStack == null) {
					firstSuitableOutputSlot = outputSlot;
					break;
				}
			}
		}
		if (firstSuitableOutputSlot == null){
			LogHelper.info("InventoryBlueprints: No output slots available.");
			return false;
		}

		// finds if there is enough inventory to craft the result
		numThatCanBeCrafted = (short) craftingManager.hasMatchingRecipe(result, invCrafting, false);
		if (numThatCanBeCrafted <= 0) {
			LogHelper.info("InventoryBlueprints: Can't craft item from inventory.");
			return false;			
		}
		else if (!doCraftItem)
			return true;

		// Craft an item (after testing that there is enough inventory above)
		int numCrafted = (short) craftingManager.hasMatchingRecipe(result, invCrafting, true);
		
		// This should never be true!
		if (numCrafted <= 0)
			return false;

		// alter output slot
		LogHelper.info("InventoryBlueprints: Copy craft result to slot: " + firstSuitableOutputSlot);
		if (invCrafting.getStackInSlot(firstSuitableOutputSlot) == null) {
			invCrafting.setInventorySlotContents(firstSuitableOutputSlot, result.copy()); // Use deep .copy() to avoid altering the recipe
		} else {
			invCrafting.getStackInSlot(firstSuitableOutputSlot).stackSize += result.stackSize;
		}
		invCrafting.markDirty();
		return true;
	}

	@SuppressWarnings("rawtypes")
	public void fillInventoryWithRecipes(){
		List recipes = craftingManager.getRecipeList();
        Iterator iterator = recipes.iterator();
        IARKRecipe irecipe;
        int i = 0;
        while (iterator.hasNext() && i < getSizeInventory()){
            irecipe = (IARKRecipe)iterator.next();
            setInventorySlotContents(i, irecipe.getRecipeOutput());
            i++;
        }	
	}
	
	// ----- For the progress indicator in the GUI: -----
	/** x position in GUI of the button pressed */
	private int xButtonPressed;
	public int getxButtonPressed() { return xButtonPressed;	}
	public void setxButtonPressed(int xButtonPressed) {	this.xButtonPressed = xButtonPressed;}
	/** y position in GUI of the button pressed */
	private int yButtonPressed;
	public int getyButtonPressed() { return yButtonPressed;	}
	public void setyButtonPressed(int yButtonPressed) {	this.yButtonPressed = yButtonPressed; }
}
