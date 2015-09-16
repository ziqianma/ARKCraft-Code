package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenBigMushroom extends WorldGenerator
{
    /** The mushroom type. 0 for brown, 1 for red. */
    private int mushroomType = -1;
    private static final String __OBFID = "CL_00000415";

    public WorldGenBigMushroom(int p_i2017_1_)
    {
        super(true);
        this.mushroomType = p_i2017_1_;
    }

    public WorldGenBigMushroom()
    {
        super(false);
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i = p_180709_2_.nextInt(2);

        if (this.mushroomType >= 0)
        {
            i = this.mushroomType;
        }

        int j = p_180709_2_.nextInt(3) + 4;
        boolean flag = true;

        if (p_180709_3_.getY() >= 1 && p_180709_3_.getY() + j + 1 < 256)
        {
            int l;
            int i1;

            for (int k = p_180709_3_.getY(); k <= p_180709_3_.getY() + 1 + j; ++k)
            {
                byte b0 = 3;

                if (k <= p_180709_3_.getY() + 3)
                {
                    b0 = 0;
                }

                for (l = p_180709_3_.getX() - b0; l <= p_180709_3_.getX() + b0 && flag; ++l)
                {
                    for (i1 = p_180709_3_.getZ() - b0; i1 <= p_180709_3_.getZ() + b0 && flag; ++i1)
                    {
                        if (k >= 0 && k < 256)
                        {
                            BlockPos pos = new BlockPos(l, k, i1);
                            net.minecraft.block.state.IBlockState state = worldIn.getBlockState(pos);

                            if (!state.getBlock().isAir(worldIn, pos) && !state.getBlock().isLeaves(worldIn, pos))
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
                Block block1 = worldIn.getBlockState(p_180709_3_.down()).getBlock();

                if (block1 != Blocks.dirt && block1 != Blocks.grass && block1 != Blocks.mycelium)
                {
                    return false;
                }
                else
                {
                    int l1 = p_180709_3_.getY() + j;

                    if (i == 1)
                    {
                        l1 = p_180709_3_.getY() + j - 3;
                    }

                    for (l = l1; l <= p_180709_3_.getY() + j; ++l)
                    {
                        i1 = 1;

                        if (l < p_180709_3_.getY() + j)
                        {
                            ++i1;
                        }

                        if (i == 0)
                        {
                            i1 = 3;
                        }

                        for (int i2 = p_180709_3_.getX() - i1; i2 <= p_180709_3_.getX() + i1; ++i2)
                        {
                            for (int j1 = p_180709_3_.getZ() - i1; j1 <= p_180709_3_.getZ() + i1; ++j1)
                            {
                                int k1 = 5;

                                if (i2 == p_180709_3_.getX() - i1)
                                {
                                    --k1;
                                }

                                if (i2 == p_180709_3_.getX() + i1)
                                {
                                    ++k1;
                                }

                                if (j1 == p_180709_3_.getZ() - i1)
                                {
                                    k1 -= 3;
                                }

                                if (j1 == p_180709_3_.getZ() + i1)
                                {
                                    k1 += 3;
                                }

                                if (i == 0 || l < p_180709_3_.getY() + j)
                                {
                                    if ((i2 == p_180709_3_.getX() - i1 || i2 == p_180709_3_.getX() + i1) && (j1 == p_180709_3_.getZ() - i1 || j1 == p_180709_3_.getZ() + i1))
                                    {
                                        continue;
                                    }

                                    if (i2 == p_180709_3_.getX() - (i1 - 1) && j1 == p_180709_3_.getZ() - i1)
                                    {
                                        k1 = 1;
                                    }

                                    if (i2 == p_180709_3_.getX() - i1 && j1 == p_180709_3_.getZ() - (i1 - 1))
                                    {
                                        k1 = 1;
                                    }

                                    if (i2 == p_180709_3_.getX() + (i1 - 1) && j1 == p_180709_3_.getZ() - i1)
                                    {
                                        k1 = 3;
                                    }

                                    if (i2 == p_180709_3_.getX() + i1 && j1 == p_180709_3_.getZ() - (i1 - 1))
                                    {
                                        k1 = 3;
                                    }

                                    if (i2 == p_180709_3_.getX() - (i1 - 1) && j1 == p_180709_3_.getZ() + i1)
                                    {
                                        k1 = 7;
                                    }

                                    if (i2 == p_180709_3_.getX() - i1 && j1 == p_180709_3_.getZ() + (i1 - 1))
                                    {
                                        k1 = 7;
                                    }

                                    if (i2 == p_180709_3_.getX() + (i1 - 1) && j1 == p_180709_3_.getZ() + i1)
                                    {
                                        k1 = 9;
                                    }

                                    if (i2 == p_180709_3_.getX() + i1 && j1 == p_180709_3_.getZ() + (i1 - 1))
                                    {
                                        k1 = 9;
                                    }
                                }

                                if (k1 == 5 && l < p_180709_3_.getY() + j)
                                {
                                    k1 = 0;
                                }

                                if (k1 != 0 || p_180709_3_.getY() >= p_180709_3_.getY() + j - 1)
                                {
                                    BlockPos blockpos1 = new BlockPos(i2, l, j1);

                                    if (worldIn.getBlockState(blockpos1).getBlock().canBeReplacedByLeaves(worldIn, blockpos1))
                                    {
                                        this.func_175905_a(worldIn, blockpos1, Block.getBlockById(Block.getIdFromBlock(Blocks.brown_mushroom_block) + i), k1);
                                    }
                                }
                            }
                        }
                    }

                    for (l = 0; l < j; ++l)
                    {
                        BlockPos upN = p_180709_3_.up(l);
                        net.minecraft.block.state.IBlockState state = worldIn.getBlockState(upN);

                        if (state.getBlock().canBeReplacedByLeaves(worldIn, upN))
                        {
                            this.func_175905_a(worldIn, p_180709_3_.up(l), Block.getBlockById(Block.getIdFromBlock(Blocks.brown_mushroom_block) + i), 10);
                        }
                    }

                    return true;
                }
            }
        }
        else
        {
            return false;
        }
    }
}