package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.vecmath.Vector3f;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockPart
{
    public final Vector3f positionFrom;
    public final Vector3f positionTo;
    public final Map mapFaces;
    public final BlockPartRotation partRotation;
    public final boolean shade;
    private static final String __OBFID = "CL_00002511";

    public BlockPart(Vector3f p_i46231_1_, Vector3f p_i46231_2_, Map p_i46231_3_, BlockPartRotation p_i46231_4_, boolean p_i46231_5_)
    {
        this.positionFrom = p_i46231_1_;
        this.positionTo = p_i46231_2_;
        this.mapFaces = p_i46231_3_;
        this.partRotation = p_i46231_4_;
        this.shade = p_i46231_5_;
        this.setDefaultUvs();
    }

    private void setDefaultUvs()
    {
        Iterator iterator = this.mapFaces.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry entry = (Entry)iterator.next();
            float[] afloat = this.getFaceUvs((EnumFacing)entry.getKey());
            ((BlockPartFace)entry.getValue()).blockFaceUV.setUvs(afloat);
        }
    }

    private float[] getFaceUvs(EnumFacing p_178236_1_)
    {
        float[] afloat;

        switch (BlockPart.SwitchEnumFacing.field_178234_a[p_178236_1_.ordinal()])
        {
            case 1:
            case 2:
                afloat = new float[] {this.positionFrom.x, this.positionFrom.z, this.positionTo.x, this.positionTo.z};
                break;
            case 3:
            case 4:
                afloat = new float[] {this.positionFrom.x, 16.0F - this.positionTo.y, this.positionTo.x, 16.0F - this.positionFrom.y};
                break;
            case 5:
            case 6:
                afloat = new float[] {this.positionFrom.z, 16.0F - this.positionTo.y, this.positionTo.z, 16.0F - this.positionFrom.y};
                break;
            default:
                throw new NullPointerException();
        }

        return afloat;
    }

    @SideOnly(Side.CLIENT)
    static class Deserializer implements JsonDeserializer
        {
            private static final String __OBFID = "CL_00002509";

            public BlockPart parseBlockPart(JsonElement p_178254_1_, Type p_178254_2_, JsonDeserializationContext p_178254_3_)
            {
                JsonObject jsonobject = p_178254_1_.getAsJsonObject();
                Vector3f vector3f = this.parsePositionFrom(jsonobject);
                Vector3f vector3f1 = this.parsePositionTo(jsonobject);
                BlockPartRotation blockpartrotation = this.parseRotation(jsonobject);
                Map map = this.parseFacesCheck(p_178254_3_, jsonobject);

                if (jsonobject.has("shade") && !JsonUtils.isJsonObjectBooleanField(jsonobject, "shade"))
                {
                    throw new JsonParseException("Expected shade to be a Boolean");
                }
                else
                {
                    boolean flag = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject, "shade", true);
                    return new BlockPart(vector3f, vector3f1, map, blockpartrotation, flag);
                }
            }

            private BlockPartRotation parseRotation(JsonObject p_178256_1_)
            {
                BlockPartRotation blockpartrotation = null;

                if (p_178256_1_.has("rotation"))
                {
                    JsonObject jsonobject1 = JsonUtils.getJsonObject(p_178256_1_, "rotation");
                    Vector3f vector3f = this.parsePosition(jsonobject1, "origin");
                    vector3f.scale(0.0625F);
                    EnumFacing.Axis axis = this.parseAxis(jsonobject1);
                    float f = this.parseAngle(jsonobject1);
                    boolean flag = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(jsonobject1, "rescale", false);
                    blockpartrotation = new BlockPartRotation(vector3f, axis, f, flag);
                }

                return blockpartrotation;
            }

            private float parseAngle(JsonObject p_178255_1_)
            {
                float f = JsonUtils.getJsonObjectFloatFieldValue(p_178255_1_, "angle");

                if (f != 0.0F && MathHelper.abs(f) != 22.5F && MathHelper.abs(f) != 45.0F)
                {
                    throw new JsonParseException("Invalid rotation " + f + " found, only -45/-22.5/0/22.5/45 allowed");
                }
                else
                {
                    return f;
                }
            }

            private EnumFacing.Axis parseAxis(JsonObject p_178252_1_)
            {
                String s = JsonUtils.getJsonObjectStringFieldValue(p_178252_1_, "axis");
                EnumFacing.Axis axis = EnumFacing.Axis.byName(s.toLowerCase());

                if (axis == null)
                {
                    throw new JsonParseException("Invalid rotation axis: " + s);
                }
                else
                {
                    return axis;
                }
            }

            private Map parseFacesCheck(JsonDeserializationContext p_178250_1_, JsonObject p_178250_2_)
            {
                Map map = this.parseFaces(p_178250_1_, p_178250_2_);

                if (map.isEmpty())
                {
                    throw new JsonParseException("Expected between 1 and 6 unique faces, got 0");
                }
                else
                {
                    return map;
                }
            }

            private Map parseFaces(JsonDeserializationContext p_178253_1_, JsonObject p_178253_2_)
            {
                EnumMap enummap = Maps.newEnumMap(EnumFacing.class);
                JsonObject jsonobject1 = JsonUtils.getJsonObject(p_178253_2_, "faces");
                Iterator iterator = jsonobject1.entrySet().iterator();

                while (iterator.hasNext())
                {
                    Entry entry = (Entry)iterator.next();
                    EnumFacing enumfacing = this.parseEnumFacing((String)entry.getKey());
                    enummap.put(enumfacing, (BlockPartFace)p_178253_1_.deserialize((JsonElement)entry.getValue(), BlockPartFace.class));
                }

                return enummap;
            }

            private EnumFacing parseEnumFacing(String p_178248_1_)
            {
                EnumFacing enumfacing = EnumFacing.byName(p_178248_1_);

                if (enumfacing == null)
                {
                    throw new JsonParseException("Unknown facing: " + p_178248_1_);
                }
                else
                {
                    return enumfacing;
                }
            }

            private Vector3f parsePositionTo(JsonObject p_178247_1_)
            {
                Vector3f vector3f = this.parsePosition(p_178247_1_, "to");

                if (vector3f.x >= -16.0F && vector3f.y >= -16.0F && vector3f.z >= -16.0F && vector3f.x <= 32.0F && vector3f.y <= 32.0F && vector3f.z <= 32.0F)
                {
                    return vector3f;
                }
                else
                {
                    throw new JsonParseException("\'to\' specifier exceeds the allowed boundaries: " + vector3f);
                }
            }

            private Vector3f parsePositionFrom(JsonObject p_178249_1_)
            {
                Vector3f vector3f = this.parsePosition(p_178249_1_, "from");

                if (vector3f.x >= -16.0F && vector3f.y >= -16.0F && vector3f.z >= -16.0F && vector3f.x <= 32.0F && vector3f.y <= 32.0F && vector3f.z <= 32.0F)
                {
                    return vector3f;
                }
                else
                {
                    throw new JsonParseException("\'from\' specifier exceeds the allowed boundaries: " + vector3f);
                }
            }

            private Vector3f parsePosition(JsonObject p_178251_1_, String p_178251_2_)
            {
                JsonArray jsonarray = JsonUtils.getJsonObjectJsonArrayField(p_178251_1_, p_178251_2_);

                if (jsonarray.size() != 3)
                {
                    throw new JsonParseException("Expected 3 " + p_178251_2_ + " values, found: " + jsonarray.size());
                }
                else
                {
                    float[] afloat = new float[3];

                    for (int i = 0; i < afloat.length; ++i)
                    {
                        afloat[i] = JsonUtils.getJsonElementFloatValue(jsonarray.get(i), p_178251_2_ + "[" + i + "]");
                    }

                    return new Vector3f(afloat);
                }
            }

            public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.parseBlockPart(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
        }

    @SideOnly(Side.CLIENT)

    static final class SwitchEnumFacing
        {
            static final int[] field_178234_a = new int[EnumFacing.values().length];
            private static final String __OBFID = "CL_00002510";

            static
            {
                try
                {
                    field_178234_a[EnumFacing.DOWN.ordinal()] = 1;
                }
                catch (NoSuchFieldError var6)
                {
                    ;
                }

                try
                {
                    field_178234_a[EnumFacing.UP.ordinal()] = 2;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                try
                {
                    field_178234_a[EnumFacing.NORTH.ordinal()] = 3;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    field_178234_a[EnumFacing.SOUTH.ordinal()] = 4;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_178234_a[EnumFacing.WEST.ordinal()] = 5;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_178234_a[EnumFacing.EAST.ordinal()] = 6;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}