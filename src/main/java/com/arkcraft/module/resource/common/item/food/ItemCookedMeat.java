package com.arkcraft.module.resource.common.item.food;

public class ItemCookedMeat extends ItemArkFood
{
	public ItemCookedMeat()
	{
		super(12, CreatureFoodType.CARNIVORE);
	}

	@Override
	public double getFoodValue()
	{
		return 5;
	}

	@Override
	public double getTamingIncrease()
	{
		return 5;
	}
}
