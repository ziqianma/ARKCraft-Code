package com.arkcraft.mod.core.machine.gui;

import com.arkcraft.mod.core.GlobalAdditions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerMP extends Container {

	public World world;
	public InventoryPlayer invPlayer;
	public BlockPos pos;
	public InventoryCrafting inputSlots;
	public IInventory outputSlot;
	
	public ContainerMP(InventoryPlayer invPlayer, World world, BlockPos pos) {
		this.world = world;
		this.invPlayer = invPlayer;
		this.pos = pos;
		inputSlots = new InventoryCrafting(this, 2, 1);
		
		this.addSlotToContainer(new Slot(inputSlots, 0, 55, 226));
		this.addSlotToContainer(new Slot(inputSlots, 1, 55, 190));
		this.addSlotToContainer(new SlotCrafting(invPlayer.player, inputSlots, outputSlot, 0, 115, 208));
		
		for(int i = 0; i < 3; i++) for(int k = 0; k < 9; k++) this.addSlotToContainer(new Slot(invPlayer, k + i * 9 + 9, 7 + k * 18, 159 + i * 18));
		for(int i = 0; i < 9; i++) this.addSlotToContainer(new Slot(invPlayer, i, 7 + i * 18, 101));
		this.onCraftMatrixChanged(inputSlots);
	}
	
	/* GET ITEMS OUT ONCE CLOSED */
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        if (!this.world.isRemote)
        {
            for (int i = 0; i < 25; ++i)
            {
                ItemStack itemstack = this.inputSlots.getStackInSlotOnClosing(i);

                if (itemstack != null)
                {
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
	public void onCraftMatrixChanged(IInventory inventory) {
		//craftResult.setInventorySlotContents(0, SmithyCraftingManager.getInstance().findMatchingRecipe(craftMatrix, world));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		if(world.getBlockState(pos).getBlock() != GlobalAdditions.smithy) return false;
		return playerIn.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;
		
	}

}
