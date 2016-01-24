package com.arkcraft.module.item.client.gui;

import java.awt.Color;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.item.common.container.ContainerInventoryAttachment;
import com.arkcraft.module.item.common.tile.TileInventoryAttachment;

public class GUIAttachment extends GuiContainer
{
	private static final ResourceLocation iconLocation = new ResourceLocation(ARKCraft.MODID,
			"textures/gui/attachment_gui.png");

	/** The inventory to render on screen */
	private final TileInventoryAttachment inventory;

	public GUIAttachment(EntityPlayer player, InventoryPlayer InvPlayer, TileInventoryAttachment tileEntity)
	{
		super(new ContainerInventoryAttachment(player, InvPlayer, tileEntity));
		this.inventory = tileEntity;
		this.xSize = 175;
		this.ySize = 165;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		String name = inventory.getDisplayName().getUnformattedText();
		final int LABEL_YPOS = 7;
		final int LABEL_XPOS = (xSize / 2) - (name.length() * 5 / 2);
		this.fontRendererObj.drawString(name, LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(iconLocation);
		int k = (width - xSize) / 2;
		int l = (height - ySize) / 2;
		drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
	}
}