package com.arkcraft.mod.common.handlers;

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
		
		//Small Saddle
		getInstance().addShapelessRecipe(
				new ItemStack(ARKCraftItems.saddle_small, 1), 
				new ItemStack(Items.leather, 15), 
				new ItemStack(Items.iron_ingot, 10), 
				new ItemStack(ARKCraftItems.fiber, 15)
				);
		//Medium Saddle
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.saddle_medium, 1), 
				new ItemStack(Items.leather, 48),
				new ItemStack(Items.iron_ingot, 32), 
				new ItemStack(ARKCraftItems.fiber, 32),
				new ItemStack(Items.diamond, 1)
				);
		//Large Saddle
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.saddle_large, 1), 
				new ItemStack(ARKCraftItems.chitin, 32),
				new ItemStack(Items.iron_ingot, 16), 
				new ItemStack(ARKCraftItems.fiber, 32),
				new ItemStack(Items.diamond, 4)
				);
		
		// Chitin Armor
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.chitinChest, 1),
				new ItemStack(ARKCraftItems.fiber, 4),
				new ItemStack(Items.leather, 10), 
				new ItemStack(ARKCraftItems.chitin, 20)
		);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.chitinLegs, 1),
				new ItemStack(ARKCraftItems.fiber, 5),
				new ItemStack(Items.leather, 12), 
				new ItemStack(ARKCraftItems.chitin, 25)
		);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.chitinHelm, 1),
				new ItemStack(ARKCraftItems.fiber, 3),
				new ItemStack(Items.leather, 7), 
				new ItemStack(ARKCraftItems.chitin, 15)
		);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.chitinBoots, 1),
				new ItemStack(ARKCraftItems.fiber, 4),
				new ItemStack(Items.leather, 6), 
				new ItemStack(ARKCraftItems.chitin, 12)
		);

		//Simple Pistol
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.simple_pistol, 1),
				new ItemStack(ARKCraftItems.metal, 60),
				new ItemStack(ARKCraftItems.wood, 5),
				new ItemStack(ARKCraftItems.hide, 15)
				);
		//Simple Bullet
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.simple_bullet, 1),
				new ItemStack(ARKCraftItems.gun_powder, 6),
				new ItemStack(Blocks.log, 2, ARKShapelessRecipe.ANY), // Any Wood
				new ItemStack(ARKCraftItems.hide, 5)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.simple_pistol_scoped, 1),
				new ItemStack(ARKCraftItems.simple_pistol, 1),
				new ItemStack(ARKCraftItems.spy_glass, 1)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.longneck_rifle, 1),
				new ItemStack(Items.iron_ingot, 32),
				new ItemStack(Blocks.log, 7, ARKShapelessRecipe.ANY), // Any Wood
				new ItemStack(ARKCraftItems.hide, 8)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.longneck_rifle_scoped, 1),
				new ItemStack(ARKCraftItems.longneck_rifle, 1),
				new ItemStack(ARKCraftItems.scope, 1)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.shotgun, 1),
				new ItemStack(Items.iron_ingot, 32),
				new ItemStack(Blocks.log, 4, ARKShapelessRecipe.ANY) // Any Wood
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.rocket_launcher, 1),
				new ItemStack(Items.iron_ingot, 48),
				new ItemStack(Items.diamond, 4),
				new ItemStack(Items.redstone, 16)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.tranq_gun, 1),
				new ItemStack(Items.iron_ingot, 7),
				new ItemStack(Blocks.log, 10, ARKShapelessRecipe.ANY), // Any Wood
				new ItemStack(ARKCraftItems.fiber, 35)
				);
		getInstance().addShapelessRecipe(new ItemStack(ARKCraftItems.crossbow, 1),
				new ItemStack(Items.iron_ingot, 7),
				new ItemStack(Blocks.log, 10, ARKShapelessRecipe.ANY), // Any Wood
				new ItemStack(ARKCraftItems.fiber, 35)
				);
	}	
}
