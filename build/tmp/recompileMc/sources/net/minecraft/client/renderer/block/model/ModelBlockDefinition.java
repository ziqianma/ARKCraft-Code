package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBlockDefinition
{
    static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(ModelBlockDefinition.class, new ModelBlockDefinition.Deserializer()).registerTypeAdapter(ModelBlockDefinition.Variant.class, new ModelBlockDefinition.Variant.Deserializer()).create();
    private final Map mapVariants = Maps.newHashMap();
    private static final String __OBFID = "CL_00002498";

    public static ModelBlockDefinition parseFromReader(Reader p_178331_0_)
    {
        return net.minecraftforge.client.model.BlockStateLoader.load(p_178331_0_, GSON);
    }

    public ModelBlockDefinition(Collection p_i46221_1_)
    {
        Iterator iterator = p_i46221_1_.iterator();

        while (iterator.hasNext())
        {
            ModelBlockDefinition.Variants variants = (ModelBlockDefinition.Variants)iterator.next();
            this.mapVariants.put(variants.name, variants);
        }
    }

    public ModelBlockDefinition(List p_i46222_1_)
    {
        Iterator iterator = p_i46222_1_.iterator();

        while (iterator.hasNext())
        {
            ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition)iterator.next();
            this.mapVariants.putAll(modelblockdefinition.mapVariants);
        }
    }

    public ModelBlockDefinition.Variants getVariants(String p_178330_1_)
    {
        ModelBlockDefinition.Variants variants = (ModelBlockDefinition.Variants)this.mapVariants.get(p_178330_1_);

        if (variants == null)
        {
            throw new ModelBlockDefinition.MissingVariantException();
        }
        else
        {
            return variants;
        }
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ instanceof ModelBlockDefinition)
        {
            ModelBlockDefinition modelblockdefinition = (ModelBlockDefinition)p_equals_1_;
            return this.mapVariants.equals(modelblockdefinition.mapVariants);
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return this.mapVariants.hashCode();
    }

    @SideOnly(Side.CLIENT)
    public static class Deserializer implements JsonDeserializer
        {
            private static final String __OBFID = "CL_00002497";

            public ModelBlockDefinition parseModelBlockDefinition(JsonElement p_178336_1_, Type p_178336_2_, JsonDeserializationContext p_178336_3_)
            {
                JsonObject jsonobject = p_178336_1_.getAsJsonObject();
                List list = this.parseVariantsList(p_178336_3_, jsonobject);
                return new ModelBlockDefinition((Collection)list);
            }

            protected List parseVariantsList(JsonDeserializationContext p_178334_1_, JsonObject p_178334_2_)
            {
                JsonObject jsonobject1 = JsonUtils.getJsonObject(p_178334_2_, "variants");
                ArrayList arraylist = Lists.newArrayList();
                Iterator iterator = jsonobject1.entrySet().iterator();

                while (iterator.hasNext())
                {
                    Entry entry = (Entry)iterator.next();
                    arraylist.add(this.parseVariants(p_178334_1_, entry));
                }

                return arraylist;
            }

            protected ModelBlockDefinition.Variants parseVariants(JsonDeserializationContext p_178335_1_, Entry p_178335_2_)
            {
                String s = (String)p_178335_2_.getKey();
                ArrayList arraylist = Lists.newArrayList();
                JsonElement jsonelement = (JsonElement)p_178335_2_.getValue();

                if (jsonelement.isJsonArray())
                {
                    Iterator iterator = jsonelement.getAsJsonArray().iterator();

                    while (iterator.hasNext())
                    {
                        JsonElement jsonelement1 = (JsonElement)iterator.next();
                        arraylist.add((ModelBlockDefinition.Variant)p_178335_1_.deserialize(jsonelement1, ModelBlockDefinition.Variant.class));
                    }
                }
                else
                {
                    arraylist.add((ModelBlockDefinition.Variant)p_178335_1_.deserialize(jsonelement, ModelBlockDefinition.Variant.class));
                }

                return new ModelBlockDefinition.Variants(s, arraylist);
            }

            public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.parseModelBlockDefinition(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
        }

    @SideOnly(Side.CLIENT)
    public class MissingVariantException extends RuntimeException
    {
        private static final String __OBFID = "CL_00002496";
    }

    @SideOnly(Side.CLIENT)
    public static class Variant
        {
            private final ResourceLocation modelLocation;
            private final ModelRotation modelRotation;
            private final boolean uvLock;
            private final int weight;
            private static final String __OBFID = "CL_00002495";

            public Variant(ResourceLocation p_i46219_1_, ModelRotation p_i46219_2_, boolean p_i46219_3_, int p_i46219_4_)
            {
                this.modelLocation = p_i46219_1_;
                this.modelRotation = p_i46219_2_;
                this.uvLock = p_i46219_3_;
                this.weight = p_i46219_4_;
            }

            public ResourceLocation getModelLocation()
            {
                return this.modelLocation;
            }

            @Deprecated
            public ModelRotation getRotation()
            {
                return this.modelRotation;
            }

            public net.minecraftforge.client.model.IModelState getState()
            {
                return this.modelRotation;
            }

            public boolean isUvLocked()
            {
                return this.uvLock;
            }

            public int getWeight()
            {
                return this.weight;
            }

            public boolean equals(Object p_equals_1_)
            {
                if (this == p_equals_1_)
                {
                    return true;
                }
                else if (!(p_equals_1_ instanceof ModelBlockDefinition.Variant))
                {
                    return false;
                }
                else
                {
                    ModelBlockDefinition.Variant variant = (ModelBlockDefinition.Variant)p_equals_1_;
                    return this.modelLocation.equals(variant.modelLocation) && this.modelRotation == variant.modelRotation && this.uvLock == variant.uvLock;
                }
            }

            public int hashCode()
            {
                int i = this.modelLocation.hashCode();
                i = 31 * i + (this.modelRotation != null ? this.modelRotation.hashCode() : 0);
                i = 31 * i + (this.uvLock ? 1 : 0);
                return i;
            }

            @SideOnly(Side.CLIENT)
            public static class Deserializer implements JsonDeserializer
                {
                    private static final String __OBFID = "CL_00002494";

                    public ModelBlockDefinition.Variant parseVariant(JsonElement p_178425_1_, Type p_178425_2_, JsonDeserializationContext p_178425_3_)
                    {
                        JsonObject jsonobject = p_178425_1_.getAsJsonObject();
                        String s = this.parseModel(jsonobject);
                        ModelRotation modelrotation = this.parseRotation(jsonobject);
                        boolean flag = this.parseUvLock(jsonobject);
                        int i = this.parseWeight(jsonobject);
                        return new ModelBlockDefinition.Variant(this.makeModelLocation(s), modelrotation, flag, i);
                    }

                    private ResourceLocation makeModelLocation(String p_178426_1_)
                    {
                        ResourceLocation resourcelocation = new ResourceLocation(p_178426_1_);
                        resourcelocation = new ResourceLocation(resourcelocation.getResourceDomain(), "block/" + resourcelocation.getResourcePath());
                        return resourcelocation;
                    }

                    private boolean parseUvLock(JsonObject p_178429_1_)
                    {
                        return JsonUtils.getJsonObjectBooleanFieldValueOrDefault(p_178429_1_, "uvlock", false);
                    }

                    protected ModelRotation parseRotation(JsonObject p_178428_1_)
                    {
                        int i = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178428_1_, "x", 0);
                        int j = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178428_1_, "y", 0);
                        ModelRotation modelrotation = ModelRotation.getModelRotation(i, j);

                        if (modelrotation == null)
                        {
                            throw new JsonParseException("Invalid BlockModelRotation x: " + i + ", y: " + j);
                        }
                        else
                        {
                            return modelrotation;
                        }
                    }

                    protected String parseModel(JsonObject p_178424_1_)
                    {
                        return JsonUtils.getJsonObjectStringFieldValue(p_178424_1_, "model");
                    }

                    protected int parseWeight(JsonObject p_178427_1_)
                    {
                        return JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178427_1_, "weight", 1);
                    }

                    public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
                    {
                        return this.parseVariant(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
                    }
                }
        }

    @SideOnly(Side.CLIENT)
    public static class Variants
        {
            private final String name;
            private final List listVariants;
            private static final String __OBFID = "CL_00002493";

            public Variants(String p_i46218_1_, List p_i46218_2_)
            {
                this.name = p_i46218_1_;
                this.listVariants = p_i46218_2_;
            }

            public List getVariants()
            {
                return this.listVariants;
            }

            public boolean equals(Object p_equals_1_)
            {
                if (this == p_equals_1_)
                {
                    return true;
                }
                else if (!(p_equals_1_ instanceof ModelBlockDefinition.Variants))
                {
                    return false;
                }
                else
                {
                    ModelBlockDefinition.Variants variants = (ModelBlockDefinition.Variants)p_equals_1_;
                    return !this.name.equals(variants.name) ? false : this.listVariants.equals(variants.listVariants);
                }
            }

            public int hashCode()
            {
                int i = this.name.hashCode();
                i = 31 * i + this.listVariants.hashCode();
                return i;
            }
        }
}