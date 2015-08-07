package com.arkcraft.mod.core.proxy;

import com.arkcraft.mod.core.handler.EntityRaptorEventHandler;
import com.arkcraft.mod.lib.LogHelper;

import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

    public void registerPreRenderers() {}
    public void registerRenderers() {}
	public void init() {}
	public void registerSubscriptions() {
		LogHelper.info("CommonProxy: Registering subscriptions");
    	MinecraftForge.EVENT_BUS.register(new EntityRaptorEventHandler());     // For Raptors 
	}	
}
