package net.minecraft.inventory;

import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class InventoryHelper
{
    private static final Random RANDOM = new Random();
    private static final String __OBFID = "CL_00002262";

    public static void dropInventoryItems(World worldIn, BlockPos p_180175_1_, IInventory p_180175_2_)
    {
        func_180174_a(worldIn, (double)p_180175_1_.getX(), (double)p_180175_1_.getY(), (double)p_180175_1_.getZ(), p_180175_2_);
    }

    public static void func_180176_a(World worldIn, Entity p_180176_1_, IInventory p_180176_2_)
    {
        func_180174_a(worldIn, p_180176_1_.posX, p_180176_1_.posY, p_180176_1_.posZ, p_180176_2_);
    }

    private static void func_180174_a(World worldIn, double p_180174_1_, double p_180174_3_, double p_180174_5_, IInventory p_180174_7_)
    {
        for (int i = 0; i < p_180174_7_.getSizeInventory(); ++i)
        {
            ItemStack itemstack = p_180174_7_.getStackInSlot(i);

            if (itemstack != null)
            {
                spawnItemStack(worldIn, p_180174_1_, p_180174_3_, p_180174_5_, itemstack);
            }
        }
    }

    private static void spawnItemStack(World worldIn, double p_180173_1_, double p_180173_3_, double p_180173_5_, ItemStack p_180173_7_)
    {
        float f = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
        float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (p_180173_7_.stackSize > 0)
        {
            int i = RANDOM.nextInt(21) + 10;

            if (i > p_180173_7_.stackSize)
            {
                i = p_180173_7_.stackSize;
            }

            p_180173_7_.stackSize -= i;
            EntityItem entityitem = new EntityItem(worldIn, p_180173_1_ + (double)f, p_180173_3_ + (double)f1, p_180173_5_ + (double)f2, new ItemStack(p_180173_7_.getItem(), i, p_180173_7_.getMetadata()));

            if (p_180173_7_.hasTagCompound())
            {
                entityitem.getEntityItem().setTagCompound((NBTTagCompound)p_180173_7_.getTagCompound().copy());
            }

            float f3 = 0.05F;
            entityitem.motionX = RANDOM.nextGaussian() * (double)f3;
            entityitem.motionY = RANDOM.nextGaussian() * (double)f3 + 0.20000000298023224D;
            entityitem.motionZ = RANDOM.nextGaussian() * (double)f3;
            worldIn.spawnEntityInWorld(entityitem);
        }
    }
}