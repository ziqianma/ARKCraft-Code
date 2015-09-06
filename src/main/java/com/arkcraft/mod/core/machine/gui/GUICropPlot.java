package com.arkcraft.mod.core.machine.gui;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.blocks.crop_test.ContainerInventoryCropPlot;
import com.arkcraft.mod.core.blocks.crop_test.TileInventoryCropPlot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/***
 * 
 * @author wildbill22
 *
 */
@SideOnly(Side.CLIENT)
public class GUICropPlot extends GuiContainer {
	
	public static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/crop_plot_gui.png");
//	public String name = "Crop Plot";
//	private final InventoryPlayer playerInventory;
	private TileInventoryCropPlot tileEntity;
	
	public GUICropPlot(InventoryPlayer invPlayer, TileInventoryCropPlot tileInventoryCropPlot) {
		super(new ContainerInventoryCropPlot(invPlayer, tileInventoryCropPlot));
//		super(new ARKContainerCropPlot(invPlayer, world, pos));
//		this.playerInventory = invPlayer;
		this.tileEntity = tileInventoryCropPlot;
		
		// Width and height of the gui:
		this.xSize = 176;
		this.ySize = 166;
	}
	
	// some [x,y] coordinates of graphical elements
	final int WATER_BAR_XPOS = 10;
	final int WATER_BAR_YPOS = 7;
	final int WATER_BAR_ICON_U = 176;   // texture position of the water bar
	final int WATER_BAR_ICON_V = 35;
	final int WATER_BAR_WIDTH = 11;
	final int WATER_BAR_HEIGHT = 41;
	
	final int ARROW_XPOS = 68;
	final int ARROW_YPOS = 17;
	final int ARROW_ICON_U = 177;   // texture position of arrow icon
	final int ARROW_ICON_V = 0;
	final int ARROW_WIDTH = 22;
	final int ARROW_HEIGHT = 16;
	
	final int FLAME_XPOS = 45;
	final int FLAME_YPOS = 36;
	final int FLAME_ICON_U = 178;   // texture position of flame icon
	final int FLAME_ICON_V = 19;
	final int FLAME_WIDTH = 13;
	final int FLAME_HEIGHT = 14;
	
	public void onGuiClosed() { super.onGuiClosed(); }
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		//this.fontRendererObj.drawString(name, (int)(xSize / 2) - name.length(), 5, Color.darkGray.getRGB());
//		 this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 1, 6, 4210752);
//		 this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		// get water progress as a double between 0 and 1
		double waterProgress = tileEntity.waterLevel();
		// draw the water progress bar
		drawTexturedModalRect(guiLeft + WATER_BAR_XPOS, guiTop + WATER_BAR_YPOS, WATER_BAR_ICON_U, WATER_BAR_ICON_V,
						              WATER_BAR_WIDTH, (int)(waterProgress * WATER_BAR_HEIGHT));
	}
}
