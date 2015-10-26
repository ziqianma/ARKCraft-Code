package com.arkcraft.mod.common.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.player.ARKPlayer;
import com.arkcraft.mod.common.lib.KeyBindings;
import com.arkcraft.mod.common.network.OpenPlayerCrafting;

/**
 * 
 * @author wildbill22
 *
 */
//All events in this class are type FMLCommonHandler.bus()

public class FMLCommonEventHandler {
	
	@SubscribeEvent
	public void onPlayerKeypressed(InputEvent.KeyInputEvent event) {
		if (KeyBindings.playerPooping.isPressed()) {
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (player instanceof EntityPlayerSP) {
				ARKPlayer.get(player).Poop();
			}
		}
		else if (KeyBindings.playerCrafting.isPressed()) {
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (player instanceof EntityPlayerSP) {
				player.openGui(ARKCraft.instance(), GlobalAdditions.GUI.PLAYER.getID(),	player.worldObj, 0, 0, 0);
				ARKCraft.modChannel.sendToServer(new OpenPlayerCrafting(true));
			}			
		}
		
	}	

	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		// Update CraftingInventory
		if (ARKPlayer.get(event.player).getInventoryBlueprints().isCrafting()){
			ARKPlayer.get(event.player).getInventoryBlueprints().update();
		}

	} 
}	
