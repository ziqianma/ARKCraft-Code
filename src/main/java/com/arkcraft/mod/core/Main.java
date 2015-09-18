package com.arkcraft.mod.core;

import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import com.arkcraft.mod.core.handler.ARKEventHandler;
import com.arkcraft.mod.core.handler.ARKPlayerEventHandler;
import com.arkcraft.mod.core.handler.FMLCommonEventHandler;
import com.arkcraft.mod.core.items.weapons.handlers.WeaponModConfig;
import com.arkcraft.mod.core.lib.Config;
import com.arkcraft.mod.core.network.PlayerPoop;
import com.arkcraft.mod.core.proxy.CommonProxy;

@Mod(modid=Main.MODID, version=Main.VERSION, name=Main.MODID,
	guiFactory = "com.arkcraft.mod.core.lib.ModGuiFactory")
public class Main {
	
	public static final String MODID = "arkcraft", VERSION = "0.1 Alpha", NAME = "ARKCraft";
	@SidedProxy(clientSide="com.arkcraft.mod.core.proxy.ClientProxy", serverSide="com.arkcraft.mod.core.proxy.CommonProxy")
	public static CommonProxy proxy;
	@Instance("arkcraft")
	public static Main instance;
	public static SimpleNetworkWrapper modChannel;
	public Config modConfig;
	public WeaponModConfig WeaponConfig;
	public static Logger	modLog;
	
	public Main() { instance = this; }
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.init(event.getSuggestedConfigurationFile());// Keep first
		GlobalAdditions.init();
		setupNetwork();	
		modLog = event.getModLog();
		
		WeaponConfig = new WeaponModConfig(new Configuration(event.getSuggestedConfigurationFile()));
		WeaponConfig.addReloadTimeSetting("simple_pistol", 30);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ARKEventHandler());
		MinecraftForge.EVENT_BUS.register(new ARKPlayerEventHandler());
		FMLCommonHandler.instance().bus().register(new FMLCommonEventHandler());
		proxy.registerRenderers();
		proxy.init();
		FMLCommonHandler.instance().bus().register(new Config());

	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
	public static Main instance() { return instance; }
	
	private void setupNetwork() {
		modChannel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		
		int id = 0;
		modChannel.registerMessage(PlayerPoop.Handler.class, PlayerPoop.class, id++, Side.SERVER);
	}
}
