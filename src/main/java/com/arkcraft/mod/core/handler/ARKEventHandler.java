package com.arkcraft.mod.core.handler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.entity.EntityRaptor;
import com.arkcraft.mod.core.entity.passive.EntityDodo;
import com.arkcraft.mod.lib.LogHelper;

public class ARKEventHandler {
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new ARKEventHandler());
		FMLCommonHandler.instance().bus().register(MinecraftForge.EVENT_BUS);
	}
	
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
	
	/***
	 * 
	 * @author VASTATIO
	 * This is normally used for when the player right clicks the entity 
	 * 
	 */
	@SubscribeEvent
	public void onEntityInteractEvent(EntityInteractEvent event) {
		LogHelper.info("The player right clicked an entity at: " + event.entity.posX + ", " + event.entity.posY + "," + event.entity.posZ + ".");
		if(event.target.interactFirst(event.entityPlayer)) return;
		if(event.target instanceof EntityDodo && !event.entity.worldObj.isRemote && ((EntityDodo)event.entity).isChested() && !event.entityPlayer.isSneaking()) {
			/* Opens the Smithy GUI for now, TODO get the right GUI opened. */
			LogHelper.info("EntityInteractEvent: The player has interacted with EntityDodo at: " + event.entity.posX + ", " + event.entity.posY + "," + event.entity.posZ + ".");
			event.entityPlayer.openGui(Main.instance, GlobalAdditions.guiID++, event.entityPlayer.worldObj, event.entityPlayer.getPosition().getX(), event.entityPlayer.getPosition().getY(), event.entityPlayer.getPosition().getZ());
		}
	}

}
