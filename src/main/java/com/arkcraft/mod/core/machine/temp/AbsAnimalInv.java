package com.arkcraft.mod.core.machine.temp;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public abstract class AbsAnimalInv<S extends AbstractSave> implements IInventory, ISaveableInventory<S> {

	protected ItemStack[] invContent;
	protected String defaultName;
	protected String customName;
	protected int maxStack = 64;
	protected boolean isDirty = false;
	
	@Override
	public int getSizeInventory() {
		if(invContent == null) return 0;
		return invContent.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		if(invContent != null) return invContent[slot];
		return null;
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = null;

        if(getStackInSlot(slot) != null) {
            if(getStackInSlot(slot).stackSize <= amount) {
                stack = getStackInSlot(slot);
                setInventorySlotContents(slot, null);
            } else {
                stack = getStackInSlot(slot).splitStack(amount);

                if(getStackInSlot(slot).stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }

        return stack;
	}
	
	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = null;
		if(getStackInSlot(slot) != null) {
			stack = getStackInSlot(slot);
			invContent[slot] = null;
		}
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack content) {
		if(invContent != null && invContent.length > slot) {
			invContent[slot] = content;
			if(content != null && content.stackSize > getInventoryStackLimit()) content.stackSize = getInventoryStackLimit();
			markDirty();
		}
	}
	
	@Override
    public int getInventoryStackLimit() {
        return maxStack;
    }

    @Override
    public void markDirty() {
        isDirty = true;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    public void openInventory(EntityPlayer player) {}
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return true;
    }

}
