package com.arkcraft.mod.core.machine.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.arkcraft.mod.core.machine.tileentity.TileEntityInventoryDodo;

public class ContainerInventoryDodo extends Container {

	private TileEntityInventoryDodo invDodo;
	
	public ContainerInventoryDodo(InventoryPlayer invPlayer, TileEntityInventoryDodo invDodo) {
		this.invDodo = invDodo;
		/* Hotbar slots */
		for(int col = 0; col < 9; col++) {
			addSlotToContainer(new Slot(invPlayer, col, 7 + col * 18, 141 + col * 18));
		}
		
		/* Player inventory */
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 9; col++) {
				int slotIndex =  col + row * 9 + 9;
				addSlotToContainer(new Slot(invPlayer, slotIndex, 7 + col * 18, 83 + row * 18));
			}
		}
		
		/* Chest inventory */
		for(int col = 0; col < 3; col++) {
			for(int row = 0; row < 3; row++) {
				int slotIndex = col + row * 3;
				addSlotToContainer(new Slot(invDodo, slotIndex, 97 + col * 18, 17 + row * 18));
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return invDodo.isUseableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
		Slot sourceSlot = (Slot)inventorySlots.get(slotIndex);
		if(sourceSlot == null || !sourceSlot.getHasStack()) return null;
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copy = sourceStack.copy();
		if(slotIndex >= 0 && slotIndex < 36) {
			if(!mergeItemStack(sourceStack, 36, 45, false)) return null;
		}
		else if(slotIndex >= 36 && slotIndex < 45) {
			if(!mergeItemStack(sourceStack, 0, 36, false)) return null;
		}
		else {
			System.err.println("Invalid Slot Index: " + slotIndex);
			return null;
		}
		
		if(sourceStack.stackSize == 0) sourceSlot.putStack(null);
		else sourceSlot.onSlotChanged();
		sourceSlot.onPickupFromSlot(player, sourceStack);
		return copy;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
		this.invDodo.closeInventory(playerIn);
	}
	
}