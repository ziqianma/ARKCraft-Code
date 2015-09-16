package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S1FPacketSetExperience implements Packet
{
    private float field_149401_a;
    private int field_149399_b;
    private int field_149400_c;
    private static final String __OBFID = "CL_00001331";

    public S1FPacketSetExperience() {}

    public S1FPacketSetExperience(float p_i45222_1_, int p_i45222_2_, int p_i45222_3_)
    {
        this.field_149401_a = p_i45222_1_;
        this.field_149399_b = p_i45222_2_;
        this.field_149400_c = p_i45222_3_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149401_a = buf.readFloat();
        this.field_149400_c = buf.readVarIntFromBuffer();
        this.field_149399_b = buf.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeFloat(this.field_149401_a);
        buf.writeVarIntToBuffer(this.field_149400_c);
        buf.writeVarIntToBuffer(this.field_149399_b);
    }

    public void func_180749_a(INetHandlerPlayClient p_180749_1_)
    {
        p_180749_1_.handleSetExperience(this);
    }

    @SideOnly(Side.CLIENT)
    public float func_149397_c()
    {
        return this.field_149401_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180749_a((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int func_149396_d()
    {
        return this.field_149399_b;
    }

    @SideOnly(Side.CLIENT)
    public int func_149395_e()
    {
        return this.field_149400_c;
    }
}