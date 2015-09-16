package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTaiga2 extends WorldGenAbstractTree
{
    private static final String __OBFID = "CL_00000435";

    public WorldGenTaiga2(boolean p_i2025_1_)
    {
        super(p_i2025_1_);
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i = p_180709_2_.nextInt(4) + 6;
        int j = 1 + p_180709_2_.nextInt(2);
        int k = i - j;
        int l = 2 + p_180709_2_.nextInt(2);
        boolean flag = true;

        if (p_180709_3_.getY() >= 1 && p_180709_3_.getY() + i + 1 <= 256)
        {
            int j1;
            int i3;

            for (int i1 = p_180709_3_.getY(); i1 <= p_180709_3_.getY() + 1 + i && flag; ++i1)
            {
                boolean flag1 = true;

                if (i1 - p_180709_3_.getY() < j)
                {
                    i3 = 0;
                }
                else
                {
                    i3 = l;
                }

                for (j1 = p_180709_3_.getX() - i3; j1 <= p_180709_3_.getX() + i3 && flag; ++j1)
                {
                    for (int k1 = p_180709_3_.getZ() - i3; k1 <= p_180709_3_.getZ() + i3 && flag; ++k1)
                    {
                        if (i1 >= 0 && i1 < 256)
                        {
                            BlockPos off = new BlockPos(j1, i1, k1);
                            Block block = worldIn.getBlockState(off).getBlock();

                            if (!block.isAir(worldIn, off) && !block.isLeaves(worldIn, off))
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
                boolean isSoil = block1.canSustainPlant(worldIn, down, net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling)Blocks.sapling);

                if (isSoil && p_180709_3_.getY() < 256 - i - 1)
                {
                    block1.onPlantGrow(worldIn, down, p_180709_3_);
                    i3 = p_180709_2_.nextInt(2);
                    j1 = 1;
                    byte b0 = 0;
                    int l1;
                    int j3;

                    for (j3 = 0; j3 <= k; ++j3)
                    {
                        l1 = p_180709_3_.getY() + i - j3;

                        for (int i2 = p_180709_3_.getX() - i3; i2 <= p_180709_3_.getX() + i3; ++i2)
                        {
                            int j2 = i2 - p_180709_3_.getX();

                            for (int k2 = p_180709_3_.getZ() - i3; k2 <= p_180709_3_.getZ() + i3; ++k2)
                            {
                                int l2 = k2 - p_180709_3_.getZ();

                                if (Math.abs(j2) != i3 || Math.abs(l2) != i3 || i3 <= 0)
                                {
                                    BlockPos blockpos1 = new BlockPos(i2, l1, k2);

                                    if (worldIn.getBlockState(blockpos1).getBlock().canBeReplacedByLeaves(worldIn, blockpos1))
                                    {
                                        this.func_175905_a(worldIn, blockpos1, Blocks.leaves, BlockPlanks.EnumType.SPRUCE.getMetadata());
                                    }
                                }
                            }
                        }

                        if (i3 >= j1)
                        {
                            i3 = b0;
                            b0 = 1;
                            ++j1;

                            if (j1 > l)
                            {
                                j1 = l;
                            }
                        }
                        else
                        {
                            ++i3;
                        }
                    }

                    j3 = p_180709_2_.nextInt(3);

                    for (l1 = 0; l1 < i - j3; ++l1)
                    {
                        BlockPos upN = p_180709_3_.up(l1);
                        Block block2 = worldIn.getBlockState(upN).getBlock();

                        if (block2.isAir(worldIn, upN) || block2.isLeaves(worldIn, upN))
                        {
                            this.func_175905_a(worldIn, p_180709_3_.up(l1), Blocks.log, BlockPlanks.EnumType.SPRUCE.getMetadata());
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