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
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class ModelBlock
{
    private static final Logger LOGGER = LogManager.getLogger();
    static final Gson SERIALIZER = (new GsonBuilder()).registerTypeAdapter(ModelBlock.class, new ModelBlock.Deserializer()).registerTypeAdapter(BlockPart.class, new BlockPart.Deserializer()).registerTypeAdapter(BlockPartFace.class, new BlockPartFace.Deserializer()).registerTypeAdapter(BlockFaceUV.class, new BlockFaceUV.Deserializer()).registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer()).registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer()).create();
    private final List elements;
    private final boolean gui3d;
    private final boolean ambientOcclusion;
    private ItemCameraTransforms cameraTransforms;
    public String name;
    public final Map textures;
    public ModelBlock parent;
    protected ResourceLocation parentLocation;
    private static final String __OBFID = "CL_00002503";

    public static ModelBlock deserialize(Reader p_178307_0_)
    {
        return (ModelBlock)SERIALIZER.fromJson(p_178307_0_, ModelBlock.class);
    }

    public static ModelBlock deserialize(String p_178294_0_)
    {
        return deserialize(new StringReader(p_178294_0_));
    }

    protected ModelBlock(List p_i46225_1_, Map p_i46225_2_, boolean p_i46225_3_, boolean p_i46225_4_, ItemCameraTransforms p_i46225_5_)
    {
        this((ResourceLocation)null, p_i46225_1_, p_i46225_2_, p_i46225_3_, p_i46225_4_, p_i46225_5_);
    }

    protected ModelBlock(ResourceLocation p_i46226_1_, Map p_i46226_2_, boolean p_i46226_3_, boolean p_i46226_4_, ItemCameraTransforms p_i46226_5_)
    {
        this(p_i46226_1_, Collections.emptyList(), p_i46226_2_, p_i46226_3_, p_i46226_4_, p_i46226_5_);
    }

    public ModelBlock(ResourceLocation p_i46227_1_, List p_i46227_2_, Map p_i46227_3_, boolean p_i46227_4_, boolean p_i46227_5_, ItemCameraTransforms p_i46227_6_)
    {
        this.name = "";
        this.elements = p_i46227_2_;
        this.ambientOcclusion = p_i46227_4_;
        this.gui3d = p_i46227_5_;
        this.textures = p_i46227_3_;
        this.parentLocation = p_i46227_1_;
        this.cameraTransforms = p_i46227_6_;
    }

    public List getElements()
    {
        return this.hasParent() ? this.parent.getElements() : this.elements;
    }

    private boolean hasParent()
    {
        return this.parent != null;
    }

    public boolean isAmbientOcclusion()
    {
        return this.hasParent() ? this.parent.isAmbientOcclusion() : this.ambientOcclusion;
    }

    public boolean isGui3d()
    {
        return this.gui3d;
    }

    public boolean isResolved()
    {
        return this.parentLocation == null || this.parent != null && this.parent.isResolved();
    }

    public void getParentFromMap(Map p_178299_1_)
    {
        if (this.parentLocation != null)
        {
            this.parent = (ModelBlock)p_178299_1_.get(this.parentLocation);
        }
    }

    public boolean isTexturePresent(String p_178300_1_)
    {
        return !"missingno".equals(this.resolveTextureName(p_178300_1_));
    }

    public String resolveTextureName(String p_178308_1_)
    {
        if (!this.startsWithHash(p_178308_1_))
        {
            p_178308_1_ = '#' + p_178308_1_;
        }

        return this.resolveTextureName(p_178308_1_, new ModelBlock.Bookkeep(null));
    }

    private String resolveTextureName(String p_178302_1_, ModelBlock.Bookkeep p_178302_2_)
    {
        if (this.startsWithHash(p_178302_1_))
        {
            if (this == p_178302_2_.modelExt)
            {
                LOGGER.warn("Unable to resolve texture due to upward reference: " + p_178302_1_ + " in " + this.name);
                return "missingno";
            }
            else
            {
                String s1 = (String)this.textures.get(p_178302_1_.substring(1));

                if (s1 == null && this.hasParent())
                {
                    s1 = this.parent.resolveTextureName(p_178302_1_, p_178302_2_);
                }

                p_178302_2_.modelExt = this;

                if (s1 != null && this.startsWithHash(s1))
                {
                    s1 = p_178302_2_.model.resolveTextureName(s1, p_178302_2_);
                }

                return s1 != null && !this.startsWithHash(s1) ? s1 : "missingno";
            }
        }
        else
        {
            return p_178302_1_;
        }
    }

    private boolean startsWithHash(String p_178304_1_)
    {
        return p_178304_1_.charAt(0) == 35;
    }

    public ResourceLocation getParentLocation()
    {
        return this.parentLocation;
    }

    public ModelBlock getRootModel()
    {
        return this.hasParent() ? this.parent.getRootModel() : this;
    }

    public ItemTransformVec3f getThirdPersonTransform()
    {
        return this.parent != null && this.cameraTransforms.thirdPerson == ItemTransformVec3f.DEFAULT ? this.parent.getThirdPersonTransform() : this.cameraTransforms.thirdPerson;
    }

    public ItemTransformVec3f getFirstPersonTransform()
    {
        return this.parent != null && this.cameraTransforms.firstPerson == ItemTransformVec3f.DEFAULT ? this.parent.getFirstPersonTransform() : this.cameraTransforms.firstPerson;
    }

    public ItemTransformVec3f getHeadTransform()
    {
        return this.parent != null && this.cameraTransforms.head == ItemTransformVec3f.DEFAULT ? this.parent.getHeadTransform() : this.cameraTransforms.head;
    }

    public ItemTransformVec3f getInGuiTransform()
    {
        return this.parent != null && this.cameraTransforms.gui == ItemTransformVec3f.DEFAULT ? this.parent.getInGuiTransform() : this.cameraTransforms.gui;
    }

    public static void checkModelHierarchy(Map p_178312_0_)
    {
        Iterator iterator = p_178312_0_.values().iterator();

        while (iterator.hasNext())
        {
            ModelBlock modelblock = (ModelBlock)iterator.next();

            try
            {
                ModelBlock modelblock1 = modelblock.parent;

                for (ModelBlock modelblock2 = modelblock1.parent; modelblock1 != modelblock2; modelblock2 = modelblock2.parent.parent)
                {
                    modelblock1 = modelblock1.parent;
                }

                throw new ModelBlock.LoopException();
            }
            catch (NullPointerException nullpointerexception)
            {
                ;
            }
        }
    }

    @SideOnly(Side.CLIENT)
    final class Bookkeep
    {
        public final ModelBlock model;
        public ModelBlock modelExt;
        private static final String __OBFID = "CL_00002501";

        private Bookkeep()
        {
            this.model = ModelBlock.this;
        }

        Bookkeep(Object p_i46224_2_)
        {
            this();
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Deserializer implements JsonDeserializer
        {
            private static final String __OBFID = "CL_00002500";

            public ModelBlock parseModelBlock(JsonElement p_178327_1_, Type p_178327_2_, JsonDeserializationContext p_178327_3_)
            {
                JsonObject jsonobject = p_178327_1_.getAsJsonObject();
                List list = this.getModelElements(p_178327_3_, jsonobject);
                String s = this.getParent(jsonobject);
                boolean flag = StringUtils.isEmpty(s);
                boolean flag1 = list.isEmpty();

                if (flag1 && flag)
                {
                    throw new JsonParseException("BlockModel requires either elements or parent, found neither");
                }
                else if (!flag && !flag1)
                {
                    throw new JsonParseException("BlockModel requires either elements or parent, found both");
                }
                else
                {
                    Map map = this.getTextures(jsonobject);
                    boolean flag2 = this.getAmbientOcclusionEnabled(jsonobject);
                    ItemCameraTransforms itemcameratransforms = ItemCameraTransforms.DEFAULT;

                    if (jsonobject.has("display"))
                    {
                        JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "display");
                        itemcameratransforms = (ItemCameraTransforms)p_178327_3_.deserialize(jsonobject1, ItemCameraTransforms.class);
                    }

                    return flag1 ? new ModelBlock(new ResourceLocation(s), map, flag2, true, itemcameratransforms) : new ModelBlock(list, map, flag2, true, itemcameratransforms);
                }
            }

            private Map getTextures(JsonObject p_178329_1_)
            {
                HashMap hashmap = Maps.newHashMap();

                if (p_178329_1_.has("textures"))
                {
                    JsonObject jsonobject1 = p_178329_1_.getAsJsonObject("textures");
                    Iterator iterator = jsonobject1.entrySet().iterator();

                    while (iterator.hasNext())
                    {
                        Entry entry = (Entry)iterator.next();
                        hashmap.put(entry.getKey(), ((JsonElement)entry.getValue()).getAsString());
                    }
                }

                return hashmap;
            }

            private String getParent(JsonObject p_178326_1_)
            {
                return JsonUtils.getJsonObjectStringFieldValueOrDefault(p_178326_1_, "parent", "");
            }

            protected boolean getAmbientOcclusionEnabled(JsonObject p_178328_1_)
            {
                return JsonUtils.getJsonObjectBooleanFieldValueOrDefault(p_178328_1_, "ambientocclusion", true);
            }

            protected List getModelElements(JsonDeserializationContext p_178325_1_, JsonObject p_178325_2_)
            {
                ArrayList arraylist = Lists.newArrayList();

                if (p_178325_2_.has("elements"))
                {
                    Iterator iterator = JsonUtils.getJsonObjectJsonArrayField(p_178325_2_, "elements").iterator();

                    while (iterator.hasNext())
                    {
                        JsonElement jsonelement = (JsonElement)iterator.next();
                        arraylist.add((BlockPart)p_178325_1_.deserialize(jsonelement, BlockPart.class));
                    }
                }

                return arraylist;
            }

            public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.parseModelBlock(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
        }

    @SideOnly(Side.CLIENT)
    public static class LoopException extends RuntimeException
        {
            private static final String __OBFID = "CL_00002499";
        }
}