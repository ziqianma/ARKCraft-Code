package com.arkcraft.mod2.common.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod2.common.config.KeyBindings;
import com.arkcraft.mod2.common.container.ContainerInventoryAttachment;
import com.arkcraft.mod2.common.entity.item.projectiles.EntityBallista;
import com.arkcraft.mod2.common.entity.player.ARKPlayer;
import com.arkcraft.mod2.common.items.ARKCraftItems;
import com.arkcraft.mod2.common.items.weapons.component.RangedCompLongneckRifle;
import com.arkcraft.mod2.common.items.weapons.handlers.IItemWeapon;
import com.arkcraft.mod2.common.network.OpenPlayerCrafting;
import com.arkcraft.mod2.common.tile.TileInventoryAttachment;

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
		else if (KeyBindings.attachment.isPressed()) {
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (player instanceof EntityPlayerSP && player.getHeldItem().getItem() == ARKCraftItems.longneck_rifle ||  player.getHeldItem().getItem() == ARKCraftItems.longneck_rifle_scoped)
			{
				LogHelper("Found Longneckrifle");
				player.openGui(ARKCraft.instance(), GlobalAdditions.GUI.ATTACHMENT_GUI.getID(),	player.worldObj, 0, 0, 0);
		//		ARKCraft.modChannel.sendToServer(new OpenPlayerCrafting(true));
	       	}
	     	
		}	
		
	}
	

	private void LogHelper(String string) {
		// TODO Auto-generated method stub
		
	}


	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
		// Update CraftingInventory
		if (ARKPlayer.get(event.player).getInventoryBlueprints().isCrafting()){
			ARKPlayer.get(event.player).getInventoryBlueprints().update();
		}
		else if (event.phase == TickEvent.Phase.START && event.player instanceof EntityPlayerSP)
		{	
		EntityPlayerSP entity = (EntityPlayerSP) event.player;
		if (entity.movementInput.jump && entity.ridingEntity instanceof EntityBallista && ((EntityBallista) entity.ridingEntity).isLoaded())	
			{	
			((EntityBallista) entity.ridingEntity).fireBallista();
		//	MsgBallistaShot msg = new MsgBallistaShot((EntityBallista)entity.ridingEntity);
		//	ARKCraft.instance.messagePipeline.sendToServer(msg);
			}
		}
	//	else if(event.player instanceof EntityPlayer){
	//		EntityPlayer player = (EntityPlayer) event.player;
	//     	ItemStack stack = player.getHeldItem();
	      //  IItemWeapon i_item_weapon = null;
	    //    if (stack.getItem() instanceof IItemWeapon){
	      //  	i_item_weapon = (IItemWeapon) stack.getItem();
	//		TileInventoryAttachment tileInventoryAttachment = new TileInventoryAttachment(stack);
	//		if(stack != null && stack.getItem() == ARKCraftItems.longneck_rifle) 
	//		{
	//			LogHelper("It is working mate");
			//	i_item_weapon.ifCanScope();
				
			}
		//}
	//}


	public boolean canShowScope() 
	{
		return true;	
	}
	
	
}

	

