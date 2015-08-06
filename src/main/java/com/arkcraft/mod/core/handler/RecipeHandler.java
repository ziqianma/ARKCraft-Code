package com.arkcraft.mod.core.handler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;

public class RecipeHandler {

	public RecipeHandler() {}
	
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
	}
	
}
