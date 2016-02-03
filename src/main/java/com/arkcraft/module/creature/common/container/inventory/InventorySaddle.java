package com.arkcraft.module.creature.common.container.inventory;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

import com.arkcraft.module.blocks.common.items.ItemDinosaurSaddle;

/**
 * @author wildbill22
 */
public class InventorySaddle extends InventoryBasic
{
	public InventorySaddle()
	{
		super("Dino Saddle", false, 1);
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot.
	 */
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return stack.getItem() instanceof ItemDinosaurSaddle;
	}
}
