package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * @deprecated use {@link net.minecraftforge.client.model.IPerspectiveAwareModel} instead
 */
@SideOnly(Side.CLIENT)
@Deprecated
public class ItemCameraTransforms
{
    public static final ItemCameraTransforms DEFAULT = new ItemCameraTransforms(ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT, ItemTransformVec3f.DEFAULT);
    public final ItemTransformVec3f thirdPerson;
    public final ItemTransformVec3f firstPerson;
    public final ItemTransformVec3f head;
    public final ItemTransformVec3f gui;
    private static final String __OBFID = "CL_00002482";

    public ItemCameraTransforms(ItemTransformVec3f p_i46213_1_, ItemTransformVec3f p_i46213_2_, ItemTransformVec3f p_i46213_3_, ItemTransformVec3f p_i46213_4_)
    {
        this.thirdPerson = p_i46213_1_;
        this.firstPerson = p_i46213_2_;
        this.head = p_i46213_3_;
        this.gui = p_i46213_4_;
    }

    @SideOnly(Side.CLIENT)
    static class Deserializer implements JsonDeserializer
        {
            private static final String __OBFID = "CL_00002481";

            public ItemCameraTransforms parseItemCameraTransforms(JsonElement p_178352_1_, Type p_178352_2_, JsonDeserializationContext p_178352_3_)
            {
                JsonObject jsonobject = p_178352_1_.getAsJsonObject();
                ItemTransformVec3f itemtransformvec3f = ItemTransformVec3f.DEFAULT;
                ItemTransformVec3f itemtransformvec3f1 = ItemTransformVec3f.DEFAULT;
                ItemTransformVec3f itemtransformvec3f2 = ItemTransformVec3f.DEFAULT;
                ItemTransformVec3f itemtransformvec3f3 = ItemTransformVec3f.DEFAULT;

                if (jsonobject.has("thirdperson"))
                {
                    itemtransformvec3f = (ItemTransformVec3f)p_178352_3_.deserialize(jsonobject.get("thirdperson"), ItemTransformVec3f.class);
                }

                if (jsonobject.has("firstperson"))
                {
                    itemtransformvec3f1 = (ItemTransformVec3f)p_178352_3_.deserialize(jsonobject.get("firstperson"), ItemTransformVec3f.class);
                }

                if (jsonobject.has("head"))
                {
                    itemtransformvec3f2 = (ItemTransformVec3f)p_178352_3_.deserialize(jsonobject.get("head"), ItemTransformVec3f.class);
                }

                if (jsonobject.has("gui"))
                {
                    itemtransformvec3f3 = (ItemTransformVec3f)p_178352_3_.deserialize(jsonobject.get("gui"), ItemTransformVec3f.class);
                }

                return new ItemCameraTransforms(itemtransformvec3f, itemtransformvec3f1, itemtransformvec3f2, itemtransformvec3f3);
            }

            public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.parseItemCameraTransforms(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
        }

    @SideOnly(Side.CLIENT)
    public static enum TransformType
    {
        NONE,
        THIRD_PERSON,
        FIRST_PERSON,
        HEAD,
        GUI;

        private static final String __OBFID = "CL_00002480";
    }
}