package net.minecraft.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import java.util.Map;
import java.util.UUID;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Session
{
    private final String username;
    private final String playerID;
    private final String token;
    private final Session.Type sessionType;
    private static final String __OBFID = "CL_00000659";
    /** Forge: Cache of the local session's GameProfile properties. */
    private com.mojang.authlib.properties.PropertyMap properties;

    public Session(String usernameIn, String playerIDIn, String tokenIn, String sessionTypeIn)
    {
        if (usernameIn == null || usernameIn.isEmpty())
        {
            usernameIn = "MissingName";
            playerIDIn = tokenIn = "NotValid";
            System.out.println("=========================================================");
            System.out.println("Warning the username was not set for this session, typically");
            System.out.println("this means you installed Forge incorrectly. We have set your");
            System.out.println("name to \"MissingName\" and your session to nothing. Please");
            System.out.println("check your instllation and post a console log from the launcher");
            System.out.println("when asking for help!");
            System.out.println("=========================================================");
        }

        this.username = usernameIn;
        this.playerID = playerIDIn;
        this.token = tokenIn;
        this.sessionType = Session.Type.setSessionType(sessionTypeIn);
    }

    public String getSessionID()
    {
        return "token:" + this.token + ":" + this.playerID;
    }

    public String getPlayerID()
    {
        return this.playerID;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getToken()
    {
        return this.token;
    }

    public GameProfile getProfile()
    {
        try
        {
            UUID uuid = UUIDTypeAdapter.fromString(this.getPlayerID());
            GameProfile ret = new GameProfile(uuid, this.getUsername());    //Forge: Adds cached GameProfile properties to returned GameProfile.
            if (properties != null) ret.getProperties().putAll(properties); // Helps to cut down on calls to the session service,
            return ret;                                                     // which helps to fix MC-52974.
        }
        catch (IllegalArgumentException illegalargumentexception)
        {
            return new GameProfile(net.minecraft.entity.player.EntityPlayer.getUUID(new GameProfile((UUID)null, this.getUsername())), this.getUsername());
        }
    }

    /**
     * Returns either 'legacy' or 'mojang' whether the account is migrated or not
     */
    public Session.Type getSessionType()
    {
        return this.sessionType;
    }

    /* ======================================== FORGE START ===================================== */
    //For internal use only. Modders should never need to use this.
    public void setProperties(com.mojang.authlib.properties.PropertyMap properties)
    {
        if(this.properties == null) this.properties = properties;
    }

    public boolean hasCachedProperties()
    {
        return properties != null;
    }
    /* ========================================= FORGE END ====================================== */

    @SideOnly(Side.CLIENT)
    public static enum Type
    {
        LEGACY("legacy"),
        MOJANG("mojang");
        private static final Map SESSION_TYPES = Maps.newHashMap();
        private final String sessionType;

        private static final String __OBFID = "CL_00001851";

        private Type(String sessionTypeIn)
        {
            this.sessionType = sessionTypeIn;
        }

        public static Session.Type setSessionType(String sessionTypeIn)
        {
            return (Session.Type)SESSION_TYPES.get(sessionTypeIn.toLowerCase());
        }

        static
        {
            Session.Type[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                Session.Type var3 = var0[var2];
                SESSION_TYPES.put(var3.sessionType, var3);
            }
        }
    }
}