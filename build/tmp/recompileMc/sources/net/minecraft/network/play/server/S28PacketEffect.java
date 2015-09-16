package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S28PacketEffect implements Packet
{
    private int soundType;
    private BlockPos field_179747_b;
    /** can be a block/item id or other depending on the soundtype */
    private int soundData;
    /** If true the sound is played across the server */
    private boolean serverWide;
    private static final String __OBFID = "CL_00001307";

    public S28PacketEffect() {}

    public S28PacketEffect(int p_i45978_1_, BlockPos p_i45978_2_, int p_i45978_3_, boolean p_i45978_4_)
    {
        this.soundType = p_i45978_1_;
        this.field_179747_b = p_i45978_2_;
        this.soundData = p_i45978_3_;
        this.serverWide = p_i45978_4_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.soundType = buf.readInt();
        this.field_179747_b = buf.readBlockPos();
        this.soundData = buf.readInt();
        this.serverWide = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.soundType);
        buf.writeBlockPos(this.field_179747_b);
        buf.writeInt(this.soundData);
        buf.writeBoolean(this.serverWide);
    }

    public void func_180739_a(INetHandlerPlayClient p_180739_1_)
    {
        p_180739_1_.handleEffect(this);
    }

    @SideOnly(Side.CLIENT)
    public boolean isSoundServerwide()
    {
        return this.serverWide;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180739_a((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int getSoundType()
    {
        return this.soundType;
    }

    @SideOnly(Side.CLIENT)
    public int getSoundData()
    {
        return this.soundData;
    }

    @SideOnly(Side.CLIENT)
    public BlockPos func_179746_d()
    {
        return this.field_179747_b;
    }
}