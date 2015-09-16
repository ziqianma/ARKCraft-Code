package net.minecraft.realms;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RealmsServerPing
{
    public volatile String nrOfPlayers = "0";
    public volatile long lastPingSnapshot = 0L;
    private static final String __OBFID = "CL_00002328";
}