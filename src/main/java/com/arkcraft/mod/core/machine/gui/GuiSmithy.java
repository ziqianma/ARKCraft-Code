package com.arkcraft.mod.core.machine.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;

/***
 * 
 * @author Vastatio
 *
 */
@SideOnly(Side.CLIENT)
public class GuiSmithy extends GuiContainer {

	private static final ResourceLocation tex = 
			new ResourceLocation(Main.MODID, "textures/gui/smithy.png");
	
	public GuiSmithy(InventoryPlayer invPlayer, World world, BlockPos pos) {
		super(new ContainerSmithy(invPlayer, world, pos));
		
		this.xSize = 176;
		this.ySize = 204;
		
	}
	
	public void onGuiClosed() { super.onGuiClosed(); }
	
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		this.fontRendererObj.drawString(StatCollector.translateToLocal("Smithy"), 50, 5, 0x000000);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY) {
		/* Clear colors, bind texture, render */
		GL11.glColor4f(1F, 1F, 1F, 1F);
		if(tex == null) System.err.println("File is not found at path: " + tex.getResourcePath());
		Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

}
