package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;

public class UserListBans extends UserList
{
    private static final String __OBFID = "CL_00001873";

    public UserListBans(File bansFile)
    {
        super(bansFile);
    }

    protected UserListEntry createEntry(JsonObject entryData)
    {
        return new UserListBansEntry(entryData);
    }

    public boolean isBanned(GameProfile profile)
    {
        return this.hasEntry(profile);
    }

    public String[] getKeys()
    {
        String[] astring = new String[this.getValues().size()];
        int i = 0;
        UserListBansEntry userlistbansentry;

        for (Iterator iterator = this.getValues().values().iterator(); iterator.hasNext(); astring[i++] = ((GameProfile)userlistbansentry.getValue()).getName())
        {
            userlistbansentry = (UserListBansEntry)iterator.next();
        }

        return astring;
    }

    protected String getProfileId(GameProfile profile)
    {
        return profile.getId().toString();
    }

    public GameProfile isUsernameBanned(String username)
    {
        Iterator iterator = this.getValues().values().iterator();
        UserListBansEntry userlistbansentry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            userlistbansentry = (UserListBansEntry)iterator.next();
        }
        while (!username.equalsIgnoreCase(((GameProfile)userlistbansentry.getValue()).getName()));

        return (GameProfile)userlistbansentry.getValue();
    }

    /**
     * Gets the key value for the given object
     */
    protected String getObjectKey(Object obj)
    {
        return this.getProfileId((GameProfile)obj);
    }
}