package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.vecmath.Vector3f;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * @deprecated use {@link net.minecraftforge.client.model.IModelState} and {@link net.minecraftforge.client.model.TRSRTransformation}
 */
@SideOnly(Side.CLIENT)
@Deprecated
public class ItemTransformVec3f implements net.minecraftforge.client.model.IModelState
{
    public net.minecraftforge.client.model.TRSRTransformation apply(net.minecraftforge.client.model.IModelPart part) { return new net.minecraftforge.client.model.TRSRTransformation(this); }
    public static final ItemTransformVec3f DEFAULT = new ItemTransformVec3f(new Vector3f(), new Vector3f(), new Vector3f(1.0F, 1.0F, 1.0F));
    public final Vector3f rotation;
    public final Vector3f translation;
    public final Vector3f scale;
    private static final String __OBFID = "CL_00002484";

    public ItemTransformVec3f(Vector3f rotation, Vector3f translation, Vector3f scale)
    {
        this.rotation = new Vector3f(rotation);
        this.translation = new Vector3f(translation);
        this.scale = new Vector3f(scale);
    }

    @SideOnly(Side.CLIENT)
    static class Deserializer implements JsonDeserializer
        {
            private static final Vector3f ROTATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
            private static final Vector3f TRANSLATION_DEFAULT = new Vector3f(0.0F, 0.0F, 0.0F);
            private static final Vector3f SCALE_DEFAULT = new Vector3f(1.0F, 1.0F, 1.0F);
            private static final String __OBFID = "CL_00002483";

            public ItemTransformVec3f deserialize0(JsonElement element, Type type, JsonDeserializationContext context)
            {
                JsonObject jsonobject = element.getAsJsonObject();
                Vector3f vector3f = this.parseVector3f(jsonobject, "rotation", ROTATION_DEFAULT);
                Vector3f vector3f1 = this.parseVector3f(jsonobject, "translation", TRANSLATION_DEFAULT);
                vector3f1.scale(0.0625F);
                MathHelper.clamp_double((double)vector3f1.x, -1.5D, 1.5D);
                MathHelper.clamp_double((double)vector3f1.y, -1.5D, 1.5D);
                MathHelper.clamp_double((double)vector3f1.z, -1.5D, 1.5D);
                Vector3f vector3f2 = this.parseVector3f(jsonobject, "scale", SCALE_DEFAULT);
                MathHelper.clamp_double((double)vector3f2.x, -1.5D, 1.5D);
                MathHelper.clamp_double((double)vector3f2.y, -1.5D, 1.5D);
                MathHelper.clamp_double((double)vector3f2.z, -1.5D, 1.5D);
                return new ItemTransformVec3f(vector3f, vector3f1, vector3f2);
            }

            private Vector3f parseVector3f(JsonObject jsonObject, String key, Vector3f defaultValue)
            {
                if (!jsonObject.has(key))
                {
                    return defaultValue;
                }
                else
                {
                    JsonArray jsonarray = JsonUtils.getJsonObjectJsonArrayField(jsonObject, key);

                    if (jsonarray.size() != 3)
                    {
                        throw new JsonParseException("Expected 3 " + key + " values, found: " + jsonarray.size());
                    }
                    else
                    {
                        float[] afloat = new float[3];

                        for (int i = 0; i < afloat.length; ++i)
                        {
                            afloat[i] = JsonUtils.getJsonElementFloatValue(jsonarray.get(i), key + "[" + i + "]");
                        }

                        return new Vector3f(afloat);
                    }
                }
            }

            public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.deserialize0(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
        }
}