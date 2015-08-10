package com.arkcraft.mod.core.machine.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import scala.actors.threadpool.Arrays;

public class TileEntityInventoryDodo extends TileEntity implements IInventory {

	private static final int NUM_SLOTS = 9;
	private ItemStack[] itemStacks = new ItemStack[NUM_SLOTS];
	
	public int getSizeInventory() { return itemStacks.length; }
	public ItemStack getStackInSlot(int slotIndex) { return itemStacks[slotIndex]; }
	
	@Override
	public ItemStack decrStackSize(int slotIndex, int count) {
		ItemStack inSlot = getStackInSlot(slotIndex);
		if(inSlot == null) return null;
		ItemStack removed;
		if(inSlot.stackSize <= count) {
			removed = inSlot;
			setInventorySlotContents(slotIndex, null);
		}
		removed = inSlot.splitStack(count);
		if(inSlot.stackSize == 0) setInventorySlotContents(slotIndex, null);
		markDirty();
		return removed;
	}
	
	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack itemStack) {
		itemStacks[slotIndex] = itemStack;
		if(itemStack != null && itemStack.stackSize > getInventoryStackLimit()) itemStack.stackSize = getInventoryStackLimit();
		markDirty();
	}
	
	public int getInventoryStackLimit() { return 64; }
	
	public boolean isUseableByPlayer(EntityPlayer player) {
		if(this.worldObj.getTileEntity(this.pos) != this) return false;
		return player.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D) < 64;
	}
	
	public boolean isItemValidForSlot(int slotIndex, ItemStack itemStack) { return true; }
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList allSlots = new NBTTagList();
		for(int i = 0; i < this.itemStacks.length; i++ ) {
			if(itemStacks[i] != null) {
				NBTTagCompound slot = new NBTTagCompound();
				slot.setByte("Slot", (byte)i);
				this.itemStacks[i].writeToNBT(nbt);
				allSlots.appendTag(slot);
			}
		}
		nbt.setTag("Items", allSlots);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		final byte NBT_TYPE = 10;
		NBTTagList allSlots = nbt.getTagList("Items", NBT_TYPE);
		Arrays.fill(itemStacks, null);
		for(int i = 0; i < allSlots.tagCount(); i++) {
			NBTTagCompound slot = allSlots.getCompoundTagAt(i);
			int slotIndex = slot.getByte("Slot") & 255;
			if(  slotIndex >= 0 && slotIndex < this.itemStacks.length) {
				this.itemStacks[slotIndex] = ItemStack.loadItemStackFromNBT(nbt);
			}
		}
	}
	
	public void clear() { Arrays.fill(itemStacks, null); }
	public String getName() { return "Inventory Dodo"; }
	public boolean hasCustomName() { return false; }
	
	@Override
	public IChatComponent getDisplayName() {
		return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
	}
	
	
	/***
	 * Going into non-relevant territory
	 */
	
	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		// TODO Auto-generated method stub
		return null;
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
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}
}
