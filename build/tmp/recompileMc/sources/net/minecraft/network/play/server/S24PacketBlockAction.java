package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S24PacketBlockAction implements Packet
{
    private BlockPos field_179826_a;
    private int field_148872_d;
    private int field_148873_e;
    private Block field_148871_f;
    private static final String __OBFID = "CL_00001286";

    public S24PacketBlockAction() {}

    public S24PacketBlockAction(BlockPos p_i45989_1_, Block p_i45989_2_, int p_i45989_3_, int p_i45989_4_)
    {
        this.field_179826_a = p_i45989_1_;
        this.field_148872_d = p_i45989_3_;
        this.field_148873_e = p_i45989_4_;
        this.field_148871_f = p_i45989_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_179826_a = buf.readBlockPos();
        this.field_148872_d = buf.readUnsignedByte();
        this.field_148873_e = buf.readUnsignedByte();
        this.field_148871_f = Block.getBlockById(buf.readVarIntFromBuffer() & 4095);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.field_179826_a);
        buf.writeByte(this.field_148872_d);
        buf.writeByte(this.field_148873_e);
        buf.writeVarIntToBuffer(Block.getIdFromBlock(this.field_148871_f) & 4095);
    }

    public void func_180726_a(INetHandlerPlayClient p_180726_1_)
    {
        p_180726_1_.handleBlockAction(this);
    }

    @SideOnly(Side.CLIENT)
    public BlockPos func_179825_a()
    {
        return this.field_179826_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180726_a((INetHandlerPlayClient)handler);
    }

    /**
     * instrument data for noteblocks
     */
    @SideOnly(Side.CLIENT)
    public int getData1()
    {
        return this.field_148872_d;
    }

    /**
     * pitch data for noteblocks
     */
    @SideOnly(Side.CLIENT)
    public int getData2()
    {
        return this.field_148873_e;
    }

    @SideOnly(Side.CLIENT)
    public Block getBlockType()
    {
        return this.field_148871_f;
    }
}