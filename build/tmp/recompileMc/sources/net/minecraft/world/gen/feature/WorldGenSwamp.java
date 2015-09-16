package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenSwamp extends WorldGenAbstractTree
{
    private static final String __OBFID = "CL_00000436";

    public WorldGenSwamp()
    {
        super(false);
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i;

        for (i = p_180709_2_.nextInt(4) + 5; worldIn.getBlockState(p_180709_3_.down()).getBlock().getMaterial() == Material.water; p_180709_3_ = p_180709_3_.down())
        {
            ;
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
                    b0 = 3;
                }

                for (k = p_180709_3_.getX() - b0; k <= p_180709_3_.getX() + b0 && flag; ++k)
                {
                    for (l = p_180709_3_.getZ() - b0; l <= p_180709_3_.getZ() + b0 && flag; ++l)
                    {
                        if (j >= 0 && j < 256)
                        {
                            BlockPos pos = new BlockPos(k, j, l);
                            Block block = worldIn.getBlockState(pos).getBlock();

                            if (!block.isAir(worldIn, pos) && !block.isLeaves(worldIn, pos))
                            {
                                if (block != Blocks.water && block != Blocks.flowing_water)
                                {
                                    flag = false;
                                }
                                else if (j > p_180709_3_.getY())
                                {
                                    flag = false;
                                }
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
                    int i1;
                    BlockPos blockpos1;
                    int l1;
                    int i2;

                    for (l1 = p_180709_3_.getY() - 3 + i; l1 <= p_180709_3_.getY() + i; ++l1)
                    {
                        k = l1 - (p_180709_3_.getY() + i);
                        l = 2 - k / 2;

                        for (i2 = p_180709_3_.getX() - l; i2 <= p_180709_3_.getX() + l; ++i2)
                        {
                            i1 = i2 - p_180709_3_.getX();

                            for (int j1 = p_180709_3_.getZ() - l; j1 <= p_180709_3_.getZ() + l; ++j1)
                            {
                                int k1 = j1 - p_180709_3_.getZ();

                                if (Math.abs(i1) != l || Math.abs(k1) != l || p_180709_2_.nextInt(2) != 0 && k != 0)
                                {
                                    blockpos1 = new BlockPos(i2, l1, j1);

                                    if (worldIn.getBlockState(blockpos1).getBlock().canBeReplacedByLeaves(worldIn, blockpos1))
                                    {
                                        this.func_175906_a(worldIn, blockpos1, Blocks.leaves);
                                    }
                                }
                            }
                        }
                    }

                    for (l1 = 0; l1 < i; ++l1)
                    {
                        BlockPos upN = p_180709_3_.up(l1);
                        Block block2 = worldIn.getBlockState(upN).getBlock();

                        if (block2.isAir(worldIn, upN) || block2.isLeaves(worldIn, upN) || block2 == Blocks.flowing_water || block2 == Blocks.water)
                        {
                            this.func_175906_a(worldIn, p_180709_3_.up(l1), Blocks.log);
                        }
                    }

                    for (l1 = p_180709_3_.getY() - 3 + i; l1 <= p_180709_3_.getY() + i; ++l1)
                    {
                        k = l1 - (p_180709_3_.getY() + i);
                        l = 2 - k / 2;

                        for (i2 = p_180709_3_.getX() - l; i2 <= p_180709_3_.getX() + l; ++i2)
                        {
                            for (i1 = p_180709_3_.getZ() - l; i1 <= p_180709_3_.getZ() + l; ++i1)
                            {
                                BlockPos blockpos4 = new BlockPos(i2, l1, i1);

                                if (worldIn.getBlockState(blockpos4).getBlock().isLeaves(worldIn, blockpos4))
                                {
                                    BlockPos blockpos5 = blockpos4.west();
                                    blockpos1 = blockpos4.east();
                                    BlockPos blockpos2 = blockpos4.north();
                                    BlockPos blockpos3 = blockpos4.south();

                                    if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(blockpos5).getBlock().isAir(worldIn, blockpos5))
                                    {
                                        this.func_175922_a(worldIn, blockpos5, BlockVine.EAST_FLAG);
                                    }

                                    if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(blockpos1).getBlock().isAir(worldIn, blockpos1))
                                    {
                                        this.func_175922_a(worldIn, blockpos1, BlockVine.WEST_FLAG);
                                    }

                                    if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(blockpos2).getBlock().isAir(worldIn, blockpos2))
                                    {
                                        this.func_175922_a(worldIn, blockpos2, BlockVine.SOUTH_FLAG);
                                    }

                                    if (p_180709_2_.nextInt(4) == 0 && worldIn.getBlockState(blockpos3).getBlock().isAir(worldIn, blockpos3))
                                    {
                                        this.func_175922_a(worldIn, blockpos3, BlockVine.NORTH_FLAG);
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

    private void func_175922_a(World worldIn, BlockPos p_175922_2_, int p_175922_3_)
    {
        this.func_175905_a(worldIn, p_175922_2_, Blocks.vine, p_175922_3_);
        int j = 4;

        for (p_175922_2_ = p_175922_2_.down(); worldIn.getBlockState(p_175922_2_).getBlock().isAir(worldIn, p_175922_2_) && j > 0; --j)
        {
            this.func_175905_a(worldIn, p_175922_2_, Blocks.vine, p_175922_3_);
            p_175922_2_ = p_175922_2_.down();
        }
    }
}