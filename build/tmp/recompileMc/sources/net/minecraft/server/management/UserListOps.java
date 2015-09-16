package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListOps extends UserList
{
    private static final String __OBFID = "CL_00001879";

    public UserListOps(File p_i1152_1_)
    {
        super(p_i1152_1_);
    }

    protected UserListEntry createEntry(JsonObject entryData)
    {
        return new UserListOpsEntry(entryData);
    }

    public String[] getKeys()
    {
        String[] astring = new String[this.getValues().size()];
        int i = 0;
        UserListOpsEntry userlistopsentry;

        for (Iterator iterator = this.getValues().values().iterator(); iterator.hasNext(); astring[i++] = ((GameProfile)userlistopsentry.getValue()).getName())
        {
            userlistopsentry = (UserListOpsEntry)iterator.next();
        }

        return astring;
    }

    protected String func_152699_b(GameProfile p_152699_1_)
    {
        return p_152699_1_.getId().toString();
    }

    /**
     * Gets the GameProfile of based on the provided username.
     */
    public GameProfile getGameProfileFromName(String p_152700_1_)
    {
        Iterator iterator = this.getValues().values().iterator();
        UserListOpsEntry userlistopsentry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            userlistopsentry = (UserListOpsEntry)iterator.next();
        }
        while (!p_152700_1_.equalsIgnoreCase(((GameProfile)userlistopsentry.getValue()).getName()));

        return (GameProfile)userlistopsentry.getValue();
    }

    /**
     * Gets the key value for the given object
     */
    protected String getObjectKey(Object obj)
    {
        return this.func_152699_b((GameProfile)obj);
    }
}