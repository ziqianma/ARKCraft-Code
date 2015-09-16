package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S32PacketConfirmTransaction implements Packet
{
    private int field_148894_a;
    private short field_148892_b;
    private boolean field_148893_c;
    private static final String __OBFID = "CL_00001291";

    public S32PacketConfirmTransaction() {}

    public S32PacketConfirmTransaction(int p_i45182_1_, short p_i45182_2_, boolean p_i45182_3_)
    {
        this.field_148894_a = p_i45182_1_;
        this.field_148892_b = p_i45182_2_;
        this.field_148893_c = p_i45182_3_;
    }

    public void func_180730_a(INetHandlerPlayClient p_180730_1_)
    {
        p_180730_1_.handleConfirmTransaction(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_148894_a = buf.readUnsignedByte();
        this.field_148892_b = buf.readShort();
        this.field_148893_c = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.field_148894_a);
        buf.writeShort(this.field_148892_b);
        buf.writeBoolean(this.field_148893_c);
    }

    @SideOnly(Side.CLIENT)
    public int func_148889_c()
    {
        return this.field_148894_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180730_a((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public short func_148890_d()
    {
        return this.field_148892_b;
    }

    @SideOnly(Side.CLIENT)
    public boolean func_148888_e()
    {
        return this.field_148893_c;
    }
}