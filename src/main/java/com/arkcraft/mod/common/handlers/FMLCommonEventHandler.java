package com.arkcraft.mod.common.handlers;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.player.ARKPlayer;
import com.arkcraft.mod.common.items.weapons.ItemLongneckRifle;
import com.arkcraft.mod.common.items.weapons.component.RangedCompLongneckRifle;
import com.arkcraft.mod.common.lib.KeyBindings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

/**
 * 
 * @author wildbill22
 *
 */
//All events in this class are type FMLCommonHandler.bus()

public class FMLCommonEventHandler {
	@SubscribeEvent
	public void onPlayerPooping(InputEvent.KeyInputEvent event) {
		if (KeyBindings.playerPooping.isPressed()) {
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (player instanceof EntityPlayerSP) {
				ARKPlayer.get(player).Poop();
			}
		}
	}	
	
	@SubscribeEvent
	public void onPlayerScoping(InputEvent.KeyInputEvent event) {
		if (KeyBindings.playerScoping.isPressed()) {
			EntityPlayer entityplayer = Minecraft.getMinecraft().thePlayer;
			
			if (entityplayer instanceof EntityPlayer)
			{
				RangedCompLongneckRifle.get(entityplayer).onUpdate(null, null, entityplayer, 0, true);
			}
		}
	}
}
			
