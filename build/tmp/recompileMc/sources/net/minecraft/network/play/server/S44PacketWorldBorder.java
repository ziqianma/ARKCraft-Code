package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S44PacketWorldBorder implements Packet
{
    private S44PacketWorldBorder.Action field_179795_a;
    private int field_179793_b;
    private double field_179794_c;
    private double field_179791_d;
    private double field_179792_e;
    private double field_179789_f;
    private long field_179790_g;
    private int field_179796_h;
    private int field_179797_i;
    private static final String __OBFID = "CL_00002292";

    public S44PacketWorldBorder() {}

    public S44PacketWorldBorder(WorldBorder p_i45962_1_, S44PacketWorldBorder.Action p_i45962_2_)
    {
        this.field_179795_a = p_i45962_2_;
        this.field_179794_c = p_i45962_1_.getCenterX();
        this.field_179791_d = p_i45962_1_.getCenterZ();
        this.field_179789_f = p_i45962_1_.getDiameter();
        this.field_179792_e = p_i45962_1_.getTargetSize();
        this.field_179790_g = p_i45962_1_.getTimeUntilTarget();
        this.field_179793_b = p_i45962_1_.getSize();
        this.field_179797_i = p_i45962_1_.getWarningDistance();
        this.field_179796_h = p_i45962_1_.getWarningTime();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_179795_a = (S44PacketWorldBorder.Action)buf.readEnumValue(S44PacketWorldBorder.Action.class);

        switch (S44PacketWorldBorder.SwitchAction.field_179947_a[this.field_179795_a.ordinal()])
        {
            case 1:
                this.field_179792_e = buf.readDouble();
                break;
            case 2:
                this.field_179789_f = buf.readDouble();
                this.field_179792_e = buf.readDouble();
                this.field_179790_g = buf.readVarLong();
                break;
            case 3:
                this.field_179794_c = buf.readDouble();
                this.field_179791_d = buf.readDouble();
                break;
            case 4:
                this.field_179797_i = buf.readVarIntFromBuffer();
                break;
            case 5:
                this.field_179796_h = buf.readVarIntFromBuffer();
                break;
            case 6:
                this.field_179794_c = buf.readDouble();
                this.field_179791_d = buf.readDouble();
                this.field_179789_f = buf.readDouble();
                this.field_179792_e = buf.readDouble();
                this.field_179790_g = buf.readVarLong();
                this.field_179793_b = buf.readVarIntFromBuffer();
                this.field_179797_i = buf.readVarIntFromBuffer();
                this.field_179796_h = buf.readVarIntFromBuffer();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(this.field_179795_a);

        switch (S44PacketWorldBorder.SwitchAction.field_179947_a[this.field_179795_a.ordinal()])
        {
            case 1:
                buf.writeDouble(this.field_179792_e);
                break;
            case 2:
                buf.writeDouble(this.field_179789_f);
                buf.writeDouble(this.field_179792_e);
                buf.writeVarLong(this.field_179790_g);
                break;
            case 3:
                buf.writeDouble(this.field_179794_c);
                buf.writeDouble(this.field_179791_d);
                break;
            case 4:
                buf.writeVarIntToBuffer(this.field_179797_i);
                break;
            case 5:
                buf.writeVarIntToBuffer(this.field_179796_h);
                break;
            case 6:
                buf.writeDouble(this.field_179794_c);
                buf.writeDouble(this.field_179791_d);
                buf.writeDouble(this.field_179789_f);
                buf.writeDouble(this.field_179792_e);
                buf.writeVarLong(this.field_179790_g);
                buf.writeVarIntToBuffer(this.field_179793_b);
                buf.writeVarIntToBuffer(this.field_179797_i);
                buf.writeVarIntToBuffer(this.field_179796_h);
        }
    }

    public void func_179787_a(INetHandlerPlayClient p_179787_1_)
    {
        p_179787_1_.handleWorldBorder(this);
    }

    @SideOnly(Side.CLIENT)
    public void func_179788_a(WorldBorder p_179788_1_)
    {
        switch (S44PacketWorldBorder.SwitchAction.field_179947_a[this.field_179795_a.ordinal()])
        {
            case 1:
                p_179788_1_.setTransition(this.field_179792_e);
                break;
            case 2:
                p_179788_1_.setTransition(this.field_179789_f, this.field_179792_e, this.field_179790_g);
                break;
            case 3:
                p_179788_1_.setCenter(this.field_179794_c, this.field_179791_d);
                break;
            case 4:
                p_179788_1_.setWarningDistance(this.field_179797_i);
                break;
            case 5:
                p_179788_1_.setWarningTime(this.field_179796_h);
                break;
            case 6:
                p_179788_1_.setCenter(this.field_179794_c, this.field_179791_d);

                if (this.field_179790_g > 0L)
                {
                    p_179788_1_.setTransition(this.field_179789_f, this.field_179792_e, this.field_179790_g);
                }
                else
                {
                    p_179788_1_.setTransition(this.field_179792_e);
                }

                p_179788_1_.setSize(this.field_179793_b);
                p_179788_1_.setWarningDistance(this.field_179797_i);
                p_179788_1_.setWarningTime(this.field_179796_h);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_179787_a((INetHandlerPlayClient)handler);
    }

    public static enum Action
    {
        SET_SIZE,
        LERP_SIZE,
        SET_CENTER,
        INITIALIZE,
        SET_WARNING_TIME,
        SET_WARNING_BLOCKS;

        private static final String __OBFID = "CL_00002290";
    }

    static final class SwitchAction
        {
            static final int[] field_179947_a = new int[S44PacketWorldBorder.Action.values().length];
            private static final String __OBFID = "CL_00002291";

            static
            {
                try
                {
                    field_179947_a[S44PacketWorldBorder.Action.SET_SIZE.ordinal()] = 1;
                }
                catch (NoSuchFieldError var6)
                {
                    ;
                }

                try
                {
                    field_179947_a[S44PacketWorldBorder.Action.LERP_SIZE.ordinal()] = 2;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                try
                {
                    field_179947_a[S44PacketWorldBorder.Action.SET_CENTER.ordinal()] = 3;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    field_179947_a[S44PacketWorldBorder.Action.SET_WARNING_BLOCKS.ordinal()] = 4;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_179947_a[S44PacketWorldBorder.Action.SET_WARNING_TIME.ordinal()] = 5;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_179947_a[S44PacketWorldBorder.Action.INITIALIZE.ordinal()] = 6;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}