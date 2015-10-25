package com.arkcraft.mod.common.handlers;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.player.ARKPlayer;
import com.arkcraft.mod.common.items.weapons.ItemLongneckRifle;
import com.arkcraft.mod.common.lib.KeyBindings;
import com.arkcraft.mod.common.lib.LogHelper;

/**
 * 
 * @author wildbill22
 *
 */
//All events in this class are type FMLCommonHandler.bus()

public class FMLCommonEventHandler {
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(ARKCraft.MODID, "textures/gui/scope.png");
	
	@SubscribeEvent
	public void onPlayerPooping(InputEvent.KeyInputEvent event) {
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
			}			
		}
	}	
    
	public boolean ShowScopeOverlap = false;
	
    @SubscribeEvent
    public void onRenderTick(RenderTickEvent evt) {
    	
    	 Minecraft mc = Minecraft.getMinecraft();
	     EntityPlayer thePlayer = mc.thePlayer;

	     
        if (evt.phase != Phase.END) {
            return;
        }
        if (thePlayer != null && Mouse.isButtonDown(0)) 
        {
        	LogHelper.info("mouse down");
	     	ItemStack stack = thePlayer.getCurrentEquippedItem();
	        IExtendedReach ieri;
	     	if (stack != null)
        	{
	        //    	LogHelper.info("ItemStack != null");
	        //      if (stack.getItem() instanceof ItemLongneckRifle)
	     		 if (stack.getItem() instanceof IExtendedReach)
	             {
	                 ieri = (IExtendedReach) stack.getItem();
	             } else
	             {
	                 ieri = null;
	             }
	     	     if (ieri != null)
	             {
	                	ShowScopeOverlap = true;//bla();
	             }
	       //      ShowScopeOverlap = true;
        	}
        }
    	else ShowScopeOverlap = false;
        if (mc.gameSettings.thirdPersonView == 0 && ShowScopeOverlap) {
        	LogHelper.info("ShowScrope");
        	ShowScrope();
        }
        
    }
  
    public void ShowScrope() 
    {
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
	
	@SubscribeEvent
	public void onMouseEvent(MouseEvent event)
	{ 
		LogHelper.info("On Mouse Event"); 
	    if (event.button == 0 && event.buttonstate)
	    {
	        Minecraft mc = Minecraft.getMinecraft();
	        EntityPlayer thePlayer = mc.thePlayer;
	        if (thePlayer != null)
	        {
	            ItemStack itemstack = thePlayer.getCurrentEquippedItem();
	            IExtendedReach ieri;
	            if (itemstack != null)
	            {
	                if (itemstack.getItem() instanceof IExtendedReach)
	                {
	                    ieri = (IExtendedReach) itemstack.getItem();
	                } else
	                {
	                    ieri = null;
	                }
	   
	                if (ieri != null)
	                {
	                	ShowScopeOverlap = true;//bla();
	                }
	            }
	        }
	    }
	}     
}	