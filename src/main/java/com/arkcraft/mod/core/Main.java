package com.arkcraft.mod.core;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.arkcraft.mod.core.entity.EntityCobble;
import com.arkcraft.mod.core.handler.GuiHandler;
import com.arkcraft.mod.core.proxy.CommonProxy;

@Mod(modid=Main.MODID, version=Main.VERSION, name=Main.MODID)
public class Main {
	
	public static final String MODID = "arkcraft", VERSION = "0.1 Alpha";
	@SidedProxy(clientSide="com.arkcraft.mod.core.proxy.ClientProxy", serverSide="com.arkcraft.mod.core.proxy.CommonProxy")
	public static CommonProxy proxy;
	public static Main instance;
	
	public Main() { instance = this; }
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		GlobalAdditions.init();
		EntityRegistry.registerModEntity(EntityCobble.class, "Cobblestone Ball", 1, this, 64, 10, true);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init();
		proxy.registerRenderers();
		new GuiHandler();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
	public static Main instance() { return instance; }
}
