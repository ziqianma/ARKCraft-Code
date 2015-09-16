package net.minecraft.item;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFireworkCharge extends Item
{
    private static final String __OBFID = "CL_00000030";

    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
        if (renderPass != 1)
        {
            return super.getColorFromItemStack(stack, renderPass);
        }
        else
        {
            NBTBase nbtbase = getExplosionTag(stack, "Colors");

            if (!(nbtbase instanceof NBTTagIntArray))
            {
                return 9079434;
            }
            else
            {
                NBTTagIntArray nbttagintarray = (NBTTagIntArray)nbtbase;
                int[] aint = nbttagintarray.getIntArray();

                if (aint.length == 1)
                {
                    return aint[0];
                }
                else
                {
                    int j = 0;
                    int k = 0;
                    int l = 0;
                    int[] aint1 = aint;
                    int i1 = aint.length;

                    for (int j1 = 0; j1 < i1; ++j1)
                    {
                        int k1 = aint1[j1];
                        j += (k1 & 16711680) >> 16;
                        k += (k1 & 65280) >> 8;
                        l += (k1 & 255) >> 0;
                    }

                    j /= aint.length;
                    k /= aint.length;
                    l /= aint.length;
                    return j << 16 | k << 8 | l;
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static NBTBase getExplosionTag(ItemStack stack, String key)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Explosion");

            if (nbttagcompound != null)
            {
                return nbttagcompound.getTag(key);
            }
        }

        return null;
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     *  
     * @param tooltip All lines to display in the Item's tooltip. This is a List of Strings.
     * @param advanced Whether the setting "Advanced tooltips" is enabled
     */
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced)
    {
        if (stack.hasTagCompound())
        {
            NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Explosion");

            if (nbttagcompound != null)
            {
                addExplosionInfo(nbttagcompound, tooltip);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void addExplosionInfo(NBTTagCompound nbt, List tooltip)
    {
        byte b0 = nbt.getByte("Type");

        if (b0 >= 0 && b0 <= 4)
        {
            tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.type." + b0).trim());
        }
        else
        {
            tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.type").trim());
        }

        int[] aint = nbt.getIntArray("Colors");
        int j;
        int k;

        if (aint.length > 0)
        {
            boolean flag = true;
            String s = "";
            int[] aint1 = aint;
            int i = aint.length;

            for (j = 0; j < i; ++j)
            {
                k = aint1[j];

                if (!flag)
                {
                    s = s + ", ";
                }

                flag = false;
                boolean flag1 = false;

                for (int l = 0; l < ItemDye.dyeColors.length; ++l)
                {
                    if (k == ItemDye.dyeColors[l])
                    {
                        flag1 = true;
                        s = s + StatCollector.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(l).getUnlocalizedName());
                        break;
                    }
                }

                if (!flag1)
                {
                    s = s + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
            }

            tooltip.add(s);
        }

        int[] aint2 = nbt.getIntArray("FadeColors");
        boolean flag2;

        if (aint2.length > 0)
        {
            flag2 = true;
            String s1 = StatCollector.translateToLocal("item.fireworksCharge.fadeTo") + " ";
            int[] aint3 = aint2;
            j = aint2.length;

            for (k = 0; k < j; ++k)
            {
                int j1 = aint3[k];

                if (!flag2)
                {
                    s1 = s1 + ", ";
                }

                flag2 = false;
                boolean flag4 = false;

                for (int i1 = 0; i1 < 16; ++i1)
                {
                    if (j1 == ItemDye.dyeColors[i1])
                    {
                        flag4 = true;
                        s1 = s1 + StatCollector.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(i1).getUnlocalizedName());
                        break;
                    }
                }

                if (!flag4)
                {
                    s1 = s1 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
                }
            }

            tooltip.add(s1);
        }

        flag2 = nbt.getBoolean("Trail");

        if (flag2)
        {
            tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.trail"));
        }

        boolean flag3 = nbt.getBoolean("Flicker");

        if (flag3)
        {
            tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.flicker"));
        }
    }
}