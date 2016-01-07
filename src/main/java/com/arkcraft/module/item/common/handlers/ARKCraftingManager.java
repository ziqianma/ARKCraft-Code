package com.arkcraft.module.item.common.handlers;

import com.arkcraft.module.item.common.container.inventory.InventoryPlayerCrafting;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapedRecipes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author wildbill22
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ARKCraftingManager
{

    private final List recipes = Lists.newArrayList();

    public ARKCraftingManager()
    {
        Collections.sort(this.recipes, new Comparator()
        {
            public int compare(IARKRecipe p_compare_1_, IARKRecipe p_compare_2_)
            {
                return p_compare_1_ instanceof ARKShapelessRecipe && p_compare_2_ instanceof ShapedRecipes ? 1 :
                        (p_compare_2_ instanceof ARKShapelessRecipe && p_compare_1_ instanceof ShapedRecipes ? -1 :
                                (p_compare_2_.getRecipeSize() < p_compare_1_.getRecipeSize() ? -1 :
                                        (p_compare_2_.getRecipeSize() > p_compare_1_.getRecipeSize() ? 1 : 0)));
            }

            public int compare(Object p_compare_1_, Object p_compare_2_)
            {
                return this.compare((IARKRecipe) p_compare_1_, (IARKRecipe) p_compare_2_);
            }
        });
    }

    /**
     * Adds a shapeless crafting recipe to the game.
     *
     * @param recipeComponents An array of ItemStack's Item's and Block's that make up the recipe.
     */
    public void addShapelessRecipe(ItemStack stack, Object... recipeComponents)
    {
        ArrayList arraylist = Lists.newArrayList();
        Object[] aobject = recipeComponents;
        int i = recipeComponents.length;

        for (int j = 0; j < i; ++j)
        {
            Object object1 = aobject[j];
            if (object1 instanceof ItemStack)
            {
                arraylist.add(((ItemStack) object1).copy());
            }
            else if (object1 instanceof Item)
            {
                arraylist.add(new ItemStack((Item) object1));
            }
            else
            {
                if (!(object1 instanceof Block))
                {
                    throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + object1.getClass().getName() + "!");
                }
                arraylist.add(new ItemStack((Block) object1));
            }
        }
        this.recipes.add(new ARKShapelessRecipe(stack, arraylist));
    }

    /**
     * Adds an IARKRecipe to the list of crafting recipes.
     *
     * @param recipe A recipe that will be added to the recipe list.
     */
    public void addRecipe(IARKRecipe recipe)
    {
        this.recipes.add(recipe);
    }

    // Added so we can use InventoryPlayerCrafting
    public int hasMatchingRecipe(ItemStack output, InventoryPlayerCrafting inventory, boolean craft)
    {
        return hasMatchingRecipe(output, inventory.getItemStacks(), craft);
    }

    /**
     * Returns number of matches for inventory that exists, also deducts inventory if craft = true
     */
    public int hasMatchingRecipe(ItemStack output, ItemStack[] itemStacksInventory, boolean craft)
    {
        Iterator iterator = this.recipes.iterator();
        IARKRecipe irecipe;
        do
        {
            if (!iterator.hasNext())
            {
                return 0;
            }
            irecipe = (IARKRecipe) iterator.next();
        }
        while (irecipe.getRecipeOutput().getItem() != output.getItem());
        if (craft)
        {
            return irecipe.craftMatches(itemStacksInventory);
        }
        else
        {
            return irecipe.findMatches(itemStacksInventory);
        }
    }

    /**
     * Returns true if ItemStack is in any recipe
     */
    public boolean isItemInRecipe(ItemStack itemStack)
    {
        Iterator iterator = this.recipes.iterator();
        IARKRecipe irecipe;
        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }
            irecipe = (IARKRecipe) iterator.next();
        }
        while (!irecipe.isItemInRecipe(itemStack));
        return true;
    }

    /**
     * returns the List<> of all recipes
     */
    public List getRecipeList()
    {
        return this.recipes;
    }

    /**
     * returns number of recipes
     */
    public int getNumRecipes()
    {
        return this.recipes.size();
    }
}
