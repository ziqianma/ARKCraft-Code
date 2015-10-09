package com.arkcraft.mod.core.machine.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.mod.core.blocks.TileInventoryMP;
import com.arkcraft.mod.core.lib.LogHelper;

public class ContainerInventoryMP extends Container {

	private TileInventoryMP tileInventoryMP;
	private InventoryBlueprints inventoryBlueprints;
	// These store cache values, used by the server to only update the client side tile entity when values have changed
	private int [] cachedFields;
	public static final int RECIPE_ITEM_SLOT_YPOS = 62;

	public ContainerInventoryMP(InventoryPlayer invPlayer, TileInventoryMP tileInventoryMP) {
		LogHelper.info("ContainerMP: constructor called.");

		this.tileInventoryMP = tileInventoryMP;
		inventoryBlueprints = tileInventoryMP.inventoryBlueprints; 
		
		/* MP inventory */
		// Recipe blueprint slot
		this.addSlotToContainer(new SlotBlueprintInventory(inventoryBlueprints, TileInventoryMP.BLUEPRINT_SLOT, 34, 18));

		// Input & Output slots
		for(int col = TileInventoryMP.FIRST_INVENTORY_SLOT; col < TileInventoryMP.INVENTORY_SLOTS_COUNT; col++) {
			addSlotToContainer(new SlotRecipeInventory(this.tileInventoryMP, col, 8 + col * 18, RECIPE_ITEM_SLOT_YPOS));
		}
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
	}
	
    @Override
    public void addCraftingToCrafters(ICrafting listener) {
        super.addCraftingToCrafters(listener);
        listener.func_175173_a(this, tileInventoryMP);
    }

	/* Nothing to do, this is a furnace type container */
    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
    	super.onContainerClosed(playerIn);
    }
    
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int sourceSlotIndex) {
		LogHelper.info("ARKContainerMP: transferStackInSlot called.");
		Slot sourceSlot = (Slot)inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return null;
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is a MP container slot
		int nonPlayerSlotsCount = TileInventoryMP.BLUEPRINT_SLOTS_COUNT + TileInventoryMP.INVENTORY_SLOTS_COUNT;
		if (sourceSlotIndex > TileInventoryMP.BLUEPRINT_SLOTS_COUNT - 1 && sourceSlotIndex < nonPlayerSlotsCount) {
			// This is a MP slot so merge the stack into the players inventory
			if (!mergeItemStack(sourceStack, nonPlayerSlotsCount, 36 + nonPlayerSlotsCount, false)){
				return null;
			}
		}
		// Check if the slot clicked is one of the vanilla container slots
		else if(sourceSlotIndex >= nonPlayerSlotsCount && sourceSlotIndex < 36 + nonPlayerSlotsCount) {
			if (tileInventoryMP.isItemValidForRecipeSlot(sourceStack)) {
				// This is a vanilla container slot so merge the stack into the MP inventory
				if(!mergeItemStack(sourceStack, TileInventoryMP.BLUEPRINT_SLOTS_COUNT, nonPlayerSlotsCount, false)) {
					return null;
				}
			}
			else
				return null;
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

	// Called when a progress bar update is received from the server. The two values (id and data) are the same two
	// values given to sendProgressBarUpdate.  In this case we are using fields so we just pass them to the tileEntity.
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
		tileInventoryMP.setField(id, data);
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

	// SlotBlueprintInventory is a slot for blueprint items
	public class SlotBlueprintInventory extends Slot {
		public SlotBlueprintInventory(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return false;
		}
	}
	
	public void setBlueprintItemStack(ItemStack stack) {
		this.inventoryBlueprints.setInventorySlotContents(0, stack);
	}
}
