package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S12PacketEntityVelocity implements Packet
{
    private int field_149417_a;
    private int field_149415_b;
    private int field_149416_c;
    private int field_149414_d;
    private static final String __OBFID = "CL_00001328";

    public S12PacketEntityVelocity() {}

    public S12PacketEntityVelocity(Entity p_i45219_1_)
    {
        this(p_i45219_1_.getEntityId(), p_i45219_1_.motionX, p_i45219_1_.motionY, p_i45219_1_.motionZ);
    }

    public S12PacketEntityVelocity(int p_i45220_1_, double p_i45220_2_, double p_i45220_4_, double p_i45220_6_)
    {
        this.field_149417_a = p_i45220_1_;
        double d3 = 3.9D;

        if (p_i45220_2_ < -d3)
        {
            p_i45220_2_ = -d3;
        }

        if (p_i45220_4_ < -d3)
        {
            p_i45220_4_ = -d3;
        }

        if (p_i45220_6_ < -d3)
        {
            p_i45220_6_ = -d3;
        }

        if (p_i45220_2_ > d3)
        {
            p_i45220_2_ = d3;
        }

        if (p_i45220_4_ > d3)
        {
            p_i45220_4_ = d3;
        }

        if (p_i45220_6_ > d3)
        {
            p_i45220_6_ = d3;
        }

        this.field_149415_b = (int)(p_i45220_2_ * 8000.0D);
        this.field_149416_c = (int)(p_i45220_4_ * 8000.0D);
        this.field_149414_d = (int)(p_i45220_6_ * 8000.0D);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149417_a = buf.readVarIntFromBuffer();
        this.field_149415_b = buf.readShort();
        this.field_149416_c = buf.readShort();
        this.field_149414_d = buf.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.field_149417_a);
        buf.writeShort(this.field_149415_b);
        buf.writeShort(this.field_149416_c);
        buf.writeShort(this.field_149414_d);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityVelocity(this);
    }

    @SideOnly(Side.CLIENT)
    public int func_149412_c()
    {
        return this.field_149417_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int func_149411_d()
    {
        return this.field_149415_b;
    }

    @SideOnly(Side.CLIENT)
    public int func_149410_e()
    {
        return this.field_149416_c;
    }

    @SideOnly(Side.CLIENT)
    public int func_149409_f()
    {
        return this.field_149414_d;
    }
}