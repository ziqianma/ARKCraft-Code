package com.arkcraft.mod.core.handlers;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.blocks.ModBlocks;
import com.arkcraft.mod.core.items.ModItems;

public class RecipeHandler {

	public RecipeHandler() {
		
	}
	
	public static void registerVanillaCraftingRecipes() {
			GameRegistry.addRecipe(new ItemStack(ModItems.chitinHelm),
					"AAA",
					"A A",
					'A', ModItems.chitin);
			GameRegistry.addRecipe(new ItemStack(ModItems.chitinChest),
					"A A", "AAA", "AAA", 'A', ModItems.chitin);
			GameRegistry.addRecipe(new ItemStack(ModItems.chitinLegs),
					"AAA", "A A", "A A", 'A', ModItems.chitin);
			GameRegistry.addRecipe(new ItemStack(ModItems.chitinBoots),
					"   ", "A A", "A A", 'A', ModItems.chitin);
			//Iron Pike
			GameRegistry.addRecipe(new ItemStack(ModItems.ironPike),
					"  A", 
					" B ", 
					"B  ", 
					'A', Items.iron_ingot, 
					'B', Items.stick);
			
			//Stone Spear
			GameRegistry.addRecipe(new ItemStack(ModItems.spear),
					"  A", 
					" B ", 
					"B  ", 
					'A', Blocks.stone, 
					'B', Items.stick);
			
			//Cobble Ball
			GameRegistry.addRecipe(new ItemStack(ModItems.cobble_ball, 4),
					" B ", 
					"BAB", 
					" B ", 
					'B', Blocks.stone, 
					'A', Blocks.cobblestone);
			
			//Slingshot
			GameRegistry.addRecipe(new ItemStack(ModItems.slingshot),
					"ACA", 
					"B B", 
					" B ", 
					'A', ModItems.fiber,
					'B', Items.stick,
					'C', Items.leather);
			
			//Motar and Pestle
			GameRegistry.addRecipe(new ItemStack(ModBlocks.pestle),
					"   ", 
					"ABA", 
					" A ", 
					'A', Blocks.stone, 
					'B', Blocks.cobblestone);
			
			//Smithy
			GameRegistry.addRecipe(new ItemStack(ModBlocks.smithy),
					"AAA", 
					"B B", 
					"B B", 
					'A', Blocks.iron_block, 
					'B', Blocks.planks);
	
			//Tranc Arrow
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.tranq_arrow), ModItems.narcotics, Items.arrow);
			
			
			
			//Pistol
			//GameRegistry.addRecipe(new ItemStack(ModItems.),
			//		"AAA", 
			//		"A  ", 
			//		"   ", 
			//		'A', Items.iron_ingot); 

			//Ammo
			//GameRegistry.addShapelessRecipe(new ItemStack(ModItems. ,3), Items.gunpowder, Items.iron_ingot);
					
			/*
			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.narcotics, 1), new ItemStack(Items.bowl), new ItemStack(ModItems.narcoBerry), new ItemStack(ModItems.fiber,1));
			GameRegistry.addRecipe(new ItemStack(ModItems.stoneSpear), "B  ", " A ", "  A", 'A', new ItemStack(Items.stick), 'B', new ItemStack(Blocks.stone));
			GameRegistry.addRecipe(new ItemStack(ModItems.ironPike), "B  ", " A ", "  A", 'A', new ItemStack(Items.stick), 'B', new ItemStack(Items.iron_ingot));
			GameRegistry.addRecipe(new ItemStack(ModItems.cobble_ball), "BB ", "BB ", 'B', new ItemStack(Blocks.cobblestone));
			*/
//			GameRegistry.addShapelessRecipe(new ItemStack(ModItems.saddle_small, 1), Items.leather, Items.iron_ingot, 
//					ModItems.fiber, ModItems.fiber, ModItems.fiber, ModItems.fiber, ModItems.fiber);
	}
	
	public static void registerPestleCraftingRecipes() {
		PestleCraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(ModItems.narcotics, 1), 
				new ItemStack(Items.bowl), 
				new ItemStack(ModItems.narcoBerry)
				);
		PestleCraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(ModItems.gun_powder, 1), 
				new ItemStack(Blocks.stone), 
				new ItemStack(ModItems.narcoBerry)
				);
	}

	public static void registerSmithyCraftingRecipes() {		
		SmithyCraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(ModItems.saddle_small, 1), 
				Items.leather, 
				Items.iron_ingot, 
				ModItems.fiber, ModItems.fiber, ModItems.fiber, ModItems.fiber
				);
		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModItems.saddle_medium, 1), 
				Items.leather, Items.leather, 
				Items.iron_ingot, Items.iron_ingot, 
				ModItems.fiber, ModItems.fiber,
				Items.diamond
				);
//		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModItems.saddle_large, 1), new ItemStack(ModItems.chitin, 32), new ItemStack(Items.diamond, 4), new ItemStack(Items.iron_ingot, 16), new ItemStack(ModItems.fiber, 15));
	}
}
