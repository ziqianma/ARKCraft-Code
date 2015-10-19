package com.arkcraft.mod.client.gui.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiScope extends Gui{

	private Minecraft mc;
	private int height;
	private int width;
//	public static final ResourceLocation texture = new ResourceLocation(AdvGuns.modid + ":textures/gui/6x Zoom Scope Overlay.png");
	public static final ResourceLocation texture = new ResourceLocation("textures/misc/pumpkinblur.png");
	
	public GuiScope(Minecraft minecraft) {
		super();
		this.mc = minecraft;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onRenderExperienceBar(RenderGameOverlayEvent event){
		
			
	}
	
	public static void drawDrawFullscreenImage(int width, int height) {
	Tessellator tessellator = Tessellator.getInstance();
	WorldRenderer worldrenderer = tessellator.getWorldRenderer();
	worldrenderer.startDrawingQuads();
	worldrenderer.addVertexWithUV(0.0D, (double)height, -90.0D, 0.0D, 1.0D);
	worldrenderer.addVertexWithUV((double)width, (double)height, -90.0D, 1.0D, 1.0D);
	worldrenderer.addVertexWithUV((double)width, 0.0D, -90.0D, 1.0D, 0.0D);
    tessellator.draw();
	    }

}