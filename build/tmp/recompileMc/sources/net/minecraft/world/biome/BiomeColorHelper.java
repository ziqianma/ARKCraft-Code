package net.minecraft.world.biome;

import java.util.Iterator;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BiomeColorHelper
{
    private static final BiomeColorHelper.ColorResolver field_180291_a = new BiomeColorHelper.ColorResolver()
    {
        private static final String __OBFID = "CL_00002148";
        public int getColorAtPos(BiomeGenBase p_180283_1_, BlockPos blockPosition)
        {
            return p_180283_1_.getGrassColorAtPos(blockPosition);
        }
    };
    private static final BiomeColorHelper.ColorResolver field_180289_b = new BiomeColorHelper.ColorResolver()
    {
        private static final String __OBFID = "CL_00002147";
        public int getColorAtPos(BiomeGenBase p_180283_1_, BlockPos blockPosition)
        {
            return p_180283_1_.getFoliageColorAtPos(blockPosition);
        }
    };
    private static final BiomeColorHelper.ColorResolver field_180290_c = new BiomeColorHelper.ColorResolver()
    {
        private static final String __OBFID = "CL_00002146";
        public int getColorAtPos(BiomeGenBase p_180283_1_, BlockPos blockPosition)
        {
            return p_180283_1_.getWaterColorMultiplier();
        }
    };
    private static final String __OBFID = "CL_00002149";

    private static int func_180285_a(IBlockAccess p_180285_0_, BlockPos p_180285_1_, BiomeColorHelper.ColorResolver p_180285_2_)
    {
        int i = 0;
        int j = 0;
        int k = 0;
        int l;

        for (Iterator iterator = BlockPos.getAllInBoxMutable(p_180285_1_.add(-1, 0, -1), p_180285_1_.add(1, 0, 1)).iterator(); iterator.hasNext(); k += l & 255)
        {
            BlockPos.MutableBlockPos mutableblockpos = (BlockPos.MutableBlockPos)iterator.next();
            l = p_180285_2_.getColorAtPos(p_180285_0_.getBiomeGenForCoords(mutableblockpos), mutableblockpos);
            i += (l & 16711680) >> 16;
            j += (l & 65280) >> 8;
        }

        return (i / 9 & 255) << 16 | (j / 9 & 255) << 8 | k / 9 & 255;
    }

    public static int getGrassColorAtPos(IBlockAccess p_180286_0_, BlockPos p_180286_1_)
    {
        return func_180285_a(p_180286_0_, p_180286_1_, field_180291_a);
    }

    public static int getFoliageColorAtPos(IBlockAccess p_180287_0_, BlockPos p_180287_1_)
    {
        return func_180285_a(p_180287_0_, p_180287_1_, field_180289_b);
    }

    public static int getWaterColorAtPos(IBlockAccess p_180288_0_, BlockPos p_180288_1_)
    {
        return func_180285_a(p_180288_0_, p_180288_1_, field_180290_c);
    }

    @SideOnly(Side.CLIENT)
    interface ColorResolver
    {
        int getColorAtPos(BiomeGenBase p_180283_1_, BlockPos blockPosition);
    }
}