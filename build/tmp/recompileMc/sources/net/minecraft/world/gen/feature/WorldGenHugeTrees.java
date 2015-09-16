package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class WorldGenHugeTrees extends WorldGenAbstractTree
{
    /** The base height of the tree */
    protected final int baseHeight;
    /** Sets the metadata for the wood blocks used */
    protected final int woodMetadata;
    /** Sets the metadata for the leaves used in huge trees */
    protected final int leavesMetadata;
    protected int field_150538_d;
    private static final String __OBFID = "CL_00000423";

    public WorldGenHugeTrees(boolean p_i45458_1_, int p_i45458_2_, int p_i45458_3_, int p_i45458_4_, int p_i45458_5_)
    {
        super(p_i45458_1_);
        this.baseHeight = p_i45458_2_;
        this.field_150538_d = p_i45458_3_;
        this.woodMetadata = p_i45458_4_;
        this.leavesMetadata = p_i45458_5_;
    }

    protected int func_150533_a(Random p_150533_1_)
    {
        int i = p_150533_1_.nextInt(3) + this.baseHeight;

        if (this.field_150538_d > 1)
        {
            i += p_150533_1_.nextInt(this.field_150538_d);
        }

        return i;
    }

    private boolean func_175926_c(World worldIn, BlockPos p_175926_2_, int p_175926_3_)
    {
        boolean flag = true;

        if (p_175926_2_.getY() >= 1 && p_175926_2_.getY() + p_175926_3_ + 1 <= 256)
        {
            for (int j = 0; j <= 1 + p_175926_3_; ++j)
            {
                byte b0 = 2;

                if (j == 0)
                {
                    b0 = 1;
                }
                else if (j >= 1 + p_175926_3_ - 2)
                {
                    b0 = 2;
                }

                for (int k = -b0; k <= b0 && flag; ++k)
                {
                    for (int l = -b0; l <= b0 && flag; ++l)
                    {
                        if (p_175926_2_.getY() + j < 0 || p_175926_2_.getY() + j >= 256 || !this.isReplaceable(worldIn, p_175926_2_.add(k, j, l)))
                        {
                            flag = false;
                        }
                    }
                }
            }

            return flag;
        }
        else
        {
            return false;
        }
    }

    private boolean func_175927_a(BlockPos p_175927_1_, World worldIn)
    {
        BlockPos blockpos1 = p_175927_1_.down();
        Block block = worldIn.getBlockState(blockpos1).getBlock();
        boolean isSoil = block.canSustainPlant(worldIn, blockpos1, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.sapling));

        if (isSoil && p_175927_1_.getY() >= 2)
        {
            this.onPlantGrow(worldIn, blockpos1, p_175927_1_);
            this.onPlantGrow(worldIn, blockpos1.east(), p_175927_1_);
            this.onPlantGrow(worldIn, blockpos1.south(), p_175927_1_);
            this.onPlantGrow(worldIn, blockpos1.south().east(), p_175927_1_);
            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean func_175929_a(World worldIn, Random p_175929_2_, BlockPos p_175929_3_, int p_175929_4_)
    {
        return this.func_175926_c(worldIn, p_175929_3_, p_175929_4_) && this.func_175927_a(p_175929_3_, worldIn);
    }

    protected void func_175925_a(World worldIn, BlockPos p_175925_2_, int p_175925_3_)
    {
        int j = p_175925_3_ * p_175925_3_;

        for (int k = -p_175925_3_; k <= p_175925_3_ + 1; ++k)
        {
            for (int l = -p_175925_3_; l <= p_175925_3_ + 1; ++l)
            {
                int i1 = k - 1;
                int j1 = l - 1;

                if (k * k + l * l <= j || i1 * i1 + j1 * j1 <= j || k * k + j1 * j1 <= j || i1 * i1 + l * l <= j)
                {
                    BlockPos blockpos1 = p_175925_2_.add(k, 0, l);
                    net.minecraft.block.state.IBlockState state = worldIn.getBlockState(blockpos1);

                    if (state.getBlock().isAir(worldIn, blockpos1) || state.getBlock().isLeaves(worldIn, blockpos1))
                    {
                        this.func_175905_a(worldIn, blockpos1, Blocks.leaves, this.leavesMetadata);
                    }
                }
            }
        }
    }

    protected void func_175928_b(World worldIn, BlockPos p_175928_2_, int p_175928_3_)
    {
        int j = p_175928_3_ * p_175928_3_;

        for (int k = -p_175928_3_; k <= p_175928_3_; ++k)
        {
            for (int l = -p_175928_3_; l <= p_175928_3_; ++l)
            {
                if (k * k + l * l <= j)
                {
                    BlockPos blockpos1 = p_175928_2_.add(k, 0, l);
                    Block block = worldIn.getBlockState(blockpos1).getBlock();

                    if (block.isAir(worldIn, blockpos1) || block.isLeaves(worldIn, blockpos1))
                    {
                        this.func_175905_a(worldIn, blockpos1, Blocks.leaves, this.leavesMetadata);
                    }
                }
            }
        }
    }

    //Just a helper macro
    private void onPlantGrow(World world, BlockPos pos, BlockPos source)
    {
        world.getBlockState(pos).getBlock().onPlantGrow(world, pos, source);
    }
}