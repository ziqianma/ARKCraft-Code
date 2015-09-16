package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenVines extends WorldGenerator
{
    private static final String __OBFID = "CL_00000439";

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        for (; p_180709_3_.getY() < 128; p_180709_3_ = p_180709_3_.up())
        {
            if (worldIn.isAirBlock(p_180709_3_))
            {
                EnumFacing[] aenumfacing = EnumFacing.Plane.HORIZONTAL.facings();
                int i = aenumfacing.length;

                for (int j = 0; j < i; ++j)
                {
                    EnumFacing enumfacing = aenumfacing[j];

                    if (Blocks.vine.canPlaceBlockOnSide(worldIn, p_180709_3_, enumfacing))
                    {
                        IBlockState iblockstate = Blocks.vine.getDefaultState().withProperty(BlockVine.NORTH, Boolean.valueOf(enumfacing == EnumFacing.NORTH)).withProperty(BlockVine.EAST, Boolean.valueOf(enumfacing == EnumFacing.EAST)).withProperty(BlockVine.SOUTH, Boolean.valueOf(enumfacing == EnumFacing.SOUTH)).withProperty(BlockVine.WEST, Boolean.valueOf(enumfacing == EnumFacing.WEST));
                        worldIn.setBlockState(p_180709_3_, iblockstate, 2);
                        break;
                    }
                }
            }
            else
            {
                p_180709_3_ = p_180709_3_.add(p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), 0, p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4));
            }
        }

        return true;
    }
}