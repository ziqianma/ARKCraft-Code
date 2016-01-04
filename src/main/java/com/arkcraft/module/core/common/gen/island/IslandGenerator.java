package com.arkcraft.module.core.common.gen.island;

import net.ilexiconn.llibrary.common.world.gen.gen.WorldHeightmapGenerator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;

import static net.minecraft.world.biome.BiomeGenBase.*;

/**
 * @author gegy1000
 */
public class IslandGenerator extends WorldHeightmapGenerator
{
    @Override
    public String getBiomeMapLocation()
    {
        return "/assets/arkcraft/textures/map/theisland/biomemap/theisland_biomemap.png";
    }

    @Override
    public String getHeightmapLocation()
    {
        return "/assets/arkcraft/textures/map/theisland/heightmap/theisland_heightmap.png";
    }

    @Override
    public double getWorldScale()
    {
        return 0.5;
    }

    @Override
    public double getHeightScale(int height)
    {
        return 1;
    }

    @Override
    public IBlockState getStoneBlock()
    {
        return Blocks.stone.getDefaultState();
    }

    @Override
    public BiomeGenBase getDefaultBiome()
    {
        return BiomeGenBase.ocean;
    }

    @Override
    public String getName()
    {
        return "The Island";
    }

    @Override
    public int getColourForBiome(BiomeGenBase biome)
    {
        if (biome == forest)
        {
            return 0x006000;
        }
        else if (biome == swampland)
        {
            return 0x007F7F;
        }
        else if (biome == icePlains)
        {
            return 0xFFFFFF;
        }
        else if (biome == frozenOcean)
        {
            return 0x9393C4;
        }
        else if (biome == plains)
        {
            return 0x65BC00;
        }
        else if (biome == beach)
        {
            return 0xFFE890;
        }
        else if (biome == extremeHills)
        {
            return 0xC1C1C1;
        }
        else if (biome == extremeHillsEdge)
        {
            return 0x6B6B6B;
        }

        return 0;
    }

    @Override
    public boolean hasOcean()
    {
        return true;
    }

    @Override
    public IBlockState getOceanLiquid()
    {
        return Blocks.water.getDefaultState();
    }

    @Override
    public int getOceanHeight(int x, int z)
    {
        return 110;
    }
}
