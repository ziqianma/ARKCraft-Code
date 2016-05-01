package com.arkcraft.module.weapon.common.item.ammo;

import net.minecraft.item.Item;

public class ItemProjectile extends Item
{
	public ItemProjectile()
	{
		super();
	}

	@Override
	public String getUnlocalizedName()
	{
		String s = super.getUnlocalizedName();
		return s.substring(s.indexOf('.') + 1);
	}
}
