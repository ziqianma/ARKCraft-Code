package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S08PacketPlayerPosLook implements Packet
{
    private double field_148940_a;
    private double field_148938_b;
    private double field_148939_c;
    private float field_148936_d;
    private float field_148937_e;
    private Set field_179835_f;
    private static final String __OBFID = "CL_00001273";

    public S08PacketPlayerPosLook() {}

    public S08PacketPlayerPosLook(double p_i45993_1_, double p_i45993_3_, double p_i45993_5_, float p_i45993_7_, float p_i45993_8_, Set p_i45993_9_)
    {
        this.field_148940_a = p_i45993_1_;
        this.field_148938_b = p_i45993_3_;
        this.field_148939_c = p_i45993_5_;
        this.field_148936_d = p_i45993_7_;
        this.field_148937_e = p_i45993_8_;
        this.field_179835_f = p_i45993_9_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_148940_a = buf.readDouble();
        this.field_148938_b = buf.readDouble();
        this.field_148939_c = buf.readDouble();
        this.field_148936_d = buf.readFloat();
        this.field_148937_e = buf.readFloat();
        this.field_179835_f = S08PacketPlayerPosLook.EnumFlags.func_180053_a(buf.readUnsignedByte());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeDouble(this.field_148940_a);
        buf.writeDouble(this.field_148938_b);
        buf.writeDouble(this.field_148939_c);
        buf.writeFloat(this.field_148936_d);
        buf.writeFloat(this.field_148937_e);
        buf.writeByte(S08PacketPlayerPosLook.EnumFlags.func_180056_a(this.field_179835_f));
    }

    public void func_180718_a(INetHandlerPlayClient p_180718_1_)
    {
        p_180718_1_.handlePlayerPosLook(this);
    }

    @SideOnly(Side.CLIENT)
    public double func_148932_c()
    {
        return this.field_148940_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180718_a((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public double func_148928_d()
    {
        return this.field_148938_b;
    }

    @SideOnly(Side.CLIENT)
    public double func_148933_e()
    {
        return this.field_148939_c;
    }

    @SideOnly(Side.CLIENT)
    public float func_148931_f()
    {
        return this.field_148936_d;
    }

    @SideOnly(Side.CLIENT)
    public float func_148930_g()
    {
        return this.field_148937_e;
    }

    @SideOnly(Side.CLIENT)
    public Set func_179834_f()
    {
        return this.field_179835_f;
    }

    public static enum EnumFlags
    {
        X(0),
        Y(1),
        Z(2),
        Y_ROT(3),
        X_ROT(4);
        private int field_180058_f;

        private static final String __OBFID = "CL_00002304";

        private EnumFlags(int p_i45992_3_)
        {
            this.field_180058_f = p_i45992_3_;
        }

        private int func_180055_a()
        {
            return 1 << this.field_180058_f;
        }

        private boolean func_180054_b(int p_180054_1_)
        {
            return (p_180054_1_ & this.func_180055_a()) == this.func_180055_a();
        }

        public static Set func_180053_a(int p_180053_0_)
        {
            EnumSet enumset = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
            S08PacketPlayerPosLook.EnumFlags[] aenumflags = values();
            int j = aenumflags.length;

            for (int k = 0; k < j; ++k)
            {
                S08PacketPlayerPosLook.EnumFlags enumflags = aenumflags[k];

                if (enumflags.func_180054_b(p_180053_0_))
                {
                    enumset.add(enumflags);
                }
            }

            return enumset;
        }

        public static int func_180056_a(Set p_180056_0_)
        {
            int i = 0;
            S08PacketPlayerPosLook.EnumFlags enumflags;

            for (Iterator iterator = p_180056_0_.iterator(); iterator.hasNext(); i |= enumflags.func_180055_a())
            {
                enumflags = (S08PacketPlayerPosLook.EnumFlags)iterator.next();
            }

            return i;
        }
    }
}