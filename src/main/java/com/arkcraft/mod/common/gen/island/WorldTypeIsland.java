package com.arkcraft.mod.common.gen.island;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;

/**
 * @author gegy1000
 */
public class WorldTypeIsland extends WorldType
{
    public double scale;

    public WorldTypeIsland(double scale)
    {
        super("island");
        this.scale = scale;
    }

    public net.minecraft.world.chunk.IChunkProvider getChunkGenerator(World world, String generatorOptions)
    {
        return new ChunkProviderIsland(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions, scale);
    }

    public WorldChunkManager getChunkManager(World world)
    {
        return new WorldChunkManagerIsland(world, scale);
    }

    /**
     * Get the height to render the clouds for this world type
     * @return The height to render clouds at
     */
    public float getCloudHeight()
    {
        return 270.0F;
    }
}
