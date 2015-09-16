package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenLiquids extends WorldGenerator
{
    private Block field_150521_a;
    private static final String __OBFID = "CL_00000434";

    public WorldGenLiquids(Block p_i45465_1_)
    {
        this.field_150521_a = p_i45465_1_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        if (worldIn.getBlockState(p_180709_3_.up()).getBlock() != Blocks.stone)
        {
            return false;
        }
        else if (worldIn.getBlockState(p_180709_3_.down()).getBlock() != Blocks.stone)
        {
            return false;
        }
        else if (worldIn.getBlockState(p_180709_3_).getBlock().getMaterial() != Material.air && worldIn.getBlockState(p_180709_3_).getBlock() != Blocks.stone)
        {
            return false;
        }
        else
        {
            int i = 0;

            if (worldIn.getBlockState(p_180709_3_.west()).getBlock() == Blocks.stone)
            {
                ++i;
            }

            if (worldIn.getBlockState(p_180709_3_.east()).getBlock() == Blocks.stone)
            {
                ++i;
            }

            if (worldIn.getBlockState(p_180709_3_.north()).getBlock() == Blocks.stone)
            {
                ++i;
            }

            if (worldIn.getBlockState(p_180709_3_.south()).getBlock() == Blocks.stone)
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

            if (i == 3 && j == 1)
            {
                worldIn.setBlockState(p_180709_3_, this.field_150521_a.getDefaultState(), 2);
                worldIn.forceBlockUpdateTick(this.field_150521_a, p_180709_3_, p_180709_2_);
            }

            return true;
        }
    }
}