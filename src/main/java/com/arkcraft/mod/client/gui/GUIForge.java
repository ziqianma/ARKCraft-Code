package com.arkcraft.mod.client.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.container.ContainerInventoryForge;
import com.arkcraft.mod.common.tile.TileInventoryForge;

@SideOnly(Side.CLIENT)
public class GUIForge extends GuiContainer {
	
	public static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/forge_gui.png");
	private TileInventoryForge tileEntity;
	
	public GUIForge(InventoryPlayer invPlayer, TileInventoryForge tileInventoryForge) {
		super(new ContainerInventoryForge(invPlayer, tileInventoryForge));
		this.tileEntity = tileInventoryForge;
		
		// Width and height of the gui:
		this.xSize = 175;
		this.ySize = 165;
	}
	
	public void onGuiClosed() { super.onGuiClosed(); }
	
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		// Display GUI name:
		String name = tileEntity.getDisplayName().getUnformattedText();
		final int LABEL_YPOS = 7;
		final int LABEL_XPOS = (xSize / 2) - (name.length() * 5 / 2);
		this.fontRendererObj.drawString(name, LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());

		// Add hovering text
		List<String> hoveringText = new ArrayList<String>();				

		// If the mouse is over the display text add the growth stage bar hovering text
		if (isInRect(guiLeft + LABEL_XPOS, guiTop + LABEL_YPOS, 50, 8, mouseX, mouseY)){
			hoveringText.add("Fertilizer compost time: ");
			int compostPercentage = (int) (tileEntity.getFractionCompostTimeComplete() * 100);
			hoveringText.add(compostPercentage + "%");
		}

		// If the mouse is over one of the thatch slots add the burn time indicator hovering text
		for (int row = 0; row < 2; row++) {
			for (int col = 0; col < 4; col++) {
				int index = col + 4 * row;
				if (tileEntity.secondsOfBurnTimeRemaining(index) > 0) {
					int x = guiLeft + ContainerInventoryForge.FORGE_SLOT_XPOS;
					int y = guiTop + ContainerInventoryForge.FORGE_SLOT_YPOS;
					if (isInRect(x + 18 * col, y + 18 * row, 16, 16, mouseX, mouseY)) {
						ItemStack stack = tileEntity.getStackInSlot(index);
						if (stack != null){
							String thatchName = stack.getItem().getItemStackDisplayName(stack);
							hoveringText.add(thatchName + " - composting Time Remaining:");
							hoveringText.add(tileEntity.secondsOfBurnTimeRemaining(index) + "s");
						}
					}
				}
			}
		}

		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()){
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
		}
	}
	
	protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY) {
		// Draw the GUI
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);		
	}
	
	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
		return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
	}
}
