package com.arkcraft.module.core.server.proxy;

import com.arkcraft.module.core.common.proxy.CommonProxy;
import com.arkcraft.module.item.server.event.ItemsServerEventHandler;

public class ServerProxy extends CommonProxy
{
	@Override
	public void init()
	{
		super.init();
		ItemsServerEventHandler.init();
	}
}
