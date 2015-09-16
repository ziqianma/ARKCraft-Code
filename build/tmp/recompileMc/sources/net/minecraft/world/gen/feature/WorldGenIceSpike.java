package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenIceSpike extends WorldGenerator
{
    private static final String __OBFID = "CL_00000417";

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        while (worldIn.isAirBlock(p_180709_3_) && p_180709_3_.getY() > 2)
        {
            p_180709_3_ = p_180709_3_.down();
        }

        if (worldIn.getBlockState(p_180709_3_).getBlock() != Blocks.snow)
        {
            return false;
        }
        else
        {
            p_180709_3_ = p_180709_3_.up(p_180709_2_.nextInt(4));
            int i = p_180709_2_.nextInt(4) + 7;
            int j = i / 4 + p_180709_2_.nextInt(2);

            if (j > 1 && p_180709_2_.nextInt(60) == 0)
            {
                p_180709_3_ = p_180709_3_.up(10 + p_180709_2_.nextInt(30));
            }

            int k;
            int l;

            for (k = 0; k < i; ++k)
            {
                float f = (1.0F - (float)k / (float)i) * (float)j;
                l = MathHelper.ceiling_float_int(f);

                for (int i1 = -l; i1 <= l; ++i1)
                {
                    float f1 = (float)MathHelper.abs_int(i1) - 0.25F;

                    for (int j1 = -l; j1 <= l; ++j1)
                    {
                        float f2 = (float)MathHelper.abs_int(j1) - 0.25F;

                        if ((i1 == 0 && j1 == 0 || f1 * f1 + f2 * f2 <= f * f) && (i1 != -l && i1 != l && j1 != -l && j1 != l || p_180709_2_.nextFloat() <= 0.75F))
                        {
                            Block block = worldIn.getBlockState(p_180709_3_.add(i1, k, j1)).getBlock();

                            if (block.getMaterial() == Material.air || block == Blocks.dirt || block == Blocks.snow || block == Blocks.ice)
                            {
                                this.func_175906_a(worldIn, p_180709_3_.add(i1, k, j1), Blocks.packed_ice);
                            }

                            if (k != 0 && l > 1)
                            {
                                block = worldIn.getBlockState(p_180709_3_.add(i1, -k, j1)).getBlock();

                                if (block.getMaterial() == Material.air || block == Blocks.dirt || block == Blocks.snow || block == Blocks.ice)
                                {
                                    this.func_175906_a(worldIn, p_180709_3_.add(i1, -k, j1), Blocks.packed_ice);
                                }
                            }
                        }
                    }
                }
            }

            k = j - 1;

            if (k < 0)
            {
                k = 0;
            }
            else if (k > 1)
            {
                k = 1;
            }

            for (int k1 = -k; k1 <= k; ++k1)
            {
                l = -k;

                while (l <= k)
                {
                    BlockPos blockpos1 = p_180709_3_.add(k1, -1, l);
                    int l1 = 50;

                    if (Math.abs(k1) == 1 && Math.abs(l) == 1)
                    {
                        l1 = p_180709_2_.nextInt(5);
                    }

                    while (true)
                    {
                        if (blockpos1.getY() > 50)
                        {
                            Block block1 = worldIn.getBlockState(blockpos1).getBlock();

                            if (block1.getMaterial() == Material.air || block1 == Blocks.dirt || block1 == Blocks.snow || block1 == Blocks.ice || block1 == Blocks.packed_ice)
                            {
                                this.func_175906_a(worldIn, blockpos1, Blocks.packed_ice);
                                blockpos1 = blockpos1.down();
                                --l1;

                                if (l1 <= 0)
                                {
                                    blockpos1 = blockpos1.down(p_180709_2_.nextInt(5) + 1);
                                    l1 = p_180709_2_.nextInt(5);
                                }

                                continue;
                            }
                        }

                        ++l;
                        break;
                    }
                }
            }

            return true;
        }
    }
}