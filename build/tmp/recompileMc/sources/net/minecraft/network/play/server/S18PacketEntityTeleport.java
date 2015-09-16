package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S18PacketEntityTeleport implements Packet
{
    private int field_149458_a;
    private int field_149456_b;
    private int field_149457_c;
    private int field_149454_d;
    private byte field_149455_e;
    private byte field_149453_f;
    private boolean field_179698_g;
    private static final String __OBFID = "CL_00001340";

    public S18PacketEntityTeleport() {}

    public S18PacketEntityTeleport(Entity p_i45233_1_)
    {
        this.field_149458_a = p_i45233_1_.getEntityId();
        this.field_149456_b = MathHelper.floor_double(p_i45233_1_.posX * 32.0D);
        this.field_149457_c = MathHelper.floor_double(p_i45233_1_.posY * 32.0D);
        this.field_149454_d = MathHelper.floor_double(p_i45233_1_.posZ * 32.0D);
        this.field_149455_e = (byte)((int)(p_i45233_1_.rotationYaw * 256.0F / 360.0F));
        this.field_149453_f = (byte)((int)(p_i45233_1_.rotationPitch * 256.0F / 360.0F));
        this.field_179698_g = p_i45233_1_.onGround;
    }

    public S18PacketEntityTeleport(int p_i45949_1_, int p_i45949_2_, int p_i45949_3_, int p_i45949_4_, byte p_i45949_5_, byte p_i45949_6_, boolean p_i45949_7_)
    {
        this.field_149458_a = p_i45949_1_;
        this.field_149456_b = p_i45949_2_;
        this.field_149457_c = p_i45949_3_;
        this.field_149454_d = p_i45949_4_;
        this.field_149455_e = p_i45949_5_;
        this.field_149453_f = p_i45949_6_;
        this.field_179698_g = p_i45949_7_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149458_a = buf.readVarIntFromBuffer();
        this.field_149456_b = buf.readInt();
        this.field_149457_c = buf.readInt();
        this.field_149454_d = buf.readInt();
        this.field_149455_e = buf.readByte();
        this.field_149453_f = buf.readByte();
        this.field_179698_g = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.field_149458_a);
        buf.writeInt(this.field_149456_b);
        buf.writeInt(this.field_149457_c);
        buf.writeInt(this.field_149454_d);
        buf.writeByte(this.field_149455_e);
        buf.writeByte(this.field_149453_f);
        buf.writeBoolean(this.field_179698_g);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityTeleport(this);
    }

    @SideOnly(Side.CLIENT)
    public int func_149451_c()
    {
        return this.field_149458_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int func_149449_d()
    {
        return this.field_149456_b;
    }

    @SideOnly(Side.CLIENT)
    public int func_149448_e()
    {
        return this.field_149457_c;
    }

    @SideOnly(Side.CLIENT)
    public int func_149446_f()
    {
        return this.field_149454_d;
    }

    @SideOnly(Side.CLIENT)
    public byte func_149450_g()
    {
        return this.field_149455_e;
    }

    @SideOnly(Side.CLIENT)
    public byte func_149447_h()
    {
        return this.field_149453_f;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_179697_g()
    {
        return this.field_179698_g;
    }
}