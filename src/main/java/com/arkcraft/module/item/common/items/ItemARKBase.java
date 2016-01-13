package com.arkcraft.module.item.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author Vastatio
 */
/*
 * For some reason, minecraft doesnt put args inside its list. //GEGY: This is
 * just decompilation
 */
public class ItemARKBase extends Item
{
	public ItemARKBase()
	{
		super();
	}

	public ItemARKBase(String name)
	{
		this();
		this.setUnlocalizedName(name);
	}

	public String[] tooltips;

	// ticks that this food will keep a dino unconscious
	public static int getItemTorporTime(ItemStack stack)
	{
		if (stack != null)
		{
			if (stack.getItem() instanceof ItemARKFood)
			{
				if (stack.getItem() == (Item) ARKCraftItems.narcotics) { return 1000; }
			}
		}
		return 0;
	}
}
