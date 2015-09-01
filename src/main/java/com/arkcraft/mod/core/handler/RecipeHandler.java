package com.arkcraft.mod.core.handler;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;

public class RecipeHandler {

	public RecipeHandler() {
		
	}
	
	public static void registerVanillaCraftingRecipes() {
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.chitinHelm),
					"AAA",
					"A A",
					'A', GlobalAdditions.chitin);
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.chitinChest),
					"A A", "AAA", "AAA", 'A', GlobalAdditions.chitin);
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.chitinLegs),
					"AAA", "A A", "A A", 'A', GlobalAdditions.chitin);
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.chitinBoots),
					"   ", "A A", "A A", 'A', GlobalAdditions.chitin);
			/*
			GameRegistry.addShapelessRecipe(new ItemStack(GlobalAdditions.narcotics, 1), new ItemStack(Items.bowl), new ItemStack(GlobalAdditions.narcoBerry), new ItemStack(GlobalAdditions.fiber,1));
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.stoneSpear), "B  ", " A ", "  A", 'A', new ItemStack(Items.stick), 'B', new ItemStack(Blocks.stone));
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.ironPike), "B  ", " A ", "  A", 'A', new ItemStack(Items.stick), 'B', new ItemStack(Items.iron_ingot));
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.cobble_ball), "BB ", "BB ", 'B', new ItemStack(Blocks.cobblestone));
			*/
			GameRegistry.addShapelessRecipe(new ItemStack(GlobalAdditions.saddle_small, 1), Items.leather, Items.iron_ingot, 
					GlobalAdditions.fiber, GlobalAdditions.fiber, GlobalAdditions.fiber, GlobalAdditions.fiber, GlobalAdditions.fiber);
	}
	
	public static void registerPestleCraftingRecipes() {
		PestleCraftingManager.getInstance().addShapelessRecipe(new ItemStack(GlobalAdditions.narcotics, 1), new ItemStack(Items.bowl), new ItemStack(GlobalAdditions.narcoBerry));
	}

	public static void registerSmithyCraftingRecipes() {
		SmithyCraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(GlobalAdditions.saddle_small, 1), 
				Items.leather, 
				Items.iron_ingot, 
				GlobalAdditions.fiber, GlobalAdditions.fiber, GlobalAdditions.fiber, GlobalAdditions.fiber
				);
		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(GlobalAdditions.saddle_medium, 1), 
				Items.leather, Items.leather, 
				Items.iron_ingot, Items.iron_ingot, 
				GlobalAdditions.fiber, GlobalAdditions.fiber,
				Items.diamond
				);
//		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(GlobalAdditions.saddle_large, 1), new ItemStack(GlobalAdditions.chitin, 32), new ItemStack(Items.diamond, 4), new ItemStack(Items.iron_ingot, 16), new ItemStack(GlobalAdditions.fiber, 15));
	}
}
