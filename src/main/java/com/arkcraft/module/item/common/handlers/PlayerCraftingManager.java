package com.arkcraft.module.item.common.handlers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.arkcraft.module.item.common.blocks.ARKCraftBlocks;
import com.arkcraft.module.item.common.items.ARKCraftItems;

/**
 * @author wildbill22 Notes about adding recipes: 1) If a block has meta data:
 *         a) If not enter in ItemStack as 3rd param, it is set to 0 b) If
 *         entered, then just a stack with that meta will match c) If
 *         ARKShapelessRecipe.ANY (32767) is used, all the different meta types
 *         for the block will match 2) Do not have two recipes for the same
 *         ItemStack, only the first will be used
 */
public class PlayerCraftingManager extends ARKCraftingManager
{

	private static PlayerCraftingManager instance = null;

	public static PlayerCraftingManager getInstance()
	{
		if (instance == null)
		{
			instance = new PlayerCraftingManager();
		}
		return instance;
	}

	public PlayerCraftingManager()
	{
		super();
		instance = this;
	}

	public static void registerPlayerCraftingRecipes()
	{

		// Compost Bin
		getInstance().addShapelessRecipe(
				new ItemStack(Item.getItemFromBlock(ARKCraftBlocks.compost_bin), 1),
				new ItemStack(ARKCraftItems.wood, 50), new ItemStack(ARKCraftItems.thatch, 15),
				new ItemStack(ARKCraftItems.fiber, 12));

		// Crop Plot
		getInstance().addShapelessRecipe(
				new ItemStack(Item.getItemFromBlock(ARKCraftBlocks.crop_plot), 1),
				new ItemStack(ARKCraftItems.wood, 40), new ItemStack(ARKCraftItems.thatch, 20),
				new ItemStack(ARKCraftItems.fiber, 30), new ItemStack(ARKCraftItems.rock, 50));

		// Mortar and Pestle
		getInstance().addShapelessRecipe(
				new ItemStack(Item.getItemFromBlock(ARKCraftBlocks.pestle), 1),
				new ItemStack(ARKCraftItems.hide, 15), new ItemStack(ARKCraftItems.rock, 65));

		// Smithy
		getInstance().addShapelessRecipe(
				new ItemStack(Item.getItemFromBlock(ARKCraftBlocks.smithy), 1),
				new ItemStack(ARKCraftItems.metal, 5), new ItemStack(ARKCraftItems.rock, 50),
				new ItemStack(ARKCraftItems.wood, 30), new ItemStack(ARKCraftItems.hide, 20));

		// Forge
		getInstance().addShapelessRecipe(
				new ItemStack(Item.getItemFromBlock(ARKCraftBlocks.refining_forge), 1),
				new ItemStack(ARKCraftItems.flint, 5), new ItemStack(ARKCraftItems.rock, 125),
				new ItemStack(ARKCraftItems.wood, 20), new ItemStack(ARKCraftItems.hide, 65),
				new ItemStack(ARKCraftItems.fiber, 40));

		// Slingshot
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.slingshot, 1),
				new ItemStack(ARKCraftItems.hide, 1), new ItemStack(ARKCraftItems.wood, 5),
				new ItemStack(ARKCraftItems.fiber, 20));

		// Spear
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.spear, 1),
				new ItemStack(ARKCraftItems.flint, 2), new ItemStack(ARKCraftItems.wood, 8),
				new ItemStack(ARKCraftItems.fiber, 12));

		// Wooden Club
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.wooden_club, 1),
				new ItemStack(ARKCraftItems.wood, 4), new ItemStack(ARKCraftItems.fiber, 15));

		// Tranq Arrow
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.tranq_arrow, 1),
				new ItemStack(ARKCraftItems.stone_arrow, 1),
				new ItemStack(ARKCraftItems.narcotics, 1));

		// Stone Arrow
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.stone_arrow, 1),
				new ItemStack(ARKCraftItems.fiber, 2), new ItemStack(ARKCraftItems.thatch, 2),
				new ItemStack(ARKCraftItems.flint, 1));

		// Cloth Armor
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.clothChest, 1),
				new ItemStack(ARKCraftItems.fiber, 40));
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.clothLegs, 1),
				new ItemStack(ARKCraftItems.fiber, 50));
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.clothHelm, 1),
				new ItemStack(ARKCraftItems.fiber, 10));
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.clothBoots, 1),
				new ItemStack(ARKCraftItems.fiber, 25), new ItemStack(ARKCraftItems.hide, 6));

		// Hide Armor
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.hideBoots, 1),
				new ItemStack(ARKCraftItems.hide, 12), new ItemStack(ARKCraftItems.fiber, 5));
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.hideLegs, 1),
				new ItemStack(ARKCraftItems.hide, 25), new ItemStack(ARKCraftItems.fiber, 10));
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.hideChest, 1),
				new ItemStack(ARKCraftItems.hide, 20), new ItemStack(ARKCraftItems.fiber, 8));
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.hideHelm, 1),
				new ItemStack(ARKCraftItems.hide, 15), new ItemStack(ARKCraftItems.fiber, 6));

		// Stone Pick
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.stone_pick, 1),
				new ItemStack(ARKCraftItems.rock, 1), new ItemStack(ARKCraftItems.wood, 1),
				new ItemStack(ARKCraftItems.thatch, 10));

		// Stone Hatchet
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.stone_hatchet, 1),
				new ItemStack(ARKCraftItems.thatch, 10), new ItemStack(ARKCraftItems.flint, 1),
				new ItemStack(ARKCraftItems.wood, 1));
	}
}
