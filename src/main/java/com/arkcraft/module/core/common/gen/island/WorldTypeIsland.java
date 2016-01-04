package com.arkcraft.module.core.common.gen.island;

import net.ilexiconn.llibrary.common.world.gen.gen.ChunkProviderHeightmap;
import net.ilexiconn.llibrary.common.world.gen.gen.WorldChunkManagerHeightmap;
import net.ilexiconn.llibrary.common.world.gen.gen.WorldHeightmapGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.WorldChunkManager;

/**
 * @author gegy1000
 */
public class WorldTypeIsland extends WorldType
{
    private WorldHeightmapGenerator generator;

    public WorldTypeIsland()
    {
        super("island");
        this.generator = new IslandGenerator();
    }

    public net.minecraft.world.chunk.IChunkProvider getChunkGenerator(World world, String generatorOptions)
    {
        return new ChunkProviderHeightmap(world, world.getSeed(), generator);
    }

    public WorldChunkManager getChunkManager(World world)
    {
        return new WorldChunkManagerHeightmap(world, generator);
    }

    /**
     * Get the height to render the clouds for this world type
     *
     * @return The height to render clouds at
     */
    public float getCloudHeight()
    {
        return 270.0F;
    }
}
