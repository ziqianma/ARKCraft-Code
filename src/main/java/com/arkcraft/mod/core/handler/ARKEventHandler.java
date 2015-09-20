package com.arkcraft.mod.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.core.lib.LogHelper;

/**
 * 
 * @author wildbill22
 *
 */
public class ARKEventHandler {
	
	/**
	 * 
	 * @author WILLIAM
	 * This is called from SpawnerAnimals class
	 * 2/24/15: It is called for vanilla hostile mobs and raptors 
	 *
	 */
	@SubscribeEvent
	public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityRaptor){
			EntityRaptor raptor = (EntityRaptor) event.entity;
	    	// If spawned by WorldGen, it will need the type set
//			raptor.type.setRandomRaptorType();
    		LogHelper.info("EntityJoinWorldEvent: Spawning Raptor at: " + raptor.posX + ", " + raptor.posY + ", " + raptor.posZ);
		}
		
		/***
		 * @author Vastatio
		 * this is so we can give the player the dino dossier, when the player joins.
		 */
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer entity = (EntityPlayer)event.entity;
			if (!event.entity.worldObj.isRemote && event.world.getTotalWorldTime() < 100) {
				entity.inventory.addItemStackToInventory(new ItemStack(GlobalAdditions.dino_book));
				LogHelper.info("EntityWorldJoinEvent: Gave Dino Dossier @ " + entity.toString());
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRender(RenderGameOverlayEvent event) {
		//TODO
	}
	
}
