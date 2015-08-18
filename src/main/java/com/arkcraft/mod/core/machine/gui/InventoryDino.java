package com.arkcraft.mod.core.machine.gui;

import java.util.Arrays;

import com.arkcraft.mod.core.lib.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class InventoryDino implements IInventory {
	// Create and initialize the items variable that will store store the items
	final int NUMBER_OF_SLOTS = 9;
	private ItemStack[] itemStacks = new ItemStack[NUMBER_OF_SLOTS];

	public InventoryDino(String title, boolean customName, int slotCount) {
//		super(title, customName, slotCount);
	}

	/* The following are some IInventory methods you are required to override */

	// Gets the number of slots in the inventory
	public int getSizeInventory() {
		return itemStacks.length;
	}

	// Gets the stack in the given slot
	public ItemStack getStackInSlot(int slotIndex) {
		return itemStacks[slotIndex];
	}

	/**
	 * Removes some of the units from itemstack in the given slot, and returns as a separate itemstack
 	 * @param slotIndex the slot number to remove the items from
	 * @param count the number of units to remove
	 * @return a new itemstack containing the units removed from the slot
	 */
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

	// overwrites the stack in the given slotIndex with the given stack
	public void setInventorySlotContents(int slotIndex, ItemStack itemstack) {
		itemStacks[slotIndex] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		markDirty();
	}

	// This is the maximum number if items allowed in each slot
	// This only affects things such as hoppers trying to insert items you need to use the container to enforce this for players
	// inserting items via the gui
	public int getInventoryStackLimit() {
		return 64;
	}

    /**
     * Do not make give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }


	// Return true if the given stack is allowed to go in the given slot.  In this case, we can insert anything.
	// This only affects things such as hoppers trying to insert items you need to use the container to enforce this for players
	// inserting items via the gui
	public boolean isItemValidForSlot(int slotIndex, ItemStack itemstack) {
		return true;
	}

    public void loadInventoryFromNBT(NBTTagList nbt)  {
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, (ItemStack)null);
        }
        for (int i = 0; i < nbt.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.getSizeInventory()) {
                this.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
            }
        }
    }

    public NBTTagList saveInventoryToNBT() {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemstack = this.getStackInSlot(i);
            if (itemstack != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
                LogHelper.info("DinoInventory: Saved a " + itemstack.getItem() + " to inventory.");
            }
        }
        return nbttaglist;
    }
	// set all slots to empty
	public void clear() {
		Arrays.fill(itemStacks, null);
	}

	// will add a key for this container to the lang file so we can name it in the GUI
	public String getName() {
		return "Dino Inventory";
	}

	public boolean hasCustomName() {
		return false;
	}

	// standard code to look up what the human-readable name is
	public IChatComponent getDisplayName() {
		return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
	}

	// -----------------------------------------------------------------------------------------------------------
	// The following methods are not needed for this example but are part of IInventory so they must be implemented

	/**
	 * This method removes the entire contents of the given slot and returns it.
	 * Used by containers such as crafting tables which return any items in their slots when you close the GUI
	 * @param slotIndex
	 * @return
	 */
	public ItemStack getStackInSlotOnClosing(int slotIndex) {
		ItemStack itemStack = getStackInSlot(slotIndex);
		if (itemStack != null) setInventorySlotContents(slotIndex, null);
		return itemStack;
	}

	public void openInventory(EntityPlayer player) {}

	public void closeInventory(EntityPlayer player) {}

	public int getField(int id) {
		return 0;
	}

	public void setField(int id, int value) {}

	public int getFieldCount() {
		return 0;
	}

	@Override
	public void markDirty() {

	}
}
