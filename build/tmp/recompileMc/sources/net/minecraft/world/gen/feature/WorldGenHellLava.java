package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenHellLava extends WorldGenerator
{
    private final Block field_150553_a;
    private final boolean field_94524_b;
    private static final String __OBFID = "CL_00000414";

    public WorldGenHellLava(Block p_i45453_1_, boolean p_i45453_2_)
    {
        this.field_150553_a = p_i45453_1_;
        this.field_94524_b = p_i45453_2_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        if (worldIn.getBlockState(p_180709_3_.up()).getBlock() != Blocks.netherrack)
        {
            return false;
        }
        else if (worldIn.getBlockState(p_180709_3_).getBlock().getMaterial() != Material.air && worldIn.getBlockState(p_180709_3_).getBlock() != Blocks.netherrack)
        {
            return false;
        }
        else
        {
            int i = 0;

            if (worldIn.getBlockState(p_180709_3_.west()).getBlock() == Blocks.netherrack)
            {
                ++i;
            }

            if (worldIn.getBlockState(p_180709_3_.east()).getBlock() == Blocks.netherrack)
            {
                ++i;
            }

            if (worldIn.getBlockState(p_180709_3_.north()).getBlock() == Blocks.netherrack)
            {
                ++i;
            }

            if (worldIn.getBlockState(p_180709_3_.south()).getBlock() == Blocks.netherrack)
            {
                ++i;
            }

            if (worldIn.getBlockState(p_180709_3_.down()).getBlock() == Blocks.netherrack)
            {
                ++i;
            }

            int j = 0;

            if (worldIn.isAirBlock(p_180709_3_.west()))
            {
                ++j;
            }

            if (worldIn.isAirBlock(p_180709_3_.east()))
            {
                ++j;
            }

            if (worldIn.isAirBlock(p_180709_3_.north()))
            {
                ++j;
            }

            if (worldIn.isAirBlock(p_180709_3_.south()))
            {
                ++j;
            }

            if (worldIn.isAirBlock(p_180709_3_.down()))
            {
                ++j;
            }

            if (!this.field_94524_b && i == 4 && j == 1 || i == 5)
            {
                worldIn.setBlockState(p_180709_3_, this.field_150553_a.getDefaultState(), 2);
                worldIn.forceBlockUpdateTick(this.field_150553_a, p_180709_3_, p_180709_2_);
            }

            return true;
        }
    }
}