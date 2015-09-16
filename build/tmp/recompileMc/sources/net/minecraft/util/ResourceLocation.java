package net.minecraft.util;

import org.apache.commons.lang3.Validate;

public class ResourceLocation
{
    protected final String resourceDomain;
    protected final String resourcePath;
    private static final String __OBFID = "CL_00001082";

    protected ResourceLocation(int p_i45928_1_, String ... resourcePathIn)
    {
        this.resourceDomain = org.apache.commons.lang3.StringUtils.isEmpty(resourcePathIn[0]) ? "minecraft" : resourcePathIn[0].toLowerCase();
        this.resourcePath = resourcePathIn[1];
        Validate.notNull(this.resourcePath);
    }

    public ResourceLocation(String p_i1293_1_)
    {
        this(0, func_177516_a(p_i1293_1_));
    }

    public ResourceLocation(String p_i1292_1_, String p_i1292_2_)
    {
        this(0, new String[] {p_i1292_1_, p_i1292_2_});
    }

    protected static String[] func_177516_a(String p_177516_0_)
    {
        String[] astring = new String[] {null, p_177516_0_};
        int i = p_177516_0_.indexOf(58);

        if (i >= 0)
        {
            astring[1] = p_177516_0_.substring(i + 1, p_177516_0_.length());

            if (i > 1)
            {
                astring[0] = p_177516_0_.substring(0, i);
            }
        }

        return astring;
    }

    public String getResourcePath()
    {
        return this.resourcePath;
    }

    public String getResourceDomain()
    {
        return this.resourceDomain;
    }

    public String toString()
    {
        return this.resourceDomain + ':' + this.resourcePath;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ResourceLocation))
        {
            return false;
        }
        else
        {
            ResourceLocation resourcelocation = (ResourceLocation)p_equals_1_;
            return this.resourceDomain.equals(resourcelocation.resourceDomain) && this.resourcePath.equals(resourcelocation.resourcePath);
        }
    }

    public int hashCode()
    {
        return 31 * this.resourceDomain.hashCode() + this.resourcePath.hashCode();
    }
}