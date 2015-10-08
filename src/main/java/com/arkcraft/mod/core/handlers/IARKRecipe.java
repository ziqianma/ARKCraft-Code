package com.arkcraft.mod.core.handlers;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public interface IARKRecipe {
    /**
     * Used to check if a recipe matches current crafting inventory
     */
//    boolean matches(InventoryCrafting p_77569_1_, World worldIn);
    boolean matches(ItemStack[] itemStacks, boolean craft);

    /**
     * Returns an Item that is the result of this recipe
     */
    ItemStack getCraftingResult(InventoryCrafting p_77572_1_);

    /**
     * Returns the size of the recipe area
     */
    int getRecipeSize();

    ItemStack getRecipeOutput();

//    ItemStack[] getRemainingItems(InventoryCrafting p_179532_1_);
}
