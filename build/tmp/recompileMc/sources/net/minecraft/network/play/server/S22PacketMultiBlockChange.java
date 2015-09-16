package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S22PacketMultiBlockChange implements Packet
{
    private ChunkCoordIntPair chunkPosCoord;
    private S22PacketMultiBlockChange.BlockUpdateData[] changedBlocks;
    private static final String __OBFID = "CL_00001290";

    public S22PacketMultiBlockChange() {}

    public S22PacketMultiBlockChange(int p_i45181_1_, short[] p_i45181_2_, Chunk p_i45181_3_)
    {
        this.chunkPosCoord = new ChunkCoordIntPair(p_i45181_3_.xPosition, p_i45181_3_.zPosition);
        this.changedBlocks = new S22PacketMultiBlockChange.BlockUpdateData[p_i45181_1_];

        for (int j = 0; j < this.changedBlocks.length; ++j)
        {
            this.changedBlocks[j] = new S22PacketMultiBlockChange.BlockUpdateData(p_i45181_2_[j], p_i45181_3_);
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.chunkPosCoord = new ChunkCoordIntPair(buf.readInt(), buf.readInt());
        this.changedBlocks = new S22PacketMultiBlockChange.BlockUpdateData[buf.readVarIntFromBuffer()];

        for (int i = 0; i < this.changedBlocks.length; ++i)
        {
            this.changedBlocks[i] = new S22PacketMultiBlockChange.BlockUpdateData(buf.readShort(), (IBlockState)Block.BLOCK_STATE_IDS.getByValue(buf.readVarIntFromBuffer()));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.chunkPosCoord.chunkXPos);
        buf.writeInt(this.chunkPosCoord.chunkZPos);
        buf.writeVarIntToBuffer(this.changedBlocks.length);
        S22PacketMultiBlockChange.BlockUpdateData[] ablockupdatedata = this.changedBlocks;
        int i = ablockupdatedata.length;

        for (int j = 0; j < i; ++j)
        {
            S22PacketMultiBlockChange.BlockUpdateData blockupdatedata = ablockupdatedata[j];
            buf.writeShort(blockupdatedata.func_180089_b());
            buf.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(blockupdatedata.func_180088_c()));
        }
    }

    public void func_180729_a(INetHandlerPlayClient p_180729_1_)
    {
        p_180729_1_.handleMultiBlockChange(this);
    }

    @SideOnly(Side.CLIENT)
    public S22PacketMultiBlockChange.BlockUpdateData[] func_179844_a()
    {
        return this.changedBlocks;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180729_a((INetHandlerPlayClient)handler);
    }

    public class BlockUpdateData
    {
        private final short field_180091_b;
        private final IBlockState field_180092_c;
        private static final String __OBFID = "CL_00002302";

        public BlockUpdateData(short p_i45984_2_, IBlockState p_i45984_3_)
        {
            this.field_180091_b = p_i45984_2_;
            this.field_180092_c = p_i45984_3_;
        }

        public BlockUpdateData(short p_i45985_2_, Chunk p_i45985_3_)
        {
            this.field_180091_b = p_i45985_2_;
            this.field_180092_c = p_i45985_3_.getBlockState(this.func_180090_a());
        }

        public BlockPos func_180090_a()
        {
            return new BlockPos(S22PacketMultiBlockChange.this.chunkPosCoord.getBlock(this.field_180091_b >> 12 & 15, this.field_180091_b & 255, this.field_180091_b >> 8 & 15));
        }

        public short func_180089_b()
        {
            return this.field_180091_b;
        }

        public IBlockState func_180088_c()
        {
            return this.field_180092_c;
        }
    }
}