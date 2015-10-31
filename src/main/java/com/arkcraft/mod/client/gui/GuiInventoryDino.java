package com.arkcraft.mod.client.gui;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.EntityTameableDinosaur;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class GuiInventoryDino extends GuiContainer {

	private InventoryDino invDino;
	// This is the resource location for the background image for the GUI
	private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/dino_tamed_gui.png");

	public GuiInventoryDino(IInventory playerInv, IInventory invDino, EntityTameableDinosaur dino) {
		super(new ContainerInventoryDino(playerInv, invDino, dino));
		this.invDino = (InventoryDino) invDino;
		this.invDino.setCustomName("Medium Dino Inventory");

		// Set the width and height of the gui.  Should match the size of the texture!
		xSize = 175;
		ySize = 242;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	// draw the foreground for the GUI - rendered after the slots, but before the dragged items and tooltips
	// renders relative to the top left corner of the background
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final int LABEL_XPOS = ContainerInventoryDino.DINO_INVENTORY_XPOS;
		final int LABEL_YPOS = 5;
		// Name of GUI at top
		fontRendererObj.drawString(invDino.getDisplayName().getUnformattedText(), LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
	}
}
