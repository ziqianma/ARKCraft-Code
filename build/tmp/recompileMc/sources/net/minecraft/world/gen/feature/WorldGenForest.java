package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenForest extends WorldGenAbstractTree
{
    private boolean field_150531_a;
    private static final String __OBFID = "CL_00000401";

    public WorldGenForest(boolean p_i45449_1_, boolean p_i45449_2_)
    {
        super(p_i45449_1_);
        this.field_150531_a = p_i45449_2_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i = p_180709_2_.nextInt(3) + 5;

        if (this.field_150531_a)
        {
            i += p_180709_2_.nextInt(7);
        }

        boolean flag = true;

        if (p_180709_3_.getY() >= 1 && p_180709_3_.getY() + i + 1 <= 256)
        {
            int k;
            int l;

            for (int j = p_180709_3_.getY(); j <= p_180709_3_.getY() + 1 + i; ++j)
            {
                byte b0 = 1;

                if (j == p_180709_3_.getY())
                {
                    b0 = 0;
                }

                if (j >= p_180709_3_.getY() + 1 + i - 2)
                {
                    b0 = 2;
                }

                for (k = p_180709_3_.getX() - b0; k <= p_180709_3_.getX() + b0 && flag; ++k)
                {
                    for (l = p_180709_3_.getZ() - b0; l <= p_180709_3_.getZ() + b0 && flag; ++l)
                    {
                        if (j >= 0 && j < 256)
                        {
                            if (!this.isReplaceable(worldIn, new BlockPos(k, j, l)))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                BlockPos down = p_180709_3_.down();
                Block block1 = worldIn.getBlockState(down).getBlock();
                boolean isSoil = block1.canSustainPlant(worldIn, down, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.sapling));

                if (isSoil && p_180709_3_.getY() < 256 - i - 1)
                {
                    block1.onPlantGrow(worldIn, down, p_180709_3_);
                    int i2;

                    for (i2 = p_180709_3_.getY() - 3 + i; i2 <= p_180709_3_.getY() + i; ++i2)
                    {
                        k = i2 - (p_180709_3_.getY() + i);
                        l = 1 - k / 2;

                        for (int i1 = p_180709_3_.getX() - l; i1 <= p_180709_3_.getX() + l; ++i1)
                        {
                            int j1 = i1 - p_180709_3_.getX();

                            for (int k1 = p_180709_3_.getZ() - l; k1 <= p_180709_3_.getZ() + l; ++k1)
                            {
                                int l1 = k1 - p_180709_3_.getZ();

                                if (Math.abs(j1) != l || Math.abs(l1) != l || p_180709_2_.nextInt(2) != 0 && k != 0)
                                {
                                    BlockPos blockpos1 = new BlockPos(i1, i2, k1);
                                    Block block = worldIn.getBlockState(blockpos1).getBlock();

                                    if (block.isAir(worldIn, blockpos1) || block.isLeaves(worldIn, blockpos1))
                                    {
                                        this.func_175905_a(worldIn, blockpos1, Blocks.leaves, BlockPlanks.EnumType.BIRCH.getMetadata());
                                    }
                                }
                            }
                        }
                    }

                    for (i2 = 0; i2 < i; ++i2)
                    {
                        BlockPos upN = p_180709_3_.up(i2);
                        Block block2 = worldIn.getBlockState(upN).getBlock();

                        if (block2.isAir(worldIn, upN) || block2.isLeaves(worldIn, upN))
                        {
                            this.func_175905_a(worldIn, p_180709_3_.up(i2), Blocks.log, BlockPlanks.EnumType.BIRCH.getMetadata());
                        }
                    }

                    return true;
                }
                else
                {
                    return false;
                }
            }
        }
        else
        {
            return false;
        }
    }
}