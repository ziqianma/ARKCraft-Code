package com.arkcraft.mod.core.machine.gui;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.blocks.crop_test.ARKContainerCropPlot;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUICropPlot extends GuiContainer {
	
	public static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/crop_plot_gui.png");
	public String name = "Crop Plot";
	private final InventoryPlayer playerInventory;
	
	public GUICropPlot(InventoryPlayer invPlayer, World world, BlockPos pos) {
		super(new ARKContainerCropPlot(invPlayer, world, pos));
		this.playerInventory = invPlayer;
		this.xSize = 176;
		this.ySize = 166;
	}
	
	public void onGuiClosed() { super.onGuiClosed(); }
	
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		
		//this.fontRendererObj.drawString(name, (int)(xSize / 2) - name.length(), 5, Color.darkGray.getRGB());
		 this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 1, 6, 4210752);
		 this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
	}
	
	protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}
