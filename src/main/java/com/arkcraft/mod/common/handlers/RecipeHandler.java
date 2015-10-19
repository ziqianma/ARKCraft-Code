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
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.cobble_ball, 4),
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
			GameRegistry.addRecipe(new ItemStack(ARKCraftItems.cobble_ball), "BB ", "BB ", 'B', new ItemStack(Blocks.cobblestone));
			*/
//			GameRegistry.addShapelessRecipe(new ItemStack(ARKCraftItems.saddle_small, 1), Items.leather, Items.iron_ingot,
//					ARKCraftItems.fiber, ARKCraftItems.fiber, ARKCraftItems.fiber, ARKCraftItems.fiber, ARKCraftItems.fiber);
	}
	
	public static void registerPestleCraftingRecipes() {
		PestleCraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(ARKCraftItems.narcotics, 1),
				new ItemStack(Items.bowl), 
				new ItemStack(ARKCraftItems.narcoBerry)
				);
		PestleCraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(ARKCraftItems.gun_powder, 1),
				new ItemStack(Blocks.stone), 
				new ItemStack(ARKCraftItems.narcoBerry)
				);
	}

	public static void registerSmithyCraftingRecipes() {		
		SmithyCraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(ARKCraftItems.saddle_small, 1), 
				new ItemStack(Items.leather, 15), 
				new ItemStack(Items.iron_ingot, 10), 
				new ItemStack(ARKCraftItems.fiber, 15)
				);
		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.saddle_medium, 1), 
				new ItemStack(Items.leather, 48),
				new ItemStack(Items.iron_ingot, 32), 
				new ItemStack(ARKCraftItems.fiber, 32),
				new ItemStack(Items.diamond, 1)
				);
		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.saddle_large, 1), 
				new ItemStack(ARKCraftItems.chitin, 32),
				new ItemStack(Items.iron_ingot, 16), 
				new ItemStack(ARKCraftItems.fiber, 32),
				new ItemStack(Items.diamond, 4)
				);
//		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModItems.boneHelm, 1),
//				new ItemStack(ModItems.trex_skull, 1),
//				new ItemStack(ModItems.bones_large, 5)
//				);
//		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModItems.boneChest, 1),
//				new ItemStack(Items.bone, 16),
//				new ItemStack(ModItems.bones_large, 8)
//				);
//		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModItems.boneLegs, 1),
//				new ItemStack(Items.bone, 16),
//				new ItemStack(ModItems.bones_large, 7)
//				);
//		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ModItems.boneBoots, 1),
//				new ItemStack(Items.bone, 16),
//				new ItemStack(ModItems.bones_large, 7)
//				);
		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.longneck_rifle, 1),
				new ItemStack(Items.iron_ingot, 32),
				new ItemStack(Blocks.glass, 1)
				);
		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.shotgun, 1),
				new ItemStack(Items.iron_ingot, 32),
				new ItemStack(Blocks.log, 4)
				);
		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.rocket_launcher, 1),
				new ItemStack(Items.iron_ingot, 48),
				new ItemStack(Items.diamond, 4),
				new ItemStack(Items.redstone, 16)
				);
		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.explosive_ball, 1),
				new ItemStack(Items.iron_ingot, 3),
				new ItemStack(Items.gunpowder, 4)
				);
		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.tranq_gun, 1),
				new ItemStack(Items.iron_ingot, 7),
				new ItemStack(Blocks.planks, 10, 0), // Oak planks
				new ItemStack(ARKCraftItems.fiber, 35)
				);
		SmithyCraftingManager.getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.crossbow, 1),
				new ItemStack(Items.iron_ingot, 7),
				new ItemStack(Blocks.planks, 10, 0), // Oak planks
				new ItemStack(ARKCraftItems.fiber, 35)
				);
	}
}
