package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaJungle extends WorldGenHugeTrees
{
    private static final String __OBFID = "CL_00000420";

    public WorldGenMegaJungle(boolean p_i45456_1_, int p_i45456_2_, int p_i45456_3_, int p_i45456_4_, int p_i45456_5_)
    {
        super(p_i45456_1_, p_i45456_2_, p_i45456_3_, p_i45456_4_, p_i45456_5_);
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        int i = this.func_150533_a(p_180709_2_);

        if (!this.func_175929_a(worldIn, p_180709_2_, p_180709_3_, i))
        {
            return false;
        }
        else
        {
            this.func_175930_c(worldIn, p_180709_3_.up(i), 2);

            for (int j = p_180709_3_.getY() + i - 2 - p_180709_2_.nextInt(4); j > p_180709_3_.getY() + i / 2; j -= 2 + p_180709_2_.nextInt(4))
            {
                float f = p_180709_2_.nextFloat() * (float)Math.PI * 2.0F;
                int k = p_180709_3_.getX() + (int)(0.5F + MathHelper.cos(f) * 4.0F);
                int l = p_180709_3_.getZ() + (int)(0.5F + MathHelper.sin(f) * 4.0F);
                int i1;

                for (i1 = 0; i1 < 5; ++i1)
                {
                    k = p_180709_3_.getX() + (int)(1.5F + MathHelper.cos(f) * (float)i1);
                    l = p_180709_3_.getZ() + (int)(1.5F + MathHelper.sin(f) * (float)i1);
                    this.func_175905_a(worldIn, new BlockPos(k, j - 3 + i1 / 2, l), Blocks.log, this.woodMetadata);
                }

                i1 = 1 + p_180709_2_.nextInt(2);
                int j1 = j;

                for (int k1 = j - i1; k1 <= j1; ++k1)
                {
                    int l1 = k1 - j1;
                    this.func_175928_b(worldIn, new BlockPos(k, k1, l), 1 - l1);
                }
            }

            for (int i2 = 0; i2 < i; ++i2)
            {
                BlockPos blockpos1 = p_180709_3_.up(i2);

                if (this.isAirLeaves(worldIn, blockpos1))
                {
                    this.func_175905_a(worldIn, blockpos1, Blocks.log, this.woodMetadata);

                    if (i2 > 0)
                    {
                        this.func_175932_b(worldIn, p_180709_2_, blockpos1.west(), BlockVine.EAST_FLAG);
                        this.func_175932_b(worldIn, p_180709_2_, blockpos1.north(), BlockVine.SOUTH_FLAG);
                    }
                }

                if (i2 < i - 1)
                {
                    BlockPos blockpos2 = blockpos1.east();

                    if (this.isAirLeaves(worldIn, blockpos2))
                    {
                        this.func_175905_a(worldIn, blockpos2, Blocks.log, this.woodMetadata);

                        if (i2 > 0)
                        {
                            this.func_175932_b(worldIn, p_180709_2_, blockpos2.east(), BlockVine.WEST_FLAG);
                            this.func_175932_b(worldIn, p_180709_2_, blockpos2.north(), BlockVine.SOUTH_FLAG);
                        }
                    }

                    BlockPos blockpos3 = blockpos1.south().east();

                    if (this.isAirLeaves(worldIn, blockpos3))
                    {
                        this.func_175905_a(worldIn, blockpos3, Blocks.log, this.woodMetadata);

                        if (i2 > 0)
                        {
                            this.func_175932_b(worldIn, p_180709_2_, blockpos3.east(), BlockVine.WEST_FLAG);
                            this.func_175932_b(worldIn, p_180709_2_, blockpos3.south(), BlockVine.NORTH_FLAG);
                        }
                    }

                    BlockPos blockpos4 = blockpos1.south();

                    if (this.isAirLeaves(worldIn, blockpos4))
                    {
                        this.func_175905_a(worldIn, blockpos4, Blocks.log, this.woodMetadata);

                        if (i2 > 0)
                        {
                            this.func_175932_b(worldIn, p_180709_2_, blockpos4.west(), BlockVine.EAST_FLAG);
                            this.func_175932_b(worldIn, p_180709_2_, blockpos4.south(), BlockVine.NORTH_FLAG);
                        }
                    }
                }
            }

            return true;
        }
    }

    private void func_175932_b(World worldIn, Random p_175932_2_, BlockPos p_175932_3_, int p_175932_4_)
    {
        if (p_175932_2_.nextInt(3) > 0 && worldIn.isAirBlock(p_175932_3_))
        {
            this.func_175905_a(worldIn, p_175932_3_, Blocks.vine, p_175932_4_);
        }
    }

    private void func_175930_c(World worldIn, BlockPos p_175930_2_, int p_175930_3_)
    {
        byte b0 = 2;

        for (int j = -b0; j <= 0; ++j)
        {
            this.func_175925_a(worldIn, p_175930_2_.up(j), p_175930_3_ + 1 - j);
        }
    }

    //Helper macro
    private boolean isAirLeaves(World world, BlockPos pos)
    {
        net.minecraft.block.Block block = world.getBlockState(pos).getBlock();
        return block.isAir(world, pos) || block.isLeaves(world, pos);
    }
}