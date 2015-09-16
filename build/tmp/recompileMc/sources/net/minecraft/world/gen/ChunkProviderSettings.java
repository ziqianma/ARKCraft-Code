package net.minecraft.world.gen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;
import net.minecraft.world.biome.BiomeGenBase;

public class ChunkProviderSettings
{
    public final float coordinateScale;
    public final float heightScale;
    public final float upperLimitScale;
    public final float lowerLimitScale;
    public final float depthNoiseScaleX;
    public final float depthNoiseScaleZ;
    public final float depthNoiseScaleExponent;
    public final float mainNoiseScaleX;
    public final float mainNoiseScaleY;
    public final float mainNoiseScaleZ;
    public final float baseSize;
    public final float stretchY;
    public final float biomeDepthWeight;
    public final float biomeDepthOffSet;
    public final float biomeScaleWeight;
    public final float biomeScaleOffset;
    public final int seaLevel;
    public final boolean useCaves;
    public final boolean useDungeons;
    public final int dungeonChance;
    public final boolean useStrongholds;
    public final boolean useVillages;
    public final boolean useMineShafts;
    public final boolean useTemples;
    public final boolean useMonuments;
    public final boolean useRavines;
    public final boolean useWaterLakes;
    public final int waterLakeChance;
    public final boolean useLavaLakes;
    public final int lavaLakeChance;
    public final boolean useLavaOceans;
    public final int fixedBiome;
    public final int biomeSize;
    public final int riverSize;
    public final int dirtSize;
    public final int dirtCount;
    public final int dirtMinHeight;
    public final int dirtMaxHeight;
    public final int gravelSize;
    public final int gravelCount;
    public final int gravelMinHeight;
    public final int gravelMaxHeight;
    public final int graniteSize;
    public final int graniteCount;
    public final int graniteMinHeight;
    public final int graniteMaxHeight;
    public final int dioriteSize;
    public final int dioriteCount;
    public final int dioriteMinHeight;
    public final int dioriteMaxHeight;
    public final int andesiteSize;
    public final int andesiteCount;
    public final int andesiteMinHeight;
    public final int andesiteMaxHeight;
    public final int coalSize;
    public final int coalCount;
    public final int coalMinHeight;
    public final int coalMaxHeight;
    public final int ironSize;
    public final int ironCount;
    public final int ironMinHeight;
    public final int ironMaxHeight;
    public final int goldSize;
    public final int goldCount;
    public final int goldMinHeight;
    public final int goldMaxHeight;
    public final int redstoneSize;
    public final int redstoneCount;
    public final int redstoneMinHeight;
    public final int redstoneMaxHeight;
    public final int diamondSize;
    public final int diamondCount;
    public final int diamondMinHeight;
    public final int diamondMaxHeight;
    public final int lapisSize;
    public final int lapisCount;
    public final int lapisCenterHeight;
    public final int lapisSpread;
    private static final String __OBFID = "CL_00002006";

    private ChunkProviderSettings(ChunkProviderSettings.Factory settingsFactory)
    {
        this.coordinateScale = settingsFactory.coordinateScale;
        this.heightScale = settingsFactory.heightScale;
        this.upperLimitScale = settingsFactory.upperLimitScale;
        this.lowerLimitScale = settingsFactory.lowerLimitScale;
        this.depthNoiseScaleX = settingsFactory.depthNoiseScaleX;
        this.depthNoiseScaleZ = settingsFactory.depthNoiseScaleZ;
        this.depthNoiseScaleExponent = settingsFactory.depthNoiseScaleExponent;
        this.mainNoiseScaleX = settingsFactory.mainNoiseScaleX;
        this.mainNoiseScaleY = settingsFactory.mainNoiseScaleY;
        this.mainNoiseScaleZ = settingsFactory.mainNoiseScaleZ;
        this.baseSize = settingsFactory.baseSize;
        this.stretchY = settingsFactory.stretchY;
        this.biomeDepthWeight = settingsFactory.biomeDepthWeight;
        this.biomeDepthOffSet = settingsFactory.biomeDepthOffset;
        this.biomeScaleWeight = settingsFactory.biomeScaleWeight;
        this.biomeScaleOffset = settingsFactory.biomeScaleOffset;
        this.seaLevel = settingsFactory.seaLevel;
        this.useCaves = settingsFactory.useCaves;
        this.useDungeons = settingsFactory.useDungeons;
        this.dungeonChance = settingsFactory.dungeonChance;
        this.useStrongholds = settingsFactory.useStrongholds;
        this.useVillages = settingsFactory.useVillages;
        this.useMineShafts = settingsFactory.useMineShafts;
        this.useTemples = settingsFactory.useTemples;
        this.useMonuments = settingsFactory.useMonuments;
        this.useRavines = settingsFactory.useRavines;
        this.useWaterLakes = settingsFactory.useWaterLakes;
        this.waterLakeChance = settingsFactory.waterLakeChance;
        this.useLavaLakes = settingsFactory.useLavaLakes;
        this.lavaLakeChance = settingsFactory.lavaLakeChance;
        this.useLavaOceans = settingsFactory.useLavaOceans;
        this.fixedBiome = settingsFactory.fixedBiome;
        this.biomeSize = settingsFactory.biomeSize;
        this.riverSize = settingsFactory.riverSize;
        this.dirtSize = settingsFactory.dirtSize;
        this.dirtCount = settingsFactory.dirtCount;
        this.dirtMinHeight = settingsFactory.dirtMinHeight;
        this.dirtMaxHeight = settingsFactory.dirtMaxHeight;
        this.gravelSize = settingsFactory.gravelSize;
        this.gravelCount = settingsFactory.gravelCount;
        this.gravelMinHeight = settingsFactory.gravelMinHeight;
        this.gravelMaxHeight = settingsFactory.gravelMaxHeight;
        this.graniteSize = settingsFactory.graniteSize;
        this.graniteCount = settingsFactory.graniteCount;
        this.graniteMinHeight = settingsFactory.graniteMinHeight;
        this.graniteMaxHeight = settingsFactory.graniteMaxHeight;
        this.dioriteSize = settingsFactory.dioriteSize;
        this.dioriteCount = settingsFactory.dioriteCount;
        this.dioriteMinHeight = settingsFactory.dioriteMinHeight;
        this.dioriteMaxHeight = settingsFactory.dioriteMaxHeight;
        this.andesiteSize = settingsFactory.andesiteSize;
        this.andesiteCount = settingsFactory.andesiteCount;
        this.andesiteMinHeight = settingsFactory.andesiteMinHeight;
        this.andesiteMaxHeight = settingsFactory.andesiteMaxHeight;
        this.coalSize = settingsFactory.coalSize;
        this.coalCount = settingsFactory.coalCount;
        this.coalMinHeight = settingsFactory.coalMinHeight;
        this.coalMaxHeight = settingsFactory.coalMaxHeight;
        this.ironSize = settingsFactory.ironSize;
        this.ironCount = settingsFactory.ironCount;
        this.ironMinHeight = settingsFactory.ironMinHeight;
        this.ironMaxHeight = settingsFactory.ironMaxHeight;
        this.goldSize = settingsFactory.goldSize;
        this.goldCount = settingsFactory.goldCount;
        this.goldMinHeight = settingsFactory.goldMinHeight;
        this.goldMaxHeight = settingsFactory.goldMaxHeight;
        this.redstoneSize = settingsFactory.redstoneSize;
        this.redstoneCount = settingsFactory.redstoneCount;
        this.redstoneMinHeight = settingsFactory.redstoneMinHeight;
        this.redstoneMaxHeight = settingsFactory.redstoneMaxHeight;
        this.diamondSize = settingsFactory.diamondSize;
        this.diamondCount = settingsFactory.diamondCount;
        this.diamondMinHeight = settingsFactory.diamondMinHeight;
        this.diamondMaxHeight = settingsFactory.diamondMaxHeight;
        this.lapisSize = settingsFactory.lapisSize;
        this.lapisCount = settingsFactory.lapisCount;
        this.lapisCenterHeight = settingsFactory.lapisCenterHeight;
        this.lapisSpread = settingsFactory.lapisSpread;
    }

    ChunkProviderSettings(ChunkProviderSettings.Factory p_i45640_1_, Object p_i45640_2_)
    {
        this(p_i45640_1_);
    }

    public static class Factory
        {
            static final Gson JSON_ADAPTER = (new GsonBuilder()).registerTypeAdapter(ChunkProviderSettings.Factory.class, new ChunkProviderSettings.Serializer()).create();
            public float coordinateScale = 684.412F;
            public float heightScale = 684.412F;
            public float upperLimitScale = 512.0F;
            public float lowerLimitScale = 512.0F;
            public float depthNoiseScaleX = 200.0F;
            public float depthNoiseScaleZ = 200.0F;
            public float depthNoiseScaleExponent = 0.5F;
            public float mainNoiseScaleX = 80.0F;
            public float mainNoiseScaleY = 160.0F;
            public float mainNoiseScaleZ = 80.0F;
            public float baseSize = 8.5F;
            public float stretchY = 12.0F;
            public float biomeDepthWeight = 1.0F;
            public float biomeDepthOffset = 0.0F;
            public float biomeScaleWeight = 1.0F;
            public float biomeScaleOffset = 0.0F;
            public int seaLevel = 63;
            public boolean useCaves = true;
            public boolean useDungeons = true;
            public int dungeonChance = 8;
            public boolean useStrongholds = true;
            public boolean useVillages = true;
            public boolean useMineShafts = true;
            public boolean useTemples = true;
            public boolean useMonuments = true;
            public boolean useRavines = true;
            public boolean useWaterLakes = true;
            public int waterLakeChance = 4;
            public boolean useLavaLakes = true;
            public int lavaLakeChance = 80;
            public boolean useLavaOceans = false;
            public int fixedBiome = -1;
            public int biomeSize = 4;
            public int riverSize = 4;
            public int dirtSize = 33;
            public int dirtCount = 10;
            public int dirtMinHeight = 0;
            public int dirtMaxHeight = 256;
            public int gravelSize = 33;
            public int gravelCount = 8;
            public int gravelMinHeight = 0;
            public int gravelMaxHeight = 256;
            public int graniteSize = 33;
            public int graniteCount = 10;
            public int graniteMinHeight = 0;
            public int graniteMaxHeight = 80;
            public int dioriteSize = 33;
            public int dioriteCount = 10;
            public int dioriteMinHeight = 0;
            public int dioriteMaxHeight = 80;
            public int andesiteSize = 33;
            public int andesiteCount = 10;
            public int andesiteMinHeight = 0;
            public int andesiteMaxHeight = 80;
            public int coalSize = 17;
            public int coalCount = 20;
            public int coalMinHeight = 0;
            public int coalMaxHeight = 128;
            public int ironSize = 9;
            public int ironCount = 20;
            public int ironMinHeight = 0;
            public int ironMaxHeight = 64;
            public int goldSize = 9;
            public int goldCount = 2;
            public int goldMinHeight = 0;
            public int goldMaxHeight = 32;
            public int redstoneSize = 8;
            public int redstoneCount = 8;
            public int redstoneMinHeight = 0;
            public int redstoneMaxHeight = 16;
            public int diamondSize = 8;
            public int diamondCount = 1;
            public int diamondMinHeight = 0;
            public int diamondMaxHeight = 16;
            public int lapisSize = 7;
            public int lapisCount = 1;
            public int lapisCenterHeight = 16;
            public int lapisSpread = 16;
            private static final String __OBFID = "CL_00002004";

            public static ChunkProviderSettings.Factory func_177865_a(String p_177865_0_)
            {
                if (p_177865_0_.length() == 0)
                {
                    return new ChunkProviderSettings.Factory();
                }
                else
                {
                    try
                    {
                        return (ChunkProviderSettings.Factory)JSON_ADAPTER.fromJson(p_177865_0_, ChunkProviderSettings.Factory.class);
                    }
                    catch (Exception exception)
                    {
                        return new ChunkProviderSettings.Factory();
                    }
                }
            }

            public String toString()
            {
                return JSON_ADAPTER.toJson(this);
            }

            public Factory()
            {
                this.func_177863_a();
            }

            public void func_177863_a()
            {
                this.coordinateScale = 684.412F;
                this.heightScale = 684.412F;
                this.upperLimitScale = 512.0F;
                this.lowerLimitScale = 512.0F;
                this.depthNoiseScaleX = 200.0F;
                this.depthNoiseScaleZ = 200.0F;
                this.depthNoiseScaleExponent = 0.5F;
                this.mainNoiseScaleX = 80.0F;
                this.mainNoiseScaleY = 160.0F;
                this.mainNoiseScaleZ = 80.0F;
                this.baseSize = 8.5F;
                this.stretchY = 12.0F;
                this.biomeDepthWeight = 1.0F;
                this.biomeDepthOffset = 0.0F;
                this.biomeScaleWeight = 1.0F;
                this.biomeScaleOffset = 0.0F;
                this.seaLevel = 63;
                this.useCaves = true;
                this.useDungeons = true;
                this.dungeonChance = 8;
                this.useStrongholds = true;
                this.useVillages = true;
                this.useMineShafts = true;
                this.useTemples = true;
                this.useMonuments = true;
                this.useRavines = true;
                this.useWaterLakes = true;
                this.waterLakeChance = 4;
                this.useLavaLakes = true;
                this.lavaLakeChance = 80;
                this.useLavaOceans = false;
                this.fixedBiome = -1;
                this.biomeSize = 4;
                this.riverSize = 4;
                this.dirtSize = 33;
                this.dirtCount = 10;
                this.dirtMinHeight = 0;
                this.dirtMaxHeight = 256;
                this.gravelSize = 33;
                this.gravelCount = 8;
                this.gravelMinHeight = 0;
                this.gravelMaxHeight = 256;
                this.graniteSize = 33;
                this.graniteCount = 10;
                this.graniteMinHeight = 0;
                this.graniteMaxHeight = 80;
                this.dioriteSize = 33;
                this.dioriteCount = 10;
                this.dioriteMinHeight = 0;
                this.dioriteMaxHeight = 80;
                this.andesiteSize = 33;
                this.andesiteCount = 10;
                this.andesiteMinHeight = 0;
                this.andesiteMaxHeight = 80;
                this.coalSize = 17;
                this.coalCount = 20;
                this.coalMinHeight = 0;
                this.coalMaxHeight = 128;
                this.ironSize = 9;
                this.ironCount = 20;
                this.ironMinHeight = 0;
                this.ironMaxHeight = 64;
                this.goldSize = 9;
                this.goldCount = 2;
                this.goldMinHeight = 0;
                this.goldMaxHeight = 32;
                this.redstoneSize = 8;
                this.redstoneCount = 8;
                this.redstoneMinHeight = 0;
                this.redstoneMaxHeight = 16;
                this.diamondSize = 8;
                this.diamondCount = 1;
                this.diamondMinHeight = 0;
                this.diamondMaxHeight = 16;
                this.lapisSize = 7;
                this.lapisCount = 1;
                this.lapisCenterHeight = 16;
                this.lapisSpread = 16;
            }

            public boolean equals(Object p_equals_1_)
            {
                if (this == p_equals_1_)
                {
                    return true;
                }
                else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass())
                {
                    ChunkProviderSettings.Factory factory = (ChunkProviderSettings.Factory)p_equals_1_;
                    return this.andesiteCount != factory.andesiteCount ? false : (this.andesiteMaxHeight != factory.andesiteMaxHeight ? false : (this.andesiteMinHeight != factory.andesiteMinHeight ? false : (this.andesiteSize != factory.andesiteSize ? false : (Float.compare(factory.baseSize, this.baseSize) != 0 ? false : (Float.compare(factory.biomeDepthOffset, this.biomeDepthOffset) != 0 ? false : (Float.compare(factory.biomeDepthWeight, this.biomeDepthWeight) != 0 ? false : (Float.compare(factory.biomeScaleOffset, this.biomeScaleOffset) != 0 ? false : (Float.compare(factory.biomeScaleWeight, this.biomeScaleWeight) != 0 ? false : (this.biomeSize != factory.biomeSize ? false : (this.coalCount != factory.coalCount ? false : (this.coalMaxHeight != factory.coalMaxHeight ? false : (this.coalMinHeight != factory.coalMinHeight ? false : (this.coalSize != factory.coalSize ? false : (Float.compare(factory.coordinateScale, this.coordinateScale) != 0 ? false : (Float.compare(factory.depthNoiseScaleExponent, this.depthNoiseScaleExponent) != 0 ? false : (Float.compare(factory.depthNoiseScaleX, this.depthNoiseScaleX) != 0 ? false : (Float.compare(factory.depthNoiseScaleZ, this.depthNoiseScaleZ) != 0 ? false : (this.diamondCount != factory.diamondCount ? false : (this.diamondMaxHeight != factory.diamondMaxHeight ? false : (this.diamondMinHeight != factory.diamondMinHeight ? false : (this.diamondSize != factory.diamondSize ? false : (this.dioriteCount != factory.dioriteCount ? false : (this.dioriteMaxHeight != factory.dioriteMaxHeight ? false : (this.dioriteMinHeight != factory.dioriteMinHeight ? false : (this.dioriteSize != factory.dioriteSize ? false : (this.dirtCount != factory.dirtCount ? false : (this.dirtMaxHeight != factory.dirtMaxHeight ? false : (this.dirtMinHeight != factory.dirtMinHeight ? false : (this.dirtSize != factory.dirtSize ? false : (this.dungeonChance != factory.dungeonChance ? false : (this.fixedBiome != factory.fixedBiome ? false : (this.goldCount != factory.goldCount ? false : (this.goldMaxHeight != factory.goldMaxHeight ? false : (this.goldMinHeight != factory.goldMinHeight ? false : (this.goldSize != factory.goldSize ? false : (this.graniteCount != factory.graniteCount ? false : (this.graniteMaxHeight != factory.graniteMaxHeight ? false : (this.graniteMinHeight != factory.graniteMinHeight ? false : (this.graniteSize != factory.graniteSize ? false : (this.gravelCount != factory.gravelCount ? false : (this.gravelMaxHeight != factory.gravelMaxHeight ? false : (this.gravelMinHeight != factory.gravelMinHeight ? false : (this.gravelSize != factory.gravelSize ? false : (Float.compare(factory.heightScale, this.heightScale) != 0 ? false : (this.ironCount != factory.ironCount ? false : (this.ironMaxHeight != factory.ironMaxHeight ? false : (this.ironMinHeight != factory.ironMinHeight ? false : (this.ironSize != factory.ironSize ? false : (this.lapisCenterHeight != factory.lapisCenterHeight ? false : (this.lapisCount != factory.lapisCount ? false : (this.lapisSize != factory.lapisSize ? false : (this.lapisSpread != factory.lapisSpread ? false : (this.lavaLakeChance != factory.lavaLakeChance ? false : (Float.compare(factory.lowerLimitScale, this.lowerLimitScale) != 0 ? false : (Float.compare(factory.mainNoiseScaleX, this.mainNoiseScaleX) != 0 ? false : (Float.compare(factory.mainNoiseScaleY, this.mainNoiseScaleY) != 0 ? false : (Float.compare(factory.mainNoiseScaleZ, this.mainNoiseScaleZ) != 0 ? false : (this.redstoneCount != factory.redstoneCount ? false : (this.redstoneMaxHeight != factory.redstoneMaxHeight ? false : (this.redstoneMinHeight != factory.redstoneMinHeight ? false : (this.redstoneSize != factory.redstoneSize ? false : (this.riverSize != factory.riverSize ? false : (this.seaLevel != factory.seaLevel ? false : (Float.compare(factory.stretchY, this.stretchY) != 0 ? false : (Float.compare(factory.upperLimitScale, this.upperLimitScale) != 0 ? false : (this.useCaves != factory.useCaves ? false : (this.useDungeons != factory.useDungeons ? false : (this.useLavaLakes != factory.useLavaLakes ? false : (this.useLavaOceans != factory.useLavaOceans ? false : (this.useMineShafts != factory.useMineShafts ? false : (this.useRavines != factory.useRavines ? false : (this.useStrongholds != factory.useStrongholds ? false : (this.useTemples != factory.useTemples ? false : (this.useMonuments != factory.useMonuments ? false : (this.useVillages != factory.useVillages ? false : (this.useWaterLakes != factory.useWaterLakes ? false : this.waterLakeChance == factory.waterLakeChance))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))));
                }
                else
                {
                    return false;
                }
            }

            public int hashCode()
            {
                int i = this.coordinateScale != 0.0F ? Float.floatToIntBits(this.coordinateScale) : 0;
                i = 31 * i + (this.heightScale != 0.0F ? Float.floatToIntBits(this.heightScale) : 0);
                i = 31 * i + (this.upperLimitScale != 0.0F ? Float.floatToIntBits(this.upperLimitScale) : 0);
                i = 31 * i + (this.lowerLimitScale != 0.0F ? Float.floatToIntBits(this.lowerLimitScale) : 0);
                i = 31 * i + (this.depthNoiseScaleX != 0.0F ? Float.floatToIntBits(this.depthNoiseScaleX) : 0);
                i = 31 * i + (this.depthNoiseScaleZ != 0.0F ? Float.floatToIntBits(this.depthNoiseScaleZ) : 0);
                i = 31 * i + (this.depthNoiseScaleExponent != 0.0F ? Float.floatToIntBits(this.depthNoiseScaleExponent) : 0);
                i = 31 * i + (this.mainNoiseScaleX != 0.0F ? Float.floatToIntBits(this.mainNoiseScaleX) : 0);
                i = 31 * i + (this.mainNoiseScaleY != 0.0F ? Float.floatToIntBits(this.mainNoiseScaleY) : 0);
                i = 31 * i + (this.mainNoiseScaleZ != 0.0F ? Float.floatToIntBits(this.mainNoiseScaleZ) : 0);
                i = 31 * i + (this.baseSize != 0.0F ? Float.floatToIntBits(this.baseSize) : 0);
                i = 31 * i + (this.stretchY != 0.0F ? Float.floatToIntBits(this.stretchY) : 0);
                i = 31 * i + (this.biomeDepthWeight != 0.0F ? Float.floatToIntBits(this.biomeDepthWeight) : 0);
                i = 31 * i + (this.biomeDepthOffset != 0.0F ? Float.floatToIntBits(this.biomeDepthOffset) : 0);
                i = 31 * i + (this.biomeScaleWeight != 0.0F ? Float.floatToIntBits(this.biomeScaleWeight) : 0);
                i = 31 * i + (this.biomeScaleOffset != 0.0F ? Float.floatToIntBits(this.biomeScaleOffset) : 0);
                i = 31 * i + this.seaLevel;
                i = 31 * i + (this.useCaves ? 1 : 0);
                i = 31 * i + (this.useDungeons ? 1 : 0);
                i = 31 * i + this.dungeonChance;
                i = 31 * i + (this.useStrongholds ? 1 : 0);
                i = 31 * i + (this.useVillages ? 1 : 0);
                i = 31 * i + (this.useMineShafts ? 1 : 0);
                i = 31 * i + (this.useTemples ? 1 : 0);
                i = 31 * i + (this.useMonuments ? 1 : 0);
                i = 31 * i + (this.useRavines ? 1 : 0);
                i = 31 * i + (this.useWaterLakes ? 1 : 0);
                i = 31 * i + this.waterLakeChance;
                i = 31 * i + (this.useLavaLakes ? 1 : 0);
                i = 31 * i + this.lavaLakeChance;
                i = 31 * i + (this.useLavaOceans ? 1 : 0);
                i = 31 * i + this.fixedBiome;
                i = 31 * i + this.biomeSize;
                i = 31 * i + this.riverSize;
                i = 31 * i + this.dirtSize;
                i = 31 * i + this.dirtCount;
                i = 31 * i + this.dirtMinHeight;
                i = 31 * i + this.dirtMaxHeight;
                i = 31 * i + this.gravelSize;
                i = 31 * i + this.gravelCount;
                i = 31 * i + this.gravelMinHeight;
                i = 31 * i + this.gravelMaxHeight;
                i = 31 * i + this.graniteSize;
                i = 31 * i + this.graniteCount;
                i = 31 * i + this.graniteMinHeight;
                i = 31 * i + this.graniteMaxHeight;
                i = 31 * i + this.dioriteSize;
                i = 31 * i + this.dioriteCount;
                i = 31 * i + this.dioriteMinHeight;
                i = 31 * i + this.dioriteMaxHeight;
                i = 31 * i + this.andesiteSize;
                i = 31 * i + this.andesiteCount;
                i = 31 * i + this.andesiteMinHeight;
                i = 31 * i + this.andesiteMaxHeight;
                i = 31 * i + this.coalSize;
                i = 31 * i + this.coalCount;
                i = 31 * i + this.coalMinHeight;
                i = 31 * i + this.coalMaxHeight;
                i = 31 * i + this.ironSize;
                i = 31 * i + this.ironCount;
                i = 31 * i + this.ironMinHeight;
                i = 31 * i + this.ironMaxHeight;
                i = 31 * i + this.goldSize;
                i = 31 * i + this.goldCount;
                i = 31 * i + this.goldMinHeight;
                i = 31 * i + this.goldMaxHeight;
                i = 31 * i + this.redstoneSize;
                i = 31 * i + this.redstoneCount;
                i = 31 * i + this.redstoneMinHeight;
                i = 31 * i + this.redstoneMaxHeight;
                i = 31 * i + this.diamondSize;
                i = 31 * i + this.diamondCount;
                i = 31 * i + this.diamondMinHeight;
                i = 31 * i + this.diamondMaxHeight;
                i = 31 * i + this.lapisSize;
                i = 31 * i + this.lapisCount;
                i = 31 * i + this.lapisCenterHeight;
                i = 31 * i + this.lapisSpread;
                return i;
            }

            public ChunkProviderSettings func_177864_b()
            {
                return new ChunkProviderSettings(this, null);
            }
        }

    public static class Serializer implements JsonDeserializer, JsonSerializer
        {
            private static final String __OBFID = "CL_00002003";

            public ChunkProviderSettings.Factory func_177861_a(JsonElement p_177861_1_, Type p_177861_2_, JsonDeserializationContext p_177861_3_)
            {
                JsonObject jsonobject = p_177861_1_.getAsJsonObject();
                ChunkProviderSettings.Factory factory = new ChunkProviderSettings.Factory();

                try
                {
                    factory.coordinateScale = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "coordinateScale", factory.coordinateScale);
                    factory.heightScale = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "heightScale", factory.heightScale);
                    factory.lowerLimitScale = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "lowerLimitScale", factory.lowerLimitScale);
                    factory.upperLimitScale = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "upperLimitScale", factory.upperLimitScale);
                    factory.depthNoiseScaleX = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "depthNoiseScaleX", factory.depthNoiseScaleX);
                    factory.depthNoiseScaleZ = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "depthNoiseScaleZ", factory.depthNoiseScaleZ);
                    factory.depthNoiseScaleExponent = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "depthNoiseScaleExponent", factory.depthNoiseScaleExponent);
                    factory.mainNoiseScaleX = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "mainNoiseScaleX", factory.mainNoiseScaleX);
                    factory.mainNoiseScaleY = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "mainNoiseScaleY", factory.mainNoiseScaleY);
                    factory.mainNoiseScaleZ = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "mainNoiseScaleZ", factory.mainNoiseScaleZ);
                    factory.baseSize = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "baseSize", factory.baseSize);
                    factory.stretchY = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "stretchY", factory.stretchY);
                    factory.biomeDepthWeight = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "biomeDepthWeight", factory.biomeDepthWeight);
                    factory.biomeDepthOffset = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "biomeDepthOffset", factory.biomeDepthOffset);
                    factory.biomeScaleWeight = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "biomeScaleWeight", factory.biomeScaleWeight);
                    factory.biomeScaleOffset = JsonUtils.getJsonObjectFloatFieldValueOrDefault(jsonobject, "biomeScaleOffset", factory.biomeScaleOffset);
                    factory.seaLevel = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "seaLevel", factory.seaLevel);
                    factory.useCaves = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "useCaves", factory.useCaves);
                    factory.useDungeons = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "useDungeons", factory.useDungeons);
                    factory.dungeonChance = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "dungeonChance", factory.dungeonChance);
                    factory.useStrongholds = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "useStrongholds", factory.useStrongholds);
                    factory.useVillages = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "useVillages", factory.useVillages);
                    factory.useMineShafts = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "useMineShafts", factory.useMineShafts);
                    factory.useTemples = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "useTemples", factory.useTemples);
                    factory.useMonuments = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "useMonuments", factory.useMonuments);
                    factory.useRavines = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "useRavines", factory.useRavines);
                    factory.useWaterLakes = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "useWaterLakes", factory.useWaterLakes);
                    factory.waterLakeChance = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "waterLakeChance", factory.waterLakeChance);
                    factory.useLavaLakes = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "useLavaLakes", factory.useLavaLakes);
                    factory.lavaLakeChance = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "lavaLakeChance", factory.lavaLakeChance);
                    factory.useLavaOceans = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "useLavaOceans", factory.useLavaOceans);
                    factory.fixedBiome = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "fixedBiome", factory.fixedBiome);

                    if (factory.fixedBiome < 38 && factory.fixedBiome >= -1)
                    {
                        if (factory.fixedBiome >= BiomeGenBase.hell.biomeID)
                        {
                            factory.fixedBiome += 2;
                        }
                    }
                    else
                    {
                        factory.fixedBiome = -1;
                    }

                    factory.biomeSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "biomeSize", factory.biomeSize);
                    factory.riverSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "riverSize", factory.riverSize);
                    factory.dirtSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "dirtSize", factory.dirtSize);
                    factory.dirtCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "dirtCount", factory.dirtCount);
                    factory.dirtMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "dirtMinHeight", factory.dirtMinHeight);
                    factory.dirtMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "dirtMaxHeight", factory.dirtMaxHeight);
                    factory.gravelSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "gravelSize", factory.gravelSize);
                    factory.gravelCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "gravelCount", factory.gravelCount);
                    factory.gravelMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "gravelMinHeight", factory.gravelMinHeight);
                    factory.gravelMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "gravelMaxHeight", factory.gravelMaxHeight);
                    factory.graniteSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "graniteSize", factory.graniteSize);
                    factory.graniteCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "graniteCount", factory.graniteCount);
                    factory.graniteMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "graniteMinHeight", factory.graniteMinHeight);
                    factory.graniteMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "graniteMaxHeight", factory.graniteMaxHeight);
                    factory.dioriteSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "dioriteSize", factory.dioriteSize);
                    factory.dioriteCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "dioriteCount", factory.dioriteCount);
                    factory.dioriteMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "dioriteMinHeight", factory.dioriteMinHeight);
                    factory.dioriteMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "dioriteMaxHeight", factory.dioriteMaxHeight);
                    factory.andesiteSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "andesiteSize", factory.andesiteSize);
                    factory.andesiteCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "andesiteCount", factory.andesiteCount);
                    factory.andesiteMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "andesiteMinHeight", factory.andesiteMinHeight);
                    factory.andesiteMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "andesiteMaxHeight", factory.andesiteMaxHeight);
                    factory.coalSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "coalSize", factory.coalSize);
                    factory.coalCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "coalCount", factory.coalCount);
                    factory.coalMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "coalMinHeight", factory.coalMinHeight);
                    factory.coalMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "coalMaxHeight", factory.coalMaxHeight);
                    factory.ironSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "ironSize", factory.ironSize);
                    factory.ironCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "ironCount", factory.ironCount);
                    factory.ironMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "ironMinHeight", factory.ironMinHeight);
                    factory.ironMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "ironMaxHeight", factory.ironMaxHeight);
                    factory.goldSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "goldSize", factory.goldSize);
                    factory.goldCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "goldCount", factory.goldCount);
                    factory.goldMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "goldMinHeight", factory.goldMinHeight);
                    factory.goldMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "goldMaxHeight", factory.goldMaxHeight);
                    factory.redstoneSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "redstoneSize", factory.redstoneSize);
                    factory.redstoneCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "redstoneCount", factory.redstoneCount);
                    factory.redstoneMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "redstoneMinHeight", factory.redstoneMinHeight);
                    factory.redstoneMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "redstoneMaxHeight", factory.redstoneMaxHeight);
                    factory.diamondSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "diamondSize", factory.diamondSize);
                    factory.diamondCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "diamondCount", factory.diamondCount);
                    factory.diamondMinHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "diamondMinHeight", factory.diamondMinHeight);
                    factory.diamondMaxHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "diamondMaxHeight", factory.diamondMaxHeight);
                    factory.lapisSize = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "lapisSize", factory.lapisSize);
                    factory.lapisCount = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "lapisCount", factory.lapisCount);
                    factory.lapisCenterHeight = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "lapisCenterHeight", factory.lapisCenterHeight);
                    factory.lapisSpread = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(jsonobject, "lapisSpread", factory.lapisSpread);
                }
                catch (Exception exception)
                {
                    ;
                }

                return factory;
            }

            public JsonElement func_177862_a(ChunkProviderSettings.Factory p_177862_1_, Type p_177862_2_, JsonSerializationContext p_177862_3_)
            {
                JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("coordinateScale", Float.valueOf(p_177862_1_.coordinateScale));
                jsonobject.addProperty("heightScale", Float.valueOf(p_177862_1_.heightScale));
                jsonobject.addProperty("lowerLimitScale", Float.valueOf(p_177862_1_.lowerLimitScale));
                jsonobject.addProperty("upperLimitScale", Float.valueOf(p_177862_1_.upperLimitScale));
                jsonobject.addProperty("depthNoiseScaleX", Float.valueOf(p_177862_1_.depthNoiseScaleX));
                jsonobject.addProperty("depthNoiseScaleZ", Float.valueOf(p_177862_1_.depthNoiseScaleZ));
                jsonobject.addProperty("depthNoiseScaleExponent", Float.valueOf(p_177862_1_.depthNoiseScaleExponent));
                jsonobject.addProperty("mainNoiseScaleX", Float.valueOf(p_177862_1_.mainNoiseScaleX));
                jsonobject.addProperty("mainNoiseScaleY", Float.valueOf(p_177862_1_.mainNoiseScaleY));
                jsonobject.addProperty("mainNoiseScaleZ", Float.valueOf(p_177862_1_.mainNoiseScaleZ));
                jsonobject.addProperty("baseSize", Float.valueOf(p_177862_1_.baseSize));
                jsonobject.addProperty("stretchY", Float.valueOf(p_177862_1_.stretchY));
                jsonobject.addProperty("biomeDepthWeight", Float.valueOf(p_177862_1_.biomeDepthWeight));
                jsonobject.addProperty("biomeDepthOffset", Float.valueOf(p_177862_1_.biomeDepthOffset));
                jsonobject.addProperty("biomeScaleWeight", Float.valueOf(p_177862_1_.biomeScaleWeight));
                jsonobject.addProperty("biomeScaleOffset", Float.valueOf(p_177862_1_.biomeScaleOffset));
                jsonobject.addProperty("seaLevel", Integer.valueOf(p_177862_1_.seaLevel));
                jsonobject.addProperty("useCaves", Boolean.valueOf(p_177862_1_.useCaves));
                jsonobject.addProperty("useDungeons", Boolean.valueOf(p_177862_1_.useDungeons));
                jsonobject.addProperty("dungeonChance", Integer.valueOf(p_177862_1_.dungeonChance));
                jsonobject.addProperty("useStrongholds", Boolean.valueOf(p_177862_1_.useStrongholds));
                jsonobject.addProperty("useVillages", Boolean.valueOf(p_177862_1_.useVillages));
                jsonobject.addProperty("useMineShafts", Boolean.valueOf(p_177862_1_.useMineShafts));
                jsonobject.addProperty("useTemples", Boolean.valueOf(p_177862_1_.useTemples));
                jsonobject.addProperty("useMonuments", Boolean.valueOf(p_177862_1_.useMonuments));
                jsonobject.addProperty("useRavines", Boolean.valueOf(p_177862_1_.useRavines));
                jsonobject.addProperty("useWaterLakes", Boolean.valueOf(p_177862_1_.useWaterLakes));
                jsonobject.addProperty("waterLakeChance", Integer.valueOf(p_177862_1_.waterLakeChance));
                jsonobject.addProperty("useLavaLakes", Boolean.valueOf(p_177862_1_.useLavaLakes));
                jsonobject.addProperty("lavaLakeChance", Integer.valueOf(p_177862_1_.lavaLakeChance));
                jsonobject.addProperty("useLavaOceans", Boolean.valueOf(p_177862_1_.useLavaOceans));
                jsonobject.addProperty("fixedBiome", Integer.valueOf(p_177862_1_.fixedBiome));
                jsonobject.addProperty("biomeSize", Integer.valueOf(p_177862_1_.biomeSize));
                jsonobject.addProperty("riverSize", Integer.valueOf(p_177862_1_.riverSize));
                jsonobject.addProperty("dirtSize", Integer.valueOf(p_177862_1_.dirtSize));
                jsonobject.addProperty("dirtCount", Integer.valueOf(p_177862_1_.dirtCount));
                jsonobject.addProperty("dirtMinHeight", Integer.valueOf(p_177862_1_.dirtMinHeight));
                jsonobject.addProperty("dirtMaxHeight", Integer.valueOf(p_177862_1_.dirtMaxHeight));
                jsonobject.addProperty("gravelSize", Integer.valueOf(p_177862_1_.gravelSize));
                jsonobject.addProperty("gravelCount", Integer.valueOf(p_177862_1_.gravelCount));
                jsonobject.addProperty("gravelMinHeight", Integer.valueOf(p_177862_1_.gravelMinHeight));
                jsonobject.addProperty("gravelMaxHeight", Integer.valueOf(p_177862_1_.gravelMaxHeight));
                jsonobject.addProperty("graniteSize", Integer.valueOf(p_177862_1_.graniteSize));
                jsonobject.addProperty("graniteCount", Integer.valueOf(p_177862_1_.graniteCount));
                jsonobject.addProperty("graniteMinHeight", Integer.valueOf(p_177862_1_.graniteMinHeight));
                jsonobject.addProperty("graniteMaxHeight", Integer.valueOf(p_177862_1_.graniteMaxHeight));
                jsonobject.addProperty("dioriteSize", Integer.valueOf(p_177862_1_.dioriteSize));
                jsonobject.addProperty("dioriteCount", Integer.valueOf(p_177862_1_.dioriteCount));
                jsonobject.addProperty("dioriteMinHeight", Integer.valueOf(p_177862_1_.dioriteMinHeight));
                jsonobject.addProperty("dioriteMaxHeight", Integer.valueOf(p_177862_1_.dioriteMaxHeight));
                jsonobject.addProperty("andesiteSize", Integer.valueOf(p_177862_1_.andesiteSize));
                jsonobject.addProperty("andesiteCount", Integer.valueOf(p_177862_1_.andesiteCount));
                jsonobject.addProperty("andesiteMinHeight", Integer.valueOf(p_177862_1_.andesiteMinHeight));
                jsonobject.addProperty("andesiteMaxHeight", Integer.valueOf(p_177862_1_.andesiteMaxHeight));
                jsonobject.addProperty("coalSize", Integer.valueOf(p_177862_1_.coalSize));
                jsonobject.addProperty("coalCount", Integer.valueOf(p_177862_1_.coalCount));
                jsonobject.addProperty("coalMinHeight", Integer.valueOf(p_177862_1_.coalMinHeight));
                jsonobject.addProperty("coalMaxHeight", Integer.valueOf(p_177862_1_.coalMaxHeight));
                jsonobject.addProperty("ironSize", Integer.valueOf(p_177862_1_.ironSize));
                jsonobject.addProperty("ironCount", Integer.valueOf(p_177862_1_.ironCount));
                jsonobject.addProperty("ironMinHeight", Integer.valueOf(p_177862_1_.ironMinHeight));
                jsonobject.addProperty("ironMaxHeight", Integer.valueOf(p_177862_1_.ironMaxHeight));
                jsonobject.addProperty("goldSize", Integer.valueOf(p_177862_1_.goldSize));
                jsonobject.addProperty("goldCount", Integer.valueOf(p_177862_1_.goldCount));
                jsonobject.addProperty("goldMinHeight", Integer.valueOf(p_177862_1_.goldMinHeight));
                jsonobject.addProperty("goldMaxHeight", Integer.valueOf(p_177862_1_.goldMaxHeight));
                jsonobject.addProperty("redstoneSize", Integer.valueOf(p_177862_1_.redstoneSize));
                jsonobject.addProperty("redstoneCount", Integer.valueOf(p_177862_1_.redstoneCount));
                jsonobject.addProperty("redstoneMinHeight", Integer.valueOf(p_177862_1_.redstoneMinHeight));
                jsonobject.addProperty("redstoneMaxHeight", Integer.valueOf(p_177862_1_.redstoneMaxHeight));
                jsonobject.addProperty("diamondSize", Integer.valueOf(p_177862_1_.diamondSize));
                jsonobject.addProperty("diamondCount", Integer.valueOf(p_177862_1_.diamondCount));
                jsonobject.addProperty("diamondMinHeight", Integer.valueOf(p_177862_1_.diamondMinHeight));
                jsonobject.addProperty("diamondMaxHeight", Integer.valueOf(p_177862_1_.diamondMaxHeight));
                jsonobject.addProperty("lapisSize", Integer.valueOf(p_177862_1_.lapisSize));
                jsonobject.addProperty("lapisCount", Integer.valueOf(p_177862_1_.lapisCount));
                jsonobject.addProperty("lapisCenterHeight", Integer.valueOf(p_177862_1_.lapisCenterHeight));
                jsonobject.addProperty("lapisSpread", Integer.valueOf(p_177862_1_.lapisSpread));
                return jsonobject;
            }

            public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.func_177861_a(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }

            public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
            {
                return this.func_177862_a((ChunkProviderSettings.Factory)p_serialize_1_, p_serialize_2_, p_serialize_3_);
            }
        }
}