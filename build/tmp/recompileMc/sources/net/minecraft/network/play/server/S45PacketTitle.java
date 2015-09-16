package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S45PacketTitle implements Packet
{
    private S45PacketTitle.Type type;
    private IChatComponent message;
    private int fadeInTime;
    private int displayTime;
    private int fadeOutTime;
    private static final String __OBFID = "CL_00002287";

    public S45PacketTitle() {}

    public S45PacketTitle(S45PacketTitle.Type type, IChatComponent message)
    {
        this(type, message, -1, -1, -1);
    }

    public S45PacketTitle(int fadeInTime, int displayTime, int fadeOutTime)
    {
        this(S45PacketTitle.Type.TIMES, (IChatComponent)null, fadeInTime, displayTime, fadeOutTime);
    }

    public S45PacketTitle(S45PacketTitle.Type type, IChatComponent message, int fadeInTime, int displayTime, int fadeOutTime)
    {
        this.type = type;
        this.message = message;
        this.fadeInTime = fadeInTime;
        this.displayTime = displayTime;
        this.fadeOutTime = fadeOutTime;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.type = (S45PacketTitle.Type)buf.readEnumValue(S45PacketTitle.Type.class);

        if (this.type == S45PacketTitle.Type.TITLE || this.type == S45PacketTitle.Type.SUBTITLE)
        {
            this.message = buf.readChatComponent();
        }

        if (this.type == S45PacketTitle.Type.TIMES)
        {
            this.fadeInTime = buf.readInt();
            this.displayTime = buf.readInt();
            this.fadeOutTime = buf.readInt();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(this.type);

        if (this.type == S45PacketTitle.Type.TITLE || this.type == S45PacketTitle.Type.SUBTITLE)
        {
            buf.writeChatComponent(this.message);
        }

        if (this.type == S45PacketTitle.Type.TIMES)
        {
            buf.writeInt(this.fadeInTime);
            buf.writeInt(this.displayTime);
            buf.writeInt(this.fadeOutTime);
        }
    }

    public void process(INetHandlerPlayClient handler)
    {
        handler.handleTitle(this);
    }

    @SideOnly(Side.CLIENT)
    public S45PacketTitle.Type func_179807_a()
    {
        return this.type;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.process((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public IChatComponent func_179805_b()
    {
        return this.message;
    }

    @SideOnly(Side.CLIENT)
    public int func_179806_c()
    {
        return this.fadeInTime;
    }

    @SideOnly(Side.CLIENT)
    public int func_179804_d()
    {
        return this.displayTime;
    }

    @SideOnly(Side.CLIENT)
    public int func_179803_e()
    {
        return this.fadeOutTime;
    }

    public static enum Type
    {
        TITLE,
        SUBTITLE,
        TIMES,
        CLEAR,
        RESET;

        private static final String __OBFID = "CL_00002286";

        public static S45PacketTitle.Type byName(String name)
        {
            S45PacketTitle.Type[] atype = values();
            int i = atype.length;

            for (int j = 0; j < i; ++j)
            {
                S45PacketTitle.Type type = atype[j];

                if (type.name().equalsIgnoreCase(name))
                {
                    return type;
                }
            }

            return TITLE;
        }

        public static String[] getNames()
        {
            String[] astring = new String[values().length];
            int i = 0;
            S45PacketTitle.Type[] atype = values();
            int j = atype.length;

            for (int k = 0; k < j; ++k)
            {
                S45PacketTitle.Type type = atype[k];
                astring[i++] = type.name().toLowerCase();
            }

            return astring;
        }
    }
}