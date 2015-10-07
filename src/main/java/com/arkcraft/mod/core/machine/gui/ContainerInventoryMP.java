package com.arkcraft.mod.core.machine.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.arkcraft.mod.core.blocks.TileInventoryMP;
import com.arkcraft.mod.core.lib.LogHelper;

public class ContainerInventoryMP extends Container {

	private TileInventoryMP tileInventoryMP;
	private final int MP_SLOT_COUNT = 10;
	// These store cache values, used by the server to only update the client side tile entity when values have changed
	private int [] cachedFields;
	public static final int RECIPE_ITEM_SLOT_YPOS = 62;

	public ContainerInventoryMP(InventoryPlayer invPlayer, TileInventoryMP tileInventoryMP) {
		LogHelper.info("ContainerMP: constructor called.");
		
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

		/* MP inventory */
		// Recipe blueprint slot
		this.addSlotToContainer(new Slot(tileInventoryMP, TileInventoryMP.BLUEPRINT_SLOT, 44, 18));
		// Fertilizer slots
		for(int col = TileInventoryMP.FIRST_INVENTORY_SLOT; col < MP_SLOT_COUNT - 1; col++) {
			addSlotToContainer(new SlotRecipeInventory(tileInventoryMP, col, 8 + col * 18, RECIPE_ITEM_SLOT_YPOS));
		}
	}
	
	/* Nothing to do, this is a furnace type container */
    public void onContainerClosed(EntityPlayer playerIn) {
    	super.onContainerClosed(playerIn);
    }
    
    // TODO: Write this section
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		super.transferStackInSlot(playerIn, index);
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 0) {
				if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 37) {
				if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
					return null;
				}
			} else if (index >= 37 && index < 46) {
				if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
					return null;
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
				return null;
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(playerIn, itemstack1);
		}

		return itemstack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tileInventoryMP.isUseableByPlayer(playerIn);
	}

	// This is where you check if any values have changed and if so send an update to any clients accessing this container
	// The container itemstacks are tested in Container.detectAndSendChanges, so we don't need to do that
	// We iterate through all of the TileEntity Fields to find any which have changed, and send them.
	// You don't have to use fields if you don't wish to; just manually match the ID in sendProgressBarUpdate with the value in
	//   updateProgressBar()
	// The progress bar values are restricted to shorts.  If you have a larger value (eg int), it's not a good idea to try and split it
	//   up into two shorts because the progress bar values are sent independently, and unless you add synchronisation logic at the
	//   receiving side, your int value will be wrong until the second short arrives.  Use a custom packet instead.
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		boolean allFieldsHaveChanged = false;
		boolean fieldHasChanged [] = new boolean[tileInventoryMP.getFieldCount()];
		if (cachedFields == null) {
			cachedFields = new int[tileInventoryMP.getFieldCount()];
			allFieldsHaveChanged = true;
		}
		for (int i = 0; i < cachedFields.length; ++i) {
			if (allFieldsHaveChanged || cachedFields[i] != tileInventoryMP.getField(i)) {
				cachedFields[i] = tileInventoryMP.getField(i);
				fieldHasChanged[i] = true;
			}
		}

		// go through the list of crafters (players using this container) and update them if necessary
		for (int i = 0; i < this.crafters.size(); ++i) {
			ICrafting icrafting = (ICrafting)this.crafters.get(i);
			for (int fieldID = 0; fieldID < tileInventoryMP.getFieldCount(); ++fieldID) {
				if (fieldHasChanged[fieldID]) {
					// Note that although sendProgressBarUpdate takes 2 ints on a server these are truncated to shorts
					icrafting.sendProgressBarUpdate(this, fieldID, cachedFields[fieldID]);
				}
			}
		}
	}
	
	// SlotRecipeInventory is a slot for recipe items
	public class SlotRecipeInventory extends Slot {
		public SlotRecipeInventory(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return tileInventoryMP.isItemValidForRecipeSlot(stack);
		}
	}
}
