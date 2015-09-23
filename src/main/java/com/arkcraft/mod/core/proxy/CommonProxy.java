package com.arkcraft.mod.core.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.arkcraft.mod.core.items.weapons.handlers.WeaponModConfig;


public class CommonProxy {

	public CommonProxy() {}
    public void registerPreRenderers() {}
    public void registerRenderers() {}
	public void init() {}
	public void registerWeapons(WeaponModConfig config)	{}
	public void registerEventHandlers(){MinecraftForge.EVENT_BUS.register(new CommonEventHandler());}

}
