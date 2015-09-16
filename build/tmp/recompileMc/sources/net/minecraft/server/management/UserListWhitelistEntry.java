package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.util.UUID;

public class UserListWhitelistEntry extends UserListEntry
{
    private static final String __OBFID = "CL_00001870";

    public UserListWhitelistEntry(GameProfile profile)
    {
        super(profile);
    }

    public UserListWhitelistEntry(JsonObject p_i1130_1_)
    {
        super(func_152646_b(p_i1130_1_), p_i1130_1_);
    }

    protected void onSerialization(JsonObject data)
    {
        if (this.getValue() != null)
        {
            data.addProperty("uuid", ((GameProfile)this.getValue()).getId() == null ? "" : ((GameProfile)this.getValue()).getId().toString());
            data.addProperty("name", ((GameProfile)this.getValue()).getName());
            super.onSerialization(data);
        }
    }

    private static GameProfile func_152646_b(JsonObject p_152646_0_)
    {
        if (p_152646_0_.has("uuid") && p_152646_0_.has("name"))
        {
            String s = p_152646_0_.get("uuid").getAsString();
            UUID uuid;

            try
            {
                uuid = UUID.fromString(s);
            }
            catch (Throwable throwable)
            {
                return null;
            }

            return new GameProfile(uuid, p_152646_0_.get("name").getAsString());
        }
        else
        {
            return null;
        }
    }
}