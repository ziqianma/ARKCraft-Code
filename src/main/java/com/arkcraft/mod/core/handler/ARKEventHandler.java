package com.arkcraft.mod.core.handler;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.core.lib.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		if(event.entity instanceof EntityPlayer) {
			EntityPlayer entity = (EntityPlayer)event.entity;
			entity.inventory.addItemStackToInventory(new ItemStack(GlobalAdditions.dinoBook));
			LogHelper.info("EntityWorldJoinEvent: Gave Dino Dossier @ " + entity.toString());
		}
	}
	
}
