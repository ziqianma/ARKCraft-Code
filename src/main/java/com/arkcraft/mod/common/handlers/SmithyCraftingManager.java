package com.arkcraft.mod.common.handlers;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.arkcraft.mod.common.items.ARKCraftItems;

/***
 * 
 * @author wildbill22
 *
 */
public class SmithyCraftingManager extends ARKCraftingManager{
	
	private static SmithyCraftingManager instance = null;
    
	public SmithyCraftingManager() {
		super();
		instance = this; 
	}
	
	public static SmithyCraftingManager getInstance() {
		if (instance == null)
			instance = new SmithyCraftingManager();
		return instance; 
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
