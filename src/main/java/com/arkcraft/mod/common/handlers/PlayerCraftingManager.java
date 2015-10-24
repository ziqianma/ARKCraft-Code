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
		PlayerCraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(ARKCraftItems.slingshot, 1),
				new ItemStack(Items.leather, 1), 
				new ItemStack(Blocks.log, 1, 0), // Oak Wood
				new ItemStack(ARKCraftItems.fiber, 20)
				);
		PlayerCraftingManager.getInstance().addShapelessRecipe(
				new ItemStack(ARKCraftItems.spear, 1),
				new ItemStack(Items.flint, 2), 
				new ItemStack(Blocks.log, 2, 0), // Oak 
//				new ItemStack(Blocks.log, 1, ARKShapelessRecipe.ANY), // Any Wood!
				new ItemStack(ARKCraftItems.fiber, 12)
				);
	}
}
