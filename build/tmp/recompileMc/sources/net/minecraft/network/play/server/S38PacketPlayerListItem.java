package net.minecraft.network.play.server;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class S38PacketPlayerListItem implements Packet
{
    private S38PacketPlayerListItem.Action field_179770_a;
    private final List field_179769_b = Lists.newArrayList();
    private static final String __OBFID = "CL_00001318";

    public S38PacketPlayerListItem() {}

    public S38PacketPlayerListItem(S38PacketPlayerListItem.Action p_i45967_1_, EntityPlayerMP ... p_i45967_2_)
    {
        this.field_179770_a = p_i45967_1_;
        EntityPlayerMP[] aentityplayermp = p_i45967_2_;
        int i = p_i45967_2_.length;

        for (int j = 0; j < i; ++j)
        {
            EntityPlayerMP entityplayermp1 = aentityplayermp[j];
            this.field_179769_b.add(new S38PacketPlayerListItem.AddPlayerData(entityplayermp1.getGameProfile(), entityplayermp1.ping, entityplayermp1.theItemInWorldManager.getGameType(), entityplayermp1.getTabListDisplayName()));
        }
    }

    public S38PacketPlayerListItem(S38PacketPlayerListItem.Action p_i45968_1_, Iterable p_i45968_2_)
    {
        this.field_179770_a = p_i45968_1_;
        Iterator iterator = p_i45968_2_.iterator();

        while (iterator.hasNext())
        {
            EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();
            this.field_179769_b.add(new S38PacketPlayerListItem.AddPlayerData(entityplayermp.getGameProfile(), entityplayermp.ping, entityplayermp.theItemInWorldManager.getGameType(), entityplayermp.getTabListDisplayName()));
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_179770_a = (S38PacketPlayerListItem.Action)buf.readEnumValue(S38PacketPlayerListItem.Action.class);
        int i = buf.readVarIntFromBuffer();

        for (int j = 0; j < i; ++j)
        {
            GameProfile gameprofile = null;
            int k = 0;
            WorldSettings.GameType gametype = null;
            IChatComponent ichatcomponent = null;

            switch (S38PacketPlayerListItem.SwitchAction.field_179938_a[this.field_179770_a.ordinal()])
            {
                case 1:
                    gameprofile = new GameProfile(buf.readUuid(), buf.readStringFromBuffer(16));
                    int l = buf.readVarIntFromBuffer();

                    for (int i1 = 0; i1 < l; ++i1)
                    {
                        String s = buf.readStringFromBuffer(32767);
                        String s1 = buf.readStringFromBuffer(32767);

                        if (buf.readBoolean())
                        {
                            gameprofile.getProperties().put(s, new Property(s, s1, buf.readStringFromBuffer(32767)));
                        }
                        else
                        {
                            gameprofile.getProperties().put(s, new Property(s, s1));
                        }
                    }

                    gametype = WorldSettings.GameType.getByID(buf.readVarIntFromBuffer());
                    k = buf.readVarIntFromBuffer();

                    if (buf.readBoolean())
                    {
                        ichatcomponent = buf.readChatComponent();
                    }

                    break;
                case 2:
                    gameprofile = new GameProfile(buf.readUuid(), (String)null);
                    gametype = WorldSettings.GameType.getByID(buf.readVarIntFromBuffer());
                    break;
                case 3:
                    gameprofile = new GameProfile(buf.readUuid(), (String)null);
                    k = buf.readVarIntFromBuffer();
                    break;
                case 4:
                    gameprofile = new GameProfile(buf.readUuid(), (String)null);

                    if (buf.readBoolean())
                    {
                        ichatcomponent = buf.readChatComponent();
                    }

                    break;
                case 5:
                    gameprofile = new GameProfile(buf.readUuid(), (String)null);
            }

            this.field_179769_b.add(new S38PacketPlayerListItem.AddPlayerData(gameprofile, k, gametype, ichatcomponent));
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(this.field_179770_a);
        buf.writeVarIntToBuffer(this.field_179769_b.size());
        Iterator iterator = this.field_179769_b.iterator();

        while (iterator.hasNext())
        {
            S38PacketPlayerListItem.AddPlayerData addplayerdata = (S38PacketPlayerListItem.AddPlayerData)iterator.next();

            switch (S38PacketPlayerListItem.SwitchAction.field_179938_a[this.field_179770_a.ordinal()])
            {
                case 1:
                    buf.writeUuid(addplayerdata.func_179962_a().getId());
                    buf.writeString(addplayerdata.func_179962_a().getName());
                    buf.writeVarIntToBuffer(addplayerdata.func_179962_a().getProperties().size());
                    Iterator iterator1 = addplayerdata.func_179962_a().getProperties().values().iterator();

                    while (iterator1.hasNext())
                    {
                        Property property = (Property)iterator1.next();
                        buf.writeString(property.getName());
                        buf.writeString(property.getValue());

                        if (property.hasSignature())
                        {
                            buf.writeBoolean(true);
                            buf.writeString(property.getSignature());
                        }
                        else
                        {
                            buf.writeBoolean(false);
                        }
                    }

                    buf.writeVarIntToBuffer(addplayerdata.func_179960_c().getID());
                    buf.writeVarIntToBuffer(addplayerdata.func_179963_b());

                    if (addplayerdata.func_179961_d() == null)
                    {
                        buf.writeBoolean(false);
                    }
                    else
                    {
                        buf.writeBoolean(true);
                        buf.writeChatComponent(addplayerdata.func_179961_d());
                    }

                    break;
                case 2:
                    buf.writeUuid(addplayerdata.func_179962_a().getId());
                    buf.writeVarIntToBuffer(addplayerdata.func_179960_c().getID());
                    break;
                case 3:
                    buf.writeUuid(addplayerdata.func_179962_a().getId());
                    buf.writeVarIntToBuffer(addplayerdata.func_179963_b());
                    break;
                case 4:
                    buf.writeUuid(addplayerdata.func_179962_a().getId());

                    if (addplayerdata.func_179961_d() == null)
                    {
                        buf.writeBoolean(false);
                    }
                    else
                    {
                        buf.writeBoolean(true);
                        buf.writeChatComponent(addplayerdata.func_179961_d());
                    }

                    break;
                case 5:
                    buf.writeUuid(addplayerdata.func_179962_a().getId());
            }
        }
    }

    public void func_180743_a(INetHandlerPlayClient p_180743_1_)
    {
        p_180743_1_.handlePlayerListItem(this);
    }

    @SideOnly(Side.CLIENT)
    public List func_179767_a()
    {
        return this.field_179769_b;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180743_a((INetHandlerPlayClient)handler);
    }

    @SideOnly(Side.CLIENT)
    public S38PacketPlayerListItem.Action func_179768_b()
    {
        return this.field_179770_a;
    }

    public static enum Action
    {
        ADD_PLAYER,
        UPDATE_GAME_MODE,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        REMOVE_PLAYER;

        private static final String __OBFID = "CL_00002295";
    }

    public class AddPlayerData
    {
        private final int field_179966_b;
        private final WorldSettings.GameType field_179967_c;
        private final GameProfile field_179964_d;
        private final IChatComponent field_179965_e;
        private static final String __OBFID = "CL_00002294";

        public AddPlayerData(GameProfile p_i45965_2_, int p_i45965_3_, WorldSettings.GameType p_i45965_4_, IChatComponent p_i45965_5_)
        {
            this.field_179964_d = p_i45965_2_;
            this.field_179966_b = p_i45965_3_;
            this.field_179967_c = p_i45965_4_;
            this.field_179965_e = p_i45965_5_;
        }

        public GameProfile func_179962_a()
        {
            return this.field_179964_d;
        }

        public int func_179963_b()
        {
            return this.field_179966_b;
        }

        public WorldSettings.GameType func_179960_c()
        {
            return this.field_179967_c;
        }

        public IChatComponent func_179961_d()
        {
            return this.field_179965_e;
        }
    }

    static final class SwitchAction
        {
            static final int[] field_179938_a = new int[S38PacketPlayerListItem.Action.values().length];
            private static final String __OBFID = "CL_00002296";

            static
            {
                try
                {
                    field_179938_a[S38PacketPlayerListItem.Action.ADD_PLAYER.ordinal()] = 1;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                try
                {
                    field_179938_a[S38PacketPlayerListItem.Action.UPDATE_GAME_MODE.ordinal()] = 2;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    field_179938_a[S38PacketPlayerListItem.Action.UPDATE_LATENCY.ordinal()] = 3;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_179938_a[S38PacketPlayerListItem.Action.UPDATE_DISPLAY_NAME.ordinal()] = 4;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_179938_a[S38PacketPlayerListItem.Action.REMOVE_PLAYER.ordinal()] = 5;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}