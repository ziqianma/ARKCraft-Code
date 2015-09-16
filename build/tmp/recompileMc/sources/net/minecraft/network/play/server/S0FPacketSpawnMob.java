package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S0FPacketSpawnMob implements Packet
{
    private int field_149042_a;
    private int field_149040_b;
    private int field_149041_c;
    private int field_149038_d;
    private int field_149039_e;
    private int field_149036_f;
    private int field_149037_g;
    private int field_149047_h;
    private byte field_149048_i;
    private byte field_149045_j;
    private byte field_149046_k;
    private DataWatcher field_149043_l;
    private List field_149044_m;
    private static final String __OBFID = "CL_00001279";

    public S0FPacketSpawnMob() {}

    public S0FPacketSpawnMob(EntityLivingBase p_i45192_1_)
    {
        this.field_149042_a = p_i45192_1_.getEntityId();
        this.field_149040_b = (byte)EntityList.getEntityID(p_i45192_1_);
        this.field_149041_c = MathHelper.floor_double(p_i45192_1_.posX * 32.0D);
        this.field_149038_d = MathHelper.floor_double(p_i45192_1_.posY * 32.0D);
        this.field_149039_e = MathHelper.floor_double(p_i45192_1_.posZ * 32.0D);
        this.field_149048_i = (byte)((int)(p_i45192_1_.rotationYaw * 256.0F / 360.0F));
        this.field_149045_j = (byte)((int)(p_i45192_1_.rotationPitch * 256.0F / 360.0F));
        this.field_149046_k = (byte)((int)(p_i45192_1_.rotationYawHead * 256.0F / 360.0F));
        double d0 = 3.9D;
        double d1 = p_i45192_1_.motionX;
        double d2 = p_i45192_1_.motionY;
        double d3 = p_i45192_1_.motionZ;

        if (d1 < -d0)
        {
            d1 = -d0;
        }

        if (d2 < -d0)
        {
            d2 = -d0;
        }

        if (d3 < -d0)
        {
            d3 = -d0;
        }

        if (d1 > d0)
        {
            d1 = d0;
        }

        if (d2 > d0)
        {
            d2 = d0;
        }

        if (d3 > d0)
        {
            d3 = d0;
        }

        this.field_149036_f = (int)(d1 * 8000.0D);
        this.field_149037_g = (int)(d2 * 8000.0D);
        this.field_149047_h = (int)(d3 * 8000.0D);
        this.field_149043_l = p_i45192_1_.getDataWatcher();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149042_a = buf.readVarIntFromBuffer();
        this.field_149040_b = buf.readByte() & 255;
        this.field_149041_c = buf.readInt();
        this.field_149038_d = buf.readInt();
        this.field_149039_e = buf.readInt();
        this.field_149048_i = buf.readByte();
        this.field_149045_j = buf.readByte();
        this.field_149046_k = buf.readByte();
        this.field_149036_f = buf.readShort();
        this.field_149037_g = buf.readShort();
        this.field_149047_h = buf.readShort();
        this.field_149044_m = DataWatcher.readWatchedListFromPacketBuffer(buf);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.field_149042_a);
        buf.writeByte(this.field_149040_b & 255);
        buf.writeInt(this.field_149041_c);
        buf.writeInt(this.field_149038_d);
        buf.writeInt(this.field_149039_e);
        buf.writeByte(this.field_149048_i);
        buf.writeByte(this.field_149045_j);
        buf.writeByte(this.field_149046_k);
        buf.writeShort(this.field_149036_f);
        buf.writeShort(this.field_149037_g);
        buf.writeShort(this.field_149047_h);
        this.field_149043_l.writeTo(buf);
    }

    public void func_180721_a(INetHandlerPlayClient p_180721_1_)
    {
        p_180721_1_.handleSpawnMob(this);
    }

    @SideOnly(Side.CLIENT)
    public List func_149027_c()
    {
        if (this.field_149044_m == null)
        {
            this.field_149044_m = this.field_149043_l.getAllWatched();
        }

        return this.field_149044_m;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180721_a((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int func_149024_d()
    {
        return this.field_149042_a;
    }

    @SideOnly(Side.CLIENT)
    public int func_149025_e()
    {
        return this.field_149040_b;
    }

    @SideOnly(Side.CLIENT)
    public int func_149023_f()
    {
        return this.field_149041_c;
    }

    @SideOnly(Side.CLIENT)
    public int func_149034_g()
    {
        return this.field_149038_d;
    }

    @SideOnly(Side.CLIENT)
    public int func_149029_h()
    {
        return this.field_149039_e;
    }

    @SideOnly(Side.CLIENT)
    public int func_149026_i()
    {
        return this.field_149036_f;
    }

    @SideOnly(Side.CLIENT)
    public int func_149033_j()
    {
        return this.field_149037_g;
    }

    @SideOnly(Side.CLIENT)
    public int func_149031_k()
    {
        return this.field_149047_h;
    }

    @SideOnly(Side.CLIENT)
    public byte func_149028_l()
    {
        return this.field_149048_i;
    }

    @SideOnly(Side.CLIENT)
    public byte func_149030_m()
    {
        return this.field_149045_j;
    }

    @SideOnly(Side.CLIENT)
    public byte func_149032_n()
    {
        return this.field_149046_k;
    }
}