package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S27PacketExplosion implements Packet
{
    private double field_149158_a;
    private double field_149156_b;
    private double field_149157_c;
    private float field_149154_d;
    private List field_149155_e;
    private float field_149152_f;
    private float field_149153_g;
    private float field_149159_h;
    private static final String __OBFID = "CL_00001300";

    public S27PacketExplosion() {}

    public S27PacketExplosion(double p_i45193_1_, double p_i45193_3_, double p_i45193_5_, float p_i45193_7_, List p_i45193_8_, Vec3 p_i45193_9_)
    {
        this.field_149158_a = p_i45193_1_;
        this.field_149156_b = p_i45193_3_;
        this.field_149157_c = p_i45193_5_;
        this.field_149154_d = p_i45193_7_;
        this.field_149155_e = Lists.newArrayList(p_i45193_8_);

        if (p_i45193_9_ != null)
        {
            this.field_149152_f = (float)p_i45193_9_.xCoord;
            this.field_149153_g = (float)p_i45193_9_.yCoord;
            this.field_149159_h = (float)p_i45193_9_.zCoord;
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149158_a = (double)buf.readFloat();
        this.field_149156_b = (double)buf.readFloat();
        this.field_149157_c = (double)buf.readFloat();
        this.field_149154_d = buf.readFloat();
        int i = buf.readInt();
        this.field_149155_e = Lists.newArrayListWithCapacity(i);
        int j = (int)this.field_149158_a;
        int k = (int)this.field_149156_b;
        int l = (int)this.field_149157_c;

        for (int i1 = 0; i1 < i; ++i1)
        {
            int j1 = buf.readByte() + j;
            int k1 = buf.readByte() + k;
            int l1 = buf.readByte() + l;
            this.field_149155_e.add(new BlockPos(j1, k1, l1));
        }

        this.field_149152_f = buf.readFloat();
        this.field_149153_g = buf.readFloat();
        this.field_149159_h = buf.readFloat();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeFloat((float)this.field_149158_a);
        buf.writeFloat((float)this.field_149156_b);
        buf.writeFloat((float)this.field_149157_c);
        buf.writeFloat(this.field_149154_d);
        buf.writeInt(this.field_149155_e.size());
        int i = (int)this.field_149158_a;
        int j = (int)this.field_149156_b;
        int k = (int)this.field_149157_c;
        Iterator iterator = this.field_149155_e.iterator();

        while (iterator.hasNext())
        {
            BlockPos blockpos = (BlockPos)iterator.next();
            int l = blockpos.getX() - i;
            int i1 = blockpos.getY() - j;
            int j1 = blockpos.getZ() - k;
            buf.writeByte(l);
            buf.writeByte(i1);
            buf.writeByte(j1);
        }

        buf.writeFloat(this.field_149152_f);
        buf.writeFloat(this.field_149153_g);
        buf.writeFloat(this.field_149159_h);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleExplosion(this);
    }

    @SideOnly(Side.CLIENT)
    public float func_149149_c()
    {
        return this.field_149152_f;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public float func_149144_d()
    {
        return this.field_149153_g;
    }

    @SideOnly(Side.CLIENT)
    public float func_149147_e()
    {
        return this.field_149159_h;
    }

    @SideOnly(Side.CLIENT)
    public double func_149148_f()
    {
        return this.field_149158_a;
    }

    @SideOnly(Side.CLIENT)
    public double func_149143_g()
    {
        return this.field_149156_b;
    }

    @SideOnly(Side.CLIENT)
    public double func_149145_h()
    {
        return this.field_149157_c;
    }

    @SideOnly(Side.CLIENT)
    public float func_149146_i()
    {
        return this.field_149154_d;
    }

    @SideOnly(Side.CLIENT)
    public List func_149150_j()
    {
        return this.field_149155_e;
    }
}