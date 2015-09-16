package net.minecraft.world.gen.feature;

import com.google.common.base.Predicates;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenDesertWells extends WorldGenerator
{
    private static final BlockStateHelper field_175913_a = BlockStateHelper.forBlock(Blocks.sand).where(BlockSand.VARIANT, Predicates.equalTo(BlockSand.EnumType.SAND));
    private final IBlockState field_175911_b;
    private final IBlockState field_175912_c;
    private final IBlockState field_175910_d;
    private static final String __OBFID = "CL_00000407";

    public WorldGenDesertWells()
    {
        this.field_175911_b = Blocks.stone_slab.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        this.field_175912_c = Blocks.sandstone.getDefaultState();
        this.field_175910_d = Blocks.flowing_water.getDefaultState();
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        while (worldIn.isAirBlock(p_180709_3_) && p_180709_3_.getY() > 2)
        {
            p_180709_3_ = p_180709_3_.down();
        }

        if (!field_175913_a.matchesState(worldIn.getBlockState(p_180709_3_)))
        {
            return false;
        }
        else
        {
            int i;
            int j;

            for (i = -2; i <= 2; ++i)
            {
                for (j = -2; j <= 2; ++j)
                {
                    if (worldIn.isAirBlock(p_180709_3_.add(i, -1, j)) && worldIn.isAirBlock(p_180709_3_.add(i, -2, j)))
                    {
                        return false;
                    }
                }
            }

            for (i = -1; i <= 0; ++i)
            {
                for (j = -2; j <= 2; ++j)
                {
                    for (int k = -2; k <= 2; ++k)
                    {
                        worldIn.setBlockState(p_180709_3_.add(j, i, k), this.field_175912_c, 2);
                    }
                }
            }

            worldIn.setBlockState(p_180709_3_, this.field_175910_d, 2);
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext())
            {
                EnumFacing enumfacing = (EnumFacing)iterator.next();
                worldIn.setBlockState(p_180709_3_.offset(enumfacing), this.field_175910_d, 2);
            }

            for (i = -2; i <= 2; ++i)
            {
                for (j = -2; j <= 2; ++j)
                {
                    if (i == -2 || i == 2 || j == -2 || j == 2)
                    {
                        worldIn.setBlockState(p_180709_3_.add(i, 1, j), this.field_175912_c, 2);
                    }
                }
            }

            worldIn.setBlockState(p_180709_3_.add(2, 1, 0), this.field_175911_b, 2);
            worldIn.setBlockState(p_180709_3_.add(-2, 1, 0), this.field_175911_b, 2);
            worldIn.setBlockState(p_180709_3_.add(0, 1, 2), this.field_175911_b, 2);
            worldIn.setBlockState(p_180709_3_.add(0, 1, -2), this.field_175911_b, 2);

            for (i = -1; i <= 1; ++i)
            {
                for (j = -1; j <= 1; ++j)
                {
                    if (i == 0 && j == 0)
                    {
                        worldIn.setBlockState(p_180709_3_.add(i, 4, j), this.field_175912_c, 2);
                    }
                    else
                    {
                        worldIn.setBlockState(p_180709_3_.add(i, 4, j), this.field_175911_b, 2);
                    }
                }
            }

            for (i = 1; i <= 3; ++i)
            {
                worldIn.setBlockState(p_180709_3_.add(-1, i, -1), this.field_175912_c, 2);
                worldIn.setBlockState(p_180709_3_.add(-1, i, 1), this.field_175912_c, 2);
                worldIn.setBlockState(p_180709_3_.add(1, i, -1), this.field_175912_c, 2);
                worldIn.setBlockState(p_180709_3_.add(1, i, 1), this.field_175912_c, 2);
            }

            return true;
        }
    }
}