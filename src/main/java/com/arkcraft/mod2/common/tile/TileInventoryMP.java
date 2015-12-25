package com.arkcraft.mod2.common.tile;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod2.common.config.MOD2_BALANCE;
import com.arkcraft.mod2.common.container.inventory.InventoryBlueprints;
import com.arkcraft.mod2.common.handlers.IARKRecipe;
import com.arkcraft.mod2.common.handlers.PestleCraftingManager;
import com.arkcraft.mod2.common.network.UpdateMPToCraftItem;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/***
 * 
 * @author wildbill22
 *
 */
public class TileInventoryMP extends TileEntity implements IInventory, IUpdatePlayerListBox {
	// class variables
	int tick = 20;
	
	// Constants for the inventory
	public static final int INVENTORY_SLOTS_COUNT = 9;
	public static final int TOTAL_SLOTS_COUNT = INVENTORY_SLOTS_COUNT;
	public static final int FIRST_INVENTORY_SLOT = 0;
	public static final int LAST_INVENTORY_SLOT = INVENTORY_SLOTS_COUNT - 1; 
	public static final int BLUEPRINT_SLOTS_COUNT = 1;
	public static final int BLUEPRINT_SLOT = 0;
	
	// Variables to sync from client to server
	private boolean craftAll = false;
	private boolean craftOne = false;
	/** The currently displayed blueprint */
	private short blueprintSelected = 0;
	private boolean guiOpen = false;
	
	private void sendUpdateToServer(){
		ARKCraft.modChannel.sendToServer(new UpdateMPToCraftItem(blueprintSelected, craftOne, craftAll, guiOpen, this.pos));
	}
	
	public void setGuiOpen(boolean guiOpen, boolean andUpdateServer) {
		this.guiOpen = guiOpen;
		if (andUpdateServer)
			sendUpdateToServer();
	}

	/** Set true to craft items */
    @SideOnly(Side.CLIENT)
	public void setCraftAllPressed(boolean craftAllPressed, boolean andUpdateServer){
		this.craftAll = craftAllPressed;
		if (andUpdateServer)
			sendUpdateToServer();
	}
		
	/** Set true to craft items */
    @SideOnly(Side.CLIENT)
	public void setCraftOnePressed(boolean craftOnePressed, boolean andUpdateServer){
		this.craftOne = craftOnePressed;
		if (andUpdateServer)
			sendUpdateToServer();
	}
	
	public boolean isCrafting(){
		return craftOne || craftAll;
	}
	
	public boolean isCraftingOne(){
		return craftOne;
	}
	
	public boolean isCraftingAll(){
		return craftAll;
	}
	
	/** itemStacks variable that will store the blueprints */
	private ItemStack[] blueprintStacks;
	
	/** The number of blueprints (number of recipes defined) */
	private int numBlueprints;
	/** Get blueprint selected (to be displayed) */
	public int getNumBlueprints(){
		return numBlueprints;
	}

	/** The blueprint in the blueprint slots that is selected */
//	private short blueprintSlotSelected = 0; // Currently just one slot
	
	public InventoryBlueprints inventoryBlueprints = new InventoryBlueprints("Blueprints", false, BLUEPRINT_SLOTS_COUNT);
	
	/** Get blueprint selected (to be displayed) */
	public int getBlueprintSelected(){
		return blueprintSelected;
	}
	public void setBlueprintSelected(int blueprintSelected) {
		this.blueprintSelected = (short) blueprintSelected;
        this.inventoryBlueprints.setInventorySlotContents(0, blueprintStacks[blueprintSelected]);        
	}
	/** Select next blueprint */
    @SideOnly(Side.CLIENT)
	public void selectNextBlueprint(){
		blueprintSelected++;
		if (blueprintSelected >= numBlueprints)
			blueprintSelected = (short) (numBlueprints - 1);
        this.inventoryBlueprints.setInventorySlotContents(0, blueprintStacks[blueprintSelected]);        
		sendUpdateToServer();
	}
	/** Select next blueprint */
    @SideOnly(Side.CLIENT)
	public void selectPrevBlueprint(){
		blueprintSelected--;
		if (blueprintSelected <= 0)
			blueprintSelected = 0;
        this.inventoryBlueprints.setInventorySlotContents(0, blueprintStacks[blueprintSelected]);        
		sendUpdateToServer();
	}
	
	// Create and initialize the itemStacks variable that will store the itemStacks
	private ItemStack[] itemStacks = new ItemStack[TOTAL_SLOTS_COUNT];
	
	@SuppressWarnings("rawtypes")
	public TileInventoryMP(){
//		LogHelper.info("TileInventoryMP: constructor called.");
		numBlueprints = PestleCraftingManager.getInstance().getNumRecipes();
		blueprintStacks = new ItemStack[numBlueprints];
		List recipes = PestleCraftingManager.getInstance().getRecipeList();
        Iterator iterator = recipes.iterator();
        IARKRecipe irecipe;
        int i = 0;
        while (iterator.hasNext()){
            irecipe = (IARKRecipe)iterator.next();
            blueprintStacks[i] = irecipe.getRecipeOutput();
            i++;
        }
        setBlueprintSelected(0);
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
	private static final short CRAFT_TIME_FOR_ITEM = (short) MOD2_BALANCE.MORTAR_AND_PESTLE.CRAFT_TIME_FOR_ITEM;

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
	
	// Returns double between 0 and 1 representing % done
	public double fractionCraftingRemainingForItem() {
		if (craftingTime < 0)
			return 0.0D;
		double fraction = craftingTime / (double)CRAFT_TIME_FOR_ITEM;
		return MathHelper.clamp_double(fraction, 0.0, 1.0);
	}

	// This method is called every tick to update the tile entity, i.e.
	// It runs both on the server and the client.
	@Override
	public void update() {
		if (tick >= 0){
			tick--;
			return;			
		} else {
			tick = 20;
		}
		
		// If not crafting an item, return
		if (!craftAll && !craftOne)
			return;
		// Reset crafting time if it reaches -1 (is true after crafting one of multiple, or after pushing button in GUI)
		if (craftingTime < 0){
			craftingTime = CRAFT_TIME_FOR_ITEM;
			if (this.guiOpen){
				// See if an item can be crafted
				if (!craftItem(false)){
					craftAll = false;
					craftOne = false;
					craftingTime = -1;
				}
			}
		}
		else
			craftingTime--;
		
		// If craftingTime has reached -1, try and craft the item
		if (craftingTime < 0) {
//			LogHelper.info("TileInventoryMP: About to craft the item on " + (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? "client" : "server"));
			if (!craftItem())
				craftAll = false;
			if (craftOne)
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
		ItemStack result = blueprintStacks[blueprintSelected];

		// No recipes?
		if (result == null)
			return false;
		
//		LogHelper.info("TileInventoryMP: Update called on " + (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? "client" : "server"));
//		LogHelper.info("TileInventoryMP: craftAll = " + (craftAll == true ? "true" : "false"));

		// find the first suitable output slot, 1st check for identical item that has enough space
		for (int outputSlot = LAST_INVENTORY_SLOT; outputSlot > FIRST_INVENTORY_SLOT; outputSlot--) {
			ItemStack outputStack = itemStacks[outputSlot];
			if (outputStack != null && outputStack.getItem() == result.getItem() && 
					(!outputStack.getHasSubtypes() || outputStack.getMetadata() == outputStack.getMetadata())
					&& ItemStack.areItemStackTagsEqual(outputStack, result)) {
				int combinedSize = itemStacks[outputSlot].stackSize + result.stackSize;
				if (combinedSize <= getInventoryStackLimit() && combinedSize <= itemStacks[outputSlot].getMaxStackSize()) {
					firstSuitableOutputSlot = outputSlot;
					break;
				}
			}
		}
		if (firstSuitableOutputSlot == null) {
			// 2nd look for for empty slot if no partially filled slots are found
			for (int outputSlot = LAST_INVENTORY_SLOT; outputSlot > FIRST_INVENTORY_SLOT; outputSlot--) {
				ItemStack outputStack = itemStacks[outputSlot];
				if (outputStack == null) {
					firstSuitableOutputSlot = outputSlot;
					break;
				}
			}
		}
		if (firstSuitableOutputSlot == null){
			LogHelper.info("TileInventoryMP: No output slots available.");
			return false;
		}

		// finds if there is enough inventory to craft the result
		if (!doCraftItem) {
			numThatCanBeCrafted = (short) PestleCraftingManager.getInstance().hasMatchingRecipe(result, itemStacks, false);
			if (numThatCanBeCrafted <= 0) {
				if (this.guiOpen)
					LogHelper.info("TileInvetoryMP: Can't craft item from inventory.");
				return false;			
			}
			return true;
		}

		// Craft an item
		int numCrafted = (short) PestleCraftingManager.getInstance().hasMatchingRecipe(result, itemStacks, true);
		
		if (numCrafted <= 0)
			return false;

		// alter output slot
		if (itemStacks[firstSuitableOutputSlot] == null) {
			itemStacks[firstSuitableOutputSlot] = result.copy(); // Use deep .copy() to avoid altering the recipe
		} else {
			itemStacks[firstSuitableOutputSlot].stackSize += result.stackSize;
		}
		markDirty();
		return true;
	}

	@Override
	public String getName() {
		return "container.mortar_and_pestle.name";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
	}

	@Override
	public int getSizeInventory() {
		return itemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return itemStacks[index];
	}

	@Override
	public ItemStack decrStackSize(int slotIndex, int count) {
		ItemStack itemStackInSlot = getStackInSlot(slotIndex);
		if (itemStackInSlot == null) return null;

		ItemStack itemStackRemoved;
		if (itemStackInSlot.stackSize <= count) {
			itemStackRemoved = itemStackInSlot;
			setInventorySlotContents(slotIndex, null);
		} else {
			itemStackRemoved = itemStackInSlot.splitStack(count);
			if (itemStackInSlot.stackSize == 0) {
				setInventorySlotContents(slotIndex, null);
			}
		}
		markDirty();
		return itemStackRemoved;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		if (itemStacks[index] != null){
			ItemStack itemstack = itemStacks[index];
			itemStacks[index] = null;
			return itemstack;
		}
		return null;
	}

	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack itemstack) {
		itemStacks[slotIndex] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		if (this.worldObj.getTileEntity(this.pos) != this) return false;
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public void clear() {
		Arrays.fill(itemStacks, null);
	}
	
	// Return true if stack is a valid fertilizer for the crop plot
	public boolean isItemValidForRecipeSlot(ItemStack stack) {
		return PestleCraftingManager.getInstance().isItemInRecipe(stack);
	}

	//------------------------------

	// This is where you save any data that you don't want to lose when the tile entity unloads
	// In this case, it saves the state of the mortar and pestle and the itemstacks stored in the inventory
	@Override
	public void writeToNBT(NBTTagCompound parentNBTTagCompound){
		super.writeToNBT(parentNBTTagCompound); // The super call is required to save and load the tiles location

		// Save the stored item stacks

		// to use an analogy with Java, this code generates an array of hashmaps
		// The itemStack in each slot is converted to an NBTTagCompound, which is effectively a hashmap of key->value pairs such
		//   as slot=1, id=2353, count=1, etc
		// Each of these NBTTagCompound are then inserted into NBTTagList, which is similar to an array.		
		// Recipe items inventory
		NBTTagList dataForAllSlots = new NBTTagList();
		for (int i = 0; i < this.itemStacks.length; ++i) {
			if (this.itemStacks[i] != null) {
				NBTTagCompound dataForThisSlot = new NBTTagCompound();
				dataForThisSlot.setByte("Slot", (byte) i);
				this.itemStacks[i].writeToNBT(dataForThisSlot);
				dataForAllSlots.appendTag(dataForThisSlot);
			}
		}
		// the array of hashmaps is then inserted into the parent hashmap for the container
		parentNBTTagCompound.setTag("Items", dataForAllSlots);

		// Save everything else
		parentNBTTagCompound.setShort("blueprintSelected", blueprintSelected);
		parentNBTTagCompound.setBoolean("craftAll", craftAll);
//		LogHelper.info("TileInventoryMP: Wrote inventory.");		
	}

	// This is where you load the data that you saved in writeToNBT
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound){
		super.readFromNBT(nbtTagCompound); // The super call is required to save and load the tiles location

		// recipe items inventory
		final byte NBT_TYPE_COMPOUND = 10;       // See NBTBase.createNewByType() for a listing
		NBTTagList dataForAllSlots = nbtTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);

		Arrays.fill(itemStacks, null);           // set all slots to empty
		for (int i = 0; i < dataForAllSlots.tagCount(); ++i) {
			NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
			byte slotNumber = dataForOneSlot.getByte("Slot");
			if (slotNumber >= 0 && slotNumber < this.itemStacks.length) {
				this.itemStacks[slotNumber] = ItemStack.loadItemStackFromNBT(dataForOneSlot);
			}
		}

		// Load everything else.  Trim the arrays (or pad with 0) to make sure they have the correct number of elements
		blueprintSelected = nbtTagCompound.getShort("blueprintSelected");
		craftAll = nbtTagCompound.getBoolean("craftAll");
//		LogHelper.info("TileInventoryMP: Read inventory.");
	}

	// When the world loads from disk, the server needs to send the TileEntity information to the client
	//  it uses getDescriptionPacket() and onDataPacket() to do this
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		final int METADATA = 0;
		return new S35PacketUpdateTileEntity(this.pos, METADATA, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	// Fields are used to send non-inventory information from the server to interested clients
	// The container code caches the fields and sends the client any fields which have changed.
	// The field ID is limited to byte, and the field value is limited to short. (if you use more than this, they get cast down
	//   in the network packets)
	// If you need more than this, or shorts are too small, use a custom packet in your container instead.

//	private static final byte BLUEPRINT_SEL_FIELD_ID = 0;
	private static final byte NUMBER_OF_FIELDS = 0;

	@Override
	public int getField(int id) {
//		if (id == BLUEPRINT_SEL_FIELD_ID) return blueprintSelected;
		System.err.println("Invalid field ID in TileInventoryMP.getField:" + id);
		return 0;
	}

	@Override
	public void setField(int id, int value)	{
//		if (id == BLUEPRINT_SEL_FIELD_ID) {
//			blueprintSelected = (short)value;
//		} 
//		else {
			System.err.println("Invalid field ID in TileInventoryMP.setField:" + id);
//		}
	}

	@Override
	public int getFieldCount() {
		return NUMBER_OF_FIELDS;
	}
	
    /**
     * Called from Chunk.setBlockIDWithMetadata, determines if this tile entity should be re-created when the ID, or Metadata changes.
     * Use with caution as this will leave straggler TileEntities, or create conflicts with other TileEntities if not used properly.
     *
     * @param world Current world
     * @param pos Tile's world position
     * @param oldID The old ID of the block
     * @param newID The new ID of the block (May be the same)
     * @return True to remove the old tile entity, false to keep it in tact {and create a new one if the new values specify to}
     */
	@Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate){
        return false;
    }
}
