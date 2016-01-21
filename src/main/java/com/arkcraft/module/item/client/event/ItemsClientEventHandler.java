package com.arkcraft.module.item.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.GlobalAdditions;
import com.arkcraft.module.core.common.network.OpenAttachmentInventory;
import com.arkcraft.module.core.common.network.OpenPlayerCrafting;
import com.arkcraft.module.item.common.blocks.ARKCraftBlocks;
import com.arkcraft.module.item.common.config.KeyBindings;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityBallista;
import com.arkcraft.module.item.common.entity.player.ARKPlayer;
import com.arkcraft.module.item.common.items.weapons.handlers.IItemWeapon;
import com.arkcraft.module.item.common.items.weapons.ranged.ItemRangedWeapon;
import com.arkcraft.module.item.common.tile.TileFlashlight;
import com.arkcraft.module.item.common.tile.TileInventoryAttachment;

public class ItemsClientEventHandler
{
	public static void init()
	{
		ItemsClientEventHandler handler = new ItemsClientEventHandler();
		FMLCommonHandler.instance().bus().register(handler);
		MinecraftForge.EVENT_BUS.register(handler);
	}

	private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/scope.png");
	public boolean showScopeOverlap = false;
	private Minecraft mc = Minecraft.getMinecraft();

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onMouseEvent(MouseEvent evt)
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer thePlayer = mc.thePlayer;

		if (thePlayer != null && evt.button == 0)
		{
			LogHelper.info("mouse down");
			ItemStack stack = thePlayer.getCurrentEquippedItem();
			if (stack != null)
			{
				IItemWeapon i_item_weapon;
				if (stack.getItem() instanceof IItemWeapon)
				{
					i_item_weapon = (IItemWeapon) stack.getItem();
					TileInventoryAttachment inv = new TileInventoryAttachment(stack);
					if (inv.isScopePresent())
					{
						if (evt.buttonstate)
						{
							showScopeOverlap = true;
						}
						else
						{
							showScopeOverlap = false;
						}
						evt.setCanceled(true);
					}
				}
				else
				{
					i_item_weapon = null;
				}
				// Weapon with scope?
				// if (i_item_weapon != null && i_item_weapon.ifCanScope())
				// {
				// if (evt.buttonstate)
				// {
				// ShowScopeOverlap = true;
				// }
				// else
				// {
				// ShowScopeOverlap = false;
				// }
				// evt.setCanceled(true);
				// }
			}
		}
	}

	@SubscribeEvent
	public void onFOVUpdate(FOVUpdateEvent evt)
	{
		if (mc.gameSettings.thirdPersonView == 0 && showScopeOverlap)
		{
			evt.newfov = 1 / 6.0F;
		}
	}

	@SubscribeEvent
	public void onRenderHand(RenderHandEvent evt)
	{
		if (showScopeOverlap)
		{
			evt.setCanceled(true);
		}
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRender(RenderGameOverlayEvent evt)
	{
		if (evt.type == RenderGameOverlayEvent.ElementType.HELMET)
		{
			if (mc.gameSettings.thirdPersonView == 0 && showScopeOverlap)
			{
				evt.setCanceled(true); // Removes the normal crosshairs and uses
										// just the overlay crosshairs
				LogHelper.info("onRender ShowScopeOverlap = true");
				showScope();
			}
		}
	}

	@SubscribeEvent
	public void holding(RenderLivingEvent.Pre event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer thePlayer = mc.thePlayer;
		if (!event.isCanceled() & event.entity instanceof EntityPlayer && showScopeOverlap)
		{
			ItemStack stack = thePlayer.getCurrentEquippedItem();
			if (stack != null)
			{
				IItemWeapon i_item_weapon;
				if (stack.getItem() instanceof IItemWeapon)
				{
					i_item_weapon = (IItemWeapon) stack.getItem();
				}
				else
				{
					i_item_weapon = null;
				}
				if (i_item_weapon != null)
				{

					ModelPlayer model = (ModelPlayer) event.renderer.getMainModel();

					model.aimedBow = true;
				}

				/*
				 * ModelPlayer model =
				 * (ModelPlayer)event.renderer.getMainModel();
				 * model.bipedLeftArm.rotateAngleX =
				 * model.bipedLeftArm.rotateAngleX * 0.5F - ((float)Math.PI /
				 * 10F) * (float)model.heldItemLeft; model.heldItemRight = 0;
				 * 
				 * /* if (model.heldItemLeft != 0) {
				 * model.bipedLeftArm.rotateAngleX =
				 * model.bipedLeftArm.rotateAngleX * 0.5F - ((float)Math.PI /
				 * 10F) * (float)model.heldItemLeft; } switch
				 * (model.heldItemRight) { case 0: case 2: default: break; case
				 * 1: model.bipedRightArm.rotateAngleX =
				 * model.bipedRightArm.rotateAngleX * 0.5F - ((float)Math.PI /
				 * 10F) * (float)model.heldItemRight; break; case 3:
				 * model.bipedRightArm.rotateAngleX =
				 * model.bipedRightArm.rotateAngleX * 0.5F - ((float)Math.PI /
				 * 10F) * (float)model.heldItemRight;
				 * model.bipedRightArm.rotateAngleY = -0.5235988F; }
				 */

			}
		}
	}

	public void showScope()
	{
		LogHelper.info("In ShowScope");
		Minecraft mc = Minecraft.getMinecraft();
		GL11.glPushMatrix();
		mc.entityRenderer.setupOverlayRendering();
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		mc.renderEngine.bindTexture(OVERLAY_TEXTURE);

		ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		double width = res.getScaledWidth_double();
		double height = res.getScaledHeight_double();

		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer worldrenderer = tessellator.getWorldRenderer();

		worldrenderer.startDrawingQuads();
		worldrenderer.addVertexWithUV(0.0D, height, -90.0D, 0.0D, 1.0D);
		worldrenderer.addVertexWithUV(width, height, -90.0D, 1.0D, 1.0D);
		worldrenderer.addVertexWithUV(width, 0.0D, -90.0D, 1.0D, 0.0D);
		worldrenderer.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator.draw();

		GL11.glPopMatrix();
	}

//	@SubscribeEvent
	public void onPlayerTickEvent(TickEvent.PlayerTickEvent event)
	{
		IItemWeapon i_item_weapon;
		
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer thePlayer = mc.thePlayer;
		
		ItemStack stack = thePlayer.getCurrentEquippedItem();
		
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
		else if  (stack.getItem() instanceof IItemWeapon)
		{
			i_item_weapon = (IItemWeapon) stack.getItem();
			TileInventoryAttachment inv = new TileInventoryAttachment(stack);
            MovingObjectPosition mop = rayTrace(thePlayer, 20, 1.0F);

            if(inv.isFlashPresent())
            {
	            if (mop != null)
	            {
	                if (!(mop.typeOfHit == MovingObjectPosition.MovingObjectType.MISS))
	                {
	                    BlockPos pos;

	                    if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
	                    {
	                        pos = mop.entityHit.getPosition();
	                    }
	                    else
	                    {
	                        pos = mop.getBlockPos();
	                        pos = pos.offset(mop.sideHit);
	                    }

	                    if (thePlayer.worldObj.getBlockState(pos).getBlock() == ARKCraftBlocks.block_flashlight)
	                    {
	                        TileFlashlight tileLight = (TileFlashlight) thePlayer.worldObj.getTileEntity(pos);
	                        tileLight.ticks = 0;
	                    }
	                    else
	                    {
	                        if (thePlayer.worldObj.isAirBlock(pos))
	                        {
	                        	thePlayer.worldObj.setBlockState(pos, ARKCraftBlocks.block_flashlight.getDefaultState());
	                        }
	                    }
	                }
	            }
	        }
		}
		else
		{
			i_item_weapon = null;
		}
    }

    public Vec3 getPositionEyes(EntityPlayer player, float partialTick)
    {
        if (partialTick == 1.0F)
        {
            return new Vec3(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
        }
        else
        {
            double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTick;
            double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTick + (double)player.getEyeHeight();
            double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTick;
            return new Vec3(d0, d1, d2);
        }
    }

    public MovingObjectPosition rayTrace(EntityPlayer player, double distance, float partialTick)
    {
        Vec3 vec3 = getPositionEyes(player, partialTick);
        Vec3 vec31 = player.getLook(partialTick);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
        return player.worldObj.rayTraceBlocks(vec3, vec32, false, false, true);
    }

	@SubscribeEvent
	public void onPlayerKeypressed(InputEvent.KeyInputEvent event)
	{
		if (KeyBindings.attachment.isPressed())
		{
			EntityPlayerSP p = mc.thePlayer;
			if (p instanceof EntityPlayerSP && p.inventory.getCurrentItem().getItem() instanceof ItemRangedWeapon)
			{
				p.openGui(ARKCraft.instance, GlobalAdditions.GUI.ATTACHMENT_GUI.getID(),
						p.worldObj, 0, 0, 0);
				ARKCraft.modChannel.sendToServer(new OpenAttachmentInventory());
			}
		}
		else if (KeyBindings.playerPooping.isPressed())
		{
			EntityPlayerSP player = mc.thePlayer;
			if (player instanceof EntityPlayerSP)
			{
				ARKPlayer.get(player).Poop();
			}
		}
		else if (KeyBindings.playerCrafting.isPressed())
		{
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (player instanceof EntityPlayerSP)
			{
				player.openGui(ARKCraft.instance(), GlobalAdditions.GUI.PLAYER.getID(),
						player.worldObj, 0, 0, 0);
				ARKCraft.modChannel.sendToServer(new OpenPlayerCrafting(true));
			}
		}
	}
}
