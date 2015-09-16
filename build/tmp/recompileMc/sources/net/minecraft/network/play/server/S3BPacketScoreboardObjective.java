package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S3BPacketScoreboardObjective implements Packet
{
    private String field_149343_a;
    private String field_149341_b;
    private IScoreObjectiveCriteria.EnumRenderType field_179818_c;
    private int field_149342_c;
    private static final String __OBFID = "CL_00001333";

    public S3BPacketScoreboardObjective() {}

    public S3BPacketScoreboardObjective(ScoreObjective p_i45224_1_, int p_i45224_2_)
    {
        this.field_149343_a = p_i45224_1_.getName();
        this.field_149341_b = p_i45224_1_.getDisplayName();
        this.field_179818_c = p_i45224_1_.getCriteria().getRenderType();
        this.field_149342_c = p_i45224_2_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149343_a = buf.readStringFromBuffer(16);
        this.field_149342_c = buf.readByte();

        if (this.field_149342_c == 0 || this.field_149342_c == 2)
        {
            this.field_149341_b = buf.readStringFromBuffer(32);
            this.field_179818_c = IScoreObjectiveCriteria.EnumRenderType.func_178795_a(buf.readStringFromBuffer(16));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeString(this.field_149343_a);
        buf.writeByte(this.field_149342_c);

        if (this.field_149342_c == 0 || this.field_149342_c == 2)
        {
            buf.writeString(this.field_149341_b);
            buf.writeString(this.field_179818_c.func_178796_a());
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleScoreboardObjective(this);
    }

    @SideOnly(Side.CLIENT)
    public String func_149339_c()
    {
        return this.field_149343_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public String func_149337_d()
    {
        return this.field_149341_b;
    }

    @SideOnly(Side.CLIENT)
    public int func_149338_e()
    {
        return this.field_149342_c;
    }

    @SideOnly(Side.CLIENT)
    public IScoreObjectiveCriteria.EnumRenderType func_179817_d()
    {
        return this.field_179818_c;
    }
}