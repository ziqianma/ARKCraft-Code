package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S0APacketUseBed implements Packet
{
    private int playerID;
    /** Block location of the head part of the bed */
    private BlockPos bedPos;
    private static final String __OBFID = "CL_00001319";

    public S0APacketUseBed() {}

    public S0APacketUseBed(EntityPlayer p_i45964_1_, BlockPos p_i45964_2_)
    {
        this.playerID = p_i45964_1_.getEntityId();
        this.bedPos = p_i45964_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.playerID = buf.readVarIntFromBuffer();
        this.bedPos = buf.readBlockPos();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.playerID);
        buf.writeBlockPos(this.bedPos);
    }

    public void func_180744_a(INetHandlerPlayClient p_180744_1_)
    {
        p_180744_1_.handleUseBed(this);
    }

    @SideOnly(Side.CLIENT)
    public EntityPlayer getPlayer(World worldIn)
    {
        return (EntityPlayer)worldIn.getEntityByID(this.playerID);
    }

    @SideOnly(Side.CLIENT)
    public BlockPos func_179798_a()
    {
        return this.bedPos;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180744_a((INetHandlerPlayClient)handler);
    }
}