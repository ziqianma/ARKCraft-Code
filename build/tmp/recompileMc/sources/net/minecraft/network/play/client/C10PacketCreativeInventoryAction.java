package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class C10PacketCreativeInventoryAction implements Packet
{
    private int slotId;
    private ItemStack stack;
    private static final String __OBFID = "CL_00001369";

    public C10PacketCreativeInventoryAction() {}

    @SideOnly(Side.CLIENT)
    public C10PacketCreativeInventoryAction(int p_i45263_1_, ItemStack p_i45263_2_)
    {
        this.slotId = p_i45263_1_;
        this.stack = p_i45263_2_ != null ? p_i45263_2_.copy() : null;
    }

    public void handle(INetHandlerPlayServer handler)
    {
        handler.processCreativeInventoryAction(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.slotId = buf.readShort();
        this.stack = buf.readItemStackFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeShort(this.slotId);
        buf.writeItemStackToBuffer(this.stack);
    }

    public int getSlotId()
    {
        return this.slotId;
    }

    public ItemStack getStack()
    {
        return this.stack;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.handle((INetHandlerPlayServer)handler);
    }
}