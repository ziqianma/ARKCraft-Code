package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenIcePath extends WorldGenerator
{
    private Block field_150555_a;
    private int field_150554_b;
    private static final String __OBFID = "CL_00000416";

    public WorldGenIcePath(int p_i45454_1_)
    {
        this.field_150555_a = Blocks.packed_ice;
        this.field_150554_b = p_i45454_1_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        while (worldIn.isAirBlock(p_180709_3_) && p_180709_3_.getY() > 2)
        {
            p_180709_3_ = p_180709_3_.down();
        }

        if (worldIn.getBlockState(p_180709_3_).getBlock() != Blocks.snow)
        {
            return false;
        }
        else
        {
            int i = p_180709_2_.nextInt(this.field_150554_b - 2) + 2;
            byte b0 = 1;

            for (int j = p_180709_3_.getX() - i; j <= p_180709_3_.getX() + i; ++j)
            {
                for (int k = p_180709_3_.getZ() - i; k <= p_180709_3_.getZ() + i; ++k)
                {
                    int l = j - p_180709_3_.getX();
                    int i1 = k - p_180709_3_.getZ();

                    if (l * l + i1 * i1 <= i * i)
                    {
                        for (int j1 = p_180709_3_.getY() - b0; j1 <= p_180709_3_.getY() + b0; ++j1)
                        {
                            BlockPos blockpos1 = new BlockPos(j, j1, k);
                            Block block = worldIn.getBlockState(blockpos1).getBlock();

                            if (block == Blocks.dirt || block == Blocks.snow || block == Blocks.ice)
                            {
                                worldIn.setBlockState(blockpos1, this.field_150555_a.getDefaultState(), 2);
                            }
                        }
                    }
                }
            }

            return true;
        }
    }
}