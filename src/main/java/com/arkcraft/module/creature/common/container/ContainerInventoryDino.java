package com.arkcraft.module.creature.common.container;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.common.container.inventory.InventoryDino;
import com.arkcraft.module.creature.common.entity.EntityTameableDinosaur;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * @author wildbill22
 */
public class ContainerInventoryDino extends Container
{

    private IInventory invDino;
    private InventoryBasic invSaddle;
    private EntityPlayer player;
    private int DINO_SLOT_COUNT;

    // some [x,y] coordinates of graphical elements (also used elsewhere)
    public static final int DINO_INVENTORY_XPOS = 62;
    public static final int DINO_INVENTORY_YPOS = 27;

    public ContainerInventoryDino(IInventory invPlayer, final IInventory invDino, final EntityTameableDinosaur dino)
    {
        LogHelper.info("ContainerInventoryDino: constructor called.");
        this.invDino = invDino;
        DINO_SLOT_COUNT = dino.saddleType.getInventorySize();
        invSaddle = dino.invSaddle;
        invSaddle.openInventory(player);
        ((InventoryDino) invDino).openInventory(player);

		/* Hotbar inventory */
        final int HOTBAR_YPOS = 219;
        for (int col = 0; col < 9; col++)
        {
            addSlotToContainer(new Slot(invPlayer, col, 8 + col * 18, HOTBAR_YPOS));
        }
		
		/* Player inventory */
        final int PLAYER_INVENTORY_YPOS = 161;
        for (int row = 0; row < 3; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                int slotIndex = col + row * 9 + 9;
                addSlotToContainer(new Slot(invPlayer, slotIndex, 8 + col * 18, PLAYER_INVENTORY_YPOS + row * 18));
            }
        }
		
		/* Dino inventory */
        if (DINO_SLOT_COUNT != invDino.getSizeInventory())
        {
            LogHelper.error("Mismatched slot count in container(" + DINO_SLOT_COUNT + ") and DinoInventory (" + invDino.getSizeInventory() + ")");
        }
        if (dino.isTamed())
        {
            final int NUM_ROWS_DINO_INV = 5;
            for (int row = 0; row < NUM_ROWS_DINO_INV; row++)
            {
                for (int col = 0; col < 5; col++)
                {
                    int slotIndex = col + row * NUM_ROWS_DINO_INV;
                    addSlotToContainer(new Slot(invDino, slotIndex, DINO_INVENTORY_XPOS + col * 18,
                            DINO_INVENTORY_YPOS + row * 18));
                }
            }
        }
		
		/* Saddle inventory */
        final int SADDLE_INVENTORY_XPOS = 8;
        final int SADDLE_INVENTORY_YPOS = 129;
        addSlotToContainer(new SlotSaddle(invSaddle, 0, SADDLE_INVENTORY_XPOS, SADDLE_INVENTORY_YPOS));
    }

    @Override
    public void addCraftingToCrafters(ICrafting listener)
    {
        LogHelper.info("ContainerInventoryDino: addCraftingToCrafters called.");
        super.addCraftingToCrafters(listener);
        listener.func_175173_a(this, invDino);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
//    @Override
//    public void detectAndSendChanges() {
//    	super.detectAndSendChanges();
//    }
    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.invDino.isUseableByPlayer(playerIn);
    }

    // Called when you shift click
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int sourceSlotIndex)
    {
        LogHelper.info("ContainerInventoryDino: transferStackInSlot called.");
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
            // This is a vanilla container slot so merge the stack into the dodo inventory
            if (!mergeItemStack(sourceStack, 36, 36 + DINO_SLOT_COUNT, false))
            {
                return null;
            }
        }
        // Check if the slot clicked is a dodo container slot
        else if (sourceSlotIndex >= 36 && sourceSlotIndex < 36 + DINO_SLOT_COUNT)
        {
            // This is a dino slot so merge the stack into the players inventory
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

        sourceSlot.onPickupFromSlot(player, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        if (playerIn.worldObj.isRemote)
        {
            LogHelper.info("ContainerInventoryDino: onContainerClosed called on client.");
        }
        else
        {
            LogHelper.info("ContainerInventoryDino: onContainerClosed called on server.");
        }

        super.onContainerClosed(playerIn);

        invSaddle.closeInventory(playerIn);
        ((InventoryDino) invDino).closeInventory(playerIn);

        this.crafters.remove(playerIn);
    }

    // SlotSaddle is a slot for saddle items
    public class SlotSaddle extends Slot
    {
        public SlotSaddle(IInventory inventoryIn, int index, int xPosition, int yPosition)
        {
            super(inventoryIn, index, xPosition, yPosition);
        }

        // if this function returns false, the player won't be able to insert the given item into this slot
        @Override
        public boolean isItemValid(ItemStack stack)
        {
            if (stack != null)
            {
//				if (stack.getItem() == ((InventorySaddle)inventory).entityDino.saddleType.getSaddleItem()) TODO
                return true;
            }
            return false;
        }
    }
}