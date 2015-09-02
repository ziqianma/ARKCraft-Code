package com.arkcraft.mod.core.blocks.crop_test;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.lib.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ARKContainerCropPlot extends Container {

	public World world;
	public InventoryPlayer invPlayer;
	public BlockPos pos;
	public InventoryCrafting inputSlots;
	public IInventory outputSlots;

	public ARKContainerCropPlot(InventoryPlayer invPlayer, World world, BlockPos pos) {
		LogHelper.info("ContainerMP: constructor called.");
		this.world = world;
		this.invPlayer = invPlayer;
		this.pos = pos;
		inputSlots = new InventoryCrafting(this, 7,7);
		outputSlots = new InventoryCraftResult();
		
		this.addSlotToContainer(new Slot(inputSlots, 1, 26, 35));

		this.addSlotToContainer(new Slot(inputSlots, 2, 62, 26));
		this.addSlotToContainer(new Slot(inputSlots, 3, 80, 26));
		this.addSlotToContainer(new Slot(inputSlots, 4, 98, 26));

		this.addSlotToContainer(new Slot(inputSlots, 5, 62, 44));
		this.addSlotToContainer(new Slot(inputSlots, 6, 80, 44));
		this.addSlotToContainer(new Slot(inputSlots, 7, 98, 44));

		this.addSlotToContainer(new Slot(outputSlots, 1, 134, 26));
		this.addSlotToContainer(new Slot(outputSlots, 2, 134, 44));
		
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
		
		this.onCraftMatrixChanged(inputSlots);
	}
	
	/* GET ITEMS OUT ONCE CLOSED */
    public void onContainerClosed(EntityPlayer playerIn) {
		if (playerIn.worldObj.isRemote)
			LogHelper.info("ContainerInventoryDodo: onContainerClosed called on client.");
		else
			LogHelper.info("ContainerInventoryDodo: onContainerClosed called on server.");

    	super.onContainerClosed(playerIn);

        if (!this.world.isRemote) {
            for (int i = 0; i < 2; ++i) {
                ItemStack itemstack = this.inputSlots.getStackInSlotOnClosing(i);

                if (itemstack != null) {
                    playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }
    
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
		if (world.getBlockState(pos).getBlock() != GlobalAdditions.crop_plot) 
			return false;
		else
			return playerIn.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;	
	}
}