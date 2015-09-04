package com.arkcraft.mod.core.blocks.crop_test;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.lib.LogHelper;

public class ARKContainerCropPlot extends Container {

	public World world;
	public InventoryPlayer invPlayer;
	public BlockPos pos;
	public InventoryCrafting inputSlots;
	public IInventory outputSlots;
	private final int CROP_SLOT_COUNT = 7;

	public ARKContainerCropPlot(InventoryPlayer invPlayer, World world, BlockPos pos) {
		LogHelper.info("ARKContainerCropPlot: constructor called.");
		this.world = world;
		this.invPlayer = invPlayer;
		this.pos = pos;
		inputSlots = new InventoryCrafting(this, 7, 7);
		outputSlots = new InventoryCraftResult();
		
		/* Crop inventory */
		if (CROP_SLOT_COUNT != inputSlots.getSizeInventory()) {
			LogHelper.error("Mismatched slot count in container(" + CROP_SLOT_COUNT + ") and CropInventory (" + inputSlots.getSizeInventory()+")");
		}
		final int INPUT_SLOT_YPOS = 53;
		for(int col = 0; col < CROP_SLOT_COUNT; col++) {
			if (col != 1)
				addSlotToContainer(new Slot(invPlayer, col, 8 + col * 18, INPUT_SLOT_YPOS));
			else
				this.addSlotToContainer(new Slot(inputSlots, 1, 44, 17)); // Seed input slot
		}
		
		// Berry output slot
		this.addSlotToContainer(new Slot(outputSlots, 1, 100, 13));
		
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
			LogHelper.info("ARKContainerCropPlot: onContainerClosed called on client.");
		else
			LogHelper.info("ARKContainerCropPlot: onContainerClosed called on server.");

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
    
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int sourceSlotIndex) {
		LogHelper.info("ARKContainerCropPlot: transferStackInSlot called.");
		Slot sourceSlot = (Slot)inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return null;
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if(sourceSlotIndex >= 0 && sourceSlotIndex < 36) {
			// This is a vanilla container slot so merge the stack into the dodo inventory
			if(!mergeItemStack(sourceStack, 36, 36 + CROP_SLOT_COUNT, false)) {
				return null;
			}
		}
		// Check if the slot clicked is a dodo container slot
		else if (sourceSlotIndex >= 36 && sourceSlotIndex < 36 + CROP_SLOT_COUNT) {
			// This is a dodo slot so merge the stack into the players inventory
			if (!mergeItemStack(sourceStack, 0, 36, false)){
				return null;
			}
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
		
		sourceSlot.onPickupFromSlot(playerIn, sourceStack);
		return copyOfSourceStack;
	}
	  
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		if (world.getBlockState(pos).getBlock() != GlobalAdditions.crop_plot) 
			return false;
		else
			return playerIn.getDistanceSq((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D) <= 64.0D;	
	}
}