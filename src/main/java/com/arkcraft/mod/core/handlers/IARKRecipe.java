package com.arkcraft.mod.core.handlers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public interface IARKRecipe {
    /**
     * Used to check if a recipe matches current crafting inventory
     */
    int findMatches(ItemStack[] itemStacks);
    
    /**
     * Used to craft recipe from current crafting inventory
     */
    int craftMatches(ItemStack[] itemStacks);

    /**
     * Returns an Item that is the result of this recipe
     */
    ItemStack getCraftingResult(InventoryCrafting p_77572_1_);

    /**
     * Returns true if ItemStack is in any recipe
     */
	public boolean isItemInRecipe(ItemStack itemStack);

	/**
     * Returns the size of the recipe area
     */
    int getRecipeSize();
    
	/**
     * Returns the itemStack produced when the recipe is crafted
     */
    ItemStack getRecipeOutput();
}
