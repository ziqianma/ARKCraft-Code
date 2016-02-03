package com.arkcraft.module.weapon;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.arkcraft.module.weapon.client.event.ClientEventHandler;
import com.arkcraft.module.weapon.client.gui.GuiOverlayReloading;
import com.arkcraft.module.weapon.common.event.CommonEventHandler;
import com.arkcraft.module.weapon.init.Blocks;
import com.arkcraft.module.weapon.init.Items;
import com.arkcraft.module.weapon.init.Models;

public class WeaponModule
{
	public static boolean preInitialized;
	public static boolean initialized;
	public static boolean postInitialized;

	public static Items items;
	public static Blocks blocks;
	public static Models models;

	public static void preInit()
	{
		CommonEventHandler.init();

		MinecraftForge.EVENT_BUS.register(new GuiOverlayReloading());

		items = new Items();
		blocks = new Blocks();

		if (FMLCommonHandler.instance().getSide().isClient()) clientPreInit();
		else serverPreInit();
		preInitialized = true;
	}

	public static void init()
	{
		if (FMLCommonHandler.instance().getSide().isClient()) clientInit();
		else serverInit();
		initialized = true;
	}

	public static void postInit()
	{
		if (FMLCommonHandler.instance().getSide().isClient()) clientPostInit();
		else serverPostInit();
		postInitialized = true;
	}

	private static void clientPreInit()
	{
		ClientEventHandler.init();
	}

	private static void serverPreInit()
	{

	}

	private static void clientInit()
	{
		Models.init();
	}

	private static void serverInit()
	{

	}

	private static void clientPostInit()
	{

	}

	private static void serverPostInit()
	{

	}
}
