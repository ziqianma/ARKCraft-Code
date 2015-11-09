package com.arkcraft.mod.common.handlers;

import com.arkcraft.mod.common.blocks.ARKCraftBlocks;
import com.arkcraft.mod.common.items.ARKCraftItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RecipeHandler {

	public RecipeHandler() {
		
	}
	
	public static void registerVanillaCraftingRecipes() {
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.chitinHelm),
					"AAA",
					"A A",
					'A', ARKCraftItems.chitin);
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.chitinChest),
					"A A", "AAA", "AAA", 'A', ARKCraftItems.chitin);
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.chitinLegs),
					"AAA", "A A", "A A", 'A', ARKCraftItems.chitin);
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.chitinBoots),
					"   ", "A A", "A A", 'A', ARKCraftItems.chitin);
			//Iron Pike
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.ironPike),
					"  A", 
					" B ", 
					"B  ", 
					'A', Items.iron_ingot, 
					'B', Items.stick);
			
			//Stone Spear
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.spear),
					"  A", 
					" B ", 
					"B  ", 
					'A', Blocks.stone, 
					'B', Items.stick);
			
			//Cobble Ball
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.rock, 4),
					" B ", 
					"BAB", 
					" B ", 
					'B', Blocks.stone, 
					'A', Blocks.cobblestone);
			
			//Slingshot
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.slingshot),
					"ACA", 
					"B B", 
					" B ", 
					'A', ARKCraftItems.fiber,
					'B', Items.stick,
					'C', Items.leather);
			
			//Motar and Pestle
			GameRegistry.addRecipe(new ItemStack(ARKCraftBlocks.pestle),
					"   ", 
					"ABA", 
					" A ", 
					'A', Blocks.stone, 
					'B', Blocks.cobblestone);
			
			//Smithy
			GameRegistry.addRecipe(new ItemStack(ARKCraftBlocks.smithy),
					"AAA", 
					"B B", 
					"B B", 
					'A', Blocks.iron_block, 
					'B', Blocks.planks);
	
			//Tranc Arrow
			GameRegistry.addShapelessRecipe(new ItemStack(ARKCraftItems.tranq_arrow), ARKCraftItems.narcotics, Items.arrow);
			
			
			
			//Pistol
			//GameRegistry.addRecipe(new ItemStack(ARKCraftItems.),
			//		"AAA", 
			//		"A  ", 
			//		"   ", 
			//		'A', Items.iron_ingot); 

			//Ammo
			//GameRegistry.addShapelessRecipe(new ItemStack(ARKCraftItems. ,3), Items.gunpowder, Items.iron_ingot);
					
			/*
			GameRegistry.addShapelessRecipe(new ItemStack(ARKCraftItems.narcotics, 1), new ItemStack(Items.bowl), new ItemStack(ARKCraftItems.narcoBerry), new ItemStack(ARKCraftItems.fiber,1));
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.stoneSpear), "B  ", " A ", "  A", 'A', new ItemStack(Items.stick), 'B', new ItemStack(Blocks.stone));
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.ironPike), "B  ", " A ", "  A", 'A', new ItemStack(Items.stick), 'B', new ItemStack(Items.iron_ingot));
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.rock), "BB ", "BB ", 'B', new ItemStack(Blocks.cobblestone));
			*/
//			GameRegistry.addShapelessRecipe(new ItemStack(ARKCraftItems.saddle_small, 1), Items.leather, Items.iron_ingot,
//					ARKCraftItems.fiber, ARKCraftItems.fiber, ARKCraftItems.fiber, ARKCraftItems.fiber, ARKCraftItems.fiber);
	}	
}
