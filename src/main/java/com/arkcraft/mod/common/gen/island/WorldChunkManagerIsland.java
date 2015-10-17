package com.arkcraft.mod.common.gen.island;

import com.google.common.collect.Lists;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * @author gegy1000
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class WorldChunkManagerIsland extends WorldChunkManager
{
    /** The biome list. */
    private BiomeCache biomeCache;
    /** A list of biomes that the player can spawn in. */
	private List biomesToSpawnIn;
	private String field_180301_f;
    private double scale;
    private long seed;

    protected WorldChunkManagerIsland(double scale)
    {
        this.biomeCache = new BiomeCache(this);
        this.field_180301_f = "";
        this.biomesToSpawnIn = Lists.newArrayList();
        this.biomesToSpawnIn.addAll(allowedBiomes);
        this.scale = scale;
    }

    public WorldChunkManagerIsland(long seed, WorldType worldType, String p_i45744_4_, double scale)
    {
        this(scale);
        this.field_180301_f = p_i45744_4_;
        this.seed = seed;
    }

    public WorldChunkManagerIsland(World worldIn, double scale)
    {
        this(worldIn.getSeed(), worldIn.getWorldInfo().getTerrainType(), worldIn.getWorldInfo().getGeneratorOptions(), scale);
    }

    /**
     * Gets the list of valid biomes for the player to spawn in.
     */
    public List getBiomesToSpawnIn()
    {
        return this.biomesToSpawnIn;
    }

    /**
     * Returns the biome generator
     */
    public BiomeGenBase getBiomeGenerator(BlockPos p_180631_1_)
    {
        return this.func_180300_a(p_180631_1_, (BiomeGenBase) null);
    }

    public BiomeGenBase func_180300_a(BlockPos p_180300_1_, BiomeGenBase p_180300_2_)
    {
        return this.biomeCache.func_180284_a(p_180300_1_.getX(), p_180300_1_.getZ(), p_180300_2_);
    }

    /**
     * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
     */
    public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length)
    {
        IntCache.resetIntCache();

        if (listToReuse == null || listToReuse.length < width * length)
        {
            listToReuse = new float[width * length];
        }

//        int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);

        for (int i1 = 0; i1 < width * length; ++i1)
        {
            try
            {
                float f = (float)BiomeGenBase.getBiomeFromBiomeList(getBiomeAt(x, z).biomeID, BiomeGenBase.field_180279_ad).getIntRainfall() / 65536.0F;

                if (f > 1.0F)
                {
                    f = 1.0F;
                }

                listToReuse[i1] = f;
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("DownfallBlock");
                crashreportcategory.addCrashSection("biome id", Integer.valueOf(i1));
                crashreportcategory.addCrashSection("downfalls[] size", Integer.valueOf(listToReuse.length));
                crashreportcategory.addCrashSection("x", Integer.valueOf(x));
                crashreportcategory.addCrashSection("z", Integer.valueOf(z));
                crashreportcategory.addCrashSection("w", Integer.valueOf(width));
                crashreportcategory.addCrashSection("h", Integer.valueOf(length));
                throw new ReportedException(crashreport);
            }
        }

        return listToReuse;
    }

    /**
     * Return an adjusted version of a given temperature based on the y height
     */
    @SideOnly(Side.CLIENT)
    public float getTemperatureAtHeight(float p_76939_1_, int p_76939_2_)
    {
        return p_76939_1_;
    }

    /**
     * Returns an array of biomes for the location input.
     */
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int x, int z, int width, int height)
    {
        IntCache.resetIntCache();

        if (biomes == null || biomes.length < width * height)
        {
            biomes = new BiomeGenBase[width * height];
        }

//        int[] aint = this.genBiomes.getInts(biomes, z, width, height);

        try
        {
            for (int i1 = 0; i1 < width * height; ++i1)
            {
                biomes[i1] = getBiomeAt(x, z);//BiomeGenBase.getBiomeFromBiomeList(aint[i1], BiomeGenBase.field_180279_ad);
            }

            return biomes;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
            crashreportcategory.addCrashSection("biomes[] size", Integer.valueOf(biomes.length));
            crashreportcategory.addCrashSection("x", Integer.valueOf(x));
            crashreportcategory.addCrashSection("z", Integer.valueOf(z));
            crashreportcategory.addCrashSection("w", Integer.valueOf(width));
            crashreportcategory.addCrashSection("h", Integer.valueOf(height));
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager Args: oldBiomeList, x, z, width, depth
     */
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] oldBiomeList, int x, int z, int width, int depth)
    {
        return this.getBiomeGenAt(oldBiomeList, x, z, width, depth, true);
    }

    /**
     * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
     * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
     *
     * @param cacheFlag If false, don't check biomeCache to avoid infinite loop in BiomeCacheBlock
     */
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
    {
        IntCache.resetIntCache();

        if (listToReuse == null || listToReuse.length < width * length)
        {
            listToReuse = new BiomeGenBase[width * length];
        }

        if (cacheFlag && width == 16 && length == 16 && (x & 15) == 0 && (z & 15) == 0)
        {
            BiomeGenBase[] abiomegenbase1 = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(abiomegenbase1, 0, listToReuse, 0, width * length);
            return listToReuse;
        }
        else
        {
//            int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);

            for (int i1 = 0; i1 < width * length; ++i1)
            {
                listToReuse[i1] = getBiomeAt(x, z);//BiomeGenBase.getBiomeFromBiomeList(aint[i1], BiomeGenBase.field_180279_ad);
            }

            return listToReuse;
        }
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    public boolean areBiomesViable(int x, int z, int radius, List allowed)
    {
        IntCache.resetIntCache();
        int l = x - radius >> 2;
        int i1 = z - radius >> 2;
        int j1 = x + radius >> 2;
        int k1 = z + radius >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
//        int[] aint = this.genBiomes.getInts(l, i1, l1, i2);

        try
        {
            for (int j2 = 0; j2 < l1 * i2; ++j2)
            {
                BiomeGenBase biomegenbase = getBiomeAt(x, z);//BiomeGenBase.getBiome(aint[j2]);

                if (!allowed.contains(biomegenbase))
                {
                    return false;
                }
            }

            return true;
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
//            crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
            crashreportcategory.addCrashSection("x", Integer.valueOf(x));
            crashreportcategory.addCrashSection("z", Integer.valueOf(z));
            crashreportcategory.addCrashSection("radius", Integer.valueOf(radius));
            crashreportcategory.addCrashSection("allowed", allowed);
            throw new ReportedException(crashreport);
        }
    }

    public BlockPos findBiomePosition(int x, int z, int range, List biomes, Random random)
    {
        IntCache.resetIntCache();
        int l = x - range >> 2;
        int i1 = z - range >> 2;
        int j1 = x + range >> 2;
        int k1 = z + range >> 2;
        int l1 = j1 - l + 1;
        int i2 = k1 - i1 + 1;
//        int[] aint = this.genBiomes.getInts(l, i1, l1, i2);
        BlockPos blockpos = null;
        int j2 = 0;

        for (int k2 = 0; k2 < l1 * i2; ++k2)
        {
            int l2 = l + k2 % l1 << 2;
            int i3 = i1 + k2 / l1 << 2;
            BiomeGenBase biomegenbase = getBiomeAt(x, z);//BiomeGenBase.getBiome(aint[k2]);

            if (biomes.contains(biomegenbase) && (blockpos == null || random.nextInt(j2 + 1) == 0))
            {
                blockpos = new BlockPos(l2, 0, i3);
                ++j2;
            }
        }

        return blockpos;
    }

    private BiomeGenBase getBiomeAt(int x, int z)
    {
//        Random random = new Random(seed * x * z);
//
//        int height = IslandGen.getHeightForCoords(x, z, scale);
//
//        int halfZ = (IslandGen.height * scale) / 2;
//
//        float temperature = 2.0F - IslandGen.getScaled(halfZ - z, halfZ, 2.0F);
//
//        temperature += (random.nextFloat() / 2) - 0.25F;
//
//        if(height >= 60)
//        {
//            temperature -= IslandGen.getScaled(height - 64, 192, -2.0F);
//
//            if (temperature < 0.0F)
//            {
//                temperature = 0.0F;
//            }
//
//            BiomeGenBase closest = null;
//            float closestTemp = Float.MAX_VALUE;
//
//            BlockPos tempPos = new BlockPos(x, height, z);
//
//            for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray())
//            {
//                if (biome != null && biome != BiomeGenBase.ocean && biome != BiomeGenBase.deepOcean && biome != BiomeGenBase.frozenOcean)
//                {
//                    float biomeTemp = biome.getFloatTemperature(tempPos);
//
//                    if (biomeTemp - temperature < closestTemp)
//                    {
//                        closestTemp = biomeTemp;
//                        closest = biome;
//                    }
//                }
//            }
//
//            if (closest != null)
//            {
//                return closest;
//            }
//        }
//
//        if(temperature <= 0.5F)
//        {
//            return BiomeGenBase.frozenOcean;
//        }
//        else
//        {
//            return BiomeGenBase.ocean;
//        }

        return IslandGen.getBiomeAt(x, z, scale);
    }

    /**
     * Calls the WorldChunkManager's biomeCache.cleanupCache()
     */
    public void cleanupCache()
    {
        this.biomeCache.cleanupCache();
    }

    public GenLayer[] getModdedBiomeGenerators(WorldType worldType, long seed, GenLayer[] original)
    {
        WorldTypeEvent.InitBiomeGens event = new WorldTypeEvent.InitBiomeGens(worldType, seed, original);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        return event.newBiomeGens;
    }
}