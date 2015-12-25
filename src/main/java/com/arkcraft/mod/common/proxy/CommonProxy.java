package com.arkcraft.mod.common.proxy;

import com.arkcraft.mod.client.gui.book.proxy.DCommon;
import com.arkcraft.mod2.common.event.CommonEventHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy {

	@SidedProxy(clientSide="com.arkcraft.mod.client.gui.book.proxy.DClient", serverSide="com.arkcraft.mod.client.gui.book.proxy.DCommon")
	public static DCommon dossierProxy;

	public CommonProxy() {}
    public void registerPreRenderers() {}
    public void registerRenderers() {}
	public void init() {}
	public void registerWeapons() {}
	public void registerEventHandlers(){MinecraftForge.EVENT_BUS.register(new CommonEventHandler());}

	public EntityPlayer getPlayer()
	{
		return null;
	}
}
