package com.arkcraft.module.item.common.container;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.item.common.items.ARKCraftItems;
import com.arkcraft.module.item.common.tile.TileInventoryAttachment2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author wildbill22
 */
public class ContainerInventoryAttachment extends Container
{
    private TileInventoryAttachment2 tileInventoryAttachment;
    private final int SLOT_COUNT = 4;
    public static final int COMPOST_SLOT_YPOS = 26;
    public static final int COMPOST_SLOT_XPOS = 53;

    // These store cache values, used by the server to only update the client side tile entity when values have changed
    private int[] cachedFields;

    public ContainerInventoryAttachment(EntityPlayer player, InventoryPlayer invPlayer, TileInventoryAttachment2 tileInventoryAttachment)
    {
        LogHelper.info("ContainerInventoryAttachment: constructor called.");
        this.tileInventoryAttachment = tileInventoryAttachment;

		/* Compost bin inventory */
        if (SLOT_COUNT != tileInventoryAttachment.getSizeInventory())
        {
            LogHelper.error("Mismatched slot count in container(" + SLOT_COUNT + ") and CompostBinInventory ("
                    + tileInventoryAttachment.getSizeInventory() + ")");
        }
        for (int row = 0; row < 2; row++)
        {
            for (int col = 0; col < 4; col++)
            {
                int slotIndex = col + row * 4;
                addSlotToContainer(new SlotCompost(tileInventoryAttachment, slotIndex, COMPOST_SLOT_XPOS + col * 18, COMPOST_SLOT_YPOS + row * 18));
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

    /* Nothing to do, this is a furnace type container */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        super.onContainerClosed(playerIn);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int sourceSlotIndex)
    {
        LogHelper.info("ContainerInventoryCompostBin: transferStackInSlot called.");
        Slot sourceSlot = (Slot) inventorySlots.get(sourceSlotIndex);
        if (sourceSlot == null || !sourceSlot.getHasStack())
        {
            return null;
        }
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (sourceSlotIndex >= SLOT_COUNT && sourceSlotIndex < 36 + SLOT_COUNT)
        {
            if (tileInventoryAttachment.isItemValidForSlot(sourceSlotIndex, sourceStack))
            {
                // This is a vanilla container slot so merge the stack into the composting bin inventory
                if (!mergeItemStack(sourceStack, 0, SLOT_COUNT, false))
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }
        // Check if the slot clicked is a compost bin container slot
        else if (sourceSlotIndex >= 0 && sourceSlotIndex < SLOT_COUNT)
        {
            // This is a compost bin slot so merge the stack into the players inventory
            if (!mergeItemStack(sourceStack, SLOT_COUNT, 36 + SLOT_COUNT, false))
            {
                return null;
            }
        }
        else
        {
            LogHelper.error("Invalid slotIndex:" + sourceSlotIndex);
            return null;
        }

        // If stack size == 0 (the entire stack was moved) set slot contents to null
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
        return tileInventoryAttachment.isUseableByPlayer(playerIn);
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
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        boolean allFieldsHaveChanged = false;
        boolean fieldHasChanged[] = new boolean[tileInventoryAttachment.getFieldCount()];
        if (cachedFields == null)
        {
            cachedFields = new int[tileInventoryAttachment.getFieldCount()];
            allFieldsHaveChanged = true;
        }
        for (int i = 0; i < cachedFields.length; ++i)
        {
            if (allFieldsHaveChanged || cachedFields[i] != tileInventoryAttachment.getField(i))
            {
                cachedFields[i] = tileInventoryAttachment.getField(i);
                fieldHasChanged[i] = true;
            }
        }

        // go through the list of crafters (players using this container) and update them if necessary
        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            for (int fieldID = 0; fieldID < tileInventoryAttachment.getFieldCount(); ++fieldID)
            {
                if (fieldHasChanged[fieldID])
                {
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
    public void updateProgressBar(int id, int data)
    {
        tileInventoryAttachment.setField(id, data);
    }

    // SlotFertilizer is a slot for fertilizer items
    public class SlotCompost extends Slot
    {
        public SlotCompost(IInventory inventoryIn, int index, int xPosition, int yPosition)
        {
            super(inventoryIn, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given item into this slot
        @Override
        public boolean isItemValid(ItemStack stack)
        {
            return !(stack.getItem() == ARKCraftItems.longneck_rifle);
        }
    }
}