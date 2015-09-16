package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenShrub extends WorldGenTrees
{
    private int field_150528_a;
    private int field_150527_b;
    private static final String __OBFID = "CL_00000411";

    public WorldGenShrub(int p_i2015_1_, int p_i2015_2_)
    {
        super(false);
        this.field_150527_b = p_i2015_1_;
        this.field_150528_a = p_i2015_2_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        Block block;

        do
        {
            block = worldIn.getBlockState(p_180709_3_).getBlock();
            if (!block.isAir(worldIn, p_180709_3_) && !block.isLeaves(worldIn, p_180709_3_)) break;
            p_180709_3_ = p_180709_3_.down();
        } while (p_180709_3_.getY() > 0);

        Block block1 = worldIn.getBlockState(p_180709_3_).getBlock();

        if (block1.canSustainPlant(worldIn, p_180709_3_, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.sapling)))
        {
            p_180709_3_ = p_180709_3_.up();
            this.func_175905_a(worldIn, p_180709_3_, Blocks.log, this.field_150527_b);

            for (int i = p_180709_3_.getY(); i <= p_180709_3_.getY() + 2; ++i)
            {
                int j = i - p_180709_3_.getY();
                int k = 2 - j;

                for (int l = p_180709_3_.getX() - k; l <= p_180709_3_.getX() + k; ++l)
                {
                    int i1 = l - p_180709_3_.getX();

                    for (int j1 = p_180709_3_.getZ() - k; j1 <= p_180709_3_.getZ() + k; ++j1)
                    {
                        int k1 = j1 - p_180709_3_.getZ();

                        if (Math.abs(i1) != k || Math.abs(k1) != k || p_180709_2_.nextInt(2) != 0)
                        {
                            BlockPos blockpos1 = new BlockPos(l, i, j1);

                            if (worldIn.getBlockState(blockpos1).getBlock().canBeReplacedByLeaves(worldIn, blockpos1))
                            {
                                this.func_175905_a(worldIn, blockpos1, Blocks.leaves, this.field_150528_a);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}