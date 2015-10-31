package com.arkcraft.mod.client.gui;

import com.arkcraft.mod.common.entity.player.ARKPlayer;
import com.arkcraft.mod.common.lib.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerInventoryPlayerCrafting extends Container {

	private InventoryBlueprints inventoryBlueprints;
	private InventoryPlayerCrafting inventoryPlayerCrafting;
	// These store cache values, used by the server to only update the client side tile entity when values have changed
//	private int [] cachedFields;
	
	// some [x,y] coordinates of graphical elements (also used elsewhere)
    public static final int BLUEPRINT_XPOS = 26;
	public static final int BLUEPRINT_YPOS = 27;
	public static final int PLAYER_CRAFTING_YPOS = 99;
	public static final int NUM_COLUMNS_BP = 5;
	public static final int NUM_ROWS_BP = 4;
	public static final int BP_SLOT_COUNT = NUM_COLUMNS_BP * NUM_ROWS_BP; 

	public ContainerInventoryPlayerCrafting(InventoryPlayer invPlayer, EntityPlayer player) {
		LogHelper.info("ContainerInventoryPlayerCrafting: Constructor called on " + FMLCommonHandler.instance().getEffectiveSide());

		inventoryBlueprints = ARKPlayer.get(player).getInventoryBlueprints();
		inventoryPlayerCrafting = ARKPlayer.get(player).getInventoryPlayer();
		
		// Recipe blueprint slots
		if (BP_SLOT_COUNT != inventoryBlueprints.getSizeInventory()) {
			LogHelper.error("Mismatched slot count in container(" + BP_SLOT_COUNT + ") and InventoryBlueprints (" 
						+ inventoryBlueprints.getSizeInventory()+")");
		}
		for(int row = 0; row < NUM_ROWS_BP; row++) {
			for(int col = 0; col < NUM_COLUMNS_BP; col++) {
				int slotIndex =  col + row * NUM_COLUMNS_BP;
				this.addSlotToContainer(new SlotBlueprintInventory(inventoryBlueprints, slotIndex,
						BLUEPRINT_XPOS + col * 18, BLUEPRINT_YPOS + row * 18));
			}
		}
		
		// Player Crafting Inventory slots
		final int NUM_COLUMNS_PC = 5;
		final int NUM_ROWS_PC = 2;
		final int PC_XPOS = 26;
		final int PC_SLOT_COUNT = NUM_COLUMNS_PC * NUM_ROWS_PC; 
		if (PC_SLOT_COUNT != inventoryPlayerCrafting.getSizeInventory()) {
			LogHelper.error("Mismatched slot count in container(" + PC_SLOT_COUNT + ") and InventoryPlayerCrafting (" 
						+ inventoryPlayerCrafting.getSizeInventory()+")");
			return;
		}
		for(int row = 0; row < NUM_ROWS_PC; row++) {
			for(int col = 0; col < NUM_COLUMNS_PC; col++) {
				int slotIndex =  col + row * NUM_COLUMNS_PC;
				
				this.addSlotToContainer(new SlotPlayerCraftingInventory(inventoryPlayerCrafting, slotIndex,
						PC_XPOS + col * 18, PLAYER_CRAFTING_YPOS + row * 18));
			}
		}
		
		/* Player inventory */
		final int PLAYER_INVENTORY_YPOS = 161;
		for(int row = 0; row < 3; row++) {
			for(int col = 0; col < 9; col++) {
				int slotIndex =  col + row * 9 + 9;
				addSlotToContainer(new Slot(invPlayer, slotIndex, 8 + col * 18, PLAYER_INVENTORY_YPOS + row * 18));
			}
		}
		
		/* Hotbar inventory */
		final int HOTBAR_YPOS = 219;
		for(int col = 0; col < 9; col++) {
			addSlotToContainer(new Slot(invPlayer, col, 8 + col * 18, HOTBAR_YPOS));
		}		
	}
	
	/* Nothing to do, this is a furnace type container */
    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
    	super.onContainerClosed(playerIn);
    }
    
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int sourceSlotIndex) {
		LogHelper.info("ARKContainerSmithy: transferStackInSlot called.");
		Slot sourceSlot = (Slot)inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) return null;
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is the crafting inventory container slot
		int nonPlayerSlotsCount = inventoryPlayerCrafting.getSizeInventory() + inventoryBlueprints.getSizeInventory();
		if (sourceSlotIndex > inventoryBlueprints.getSizeInventory() - 1 && sourceSlotIndex < nonPlayerSlotsCount) {
			// This is a player crafting inventory slot so merge the stack into the players inventory
			if (!mergeItemStack(sourceStack, nonPlayerSlotsCount, 36 + nonPlayerSlotsCount, false)){
				return null;
			}
		}
		// Check if the slot clicked is one of the vanilla container slots
		else if(sourceSlotIndex >= nonPlayerSlotsCount && sourceSlotIndex < 36 + nonPlayerSlotsCount) {
			if (inventoryPlayerCrafting.isItemValidForSlot(sourceSlotIndex, sourceStack)) {
				// This is a vanilla container slot so merge the stack into the Smithy inventory
				if(!mergeItemStack(sourceStack, inventoryBlueprints.getSizeInventory(), nonPlayerSlotsCount, false)) {
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
		return true;
	}

	// This is where you check if any values have changed and if so send an update to any clients accessing this container
	// The container itemstacks are tested in Container.detectAndSendChanges, so we don't need to do that
	// We iterate through all of the TileEntity Fields to find any which have changed, and send them.
	// You don't have to use fields if you don't wish to; just manually match the ID in sendProgressBarUpdate with the value in
	//   updateProgressBar()
	// The progress bar values are restricted to shorts.  If you have a larger value (eg int), it's not a good idea to try and split it
	//   up into two shorts because the progress bar values are sent independently, and unless you add synchronization logic at the
	//   receiving side, your int value will be wrong until the second short arrives.  Use a custom packet instead.
//	@Override
//	public void detectAndSendChanges() {
//		super.detectAndSendChanges();
//		
//		boolean allFieldsHaveChanged = false;
//		boolean fieldHasChanged [] = new boolean[tileInventorySmithy.getFieldCount()];
//		if (cachedFields == null) {
//			cachedFields = new int[tileInventorySmithy.getFieldCount()];
//			allFieldsHaveChanged = true;
//		}
//		for (int i = 0; i < cachedFields.length; ++i) {
//			if (allFieldsHaveChanged || cachedFields[i] != tileInventorySmithy.getField(i)) {
//				cachedFields[i] = tileInventorySmithy.getField(i);
//				fieldHasChanged[i] = true;
//			}
//		}
//
//		// go through the list of crafters (players using this container) and update them if necessary
//		for (int i = 0; i < this.crafters.size(); ++i) {
//			ICrafting icrafting = (ICrafting)this.crafters.get(i);
//			for (int fieldID = 0; fieldID < tileInventorySmithy.getFieldCount(); ++fieldID) {
//				if (fieldHasChanged[fieldID]) {
//					// Note that although sendProgressBarUpdate takes 2 ints on a server these are truncated to shorts
//					icrafting.sendProgressBarUpdate(this, fieldID, cachedFields[fieldID]);
//				}
//			}
//		}
//	}

	// Called when a progress bar update is received from the server. The two values (id and data) are the same two
	// values given to sendProgressBarUpdate.  In this case we are using fields so we just pass them to the tileEntity.
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data) {
//		LogHelper.info("ContainerInventorySmithy-updateProgressBar: Called on " + (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ? "client" : "server"));
//		LogHelper.info("ContainerInventorySmithy-updateProgressBar: id = " + id + ", data = " + data);
//		tileInventorySmithy.setField(id, data);
	}

	// SlotPlayerCraftingInventory is a slot for recipe items and the output of recipes
	public class SlotPlayerCraftingInventory extends Slot {
		public SlotPlayerCraftingInventory(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack) {
			return true;
//			return tileInventorySmithy.isItemValidForRecipeSlot(stack);
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
		
		@Override
	    public boolean canTakeStack(EntityPlayer playerIn){
			return false;
		}
	}
	
//	public void setBlueprintItemStack(ItemStack stack) {
//		this.inventoryBlueprints.setInventorySlotContents(0, stack);
//	}

	// Used by GUI to see if any players have the GUI open
//	public int getNumCrafters() {
//		return this.crafters.size();
//	}
}
