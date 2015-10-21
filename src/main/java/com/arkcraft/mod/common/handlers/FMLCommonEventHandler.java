package com.arkcraft.mod.common.handlers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.player.ARKPlayer;
import com.arkcraft.mod.common.items.weapons.ItemLongneckRifle;
import com.arkcraft.mod.common.items.weapons.component.RangedCompLongneckRifle;
import com.arkcraft.mod.common.lib.KeyBindings;

/**
 * 
 * @author wildbill22
 *
 */
//All events in this class are type FMLCommonHandler.bus()

public class FMLCommonEventHandler {
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation(ARKCraft.MODID, "textures/gui/scope.png");
    
    private static final float MIN_ZOOM = 1 / 1.5F;
    private static final float MAX_ZOOM = 1 / 10.0F;
    private static float currentZoom = 1 / 6.0F;
	
	@SubscribeEvent
	public void onPlayerPooping(InputEvent.KeyInputEvent event) {
		if (KeyBindings.playerPooping.isPressed()) {
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (player instanceof EntityPlayerSP) {
				ARKPlayer.get(player).Poop();
			}
		}
	}	
	
	/*
	@SubscribeEvent
	public void onPlayerScoping(InputEvent.KeyInputEvent event) {
		if (KeyBindings.playerScoping.isPressed()) {
			EntityPlayer entityplayer = Minecraft.getMinecraft().thePlayer;
			
			if (entityplayer instanceof EntityPlayer)
			{
				RangedCompLongneckRifle.get(entityplayer).onUpdate(null, null, entityplayer, 0, false);
			}
		}
	}	*/
	
    @SubscribeEvent
    public void onFOVUpdate(FOVUpdateEvent evt) {
        if (isScoping() && mc.gameSettings.thirdPersonView == 0) {
            evt.newfov = currentZoom;
        }
    }
    
    @SubscribeEvent
    public void onMouseScroll(MouseEvent evt) {
        if (isScoping() && evt.dwheel != 0 && mc.gameSettings.thirdPersonView == 0) {
            currentZoom = 1 / Math.min(Math.max(1 / currentZoom + evt.dwheel / 180F, 1 / MIN_ZOOM), 1 / MAX_ZOOM);
            evt.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onRenderTick(RenderTickEvent evt) {
        if (evt.phase != Phase.END) {
            return;
        }
        
        if (isScoping() && mc.gameSettings.thirdPersonView == 0) {
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
    
    @SubscribeEvent
    public void onRenderHand(RenderHandEvent evt) {
        if (isScoping()) {
            evt.setCanceled(true);
        }
    }
    
    private static boolean isScoping(EntityPlayer player, boolean keybind) {
        ItemStack stack = player.getItemInUse();
        if (stack != null && stack.getItem() instanceof ItemLongneckRifle) {
            return true;
        } else if (keybind && KeyBindings.playerScoping.isPressed()) {
            for (ItemStack invStack : player.inventory.mainInventory) {
                if (invStack != null && invStack.getItem() instanceof ItemLongneckRifle) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean isScoping(boolean keybind) {
        EntityPlayer player = mc.thePlayer;
        if (player == null) {
            return false;
        }
        return isScoping(player, keybind);
    }
    
    private static boolean isScoping() {
        return isScoping(true);
    }
    
}