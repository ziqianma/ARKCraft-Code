package com.arkcraft.module.item.common.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.arkcraft.module.item.common.items.weapons.ItemRangedWeapon;

public class SlotMagicBag extends Slot
{
	public SlotMagicBag(IInventory inv, int index, int xPos, int yPos) {
		super(inv, index, xPos, yPos);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return !(stack.getItem() instanceof ItemRangedWeapon);

	}
}