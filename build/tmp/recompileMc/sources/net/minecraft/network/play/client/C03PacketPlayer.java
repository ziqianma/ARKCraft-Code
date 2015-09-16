package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class C03PacketPlayer implements Packet
{
    protected double x;
    protected double y;
    protected double z;
    protected float yaw;
    protected float pitch;
    protected boolean onGround;
    protected boolean moving;
    protected boolean rotating;
    private static final String __OBFID = "CL_00001360";

    public C03PacketPlayer() {}

    @SideOnly(Side.CLIENT)
    public C03PacketPlayer(boolean isOnGround)
    {
        this.onGround = isOnGround;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlayer(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.onGround = buf.readUnsignedByte() != 0;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.onGround ? 1 : 0);
    }

    public double getPositionX()
    {
        return this.x;
    }

    public double getPositionY()
    {
        return this.y;
    }

    public double getPositionZ()
    {
        return this.z;
    }

    public float getYaw()
    {
        return this.yaw;
    }

    public float getPitch()
    {
        return this.pitch;
    }

    public boolean isOnGround()
    {
        return this.onGround;
    }

    public boolean isMoving()
    {
        return this.moving;
    }

    public boolean getRotating()
    {
        return this.rotating;
    }

    public void setMoving(boolean isMoving)
    {
        this.moving = isMoving;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayServer)handler);
    }

    public static class C04PacketPlayerPosition extends C03PacketPlayer
        {
            private static final String __OBFID = "CL_00001361";

            public C04PacketPlayerPosition()
            {
                this.moving = true;
            }

            @SideOnly(Side.CLIENT)
            public C04PacketPlayerPosition(double p_i45942_1_, double p_i45942_3_, double p_i45942_5_, boolean p_i45942_7_)
            {
                this.x = p_i45942_1_;
                this.y = p_i45942_3_;
                this.z = p_i45942_5_;
                this.onGround = p_i45942_7_;
                this.moving = true;
            }

            /**
             * Reads the raw packet data from the data stream.
             */
            public void readPacketData(PacketBuffer buf) throws IOException
            {
                this.x = buf.readDouble();
                this.y = buf.readDouble();
                this.z = buf.readDouble();
                super.readPacketData(buf);
            }

            /**
             * Writes the raw packet data to the data stream.
             */
            public void writePacketData(PacketBuffer buf) throws IOException
            {
                buf.writeDouble(this.x);
                buf.writeDouble(this.y);
                buf.writeDouble(this.z);
                super.writePacketData(buf);
            }

            /**
             * Passes this Packet on to the NetHandler for processing.
             */
            public void processPacket(INetHandler handler)
            {
                super.processPacket((INetHandlerPlayServer)handler);
            }
        }

    public static class C05PacketPlayerLook extends C03PacketPlayer
        {
            private static final String __OBFID = "CL_00001363";

            public C05PacketPlayerLook()
            {
                this.rotating = true;
            }

            @SideOnly(Side.CLIENT)
            public C05PacketPlayerLook(float p_i45255_1_, float p_i45255_2_, boolean p_i45255_3_)
            {
                this.yaw = p_i45255_1_;
                this.pitch = p_i45255_2_;
                this.onGround = p_i45255_3_;
                this.rotating = true;
            }

            /**
             * Reads the raw packet data from the data stream.
             */
            public void readPacketData(PacketBuffer buf) throws IOException
            {
                this.yaw = buf.readFloat();
                this.pitch = buf.readFloat();
                super.readPacketData(buf);
            }

            /**
             * Writes the raw packet data to the data stream.
             */
            public void writePacketData(PacketBuffer buf) throws IOException
            {
                buf.writeFloat(this.yaw);
                buf.writeFloat(this.pitch);
                super.writePacketData(buf);
            }

            /**
             * Passes this Packet on to the NetHandler for processing.
             */
            public void processPacket(INetHandler handler)
            {
                super.processPacket((INetHandlerPlayServer)handler);
            }
        }

    public static class C06PacketPlayerPosLook extends C03PacketPlayer
        {
            private static final String __OBFID = "CL_00001362";

            public C06PacketPlayerPosLook()
            {
                this.moving = true;
                this.rotating = true;
            }

            @SideOnly(Side.CLIENT)
            public C06PacketPlayerPosLook(double p_i45941_1_, double p_i45941_3_, double p_i45941_5_, float p_i45941_7_, float p_i45941_8_, boolean p_i45941_9_)
            {
                this.x = p_i45941_1_;
                this.y = p_i45941_3_;
                this.z = p_i45941_5_;
                this.yaw = p_i45941_7_;
                this.pitch = p_i45941_8_;
                this.onGround = p_i45941_9_;
                this.rotating = true;
                this.moving = true;
            }

            /**
             * Reads the raw packet data from the data stream.
             */
            public void readPacketData(PacketBuffer buf) throws IOException
            {
                this.x = buf.readDouble();
                this.y = buf.readDouble();
                this.z = buf.readDouble();
                this.yaw = buf.readFloat();
                this.pitch = buf.readFloat();
                super.readPacketData(buf);
            }

            /**
             * Writes the raw packet data to the data stream.
             */
            public void writePacketData(PacketBuffer buf) throws IOException
            {
                buf.writeDouble(this.x);
                buf.writeDouble(this.y);
                buf.writeDouble(this.z);
                buf.writeFloat(this.yaw);
                buf.writeFloat(this.pitch);
                super.writePacketData(buf);
            }

            /**
             * Passes this Packet on to the NetHandler for processing.
             */
            public void processPacket(INetHandler handler)
            {
                super.processPacket((INetHandlerPlayServer)handler);
            }
        }
}