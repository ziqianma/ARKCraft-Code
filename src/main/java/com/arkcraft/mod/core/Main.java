package com.arkcraft.mod.core;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
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

import org.apache.logging.log4j.Logger;

import com.arkcraft.mod.core.blocks.ModBlocks;
import com.arkcraft.mod.core.handlers.ARKEventHandler;
import com.arkcraft.mod.core.handlers.ARKPlayerEventHandler;
import com.arkcraft.mod.core.handlers.FMLCommonEventHandler;
import com.arkcraft.mod.core.items.ModItems;
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
	public static Logger	modLog;
	
	public static Map<String, Item> allItems = new HashMap<String, Item>();
	
	public Main() { instance = this; }
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Create the config first, it is used below
		Config.init(event.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(new Config());

		ModBlocks.init();
		ModItems.init();
		GlobalAdditions.init();
		
		setupNetwork();	
		modLog = event.getModLog();		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ARKEventHandler());
		MinecraftForge.EVENT_BUS.register(new ARKPlayerEventHandler());
		FMLCommonHandler.instance().bus().register(new FMLCommonEventHandler());
		proxy.registerRenderers();
		proxy.registerWeapons();
		proxy.init();
		proxy.registerEventHandlers();		
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
