package com.arkcraft.mod.client.gui;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.player.ARKPlayer;
import com.arkcraft.mod.common.handlers.PlayerCraftingManager;
import com.arkcraft.mod.common.lib.BALANCE;
import com.arkcraft.mod.common.lib.LogHelper;
import com.arkcraft.mod.common.network.UpdatePlayerCrafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/***
 * 
 * @author wildbill22
 *
 */
public class InventoryBlueprints extends InventoryBasic {
	
	public InventoryBlueprints(String title, boolean customName, int slotCount) {
		super(title, customName, slotCount);
	}

    public void loadInventoryFromNBT(NBTTagCompound nbt)  {
		final byte NBT_TYPE_COMPOUND = 10;  
		NBTTagList dataForAllBlueprints = nbt.getTagList("Blueprints", NBT_TYPE_COMPOUND);
		loadInventoryFromNBT(dataForAllBlueprints);
    }
    
    public void loadInventoryFromNBT(NBTTagList nbt)  {
        int i;
        for (i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, (ItemStack)null);
        }
        for (i = 0; i < nbt.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.getSizeInventory()) {
                this.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
            }
        }
    }

    public void saveInventoryToNBT(NBTTagCompound nbt) {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemstack = this.getStackInSlot(i);
            if (itemstack != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
                LogHelper.info("InventoryBlueprints: Saved a " + itemstack.getItem() + " to inventory.");
            }
        }
		nbt.setTag("Blueprints", nbttaglist);
    }

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
//	private boolean craftAll = false;
	private boolean craftOne = false;
	private short blueprintPressed = 0; // Button pressed
	
	private void sendUpdateToServer(){
		ARKCraft.modChannel.sendToServer(new UpdatePlayerCrafting(craftOne, blueprintPressed));
	}
	
	public void setCraftOnePressed(int i, boolean craftOnePressed, boolean andUpdateServer) {
		this.craftOne = craftOnePressed;
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

    @SideOnly(Side.CLIENT)
	public int getNumToBeCrafted() {
		if (tick == 20){
			canCraft();
		}
		return numThatCanBeCrafted;
	}

	/** The number of seconds required to craft an item */
	private static final short CRAFT_TIME_FOR_ITEM = (short) BALANCE.MORTAR_AND_PESTLE.CRAFT_TIME_FOR_ITEM;

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

	int tick = 20;
	
	// This method is called every tick to update the tile entity, i.e.
	// It runs both on the server and the client.
	public void update() {
		if (tick >= 0){
			tick--;
			return;			
		} else {
			tick = 20;
		}
		
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
			}
		}
		else
			craftingTime--;
		
		// If craftingTime has reached -1, try and craft the item
		if (craftingTime < 0) {
			LogHelper.info("TileInventorySmith: About to craft the item on " + (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? "client" : "server"));
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
		
//		LogHelper.info("TileInventorySmithy: Update called on " + (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? "client" : "server"));
//		LogHelper.info("TileInventorySmithy: craftAll = " + (craftAll == true ? "true" : "false"));

		// find the first suitable output slot, 1st check for identical item that has enough space
		for (int outputSlot = ARKPlayer.LAST_INVENTORY_SLOT; outputSlot > ARKPlayer.FIRST_INVENTORY_SLOT; outputSlot--) {
			ItemStack outputStack = getStackInSlot(outputSlot);
			if (outputStack != null && outputStack.getItem() == result.getItem() && 
					(!outputStack.getHasSubtypes() || outputStack.getMetadata() == outputStack.getMetadata())
					&& ItemStack.areItemStackTagsEqual(outputStack, result)) {
				int combinedSize = getStackInSlot(outputSlot).stackSize + result.stackSize;
				if (combinedSize <= getInventoryStackLimit() && combinedSize <= getStackInSlot(outputSlot).getMaxStackSize()) {
					firstSuitableOutputSlot = outputSlot;
					break;
				}
			}
		}
		if (firstSuitableOutputSlot == null) {
			// 2nd look for for empty slot if no partially filled slots are found
			for (int outputSlot = ARKPlayer.LAST_INVENTORY_SLOT; outputSlot > ARKPlayer.FIRST_INVENTORY_SLOT; outputSlot--) {
				ItemStack outputStack = getStackInSlot(outputSlot);
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
		if (!doCraftItem) {
//			numThatCanBeCrafted = (short) SmithyCraftingManager.getInstance().hasMatchingRecipe(result, itemStacks, false);
			numThatCanBeCrafted = (short) PlayerCraftingManager.getInstance().hasMatchingRecipe(result, this.getItemStacks(), false);
			if (numThatCanBeCrafted <= 0) {
				LogHelper.info("TileInventorySmithy: Can't craft item from inventory.");
				return false;			
			}
			return true;
		}

		// Craft an item
		int numCrafted = (short) PlayerCraftingManager.getInstance().hasMatchingRecipe(result, this.getItemStacks(), true);
		
		if (numCrafted <= 0)
			return false;

		// alter output slot
		LogHelper.info("TileInventorySmithy: Update called on " + (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? "client" : "server"));
		LogHelper.info("TileInventorySmithy: Copy craft result to slot: " + firstSuitableOutputSlot);
		if (getStackInSlot(firstSuitableOutputSlot) == null) {
			this.setInventorySlotContents(firstSuitableOutputSlot, result.copy()); // Use deep .copy() to avoid altering the recipe
		} else {
			getStackInSlot(firstSuitableOutputSlot).stackSize += result.stackSize;
		}
		markDirty();
		return true;
	}

	private ItemStack[] getItemStacks() {
		ItemStack[] blueprintStacks = new ItemStack[getSizeInventory()];
		for (int i = 0; i < getSizeInventory(); i++)
            blueprintStacks[i] = getStackInSlot(i);
		return blueprintStacks;
	}

//	public int getBlueprintSelected() {
//	// TODO Auto-generated method stub
//	return 0;
//}

//	public void setCraftAllPressed(boolean b, boolean c) {
//		// TODO Auto-generated method stub
//		
//	}

//	public boolean isCraftingAll() {
//	// TODO Auto-generated method stub
//	return false;
//}

//	public String getNumToBeCrafted() {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
