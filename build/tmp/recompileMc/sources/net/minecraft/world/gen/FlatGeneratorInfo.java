package net.minecraft.world.gen;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;

public class FlatGeneratorInfo
{
    /** List of layers on this preset. */
    private final List flatLayers = Lists.newArrayList();
    /** List of world features enabled on this preset. */
    private final Map worldFeatures = Maps.newHashMap();
    private int biomeToUse;
    private static final String __OBFID = "CL_00000440";

    /**
     * Return the biome used on this preset.
     */
    public int getBiome()
    {
        return this.biomeToUse;
    }

    /**
     * Set the biome used on this preset.
     */
    public void setBiome(int p_82647_1_)
    {
        this.biomeToUse = p_82647_1_;
    }

    /**
     * Return the list of world features enabled on this preset.
     */
    public Map getWorldFeatures()
    {
        return this.worldFeatures;
    }

    /**
     * Return the list of layers on this preset.
     */
    public List getFlatLayers()
    {
        return this.flatLayers;
    }

    public void func_82645_d()
    {
        int i = 0;
        FlatLayerInfo flatlayerinfo;

        for (Iterator iterator = this.flatLayers.iterator(); iterator.hasNext(); i += flatlayerinfo.getLayerCount())
        {
            flatlayerinfo = (FlatLayerInfo)iterator.next();
            flatlayerinfo.setMinY(i);
        }
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(3);
        stringbuilder.append(";");
        int i;

        for (i = 0; i < this.flatLayers.size(); ++i)
        {
            if (i > 0)
            {
                stringbuilder.append(",");
            }

            stringbuilder.append(((FlatLayerInfo)this.flatLayers.get(i)).toString());
        }

        stringbuilder.append(";");
        stringbuilder.append(this.biomeToUse);

        if (!this.worldFeatures.isEmpty())
        {
            stringbuilder.append(";");
            i = 0;
            Iterator iterator = this.worldFeatures.entrySet().iterator();

            while (iterator.hasNext())
            {
                Entry entry = (Entry)iterator.next();

                if (i++ > 0)
                {
                    stringbuilder.append(",");
                }

                stringbuilder.append(((String)entry.getKey()).toLowerCase());
                Map map = (Map)entry.getValue();

                if (!map.isEmpty())
                {
                    stringbuilder.append("(");
                    int j = 0;
                    Iterator iterator1 = map.entrySet().iterator();

                    while (iterator1.hasNext())
                    {
                        Entry entry1 = (Entry)iterator1.next();

                        if (j++ > 0)
                        {
                            stringbuilder.append(" ");
                        }

                        stringbuilder.append((String)entry1.getKey());
                        stringbuilder.append("=");
                        stringbuilder.append((String)entry1.getValue());
                    }

                    stringbuilder.append(")");
                }
            }
        }
        else
        {
            stringbuilder.append(";");
        }

        return stringbuilder.toString();
    }

    private static FlatLayerInfo func_180715_a(int p_180715_0_, String p_180715_1_, int p_180715_2_)
    {
        String[] astring = p_180715_0_ >= 3 ? p_180715_1_.split("\\*", 2) : p_180715_1_.split("x", 2);
        int k = 1;
        int l = 0;

        if (astring.length == 2)
        {
            try
            {
                k = Integer.parseInt(astring[0]);

                if (p_180715_2_ + k >= 256)
                {
                    k = 256 - p_180715_2_;
                }

                if (k < 0)
                {
                    k = 0;
                }
            }
            catch (Throwable throwable)
            {
                return null;
            }
        }

        Block block = null;

        try
        {
            String s1 = astring[astring.length - 1];

            if (p_180715_0_ < 3)
            {
                astring = s1.split(":", 2);

                if (astring.length > 1)
                {
                    l = Integer.parseInt(astring[1]);
                }

                block = Block.getBlockById(Integer.parseInt(astring[0]));
            }
            else
            {
                astring = s1.split(":", 3);
                block = astring.length > 1 ? Block.getBlockFromName(astring[0] + ":" + astring[1]) : null;

                if (block != null)
                {
                    l = astring.length > 2 ? Integer.parseInt(astring[2]) : 0;
                }
                else
                {
                    block = Block.getBlockFromName(astring[0]);

                    if (block != null)
                    {
                        l = astring.length > 1 ? Integer.parseInt(astring[1]) : 0;
                    }
                }

                if (block == null)
                {
                    return null;
                }
            }

            if (block == Blocks.air)
            {
                l = 0;
            }

            if (l < 0 || l > 15)
            {
                l = 0;
            }
        }
        catch (Throwable throwable1)
        {
            return null;
        }

        FlatLayerInfo flatlayerinfo = new FlatLayerInfo(p_180715_0_, k, block, l);
        flatlayerinfo.setMinY(p_180715_2_);
        return flatlayerinfo;
    }

    private static List func_180716_a(int p_180716_0_, String p_180716_1_)
    {
        if (p_180716_1_ != null && p_180716_1_.length() >= 1)
        {
            ArrayList arraylist = Lists.newArrayList();
            String[] astring = p_180716_1_.split(",");
            int j = 0;
            String[] astring1 = astring;
            int k = astring.length;

            for (int l = 0; l < k; ++l)
            {
                String s1 = astring1[l];
                FlatLayerInfo flatlayerinfo = func_180715_a(p_180716_0_, s1, j);

                if (flatlayerinfo == null)
                {
                    return null;
                }

                arraylist.add(flatlayerinfo);
                j += flatlayerinfo.getLayerCount();
            }

            return arraylist;
        }
        else
        {
            return null;
        }
    }

    public static FlatGeneratorInfo createFlatGeneratorFromString(String p_82651_0_)
    {
        if (p_82651_0_ == null)
        {
            return getDefaultFlatGenerator();
        }
        else
        {
            String[] astring = p_82651_0_.split(";", -1);
            int i = astring.length == 1 ? 0 : MathHelper.parseIntWithDefault(astring[0], 0);

            if (i >= 0 && i <= 3)
            {
                FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
                int j = astring.length == 1 ? 0 : 1;
                List list = func_180716_a(i, astring[j++]);

                if (list != null && !list.isEmpty())
                {
                    flatgeneratorinfo.getFlatLayers().addAll(list);
                    flatgeneratorinfo.func_82645_d();
                    int k = BiomeGenBase.plains.biomeID;

                    if (i > 0 && astring.length > j)
                    {
                        k = MathHelper.parseIntWithDefault(astring[j++], k);
                    }

                    flatgeneratorinfo.setBiome(k);

                    if (i > 0 && astring.length > j)
                    {
                        String[] astring1 = astring[j++].toLowerCase().split(",");
                        String[] astring2 = astring1;
                        int l = astring1.length;

                        for (int i1 = 0; i1 < l; ++i1)
                        {
                            String s1 = astring2[i1];
                            String[] astring3 = s1.split("\\(", 2);
                            HashMap hashmap = Maps.newHashMap();

                            if (astring3[0].length() > 0)
                            {
                                flatgeneratorinfo.getWorldFeatures().put(astring3[0], hashmap);

                                if (astring3.length > 1 && astring3[1].endsWith(")") && astring3[1].length() > 1)
                                {
                                    String[] astring4 = astring3[1].substring(0, astring3[1].length() - 1).split(" ");

                                    for (int j1 = 0; j1 < astring4.length; ++j1)
                                    {
                                        String[] astring5 = astring4[j1].split("=", 2);

                                        if (astring5.length == 2)
                                        {
                                            hashmap.put(astring5[0], astring5[1]);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else
                    {
                        flatgeneratorinfo.getWorldFeatures().put("village", Maps.newHashMap());
                    }

                    return flatgeneratorinfo;
                }
                else
                {
                    return getDefaultFlatGenerator();
                }
            }
            else
            {
                return getDefaultFlatGenerator();
            }
        }
    }

    public static FlatGeneratorInfo getDefaultFlatGenerator()
    {
        FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
        flatgeneratorinfo.setBiome(BiomeGenBase.plains.biomeID);
        flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.bedrock));
        flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(2, Blocks.dirt));
        flatgeneratorinfo.getFlatLayers().add(new FlatLayerInfo(1, Blocks.grass));
        flatgeneratorinfo.func_82645_d();
        flatgeneratorinfo.getWorldFeatures().put("village", Maps.newHashMap());
        return flatgeneratorinfo;
    }
}