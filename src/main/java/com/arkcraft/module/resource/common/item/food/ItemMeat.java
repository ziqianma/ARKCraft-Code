package com.arkcraft.module.resource.common.item.food;

public class ItemMeat extends ItemArkFood
{
	public ItemMeat()
	{
		super(4, CreatureFoodType.CARNIVORE);
	}

	@Override
	public double getFoodValue()
	{
		return 10;
	}

	@Override
	public double getTamingIncrease()
	{
		return 15;
	}
}
