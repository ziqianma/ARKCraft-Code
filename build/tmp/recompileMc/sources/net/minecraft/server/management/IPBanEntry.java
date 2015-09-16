package net.minecraft.server.management;

import com.google.gson.JsonObject;
import java.util.Date;

public class IPBanEntry extends BanEntry
{
    private static final String __OBFID = "CL_00001883";

    public IPBanEntry(String p_i46330_1_)
    {
        this(p_i46330_1_, (Date)null, (String)null, (Date)null, (String)null);
    }

    public IPBanEntry(String p_i1159_1_, Date startDate, String banner, Date endDate, String p_i1159_5_)
    {
        super(p_i1159_1_, startDate, banner, endDate, p_i1159_5_);
    }

    public IPBanEntry(JsonObject p_i46331_1_)
    {
        super(func_152647_b(p_i46331_1_), p_i46331_1_);
    }

    private static String func_152647_b(JsonObject p_152647_0_)
    {
        return p_152647_0_.has("ip") ? p_152647_0_.get("ip").getAsString() : null;
    }

    protected void onSerialization(JsonObject data)
    {
        if (this.getValue() != null)
        {
            data.addProperty("ip", (String)this.getValue());
            super.onSerialization(data);
        }
    }
}