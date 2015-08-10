package com.arkcraft.mod.core.machine.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.arkcraft.mod.core.entity.passive.EntityDodo;

public class ContainerInventoryDodo extends Container {

	private IInventory invDodo;
	private EntityDodo dodo;
	
	public ContainerInventoryDodo(IInventory invPlayer, final IInventory invDodo, final EntityDodo dodo, EntityPlayer player) {
		this.invDodo = invDodo;
		this.dodo = dodo;
		invDodo.openInventory(player);
		
		/* Hotbar inventory */
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
		if(dodo.isChested()) {
			for(int col = 0; col < 3; col++) {
				for(int row = 0; row < 3; row++) {
					int slotIndex = col + row * 3;
					addSlotToContainer(new Slot(invPlayer, slotIndex, 97 + col * 18, 17 + row * 18));
				}
			}
		}
	}
	
	@Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.invDodo.isUseableByPlayer(playerIn) && this.dodo.isEntityAlive() && this.dodo.getDistanceToEntity(playerIn) < 8.0F;
    }

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex)
	{
		Slot sourceSlot = (Slot)inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return null;
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if (sourceSlotIndex >= 0 && sourceSlotIndex < 36) {
			// This is a vanilla container slot so merge the stack into the tile inventory
			if (!mergeItemStack(sourceStack, 36, 45, false)){
				return null;
			}
		} else if (sourceSlotIndex >= 36 && sourceSlotIndex < 45) {
			// This is a TE slot so merge the stack into the players inventory
			if (!mergeItemStack(sourceStack, 0, 36, false)) {
				return null;
			}
		} else {
			System.err.print("Invalid slotIndex:" + sourceSlotIndex);
			return null;
		}

		// If stack size == 0 (the entire stack was moved) set slot contents to null
		if (sourceStack.stackSize == 0) {
			sourceSlot.putStack(null);
		} else {
			sourceSlot.onSlotChanged();
		}

		sourceSlot.onPickupFromSlot(player, sourceStack);
		return copyOfSourceStack;
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
	}
}