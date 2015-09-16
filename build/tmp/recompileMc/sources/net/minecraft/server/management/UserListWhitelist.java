package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import java.util.Iterator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UserListWhitelist extends UserList
{
    private static final String __OBFID = "CL_00001871";

    public UserListWhitelist(File p_i1132_1_)
    {
        super(p_i1132_1_);
    }

    protected UserListEntry createEntry(JsonObject entryData)
    {
        return new UserListWhitelistEntry(entryData);
    }

    public String[] getKeys()
    {
        String[] astring = new String[this.getValues().size()];
        int i = 0;
        UserListWhitelistEntry userlistwhitelistentry;

        for (Iterator iterator = this.getValues().values().iterator(); iterator.hasNext(); astring[i++] = ((GameProfile)userlistwhitelistentry.getValue()).getName())
        {
            userlistwhitelistentry = (UserListWhitelistEntry)iterator.next();
        }

        return astring;
    }

    @SideOnly(Side.SERVER)
    public boolean func_152705_a(GameProfile p_152705_1_)
    {
        return this.hasEntry(p_152705_1_);
    }

    protected String func_152704_b(GameProfile p_152704_1_)
    {
        return p_152704_1_.getId().toString();
    }

    public GameProfile func_152706_a(String p_152706_1_)
    {
        Iterator iterator = this.getValues().values().iterator();
        UserListWhitelistEntry userlistwhitelistentry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            userlistwhitelistentry = (UserListWhitelistEntry)iterator.next();
        }
        while (!p_152706_1_.equalsIgnoreCase(((GameProfile)userlistwhitelistentry.getValue()).getName()));

        return (GameProfile)userlistwhitelistentry.getValue();
    }

    /**
     * Gets the key value for the given object
     */
    protected String getObjectKey(Object obj)
    {
        return this.func_152704_b((GameProfile)obj);
    }
}