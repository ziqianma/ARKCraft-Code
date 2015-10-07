package com.arkcraft.mod.core.machine.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.blocks.TileInventoryCropPlot;
import com.arkcraft.mod.core.blocks.TileInventoryMP;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

/* Mortar and Pestle */
/***
 * 
 * @author Vastatio
 *
 */
public class GuiMP extends GuiContainer {

	public String name = "Mortar and Pestle";
	public static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/mortar_and_pestle.png");
	private TileInventoryMP tileEntity;

	public GuiMP(InventoryPlayer invPlayer, TileInventoryMP tileInventoryMP) {
		super(new ContainerInventoryMP(invPlayer, tileInventoryMP));
		this.tileEntity = tileInventoryMP;
		this.xSize = 175;
		this.ySize = 165;
	}
	
	// some [x,y] coordinates of graphical elements
	final int LEFT_BUTTON_XPOS = 30;
	final int LEFT_BUTTON_YPOS = 22;
//	final int LEFT_BUTTON_ICON_U = 176;   // texture position of the water bar
//	final int LEFT_BUTTON_ICON_V = 17;
	final int LEFT_BUTTON_WIDTH = 10;
	final int LEFT_BUTTON_HEIGHT = 8;

	final int RIGHT_BUTTON_XPOS = 64;
	final int RIGHT_BUTTON_YPOS = 22;
//	final int RIGHT_BUTTON_ICON_U = 176;   // texture position of the water bar
//	final int RIGHT_BUTTON_ICON_V = 17;
	final int RIGHT_BUTTON_WIDTH = 10;
	final int RIGHT_BUTTON_HEIGHT = 8;

	final int CRAFT_BUTTON_XPOS = 29;
	final int CRAFT_BUTTON_YPOS = 41;
//	final int CRAFT_BUTTON_ICON_U = 176;   // texture position of the water bar
//	final int CRAFT_BUTTON_ICON_V = 17;
	final int CRAFT_BUTTON_WIDTH = 45;
	final int CRAFT_BUTTON_HEIGHT = 11;

	final int CRAFTING_TEXT_XPOS = 80;
	final int CRAFTING_TEXT_YPOS = 22;

	public void onGuiClosed() { super.onGuiClosed(); }
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		this.fontRendererObj.drawString(name, (int)(xSize / 2) - (name.length() * 5 / 2), 5, Color.darkGray.getRGB());
		// Number being crafted
		this.fontRendererObj.drawString("Crafting " + tileEntity.getNumToBeCrafted() + " item(s)", 
				CRAFTING_TEXT_XPOS, CRAFTING_TEXT_YPOS, Color.darkGray.getRGB());

		List<String> hoveringText = new ArrayList<String>();

		// Add hovering text if the mouse is over the Craft all button
		if (isInRect(guiLeft + CRAFT_BUTTON_XPOS, guiTop + CRAFT_BUTTON_YPOS, CRAFT_BUTTON_WIDTH, CRAFT_BUTTON_HEIGHT, mouseX, mouseY)){
			hoveringText.add("Can craft " + tileEntity.getNumToBeCrafted() + " item.");
		}
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
		return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
	}
}
