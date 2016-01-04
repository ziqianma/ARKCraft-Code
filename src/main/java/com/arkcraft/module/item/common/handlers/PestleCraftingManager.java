package com.arkcraft.module.item.common.handlers;

import com.arkcraft.module.item.common.items.ARKCraftItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

/**
 * @author wildbill22
 *         Notes about adding recipes:
 *         1) If a block has meta data:
 *         a) If not enter in ItemStack as 3rd param, it is set to 0
 *         b) If entered, then just a stack with that meta will match
 *         c) If ARKShapelessRecipe.ANY (32767) is used, all the different meta types for the block will match
 *         2) Do not have two recipes for the same ItemStack, only the first will be used
 */
public class PestleCraftingManager extends ARKCraftingManager
{

    private static PestleCraftingManager instance = null;

    public PestleCraftingManager()
    {
        super();
        instance = this;
    }

    public static PestleCraftingManager getInstance()
    {
        if (instance == null)
        {
            instance = new PestleCraftingManager();
        }
        return instance;
    }

    public static void registerPestleCraftingRecipes()
    {

        //Narcotics
        getInstance().addShapelessRecipe(
                new ItemStack(ARKCraftItems.narcotics, 1),
                new ItemStack(ARKCraftItems.narcoBerry, 5),
                new ItemStack(ARKCraftItems.spoiled_meat, 1)
        );
        //Gun Powder
        getInstance().addShapelessRecipe(
                new ItemStack(ARKCraftItems.gun_powder, 1),
                new ItemStack(ARKCraftItems.flint, 1),
                new ItemStack(Items.coal, 1)
        );
        //Spark Powder
        getInstance().addShapelessRecipe(
                new ItemStack(ARKCraftItems.spark_powder, 1),
                new ItemStack(ARKCraftItems.flint, 2),
                new ItemStack(ARKCraftItems.rock, 1)
        );

        //Cementing Pastes
        getInstance().addShapelessRecipe(
                new ItemStack(ARKCraftItems.cementing_pastes, 1),
                new ItemStack(ARKCraftItems.rock, 8),
                new ItemStack(ARKCraftItems.chitin, 4)
        );
    }
}
