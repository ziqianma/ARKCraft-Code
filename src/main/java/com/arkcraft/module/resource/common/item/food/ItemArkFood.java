package com.arkcraft.module.resource.common.item.food;

import net.minecraft.item.ItemFood;

public abstract class ItemArkFood extends ItemFood implements DinoFood
{
	private CreatureFoodType type;

	public ItemArkFood(int amount, CreatureFoodType type)
	{
		super(amount, false);
		this.type = type;
	}
}
