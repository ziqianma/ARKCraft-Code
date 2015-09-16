package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockFaceUV
{
    public float[] uvs;
    public final int rotation;
    private static final String __OBFID = "CL_00002505";

    public BlockFaceUV(float[] p_i46228_1_, int p_i46228_2_)
    {
        this.uvs = p_i46228_1_;
        this.rotation = p_i46228_2_;
    }

    public float func_178348_a(int p_178348_1_)
    {
        if (this.uvs == null)
        {
            throw new NullPointerException("uvs");
        }
        else
        {
            int j = this.func_178347_d(p_178348_1_);
            return j != 0 && j != 1 ? this.uvs[2] : this.uvs[0];
        }
    }

    public float func_178346_b(int p_178346_1_)
    {
        if (this.uvs == null)
        {
            throw new NullPointerException("uvs");
        }
        else
        {
            int j = this.func_178347_d(p_178346_1_);
            return j != 0 && j != 3 ? this.uvs[3] : this.uvs[1];
        }
    }

    private int func_178347_d(int p_178347_1_)
    {
        return (p_178347_1_ + this.rotation / 90) % 4;
    }

    public int func_178345_c(int p_178345_1_)
    {
        return (p_178345_1_ + (4 - this.rotation / 90)) % 4;
    }

    public void setUvs(float[] p_178349_1_)
    {
        if (this.uvs == null)
        {
            this.uvs = p_178349_1_;
        }
    }

    @SideOnly(Side.CLIENT)
    static class Deserializer implements JsonDeserializer
        {
            private static final String __OBFID = "CL_00002504";

            public BlockFaceUV parseBlockFaceUV(JsonElement p_178293_1_, Type p_178293_2_, JsonDeserializationContext p_178293_3_)
            {
                JsonObject jsonobject = p_178293_1_.getAsJsonObject();
                float[] afloat = this.parseUV(jsonobject);
                int i = this.parseRotation(jsonobject);
                return new BlockFaceUV(afloat, i);
            }

            protected int parseRotation(JsonObject p_178291_1_)
            {
                int i = JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178291_1_, "rotation", 0);

                if (i >= 0 && i % 90 == 0 && i / 90 <= 3)
                {
                    return i;
                }
                else
                {
                    throw new JsonParseException("Invalid rotation " + i + " found, only 0/90/180/270 allowed");
                }
            }

            private float[] parseUV(JsonObject p_178292_1_)
            {
                if (!p_178292_1_.has("uv"))
                {
                    return null;
                }
                else
                {
                    JsonArray jsonarray = JsonUtils.getJsonObjectJsonArrayField(p_178292_1_, "uv");

                    if (jsonarray.size() != 4)
                    {
                        throw new JsonParseException("Expected 4 uv values, found: " + jsonarray.size());
                    }
                    else
                    {
                        float[] afloat = new float[4];

                        for (int i = 0; i < afloat.length; ++i)
                        {
                            afloat[i] = JsonUtils.getJsonElementFloatValue(jsonarray.get(i), "uv[" + i + "]");
                        }

                        return afloat;
                    }
                }
            }

            public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.parseBlockFaceUV(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
        }
}