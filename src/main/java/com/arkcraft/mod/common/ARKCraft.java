package com.arkcraft.mod.common;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.blocks.ARKCraftBlocks;
import com.arkcraft.mod.common.gen.WorldGeneratorBushes;
import com.arkcraft.mod.common.gen.island.IslandGen;
import com.arkcraft.mod.common.gen.island.WorldTypeIsland;
import com.arkcraft.mod.common.handlers.ARKEventHandler;
import com.arkcraft.mod.common.handlers.ARKPlayerEventHandler;
import com.arkcraft.mod.common.handlers.FMLCommonEventHandler;
import com.arkcraft.mod.common.items.ARKCraftItems;
import com.arkcraft.mod.common.lib.Config;
import com.arkcraft.mod.common.network.OpenPlayerCrafting;
import com.arkcraft.mod.common.network.PlayerPoop;
import com.arkcraft.mod.common.network.UpdateMPToCraftItem;
import com.arkcraft.mod.common.network.UpdatePlayerCrafting;
import com.arkcraft.mod.common.network.UpdateSmithyToCraftItem;
import com.arkcraft.mod.common.proxy.CommonProxy;
import net.minecraft.item.Item;
import net.minecraft.world.WorldType;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

@Mod(modid= ARKCraft.MODID, version= ARKCraft.VERSION, name= ARKCraft.MODID, guiFactory = "com.arkcraft.mod.common.lib.ModGuiFactory", dependencies = "required-after:llibrary@[0.4.3]")
public class ARKCraft
{
	public static final String MODID = "arkcraft", VERSION = "${version}", NAME = "ARKCraft";
	@SidedProxy(clientSide="com.arkcraft.mod.client.proxy.ClientProxy", serverSide="com.arkcraft.mod.common.proxy.CommonProxy")
	public static CommonProxy proxy;
	@Instance("arkcraft")
	public static ARKCraft instance;
	public static SimpleNetworkWrapper modChannel;
	public static Logger	modLog;

	public static final WorldType island = new WorldTypeIsland(1);

	public static Map<String, Item> allItems = new HashMap<String, Item>();
	
	public ARKCraft() { instance = this; }
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Create the config first, it is used below
		Config.init(event.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(new Config());
		GameRegistry.registerWorldGenerator(new WorldGeneratorBushes(), 0);

		ARKCraftBlocks.init();
		ARKCraftItems.init();
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
		proxy.registerEventHandlers();		
		proxy.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		IslandGen.loadHeightmap();
		IslandGen.loadBiomemap();
	}

	public static ARKCraft instance() { return instance; }
	
	private void setupNetwork() {
		modChannel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		
		int id = 0;
		// The handler (usually in the packet class), the packet class, unique id, side the packet is received on
		modChannel.registerMessage(PlayerPoop.Handler.class, PlayerPoop.class, id++, Side.SERVER);		
		modChannel.registerMessage(UpdateMPToCraftItem.Handler.class, UpdateMPToCraftItem.class, id++, Side.SERVER);
		modChannel.registerMessage(UpdateSmithyToCraftItem.Handler.class, UpdateSmithyToCraftItem.class, id++, Side.SERVER);
		modChannel.registerMessage(OpenPlayerCrafting.Handler.class, OpenPlayerCrafting.class, id++, Side.SERVER);
		modChannel.registerMessage(UpdatePlayerCrafting.Handler.class, UpdatePlayerCrafting.class, id++, Side.SERVER);
	}
	
	public boolean isDebugger()
	{
		return "${version}".equals("${" + "version" + "}");
	}
}
