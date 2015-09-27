package com.arkcraft.mod.core.proxy;

import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public CommonProxy() {}
    public void registerPreRenderers() {}
    public void registerRenderers() {}
	public void init() {}
	public void registerWeapons() {}
	public void registerEventHandlers(){MinecraftForge.EVENT_BUS.register(new CommonEventHandler());}

}
