package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenGlowStone1 extends WorldGenerator
{
    private static final String __OBFID = "CL_00000419";

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        if (!worldIn.isAirBlock(p_180709_3_))
        {
            return false;
        }
        else if (worldIn.getBlockState(p_180709_3_.up()).getBlock() != Blocks.netherrack)
        {
            return false;
        }
        else
        {
            worldIn.setBlockState(p_180709_3_, Blocks.glowstone.getDefaultState(), 2);

            for (int i = 0; i < 1500; ++i)
            {
                BlockPos blockpos1 = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), -p_180709_2_.nextInt(12), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

                if (worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.air)
                {
                    int j = 0;
                    EnumFacing[] aenumfacing = EnumFacing.values();
                    int k = aenumfacing.length;

                    for (int l = 0; l < k; ++l)
                    {
                        EnumFacing enumfacing = aenumfacing[l];

                        if (worldIn.getBlockState(blockpos1.offset(enumfacing)).getBlock() == Blocks.glowstone)
                        {
                            ++j;
                        }

                        if (j > 1)
                        {
                            break;
                        }
                    }

                    if (j == 1)
                    {
                        worldIn.setBlockState(blockpos1, Blocks.glowstone.getDefaultState(), 2);
                    }
                }
            }

            return true;
        }
    }
}