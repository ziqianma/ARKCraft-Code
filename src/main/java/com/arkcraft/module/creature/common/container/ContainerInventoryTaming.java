package com.arkcraft.module.creature.common.container;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.common.container.inventory.InventoryTaming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author wildbill22
 */
public class ContainerInventoryTaming extends Container
{

    private InventoryTaming invTaming;
    public InventoryCrafting inputSlots;
    private final int TAMING_SLOT_COUNT = 2;
    public static final int NARCO_SLOT_XPOS = 72;
    public static final int NARCO_SLOT_YPOS = 101;
    public static final int FOOD_SLOT_XPOS = 144;
    public static final int FOOD_SLOT_YPOS = 101;

    // These store cache values, used by the server to only update the client side tile entity when values have changed
    private int[] cachedFields;

    public ContainerInventoryTaming(InventoryPlayer invPlayer, InventoryTaming inventoryTaming, EntityPlayer player)
    {
        this.invTaming = inventoryTaming;
        inventoryTaming.playerTaming = player;
        LogHelper.info("ContainerInventoryTaming: constructor called.");

		/* Taming inventory */
        if (TAMING_SLOT_COUNT != invTaming.getSizeInventory())
        {
            LogHelper.error("Mismatched slot count in container(" + TAMING_SLOT_COUNT + ") and InventoryTaming (" + inputSlots.getSizeInventory() + ")");
        }
        this.addSlotToContainer(new SlotNarco(invTaming, InventoryTaming.NARCO_SLOT, NARCO_SLOT_XPOS, NARCO_SLOT_YPOS)); // Narco input slot
        this.addSlotToContainer(new SlotFood(invTaming, InventoryTaming.FOOD_SLOT, FOOD_SLOT_XPOS, FOOD_SLOT_YPOS));  // Food input slot
			
		/* Hotbar inventory */
        final int PLAYER_INVENTORY_XPOS = 36;
        final int HOTBAR_YPOS = 195;
        for (int col = 0; col < 9; col++)
        {
            addSlotToContainer(new Slot(invPlayer, col, PLAYER_INVENTORY_XPOS + col * 18, HOTBAR_YPOS));
        }
		
		/* Player inventory */
        final int PLAYER_INVENTORY_YPOS = 137;
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                int slotIndex = col + row * 9 + 9;
                addSlotToContainer(new Slot(invPlayer, slotIndex, PLAYER_INVENTORY_XPOS + col * 18, PLAYER_INVENTORY_YPOS + row * 18));
            }
        }
        this.onCraftMatrixChanged(inputSlots);
    }

    @Override
    public void addCraftingToCrafters(ICrafting listener)
    {
//		LogHelper.info("ContainerInventoryTaming: addCraftingToCrafters called.");
        super.addCraftingToCrafters(listener);
        listener.func_175173_a(this, invTaming);
    }

    /* GET ITEMS OUT ONCE CLOSED ???? */
    public void onContainerClosed(EntityPlayer playerIn)
    {
        if (playerIn.worldObj.isRemote)
        {
            LogHelper.info("ContainerTaming: onContainerClosed called on client.");
        }
        else
        {
            LogHelper.info("ContainerTaming: onContainerClosed called on server.");
        }

        super.onContainerClosed(playerIn);
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int sourceSlotIndex)
    {
        LogHelper.info("ARKContainerCropPlot: transferStackInSlot called.");
        Slot sourceSlot = (Slot) inventorySlots.get(sourceSlotIndex);
        if (sourceSlot == null || !sourceSlot.getHasStack())
        {
            return null;
        }
        ItemStack sourceStack = sourceSlot.getStack();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (sourceSlotIndex >= 0 && sourceSlotIndex < 36)
        {
            if (invTaming.isItemValidForNarcoSlot(sourceStack))
            {
                // This is a vanilla container slot so merge the stack into the taming inventory
                if (!mergeItemStack(sourceStack, 36, 36 + InventoryTaming.NARCO_SLOT, false))
                {
                    return null;
                }
            }
            if (invTaming.isItemValidForFoodSlot(sourceStack))
            {
                // This is a vanilla container slot so merge the stack into the taming inventory
                if (!mergeItemStack(sourceStack, 36, 36 + InventoryTaming.FOOD_SLOT, false))
                {
                    return null;
                }
            }
        }
        // Check if the slot clicked is a crop plot container slot
        else if (sourceSlotIndex >= 36 && sourceSlotIndex < 36 + TAMING_SLOT_COUNT)
        {
            // This is a crop plot slot so merge the stack into the players inventory
            if (!mergeItemStack(sourceStack, 0, 36, false))
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
        return invTaming.isUseableByPlayer(playerIn);
    }

    // This is where you check if any values have changed and if so send an update to any clients accessing this container
    // The container itemstacks are tested in Container.detectAndSendChanges, so we don't need to do that
    // We iterate through all of the TileEntity Fields to find any which have changed, and send them.
    // You don't have to use fields if you don't wish to; just manually match the ID in sendProgressBarUpdate with the value in
    //   updateProgressBar()
    // The progress bar values are restricted to shorts.  If you have a larger value (eg int), it's not a good idea to try and split it
    //   up into two shorts because the progress bar values are sent independently, and unless you add synchronization logic at the
    //   receiving side, your int value will be wrong until the second short arrives.  Use a custom packet instead.
    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        boolean allFieldsHaveChanged = false;
        boolean fieldHasChanged[] = new boolean[invTaming.getFieldCount()];
        if (cachedFields == null)
        {
            cachedFields = new int[invTaming.getFieldCount()];
            allFieldsHaveChanged = true;
        }
        for (int i = 0; i < cachedFields.length; ++i)
        {
            if (allFieldsHaveChanged || cachedFields[i] != invTaming.getField(i))
            {
                cachedFields[i] = invTaming.getField(i);
                fieldHasChanged[i] = true;
            }
        }

        // go through the list of crafters (players using this container) and update them if necessary
        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting) this.crafters.get(i);
            for (int fieldID = 0; fieldID < invTaming.getFieldCount(); ++fieldID)
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
        invTaming.setField(id, data);
    }

    // SlotNarco is a slot for narcotics
    public class SlotNarco extends Slot
    {
        public SlotNarco(IInventory inventoryIn, int index, int xPosition, int yPosition)
        {
            super(inventoryIn, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given item into this slot
        @Override
        public boolean isItemValid(ItemStack stack)
        {
            return invTaming.isItemValidForNarcoSlot(stack);
        }
    }

    // SlotFood is a slot for taming food
    public class SlotFood extends Slot
    {
        public SlotFood(IInventory inventoryIn, int index, int xPosition, int yPosition)
        {
            super(inventoryIn, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given item into this slot
        @Override
        public boolean isItemValid(ItemStack stack)
        {
            return invTaming.isItemValidForFoodSlot(stack);
        }
    }
}