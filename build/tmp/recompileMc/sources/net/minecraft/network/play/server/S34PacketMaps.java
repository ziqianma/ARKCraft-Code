package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.Collection;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S34PacketMaps implements Packet
{
    private int mapId;
    private byte field_179739_b;
    private Vec4b[] field_179740_c;
    private int field_179737_d;
    private int field_179738_e;
    private int field_179735_f;
    private int field_179736_g;
    private byte[] field_179741_h;
    private static final String __OBFID = "CL_00001311";

    public S34PacketMaps() {}

    public S34PacketMaps(int p_i45975_1_, byte p_i45975_2_, Collection p_i45975_3_, byte[] p_i45975_4_, int p_i45975_5_, int p_i45975_6_, int p_i45975_7_, int p_i45975_8_)
    {
        this.mapId = p_i45975_1_;
        this.field_179739_b = p_i45975_2_;
        this.field_179740_c = (Vec4b[])p_i45975_3_.toArray(new Vec4b[p_i45975_3_.size()]);
        this.field_179737_d = p_i45975_5_;
        this.field_179738_e = p_i45975_6_;
        this.field_179735_f = p_i45975_7_;
        this.field_179736_g = p_i45975_8_;
        this.field_179741_h = new byte[p_i45975_7_ * p_i45975_8_];

        for (int j1 = 0; j1 < p_i45975_7_; ++j1)
        {
            for (int k1 = 0; k1 < p_i45975_8_; ++k1)
            {
                this.field_179741_h[j1 + k1 * p_i45975_7_] = p_i45975_4_[p_i45975_5_ + j1 + (p_i45975_6_ + k1) * 128];
            }
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.mapId = buf.readVarIntFromBuffer();
        this.field_179739_b = buf.readByte();
        this.field_179740_c = new Vec4b[buf.readVarIntFromBuffer()];

        for (int i = 0; i < this.field_179740_c.length; ++i)
        {
            short short1 = (short)buf.readByte();
            this.field_179740_c[i] = new Vec4b((byte)(short1 >> 4 & 15), buf.readByte(), buf.readByte(), (byte)(short1 & 15));
        }

        this.field_179735_f = buf.readUnsignedByte();

        if (this.field_179735_f > 0)
        {
            this.field_179736_g = buf.readUnsignedByte();
            this.field_179737_d = buf.readUnsignedByte();
            this.field_179738_e = buf.readUnsignedByte();
            this.field_179741_h = buf.readByteArray();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.mapId);
        buf.writeByte(this.field_179739_b);
        buf.writeVarIntToBuffer(this.field_179740_c.length);
        Vec4b[] avec4b = this.field_179740_c;
        int i = avec4b.length;

        for (int j = 0; j < i; ++j)
        {
            Vec4b vec4b = avec4b[j];
            buf.writeByte((vec4b.func_176110_a() & 15) << 4 | vec4b.func_176111_d() & 15);
            buf.writeByte(vec4b.func_176112_b());
            buf.writeByte(vec4b.func_176113_c());
        }

        buf.writeByte(this.field_179735_f);

        if (this.field_179735_f > 0)
        {
            buf.writeByte(this.field_179736_g);
            buf.writeByte(this.field_179737_d);
            buf.writeByte(this.field_179738_e);
            buf.writeByteArray(this.field_179741_h);
        }
    }

    public void func_180741_a(INetHandlerPlayClient p_180741_1_)
    {
        p_180741_1_.handleMaps(this);
    }

    @SideOnly(Side.CLIENT)
    public int getMapId()
    {
        return this.mapId;
    }

    @SideOnly(Side.CLIENT)
    public void func_179734_a(MapData p_179734_1_)
    {
        p_179734_1_.scale = this.field_179739_b;
        p_179734_1_.playersVisibleOnMap.clear();
        int i;

        for (i = 0; i < this.field_179740_c.length; ++i)
        {
            Vec4b vec4b = this.field_179740_c[i];
            p_179734_1_.playersVisibleOnMap.put("icon-" + i, vec4b);
        }

        for (i = 0; i < this.field_179735_f; ++i)
        {
            for (int j = 0; j < this.field_179736_g; ++j)
            {
                p_179734_1_.colors[this.field_179737_d + i + (this.field_179738_e + j) * 128] = this.field_179741_h[i + j * this.field_179735_f];
            }
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180741_a((INetHandlerPlayClient)handler);
    }
}