package com.arkcraft.mod.common.gen.island;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.ChunkProviderEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.TerrainGen;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

import java.util.List;
import java.util.Random;

import static net.minecraftforge.event.terraingen.InitMapGenEvent.EventType.*;
import static net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType.*;

/**
 * @author gegy1000
 */
public class ChunkProviderIsland implements IChunkProvider
{
    /** RNG. */
    private Random rand;
    private NoiseGeneratorOctaves zMapNoiseGen;
    private NoiseGeneratorOctaves heightMapNoiseGen;
    private NoiseGeneratorOctaves xMapNoiseGen;
    private NoiseGeneratorPerlin noiseGen4;
    /** A NoiseGeneratorOctaves used in generating terrain */
    public NoiseGeneratorOctaves noiseGen5;
    /** A NoiseGeneratorOctaves used in generating terrain */
    public NoiseGeneratorOctaves noiseGen6;
    public NoiseGeneratorOctaves mobSpawnerNoise;
    /** Reference to the World object. */
    private World worldObj;
    /** are map structures going to be generated (e.g. strongholds) */
    private final boolean mapFeaturesEnabled;
    private WorldType field_177475_o;
    private final float[] parabolicField;
    private ChunkProviderSettings settings;
    private Block liquid;
    private double[] stoneNoise;
//    private MapGenBase caveGenerator;
//    /** Holds Stronghold Generator */
//    private MapGenStronghold strongholdGenerator;
//    /** Holds Village Generator */
//    private MapGenVillage villageGenerator;
//    /** Holds Mineshaft Generator */
//    private MapGenMineshaft mineshaftGenerator;
    private MapGenScatteredFeature scatteredFeatureGenerator;
    /** Holds ravine generator */
//    private MapGenBase ravineGenerator;
//    private StructureOceanMonument oceanMonumentGenerator;
    /** The biomes that are used to generate the chunk */
    private BiomeGenBase[] biomesForGeneration;

    private double scale;

    public ChunkProviderIsland(World world, long seed, boolean mapFeaturesEnabled, String generatorOptions, double scale)
    {
        this.scale = scale;
        this.liquid = Blocks.water;
        this.stoneNoise = new double[256];
//        this.caveGenerator = new MapGenCaves();
//        this.strongholdGenerator = new MapGenStronghold();
//        this.villageGenerator = new MapGenVillage();
//        this.mineshaftGenerator = new MapGenMineshaft();
        this.scatteredFeatureGenerator = new MapGenScatteredFeature();
//        this.ravineGenerator = new MapGenRavine();
//        this.oceanMonumentGenerator = new StructureOceanMonument();
//        {
//            caveGenerator = TerrainGen.getModdedMapGen(caveGenerator, CAVE);
//            strongholdGenerator = (MapGenStronghold)TerrainGen.getModdedMapGen(strongholdGenerator, STRONGHOLD);
//            villageGenerator = (MapGenVillage)TerrainGen.getModdedMapGen(villageGenerator, VILLAGE);
//            mineshaftGenerator = (MapGenMineshaft)TerrainGen.getModdedMapGen(mineshaftGenerator, MINESHAFT);
            scatteredFeatureGenerator = (MapGenScatteredFeature)TerrainGen.getModdedMapGen(scatteredFeatureGenerator, SCATTERED_FEATURE);
//            ravineGenerator = TerrainGen.getModdedMapGen(ravineGenerator, RAVINE);
//            oceanMonumentGenerator = (StructureOceanMonument)TerrainGen.getModdedMapGen(oceanMonumentGenerator, OCEAN_MONUMENT);
//        }
        this.worldObj = world;
        this.mapFeaturesEnabled = mapFeaturesEnabled;
        this.field_177475_o = world.getWorldInfo().getTerrainType();
        this.rand = new Random(seed);
        this.zMapNoiseGen = new NoiseGeneratorOctaves(this.rand, 16);
        this.heightMapNoiseGen = new NoiseGeneratorOctaves(this.rand, 16);
        this.xMapNoiseGen = new NoiseGeneratorOctaves(this.rand, 8);
        this.noiseGen4 = new NoiseGeneratorPerlin(this.rand, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.parabolicField = new float[25];

        for (int j = -2; j <= 2; ++j)
        {
            for (int k = -2; k <= 2; ++k)
            {
                float f = 10.0F / MathHelper.sqrt_float((float)(j * j + k * k) + 0.2F);
                this.parabolicField[j + 2 + (k + 2) * 5] = f;
            }
        }

        if (generatorOptions != null)
        {
            this.settings = ChunkProviderSettings.Factory.func_177865_a(generatorOptions).func_177864_b();
            this.liquid = this.settings.useLavaOceans ? Blocks.lava : Blocks.water;
        }

        NoiseGenerator[] noiseGens = {zMapNoiseGen, heightMapNoiseGen, xMapNoiseGen, noiseGen4, noiseGen5, noiseGen6, mobSpawnerNoise};
        noiseGens = TerrainGen.getModdedNoiseGenerators(world, this.rand, noiseGens);
        this.zMapNoiseGen = (NoiseGeneratorOctaves)noiseGens[0];
        this.heightMapNoiseGen = (NoiseGeneratorOctaves)noiseGens[1];
        this.xMapNoiseGen = (NoiseGeneratorOctaves)noiseGens[2];
        this.noiseGen4 = (NoiseGeneratorPerlin)noiseGens[3];
        this.noiseGen5 = (NoiseGeneratorOctaves)noiseGens[4];
        this.noiseGen6 = (NoiseGeneratorOctaves)noiseGens[5];
        this.mobSpawnerNoise = (NoiseGeneratorOctaves)noiseGens[6];
    }

    public void setBlocksInChunk(int chunkX, int chunkZ, ChunkPrimer chunkPrimer)
    {
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, chunkX * 4 - 2, chunkZ * 4 - 2, 10, 10);

        for (int x = 0; x < 16; x++)
        {
            for (int z = 0; z < 16; z++)
            {
                int height = (IslandGen.getHeightForCoords(x + (chunkX * 16), z + (chunkZ * 16), scale));

                for (int y = 0; y < Math.max(64, height); y++)
                {
                    if(y < height)
                    {
                        chunkPrimer.setBlockState(x, y, z, Blocks.stone.getDefaultState());
                    }
                    else
                    {
                        chunkPrimer.setBlockState(x, y, z, this.liquid.getDefaultState());
                    }
                }
            }
        }
    }

    public void func_180517_a(int chunkX, int chunkZ, ChunkPrimer chunkPrimer, BiomeGenBase[] biomes)
    {
        ChunkProviderEvent.ReplaceBiomeBlocks event = new ChunkProviderEvent.ReplaceBiomeBlocks(this, chunkX, chunkZ, chunkPrimer, this.worldObj);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() == Result.DENY) return;

        double d0 = 0.03125D;
        this.stoneNoise = this.noiseGen4.func_151599_a(this.stoneNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);

        for (int k = 0; k < 16; ++k)
        {
            for (int l = 0; l < 16; ++l)
            {
                BiomeGenBase biome = biomes[l + k * 16];
                biome.genTerrainBlocks(this.worldObj, this.rand, chunkPrimer, chunkX * 16 + k, chunkZ * 16 + l, this.stoneNoise[l + k * 16]);
            }
        }
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int x, int z)
    {
        this.rand.setSeed((long)x * 341873128712L + (long) z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.setBlocksInChunk(x, z, chunkprimer);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        this.func_180517_a(x, z, chunkprimer, this.biomesForGeneration);

//        if (this.settings.useCaves)
//        {
//            this.caveGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
//        }
//
//        if (this.settings.useRavines)
//        {
//            this.ravineGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
//        }
//
//        if (this.settings.useMineShafts && this.mapFeaturesEnabled)
//        {
//            this.mineshaftGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
//        }
//
//        if (this.settings.useVillages && this.mapFeaturesEnabled)
//        {
//            this.villageGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
//        }

//        if (this.settings.useStrongholds && this.mapFeaturesEnabled)
//        {
//            this.strongholdGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
//        }

//        if (this.settings.useTemples && this.mapFeaturesEnabled)
//        {
//            this.scatteredFeatureGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
//        }

//        if (this.settings.useMonuments && this.mapFeaturesEnabled)
//        {
//            this.oceanMonumentGenerator.func_175792_a(this, this.worldObj, x, z, chunkprimer);
//        }

        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);

        byte[] biomeArray = chunk.getBiomeArray();

        //Set biomes

        for (int biomeIndex = 0; biomeIndex < biomeArray.length; ++biomeIndex)
            biomeArray[biomeIndex] = (byte)this.biomesForGeneration[biomeIndex].biomeID;

        chunk.generateSkylightMap();
        return chunk;
    }

    /**
     * Checks to see if a chunk exists at x, z
     */
    public boolean chunkExists(int x, int z)
    {
        return true;
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider provider, int p_73153_2_, int p_73153_3_)
    {
        BlockFalling.fallInstantly = true;
        int k = p_73153_2_ * 16;
        int l = p_73153_3_ * 16;
        BlockPos blockpos = new BlockPos(k, 0, l);
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.worldObj.getSeed());
        long i1 = this.rand.nextLong() / 2L * 2L + 1L;
        long j1 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)p_73153_2_ * i1 + (long)p_73153_3_ * j1 ^ this.worldObj.getSeed());
        boolean flag = false;
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Pre(provider, worldObj, rand, p_73153_2_, p_73153_3_, flag));

//        if (this.settings.useMineShafts && this.mapFeaturesEnabled)
//        {
//            this.mineshaftGenerator.func_175794_a(this.worldObj, this.rand, chunkcoordintpair);
//        }
//
//        if (this.settings.useVillages && this.mapFeaturesEnabled)
//        {
//            flag = this.villageGenerator.func_175794_a(this.worldObj, this.rand, chunkcoordintpair);
//        }
//
//        if (this.settings.useStrongholds && this.mapFeaturesEnabled)
//        {
//            this.strongholdGenerator.func_175794_a(this.worldObj, this.rand, chunkcoordintpair);
//        }

//        if (this.settings.useTemples && this.mapFeaturesEnabled)
//        {
//            this.scatteredFeatureGenerator.func_175794_a(this.worldObj, this.rand, chunkcoordintpair);
//        }

//        if (this.settings.useMonuments && this.mapFeaturesEnabled)
//        {
//            this.oceanMonumentGenerator.func_175794_a(this.worldObj, this.rand, chunkcoordintpair);
//        }

        int k1;
        int l1;
        int i2;

//        if (biomegenbase != BiomeGenBase.desert && biomegenbase != BiomeGenBase.desertHills && this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0
//                && TerrainGen.populate(provider, worldObj, rand, p_73153_2_, p_73153_3_, flag, LAKE))
//        {
//            k1 = this.rand.nextInt(16) + 8;
//            l1 = this.rand.nextInt(256);
//            i2 = this.rand.nextInt(16) + 8;
//            (new WorldGenLakes(Blocks.water)).generate(this.worldObj, this.rand, blockpos.add(k1, l1, i2));
//        }

//        if (TerrainGen.populate(provider, worldObj, rand, p_73153_2_, p_73153_3_, flag, LAVA) && !flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes)
//        {
//            k1 = this.rand.nextInt(16) + 8;
//            l1 = this.rand.nextInt(this.rand.nextInt(248) + 8);
//            i2 = this.rand.nextInt(16) + 8;
//
//            if (l1 < 63 || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0)
//            {
//                (new WorldGenLakes(Blocks.lava)).generate(this.worldObj, this.rand, blockpos.add(k1, l1, i2));
//            }
//        }

//        if (this.settings.useDungeons)
//        {
//            boolean doGen = TerrainGen.populate(provider, worldObj, rand, p_73153_2_, p_73153_3_, flag, DUNGEON);
//            for (k1 = 0; doGen && k1 < this.settings.dungeonChance; ++k1)
//            {
//                l1 = this.rand.nextInt(16) + 8;
//                i2 = this.rand.nextInt(256);
//                int j2 = this.rand.nextInt(16) + 8;
//                (new WorldGenDungeons()).generate(this.worldObj, this.rand, blockpos.add(l1, i2, j2));
//            }
//        }

        biomegenbase.decorate(this.worldObj, this.rand, new BlockPos(k, 0, l));
        if (TerrainGen.populate(provider, worldObj, rand, p_73153_2_, p_73153_3_, flag, ANIMALS))
        {
            SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, k + 8, l + 8, 16, 16, this.rand);
        }
        blockpos = blockpos.add(8, 0, 8);

        boolean doGen = TerrainGen.populate(provider, worldObj, rand, p_73153_2_, p_73153_3_, flag, ICE);
        for (k1 = 0; doGen && k1 < 16; ++k1)
        {
            for (l1 = 0; l1 < 16; ++l1)
            {
                BlockPos blockpos1 = this.worldObj.getPrecipitationHeight(blockpos.add(k1, 0, l1));
                BlockPos blockpos2 = blockpos1.down();

                if (this.worldObj.func_175675_v(blockpos2))
                {
                    this.worldObj.setBlockState(blockpos2, Blocks.ice.getDefaultState(), 2);
                }

                if (this.worldObj.canSnowAt(blockpos1, true))
                {
                    this.worldObj.setBlockState(blockpos1, Blocks.snow_layer.getDefaultState(), 2);
                }
            }
        }

        MinecraftForge.EVENT_BUS.post(new PopulateChunkEvent.Post(provider, worldObj, rand, p_73153_2_, p_73153_3_, flag));

        BlockFalling.fallInstantly = false;
    }

    public boolean func_177460_a(IChunkProvider provider, Chunk chunk, int x, int y)
    {
        boolean flag = false;

//        if (this.settings.useMonuments && this.mapFeaturesEnabled && chunk.getInhabitedTime() < 3600L)
//        {
//            flag |= this.oceanMonumentGenerator.func_175794_a(this.worldObj, this.rand, new ChunkCoordIntPair(x, y));
//        }

        return flag;
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean saveAll, IProgressUpdate progressUpdate)
    {
        return true;
    }

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unimplemented.
     */
    public void saveExtraData() {}

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    public boolean unloadQueuedChunks()
    {
        return false;
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave()
    {
        return true;
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString()
    {
        return "RandomLevelSource";
    }

    public List func_177458_a(EnumCreatureType creatureType, BlockPos pos)
    {
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);

        if (this.mapFeaturesEnabled)
        {
            if (creatureType == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.func_175798_a(pos))
            {
                return this.scatteredFeatureGenerator.getScatteredFeatureSpawnList();
            }

//            if (creatureType == EnumCreatureType.MONSTER && this.settings.useMonuments && this.oceanMonumentGenerator.func_175796_a(this.worldObj, pos))
//            {
//                return this.oceanMonumentGenerator.func_175799_b();
//            }
        }

        return biomegenbase.getSpawnableList(creatureType);
    }

    public BlockPos getStrongholdGen(World world, String gen, BlockPos pos)
    {
        return pos;
//        return "Stronghold".equals(gen) && this.strongholdGenerator != null ? this.strongholdGenerator.getClosestStrongholdPos(world, pos) : null;
    }

    public int getLoadedChunkCount()
    {
        return 0;
    }

    public void recreateStructures(Chunk chunk, int x, int z)
    {
//        if (this.settings.useMineShafts && this.mapFeaturesEnabled)
//        {
//            this.mineshaftGenerator.func_175792_a(this, this.worldObj, x, z, (ChunkPrimer)null);
//        }
//
//        if (this.settings.useVillages && this.mapFeaturesEnabled)
//        {
//            this.villageGenerator.func_175792_a(this, this.worldObj, x, z, (ChunkPrimer)null);
//        }
//
//        if (this.settings.useStrongholds && this.mapFeaturesEnabled)
//        {
//            this.strongholdGenerator.func_175792_a(this, this.worldObj, x, z, (ChunkPrimer)null);
//        }
//
//        if (this.settings.useTemples && this.mapFeaturesEnabled)
//        {
//            this.scatteredFeatureGenerator.func_175792_a(this, this.worldObj, x, z, (ChunkPrimer)null);
//        }
//
//        if (this.settings.useMonuments && this.mapFeaturesEnabled)
//        {
//            this.oceanMonumentGenerator.func_175792_a(this, this.worldObj, x, z, (ChunkPrimer)null);
//        }
    }

    public Chunk provideChunk(BlockPos pos)
    {
        return this.provideChunk(pos.getX() >> 4, pos.getZ() >> 4);
    }
}