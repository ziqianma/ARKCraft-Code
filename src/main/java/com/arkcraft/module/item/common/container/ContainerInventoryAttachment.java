package com.arkcraft.module.item.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.item.common.tile.TileInventoryAttachment;

public class ContainerInventoryAttachment extends Container
{
	/** The Item Inventory for this Container */
	private final TileInventoryAttachment tileInventoryAttachment;
	private final int ATTACHMENT_SLOT_COUNT = 10;
    public static final int ATTACHMENT_SLOT_YPOS = 26;
    public static final int ATTACHMENT_SLOT_XPOS = 53;
	
	public ContainerInventoryAttachment(EntityPlayer player, InventoryPlayer invPlayer, TileInventoryAttachment tileInventoryAttachment)
    {
        LogHelper.info("ContainerInventoryCompostBin: constructor called.");
        this.tileInventoryAttachment = tileInventoryAttachment;

		/* Compost bin inventory */
        if (ATTACHMENT_SLOT_COUNT != tileInventoryAttachment.getSizeInventory())
        {
            LogHelper.error("Mismatched slot count in container(" + ATTACHMENT_SLOT_COUNT + ") and CompostBinInventory ("
                    + tileInventoryAttachment.getSizeInventory() + ")");
        }
        for (int row = 0; row < 2; row++)
        {
            for (int col = 0; col < 4; col++)
            {
                int slotIndex = col + row * 4;
                addSlotToContainer(new SlotAttachment(tileInventoryAttachment, slotIndex, ATTACHMENT_SLOT_XPOS + col * 18, ATTACHMENT_SLOT_YPOS + row * 18));
            }
        }

		/* Player inventory */
        final int PLAYER_INVENTORY_YPOS = 84;
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                int slotIndex = col + row * 9 + 9;
                addSlotToContainer(new Slot(invPlayer, slotIndex, 8 + col * 18, PLAYER_INVENTORY_YPOS + row * 18));
            }
        }
		
		/* Hotbar inventory */
        final int HOTBAR_YPOS = 142;
        for (int col = 0; col < 9; col++)
        {
            addSlotToContainer(new Slot(invPlayer, col, 8 + col * 18, HOTBAR_YPOS));
        }
    }

	@Override
	public boolean canInteractWith(EntityPlayer player) 
	{
		return tileInventoryAttachment.isUseableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			// If item is in our custom Inventory or an ARMOR slot
			if (index < ATTACHMENT_SLOT_COUNT)
			{
				// try to place in player inventory / action bar
				if (!this.mergeItemStack(itemstack1, 0, ATTACHMENT_SLOT_COUNT, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			// Item is in inventory / hotbar, try to place in custom inventory or armor slots
			else
			{
				// item is in inventory or action bar
				if (index >= ATTACHMENT_SLOT_COUNT)
				{
					// place in custom inventory
					if (!this.mergeItemStack(itemstack1, 0, ATTACHMENT_SLOT_COUNT, false))
					{
						return null;
					}
				}
			}

			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}

	@Override
	public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player) {
		// this will prevent the player from interacting with the item that opened the inventory:
		if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem()) {
			return null;
		}
		return super.slotClick(slot, button, flag, player);
	}

	// IMPORTANT to override the mergeItemStack method if your inventory stack size limit is 1
	/**
	 * Vanilla method fails to account for stack size limits of one, resulting in only one
	 * item getting placed in the slot and the rest disappearing into thin air; vanilla
	 * method also fails to check whether stack is valid for slot
	 */
	@Override
	protected boolean mergeItemStack(ItemStack stack, int start, int end, boolean backwards)
	{
		boolean flag1 = false;
		int k = (backwards ? end - 1 : start);
		Slot slot;
		ItemStack itemstack1;

		if (stack.isStackable())
		{
			while (stack.stackSize > 0 && (!backwards && k < end || backwards && k >= start))
			{
				slot = (Slot) inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (!slot.isItemValid(stack)) {
					k += (backwards ? -1 : 1);
					continue;
				}

				if (itemstack1 != null && itemstack1.getItem() == stack.getItem() &&
						(!stack.getHasSubtypes() || stack.getItemDamage() == itemstack1.getItemDamage()) &&
						ItemStack.areItemStackTagsEqual(stack, itemstack1))
				{
					int l = itemstack1.stackSize + stack.stackSize;

					if (l <= stack.getMaxStackSize() && l <= slot.getSlotStackLimit()) {
						stack.stackSize = 0;
						itemstack1.stackSize = l;
						tileInventoryAttachment.markDirty();
						flag1 = true;
					} else if (itemstack1.stackSize < stack.getMaxStackSize() && l < slot.getSlotStackLimit()) {
						stack.stackSize -= stack.getMaxStackSize() - itemstack1.stackSize;
						itemstack1.stackSize = stack.getMaxStackSize();
						tileInventoryAttachment.markDirty();
						flag1 = true;
					}
				}

				k += (backwards ? -1 : 1);
			}
		}

		if (stack.stackSize > 0)
		{
			k = (backwards ? end - 1 : start);

			while (!backwards && k < end || backwards && k >= start) {
				slot = (Slot) inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (!slot.isItemValid(stack)) {
					k += (backwards ? -1 : 1);
					continue;
				}

				if (itemstack1 == null) {
					int l = stack.stackSize;

					if (l <= slot.getSlotStackLimit()) {
						slot.putStack(stack.copy());
						stack.stackSize = 0;
						tileInventoryAttachment.markDirty();
						flag1 = true;
						break;
					} else {
						putStackInSlot(k, new ItemStack(stack.getItem(), slot.getSlotStackLimit(), stack.getItemDamage()));
						stack.stackSize -= slot.getSlotStackLimit();
						tileInventoryAttachment.markDirty();
						flag1 = true;
					}
				}

				k += (backwards ? -1 : 1);
			}
		}

		return flag1;
	}
}