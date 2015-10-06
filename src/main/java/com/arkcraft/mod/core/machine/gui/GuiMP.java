package com.arkcraft.mod.core.machine.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/* Mortar and Pestle */
/***
 * 
 * @author Vastatio
 *
 */
public class GuiMP extends GuiContainer {

	public String name = "Mortar and Pestle";
	public static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/mortar_and_pestle.png");
	
	public GuiMP(InventoryPlayer invPlayer, World w, BlockPos pos) {
		super(new ContainerInventoryMP(invPlayer, w, pos));
		this.xSize = 177;
		this.ySize = 166;
	}
	
	public void onGuiClosed() { super.onGuiClosed(); }
	
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		this.fontRendererObj.drawString(name, (int)(xSize / 2) - name.length(), 5, Color.darkGray.getRGB());
	}
	
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
}
