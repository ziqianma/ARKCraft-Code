package com.arkcraft.mod.core.book;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.arkcraft.mod.core.Main;

public class PageTitle extends Page {

	public String title;
	public String text;
	public String image;

	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiDossier dossier) {
		if(title != null) {
			if(canTranslate) StatCollector.translateToLocal(title);
			renderer.drawString(title, guiLeft + 25 + renderer.getStringWidth(title), guiTop + 4, 0);
		}
		
		if(image != null) {
			ResourceLocation imagePath = new ResourceLocation(Main.MODID, image);
			if(imagePath != null) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(imagePath);
			}
		}
	}
	
	public String getTitle() { return title; }
	public String getText() { return text; }
	public String getImagePath() { return image; }
	
}
