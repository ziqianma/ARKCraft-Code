package com.arkcraft.mod.core.machine.gui;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.handler.PestleCraftingManager;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/***
 * 
 * @author Vastatio
 *
 */
public class ContainerSmithy extends Container {

	public InventoryCrafting craftMatrix;
	public IInventory craftResult[] = new IInventory[24];
	private World world;
	private BlockPos pos;

	public ContainerSmithy(InventoryPlayer invPlayer, World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
		craftMatrix = new InventoryCrafting(this, 4, 6);
		final int MATRIX_SLOT_YPOS = 18;
		for (int i = 0; i < 24; i++)
			craftResult[i] = new InventoryCraftResult();
		
		/* Crafting Matrix */
		final int MATRIX_SLOT_XPOS = 98;
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 6; col++) {
				this.addSlotToContainer(new Slot(craftMatrix, col + row * 4, MATRIX_SLOT_XPOS + col * 18, MATRIX_SLOT_YPOS + row * 18));
			}
		}
		
		/* Output slot */
		final int OUTPUT_SLOT_YPOS = 140;
		final int OUTPUT_SLOT_XPOS = 98;
		for (int row = 0; row < 4; row++) {
			for (int col = 0; col < 6; col++) {
				this.addSlotToContainer(new SlotCrafting(invPlayer.player, craftMatrix,	craftResult[col + row * 4], col + row * 4, 
						OUTPUT_SLOT_XPOS + col * 18, OUTPUT_SLOT_YPOS + row * 18));
			}
		}

		/* Player inventory */
		final int PLAYER_INVENTORY_YPOS = 140;
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				int slotIndex =  col + row * 9 + 9;
				addSlotToContainer(new Slot(invPlayer, slotIndex, 8 + col * 18, PLAYER_INVENTORY_YPOS + row * 18));
			}
		}

		/* Hotbar inventory */
		final int HOTBAR_YPOS = 198;
		for(int col = 0; col < 9; col++) {
			addSlotToContainer(new Slot(invPlayer, col, 8 + col * 18, HOTBAR_YPOS));
		}

		this.onCraftMatrixChanged(craftMatrix);
	}
	
	/* GET ITEMS OUT ONCE CLOSED */
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        if (!this.world.isRemote) {
            for (int i = 0; i < 24; ++i) {
                ItemStack itemstack = this.craftMatrix.getStackInSlotOnClosing(i);

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
	public void onCraftMatrixChanged(IInventory inventory) {
		//craftResult.setInventorySlotContents(0, SmithyCraftingManager.getInstance().findMatchingRecipe(craftMatrix, world));
		ItemStack[] itemStacks = PestleCraftingManager.getInstance().findMatchingRecipes(this.craftMatrix, this.world);
		for (int i = 0; i < 24 && itemStacks[i] != null; i++)
			this.craftResult[i].setInventorySlotContents(0, itemStacks[i]);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		if(world.getBlockState(pos).getBlock() != GlobalAdditions.smithy) 
			return false;
		return playerIn.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;	
	}
}
