package com.arkcraft.module.blocks.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.GlobalAdditions;
import com.arkcraft.module.core.common.entity.data.ARKPlayer;
import com.arkcraft.module.core.common.network.OpenPlayerCrafting;
import com.arkcraft.module.weapon.common.entity.EntityBallista;

@SideOnly(Side.CLIENT)
public class ItemsClientEventHandler
{
	public static void init()
	{
		ItemsClientEventHandler handler = new ItemsClientEventHandler();
		FMLCommonHandler.instance().bus().register(handler);
		MinecraftForge.EVENT_BUS.register(handler);
	}

	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		// Update CraftingInventory
		if (ARKPlayer.get(event.player).getInventoryBlueprints().isCrafting())
		{
			ARKPlayer.get(event.player).getInventoryBlueprints().update();
		}
		else if (event.phase == TickEvent.Phase.START && event.player instanceof EntityPlayerSP)
		{
			EntityPlayerSP entity = (EntityPlayerSP) event.player;
			if (entity.movementInput.jump && entity.ridingEntity instanceof EntityBallista && ((EntityBallista) entity.ridingEntity)
					.isLoaded())
			{
				((EntityBallista) entity.ridingEntity).fireBallista();
				// MsgBallistaShot msg = new
				// MsgBallistaShot((EntityBallista)entity.ridingEntity);
				// ARKCraft.instance.messagePipeline.sendToServer(msg);
			}
		}
	}

	public Vec3 getPositionEyes(EntityPlayer player, float partialTick)
	{
		if (partialTick == 1.0F)
		{
			return new Vec3(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);
		}
		else
		{
			double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double) partialTick;
			double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double) partialTick + (double) player
					.getEyeHeight();
			double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) partialTick;
			return new Vec3(d0, d1, d2);
		}
	}

	public MovingObjectPosition rayTrace(EntityPlayer player, double distance, float partialTick)
	{
		Vec3 vec3 = getPositionEyes(player, partialTick);
		Vec3 vec31 = player.getLook(partialTick);
		Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance,
				vec31.zCoord * distance);
		return player.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
	}

	@SubscribeEvent
	public void onPlayerKeypressed(InputEvent.KeyInputEvent event)
	{
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
		if (KeyBindings.playerPooping.isPressed())
		{
			ARKPlayer.get(player).poop();
		}
		else if (KeyBindings.playerCrafting.isPressed())
		{
			player.openGui(ARKCraft.instance(), GlobalAdditions.GUI.PLAYER.getID(),
					player.worldObj, 0, 0, 0);
			ARKCraft.modChannel.sendToServer(new OpenPlayerCrafting(true));
		}
	}
}
