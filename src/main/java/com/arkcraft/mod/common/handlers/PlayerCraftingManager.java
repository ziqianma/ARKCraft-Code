package com.arkcraft.mod.common.handlers;

import com.arkcraft.mod.common.blocks.ARKCraftBlocks;
import com.arkcraft.mod.common.items.ARKCraftItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author wildbill22
 * Notes about adding recipes:
 * 1) If a block has meta data:
 *    a) If not enter in ItemStack as 3rd param, it is set to 0
 *    b) If entered, then just a stack with that meta will match
 *    c) If ARKShapelessRecipe.ANY (32767) is used, all the different meta types for the block will match
 * 2) Do not have two recipes for the same ItemStack, only the first will be used
 *
 */
public class PlayerCraftingManager extends ARKCraftingManager{

	private static PlayerCraftingManager instance = null;
    
	public static PlayerCraftingManager getInstance() {
		if (instance == null)
			instance = new PlayerCraftingManager();
		return instance; 
	}
	    
	public PlayerCraftingManager() {
		super();
		instance = this; 
	}
	
	public static void registerPlayerCraftingRecipes() {
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftBlocks.compost_bin, 1),
				new ItemStack(Blocks.log, 50, ARKShapelessRecipe.ANY), // Any Wood
				new ItemStack(ARKCraftItems.thatch, 10), 
				new ItemStack(ARKCraftItems.fiber, 12)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftBlocks.crop_plot, 1),
				new ItemStack(Blocks.log, 20, ARKShapelessRecipe.ANY), // Any Wood
				new ItemStack(ARKCraftItems.thatch, 10), 
				new ItemStack(ARKCraftItems.fiber, 15),
				new ItemStack(Blocks.cobblestone, 25)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.slingshot, 1),
				new ItemStack(Items.leather, 1), 
				new ItemStack(Blocks.log, 5, ARKShapelessRecipe.ANY), // Any Wood
				new ItemStack(ARKCraftItems.fiber, 20)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.spear, 1),
				new ItemStack(Items.flint, 2), 
				new ItemStack(Blocks.log, 8, ARKShapelessRecipe.ANY), // Any Wood
				new ItemStack(ARKCraftItems.fiber, 12)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.spy_glass, 1),
				new ItemStack(Blocks.log, 5, ARKShapelessRecipe.ANY), // Any Wood
				new ItemStack(Items.leather, 10), 
				new ItemStack(ARKCraftItems.fiber, 10),
				new ItemStack(Items.diamond, 2)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftBlocks.pestle, 1),
				new ItemStack(Items.leather, 15), 
				new ItemStack(Blocks.cobblestone, 65)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftBlocks.smithy, 1),
				new ItemStack(Items.iron_ingot, 15), 
				new ItemStack(Blocks.cobblestone, 50),
				new ItemStack(Blocks.log, 30, ARKShapelessRecipe.ANY), // Any Wood
				new ItemStack(Items.leather, 20) 
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.tranq_arrow, 1),
				new ItemStack(ARKCraftItems.stone_arrow, 1),
				new ItemStack(ARKCraftItems.narcotics, 1)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.clothChest, 1),
				new ItemStack(ARKCraftItems.fiber, 40)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.clothLegs, 1),
				new ItemStack(ARKCraftItems.fiber, 50)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.clothHelm, 1),
				new ItemStack(ARKCraftItems.fiber, 10),
				new ItemStack(Items.leather, 4)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.clothBoots, 1),
				new ItemStack(ARKCraftItems.fiber, 25),
				new ItemStack(Items.leather, 6)
				);
//		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.hideChest, 1),
//				new ItemStack(ARKCraftItems.fiber, 8),
//				new ItemStack(Items.leather, 20)
//				);
//		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.hideLegs, 1),
//				new ItemStack(ARKCraftItems.fiber, 10),
//				new ItemStack(Items.leather, 25)
//				);
//		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.hideHelm, 1),
//				new ItemStack(ARKCraftItems.fiber, 6),
//				new ItemStack(Items.leather, 15)
//				);
//		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.hideBoots, 1),
//				new ItemStack(ARKCraftItems.fiber, 5),
//				new ItemStack(Items.leather, 10)
//				);
		
		// TODO: Might want to move these to the Smithy!
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.boneChest, 1),
				new ItemStack(Items.bone, 16),
				new ItemStack(ARKCraftItems.chitin, 8) // Should be large bone
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.boneLegs, 1),
				new ItemStack(Items.bone, 16),
				new ItemStack(ARKCraftItems.chitin, 7) // Should be large bone
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.boneHelm, 1),
				new ItemStack(Items.bone, 5), // Should be T-Rex Skull
				new ItemStack(ARKCraftItems.chitin, 5) // Should be large bone
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.boneBoots, 1),
				new ItemStack(Items.bone, 16),
				new ItemStack(ARKCraftItems.chitin, 7) // Should be large bone
				);
	}
}
