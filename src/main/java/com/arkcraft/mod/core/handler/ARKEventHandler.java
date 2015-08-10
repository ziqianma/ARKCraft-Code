package com.arkcraft.mod.core.handler;

import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.arkcraft.mod.core.entity.EntityRaptor;
import com.arkcraft.mod.lib.LogHelper;

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
			raptor.type.setRandomRaptorType();
    		LogHelper.info("EntityJoinWorldEvent: Spawning Raptor at: " + raptor.posX + ", " + raptor.posY + ", " + raptor.posZ);
		}
	}
	
}
