package com.arkcraft.mod.core.machine.gui;

import java.util.Arrays;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.entity.DinoTameable;
import com.arkcraft.mod.core.items.ARKFecesItem;
import com.arkcraft.mod.core.items.ARKFood;
import com.arkcraft.mod.core.items.ARKSeedItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;

/***
 * 
 * @author wildbill22
 *
 */
public class InventoryTaming implements IInventory, IUpdatePlayerListBox {
	DinoTameable entityDino;
	public EntityPlayer playerTaming;  // The player who starts the taming process (with the GUI)
	
	public InventoryTaming(DinoTameable entityDino) {
		this.entityDino = entityDino;
		TAMING_TIME_FOR_COMPLETION = (short) (entityDino.getTamingSeconds() * 20);
	}

	// Create and initialize the itemStacks variable that will store store the itemStacks
	public static final int NARCO_SLOTS_COUNT = 1;
	public static final int FOOD_SLOTS_COUNT = 1;
	public static final int TOTAL_SLOTS_COUNT = NARCO_SLOTS_COUNT + FOOD_SLOTS_COUNT;

	public static final int NARCO_SLOT = 0;
	public static final int FOOD_SLOT = 1;
	public static final int FIRST_INPUT_SLOT = NARCO_SLOT;

	private ItemStack[] itemStacks = new ItemStack[TOTAL_SLOTS_COUNT];

	/** The number of feeding ticks remaining on the current piece of food */
	private int [] feedingTimeRemaining = new int[FOOD_SLOTS_COUNT];
	/** The initial food value of the currently feeding food (in ticks of feeding duration) */
	private int [] feedingTimeInitialValue = new int[FOOD_SLOTS_COUNT];

	/** The number of ticks the dino has been taming */
	private short tamingTime;
	/** The number of ticks required to tame the dino */
	private static short TAMING_TIME_FOR_COMPLETION = 1000;  // vanilla value is 1000 = 50 seconds

	/**
	 * Returns the amount of food remaining on the currently feeding item in the given food slot.
	 * @param foodSlot the number of the food slot (1)
	 * @return fraction remaining, between 0 - 1
	 */
	public double fractionOfFoodRemaining(int foodSlot)	{
		if (feedingTimeInitialValue[foodSlot] <= 0 ) return 0;
		double fraction = feedingTimeRemaining[foodSlot] / (double)feedingTimeInitialValue[foodSlot];
		return MathHelper.clamp_double(fraction, 0.0, 1.0);
	}

	/**
	 * return the remaining feed time of the food in the given slot
	 * @param foodSlot the number of the food slot (1)
	 * @return seconds remaining
	 */
	public int secondsOfFoodRemaining(int foodSlot)	{
		if (feedingTimeRemaining[foodSlot] <= 0 ) return 0;
		return feedingTimeRemaining[foodSlot] / 20; // 20 ticks per second
	}

	/**
	 * Get the number of slots which have food in them.
	 * @return number of slots with food, 0 - FOOD_SLOTS_COUNT
	 */
	public int numberOfFoodInSlots() {
		int foodCount = 0;
		for (int feedingTime : feedingTimeRemaining) {
			if (feedingTime > 0) 
				++foodCount;
		}
		return foodCount;
	}

	/**
	 * Returns the amount of taming time completed.
	 * @return fraction remaining, between 0 - 1
	 */
	public double fractionOfTamingTimeComplete()	{
		double fraction = tamingTime / (double)TAMING_TIME_FOR_COMPLETION;
		return MathHelper.clamp_double(fraction, 0.0, 1.0);
	}

	// This method is called every tick to update the tile entity, i.e.
	// - see if the fertilizer has run out, and if so turn the crop plot "off" and slowly uncook the current item (if any)
	// - see if any of the items have finished smelting
	// It runs both on the server and the client.
	@Override
	public void update() {
		// If there is nothing to feed or not unconscious, reset feedTime and return
		if (canTame()) {
			int numberOfFoodFeeding = feedDino();

			// If food is available, keep taming, otherwise start "untaming" it at double speed
			if (numberOfFoodFeeding > 0) {
				tamingTime += numberOfFoodFeeding;
			}	else {
				tamingTime -= 2;
			}

			if (tamingTime < 0) tamingTime = 0;

			// If tamingTime has reached maxTamingTime the dino is tamed
			if (tamingTime >= TAMING_TIME_FOR_COMPLETION) {
				setToTamed();
				tamingTime = 0;
			}
		}	else {
			tamingTime = 0;
		}
	}

	/**
	 * For each food slot: decreases the feed time, checks if feedTimeRemaining = 0 and tries to consume a new piece of food if one is available
	 * @return the number of food slots which are being eaten
	 */
	private int feedDino() {
		int feedingCount = 0;
		boolean inventoryChanged = false;
		// Iterate over all the food slots
		for (int i = 0; i < FOOD_SLOTS_COUNT; i++) {
			int foodSlotNumber = i;
			if (feedingTimeRemaining[i] > 0) {
				--feedingTimeRemaining[i];
				++feedingCount;
			}
			if (feedingTimeRemaining[i] == 0) {
				if (itemStacks[foodSlotNumber] != null && getItemFeedTime(itemStacks[foodSlotNumber]) > 0) {
					// If the stack in this slot is not null and is fuel, set burnTimeRemaining & burnTimeInitialValue to the
					// item's burn time and decrease the stack size
					feedingTimeRemaining[i] = feedingTimeInitialValue[i] = getItemFeedTime(itemStacks[foodSlotNumber]);
					--itemStacks[foodSlotNumber].stackSize;
					++feedingCount;
					inventoryChanged = true;
				// If the stack size now equals 0 set the slot contents to the items container item. This is for fuel
				// items such as lava buckets so that the bucket is not consumed. If the item dose not have
				// a container item getContainerItem returns null which sets the slot contents to null
					if (itemStacks[foodSlotNumber].stackSize == 0) {
						itemStacks[foodSlotNumber] = itemStacks[foodSlotNumber].getItem().getContainerItem(itemStacks[foodSlotNumber]);
					}
				}
			}
		}
		if (inventoryChanged) markDirty();
		return feedingCount;
	}

	/**
	 * Check if the dino is tameable
	 * @return true if taming is complete
	 */
	private boolean canTame() {
		return this.entityDino.isTameable();
	}

	/**
	 * Tame the dino, if possible
	 * @return 
	 */
	private void setToTamed() {
		this.entityDino.setTamed(playerTaming, true);
	}

	// returns the number of ticks the given item will burn. Returns 0 if the given item is not a valid fuel
	public static short getItemFeedTime(ItemStack stack) {
		int feedtime = ARKFood.getItemFeedTime(stack);
		return (short)MathHelper.clamp_int(feedtime, 0, Short.MAX_VALUE);
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
		return entityDino.isUseableByPlayer(player);
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
	public double unconciousLevel() {
		// TODO Auto-generated method stub
		return 0.5;
	}

	// Returns double between 0 and 1 representing % full level
	public double tamingLevel() {
		// TODO Auto-generated method stub
		return 0.5;
	}

	// Return true if stack is a water for the crop plot
	public boolean isItemValidForNarcoSlot(ItemStack stack) {
		if (stack != null && stack.getItem() == GlobalAdditions.narcoBerry || stack.getItem() == GlobalAdditions.narcotics)
			return true;
		else
			return false;
	}

	// Return true if stack is a valid seed for the crop plot
	public boolean isItemValidForFoodSlot(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ARKFood && ARKFood.getItemFeedTime(stack) > 0)
			return true;
		else
			return false;
	}	
	
	//------------------------------

	// Fields are used to send non-inventory information from the server to interested clients
	// The container code caches the fields and sends the client any fields which have changed.
	// The field ID is limited to byte, and the field value is limited to short. (if you use more than this, they get cast down
	//   in the network packets)
	// If you need more than this, or shorts are too small, use a custom packet in your container instead.

	private static final byte GROW_FIELD_ID = 0;
	private static final byte NUMBER_OF_FIELDS = 1;

	@Override
	public int getField(int id) {
		if (id == GROW_FIELD_ID) return tamingTime;
		System.err.println("Invalid field ID in InventoryTaming.getField:" + id);
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		if (id == GROW_FIELD_ID) {
			tamingTime = (short)value;
		} else {
			System.err.println("Invalid field ID in InventoryTaming.setField:" + id);
		}
	}

	@Override
	public int getFieldCount() {
		return NUMBER_OF_FIELDS;
	}

	@Override
	public void markDirty() {
		entityDino.markDirty();
	}
}
