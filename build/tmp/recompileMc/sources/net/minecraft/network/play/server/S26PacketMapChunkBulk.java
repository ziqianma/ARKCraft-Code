package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S26PacketMapChunkBulk implements Packet
{
    private int[] field_149266_a;
    private int[] field_149264_b;
    private S21PacketChunkData.Extracted[] field_179755_c;
    private boolean field_149267_h;
    private static final String __OBFID = "CL_00001306";

    public S26PacketMapChunkBulk() {}

    public S26PacketMapChunkBulk(List chunks)
    {
        int i = chunks.size();
        this.field_149266_a = new int[i];
        this.field_149264_b = new int[i];
        this.field_179755_c = new S21PacketChunkData.Extracted[i];
        this.field_149267_h = !((Chunk)chunks.get(0)).getWorld().provider.getHasNoSky();

        for (int j = 0; j < i; ++j)
        {
            Chunk chunk = (Chunk)chunks.get(j);
            S21PacketChunkData.Extracted extracted = S21PacketChunkData.func_179756_a(chunk, true, this.field_149267_h, 65535);
            this.field_149266_a[j] = chunk.xPosition;
            this.field_149264_b[j] = chunk.zPosition;
            this.field_179755_c[j] = extracted;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149267_h = buf.readBoolean();
        int i = buf.readVarIntFromBuffer();
        this.field_149266_a = new int[i];
        this.field_149264_b = new int[i];
        this.field_179755_c = new S21PacketChunkData.Extracted[i];
        int j;

        for (j = 0; j < i; ++j)
        {
            this.field_149266_a[j] = buf.readInt();
            this.field_149264_b[j] = buf.readInt();
            this.field_179755_c[j] = new S21PacketChunkData.Extracted();
            this.field_179755_c[j].dataSize = buf.readShort() & 65535;
            this.field_179755_c[j].data = new byte[S21PacketChunkData.func_180737_a(Integer.bitCount(this.field_179755_c[j].dataSize), this.field_149267_h, true)];
        }

        for (j = 0; j < i; ++j)
        {
            buf.readBytes(this.field_179755_c[j].data);
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBoolean(this.field_149267_h);
        buf.writeVarIntToBuffer(this.field_179755_c.length);
        int i;

        for (i = 0; i < this.field_149266_a.length; ++i)
        {
            buf.writeInt(this.field_149266_a[i]);
            buf.writeInt(this.field_149264_b[i]);
            buf.writeShort((short)(this.field_179755_c[i].dataSize & 65535));
        }

        for (i = 0; i < this.field_149266_a.length; ++i)
        {
            buf.writeBytes(this.field_179755_c[i].data);
        }
    }

    public void func_180738_a(INetHandlerPlayClient p_180738_1_)
    {
        p_180738_1_.handleMapChunkBulk(this);
    }

    @SideOnly(Side.CLIENT)
    public int func_149255_a(int p_149255_1_)
    {
        return this.field_149266_a[p_149255_1_];
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180738_a((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int func_149253_b(int p_149253_1_)
    {
        return this.field_149264_b[p_149253_1_];
    }

    @SideOnly(Side.CLIENT)
    public int func_149254_d()
    {
        return this.field_149266_a.length;
    }

    @SideOnly(Side.CLIENT)
    public byte[] func_149256_c(int p_149256_1_)
    {
        return this.field_179755_c[p_149256_1_].data;
    }

    @SideOnly(Side.CLIENT)
    public int func_179754_d(int p_179754_1_)
    {
        return this.field_179755_c[p_179754_1_].dataSize;
    }
}