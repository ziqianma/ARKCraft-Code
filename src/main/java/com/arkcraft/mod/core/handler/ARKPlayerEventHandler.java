package com.arkcraft.mod.core.handler;

import com.arkcraft.mod.core.entity.player.ARKPlayer;
import com.arkcraft.mod.core.lib.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/***
 * 
 * @author wildbill22
 *
 */
// All events in this class are type MinecraftForge.EVENT_BUS
public class ARKPlayerEventHandler {
	// Add ARKPlayer properties to player
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if (event.entity instanceof EntityPlayer && ARKPlayer.get((EntityPlayer) event.entity) == null) {
			ARKPlayer.register((EntityPlayer) event.entity, event.entity.worldObj);
			if (event.entity.worldObj.isRemote) // On client
				LogHelper.info("ARKPlayerEventHandler: Registered a new ARKPlayer on client.");
			else
				LogHelper.info("ARKPlayerEventHandler: Registered a new ARKPlayer on server.");
		}
	}
	
	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone event) {
		LogHelper.info("ARKPlayerEventHandler: Cloning player extended properties");
		ARKPlayer.get(event.entityPlayer).copy(ARKPlayer.get(event.original));
	}

	@SubscribeEvent
	public void onLivingUpdateEvent(LivingUpdateEvent event) {
		if (event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			
			// Enable pooping once a minute
			if (player.ticksExisted % 600 == 0)
				ARKPlayer.get(player).setCanPoop(true);
		}
	}
}
