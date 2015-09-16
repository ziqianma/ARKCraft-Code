package net.minecraft.realms;

import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RealmsServerAddress
{
    private final String host;
    private final int port;
    private static final String __OBFID = "CL_00001864";

    protected RealmsServerAddress(String p_i1121_1_, int p_i1121_2_)
    {
        this.host = p_i1121_1_;
        this.port = p_i1121_2_;
    }

    public String getHost()
    {
        return this.host;
    }

    public int getPort()
    {
        return this.port;
    }

    public static RealmsServerAddress parseString(String p_parseString_0_)
    {
        ServerAddress serveraddress = ServerAddress.func_78860_a(p_parseString_0_);
        return new RealmsServerAddress(serveraddress.getIP(), serveraddress.getPort());
    }
}