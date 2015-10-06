package com.arkcraft.mod.core.blocks;

import java.util.Arrays;

import com.arkcraft.mod.core.items.ARKFecesItem;
import com.arkcraft.mod.core.items.ARKSeedItem;
import com.arkcraft.mod.core.lib.LogHelper;

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

/***
 * 
 * @author wildbill22
 *
 */
public class TileInventoryMP extends TileEntity implements IInventory, IUpdatePlayerListBox {
	public static final int BLUEPRINT_SLOTS_COUNT = 1;
	public static final int INVENTORY_SLOTS_COUNT = 6;
	public static final int TOTAL_SLOTS_COUNT = BLUEPRINT_SLOTS_COUNT + INVENTORY_SLOTS_COUNT;

	public static final int BLUEPRINT_SLOT = 0;
	public static final int FIRST_INVENTORY_SLOT = 2;

	// Create and initialize the itemStacks variable that will store store the itemStacks
	private ItemStack[] itemStacks = new ItemStack[TOTAL_SLOTS_COUNT];
	
	// Other class variables
	int tick = 20;
	
	/** The number of ticks the current item has been crafting */
	private short craftingTime;
	/** The number of ticks required to grow a berry */
	private static final short CRAFT_TIME_FOR_ITEM = 10;  // vanilla value is 45 seconds

	// Maybe change to seconds?
	// Returns double between 0 and 1 representing % done
	public double fractionCraftingRemaining() {
		double fraction = craftingTime / (double)CRAFT_TIME_FOR_ITEM;
		return MathHelper.clamp_double(fraction, 0.0, 1.0);
	}

	// This method is called every tick to update the tile entity, i.e.
	// - see if the fertilizer has run out, and if so turn the crop plot "off" and slowly un-grow the current item (if any)
	// - see if any of the items have finished growing and ready to harvest
	// It runs both on the server and the client.
	@Override
	public void update() {
		if (tick >= 0){
			tick--;
			return;			
		} else {
			tick = 20;
		}
		
		// If there is no seed or water, or there is no room in the output, reset growTime and return
		if (canCraft()) {
			if (craftingTime < 0) craftingTime = 0;
			
			// If growTime has reached maximum, harvest a berry and reset growTime
			if (craftingTime >= CRAFT_TIME_FOR_ITEM) {
				craftItem();
				craftingTime = 0;
			}
		}
	}

	/**
	 * Check if the plot is harvestable and there is sufficient space in the output slots
	 * @return true if harvesting a berry is possible
	 */
	private boolean canCraft() {return craftItem(false);}

	/**
	 * Craft an item, if possible
	 */
	private void craftItem() {craftItem(true);}

	/**
	 * checks that there is an item to be harvested in one of the input slots and that there is room for the result in the output slots
	 * If desired, performs the berry harvest
	 * @param harvestBerry  If true, harvest a berry. If false, check whether harvesting is possible, but don't change the inventory
	 * @return false if no berry can be harvested, true otherwise
	 */
	private boolean craftItem(boolean craftItem){
		Integer firstSuitableOutputSlot = null;
		ItemStack result = null;
		boolean canCraftItem = true;

		if (craftingTime <= 0) {
			craftingTime = 0;
			canCraftItem = false;				 
		}
		
		// If true, we craft item
		if (!craftItem) return true;

		// alter output slot
		if (itemStacks[firstSuitableOutputSlot] == null) {
			itemStacks[firstSuitableOutputSlot] = result.copy(); // Use deep .copy() to avoid altering the recipe
		} else {
			itemStacks[firstSuitableOutputSlot].stackSize += result.stackSize;
		}
		markDirty();
		return true;
	}

	// returns the growth result for the given stack. Returns null if the given stack can not be grown
	public static ItemStack getGrowingResultForItem(ItemStack stack) { return ARKSeedItem.getBerryForSeed(stack); }

	// returns the number of ticks the given item will grow. Returns 0 if the given item is not a valid fertilizer
	public static short getItemGrowTime(ItemStack stack) {
		int growtime = ARKFecesItem.getItemGrowTime(stack);
		return (short)MathHelper.clamp_int(growtime, 0, Short.MAX_VALUE);
	}

	@Override
	public String getName() {
		return "container.crop_plot.name";
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
		// TODO Auto-generated method stub
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
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		Arrays.fill(itemStacks, null);
	}
	
	// Return true if stack is a valid fertilizer for the crop plot
	public boolean isItemValidForRecipeSlot(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ARKFecesItem)
			return true;
		else
			return false;
	}

	//------------------------------

	// This is where you save any data that you don't want to lose when the tile entity unloads
	// In this case, it saves the state of the furnace (burn time etc) and the itemstacks stored in the fuel, input, and output slots
	@Override
	public void writeToNBT(NBTTagCompound parentNBTTagCompound)
	{
		super.writeToNBT(parentNBTTagCompound); // The super call is required to save and load the tiles location

		// Save the stored item stacks

		// to use an analogy with Java, this code generates an array of hashmaps
		// The itemStack in each slot is converted to an NBTTagCompound, which is effectively a hashmap of key->value pairs such
		//   as slot=1, id=2353, count=1, etc
		// Each of these NBTTagCompound are then inserted into NBTTagList, which is similar to an array.
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
		parentNBTTagCompound.setShort("growTime", craftingTime);
		LogHelper.info("TileInventoryCropPlot: Wrote inventory.");
	}

	// This is where you load the data that you saved in writeToNBT
	@Override
	public void readFromNBT(NBTTagCompound nbtTagCompound)
	{
		super.readFromNBT(nbtTagCompound); // The super call is required to save and load the tiles location
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
		craftingTime = nbtTagCompound.getShort("growTime");
		LogHelper.info("TileInventoryCropPlot: Read inventory.");
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

	private static final byte CRAFT_FIELD_ID = 0;
	private static final byte NUMBER_OF_FIELDS = 1;

	@Override
	public int getField(int id) {
		if (id == CRAFT_FIELD_ID) return craftingTime;
		System.err.println("Invalid field ID in TileInventoryCropPlot.getField:" + id);
		return 0;
	}

	@Override
	public void setField(int id, int value)	{
		if (id == CRAFT_FIELD_ID) {
			craftingTime = (short)value;
		} else {
			System.err.println("Invalid field ID in TileInventoryCropPlot.setField:" + id);
		}
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

	public ItemStack getStack(int i) {
		return itemStacks[i];
	}
}
