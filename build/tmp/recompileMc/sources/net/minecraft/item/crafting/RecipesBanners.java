package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.world.World;

public class RecipesBanners
{
    private static final String __OBFID = "CL_00002160";

    void func_179534_a(CraftingManager p_179534_1_)
    {
        EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        int i = aenumdyecolor.length;

        for (int j = 0; j < i; ++j)
        {
            EnumDyeColor enumdyecolor = aenumdyecolor[j];
            p_179534_1_.addRecipe(new ItemStack(Items.banner, 1, enumdyecolor.getDyeDamage()), new Object[] {"###", "###", " | ", '#', new ItemStack(Blocks.wool, 1, enumdyecolor.getMetadata()), '|', Items.stick});
        }

        p_179534_1_.addRecipe(new RecipesBanners.RecipeDuplicatePattern(null));
        p_179534_1_.addRecipe(new RecipesBanners.RecipeAddPattern(null));
    }

    public static class RecipeAddPattern implements IRecipe
        {
            private static final String __OBFID = "CL_00002158";

            private RecipeAddPattern() {}

            /**
             * Used to check if a recipe matches current crafting inventory
             */
            public boolean matches(InventoryCrafting p_77569_1_, World worldIn)
            {
                boolean flag = false;

                for (int i = 0; i < p_77569_1_.getSizeInventory(); ++i)
                {
                    ItemStack itemstack = p_77569_1_.getStackInSlot(i);

                    if (itemstack != null && itemstack.getItem() == Items.banner)
                    {
                        if (flag)
                        {
                            return false;
                        }

                        if (TileEntityBanner.getPatterns(itemstack) >= 6)
                        {
                            return false;
                        }

                        flag = true;
                    }
                }

                if (!flag)
                {
                    return false;
                }
                else
                {
                    return this.func_179533_c(p_77569_1_) != null;
                }
            }

            /**
             * Returns an Item that is the result of this recipe
             */
            public ItemStack getCraftingResult(InventoryCrafting p_77572_1_)
            {
                ItemStack itemstack = null;

                for (int i = 0; i < p_77572_1_.getSizeInventory(); ++i)
                {
                    ItemStack itemstack1 = p_77572_1_.getStackInSlot(i);

                    if (itemstack1 != null && itemstack1.getItem() == Items.banner)
                    {
                        itemstack = itemstack1.copy();
                        itemstack.stackSize = 1;
                        break;
                    }
                }

                TileEntityBanner.EnumBannerPattern enumbannerpattern = this.func_179533_c(p_77572_1_);

                if (enumbannerpattern != null)
                {
                    int k = 0;
                    ItemStack itemstack2;

                    for (int j = 0; j < p_77572_1_.getSizeInventory(); ++j)
                    {
                        itemstack2 = p_77572_1_.getStackInSlot(j);

                        if (itemstack2 != null && itemstack2.getItem() == Items.dye)
                        {
                            k = itemstack2.getMetadata();
                            break;
                        }
                    }

                    NBTTagCompound nbttagcompound1 = itemstack.getSubCompound("BlockEntityTag", true);
                    itemstack2 = null;
                    NBTTagList nbttaglist;

                    if (nbttagcompound1.hasKey("Patterns", 9))
                    {
                        nbttaglist = nbttagcompound1.getTagList("Patterns", 10);
                    }
                    else
                    {
                        nbttaglist = new NBTTagList();
                        nbttagcompound1.setTag("Patterns", nbttaglist);
                    }

                    NBTTagCompound nbttagcompound = new NBTTagCompound();
                    nbttagcompound.setString("Pattern", enumbannerpattern.getPatternID());
                    nbttagcompound.setInteger("Color", k);
                    nbttaglist.appendTag(nbttagcompound);
                }

                return itemstack;
            }

            /**
             * Returns the size of the recipe area
             */
            public int getRecipeSize()
            {
                return 10;
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

            private TileEntityBanner.EnumBannerPattern func_179533_c(InventoryCrafting p_179533_1_)
            {
                TileEntityBanner.EnumBannerPattern[] aenumbannerpattern = TileEntityBanner.EnumBannerPattern.values();
                int i = aenumbannerpattern.length;

                for (int j = 0; j < i; ++j)
                {
                    TileEntityBanner.EnumBannerPattern enumbannerpattern = aenumbannerpattern[j];

                    if (enumbannerpattern.hasValidCrafting())
                    {
                        boolean flag = true;
                        int i1;

                        if (enumbannerpattern.hasCraftingStack())
                        {
                            boolean flag1 = false;
                            boolean flag2 = false;

                            for (i1 = 0; i1 < p_179533_1_.getSizeInventory() && flag; ++i1)
                            {
                                ItemStack itemstack1 = p_179533_1_.getStackInSlot(i1);

                                if (itemstack1 != null && itemstack1.getItem() != Items.banner)
                                {
                                    if (itemstack1.getItem() == Items.dye)
                                    {
                                        if (flag2)
                                        {
                                            flag = false;
                                            break;
                                        }

                                        flag2 = true;
                                    }
                                    else
                                    {
                                        if (flag1 || !itemstack1.isItemEqual(enumbannerpattern.getCraftingStack()))
                                        {
                                            flag = false;
                                            break;
                                        }

                                        flag1 = true;
                                    }
                                }
                            }

                            if (!flag1)
                            {
                                flag = false;
                            }
                        }
                        else if (p_179533_1_.getSizeInventory() != enumbannerpattern.getCraftingLayers().length * enumbannerpattern.getCraftingLayers()[0].length())
                        {
                            flag = false;
                        }
                        else
                        {
                            int k = -1;

                            for (int l = 0; l < p_179533_1_.getSizeInventory() && flag; ++l)
                            {
                                i1 = l / 3;
                                int j1 = l % 3;
                                ItemStack itemstack = p_179533_1_.getStackInSlot(l);

                                if (itemstack != null && itemstack.getItem() != Items.banner)
                                {
                                    if (itemstack.getItem() != Items.dye)
                                    {
                                        flag = false;
                                        break;
                                    }

                                    if (k != -1 && k != itemstack.getMetadata())
                                    {
                                        flag = false;
                                        break;
                                    }

                                    if (enumbannerpattern.getCraftingLayers()[i1].charAt(j1) == 32)
                                    {
                                        flag = false;
                                        break;
                                    }

                                    k = itemstack.getMetadata();
                                }
                                else if (enumbannerpattern.getCraftingLayers()[i1].charAt(j1) != 32)
                                {
                                    flag = false;
                                    break;
                                }
                            }
                        }

                        if (flag)
                        {
                            return enumbannerpattern;
                        }
                    }
                }

                return null;
            }

            RecipeAddPattern(Object p_i45780_1_)
            {
                this();
            }
        }

    public static class RecipeDuplicatePattern implements IRecipe
        {
            private static final String __OBFID = "CL_00002157";

            private RecipeDuplicatePattern() {}

            /**
             * Used to check if a recipe matches current crafting inventory
             */
            public boolean matches(InventoryCrafting p_77569_1_, World worldIn)
            {
                ItemStack itemstack = null;
                ItemStack itemstack1 = null;

                for (int i = 0; i < p_77569_1_.getSizeInventory(); ++i)
                {
                    ItemStack itemstack2 = p_77569_1_.getStackInSlot(i);

                    if (itemstack2 != null)
                    {
                        if (itemstack2.getItem() != Items.banner)
                        {
                            return false;
                        }

                        if (itemstack != null && itemstack1 != null)
                        {
                            return false;
                        }

                        int j = TileEntityBanner.getBaseColor(itemstack2);
                        boolean flag = TileEntityBanner.getPatterns(itemstack2) > 0;

                        if (itemstack != null)
                        {
                            if (flag)
                            {
                                return false;
                            }

                            if (j != TileEntityBanner.getBaseColor(itemstack))
                            {
                                return false;
                            }

                            itemstack1 = itemstack2;
                        }
                        else if (itemstack1 != null)
                        {
                            if (!flag)
                            {
                                return false;
                            }

                            if (j != TileEntityBanner.getBaseColor(itemstack1))
                            {
                                return false;
                            }

                            itemstack = itemstack2;
                        }
                        else if (flag)
                        {
                            itemstack = itemstack2;
                        }
                        else
                        {
                            itemstack1 = itemstack2;
                        }
                    }
                }

                return itemstack != null && itemstack1 != null;
            }

            /**
             * Returns an Item that is the result of this recipe
             */
            public ItemStack getCraftingResult(InventoryCrafting p_77572_1_)
            {
                for (int i = 0; i < p_77572_1_.getSizeInventory(); ++i)
                {
                    ItemStack itemstack = p_77572_1_.getStackInSlot(i);

                    if (itemstack != null && TileEntityBanner.getPatterns(itemstack) > 0)
                    {
                        ItemStack itemstack1 = itemstack.copy();
                        itemstack1.stackSize = 1;
                        return itemstack1;
                    }
                }

                return null;
            }

            /**
             * Returns the size of the recipe area
             */
            public int getRecipeSize()
            {
                return 2;
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

                    if (itemstack != null)
                    {
                        if (itemstack.getItem().hasContainerItem(itemstack))
                        {
                            aitemstack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
                        }
                        else if (itemstack.hasTagCompound() && TileEntityBanner.getPatterns(itemstack) > 0)
                        {
                            aitemstack[i] = itemstack.copy();
                            aitemstack[i].stackSize = 1;
                        }
                    }
                }

                return aitemstack;
            }

            RecipeDuplicatePattern(Object p_i45779_1_)
            {
                this();
            }
        }
}