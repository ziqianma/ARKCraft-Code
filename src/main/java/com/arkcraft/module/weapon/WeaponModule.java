package com.arkcraft.module.weapon;

import net.minecraftforge.fml.common.FMLCommonHandler;

import com.arkcraft.module.weapon.client.event.ClientEventHandler;
import com.arkcraft.module.weapon.common.event.CommonEventHandler;
import com.arkcraft.module.weapon.init.Blocks;
import com.arkcraft.module.weapon.init.Items;
import com.arkcraft.module.weapon.init.Models;

public class WeaponModule
{
	public static Items items;
	public static Blocks blocks;

	public static void preInit()
	{
		CommonEventHandler.init();
		items = new Items();
		blocks = new Blocks();
		if (FMLCommonHandler.instance().getSide().isClient()) clientPreInit();
		else serverPreInit();
	}

	public static void init()
	{
		if (FMLCommonHandler.instance().getSide().isClient()) clientInit();
		else serverInit();
	}

	private static void clientPreInit()
	{
		ClientEventHandler.init();
	}

	private static void clientInit()
	{
		Models.init();
	}

	private static void serverPreInit()
	{

	}

	private static void serverInit()
	{

	}
}
