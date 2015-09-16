package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenDoublePlant extends WorldGenerator
{
    private BlockDoublePlant.EnumPlantType field_150549_a;
    private static final String __OBFID = "CL_00000408";

    public void func_180710_a(BlockDoublePlant.EnumPlantType p_180710_1_)
    {
        this.field_150549_a = p_180710_1_;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        boolean flag = false;

        for (int i = 0; i < 64; ++i)
        {
            BlockPos blockpos1 = p_180709_3_.add(p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8), p_180709_2_.nextInt(4) - p_180709_2_.nextInt(4), p_180709_2_.nextInt(8) - p_180709_2_.nextInt(8));

            if (worldIn.isAirBlock(blockpos1) && (!worldIn.provider.getHasNoSky() || blockpos1.getY() < 254) && Blocks.double_plant.canPlaceBlockAt(worldIn, blockpos1))
            {
                Blocks.double_plant.placeAt(worldIn, blockpos1, this.field_150549_a, 2);
                flag = true;
            }
        }

        return flag;
    }
}