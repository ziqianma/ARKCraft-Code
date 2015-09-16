package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S20PacketEntityProperties implements Packet
{
    private int field_149445_a;
    private final List field_149444_b = Lists.newArrayList();
    private static final String __OBFID = "CL_00001341";

    public S20PacketEntityProperties() {}

    public S20PacketEntityProperties(int p_i45236_1_, Collection p_i45236_2_)
    {
        this.field_149445_a = p_i45236_1_;
        Iterator iterator = p_i45236_2_.iterator();

        while (iterator.hasNext())
        {
            IAttributeInstance iattributeinstance = (IAttributeInstance)iterator.next();
            this.field_149444_b.add(new S20PacketEntityProperties.Snapshot(iattributeinstance.getAttribute().getAttributeUnlocalizedName(), iattributeinstance.getBaseValue(), iattributeinstance.func_111122_c()));
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149445_a = buf.readVarIntFromBuffer();
        int i = buf.readInt();

        for (int j = 0; j < i; ++j)
        {
            String s = buf.readStringFromBuffer(64);
            double d0 = buf.readDouble();
            ArrayList arraylist = Lists.newArrayList();
            int k = buf.readVarIntFromBuffer();

            for (int l = 0; l < k; ++l)
            {
                UUID uuid = buf.readUuid();
                arraylist.add(new AttributeModifier(uuid, "Unknown synced attribute modifier", buf.readDouble(), buf.readByte()));
            }

            this.field_149444_b.add(new S20PacketEntityProperties.Snapshot(s, d0, arraylist));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.field_149445_a);
        buf.writeInt(this.field_149444_b.size());
        Iterator iterator = this.field_149444_b.iterator();

        while (iterator.hasNext())
        {
            S20PacketEntityProperties.Snapshot snapshot = (S20PacketEntityProperties.Snapshot)iterator.next();
            buf.writeString(snapshot.func_151409_a());
            buf.writeDouble(snapshot.func_151410_b());
            buf.writeVarIntToBuffer(snapshot.func_151408_c().size());
            Iterator iterator1 = snapshot.func_151408_c().iterator();

            while (iterator1.hasNext())
            {
                AttributeModifier attributemodifier = (AttributeModifier)iterator1.next();
                buf.writeUuid(attributemodifier.getID());
                buf.writeDouble(attributemodifier.getAmount());
                buf.writeByte(attributemodifier.getOperation());
            }
        }
    }

    public void func_180754_a(INetHandlerPlayClient p_180754_1_)
    {
        p_180754_1_.handleEntityProperties(this);
    }

    @SideOnly(Side.CLIENT)
    public int func_149442_c()
    {
        return this.field_149445_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180754_a((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public List func_149441_d()
    {
        return this.field_149444_b;
    }

    public class Snapshot
    {
        private final String field_151412_b;
        private final double field_151413_c;
        private final Collection field_151411_d;
        private static final String __OBFID = "CL_00001342";

        public Snapshot(String p_i45235_2_, double p_i45235_3_, Collection p_i45235_5_)
        {
            this.field_151412_b = p_i45235_2_;
            this.field_151413_c = p_i45235_3_;
            this.field_151411_d = p_i45235_5_;
        }

        public String func_151409_a()
        {
            return this.field_151412_b;
        }

        public double func_151410_b()
        {
            return this.field_151413_c;
        }

        public Collection func_151408_c()
        {
            return this.field_151411_d;
        }
    }
}