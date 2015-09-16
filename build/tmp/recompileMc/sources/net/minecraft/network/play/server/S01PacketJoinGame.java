package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S01PacketJoinGame implements Packet
{
    private int entityId;
    private boolean hardcoreMode;
    private WorldSettings.GameType gameType;
    private int dimension;
    private EnumDifficulty difficulty;
    private int maxPlayers;
    private WorldType worldType;
    private boolean reducedDebugInfo;
    private static final String __OBFID = "CL_00001310";

    public S01PacketJoinGame() {}

    public S01PacketJoinGame(int p_i45976_1_, WorldSettings.GameType p_i45976_2_, boolean p_i45976_3_, int p_i45976_4_, EnumDifficulty p_i45976_5_, int p_i45976_6_, WorldType p_i45976_7_, boolean p_i45976_8_)
    {
        this.entityId = p_i45976_1_;
        this.dimension = p_i45976_4_;
        this.difficulty = p_i45976_5_;
        this.gameType = p_i45976_2_;
        this.maxPlayers = p_i45976_6_;
        this.hardcoreMode = p_i45976_3_;
        this.worldType = p_i45976_7_;
        this.reducedDebugInfo = p_i45976_8_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityId = buf.readInt();
        short short1 = buf.readUnsignedByte();
        this.hardcoreMode = (short1 & 8) == 8;
        int i = short1 & -9;
        this.gameType = WorldSettings.GameType.getByID(i);
        this.dimension = buf.readByte();
        this.difficulty = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
        this.maxPlayers = buf.readUnsignedByte();
        this.worldType = WorldType.parseWorldType(buf.readStringFromBuffer(16));

        if (this.worldType == null)
        {
            this.worldType = WorldType.DEFAULT;
        }

        this.reducedDebugInfo = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.entityId);
        int i = this.gameType.getID();

        if (this.hardcoreMode)
        {
            i |= 8;
        }

        buf.writeByte(i);
        buf.writeByte(this.dimension);
        buf.writeByte(this.difficulty.getDifficultyId());
        buf.writeByte(this.maxPlayers);
        buf.writeString(this.worldType.getWorldTypeName());
        buf.writeBoolean(this.reducedDebugInfo);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleJoinGame(this);
    }

    @SideOnly(Side.CLIENT)
    public int getEntityId()
    {
        return this.entityId;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public boolean isHardcoreMode()
    {
        return this.hardcoreMode;
    }

    @SideOnly(Side.CLIENT)
    public WorldSettings.GameType getGameType()
    {
        return this.gameType;
    }

    @SideOnly(Side.CLIENT)
    public int getDimension()
    {
        return this.dimension;
    }

    @SideOnly(Side.CLIENT)
    public EnumDifficulty getDifficulty()
    {
        return this.difficulty;
    }

    @SideOnly(Side.CLIENT)
    public int getMaxPlayers()
    {
        return this.maxPlayers;
    }

    @SideOnly(Side.CLIENT)
    public WorldType getWorldType()
    {
        return this.worldType;
    }

    @SideOnly(Side.CLIENT)
    public boolean isReducedDebugInfo()
    {
        return this.reducedDebugInfo;
    }
}