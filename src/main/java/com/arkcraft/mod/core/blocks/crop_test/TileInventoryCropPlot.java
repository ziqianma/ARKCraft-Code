package com.arkcraft.mod.core.blocks.crop_test;

import java.util.Arrays;

import com.arkcraft.mod.core.items.ARKFecesItem;
import com.arkcraft.mod.core.items.ARKSeedItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;

/***
 * 
 * @author wildbill22
 *
 */
public class TileInventoryCropPlot extends TileEntity  implements IInventory, IUpdatePlayerListBox  {
	// Create and initialize the itemStacks variable that will store store the itemStacks
	public static final int WATER_SLOTS_COUNT = 1;
	public static final int FERTILIZER_SLOTS_COUNT = 6;
	public static final int SEED_SLOTS_COUNT = 1;
	public static final int OUTPUT_SLOTS_COUNT = 1;
	public static final int TOTAL_SLOTS_COUNT = WATER_SLOTS_COUNT + FERTILIZER_SLOTS_COUNT + SEED_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

	public static final int FIRST_FERTILIZER_SLOT = 2;
	public static final int FIRST_INPUT_SLOT = FIRST_FERTILIZER_SLOT + FERTILIZER_SLOTS_COUNT;
	public static final int FIRST_OUTPUT_SLOT = WATER_SLOTS_COUNT + FIRST_FERTILIZER_SLOT + SEED_SLOTS_COUNT;

	private ItemStack[] itemStacks = new ItemStack[TOTAL_SLOTS_COUNT];

	/** The number of burn ticks remaining on the current piece of fuel */
	private int [] growTimeRemaining = new int[FERTILIZER_SLOTS_COUNT];
	/** The initial fuel value of the currently burning fuel (in ticks of burn duration) */
	private int [] growTimeInitialValue = new int[FERTILIZER_SLOTS_COUNT];

	/**The number of ticks the current item has been growing*/
	private short growTime;
	/** The number of ticks required to grow a berry */
	private static final short GROW_TIME_FOR_COMPLETION = 200;  // vanilla value is 200 = 10 seconds

	private int cachedNumberOfFertilizerSlots = -1;

	/**
	 * Returns the amount of fertilizer remaining on the currently burning item in the given fertilizer slot.
	 * @param fertilizerSlot the number of the fertilizer slot (0..5)
	 * @return fraction remaining, between 0 - 1
	 */
	public double fractionOfFertilizerRemaining(int fertilizerSlot)	{
		if (growTimeInitialValue[fertilizerSlot] <= 0 ) return 0;
		double fraction = growTimeRemaining[fertilizerSlot] / (double)growTimeInitialValue[fertilizerSlot];
		return MathHelper.clamp_double(fraction, 0.0, 1.0);
	}

	/**
	 * return the remaining grow time of the fertilizer in the given slot
	 * @param fertilizerSlot the number of the fertilizer slot (0..5)
	 * @return seconds remaining
	 */
	public int secondsOfFertilizerRemaining(int fertilizerSlot)	{
		if (growTimeRemaining[fertilizerSlot] <= 0 ) return 0;
		return growTimeRemaining[fertilizerSlot] / 20; // 20 ticks per second
	}

	/**
	 * Get the number of slots which have fertilizer burning in them.
	 * @return number of slots with burning fertilizer, 0 - FERTILIZER_SLOTS_COUNT
	 */
	public int numberOfBurningFertilizerSlots() {
		int burningCount = 0;
		for (int burnTime : growTimeRemaining) {
			if (burnTime > 0) ++burningCount;
		}
		return burningCount;
	}

	/**
	 * Returns the amount of grow time completed on the currently growing item.
	 * @return fraction remaining, between 0 - 1
	 */
	public double fractionOfGrowTimeComplete()	{
		double fraction = growTime / (double)GROW_TIME_FOR_COMPLETION;
		return MathHelper.clamp_double(fraction, 0.0, 1.0);
	}

	// This method is called every tick to update the tile entity, i.e.
	// - see if the fertilizer has run out, and if so turn the crop plot "off" and slowly uncook the current item (if any)
	// - see if any of the items have finished smelting
	// It runs both on the server and the client.
	@Override
	public void update() {
		// If there is nothing to smelt or there is no room in the output, reset cookTime and return
		if (canHarvest()) {
			int numberOfFertilizerBurning = burnFertilizer();

			// If fuel is available, keep cooking the item, otherwise start "uncooking" it at double speed
			if (numberOfFertilizerBurning > 0) {
				growTime += numberOfFertilizerBurning;
			}	else {
				growTime -= 2;
			}

			if (growTime < 0) growTime = 0;

			// If cookTime has reached maxCookTime smelt the item and reset cookTime
			if (growTime >= GROW_TIME_FOR_COMPLETION) {
				harvestBerry();
				growTime = 0;
			}
		}	else {
			growTime = 0;
		}

		// when the number of burning slots changes, we need to force the block to re-render, otherwise the change in
		//   state will not be visible.  Likewise, we need to force a lighting recalculation.
		// The block update (for renderer) is only required on client side, but the lighting is required on both, since
		//    the client needs it for rendering and the server needs it for crop growth etc
		int numberBurning = numberOfBurningFertilizerSlots();
		if (cachedNumberOfFertilizerSlots != numberBurning) {
			cachedNumberOfFertilizerSlots = numberBurning;
			if (worldObj.isRemote) {
				worldObj.markBlockForUpdate(pos);
			}
			worldObj.checkLightFor(EnumSkyBlock.BLOCK, pos);
		}
	}

	/**
	 * 	for each fertilizer slot: decreases the burn time, checks if burnTimeRemaining = 0 and tries to consume a new piece of fertilizer if one is available
	 * @return the number of fertilizer slots which are burning
	 */
	private int burnFertilizer() {
		int burningCount = 0;
		boolean inventoryChanged = false;
		// Iterate over all the fuel slots
		for (int i = 0; i < FERTILIZER_SLOTS_COUNT; i++) {
			int fertilizerSlotNumber = i + FIRST_FERTILIZER_SLOT;
			if (growTimeRemaining[i] > 0) {
				--growTimeRemaining[i];
				++burningCount;
			}
			if (growTimeRemaining[i] == 0) {
				if (itemStacks[fertilizerSlotNumber] != null && getItemGrowTime(itemStacks[fertilizerSlotNumber]) > 0) {
					// If the stack in this slot is not null and is fuel, set burnTimeRemaining & burnTimeInitialValue to the
					// item's burn time and decrease the stack size
					growTimeRemaining[i] = growTimeInitialValue[i] = getItemGrowTime(itemStacks[fertilizerSlotNumber]);
					--itemStacks[fertilizerSlotNumber].stackSize;
					++burningCount;
					inventoryChanged = true;
				// If the stack size now equals 0 set the slot contents to the items container item. This is for fuel
				// items such as lava buckets so that the bucket is not consumed. If the item dose not have
				// a container item getContainerItem returns null which sets the slot contents to null
					if (itemStacks[fertilizerSlotNumber].stackSize == 0) {
						itemStacks[fertilizerSlotNumber] = itemStacks[fertilizerSlotNumber].getItem().getContainerItem(itemStacks[fertilizerSlotNumber]);
					}
				}
			}
		}
		if (inventoryChanged) markDirty();
		return burningCount;
	}

	/**
	 * Check if the plot is harvestable and there is sufficient space in the output slots
	 * @return true if harvesting a berry is possible
	 */
	private boolean canHarvest() {return harvestBerry(false);}

	/**
	 * Smelt an input item into an output slot, if possible
	 */
	private void harvestBerry() {harvestBerry(true);}

	/**
	 * checks that there is an item to be smelted in one of the input slots and that there is room for the result in the output slots
	 * If desired, performs the smelt
	 * @param harvestBerry if true, harvest a berry.  if false, check whether harvesting is possible, but don't change the inventory
	 * @return false if no berry can be harvested, true otherwise
	 */
	private boolean harvestBerry(boolean harvestBerry)
	{
		Integer firstSuitableInputSlot = null;
		Integer firstSuitableOutputSlot = null;
		ItemStack result = null;

		// finds the first input slot which is smeltable and whose result fits into an output slot (stacking if possible)
		for (int inputSlot = FIRST_INPUT_SLOT; inputSlot < FIRST_INPUT_SLOT + WATER_SLOTS_COUNT; inputSlot++)	{
			if (itemStacks[inputSlot] != null) {
				result = getGrowingResultForItem(itemStacks[inputSlot]);
  			if (result != null) {
					// find the first suitable output slot- either empty, or with identical item that has enough space
					for (int outputSlot = FIRST_OUTPUT_SLOT; outputSlot < FIRST_OUTPUT_SLOT + OUTPUT_SLOTS_COUNT; outputSlot++) {
						ItemStack outputStack = itemStacks[outputSlot];
						if (outputStack == null) {
							firstSuitableInputSlot = inputSlot;
							firstSuitableOutputSlot = outputSlot;
							break;
						}

						if (outputStack.getItem() == result.getItem() && (!outputStack.getHasSubtypes() || outputStack.getMetadata() == outputStack.getMetadata())
										&& ItemStack.areItemStackTagsEqual(outputStack, result)) {
							int combinedSize = itemStacks[outputSlot].stackSize + result.stackSize;
							if (combinedSize <= getInventoryStackLimit() && combinedSize <= itemStacks[outputSlot].getMaxStackSize()) {
								firstSuitableInputSlot = inputSlot;
								firstSuitableOutputSlot = outputSlot;
								break;
							}
						}
					}
					if (firstSuitableInputSlot != null) break;
				}
			}
		}

		if (firstSuitableInputSlot == null) return false;
		if (!harvestBerry) return true;

		// alter input and output
		itemStacks[firstSuitableInputSlot].stackSize--;
		if (itemStacks[firstSuitableInputSlot].stackSize <=0) itemStacks[firstSuitableInputSlot] = null;
		if (itemStacks[firstSuitableOutputSlot] == null) {
			itemStacks[firstSuitableOutputSlot] = result.copy(); // Use deep .copy() to avoid altering the recipe
		} else {
			itemStacks[firstSuitableOutputSlot].stackSize += result.stackSize;
		}
		markDirty();
		return true;
	}

	// returns the smelting result for the given stack. Returns null if the given stack can not be smelted
	public static ItemStack getGrowingResultForItem(ItemStack stack) { return ARKSeedItem.getBerryForSeed(stack); }

	// returns the number of ticks the given item will burn. Returns 0 if the given item is not a valid fuel
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

	// Returns double between 0 and 1 representing % full level
	public double waterLevel() {
		// TODO Auto-generated method stub
		return 0.5;
	}

	// Return true if stack is a valid fertilizer for the crop plot
	public boolean isItemValidForFertilizerSlot(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ARKFecesItem)
			return true;
		else
			return false;
	}

	// Return true if stack is a water for the crop plot
	public boolean isItemValidForWaterSlot(ItemStack stack) {
		if (stack != null && stack.getItem() == Items.water_bucket)
			return true;
		else
			return false;
	}

	// Return true if stack is a valid seed for the crop plot
	public boolean isItemValidForSeedSlot(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ARKSeedItem)
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

//		// Save the stored item stacks

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
		parentNBTTagCompound.setShort("CookTime", growTime);
		parentNBTTagCompound.setTag("burnTimeRemaining", new NBTTagIntArray(growTimeRemaining));
		parentNBTTagCompound.setTag("burnTimeInitial", new NBTTagIntArray(growTimeInitialValue));
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
		growTime = nbtTagCompound.getShort("CookTime");
		growTimeRemaining = Arrays.copyOf(nbtTagCompound.getIntArray("burnTimeRemaining"), FERTILIZER_SLOTS_COUNT);
		growTimeInitialValue = Arrays.copyOf(nbtTagCompound.getIntArray("burnTimeInitial"), FERTILIZER_SLOTS_COUNT);
		cachedNumberOfFertilizerSlots = -1;
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

	private static final byte GROW_FIELD_ID = 0;
	private static final byte FIRST_GROW_TIME_REMAINING_FIELD_ID = 1;
	private static final byte FIRST_GROW_TIME_INITIAL_FIELD_ID = FIRST_GROW_TIME_REMAINING_FIELD_ID + (byte)FERTILIZER_SLOTS_COUNT;
	private static final byte NUMBER_OF_FIELDS = FIRST_GROW_TIME_INITIAL_FIELD_ID + (byte)FERTILIZER_SLOTS_COUNT;

	@Override
	public int getField(int id) {
		if (id == GROW_FIELD_ID) return growTime;
		if (id >= FIRST_GROW_TIME_REMAINING_FIELD_ID && id < FIRST_GROW_TIME_REMAINING_FIELD_ID + FERTILIZER_SLOTS_COUNT) {
			return growTimeRemaining[id - FIRST_GROW_TIME_REMAINING_FIELD_ID];
		}
		if (id >= FIRST_GROW_TIME_INITIAL_FIELD_ID && id < FIRST_GROW_TIME_INITIAL_FIELD_ID + FERTILIZER_SLOTS_COUNT) {
			return growTimeInitialValue[id - FIRST_GROW_TIME_INITIAL_FIELD_ID];
		}
		System.err.println("Invalid field ID in TileInventoryCropPlot.getField:" + id);
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
		if (id == GROW_FIELD_ID) {
			growTime = (short)value;
		} else if (id >= FIRST_GROW_TIME_REMAINING_FIELD_ID && id < FIRST_GROW_TIME_REMAINING_FIELD_ID + FERTILIZER_SLOTS_COUNT) {
			growTimeRemaining[id - FIRST_GROW_TIME_REMAINING_FIELD_ID] = value;
		} else if (id >= FIRST_GROW_TIME_INITIAL_FIELD_ID && id < FIRST_GROW_TIME_INITIAL_FIELD_ID + FERTILIZER_SLOTS_COUNT) {
			growTimeInitialValue[id - FIRST_GROW_TIME_INITIAL_FIELD_ID] = value;
		} else {
			System.err.println("Invalid field ID in TileInventoryCropPlot.setField:" + id);
		}
	}

	@Override
	public int getFieldCount() {
		return NUMBER_OF_FIELDS;
	}
}
