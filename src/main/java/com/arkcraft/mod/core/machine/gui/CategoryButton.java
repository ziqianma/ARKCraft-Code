package com.arkcraft.mod.core.machine.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import com.arkcraft.mod.core.Main;

public class CategoryButton extends GuiButton {

	private static final ResourceLocation inButtonTexture = new ResourceLocation(Main.MODID, "textures/gui/category_button.png");
	
	public CategoryButton(int buttonId, int x, int y) {
		super(buttonId, x, y, 20, 19, "");
		
	}
	
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		if(this.visible) {
			mc.getTextureManager().bindTexture(inButtonTexture);
			this.drawTexturedModalRect(10, 10, 20, 19, 20, 19);
		}
	}

	
}
