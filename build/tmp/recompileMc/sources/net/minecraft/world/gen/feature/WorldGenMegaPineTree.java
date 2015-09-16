package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenMegaPineTree extends WorldGenHugeTrees
{
    private boolean field_150542_e;
    private static final String __OBFID = "CL_00000421";

    public WorldGenMegaPineTree(boolean p_i45457_1_, boolean p_i45457_2_)
    {
        super(p_i45457_1_, 13, 15, BlockPlanks.EnumType.SPRUCE.getMetadata(), BlockPlanks.EnumType.SPRUCE.getMetadata());
        this.field_150542_e = p_i45457_2_;
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
            this.func_150541_c(worldIn, p_180709_3_.getX(), p_180709_3_.getZ(), p_180709_3_.getY() + i, 0, p_180709_2_);

            for (int j = 0; j < i; ++j)
            {
                if (this.isAirLeaves(worldIn, p_180709_3_.up(j)))
                {
                    this.func_175905_a(worldIn, p_180709_3_.up(j), Blocks.log, this.woodMetadata);
                }

                if (j < i - 1)
                {
                    if (this.isAirLeaves(worldIn, p_180709_3_.add(1, j, 0)))
                    {
                        this.func_175905_a(worldIn, p_180709_3_.add(1, j, 0), Blocks.log, this.woodMetadata);
                    }

                    if (this.isAirLeaves(worldIn, p_180709_3_.add(1, j, 1)))
                    {
                        this.func_175905_a(worldIn, p_180709_3_.add(1, j, 1), Blocks.log, this.woodMetadata);
                    }

                    if (this.isAirLeaves(worldIn, p_180709_3_.add(0, j, 1)))
                    {
                        this.func_175905_a(worldIn, p_180709_3_.add(0, j, 1), Blocks.log, this.woodMetadata);
                    }
                }
            }

            return true;
        }
    }

    private void func_150541_c(World worldIn, int p_150541_2_, int p_150541_3_, int p_150541_4_, int p_150541_5_, Random p_150541_6_)
    {
        int i1 = p_150541_6_.nextInt(5) + (this.field_150542_e ? this.baseHeight : 3);
        int j1 = 0;

        for (int k1 = p_150541_4_ - i1; k1 <= p_150541_4_; ++k1)
        {
            int l1 = p_150541_4_ - k1;
            int i2 = p_150541_5_ + MathHelper.floor_float((float)l1 / (float)i1 * 3.5F);
            this.func_175925_a(worldIn, new BlockPos(p_150541_2_, k1, p_150541_3_), i2 + (l1 > 0 && i2 == j1 && (k1 & 1) == 0 ? 1 : 0));
            j1 = i2;
        }
    }

    public void func_180711_a(World worldIn, Random p_180711_2_, BlockPos p_180711_3_)
    {
        this.func_175933_b(worldIn, p_180711_3_.west().north());
        this.func_175933_b(worldIn, p_180711_3_.east(2).north());
        this.func_175933_b(worldIn, p_180711_3_.west().south(2));
        this.func_175933_b(worldIn, p_180711_3_.east(2).south(2));

        for (int i = 0; i < 5; ++i)
        {
            int j = p_180711_2_.nextInt(64);
            int k = j % 8;
            int l = j / 8;

            if (k == 0 || k == 7 || l == 0 || l == 7)
            {
                this.func_175933_b(worldIn, p_180711_3_.add(-3 + k, 0, -3 + l));
            }
        }
    }

    private void func_175933_b(World worldIn, BlockPos p_175933_2_)
    {
        for (int i = -2; i <= 2; ++i)
        {
            for (int j = -2; j <= 2; ++j)
            {
                if (Math.abs(i) != 2 || Math.abs(j) != 2)
                {
                    this.func_175934_c(worldIn, p_175933_2_.add(i, 0, j));
                }
            }
        }
    }

    private void func_175934_c(World worldIn, BlockPos p_175934_2_)
    {
        for (int i = 2; i >= -3; --i)
        {
            BlockPos blockpos1 = p_175934_2_.up(i);
            Block block = worldIn.getBlockState(blockpos1).getBlock();

            if (block.canSustainPlant(worldIn, blockpos1, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.sapling)))
            {
                this.func_175905_a(worldIn, blockpos1, Blocks.dirt, BlockDirt.DirtType.PODZOL.getMetadata());
                break;
            }

            if (!block.isAir(worldIn, blockpos1) && i < 0)
            {
                break;
            }
        }
    }

    //Helper macro
    private boolean isAirLeaves(World world, BlockPos pos)
    {
        net.minecraft.block.Block block = world.getBlockState(pos).getBlock();
        return block.isAir(world, pos) || block.isLeaves(world, pos);
    }
}