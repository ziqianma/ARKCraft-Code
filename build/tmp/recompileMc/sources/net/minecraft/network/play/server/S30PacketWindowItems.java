package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S30PacketWindowItems implements Packet
{
    private int field_148914_a;
    private ItemStack[] field_148913_b;
    private static final String __OBFID = "CL_00001294";

    public S30PacketWindowItems() {}

    public S30PacketWindowItems(int p_i45186_1_, List p_i45186_2_)
    {
        this.field_148914_a = p_i45186_1_;
        this.field_148913_b = new ItemStack[p_i45186_2_.size()];

        for (int j = 0; j < this.field_148913_b.length; ++j)
        {
            ItemStack itemstack = (ItemStack)p_i45186_2_.get(j);
            this.field_148913_b[j] = itemstack == null ? null : itemstack.copy();
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_148914_a = buf.readUnsignedByte();
        short short1 = buf.readShort();
        this.field_148913_b = new ItemStack[short1];

        for (int i = 0; i < short1; ++i)
        {
            this.field_148913_b[i] = buf.readItemStackFromBuffer();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.field_148914_a);
        buf.writeShort(this.field_148913_b.length);
        ItemStack[] aitemstack = this.field_148913_b;
        int i = aitemstack.length;

        for (int j = 0; j < i; ++j)
        {
            ItemStack itemstack = aitemstack[j];
            buf.writeItemStackToBuffer(itemstack);
        }
    }

    public void func_180732_a(INetHandlerPlayClient p_180732_1_)
    {
        p_180732_1_.handleWindowItems(this);
    }

    @SideOnly(Side.CLIENT)
    public int func_148911_c()
    {
        return this.field_148914_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180732_a((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public ItemStack[] func_148910_d()
    {
        return this.field_148913_b;
    }
}