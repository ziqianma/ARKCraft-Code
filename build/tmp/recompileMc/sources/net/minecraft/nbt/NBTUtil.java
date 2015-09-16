package net.minecraft.nbt;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.util.StringUtils;

public final class NBTUtil
{
    private static final String __OBFID = "CL_00001901";

    /**
     * Reads and returns a GameProfile that has been saved to the passed in NBTTagCompound
     */
    public static GameProfile readGameProfileFromNBT(NBTTagCompound compound)
    {
        String s = null;
        String s1 = null;

        if (compound.hasKey("Name", 8))
        {
            s = compound.getString("Name");
        }

        if (compound.hasKey("Id", 8))
        {
            s1 = compound.getString("Id");
        }

        if (StringUtils.isNullOrEmpty(s) && StringUtils.isNullOrEmpty(s1))
        {
            return null;
        }
        else
        {
            UUID uuid;

            try
            {
                uuid = UUID.fromString(s1);
            }
            catch (Throwable throwable)
            {
                uuid = null;
            }

            GameProfile gameprofile = new GameProfile(uuid, s);

            if (compound.hasKey("Properties", 10))
            {
                NBTTagCompound nbttagcompound1 = compound.getCompoundTag("Properties");
                Iterator iterator = nbttagcompound1.getKeySet().iterator();

                while (iterator.hasNext())
                {
                    String s2 = (String)iterator.next();
                    NBTTagList nbttaglist = nbttagcompound1.getTagList(s2, 10);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i)
                    {
                        NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(i);
                        String s3 = nbttagcompound2.getString("Value");

                        if (nbttagcompound2.hasKey("Signature", 8))
                        {
                            gameprofile.getProperties().put(s2, new Property(s2, s3, nbttagcompound2.getString("Signature")));
                        }
                        else
                        {
                            gameprofile.getProperties().put(s2, new Property(s2, s3));
                        }
                    }
                }
            }

            return gameprofile;
        }
    }

    /**
     * Writes a GameProfile to an NBTTagCompound.
     *  
     * @param tagCompound The NBTTagCompound to write the GameProfile to.
     * @param profile The game profile you are saving.
     */
    public static NBTTagCompound writeGameProfile(NBTTagCompound tagCompound, GameProfile profile)
    {
        if (!StringUtils.isNullOrEmpty(profile.getName()))
        {
            tagCompound.setString("Name", profile.getName());
        }

        if (profile.getId() != null)
        {
            tagCompound.setString("Id", profile.getId().toString());
        }

        if (!profile.getProperties().isEmpty())
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            Iterator iterator = profile.getProperties().keySet().iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                NBTTagList nbttaglist = new NBTTagList();
                NBTTagCompound nbttagcompound2;

                for (Iterator iterator1 = profile.getProperties().get(s).iterator(); iterator1.hasNext(); nbttaglist.appendTag(nbttagcompound2))
                {
                    Property property = (Property)iterator1.next();
                    nbttagcompound2 = new NBTTagCompound();
                    nbttagcompound2.setString("Value", property.getValue());

                    if (property.hasSignature())
                    {
                        nbttagcompound2.setString("Signature", property.getSignature());
                    }
                }

                nbttagcompound1.setTag(s, nbttaglist);
            }

            tagCompound.setTag("Properties", nbttagcompound1);
        }

        return tagCompound;
    }
}