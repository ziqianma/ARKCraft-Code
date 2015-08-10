package com.arkcraft.mod.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import com.arkcraft.mod.core.handler.ARKEventHandler;
import com.arkcraft.mod.core.proxy.CommonProxy;

@Mod(modid=Main.MODID, version=Main.VERSION, name=Main.MODID)
public class Main {
	
	public static final String MODID = "arkcraft", VERSION = "0.1 Alpha";
	@SidedProxy(clientSide="com.arkcraft.mod.core.proxy.ClientProxy", serverSide="com.arkcraft.mod.core.proxy.CommonProxy")
	public static CommonProxy proxy;
	@Instance("arkcraft")
	public static Main instance;
	
	public Main() { instance = this; }
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		GlobalAdditions.init();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ARKEventHandler());
		proxy.init();
		proxy.registerRenderers();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
	public static Main instance() { return instance; }
}
