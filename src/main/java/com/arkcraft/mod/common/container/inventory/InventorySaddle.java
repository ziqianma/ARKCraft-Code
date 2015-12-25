package com.arkcraft.mod.common.container.inventory;

import com.arkcraft.mod2.common.items.ItemDinosaurSaddle;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

/***
 * 
 * @author wildbill22
 *
 */
public class InventorySaddle extends InventoryBasic {
    public InventorySaddle() {
		super("Dino Saddle", false, 1);
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
	 */
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return stack.getItem() instanceof ItemDinosaurSaddle;
	}
}
