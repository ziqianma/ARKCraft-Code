package com.arkcraft.mod.core.handlers;

import net.minecraft.init.Blocks;
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
			//Iron Pike
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.ironPike),
					"  A", 
					" B ", 
					"B  ", 
					'A', Items.iron_ingot, 
					'B', Items.stick);
			
			//Stone Spear
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.stoneSpear),
					"  A", 
					" B ", 
					"B  ", 
					'A', Blocks.stone, 
					'B', Items.stick);
			
			//Cobble Ball
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.cobble_ball, 4),
					" B ", 
					"BAB", 
					" B ", 
					'B', Blocks.stone, 
					'A', Blocks.cobblestone);
			
			//Slingshot
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.slingshot),
					"ACA", 
					"B B", 
					" B ", 
					'A', GlobalAdditions.fiber,
					'B', Items.stick,
					'C', Items.leather);
			
			//Motar and Pestle
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.pestle),
					"   ", 
					"ABA", 
					" A ", 
					'A', Blocks.stone, 
					'B', Blocks.cobblestone);
			
			//Smithy
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.smithy),
					"AAA", 
					"B B", 
					"B B", 
					'A', Blocks.iron_block, 
					'B', Blocks.planks);
	
			//Tranc Arrow
			GameRegistry.addShapelessRecipe(new ItemStack(GlobalAdditions.tranq_arrow), GlobalAdditions.narcotics, Items.arrow);
			
			
			
			//Pistol
			//GameRegistry.addRecipe(new ItemStack(GlobalAdditions.),
			//		"AAA", 
			//		"A  ", 
			//		"   ", 
			//		'A', Items.iron_ingot); 

			//Ammo
			//GameRegistry.addShapelessRecipe(new ItemStack(GlobalAdditions. ,3), Items.gunpowder, Items.iron_ingot);
					
			/*
			GameRegistry.addShapelessRecipe(new ItemStack(GlobalAdditions.narcotics, 1), new ItemStack(Items.bowl), new ItemStack(GlobalAdditions.narcoBerry), new ItemStack(GlobalAdditions.fiber,1));
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.stoneSpear), "B  ", " A ", "  A", 'A', new ItemStack(Items.stick), 'B', new ItemStack(Blocks.stone));
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.ironPike), "B  ", " A ", "  A", 'A', new ItemStack(Items.stick), 'B', new ItemStack(Items.iron_ingot));
			GameRegistry.addRecipe(new ItemStack(GlobalAdditions.cobble_ball), "BB ", "BB ", 'B', new ItemStack(Blocks.cobblestone));
			*/
//			GameRegistry.addShapelessRecipe(new ItemStack(GlobalAdditions.saddle_small, 1), Items.leather, Items.iron_ingot, 
//					GlobalAdditions.fiber, GlobalAdditions.fiber, GlobalAdditions.fiber, GlobalAdditions.fiber, GlobalAdditions.fiber);
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
