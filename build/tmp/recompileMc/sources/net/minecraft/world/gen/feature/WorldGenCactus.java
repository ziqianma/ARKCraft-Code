package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenCactus extends WorldGenerator
{
    private static final String __OBFID = "CL_00000404";

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (int i = 0; i < 10; ++i)
        {
            BlockPos blockpos1 = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

            if (worldIn.isAirBlock(blockpos1))
            {
                int j = 1 + p_180709_2_.nextInt(p_180709_2_.nextInt(3) + 1);

                for (int k = 0; k < j; ++k)
                {
                    if (Blocks.cactus.canBlockStay(worldIn, blockpos1))
                    {
                        worldIn.setBlockState(blockpos1.up(k), Blocks.cactus.getDefaultState(), 2);
                    }
                }
            }
        }

        return true;
    }
}