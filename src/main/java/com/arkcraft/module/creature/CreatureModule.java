package com.arkcraft.module.creature;

import net.minecraftforge.fml.common.FMLCommonHandler;

import com.arkcraft.module.creature.init.Models;

public class CreatureModule
{
	public static boolean preInitialized;
	public static boolean initialized;
	public static boolean postInitialized;

	public static void preInit()
	{
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
