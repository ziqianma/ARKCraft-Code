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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/***
 * 
 * @author Vastatio
 *
 */
@SideOnly(Side.CLIENT)
public class GuiSmithy extends GuiContainer {
	
	public static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/smithy.png");
	
	public GuiSmithy(InventoryPlayer invPlayer, World world, BlockPos pos) {
		super(new ContainerSmithy(invPlayer, world, pos));
		this.xSize = 182;
		this.ySize = 226;
	}
	
	public void onGuiClosed() { super.onGuiClosed(); }
	
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		this.fontRendererObj.drawString("Smithy", 91, 5, Color.darkGray.getRGB());
	}
	
	protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}
