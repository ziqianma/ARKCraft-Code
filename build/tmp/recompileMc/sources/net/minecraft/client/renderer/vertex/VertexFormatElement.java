package net.minecraft.client.renderer.vertex;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class VertexFormatElement
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final VertexFormatElement.EnumType type;
    private final VertexFormatElement.EnumUsage usage;
    private int index;
    private int elementCount;
    private int offset;
    private static final String __OBFID = "CL_00002399";

    public VertexFormatElement(int p_i46096_1_, VertexFormatElement.EnumType p_i46096_2_, VertexFormatElement.EnumUsage p_i46096_3_, int p_i46096_4_)
    {
        if (!this.func_177372_a(p_i46096_1_, p_i46096_3_))
        {
            LOGGER.warn("Multiple vertex elements of the same type other than UVs are not supported. Forcing type to UV.");
            this.usage = VertexFormatElement.EnumUsage.UV;
        }
        else
        {
            this.usage = p_i46096_3_;
        }

        this.type = p_i46096_2_;
        this.index = p_i46096_1_;
        this.elementCount = p_i46096_4_;
        this.offset = 0;
    }

    public void setOffset(int p_177371_1_)
    {
        this.offset = p_177371_1_;
    }

    public int getOffset()
    {
        return this.offset;
    }

    private final boolean func_177372_a(int p_177372_1_, VertexFormatElement.EnumUsage p_177372_2_)
    {
        return p_177372_1_ == 0 || p_177372_2_ == VertexFormatElement.EnumUsage.UV;
    }

    public final VertexFormatElement.EnumType getType()
    {
        return this.type;
    }

    public final VertexFormatElement.EnumUsage getUsage()
    {
        return this.usage;
    }

    public final int getElementCount()
    {
        return this.elementCount;
    }

    public final int getIndex()
    {
        return this.index;
    }

    public String toString()
    {
        return this.elementCount + "," + this.usage.getDisplayName() + "," + this.type.getDisplayName();
    }

    public final int getSize()
    {
        return this.type.getSize() * this.elementCount;
    }

    public final boolean isPositionElement()
    {
        return this.usage == VertexFormatElement.EnumUsage.POSITION;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass())
        {
            VertexFormatElement vertexformatelement = (VertexFormatElement)p_equals_1_;
            return this.elementCount != vertexformatelement.elementCount ? false : (this.index != vertexformatelement.index ? false : (this.offset != vertexformatelement.offset ? false : (this.type != vertexformatelement.type ? false : this.usage == vertexformatelement.usage)));
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        int i = this.type.hashCode();
        i = 31 * i + this.usage.hashCode();
        i = 31 * i + this.index;
        i = 31 * i + this.elementCount;
        i = 31 * i + this.offset;
        return i;
    }

    @SideOnly(Side.CLIENT)
    public static enum EnumType
    {
        FLOAT(4, "Float", org.lwjgl.opengl.GL11.GL_FLOAT),
        UBYTE(1, "Unsigned Byte", org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE),
        BYTE(1, "Byte", org.lwjgl.opengl.GL11.GL_BYTE),
        USHORT(2, "Unsigned Short", org.lwjgl.opengl.GL11.GL_UNSIGNED_SHORT),
        SHORT(2, "Short", org.lwjgl.opengl.GL11.GL_SHORT),
        UINT(4, "Unsigned Int", org.lwjgl.opengl.GL11.GL_UNSIGNED_INT),
        INT(4, "Int", org.lwjgl.opengl.GL11.GL_INT);
        // Commented for now, might be added in the future if anyone needs them
        //HALF_FLOAT(2, "Half Float", org.lwjgl.opengl.GL30.GL_HALF_FLOAT),
        //DOUBLE(8, "Double", org.lwjgl.opengl.GL11.GL_DOUBLE),
        //INT_2_10_10_10_REV(4, "Int 2-10-10-10 reversed", org.lwjgl.opengl.GL33.GL_INT_2_10_10_10_REV),
        //UINT_2_10_10_10_REV(4, "Unsigned Int 2-10-10-10 reversed", org.lwjgl.opengl.GL12.GL_UNSIGNED_INT_2_10_10_10_REV),
        //UINT_10F_11F_11F_REV(4, "Unsigned Int 10F 11F 11F reversed", GL_UNSIGNED_INT_10F_11F_11F_REV);

        private final int size;
        private final String displayName;
        private final int glConstant;

        private static final String __OBFID = "CL_00002398";

        private EnumType(int p_i46095_3_, String p_i46095_4_, int p_i46095_5_)
        {
            this.size = p_i46095_3_;
            this.displayName = p_i46095_4_;
            this.glConstant = p_i46095_5_;
        }

        public int getSize()
        {
            return this.size;
        }

        public String getDisplayName()
        {
            return this.displayName;
        }

        public int getGlConstant()
        {
            return this.glConstant;
        }
    }

    @SideOnly(Side.CLIENT)
    public static enum EnumUsage
    {
        POSITION("Position"),
        NORMAL("Normal"),
        COLOR("Vertex Color"),
        UV("UV"),
        // As of 1.8 - unused in vanilla; use GENERIC for now
        @Deprecated
        MATRIX("Bone Matrix"),
        @Deprecated
        BLEND_WEIGHT("Blend Weight"),
        PADDING("Padding"),
        GENERIC("Generic Attribute");

        public void preDraw(VertexFormatElement element, int stride, java.nio.ByteBuffer buffer) { net.minecraftforge.client.ForgeHooksClient.preDraw(this, element, stride, buffer); }
        public void postDraw(VertexFormatElement element, int stride, java.nio.ByteBuffer buffer) { net.minecraftforge.client.ForgeHooksClient.postDraw(this, element, stride, buffer); }

        private final String displayName;

        private static final String __OBFID = "CL_00002397";

        private EnumUsage(String p_i46094_3_)
        {
            this.displayName = p_i46094_3_;
        }

        public String getDisplayName()
        {
            return this.displayName;
        }
    }
}