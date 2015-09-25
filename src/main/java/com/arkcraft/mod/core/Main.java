package com.arkcraft.mod.core;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
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

import org.apache.logging.log4j.Logger;

import com.arkcraft.mod.core.handlers.ARKEventHandler;
import com.arkcraft.mod.core.handlers.ARKPlayerEventHandler;
import com.arkcraft.mod.core.handlers.EntityHandler;
import com.arkcraft.mod.core.handlers.FMLCommonEventHandler;
import com.arkcraft.mod.core.items.weapons.ItemLongneckRifle;
import com.arkcraft.mod.core.items.weapons.ItemShotgun;
import com.arkcraft.mod.core.items.weapons.ItemSimplePistol;
import com.arkcraft.mod.core.items.weapons.bullets.ItemProjectile;
import com.arkcraft.mod.core.items.weapons.handlers.WeaponModConfig;
import com.arkcraft.mod.core.items.weapons.projectiles.EntitySimpleBullet;
import com.arkcraft.mod.core.items.weapons.projectiles.EntitySimpleRifleAmmo;
import com.arkcraft.mod.core.items.weapons.projectiles.EntitySimpleShotgunAmmo;
import com.arkcraft.mod.core.items.weapons.projectiles.dispense.DispenseSimpleBullet;
import com.arkcraft.mod.core.items.weapons.projectiles.dispense.DispenseSimpleRifleAmmo;
import com.arkcraft.mod.core.items.weapons.projectiles.dispense.DispenseSimpleShotgunAmmo;
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
	
	public static Map<String, Item> allItems = new HashMap<String, Item>();
	
	public static ItemProjectile simple_bullet, simple_rifle_ammo, simple_shotgun_ammo;
	public static ItemSimplePistol simple_pistol;
	public static ItemLongneckRifle longneck_rifle;
	public static ItemShotgun shotgun;
	
	public Main() { instance = this; }
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Config.init(event.getSuggestedConfigurationFile());// Keep first
		GlobalAdditions.init();
		setupNetwork();	
		modLog = event.getModLog();
		
		WeaponConfig = new WeaponModConfig(new Configuration(event.getSuggestedConfigurationFile()));
		
		WeaponConfig.addEnableSetting("simple_pistol");
		WeaponConfig.addEnableSetting("longneck_rifle");
		WeaponConfig.addEnableSetting("shotgun");
		
		WeaponConfig.addReloadTimeSetting("simple_pistol", 5);
		WeaponConfig.addReloadTimeSetting("longneck_rifle", 25);
		WeaponConfig.addReloadTimeSetting("shotgun", 15);
		WeaponConfig.loadConfig();
		
		addWeapons();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ARKEventHandler());
		MinecraftForge.EVENT_BUS.register(new ARKPlayerEventHandler());
		FMLCommonHandler.instance().bus().register(new FMLCommonEventHandler());
		proxy.registerRenderers();
		proxy.registerWeapons(WeaponConfig);
		proxy.init();
		proxy.registerEventHandlers();
		FMLCommonHandler.instance().bus().register(new Config());
		
		registerDispenseBehavior();
		registerWeapons();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
	public static Main instance() { return instance; }
	
	private void setupNetwork() {
		modChannel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
		
		int id = 0;
		modChannel.registerMessage(PlayerPoop.Handler.class, PlayerPoop.class, id++, Side.SERVER);
	}
	
	
	public void addWeapons()
	{
		if (WeaponConfig.isEnabled("simple_pistol"))
		{
			simple_pistol = addSimplePistol("simple_pistol");
			simple_bullet = addItemProjectile("simple_bullet");
		}
		if (WeaponConfig.isEnabled("longneck_rifle"))
		{
			
			longneck_rifle = addLongneckRifle("longneck_rifle");
			simple_rifle_ammo = addItemProjectile("simple_rifle_ammo");
			
		}
		if (WeaponConfig.isEnabled("shotgun"))
		{
			shotgun = addShotgun("shotgun");
			simple_shotgun_ammo = addItemProjectile("simple_shotgun_ammo");
		}
	}	
	
	public void registerDispenseBehavior()
	{
		if (simple_bullet != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(simple_bullet, new DispenseSimpleBullet());
		}
		if (simple_shotgun_ammo != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(simple_shotgun_ammo, new DispenseSimpleShotgunAmmo());
		}
		if (simple_rifle_ammo != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(simple_rifle_ammo, new DispenseSimpleRifleAmmo());
		}
	}
	
	
	public void registerWeapons()
	{
		if (WeaponConfig.isEnabled("simple_pistol"))
		{
			EntityHandler.registerModEntity(EntitySimpleBullet.class, "Simple Bullet", Main.instance, 64, 10, true);
		}
		if (WeaponConfig.isEnabled("shotgun"))
		{
			EntityHandler.registerModEntity(EntitySimpleShotgunAmmo.class, "Simple Shotgun Ammo", Main.instance, 64, 10, true);		
		}
		if (WeaponConfig.isEnabled("longneck_rifle"))
		{
			EntityHandler.registerModEntity(EntitySimpleRifleAmmo.class, "Simple Rifle Ammo", Main.instance, 64, 10, true);
		}
	}	
	
	protected static ItemProjectile addItemProjectile(String name) {
		ItemProjectile i = new ItemProjectile(name);
		allItems.put(name, i);
		return i;
	}	
	protected static ItemSimplePistol addSimplePistol(String name) {
		ItemSimplePistol i = new ItemSimplePistol(name);
		allItems.put(name, i);
		return i;
	}	
	protected static ItemLongneckRifle addLongneckRifle(String name) {
		ItemLongneckRifle i = new ItemLongneckRifle(name);
		allItems.put(name, i);
		return i;
	}
	protected static ItemShotgun addShotgun(String name) {
		ItemShotgun i = new ItemShotgun(name);
		allItems.put(name, i);
		return i;
	}
	
	public static Map<String, Item> getAllItems() { return allItems; }
	
}
