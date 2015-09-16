package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S1DPacketEntityEffect implements Packet
{
    private int field_149434_a;
    private byte field_149432_b;
    private byte field_149433_c;
    private int field_149431_d;
    private byte field_179708_e;
    private static final String __OBFID = "CL_00001343";

    public S1DPacketEntityEffect() {}

    public S1DPacketEntityEffect(int p_i45237_1_, PotionEffect p_i45237_2_)
    {
        this.field_149434_a = p_i45237_1_;
        this.field_149432_b = (byte)(p_i45237_2_.getPotionID() & 255);
        this.field_149433_c = (byte)(p_i45237_2_.getAmplifier() & 255);

        if (p_i45237_2_.getDuration() > 32767)
        {
            this.field_149431_d = 32767;
        }
        else
        {
            this.field_149431_d = p_i45237_2_.getDuration();
        }

        this.field_179708_e = (byte)(p_i45237_2_.getIsShowParticles() ? 1 : 0);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149434_a = buf.readVarIntFromBuffer();
        this.field_149432_b = buf.readByte();
        this.field_149433_c = buf.readByte();
        this.field_149431_d = buf.readVarIntFromBuffer();
        this.field_179708_e = buf.readByte();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.field_149434_a);
        buf.writeByte(this.field_149432_b);
        buf.writeByte(this.field_149433_c);
        buf.writeVarIntToBuffer(this.field_149431_d);
        buf.writeByte(this.field_179708_e);
    }

    @SideOnly(Side.CLIENT)
    public boolean func_149429_c()
    {
        return this.field_149431_d == 32767;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityEffect(this);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int func_149426_d()
    {
        return this.field_149434_a;
    }

    @SideOnly(Side.CLIENT)
    public byte func_149427_e()
    {
        return this.field_149432_b;
    }

    @SideOnly(Side.CLIENT)
    public byte func_149428_f()
    {
        return this.field_149433_c;
    }

    @SideOnly(Side.CLIENT)
    public int func_180755_e()
    {
        return this.field_149431_d;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_179707_f()
    {
        return this.field_179708_e != 0;
    }
}