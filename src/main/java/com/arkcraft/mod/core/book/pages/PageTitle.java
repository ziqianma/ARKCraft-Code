package com.arkcraft.mod.core.book.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.book.GuiDossier;
import com.arkcraft.mod.core.book.fonts.SmallFontRenderer;

public class PageTitle extends Page {

	public String title;
	public String text;
	public String image;

	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiDossier dossier) {
		if(image != null) {
			ResourceLocation imagePath = new ResourceLocation(Main.MODID, image);
			if(imagePath != null) {
				Minecraft.getMinecraft().getTextureManager().bindTexture(imagePath);
			}
			dossier.drawTexturedModalRect(guiLeft + (dossier.guiWidth-64)/2, guiTop + 15,0,0,64,64);
			
		}
		
		if(title != null) {
			if(canTranslate) StatCollector.translateToLocal(title);
			renderer.drawString(title, guiLeft + (dossier.guiWidth-renderer.getStringWidth(title)) / 2, guiTop + 5, 0);
		}
		
		if(text != null) {
			if(canTranslate) StatCollector.translateToLocal(text);
			renderer.drawSplitString(text, guiLeft + (dossier.guiWidth-renderer.getStringWidth(title)) / 2, guiTop + 85, dossier.guiWidth - 20, 0);
		}
	}
	
	public String getTitle() { return title; }
	public String getText() { return text; }
	public String getImagePath() { return image; }
	
}
