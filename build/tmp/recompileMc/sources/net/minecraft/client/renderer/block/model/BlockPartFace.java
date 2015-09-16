package net.minecraft.client.renderer.block.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.lang.reflect.Type;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockPartFace
{
    public static final EnumFacing FACING_DEFAULT = null;
    public final EnumFacing cullFace;
    public final int tintIndex;
    public final String texture;
    public final BlockFaceUV blockFaceUV;
    private static final String __OBFID = "CL_00002508";

    public BlockPartFace(EnumFacing p_i46230_1_, int p_i46230_2_, String p_i46230_3_, BlockFaceUV p_i46230_4_)
    {
        this.cullFace = p_i46230_1_;
        this.tintIndex = p_i46230_2_;
        this.texture = p_i46230_3_;
        this.blockFaceUV = p_i46230_4_;
    }

    @SideOnly(Side.CLIENT)
    static class Deserializer implements JsonDeserializer
        {
            private static final String __OBFID = "CL_00002507";

            public BlockPartFace parseBlockPartFace(JsonElement p_178338_1_, Type p_178338_2_, JsonDeserializationContext p_178338_3_)
            {
                JsonObject jsonobject = p_178338_1_.getAsJsonObject();
                EnumFacing enumfacing = this.parseCullFace(jsonobject);
                int i = this.parseTintIndex(jsonobject);
                String s = this.parseTexture(jsonobject);
                BlockFaceUV blockfaceuv = (BlockFaceUV)p_178338_3_.deserialize(jsonobject, BlockFaceUV.class);
                return new BlockPartFace(enumfacing, i, s, blockfaceuv);
            }

            protected int parseTintIndex(JsonObject p_178337_1_)
            {
                return JsonUtils.getJsonObjectIntegerFieldValueOrDefault(p_178337_1_, "tintindex", -1);
            }

            private String parseTexture(JsonObject p_178340_1_)
            {
                return JsonUtils.getJsonObjectStringFieldValue(p_178340_1_, "texture");
            }

            private EnumFacing parseCullFace(JsonObject p_178339_1_)
            {
                String s = JsonUtils.getJsonObjectStringFieldValueOrDefault(p_178339_1_, "cullface", "");
                return EnumFacing.byName(s);
            }

            public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.parseBlockPartFace(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
        }
}