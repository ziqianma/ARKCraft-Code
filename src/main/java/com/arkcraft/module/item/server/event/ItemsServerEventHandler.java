package com.arkcraft.module.item.server.event;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class ItemsServerEventHandler
{
	public static void init()
	{
		FMLCommonHandler.instance().bus().register(new ItemsServerEventHandler());
	}
}
