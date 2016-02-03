package com.arkcraft.module.creature.common.container.test;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.arkcraft.module.creature.common.entity.EntityARKCreature;

/**
 * @author Lewis_McReu
 */
public class ContainerInventoryTaming extends Container implements
		IContainerScrollable
{
	public static final int SLOT_SPACING = 18;
	public static final int SLOT_START_X = 9;
	public static final int CREATURE_SLOT_START_Y = 79;
	public static final int PLAYER_SLOT_START_Y = 119;
	public static final int PLAYER_SLOT_HOTBAR_START_Y = 195;

	private static final int creatureSlotWidth = 9;
	private static final int creatureSlotHeight = 3;

	private EntityPlayer player;
	private EntityARKCreature creature;
	private int maxCreatureSlots;
	public final int requiredCreatureSlots;

	private int scrollingOffset = 0;

	public ContainerInventoryTaming(EntityPlayer player, EntityARKCreature creature)
	{
		super();
		maxCreatureSlots = creature.getSizeInventory() < getScrollableSlotsCount() ? creature
				.getSizeInventory() : getScrollableSlotsCount();
		requiredCreatureSlots = creature.getSizeInventory();
		this.player = player;
		this.creature = creature;
		addPlayerSlots();
		addCreatureSlots();
	}

	private void addCreatureSlots()
	{
		for (int i = 0; i < maxCreatureSlots; i++)
		{
			Slot slot = new SlotScrolling(
					creature,
					i,
					SLOT_START_X + i % getScrollableSlotsWidth() * SLOT_SPACING,
					CREATURE_SLOT_START_Y + i / getScrollableSlotsWidth() * SLOT_SPACING,
					this);
			this.addSlotToContainer(slot);
		}
	}

	private void addPlayerSlots()
	{
		for (int i = 0; i < player.inventory.getSizeInventory() - 4; i++)
		{
			Slot slot;
			if (i < 9) slot = new Slot(player.inventory, i,
					SLOT_START_X + i % 9 * SLOT_SPACING,
					PLAYER_SLOT_HOTBAR_START_Y + i / 9 * SLOT_SPACING);
			else slot = new Slot(player.inventory, i,
					SLOT_START_X + i % 9 * SLOT_SPACING,
					PLAYER_SLOT_START_Y + i / 9 * SLOT_SPACING);
			this.addSlotToContainer(slot);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return playerIn.equals(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack stack = slot.getStack();
			if (stack != null)
			{
				itemstack = stack.copy();
				if (index < 36)
				{
					if (!this.mergeItemStack(stack, 36, 36 + maxCreatureSlots,
							false)) return null;
					// else this.player.inventory.setInventorySlotContents(
					// slot.getSlotIndex(), null);
				}
				else
				{
					if (!this.mergeItemStack(stack, 0, 36, false)) return null;
				}
				this.putStackInSlot(slot.slotNumber, null);
			}
		}
		return itemstack;
	}

	@Override
	public int getScrollingOffset()
	{
		return this.scrollingOffset;
	}

	@Override
	public void scroll(int offset)
	{
		int newScrollingOffset = scrollingOffset + offset;
		if (isValidOffset(newScrollingOffset))
		{
			scrollingOffset = newScrollingOffset;
			refreshLists();
		}
	}

	private void refreshLists()
	{
		for (int i = 0; i < inventoryItemStacks.size(); i++)
		{
			Slot slot = (Slot) inventorySlots.get(i);
			if (slot instanceof SlotScrolling)
			{
				inventoryItemStacks.set(i, slot.getStack());
				((SlotScrolling) slot).refresh();
			}
		}
	}

	private boolean isValidOffset(int offset)
	{
		int maxOffset = getMaxOffset();
		return canScroll() && offset >= 0 && offset <= maxOffset;
	}

	public boolean canScroll()
	{
		return this.maxCreatureSlots < requiredCreatureSlots;
	}

	@Override
	public int getScrollableSlotsWidth()
	{
		return creatureSlotWidth;
	}

	@Override
	public int getScrollableSlotsHeight()
	{
		return creatureSlotHeight;
	}

	@Override
	public int getScrollableSlotsCount()
	{
		return creatureSlotWidth * creatureSlotHeight;
	}

	@Override
	public int getRequiredSlotsCount()
	{
		return this.requiredCreatureSlots;
	}

	@Override
	public int getMaxOffset()
	{
		return requiredCreatureSlots / getScrollableSlotsWidth() - getScrollableSlotsHeight() + 1;
	}

	@Override
	public double getRelativeScrollingOffset()
	{
		return (double) this.scrollingOffset / (double) getMaxOffset();
	}
}