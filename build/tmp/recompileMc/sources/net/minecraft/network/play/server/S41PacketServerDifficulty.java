package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S41PacketServerDifficulty implements Packet
{
    private EnumDifficulty field_179833_a;
    private boolean field_179832_b;
    private static final String __OBFID = "CL_00002303";

    public S41PacketServerDifficulty() {}

    public S41PacketServerDifficulty(EnumDifficulty p_i45987_1_, boolean p_i45987_2_)
    {
        this.field_179833_a = p_i45987_1_;
        this.field_179832_b = p_i45987_2_;
    }

    public void func_179829_a(INetHandlerPlayClient p_179829_1_)
    {
        p_179829_1_.handleServerDifficulty(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_179833_a = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.field_179833_a.getDifficultyId());
    }

    @SideOnly(Side.CLIENT)
    public boolean func_179830_a()
    {
        return this.field_179832_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_179829_a((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public EnumDifficulty func_179831_b()
    {
        return this.field_179833_a;
    }
}