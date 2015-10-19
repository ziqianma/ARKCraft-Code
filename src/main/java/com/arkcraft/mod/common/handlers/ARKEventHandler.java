package com.arkcraft.mod.common.handlers;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.arkcraft.mod.common.entity.EntityTameableDinosaur;
import com.arkcraft.mod.common.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.common.items.ARKCraftItems;
import com.arkcraft.mod.common.items.weapons.projectiles.EntityTranqArrow;
import com.arkcraft.mod.common.lib.BALANCE;
import com.arkcraft.mod.common.lib.LogHelper;

/**
 * 
 * @author wildbill22
 *
 */
// All events in this class are type MinecraftForge.EVENT_BUS
public class ARKEventHandler {

	/**
	 * 
	 * @author WILLIAM This is called from SpawnerAnimals class 2/24/15: It is
	 *         called for vanilla hostile mobs and raptors
	 *
	 */
	@SubscribeEvent
	public void onEntityJoinWorldEvent(EntityJoinWorldEvent event) {
		if (event.entity instanceof EntityRaptor) {
			EntityRaptor raptor = (EntityRaptor) event.entity;
			// If spawned by WorldGen, it will need the type set
			// raptor.type.setRandomRaptorType();
			LogHelper.info("EntityJoinWorldEvent: Spawning Raptor at: "
					+ raptor.posX + ", " + raptor.posY + ", " + raptor.posZ);
		}

		/***
		 * @author Vastatio this is so we can give the player the dino dossier,
		 *         when the player joins.
		 */
		else if (event.entity instanceof EntityPlayer) {
			EntityPlayer entity = (EntityPlayer) event.entity;
			if (!event.entity.worldObj.isRemote
					&& event.world.getTotalWorldTime() < 100) {
				entity.inventory.addItemStackToInventory(new ItemStack(
						ARKCraftItems.dino_book));
				LogHelper.info("EntityWorldJoinEvent: Gave Dino Dossier @ "
						+ entity.toString());
			}
		}
	}

	@SubscribeEvent
	public void onLivingHurtEvent(LivingHurtEvent event) {
		if (!event.entity.worldObj.isRemote
				&& event.entity instanceof EntityTameableDinosaur) {
			if (event.source.isProjectile()) {
				if (event.source.getEntity() instanceof EntityTranqArrow) {
					if (event.entity instanceof EntityTameableDinosaur) {
						((EntityTameableDinosaur) event.entity)
								.increaseTorpor(BALANCE.WEAPONS.TRANQ_AMMO_TORPOR_TIME);
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRender(RenderGameOverlayEvent event) {
		// TODO
	}

	/**
	 * 
	 * @author Jodelahithit
	 * This makes logs drop thatch, all pickaxes and axes are recognised (tools from other mods too). Axe drops 0-2 thatch and pickaxe 1-5.
	 */
	
	@SubscribeEvent
	public void onDrops(BlockEvent.HarvestDropsEvent event) {
		Random r = new Random();
		int j = r.nextInt(3);
		int k = r.nextInt(5)+1;
		System.out.println(j);
		if (event.state.getBlock() == Blocks.log) {
			if(event.harvester != null && event.harvester.getHeldItem() != null) {
				if (event.harvester.getHeldItem().getItem() instanceof ItemPickaxe) {
					for (int i = 0; i < k; i++) {
						event.drops.add(new ItemStack(ARKCraftItems.thatch));
					}
				} else if (event.harvester.getHeldItem().getItem() instanceof ItemAxe) {
					for (int i = 0; i < j; i++) {
						event.drops.add(new ItemStack(ARKCraftItems.thatch));
					}
				}
			}
		}
	}
}
