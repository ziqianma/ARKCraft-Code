package com.arkcraft.module.item.common.items;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.arkcraft.module.item.common.items.weapons.handlers.IItemWeapon;

public class ItemSpyGlass extends Item implements IItemWeapon
{
	public ItemSpyGlass()
	{
		super();
		this.setMaxStackSize(1);
	}

	@Override
	public Random getItemRand()
	{
		return null;
	}

	@Override
	public boolean canScope(ItemStack stack)
	{
		return true;
	}
}
