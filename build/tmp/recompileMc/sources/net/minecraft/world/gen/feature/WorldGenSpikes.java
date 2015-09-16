package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenSpikes extends WorldGenerator
{
    private Block field_150520_a;
    private static final String __OBFID = "CL_00000433";

    public WorldGenSpikes(Block p_i45464_1_)
    {
        this.field_150520_a = p_i45464_1_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        if (worldIn.isAirBlock(p_180709_3_) && worldIn.getBlockState(p_180709_3_.down()).getBlock() == this.field_150520_a)
        {
            int i = p_180709_2_.nextInt(32) + 6;
            int j = p_180709_2_.nextInt(4) + 1;
            int k;
            int l;
            int i1;
            int j1;

            for (k = p_180709_3_.getX() - j; k <= p_180709_3_.getX() + j; ++k)
            {
                for (l = p_180709_3_.getZ() - j; l <= p_180709_3_.getZ() + j; ++l)
                {
                    i1 = k - p_180709_3_.getX();
                    j1 = l - p_180709_3_.getZ();

                    if (i1 * i1 + j1 * j1 <= j * j + 1 && worldIn.getBlockState(new BlockPos(k, p_180709_3_.getY() - 1, l)).getBlock() != this.field_150520_a)
                    {
                        return false;
                    }
                }
            }

            for (k = p_180709_3_.getY(); k < p_180709_3_.getY() + i && k < 256; ++k)
            {
                for (l = p_180709_3_.getX() - j; l <= p_180709_3_.getX() + j; ++l)
                {
                    for (i1 = p_180709_3_.getZ() - j; i1 <= p_180709_3_.getZ() + j; ++i1)
                    {
                        j1 = l - p_180709_3_.getX();
                        int k1 = i1 - p_180709_3_.getZ();

                        if (j1 * j1 + k1 * k1 <= j * j + 1)
                        {
                            worldIn.setBlockState(new BlockPos(l, k, i1), Blocks.obsidian.getDefaultState(), 2);
                        }
                    }
                }
            }

            EntityEnderCrystal entityendercrystal = new EntityEnderCrystal(worldIn);
            entityendercrystal.setLocationAndAngles((double)((float)p_180709_3_.getX() + 0.5F), (double)(p_180709_3_.getY() + i), (double)((float)p_180709_3_.getZ() + 0.5F), p_180709_2_.nextFloat() * 360.0F, 0.0F);
            worldIn.spawnEntityInWorld(entityendercrystal);
            worldIn.setBlockState(p_180709_3_.up(i), Blocks.bedrock.getDefaultState(), 2);
            return true;
        }
        else
        {
            return false;
        }
    }
}