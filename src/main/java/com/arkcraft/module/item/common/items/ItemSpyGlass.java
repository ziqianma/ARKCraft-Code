package com.arkcraft.module.item.common.items;

import java.util.Random;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.arkcraft.module.item.common.items.weapons.component.RangedComponent;
import com.arkcraft.module.item.common.items.weapons.handlers.IItemWeapon;

public class ItemSpyGlass extends Item implements IItemWeapon
{
	public ItemSpyGlass()
	{
		super();
		this.setMaxStackSize(1);
	}

	@Override
	public boolean ifCanScope()
	{
		return true;
	}

	@Override
	public Random getItemRand()
	{
		return null;
	}

	@Override
	public RangedComponent getRangedComponent()
	{
		return null;
	}
}
