package com.arkcraft.module.core.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.common.entity.aggressive.EntityRaptor;
import com.arkcraft.module.item.common.items.ARKCraftItems;

/**
 * @author wildbill22
 */
public class CoreCommonEventHandler
{
	public static void init()
	{
		CoreCommonEventHandler handler = new CoreCommonEventHandler();
		FMLCommonHandler.instance().bus().register(handler);
		MinecraftForge.EVENT_BUS.register(handler);
	}

	/**
	 * @author Vastatio this is so we can give the player the dino dossier, when
	 *         the player joins.
	 */
	@SubscribeEvent
	public void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityRaptor)
		{
			EntityRaptor raptor = (EntityRaptor) event.entity;
			// If spawned by WorldGen, it will need the type set
			// raptor.type.setRandomRaptorType();
			LogHelper
					.info("EntityJoinWorldEvent: Spawning Raptor at: " + raptor.posX + ", " + raptor.posY + ", " + raptor.posZ);
		}

		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer entity = (EntityPlayer) event.entity;
			if (!event.entity.worldObj.isRemote && event.world.getTotalWorldTime() < 100)
			{
				entity.inventory.addItemStackToInventory(new ItemStack(ARKCraftItems.dino_book));
				LogHelper.info("EntityWorldJoinEvent: Gave Dino Dossier @ " + entity.toString());
			}
		}
	}
}
