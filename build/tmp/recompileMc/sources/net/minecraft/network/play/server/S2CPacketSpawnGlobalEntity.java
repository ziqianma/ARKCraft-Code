package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S2CPacketSpawnGlobalEntity implements Packet
{
    private int field_149059_a;
    private int field_149057_b;
    private int field_149058_c;
    private int field_149055_d;
    private int field_149056_e;
    private static final String __OBFID = "CL_00001278";

    public S2CPacketSpawnGlobalEntity() {}

    public S2CPacketSpawnGlobalEntity(Entity p_i45191_1_)
    {
        this.field_149059_a = p_i45191_1_.getEntityId();
        this.field_149057_b = MathHelper.floor_double(p_i45191_1_.posX * 32.0D);
        this.field_149058_c = MathHelper.floor_double(p_i45191_1_.posY * 32.0D);
        this.field_149055_d = MathHelper.floor_double(p_i45191_1_.posZ * 32.0D);

        if (p_i45191_1_ instanceof EntityLightningBolt)
        {
            this.field_149056_e = 1;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149059_a = buf.readVarIntFromBuffer();
        this.field_149056_e = buf.readByte();
        this.field_149057_b = buf.readInt();
        this.field_149058_c = buf.readInt();
        this.field_149055_d = buf.readInt();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.field_149059_a);
        buf.writeByte(this.field_149056_e);
        buf.writeInt(this.field_149057_b);
        buf.writeInt(this.field_149058_c);
        buf.writeInt(this.field_149055_d);
    }

    public void func_180720_a(INetHandlerPlayClient p_180720_1_)
    {
        p_180720_1_.handleSpawnGlobalEntity(this);
    }

    @SideOnly(Side.CLIENT)
    public int func_149052_c()
    {
        return this.field_149059_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180720_a((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int func_149051_d()
    {
        return this.field_149057_b;
    }

    @SideOnly(Side.CLIENT)
    public int func_149050_e()
    {
        return this.field_149058_c;
    }

    @SideOnly(Side.CLIENT)
    public int func_149049_f()
    {
        return this.field_149055_d;
    }

    @SideOnly(Side.CLIENT)
    public int func_149053_g()
    {
        return this.field_149056_e;
    }
}