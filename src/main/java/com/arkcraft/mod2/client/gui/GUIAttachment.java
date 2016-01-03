package com.arkcraft.mod2.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod2.common.container.ContainerInventoryAttachment;
import com.arkcraft.mod2.common.tile.TileInventoryAttachment;

/***
 * 
 * @author wildbill22
 *
 */
@SideOnly(Side.CLIENT)
public class GUIAttachment extends GuiContainer
{
	/** ResourceLocation takes 2 parameters: ModId, path to texture at the location:
	 * "src/minecraft/assets/modid/"
	 * 
	 * I have provided a sample texture file that works with this tutorial. Download it
	 * from Forge_Tutorials/textures/gui/
	 */
	private static final ResourceLocation iconLocation = new ResourceLocation("inventoryitemmod", "textures/gui/inventoryitem.png");

	/** The inventory to render on screen */
	private final TileInventoryAttachment inventory;

	public GUIAttachment(EntityPlayer player,InventoryPlayer invPlayer, TileInventoryAttachment inventoryItem)
	{
		super(new ContainerInventoryAttachment(player, invPlayer, inventoryItem));
		this.inventory = inventoryItem;
	}
	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		String s = this.inventory.hasCustomName() ? this.inventory.getName() : I18n.format(this.inventory.getName());
		this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 0, 4210752);
		this.fontRendererObj.drawString(I18n.format("container.inventory"), 26, this.ySize - 96 + 4, 4210752);
	}

	protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY) {
		// Draw the GUI
		Minecraft.getMinecraft().getTextureManager().bindTexture(iconLocation);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);		
	}
}