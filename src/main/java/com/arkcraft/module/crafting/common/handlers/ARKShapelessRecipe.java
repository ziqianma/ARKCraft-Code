package com.arkcraft.module.crafting.common.handlers;

import com.google.common.collect.Lists;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ARKShapelessRecipe implements IARKRecipe
{
    /**
     * Is the ItemStack that you get when craft the recipe.
     */
    private final ItemStack recipeOutput;
    /**
     * Is a List of ItemStack that composes the recipe.
     */
    @SuppressWarnings("rawtypes")
    public final List recipeItems;
    public final static int ANY = 32767; // Normally 32767

    @SuppressWarnings("rawtypes")
    public ARKShapelessRecipe(ItemStack output, List inputList)
    {
        this.recipeOutput = output;
        this.recipeItems = inputList;
    }

    public ItemStack getRecipeOutput()
    {
        return this.recipeOutput;
    }

    /**
     * Craft one item
     * Returns - 1 if item was crafted, 0 if not
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public int craftMatches(ItemStack[] itemStacksInventory)
    {
        ArrayList recipelist = Lists.newArrayList(this.recipeItems);
        for (int i = 0; i < itemStacksInventory.length; ++i)
        {
            ItemStack itemstack = itemStacksInventory[i];
            if (itemstack != null)
            {
                Iterator recipeIterator = recipelist.iterator();
                while (recipeIterator.hasNext())
                {
                    ItemStack itemstackInRecipe = (ItemStack) recipeIterator.next();
                    if (itemstack.getItem() == itemstackInRecipe.getItem() && itemstack.stackSize >= itemstackInRecipe.stackSize
                            && (itemstackInRecipe.getMetadata() == ANY || itemstack.getMetadata() == itemstackInRecipe.getMetadata()))
                    {
                        recipelist.remove(itemstackInRecipe);
                        itemStacksInventory[i].stackSize -= itemstackInRecipe.stackSize;
                        if (itemStacksInventory[i].stackSize <= 0)
                        {
                            itemStacksInventory[i] = null;
                        }
                        break;
                    }
                }
                if (recipelist.isEmpty())
                {
                    return 1;
                }
            }
        }
        return 0;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     * Only call with craft = true after ensuring enough inventory with a previous call with it set to false
     * Returns - number of items that can be crafted
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public int findMatches(ItemStack[] itemStacksInventory)
    {
        int numThatCanBeCrafted = this.recipeOutput.getMaxStackSize();
        ArrayList recipelist = Lists.newArrayList(this.recipeItems);
        Iterator recipeIterator = recipelist.iterator();
        while (recipeIterator.hasNext())
        {
            ItemStack itemstackInRecipe = (ItemStack) recipeIterator.next();
            int numInStackThatCanBeCrafted = findNumThatCanBeCrafted(itemStacksInventory, itemstackInRecipe);
            if (numInStackThatCanBeCrafted < numThatCanBeCrafted)
            {
                numThatCanBeCrafted = numInStackThatCanBeCrafted;
            }
        }
        return numThatCanBeCrafted;
    }

    private int findNumThatCanBeCrafted(ItemStack[] itemStacksInventory, ItemStack itemstackInRecipe)
    {
        int numInStack = 0;
        for (int i = 0; i < itemStacksInventory.length; ++i)
        {
            ItemStack itemstack = itemStacksInventory[i];
            if (itemstack != null)
            {
                if (itemstack.getItem() == itemstackInRecipe.getItem()
                        && (itemstackInRecipe.getMetadata() == ANY || itemstack.getMetadata() == itemstackInRecipe.getMetadata()))
                {
                    numInStack += itemstack.stackSize;
                }
            }
        }
        return numInStack / itemstackInRecipe.stackSize;
    }

    /**
     * Used to check if a recipe matches current crafting inventory
     * Only call with craft = true after ensuring enough inventory with a previous call with it set to false
     * Returns - number of items that can be crafted
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public boolean isItemInRecipe(ItemStack itemstack)
    {
        ArrayList recipelist = Lists.newArrayList(this.recipeItems);
        Iterator recipeIterator = recipelist.iterator();
        while (recipeIterator.hasNext())
        {
            ItemStack itemstackInRecipe = (ItemStack) recipeIterator.next();
            if (itemstack.getItem() == itemstackInRecipe.getItem() && itemstack.stackSize >= itemstackInRecipe.stackSize
                    && (itemstackInRecipe.getMetadata() == 32767 || itemstack.getMetadata() == itemstackInRecipe.getMetadata()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_)
    {
        return this.recipeOutput.copy();
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return this.recipeItems.size();
    }
}
