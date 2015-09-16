package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S3CPacketUpdateScore implements Packet
{
    private String name = "";
    private String objective = "";
    private int value;
    private S3CPacketUpdateScore.Action action;
    private static final String __OBFID = "CL_00001335";

    public S3CPacketUpdateScore() {}

    public S3CPacketUpdateScore(Score scoreIn)
    {
        this.name = scoreIn.getPlayerName();
        this.objective = scoreIn.getObjective().getName();
        this.value = scoreIn.getScorePoints();
        this.action = S3CPacketUpdateScore.Action.CHANGE;
    }

    public S3CPacketUpdateScore(String nameIn)
    {
        this.name = nameIn;
        this.objective = "";
        this.value = 0;
        this.action = S3CPacketUpdateScore.Action.REMOVE;
    }

    public S3CPacketUpdateScore(String nameIn, ScoreObjective objectiveIn)
    {
        this.name = nameIn;
        this.objective = objectiveIn.getName();
        this.value = 0;
        this.action = S3CPacketUpdateScore.Action.REMOVE;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.name = buf.readStringFromBuffer(40);
        this.action = (S3CPacketUpdateScore.Action)buf.readEnumValue(S3CPacketUpdateScore.Action.class);
        this.objective = buf.readStringFromBuffer(16);

        if (this.action != S3CPacketUpdateScore.Action.REMOVE)
        {
            this.value = buf.readVarIntFromBuffer();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(this.name);
        buf.writeEnumValue(this.action);
        buf.writeString(this.objective);

        if (this.action != S3CPacketUpdateScore.Action.REMOVE)
        {
            buf.writeVarIntToBuffer(this.value);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUpdateScore(this);
    }

    @SideOnly(Side.CLIENT)
    public String func_149324_c()
    {
        return this.name;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public String func_149321_d()
    {
        return this.objective;
    }

    @SideOnly(Side.CLIENT)
    public int func_149323_e()
    {
        return this.value;
    }

    @SideOnly(Side.CLIENT)
    public S3CPacketUpdateScore.Action func_180751_d()
    {
        return this.action;
    }

    public static enum Action
    {
        CHANGE,
        REMOVE;

        private static final String __OBFID = "CL_00002288";
    }
}