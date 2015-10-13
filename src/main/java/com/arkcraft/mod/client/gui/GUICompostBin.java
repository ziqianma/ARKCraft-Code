package com.arkcraft.mod.client.gui;

import java.awt.Color;

import com.arkcraft.mod.common.ARKCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.common.container.ContainerCompostBin;

@SideOnly(Side.CLIENT)
public class GUICompostBin extends GuiContainer
{
	public static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/compost_bin_gui.png");
	public String name = "compost_bin";
	
	public GUICompostBin(InventoryPlayer invPlayer, World world, BlockPos pos) {
		super(new ContainerCompostBin(invPlayer, world, pos));
		this.xSize = 182;
		this.ySize = 226;
	}
	
	public void onGuiClosed() { super.onGuiClosed(); }
	
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		this.fontRendererObj.drawString(name, (int)(xSize / 2) - name.length(), 5, Color.darkGray.getRGB());
	}
	
	protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}