package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S35PacketUpdateTileEntity implements Packet
{
    private BlockPos field_179824_a;
    /** Used only for vanilla tile entities */
    private int metadata;
    private NBTTagCompound nbt;
    private static final String __OBFID = "CL_00001285";

    public S35PacketUpdateTileEntity() {}

    public S35PacketUpdateTileEntity(BlockPos p_i45990_1_, int p_i45990_2_, NBTTagCompound p_i45990_3_)
    {
        this.field_179824_a = p_i45990_1_;
        this.metadata = p_i45990_2_;
        this.nbt = p_i45990_3_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_179824_a = buf.readBlockPos();
        this.metadata = buf.readUnsignedByte();
        this.nbt = buf.readNBTTagCompoundFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBlockPos(this.field_179824_a);
        buf.writeByte((byte)this.metadata);
        buf.writeNBTTagCompoundToBuffer(this.nbt);
    }

    public void func_180725_a(INetHandlerPlayClient p_180725_1_)
    {
        p_180725_1_.handleUpdateTileEntity(this);
    }

    @SideOnly(Side.CLIENT)
    public BlockPos func_179823_a()
    {
        return this.field_179824_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180725_a((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public int getTileEntityType()
    {
        return this.metadata;
    }

    @SideOnly(Side.CLIENT)
    public NBTTagCompound getNbtCompound()
    {
        return this.nbt;
    }
}