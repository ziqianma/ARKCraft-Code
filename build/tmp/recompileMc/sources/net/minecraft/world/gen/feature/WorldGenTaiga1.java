package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTaiga1 extends WorldGenAbstractTree
{
    private static final String __OBFID = "CL_00000427";

    public WorldGenTaiga1()
    {
        super(false);
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i = p_180709_2_.nextInt(5) + 7;
        int j = i - p_180709_2_.nextInt(2) - 3;
        int k = i - j;
        int l = 1 + p_180709_2_.nextInt(k + 1);
        boolean flag = true;

        if (p_180709_3_.getY() >= 1 && p_180709_3_.getY() + i + 1 <= 256)
        {
            int j1;
            int k1;
            int k2;

            for (int i1 = p_180709_3_.getY(); i1 <= p_180709_3_.getY() + 1 + i && flag; ++i1)
            {
                boolean flag1 = true;

                if (i1 - p_180709_3_.getY() < j)
                {
                    k2 = 0;
                }
                else
                {
                    k2 = l;
                }

                for (j1 = p_180709_3_.getX() - k2; j1 <= p_180709_3_.getX() + k2 && flag; ++j1)
                {
                    for (k1 = p_180709_3_.getZ() - k2; k1 <= p_180709_3_.getZ() + k2 && flag; ++k1)
                    {
                        if (i1 >= 0 && i1 < 256)
                        {
                            if (!this.isReplaceable(worldIn, new BlockPos(j1, i1, k1)))
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
                Block block = worldIn.getBlockState(down).getBlock();
                boolean isSoil = block.canSustainPlant(worldIn, down, net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling)Blocks.sapling);

                if (isSoil && p_180709_3_.getY() < 256 - i - 1)
                {
                    block.onPlantGrow(worldIn, down, p_180709_3_);
                    k2 = 0;

                    for (j1 = p_180709_3_.getY() + i; j1 >= p_180709_3_.getY() + j; --j1)
                    {
                        for (k1 = p_180709_3_.getX() - k2; k1 <= p_180709_3_.getX() + k2; ++k1)
                        {
                            int l1 = k1 - p_180709_3_.getX();

                            for (int i2 = p_180709_3_.getZ() - k2; i2 <= p_180709_3_.getZ() + k2; ++i2)
                            {
                                int j2 = i2 - p_180709_3_.getZ();

                                if (Math.abs(l1) != k2 || Math.abs(j2) != k2 || k2 <= 0)
                                {
                                    BlockPos blockpos1 = new BlockPos(k1, j1, i2);

                                    if (worldIn.getBlockState(blockpos1).getBlock().canBeReplacedByLeaves(worldIn, blockpos1))
                                    {
                                        this.func_175905_a(worldIn, blockpos1, Blocks.leaves, BlockPlanks.EnumType.SPRUCE.getMetadata());
                                    }
                                }
                            }
                        }

                        if (k2 >= 1 && j1 == p_180709_3_.getY() + j + 1)
                        {
                            --k2;
                        }
                        else if (k2 < l)
                        {
                            ++k2;
                        }
                    }

                    for (j1 = 0; j1 < i - 1; ++j1)
                    {
                        BlockPos upN = p_180709_3_.up(j1);
                        Block block1 = worldIn.getBlockState(upN).getBlock();

                        if (block1.isAir(worldIn, upN) || block1.isLeaves(worldIn, upN))
                        {
                            this.func_175905_a(worldIn, p_180709_3_.up(j1), Blocks.log, BlockPlanks.EnumType.SPRUCE.getMetadata());
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