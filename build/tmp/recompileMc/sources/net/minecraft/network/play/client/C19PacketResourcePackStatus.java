package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class C19PacketResourcePackStatus implements Packet
{
    private String hash;
    private C19PacketResourcePackStatus.Action status;
    private static final String __OBFID = "CL_00002282";

    public C19PacketResourcePackStatus() {}

    @SideOnly(Side.CLIENT)
    public C19PacketResourcePackStatus(String p_i45935_1_, C19PacketResourcePackStatus.Action p_i45935_2_)
    {
        if (p_i45935_1_.length() > 40)
        {
            p_i45935_1_ = p_i45935_1_.substring(0, 40);
        }

        this.hash = p_i45935_1_;
        this.status = p_i45935_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.hash = buf.readStringFromBuffer(40);
        this.status = (C19PacketResourcePackStatus.Action)buf.readEnumValue(C19PacketResourcePackStatus.Action.class);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(this.hash);
        buf.writeEnumValue(this.status);
    }

    public void handle(INetHandlerPlayServer handler)
    {
        handler.handleResourcePackStatus(this);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.handle((INetHandlerPlayServer)handler);
    }

    public static enum Action
    {
        SUCCESSFULLY_LOADED,
        DECLINED,
        FAILED_DOWNLOAD,
        ACCEPTED;

        private static final String __OBFID = "CL_00002281";
    }
}