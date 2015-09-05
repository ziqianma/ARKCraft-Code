package com.arkcraft.mod.core.machine.gui;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
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
public class GUITaming extends GuiContainer {
	
	public static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/taiming_gui.png");
	private InventoryTaming inventoryTaming;
	
	public GUITaming(InventoryPlayer invPlayer, InventoryTaming inventoryTaming, EntityPlayer player) {
		super(new ContainerInventoryTaming(invPlayer, inventoryTaming));
		this.inventoryTaming = inventoryTaming;
		inventoryTaming.playerTaming = player;
		
		// Width and height of the gui:
		this.xSize = 229;
		this.ySize = 218;
	}
	
	// some [x,y] coordinates of graphical elements
	final int UNCONSCIOUS_BAR_XPOS = 36;
	final int UNCONSCIOUS_BAR_YPOS = 47;
	final int UNCONSCIOUS_BAR_ICON_U = 0;   // texture position of the Unconscious bar
	final int UNCONSCIOUS_BAR_ICON_V = 219;
	final int UNCONSCIOUS_BAR_WIDTH = 160;
	final int UNCONSCIOUS_BAR_HEIGHT = 16;
	
	final int TAMING_BAR_XPOS = 36;
	final int TAMING_BAR_YPOS = 65;
	final int TAMING_BAR_ICON_U = 0;   // texture position of Taming
	final int TAMING_BAR_ICON_V = 235;
	final int TAMING_BAR_WIDTH = 160;
	final int TAMING_BAR_HEIGHT = 16;
	
	public void onGuiClosed() { super.onGuiClosed(); }
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);		
	}
	
	protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		// get unconscious progress as a double between 0 and 1
		double unconsciousProgress = inventoryTaming.unconciousLevel();
		// draw the unconscious progress bar
		drawTexturedModalRect(guiLeft + UNCONSCIOUS_BAR_XPOS, guiTop + UNCONSCIOUS_BAR_YPOS, UNCONSCIOUS_BAR_ICON_U, UNCONSCIOUS_BAR_ICON_V,
				(int)(unconsciousProgress * UNCONSCIOUS_BAR_WIDTH), UNCONSCIOUS_BAR_HEIGHT);

		// get taming progress as a double between 0 and 1
		double tamingProgress = inventoryTaming.tamingLevel();
		// draw the taming progress bar
		drawTexturedModalRect(guiLeft + TAMING_BAR_XPOS, guiTop + TAMING_BAR_YPOS, TAMING_BAR_ICON_U, TAMING_BAR_ICON_V,
				(int)(tamingProgress * TAMING_BAR_WIDTH), TAMING_BAR_HEIGHT);
	}
}
