package com.arkcraft.module.item.common.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.arkcraft.module.item.common.items.ARKCraftItems;

public class ForgeHandler
{
	public static void registerForgeRecipes()
	{
		registerMetalRecipe();
		registerFuels();
	}

	public static void registerFuels()
	{
		registerFuel(ARKCraftItems.wood, 600);
		registerFuel(ARKCraftItems.thatch, 140);
		registerFuel(ARKCraftItems.spark_powder, 1200);
	}

	private static void registerMetalRecipe()
	{
		ForgeRecipe r = new ForgeRecipe("metalIngot");
		r.addInputItems(ARKCraftItems.metal, ARKCraftItems.metal);
		r.setOutputItem(ARKCraftItems.metal_ingot);
		r.setBurnTime(1);
		registerRecipe(r.toString(), r);
	}

	private static Map<String, ForgeRecipe> recipes = new HashMap<String, ForgeRecipe>();
	private static Map<Item, Integer> fuels = new HashMap<Item, Integer>();

	public static boolean registerFuel(Item i, int burnTime)
	{
		return fuels.put(i, new Integer(burnTime)) == null;
	}

	public static boolean registerRecipe(String id, ForgeRecipe recipe)
	{
		return recipes.putIfAbsent(id, recipe) != null;
	}

	public static Map<String, ForgeRecipe> getRecipes()
	{
		return recipes;
	}

	public static boolean isValidFuel(Item i)
	{
		return fuels.containsKey(i);
	}

	public static int getBurnTime(Item i)
	{
		return fuels.get(i);
	}

	public static List<ForgeRecipe> findPossibleRecipes(IInventory inv)
	{
		List<ForgeRecipe> list = new ArrayList<ForgeRecipe>();
		for (ForgeRecipe r : recipes.values())
		{
			List<Item> inputs = new ArrayList<Item>(r.getInput());
			for (int i = 0; i < inv.getSizeInventory(); i++)
			{
				ItemStack s = inv.getStackInSlot(i);
				if (s != null)
				{
					ItemStack stack = s.copy();
					Item item = stack.getItem();
					while (stack.stackSize > 0)
					{
						if (inputs.remove(item)) stack.stackSize--;
						else break;
					}
				}
			}
			if (inputs.size() == 0) list.add(r);
		}

		return list;
	}

	public static ForgeRecipe getForgeRecipe(String id)
	{
		return recipes.get(id);
	}

}
