package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenSand extends WorldGenerator
{
    private Block field_150517_a;
    /** The maximum radius used when generating a patch of blocks. */
    private int radius;
    private static final String __OBFID = "CL_00000431";

    public WorldGenSand(Block p_i45462_1_, int p_i45462_2_)
    {
        this.field_150517_a = p_i45462_1_;
        this.radius = p_i45462_2_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        if (worldIn.getBlockState(p_180709_3_).getBlock().getMaterial() != Material.water)
        {
            return false;
        }
        else
        {
            int i = p_180709_2_.nextInt(this.radius - 2) + 2;
            byte b0 = 2;

            for (int j = p_180709_3_.getX() - i; j <= p_180709_3_.getX() + i; ++j)
            {
                for (int k = p_180709_3_.getZ() - i; k <= p_180709_3_.getZ() + i; ++k)
                {
                    int l = j - p_180709_3_.getX();
                    int i1 = k - p_180709_3_.getZ();

                    if (l * l + i1 * i1 <= i * i)
                    {
                        for (int j1 = p_180709_3_.getY() - b0; j1 <= p_180709_3_.getY() + b0; ++j1)
                        {
                            BlockPos blockpos1 = new BlockPos(j, j1, k);
                            Block block = worldIn.getBlockState(blockpos1).getBlock();

                            if (block == Blocks.dirt || block == Blocks.grass)
                            {
                                worldIn.setBlockState(blockpos1, this.field_150517_a.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}