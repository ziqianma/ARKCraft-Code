package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S21PacketChunkData implements Packet
{
    private int chunkX;
    private int chunkZ;
    private S21PacketChunkData.Extracted field_179758_c;
    private boolean field_149279_g;
    private static final String __OBFID = "CL_00001304";

    public S21PacketChunkData() {}

    public S21PacketChunkData(Chunk p_i45196_1_, boolean p_i45196_2_, int p_i45196_3_)
    {
        this.chunkX = p_i45196_1_.xPosition;
        this.chunkZ = p_i45196_1_.zPosition;
        this.field_149279_g = p_i45196_2_;
        this.field_179758_c = func_179756_a(p_i45196_1_, p_i45196_2_, !p_i45196_1_.getWorld().provider.getHasNoSky(), p_i45196_3_);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();
        this.field_149279_g = buf.readBoolean();
        this.field_179758_c = new S21PacketChunkData.Extracted();
        this.field_179758_c.dataSize = buf.readShort();
        this.field_179758_c.data = buf.readByteArray();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);
        buf.writeBoolean(this.field_149279_g);
        buf.writeShort((short)(this.field_179758_c.dataSize & 65535));
        buf.writeByteArray(this.field_179758_c.data);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleChunkData(this);
    }

    @SideOnly(Side.CLIENT)
    public byte[] func_149272_d()
    {
        return this.field_179758_c.data;
    }

    protected static int func_180737_a(int p_180737_0_, boolean p_180737_1_, boolean p_180737_2_)
    {
        int j = p_180737_0_ * 2 * 16 * 16 * 16;
        int k = p_180737_0_ * 16 * 16 * 16 / 2;
        int l = p_180737_1_ ? p_180737_0_ * 16 * 16 * 16 / 2 : 0;
        int i1 = p_180737_2_ ? 256 : 0;
        return j + k + l + i1;
    }

    public static S21PacketChunkData.Extracted func_179756_a(Chunk p_179756_0_, boolean p_179756_1_, boolean p_179756_2_, int p_179756_3_)
    {
        ExtendedBlockStorage[] aextendedblockstorage = p_179756_0_.getBlockStorageArray();
        S21PacketChunkData.Extracted extracted = new S21PacketChunkData.Extracted();
        ArrayList arraylist = Lists.newArrayList();
        int j;

        for (j = 0; j < aextendedblockstorage.length; ++j)
        {
            ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[j];

            if (extendedblockstorage != null && (!p_179756_1_ || !extendedblockstorage.isEmpty()) && (p_179756_3_ & 1 << j) != 0)
            {
                extracted.dataSize |= 1 << j;
                arraylist.add(extendedblockstorage);
            }
        }

        extracted.data = new byte[func_180737_a(Integer.bitCount(extracted.dataSize), p_179756_2_, p_179756_1_)];
        j = 0;
        Iterator iterator = arraylist.iterator();
        ExtendedBlockStorage extendedblockstorage1;

        while (iterator.hasNext())
        {
            extendedblockstorage1 = (ExtendedBlockStorage)iterator.next();
            char[] achar = extendedblockstorage1.getData();
            char[] achar1 = achar;
            int k = achar.length;

            for (int l = 0; l < k; ++l)
            {
                char c0 = achar1[l];
                extracted.data[j++] = (byte)(c0 & 255);
                extracted.data[j++] = (byte)(c0 >> 8 & 255);
            }
        }

        for (iterator = arraylist.iterator(); iterator.hasNext(); j = func_179757_a(extendedblockstorage1.getBlocklightArray().getData(), extracted.data, j))
        {
            extendedblockstorage1 = (ExtendedBlockStorage)iterator.next();
        }

        if (p_179756_2_)
        {
            for (iterator = arraylist.iterator(); iterator.hasNext(); j = func_179757_a(extendedblockstorage1.getSkylightArray().getData(), extracted.data, j))
            {
                extendedblockstorage1 = (ExtendedBlockStorage)iterator.next();
            }
        }

        if (p_179756_1_)
        {
            func_179757_a(p_179756_0_.getBiomeArray(), extracted.data, j);
        }

        return extracted;
    }

    private static int func_179757_a(byte[] p_179757_0_, byte[] p_179757_1_, int p_179757_2_)
    {
        System.arraycopy(p_179757_0_, 0, p_179757_1_, p_179757_2_, p_179757_0_.length);
        return p_179757_2_ + p_179757_0_.length;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int func_149273_e()
    {
        return this.chunkX;
    }

    @SideOnly(Side.CLIENT)
    public int func_149271_f()
    {
        return this.chunkZ;
    }

    @SideOnly(Side.CLIENT)
    public int func_149276_g()
    {
        return this.field_179758_c.dataSize;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_149274_i()
    {
        return this.field_149279_g;
    }

    public static class Extracted
        {
            public byte[] data;
            public int dataSize;
            private static final String __OBFID = "CL_00001305";
        }
}