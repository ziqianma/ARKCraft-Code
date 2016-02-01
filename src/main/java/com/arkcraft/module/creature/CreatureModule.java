package com.arkcraft.module.creature;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class CreatureModule
{
	public static void preInit()
	{
		if (FMLCommonHandler.instance().getSide().isClient()) clientPreInit();
		else serverPreInit();
	}

	private static void clientPreInit()
	{

	}

	private static void serverPreInit()
	{

	}

	public static void init()
	{
		if (FMLCommonHandler.instance().getSide().isClient()) clientInit();
		else serverInit();
	}

	private static void clientInit()
	{

	}

	private static void serverInit()
	{

	}

	public static void postInit()
	{
		if (FMLCommonHandler.instance().getSide().isClient()) clientPostInit();
		else serverPostInit();
	}

	private static void clientPostInit()
	{

	}

	private static void serverPostInit()
	{

	}
}
