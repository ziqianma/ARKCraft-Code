package com.arkcraft.mod.common.handlers;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.common.ARKCraft;
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
		if (event.entity instanceof EntityPlayer) {
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
//		System.out.println(j);
		if (event.harvester != null && event.harvester.getHeldItem() != null && event.state.getBlock() == Blocks.log || event.state.getBlock() == Blocks.log2) {
			if(event.harvester.getHeldItem().getItem() instanceof ItemPickaxe){
			for (int i = 0; i < k; i++) {
				event.drops.add(new ItemStack(ARKCraftItems.thatch));
			}
			}else if(event.harvester.getHeldItem().getItem() instanceof ItemAxe){
				for (int i = 0; i < j; i++){
					event.drops.add(new ItemStack(ARKCraftItems.thatch));
				}
			}
		}
	}
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(ARKCraft.MODID, "textures/gui/scope.png");
	public boolean ShowScopeOverlap = false;
	
    @SubscribeEvent
    public void onMouseEvent(MouseEvent evt) {
    	
    	 Minecraft mc = Minecraft.getMinecraft();
	     EntityPlayer thePlayer = mc.thePlayer;
	     
        if (thePlayer != null && evt.button == 0) 
        {
        	LogHelper.info("mouse down");
	     	ItemStack stack = thePlayer.getCurrentEquippedItem();
	     	if (stack != null)
        	{
		        IExtendedReach ieri;
		        if (stack.getItem() instanceof IExtendedReach)
	            {
	                 ieri = (IExtendedReach) stack.getItem();
	            } 
		        else
	            {
	                 ieri = null;
	            }
	     	     if (ieri != null)
	             {
	     	    	if (evt.buttonstate)
	     	    		ShowScopeOverlap = true;
	                	
	     	    	else
	     	    		ShowScopeOverlap = false;
	                evt.setCanceled(true);
	             }
        	}
        }
    }
    
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRender(RenderGameOverlayEvent evt) {
		if (mc.gameSettings.thirdPersonView == 0 && ShowScopeOverlap) {
			LogHelper.info("onRender ShowScopeOverlap = true");
			ShowScope(); 
		}
	}
   
    public void ShowScope() 
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
}
