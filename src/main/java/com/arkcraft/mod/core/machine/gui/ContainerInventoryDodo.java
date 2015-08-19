package com.arkcraft.mod.core.machine.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.arkcraft.mod.core.entity.passive.EntityDodo;
import com.arkcraft.mod.core.lib.LogHelper;

public class ContainerInventoryDodo extends Container {

	private IInventory invDodo;
	private EntityDodo dodo;
	private final int DODO_SLOT_COUNT = 9;
	
	public ContainerInventoryDodo(IInventory invPlayer, final IInventory invDodo, final EntityDodo dodo, EntityPlayer player) {
		this.invDodo = invDodo;
		this.dodo = dodo;
		((InventoryDino)invDodo).openInventory(player);  // BW: Why is this here?
		
		/* Hotbar inventory */
		final int HOTBAR_YPOS = 142;
		for(int col = 0; col < 9; col++) {
			addSlotToContainer(new Slot(invPlayer, col, 8 + col * 18, HOTBAR_YPOS));
		}
		
		/* Player inventory */
		final int PLAYER_INVENTORY_YPOS = 84;
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 9; col++) {
				int slotIndex =  col + row * 9 + 9;
				addSlotToContainer(new Slot(invPlayer, slotIndex, 8 + col * 18, PLAYER_INVENTORY_YPOS + row * 18));
			}
		}
		
		/* Chest inventory */
		if (DODO_SLOT_COUNT != invDodo.getSizeInventory()) {
			LogHelper.error("Mismatched slot count in container(" + DODO_SLOT_COUNT + ") and DodoInventory (" + invDodo.getSizeInventory()+")");
		}
		final int DODO_INVENTORY_XPOS = 98;
		final int DODO_INVENTORY_YPOS = 18;
		if(dodo.isChested()) {
			for(int col = 0; col < 3; col++) {
				for(int row = 0; row < 3; row++) {
					int slotIndex = col + row * 3;
					addSlotToContainer(new Slot(invDodo, slotIndex, DODO_INVENTORY_XPOS + col * 18, DODO_INVENTORY_YPOS + row * 18));
				}
			}
		}
	}
	
	@Override
    public void addCraftingToCrafters(ICrafting listener)  {
        super.addCraftingToCrafters(listener);
        listener.func_175173_a(this, invDodo);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }

	@Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.invDodo.isUseableByPlayer(playerIn) && this.dodo.isEntityAlive() && this.dodo.getDistanceToEntity(playerIn) < 8.0F;
    }

	// Called when you shift click
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex) {
		Slot sourceSlot = (Slot)inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return null;
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
//		if(sourceSlotIndex >= 0 && sourceSlotIndex < 9) {
//			if(!mergeItemStack(sourceStack, 36, 45, false));
		if(sourceSlotIndex >= 0 && sourceSlotIndex < 36) {
			// This is a vanilla container slot so merge the stack into the dodo inventory
			if(!mergeItemStack(sourceStack, 36, 36 + DODO_SLOT_COUNT, false)) {
				return null;
			}
		}
		// Check if the slot clicked is a dodo container slot
//		else if (sourceSlotIndex >= 9 && sourceSlotIndex < 36) {
//			if (!mergeItemStack(sourceStack, 36, 45, false)){
		else if (sourceSlotIndex >= 36 && sourceSlotIndex < 36 + DODO_SLOT_COUNT) {
			// This is a dodo slot so merge the stack into the players inventory
			if (!mergeItemStack(sourceStack, 0, 36, false)){
				return null;
			}
//		} else if (sourceSlotIndex >= 36 && sourceSlotIndex < 45) {
//			// This is a TE slot so merge the stack into the players inventory
//			if (!mergeItemStack(sourceStack, 9, 36, false) || !mergeItemStack(sourceStack, 0, 9, false)) {
//				return null;
//			}
		} else {
			LogHelper.error("Invalid slotIndex:" + sourceSlotIndex);
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
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		((InventoryDino)invDodo).closeInventory(playerIn);
		// TODO: Add animation to close lid?
	}
}