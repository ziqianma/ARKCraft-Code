package com.arkcraft.module.blocks.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.blocks.common.tile.TileInventoryCropPlot;

/**
 * @author wildbill22
 */
public class ContainerInventoryCropPlot extends Container
{

	private TileInventoryCropPlot tileInventoryCropPlot;
	private final int CROP_SLOT_COUNT = 9;
	public static final int FERTILIZER_SLOT_YPOS = 53;

	// These store cache values, used by the server to only update the client
	// side tile entity when values have changed
	private int[] cachedFields;

	public ContainerInventoryCropPlot(InventoryPlayer invPlayer, TileInventoryCropPlot tileInventoryCropPlot)
	{
		LogHelper.info("TileInventoryCropPlot: constructor called.");
		this.tileInventoryCropPlot = tileInventoryCropPlot;

		/* Hotbar inventory */
		final int HOTBAR_YPOS = 142;
		for (int col = 0; col < 9; col++)
		{
			addSlotToContainer(new Slot(invPlayer, col, 8 + col * 18, HOTBAR_YPOS));
		}

		/* Player inventory */
		final int PLAYER_INVENTORY_YPOS = 84;
		for (int row = 0; row < 3; row++)
		{
			for (int col = 0; col < 9; col++)
			{
				int slotIndex = col + row * 9 + 9;
				addSlotToContainer(new Slot(invPlayer, slotIndex, 8 + col * 18,
						PLAYER_INVENTORY_YPOS + row * 18));
			}
		}

		/* Crop inventory */
		if (CROP_SLOT_COUNT != tileInventoryCropPlot.getSizeInventory())
		{
			LogHelper
					.error("Mismatched slot count in container(" + CROP_SLOT_COUNT + ") and CropInventory (" + tileInventoryCropPlot
							.getSizeInventory() + ")");
		}
		// Water and Seed slot are first two
		this.addSlotToContainer(new SlotWater(tileInventoryCropPlot,
				TileInventoryCropPlot.WATER_SLOT, 8, 53)); // Water input slot
		this.addSlotToContainer(new SlotSeed(tileInventoryCropPlot,
				TileInventoryCropPlot.SEED_SLOT, 44, 17)); // Seed input slot
		// Fertilizer slots
		for (int col = TileInventoryCropPlot.FIRST_FERTILIZER_SLOT; col < CROP_SLOT_COUNT - 1; col++)
		{
			addSlotToContainer(new SlotFertilizer(tileInventoryCropPlot, col, 8 + col * 18,
					FERTILIZER_SLOT_YPOS));
		}
		// Berry output slot (berry is centered in 24 x 24 box (berry is 16 x
		// 16)
		this.addSlotToContainer(new Slot(tileInventoryCropPlot, TileInventoryCropPlot.BERRY_SLOT,
				104, 17));
	}

	/* Nothing to do, this is a furnace type container */
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int sourceSlotIndex)
	{
		LogHelper.info("ARKContainerCropPlot: transferStackInSlot called.");
		Slot sourceSlot = (Slot) inventorySlots.get(sourceSlotIndex);
		if (sourceSlot == null || !sourceSlot.getHasStack()) { return null; }
		ItemStack sourceStack = sourceSlot.getStack();
		ItemStack copyOfSourceStack = sourceStack.copy();

		// Check if the slot clicked is one of the vanilla container slots
		if (sourceSlotIndex >= 0 && sourceSlotIndex < 36)
		{
			if (tileInventoryCropPlot.isItemValidForWaterSlot(sourceStack))
			{
				// This is a vanilla container slot so merge the stack into the
				// crop plot inventory
				if (!mergeItemStack(
						sourceStack,
						36,
						36 + TileInventoryCropPlot.WATER_SLOT + TileInventoryCropPlot.WATER_SLOTS_COUNT,
						false)) { return null; }
			}
			else if (tileInventoryCropPlot.isItemValidForSeedSlot(sourceStack))
			{
				// This is a vanilla container slot so merge the stack into the
				// crop plot inventory
				if (!mergeItemStack(
						sourceStack,
						36,
						36 + TileInventoryCropPlot.SEED_SLOT + TileInventoryCropPlot.SEED_SLOTS_COUNT,
						false)) { return null; }
			}
			else if (tileInventoryCropPlot.isItemValidForFertilizerSlot(sourceStack))
			{
				// This is a vanilla container slot so merge the stack into the
				// crop plot inventory
				if (!mergeItemStack(
						sourceStack,
						36,
						36 + TileInventoryCropPlot.FIRST_FERTILIZER_SLOT + TileInventoryCropPlot.FERTILIZER_SLOTS_COUNT,
						false)) { return null; }
			}
			else
			{
				return null;
			}
		}
		// Check if the slot clicked is a crop plot container slot
		else if (sourceSlotIndex >= 36 && sourceSlotIndex < 36 + CROP_SLOT_COUNT)
		{
			// This is a crop plot slot so merge the stack into the players
			// inventory
			if (!mergeItemStack(sourceStack, 0, 36, false)) { return null; }
		}
		else
		{
			LogHelper.error("Invalid slotIndex:" + sourceSlotIndex);
			return null;
		}

		// If stack size == 0 (the entire stack was moved) set slot contents to
		// null
		if (sourceStack.stackSize == 0)
		{
			sourceSlot.putStack(null);
		}
		else
		{
			sourceSlot.onSlotChanged();
		}

		sourceSlot.onPickupFromSlot(playerIn, sourceStack);
		return copyOfSourceStack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tileInventoryCropPlot.isUseableByPlayer(playerIn);
	}

	// This is where you check if any values have changed and if so send an
	// update to any clients accessing this container
	// The container itemstacks are tested in Container.detectAndSendChanges, so
	// we don't need to do that
	// We iterate through all of the TileEntity Fields to find any which have
	// changed, and send them.
	// You don't have to use fields if you don't wish to; just manually match
	// the ID in sendProgressBarUpdate with the value in
	// updateProgressBar()
	// The progress bar values are restricted to shorts. If you have a larger
	// value (eg int), it's not a good idea to try and split it
	// up into two shorts because the progress bar values are sent
	// independently, and unless you add synchronisation logic at the
	// receiving side, your int value will be wrong until the second short
	// arrives. Use a custom packet instead.
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		boolean allFieldsHaveChanged = false;
		boolean fieldHasChanged[] = new boolean[tileInventoryCropPlot.getFieldCount()];
		if (cachedFields == null)
		{
			cachedFields = new int[tileInventoryCropPlot.getFieldCount()];
			allFieldsHaveChanged = true;
		}
		for (int i = 0; i < cachedFields.length; ++i)
		{
			if (allFieldsHaveChanged || cachedFields[i] != tileInventoryCropPlot.getField(i))
			{
				cachedFields[i] = tileInventoryCropPlot.getField(i);
				fieldHasChanged[i] = true;
			}
		}

		// go through the list of crafters (players using this container) and
		// update them if necessary
		for (int i = 0; i < this.crafters.size(); ++i)
		{
			ICrafting icrafting = (ICrafting) this.crafters.get(i);
			for (int fieldID = 0; fieldID < tileInventoryCropPlot.getFieldCount(); ++fieldID)
			{
				if (fieldHasChanged[fieldID])
				{
					// Note that although sendProgressBarUpdate takes 2 ints on
					// a server these are truncated to shorts
					icrafting.sendProgressBarUpdate(this, fieldID, cachedFields[fieldID]);
				}
			}
		}
	}

	// Called when a progress bar update is received from the server. The two
	// values (id and data) are the same two
	// values given to sendProgressBarUpdate. In this case we are using fields
	// so we just pass them to the tileEntity.
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int data)
	{
		tileInventoryCropPlot.setField(id, data);
	}

	// SlotFertilizer is a slot for fertilizer items
	public class SlotFertilizer extends Slot
	{
		public SlotFertilizer(IInventory inventoryIn, int index, int xPosition, int yPosition)
		{
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert
		// the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return tileInventoryCropPlot.isItemValidForFertilizerSlot(stack);
		}
	}

	// SlotWater is a slot for water
	public class SlotWater extends Slot
	{
		public SlotWater(IInventory inventoryIn, int index, int xPosition, int yPosition)
		{
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert
		// the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return tileInventoryCropPlot.isItemValidForWaterSlot(stack);
		}
	}

	// SlotSeed is a slot for seeds
	public class SlotSeed extends Slot
	{
		public SlotSeed(IInventory inventoryIn, int index, int xPosition, int yPosition)
		{
			super(inventoryIn, index, xPosition, yPosition);
		}

		// if this function returns false, the player won't be able to insert
		// the given item into this slot
		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return tileInventoryCropPlot.isItemValidForSeedSlot(stack);
		}
	}
}