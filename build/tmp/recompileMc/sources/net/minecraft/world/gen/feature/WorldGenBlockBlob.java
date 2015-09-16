package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenBlockBlob extends WorldGenerator
{
    private final Block field_150545_a;
    private final int field_150544_b;
    private static final String __OBFID = "CL_00000402";

    public WorldGenBlockBlob(Block p_i45450_1_, int p_i45450_2_)
    {
        super(false);
        this.field_150545_a = p_i45450_1_;
        this.field_150544_b = p_i45450_2_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        while (true)
        {
            if (p_180709_3_.getY() > 3)
            {
                label47:
                {
                    if (!worldIn.isAirBlock(p_180709_3_.down()))
                    {
                        Block block = worldIn.getBlockState(p_180709_3_.down()).getBlock();

                        if (block == Blocks.grass || block == Blocks.dirt || block == Blocks.stone)
                        {
                            break label47;
                        }
                    }

                    p_180709_3_ = p_180709_3_.down();
                    continue;
                }
            }

            if (p_180709_3_.getY() <= 3)
            {
                return false;
            }

            int i1 = this.field_150544_b;

            for (int i = 0; i1 >= 0 && i < 3; ++i)
            {
                int j = i1 + p_180709_2_.nextInt(2);
                int k = i1 + p_180709_2_.nextInt(2);
                int l = i1 + p_180709_2_.nextInt(2);
                float f = (float)(j + k + l) * 0.333F + 0.5F;
                Iterator iterator = BlockPos.getAllInBox(p_180709_3_.add(-j, -k, -l), p_180709_3_.add(j, k, l)).iterator();

                while (iterator.hasNext())
                {
                    BlockPos blockpos1 = (BlockPos)iterator.next();

                    if (blockpos1.distanceSq(p_180709_3_) <= (double)(f * f))
                    {
                        worldIn.setBlockState(blockpos1, this.field_150545_a.getDefaultState(), 4);
                    }
                }

                p_180709_3_ = p_180709_3_.add(-(i1 + 1) + p_180709_2_.nextInt(2 + i1 * 2), 0 - p_180709_2_.nextInt(2), -(i1 + 1) + p_180709_2_.nextInt(2 + i1 * 2));
            }

            return true;
        }
    }
}