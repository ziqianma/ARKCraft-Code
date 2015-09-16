package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RecipeRepairItem implements IRecipe
{
    private static final String __OBFID = "CL_00002156";

    /**
     * Used to check if a recipe matches current crafting inventory
     */
    public boolean matches(InventoryCrafting p_77569_1_, World worldIn)
    {
        ArrayList arraylist = Lists.newArrayList();

        for (int i = 0; i < p_77569_1_.getSizeInventory(); ++i)
        {
            ItemStack itemstack = p_77569_1_.getStackInSlot(i);

            if (itemstack != null)
            {
                arraylist.add(itemstack);

                if (arraylist.size() > 1)
                {
                    ItemStack itemstack1 = (ItemStack)arraylist.get(0);

                    if (itemstack.getItem() != itemstack1.getItem() || itemstack1.stackSize != 1 || itemstack.stackSize != 1 || !itemstack1.getItem().isRepairable())
                    {
                        return false;
                    }
                }
            }
        }

        return arraylist.size() == 2;
    }

    /**
     * Returns an Item that is the result of this recipe
     */
    public ItemStack getCraftingResult(InventoryCrafting p_77572_1_)
    {
        ArrayList arraylist = Lists.newArrayList();
        ItemStack itemstack;

        for (int i = 0; i < p_77572_1_.getSizeInventory(); ++i)
        {
            itemstack = p_77572_1_.getStackInSlot(i);

            if (itemstack != null)
            {
                arraylist.add(itemstack);

                if (arraylist.size() > 1)
                {
                    ItemStack itemstack1 = (ItemStack)arraylist.get(0);

                    if (itemstack.getItem() != itemstack1.getItem() || itemstack1.stackSize != 1 || itemstack.stackSize != 1 || !itemstack1.getItem().isRepairable())
                    {
                        return null;
                    }
                }
            }
        }

        if (arraylist.size() == 2)
        {
            ItemStack itemstack2 = (ItemStack)arraylist.get(0);
            itemstack = (ItemStack)arraylist.get(1);

            if (itemstack2.getItem() == itemstack.getItem() && itemstack2.stackSize == 1 && itemstack.stackSize == 1 && itemstack2.getItem().isRepairable())
            {
                Item item = itemstack2.getItem();
                int j = item.getMaxDamage() - itemstack2.getItemDamage();
                int k = item.getMaxDamage() - itemstack.getItemDamage();
                int l = j + k + item.getMaxDamage() * 5 / 100;
                int i1 = item.getMaxDamage() - l;

                if (i1 < 0)
                {
                    i1 = 0;
                }

                return new ItemStack(itemstack2.getItem(), 1, i1);
            }
        }

        return null;
    }

    /**
     * Returns the size of the recipe area
     */
    public int getRecipeSize()
    {
        return 4;
    }

    public ItemStack getRecipeOutput()
    {
        return null;
    }

    public ItemStack[] getRemainingItems(InventoryCrafting p_179532_1_)
    {
        ItemStack[] aitemstack = new ItemStack[p_179532_1_.getSizeInventory()];

        for (int i = 0; i < aitemstack.length; ++i)
        {
            ItemStack itemstack = p_179532_1_.getStackInSlot(i);
            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return aitemstack;
    }
}