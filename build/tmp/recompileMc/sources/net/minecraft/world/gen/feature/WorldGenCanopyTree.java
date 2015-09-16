package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenCanopyTree extends WorldGenAbstractTree
{
    private static final String __OBFID = "CL_00000430";

    public WorldGenCanopyTree(boolean p_i45461_1_)
    {
        super(p_i45461_1_);
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i = p_180709_2_.nextInt(3) + p_180709_2_.nextInt(2) + 6;
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
                net.minecraft.block.state.IBlockState state = worldIn.getBlockState(down);
                boolean isSoil = state.getBlock().canSustainPlant(worldIn, down, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.sapling));

                if (isSoil && p_180709_3_.getY() < 256 - i - 1)
                {
                    this.onPlantGrow(worldIn, p_180709_3_.down(), p_180709_3_);
                    this.onPlantGrow(worldIn, p_180709_3_.add(1, -1, 0), p_180709_3_);
                    this.onPlantGrow(worldIn, p_180709_3_.add(1, -1, 1), p_180709_3_);
                    this.onPlantGrow(worldIn, p_180709_3_.add(0, -1, 1), p_180709_3_);
                    EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(p_180709_2_);
                    k = i - p_180709_2_.nextInt(4);
                    l = 2 - p_180709_2_.nextInt(3);
                    int i1 = p_180709_3_.getX();
                    int j1 = p_180709_3_.getZ();
                    int k1 = 0;
                    int l1;
                    int i2;

                    for (l1 = 0; l1 < i; ++l1)
                    {
                        i2 = p_180709_3_.getY() + l1;

                        if (l1 >= k && l > 0)
                        {
                            i1 += enumfacing.getFrontOffsetX();
                            j1 += enumfacing.getFrontOffsetZ();
                            --l;
                        }

                        BlockPos blockpos1 = new BlockPos(i1, i2, j1);
                        state = worldIn.getBlockState(blockpos1);

                        if (state.getBlock().isAir(worldIn, blockpos1) || state.getBlock().isLeaves(worldIn, blockpos1))
                        {
                            this.func_175905_a(worldIn, blockpos1, Blocks.log2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4);
                            this.func_175905_a(worldIn, blockpos1.east(), Blocks.log2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4);
                            this.func_175905_a(worldIn, blockpos1.south(), Blocks.log2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4);
                            this.func_175905_a(worldIn, blockpos1.east().south(), Blocks.log2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4);
                            k1 = i2;
                        }
                    }

                    for (l1 = -2; l1 <= 0; ++l1)
                    {
                        for (i2 = -2; i2 <= 0; ++i2)
                        {
                            byte b1 = -1;
                            this.func_150526_a(worldIn, i1 + l1, k1 + b1, j1 + i2);
                            this.func_150526_a(worldIn, 1 + i1 - l1, k1 + b1, j1 + i2);
                            this.func_150526_a(worldIn, i1 + l1, k1 + b1, 1 + j1 - i2);
                            this.func_150526_a(worldIn, 1 + i1 - l1, k1 + b1, 1 + j1 - i2);

                            if ((l1 > -2 || i2 > -1) && (l1 != -1 || i2 != -2))
                            {
                                byte b2 = 1;
                                this.func_150526_a(worldIn, i1 + l1, k1 + b2, j1 + i2);
                                this.func_150526_a(worldIn, 1 + i1 - l1, k1 + b2, j1 + i2);
                                this.func_150526_a(worldIn, i1 + l1, k1 + b2, 1 + j1 - i2);
                                this.func_150526_a(worldIn, 1 + i1 - l1, k1 + b2, 1 + j1 - i2);
                            }
                        }
                    }

                    if (p_180709_2_.nextBoolean())
                    {
                        this.func_150526_a(worldIn, i1, k1 + 2, j1);
                        this.func_150526_a(worldIn, i1 + 1, k1 + 2, j1);
                        this.func_150526_a(worldIn, i1 + 1, k1 + 2, j1 + 1);
                        this.func_150526_a(worldIn, i1, k1 + 2, j1 + 1);
                    }

                    for (l1 = -3; l1 <= 4; ++l1)
                    {
                        for (i2 = -3; i2 <= 4; ++i2)
                        {
                            if ((l1 != -3 || i2 != -3) && (l1 != -3 || i2 != 4) && (l1 != 4 || i2 != -3) && (l1 != 4 || i2 != 4) && (Math.abs(l1) < 3 || Math.abs(i2) < 3))
                            {
                                this.func_150526_a(worldIn, i1 + l1, k1, j1 + i2);
                            }
                        }
                    }

                    for (l1 = -1; l1 <= 2; ++l1)
                    {
                        for (i2 = -1; i2 <= 2; ++i2)
                        {
                            if ((l1 < 0 || l1 > 1 || i2 < 0 || i2 > 1) && p_180709_2_.nextInt(3) <= 0)
                            {
                                int k2 = p_180709_2_.nextInt(3) + 2;
                                int l2;

                                for (l2 = 0; l2 < k2; ++l2)
                                {
                                    this.func_175905_a(worldIn, new BlockPos(p_180709_3_.getX() + l1, k1 - l2 - 1, p_180709_3_.getZ() + i2), Blocks.log2, BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4);
                                }

                                int j2;

                                for (l2 = -1; l2 <= 1; ++l2)
                                {
                                    for (j2 = -1; j2 <= 1; ++j2)
                                    {
                                        this.func_150526_a(worldIn, i1 + l1 + l2, k1 - 0, j1 + i2 + j2);
                                    }
                                }

                                for (l2 = -2; l2 <= 2; ++l2)
                                {
                                    for (j2 = -2; j2 <= 2; ++j2)
                                    {
                                        if (Math.abs(l2) != 2 || Math.abs(j2) != 2)
                                        {
                                            this.func_150526_a(worldIn, i1 + l1 + l2, k1 - 1, j1 + i2 + j2);
                                        }
                                    }
                                }
                            }
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

    private void func_150526_a(World worldIn, int p_150526_2_, int p_150526_3_, int p_150526_4_)
    {
        BlockPos pos = new BlockPos(p_150526_2_, p_150526_3_, p_150526_4_);
        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);

        if (state.getBlock().isAir(worldIn, pos))
        {
            this.func_175905_a(worldIn, new BlockPos(p_150526_2_, p_150526_3_, p_150526_4_), Blocks.leaves2, 1);
        }
    }

    //Just a helper macro
    private void onPlantGrow(World world, BlockPos pos, BlockPos source)
    {
        world.getBlockState(pos).getBlock().onPlantGrow(world, pos, source);
    }
}