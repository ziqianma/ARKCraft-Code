package com.arkcraft.module.items.common.general;

import net.minecraft.item.ItemSaddle;

import com.arkcraft.module.creature.common.entity.SaddleType;

public class ItemDinosaurSaddle extends ItemSaddle
{
	private SaddleType type;

	public ItemDinosaurSaddle()
	{
		super();
	}

	public void setSaddleType(SaddleType type)
	{
		this.type = type;
	}

	public SaddleType getSaddleType()
	{
		return this.type;
	}
}
