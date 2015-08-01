package com.arkcraft.mod.core.machine.gui;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiBase extends GuiContainer {

	private String localizedName;
	private static ResourceLocation tex;
	
	public GuiBase(Container base, String localizedName, String texturePath, int xSize, int ySize) {
		super(base);
		tex = new ResourceLocation(Main.MODID, texturePath);
		this.localizedName = localizedName;
		this.xSize = xSize;
		this.ySize = ySize;
	}
	
	
	/* Draw localized name at the center of the GUI, 5 pixels down */
	protected void drawGuiContainerForegroundLayer(int i, int j) { this.fontRendererObj.drawString(StatCollector.translateToLocal(localizedName), xSize / 2, 5, 0x000000); }
	
	/* Regular close */
	public void onGuiClosed() { super.onGuiClosed(); }


	/* Basic draw, draws picture of GUI in center of minecraft screen */
	/* For more complex GUI's, override, super, then add more to it if you'd like */
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		/* Clear colors, bind texture, render */
		GL11.glColor4f(1F, 1F, 1F, 1F);
		if(tex == null) System.err.println("File is not found at path: " + tex.getResourcePath());
		Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
}
