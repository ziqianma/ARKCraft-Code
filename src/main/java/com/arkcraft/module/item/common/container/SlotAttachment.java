package com.arkcraft.module.item.common.container;

import com.arkcraft.module.item.common.items.weapons.ranged.ItemRangedWeapon;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAttachment extends Slot
{
	public SlotAttachment(IInventory inv, int index, int xPos, int yPos) {
		super(inv, index, xPos, yPos);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return !(stack.getItem() instanceof ItemRangedWeapon);

	}	
}