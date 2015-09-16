package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenReed extends WorldGenerator
{
    private static final String __OBFID = "CL_00000429";

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (int i = 0; i < 20; ++i)
        {
            BlockPos blockpos1 = p_180709_3_.add(p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), 0, p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4));

            if (worldIn.isAirBlock(blockpos1))
            {
                BlockPos blockpos2 = blockpos1.down();

                if (worldIn.getBlockState(blockpos2.west()).getBlock().getMaterial() == Material.water || worldIn.getBlockState(blockpos2.east()).getBlock().getMaterial() == Material.water || worldIn.getBlockState(blockpos2.north()).getBlock().getMaterial() == Material.water || worldIn.getBlockState(blockpos2.south()).getBlock().getMaterial() == Material.water)
                {
                    int j = 2 + p_180709_2_.nextInt(p_180709_2_.nextInt(3) + 1);

                    for (int k = 0; k < j; ++k)
                    {
                        if (Blocks.reeds.canBlockStay(worldIn, blockpos1))
                        {
                            worldIn.setBlockState(blockpos1.up(k), Blocks.reeds.getDefaultState(), 2);
                        }
                    }
                }
            }
        }

        return true;
    }
}