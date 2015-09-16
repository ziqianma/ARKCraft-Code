package net.minecraft.client.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.util.QuadComparator;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;

@SideOnly(Side.CLIENT)
public class WorldRenderer
{
    private ByteBuffer byteBuffer;
    private IntBuffer rawIntBuffer;
    private FloatBuffer rawFloatBuffer;
    private int vertexCount;
    private double textureU;
    private double textureV;
    private int brightness;
    private int color;
    private int rawBufferIndex;
    /** Boolean for whether this renderer needs to be updated or not */
    private boolean needsUpdate;
    private int drawMode;
    private double xOffset;
    private double yOffset;
    private double zOffset;
    private int normal;
    private int byteIndex;
    private VertexFormat vertexFormat;
    private boolean isDrawing;
    private int bufferSize;
    private static final String __OBFID = "CL_00000942";

    public WorldRenderer(int bufferSizeIn)
    {
        this.bufferSize = bufferSizeIn;
        this.byteBuffer = GLAllocation.createDirectByteBuffer(bufferSizeIn * 4);
        this.rawIntBuffer = this.byteBuffer.asIntBuffer();
        this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
        this.vertexFormat = new VertexFormat();
        this.vertexFormat.setElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
    }

    private void growBuffer(int p_178983_1_)
    {
        LogManager.getLogger().warn("Needed to grow BufferBuilder buffer: Old size " + this.bufferSize * 4 + " bytes, new size " + (this.bufferSize * 4 + p_178983_1_) + " bytes.");
        this.bufferSize += p_178983_1_ / 4;
        ByteBuffer bytebuffer = GLAllocation.createDirectByteBuffer(this.bufferSize * 4);
        this.rawIntBuffer.position(0);
        bytebuffer.asIntBuffer().put(this.rawIntBuffer);
        this.byteBuffer = bytebuffer;
        this.rawIntBuffer = this.byteBuffer.asIntBuffer();
        this.rawFloatBuffer = this.byteBuffer.asFloatBuffer();
    }

    public WorldRenderer.State getVertexState(float p_178971_1_, float p_178971_2_, float p_178971_3_)
    {
        int[] aint = new int[this.rawBufferIndex];
        PriorityQueue priorityqueue = new PriorityQueue(this.rawBufferIndex, new QuadComparator(this.rawFloatBuffer, (float)((double)p_178971_1_ + this.xOffset), (float)((double)p_178971_2_ + this.yOffset), (float)((double)p_178971_3_ + this.zOffset), this.vertexFormat.getNextOffset() / 4));
        int i = this.vertexFormat.getNextOffset();
        int j;

        for (j = 0; j < this.rawBufferIndex; j += i)
        {
            priorityqueue.add(Integer.valueOf(j));
        }

        for (j = 0; !priorityqueue.isEmpty(); j += i)
        {
            int k = ((Integer)priorityqueue.remove()).intValue();

            for (int l = 0; l < i; ++l)
            {
                aint[j + l] = this.rawIntBuffer.get(k + l);
            }
        }

        this.rawIntBuffer.clear();
        this.rawIntBuffer.put(aint);
        return new WorldRenderer.State(aint, this.rawBufferIndex, this.vertexCount, new VertexFormat(this.vertexFormat));
    }

    public void setVertexState(WorldRenderer.State p_178993_1_)
    {
        this.rawIntBuffer.clear();
        this.rawIntBuffer.put(p_178993_1_.getRawBuffer());
        this.rawBufferIndex = p_178993_1_.getRawBufferIndex();
        this.vertexCount = p_178993_1_.getVertexCount();
        this.vertexFormat = new VertexFormat(p_178993_1_.getVertexFormat());
    }

    public void reset()
    {
        this.vertexCount = 0;
        this.rawBufferIndex = 0;
        this.vertexFormat.clear();
        this.vertexFormat.setElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
    }

    public void startDrawingQuads()
    {
        this.startDrawing(7);
    }

    public void startDrawing(int p_178964_1_)
    {
        if (this.isDrawing)
        {
            throw new IllegalStateException("Already building!");
        }
        else
        {
            this.isDrawing = true;
            this.reset();
            this.drawMode = p_178964_1_;
            this.needsUpdate = false;
        }
    }

    public void setTextureUV(double p_178992_1_, double p_178992_3_)
    {
        if (!this.vertexFormat.hasElementOffset(0) && !this.vertexFormat.hasElementOffset(1))
        {
            VertexFormatElement vertexformatelement = new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2);
            this.vertexFormat.setElement(vertexformatelement);
        }

        this.textureU = p_178992_1_;
        this.textureV = p_178992_3_;
    }

    public void setBrightness(int p_178963_1_)
    {
        if (!this.vertexFormat.hasElementOffset(1))
        {
            if (!this.vertexFormat.hasElementOffset(0))
            {
                this.vertexFormat.setElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.UV, 2));
            }

            VertexFormatElement vertexformatelement = new VertexFormatElement(1, VertexFormatElement.EnumType.SHORT, VertexFormatElement.EnumUsage.UV, 2);
            this.vertexFormat.setElement(vertexformatelement);
        }

        this.brightness = p_178963_1_;
    }

    public void setColorOpaque_F(float p_178986_1_, float p_178986_2_, float p_178986_3_)
    {
        this.setColorOpaque((int)(p_178986_1_ * 255.0F), (int)(p_178986_2_ * 255.0F), (int)(p_178986_3_ * 255.0F));
    }

    public void setColorRGBA_F(float p_178960_1_, float p_178960_2_, float p_178960_3_, float p_178960_4_)
    {
        this.setColorRGBA((int)(p_178960_1_ * 255.0F), (int)(p_178960_2_ * 255.0F), (int)(p_178960_3_ * 255.0F), (int)(p_178960_4_ * 255.0F));
    }

    /**
     * Sets a new position for the renderer and setting it up so it can be reloaded with the new data for that position
     */
    public void setColorOpaque(int p_78913_1_, int p_78913_2_, int p_78913_3_)
    {
        this.setColorRGBA(p_78913_1_, p_78913_2_, p_78913_3_, 255);
    }

    public void putBrightness4(int p_178962_1_, int p_178962_2_, int p_178962_3_, int p_178962_4_)
    {
        int i1 = (this.vertexCount - 4) * (this.vertexFormat.getNextOffset() / 4) + this.vertexFormat.getElementOffsetById(1) / 4;
        int j1 = this.vertexFormat.getNextOffset() >> 2;
        this.rawIntBuffer.put(i1, p_178962_1_);
        this.rawIntBuffer.put(i1 + j1, p_178962_2_);
        this.rawIntBuffer.put(i1 + j1 * 2, p_178962_3_);
        this.rawIntBuffer.put(i1 + j1 * 3, p_178962_4_);
    }

    public void putPosition(double p_178987_1_, double p_178987_3_, double p_178987_5_)
    {
        if (this.rawBufferIndex >= this.bufferSize - this.vertexFormat.getNextOffset())
        {
            this.growBuffer(2097152);
        }

        int i = this.vertexFormat.getNextOffset() / 4;
        int j = (this.vertexCount - 4) * i;

        for (int k = 0; k < 4; ++k)
        {
            int l = j + k * i;
            int i1 = l + 1;
            int j1 = i1 + 1;
            this.rawIntBuffer.put(l, Float.floatToRawIntBits((float)(p_178987_1_ + this.xOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(l))));
            this.rawIntBuffer.put(i1, Float.floatToRawIntBits((float)(p_178987_3_ + this.yOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(i1))));
            this.rawIntBuffer.put(j1, Float.floatToRawIntBits((float)(p_178987_5_ + this.zOffset) + Float.intBitsToFloat(this.rawIntBuffer.get(j1))));
        }
    }

    /**
     * Takes in the pass the call list is being requested for. Args: renderPass
     */
    public int getColorIndex(int p_78909_1_)
    {
        return ((this.vertexCount - p_78909_1_) * this.vertexFormat.getNextOffset() + this.vertexFormat.getColorOffset()) / 4;
    }

    public void putColorMultiplier(float p_178978_1_, float p_178978_2_, float p_178978_3_, int p_178978_4_)
    {
        int j = this.getColorIndex(p_178978_4_);
        int k = this.rawIntBuffer.get(j);
        int l;
        int i1;
        int j1;

        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
        {
            l = (int)((float)(k & 255) * p_178978_1_);
            i1 = (int)((float)(k >> 8 & 255) * p_178978_2_);
            j1 = (int)((float)(k >> 16 & 255) * p_178978_3_);
            k &= -16777216;
            k |= j1 << 16 | i1 << 8 | l;
        }
        else
        {
            l = (int)((float)(this.color >> 24 & 255) * p_178978_1_);
            i1 = (int)((float)(this.color >> 16 & 255) * p_178978_2_);
            j1 = (int)((float)(this.color >> 8 & 255) * p_178978_3_);
            k &= 255;
            k |= l << 24 | i1 << 16 | j1 << 8;
        }

        if (this.needsUpdate)
        {
            k = -1;
        }

        this.rawIntBuffer.put(j, k);
    }

    private void putColor(int p_178988_1_, int p_178988_2_)
    {
        int k = this.getColorIndex(p_178988_2_);
        int l = p_178988_1_ >> 16 & 255;
        int i1 = p_178988_1_ >> 8 & 255;
        int j1 = p_178988_1_ & 255;
        int k1 = p_178988_1_ >> 24 & 255;
        this.putColorRGBA(k, l, i1, j1, k1);
    }

    public void putColorRGB_F(float p_178994_1_, float p_178994_2_, float p_178994_3_, int p_178994_4_)
    {
        int j = this.getColorIndex(p_178994_4_);
        int k = MathHelper.clamp_int((int)(p_178994_1_ * 255.0F), 0, 255);
        int l = MathHelper.clamp_int((int)(p_178994_2_ * 255.0F), 0, 255);
        int i1 = MathHelper.clamp_int((int)(p_178994_3_ * 255.0F), 0, 255);
        this.putColorRGBA(j, k, l, i1, 255);
    }

    public void putColorRGBA(int p_178972_1_, int p_178972_2_, int p_178972_3_, int p_178972_4_, int p_178972_5_)
    {
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
        {
            this.rawIntBuffer.put(p_178972_1_, p_178972_5_ << 24 | p_178972_4_ << 16 | p_178972_3_ << 8 | p_178972_2_);
        }
        else
        {
            this.rawIntBuffer.put(p_178972_1_, p_178972_2_ << 24 | p_178972_3_ << 16 | p_178972_4_ << 8 | p_178972_5_);
        }
    }

    public void setColorRGBA(int p_178961_1_, int p_178961_2_, int p_178961_3_, int p_178961_4_)
    {
        if (!this.needsUpdate)
        {
            if (p_178961_1_ > 255)
            {
                p_178961_1_ = 255;
            }

            if (p_178961_2_ > 255)
            {
                p_178961_2_ = 255;
            }

            if (p_178961_3_ > 255)
            {
                p_178961_3_ = 255;
            }

            if (p_178961_4_ > 255)
            {
                p_178961_4_ = 255;
            }

            if (p_178961_1_ < 0)
            {
                p_178961_1_ = 0;
            }

            if (p_178961_2_ < 0)
            {
                p_178961_2_ = 0;
            }

            if (p_178961_3_ < 0)
            {
                p_178961_3_ = 0;
            }

            if (p_178961_4_ < 0)
            {
                p_178961_4_ = 0;
            }

            if (!this.vertexFormat.hasColor())
            {
                VertexFormatElement vertexformatelement = new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4);
                this.vertexFormat.setElement(vertexformatelement);
            }

            if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)
            {
                this.color = p_178961_4_ << 24 | p_178961_3_ << 16 | p_178961_2_ << 8 | p_178961_1_;
            }
            else
            {
                this.color = p_178961_1_ << 24 | p_178961_2_ << 16 | p_178961_3_ << 8 | p_178961_4_;
            }
        }
    }

    public void setColorOpaque_B(byte p_178982_1_, byte p_178982_2_, byte p_178982_3_)
    {
        this.setColorOpaque(p_178982_1_ & 255, p_178982_2_ & 255, p_178982_3_ & 255);
    }

    public void addVertexWithUV(double p_178985_1_, double p_178985_3_, double p_178985_5_, double p_178985_7_, double p_178985_9_)
    {
        this.setTextureUV(p_178985_7_, p_178985_9_);
        this.addVertex(p_178985_1_, p_178985_3_, p_178985_5_);
    }

    public void setVertexFormat(VertexFormat p_178967_1_)
    {
        this.vertexFormat = new VertexFormat(p_178967_1_);
    }

    public void addVertexData(int[] p_178981_1_)
    {
        int i = this.vertexFormat.getNextOffset() / 4;
        this.vertexCount += p_178981_1_.length / i;
        this.rawIntBuffer.position(this.rawBufferIndex);
        this.rawIntBuffer.put(p_178981_1_);
        this.rawBufferIndex += p_178981_1_.length;
    }

    public void addVertex(double p_178984_1_, double p_178984_3_, double p_178984_5_)
    {
        if (this.rawBufferIndex >= this.bufferSize - this.vertexFormat.getNextOffset())
        {
            this.growBuffer(2097152);
        }

        List list = this.vertexFormat.getElements();
        Iterator iterator = list.iterator();

        while (iterator.hasNext())
        {
            VertexFormatElement vertexformatelement = (VertexFormatElement)iterator.next();
            int i = vertexformatelement.getOffset() >> 2;
            int j = this.rawBufferIndex + i;

            switch (WorldRenderer.SwitchEnumUsage.VALUES[vertexformatelement.getUsage().ordinal()])
            {
                case 1:
                    this.rawIntBuffer.put(j, Float.floatToRawIntBits((float)(p_178984_1_ + this.xOffset)));
                    this.rawIntBuffer.put(j + 1, Float.floatToRawIntBits((float)(p_178984_3_ + this.yOffset)));
                    this.rawIntBuffer.put(j + 2, Float.floatToRawIntBits((float)(p_178984_5_ + this.zOffset)));
                    break;
                case 2:
                    this.rawIntBuffer.put(j, this.color);
                    break;
                case 3:
                    if (vertexformatelement.getIndex() == 0)
                    {
                        this.rawIntBuffer.put(j, Float.floatToRawIntBits((float)this.textureU));
                        this.rawIntBuffer.put(j + 1, Float.floatToRawIntBits((float)this.textureV));
                    }
                    else
                    {
                        this.rawIntBuffer.put(j, this.brightness);
                    }

                    break;
                case 4:
                    this.rawIntBuffer.put(j, this.normal);
            }
        }

        this.rawBufferIndex += this.vertexFormat.getNextOffset() >> 2;
        ++this.vertexCount;
    }

    public void setColorOpaque_I(int p_178991_1_)
    {
        int j = p_178991_1_ >> 16 & 255;
        int k = p_178991_1_ >> 8 & 255;
        int l = p_178991_1_ & 255;
        this.setColorOpaque(j, k, l);
    }

    public void setColorRGBA_I(int p_178974_1_, int p_178974_2_)
    {
        int k = p_178974_1_ >> 16 & 255;
        int l = p_178974_1_ >> 8 & 255;
        int i1 = p_178974_1_ & 255;
        this.setColorRGBA(k, l, i1, p_178974_2_);
    }

    /**
     * Marks the current renderer data as dirty and needing to be updated.
     */
    public void markDirty()
    {
        this.needsUpdate = true;
    }

    public void setNormal(float p_178980_1_, float p_178980_2_, float p_178980_3_)
    {
        if (!this.vertexFormat.hasNormal())
        {
            VertexFormatElement vertexformatelement = new VertexFormatElement(0, VertexFormatElement.EnumType.BYTE, VertexFormatElement.EnumUsage.NORMAL, 3);
            this.vertexFormat.setElement(vertexformatelement);
            this.vertexFormat.setElement(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.PADDING, 1));
        }

        byte b2 = (byte)((int)(p_178980_1_ * 127.0F));
        byte b0 = (byte)((int)(p_178980_2_ * 127.0F));
        byte b1 = (byte)((int)(p_178980_3_ * 127.0F));
        this.normal = b2 & 255 | (b0 & 255) << 8 | (b1 & 255) << 16;
    }

    public void putNormal(float p_178975_1_, float p_178975_2_, float p_178975_3_)
    {
        byte b0 = (byte)((int)(p_178975_1_ * 127.0F));
        byte b1 = (byte)((int)(p_178975_2_ * 127.0F));
        byte b2 = (byte)((int)(p_178975_3_ * 127.0F));
        int i = this.vertexFormat.getNextOffset() >> 2;
        int j = (this.vertexCount - 4) * i + this.vertexFormat.getNormalOffset() / 4;
        this.normal = b0 & 255 | (b1 & 255) << 8 | (b2 & 255) << 16;
        this.rawIntBuffer.put(j, this.normal);
        this.rawIntBuffer.put(j + i, this.normal);
        this.rawIntBuffer.put(j + i * 2, this.normal);
        this.rawIntBuffer.put(j + i * 3, this.normal);
    }

    public void setTranslation(double p_178969_1_, double p_178969_3_, double p_178969_5_)
    {
        this.xOffset = p_178969_1_;
        this.yOffset = p_178969_3_;
        this.zOffset = p_178969_5_;
    }

    public int finishDrawing()
    {
        if (!this.isDrawing)
        {
            throw new IllegalStateException("Not building!");
        }
        else
        {
            this.isDrawing = false;

            if (this.vertexCount > 0)
            {
                this.byteBuffer.position(0);
                this.byteBuffer.limit(this.rawBufferIndex * 4);
            }

            this.byteIndex = this.rawBufferIndex * 4;
            return this.byteIndex;
        }
    }

    public int getByteIndex()
    {
        return this.byteIndex;
    }

    public ByteBuffer getByteBuffer()
    {
        return this.byteBuffer;
    }

    public VertexFormat getVertexFormat()
    {
        return this.vertexFormat;
    }

    public int getVertexCount()
    {
        return this.vertexCount;
    }

    public int getDrawMode()
    {
        return this.drawMode;
    }

    public void putColor4(int p_178968_1_)
    {
        for (int j = 0; j < 4; ++j)
        {
            this.putColor(p_178968_1_, j + 1);
        }
    }

    public void putColorRGB_F4(float p_178990_1_, float p_178990_2_, float p_178990_3_)
    {
        for (int i = 0; i < 4; ++i)
        {
            this.putColorRGB_F(p_178990_1_, p_178990_2_, p_178990_3_, i + 1);
        }
    }

    @SideOnly(Side.CLIENT)
    public class State
    {
        private final int[] stateRawBuffer;
        private final int stateRawBufferIndex;
        private final int stateVertexCount;
        private final VertexFormat stateVertexFormat;
        private static final String __OBFID = "CL_00002568";

        public State(int[] p_i46274_2_, int p_i46274_3_, int p_i46274_4_, VertexFormat p_i46274_5_)
        {
            this.stateRawBuffer = p_i46274_2_;
            this.stateRawBufferIndex = p_i46274_3_;
            this.stateVertexCount = p_i46274_4_;
            this.stateVertexFormat = p_i46274_5_;
        }

        public int[] getRawBuffer()
        {
            return this.stateRawBuffer;
        }

        public int getRawBufferIndex()
        {
            return this.stateRawBufferIndex;
        }

        public int getVertexCount()
        {
            return this.stateVertexCount;
        }

        public VertexFormat getVertexFormat()
        {
            return this.stateVertexFormat;
        }
    }

    @SideOnly(Side.CLIENT)

    static final class SwitchEnumUsage
        {
            static final int[] VALUES = new int[VertexFormatElement.EnumUsage.values().length];
            private static final String __OBFID = "CL_00002569";

            static
            {
                try
                {
                    VALUES[VertexFormatElement.EnumUsage.POSITION.ordinal()] = 1;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    VALUES[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 2;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    VALUES[VertexFormatElement.EnumUsage.UV.ordinal()] = 3;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    VALUES[VertexFormatElement.EnumUsage.NORMAL.ordinal()] = 4;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}