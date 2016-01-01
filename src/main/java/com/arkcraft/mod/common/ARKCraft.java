package com.arkcraft.mod.common;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.config.Mod1Config;
import com.arkcraft.mod.common.event.Mod1ARKEventHandler;
import com.arkcraft.mod.common.gen.WorldGeneratorBushes;
import com.arkcraft.mod.common.gen.island.WorldTypeIsland;
import com.arkcraft.mod.common.proxy.CommonProxy;
import com.arkcraft.mod2.common.blocks.ARKCraftBlocks;
import com.arkcraft.mod2.common.config.Mod2Config;
import com.arkcraft.mod2.common.event.ARKPlayerEventHandler;
import com.arkcraft.mod2.common.event.FMLCommonEventHandler;
import com.arkcraft.mod2.common.event.Mod2ARKEventHandler;
import com.arkcraft.mod2.common.items.ARKCraftItems;
import com.arkcraft.mod2.common.items.potions.ARKCraftPotionEffects;
import com.arkcraft.mod2.common.network.ARKMessagePipeline;
import com.arkcraft.mod2.common.network.OpenPlayerCrafting;
import com.arkcraft.mod2.common.network.PlayerPoop;
import com.arkcraft.mod2.common.network.UpdateMPToCraftItem;
import com.arkcraft.mod2.common.network.UpdatePlayerCrafting;
import com.arkcraft.mod2.common.network.UpdateSmithyToCraftItem;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@Mod(modid= ARKCraft.MODID, version= ARKCraft.VERSION, name= ARKCraft.MODID, guiFactory = "com.arkcraft.lib.ModGuiFactory", dependencies = "required-after:llibrary@[0.5.5]")
public class ARKCraft
{
	public static final String MODID = "arkcraft", VERSION = "${version}", NAME = "ARKCraft";
	@SidedProxy(clientSide="com.arkcraft.mod.client.proxy.ClientProxy", serverSide="com.arkcraft.mod.common.proxy.CommonProxy")
	public static CommonProxy proxy;
	@Instance("arkcraft")
	public static ARKCraft instance;
	public static SimpleNetworkWrapper modChannel;
	public static Logger	modLog;
	public ARKMessagePipeline		messagePipeline;

	public static final WorldType island = new WorldTypeIsland();

	public static Map<String, Item> allItems = new HashMap<String, Item>();
	
	public ARKCraft() 
	{
		instance = this; 
		messagePipeline = new ARKMessagePipeline();	
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Create the config first, it is used below
//		Config.init(event.getSuggestedConfigurationFile());
		Mod1Config.init(event.getModConfigurationDirectory());
		FMLCommonHandler.instance().bus().register(new Mod1Config());
		Mod2Config.init(event.getModConfigurationDirectory());
		FMLCommonHandler.instance().bus().register(new Mod2Config());

		GameRegistry.registerWorldGenerator(new WorldGeneratorBushes(), 0);

		ARKCraftBlocks.init();
		ARKCraftItems.init();
		ARKCraftPotionEffects.init();
		GlobalAdditions.init();
		
		Potion[] potionTypes;

		for (Field f : Potion.class.getDeclaredFields()) {
			f.setAccessible(true);

			try {
				if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a")) {
					Field modfield = Field.class.getDeclaredField("modifiers");
					modfield.setAccessible(true);
					modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);
					potionTypes = (Potion[]) f.get(null);
					final Potion[] newPotionTypes = new Potion[256];
					System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
					f.set(null, newPotionTypes);
				}
			} catch (Exception e) {
				System.err.println("(Potions!) Severe error, please report this to the mod author:");
				System.err.println(e);
			}
		}
		
		setupNetwork();	
		modLog = event.getModLog();		
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new Mod1ARKEventHandler());
		MinecraftForge.EVENT_BUS.register(new Mod2ARKEventHandler());
		MinecraftForge.EVENT_BUS.register(new ARKPlayerEventHandler());
		FMLCommonHandler.instance().bus().register(new FMLCommonEventHandler());
		proxy.registerRenderers();
		proxy.registerWeapons();
		proxy.registerEventHandlers();		
		proxy.init();
		messagePipeline.initalize();
		proxy.registerPackets(messagePipeline);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) 
	{
		messagePipeline.postInitialize();
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
