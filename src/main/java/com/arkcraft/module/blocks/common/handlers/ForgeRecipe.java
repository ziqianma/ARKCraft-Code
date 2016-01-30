package com.arkcraft.module.blocks.common.handlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;

public class ForgeRecipe
{
	private List<Item> input;
	private Item output;
	private double burnTime;
	private final String id;

	public ForgeRecipe(String id)
	{
		this.id = id;
		input = new ArrayList<Item>();
	}

	public List<Item> getInput()
	{
		return this.input;
	}

	public Item getOutput()
	{
		return this.output;
	}

	public void addInputItems(Item... i)
	{
		for (Item it : i)
			this.input.add(it);
	}

	public void setOutputItem(Item i)
	{
		this.output = i;
	}

	public double getBurnTime()
	{
		return this.burnTime;
	}

	public void setBurnTime(double burnTime)
	{
		this.burnTime = burnTime;
	}

	@Override
	public String toString()
	{
		return this.id;
	}
}
