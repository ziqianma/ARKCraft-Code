package com.arkcraft.mod.core.book.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.book.GuiDossier;
import com.arkcraft.mod.core.book.fonts.SmallFontRenderer;

/**
 * Table of Contents Page.
 * @author Vastatio
 * 
 */
public class PageChapter extends Page {

	public String title;
	public LinkObj[] links;
	
	@Override
	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiDossier dossier) {
		
		if(title != null) {
			if(canTranslate) title = StatCollector.translateToLocal(title);
			renderer.drawString("\u00a7n" + title, guiLeft + (dossier.guiWidth-renderer.getStringWidth(title)) / 2, guiTop + 5, 0);
		}
	
		if(links != null) {

		}
	}
	
	public String getTitle() { return title; }
	
	public void drawIcon(String iconName, GuiDossier dossier, int guiTop, int guiLeft) {
		ResourceLocation loc = new ResourceLocation(Main.MODID, "textures/gui/icons.png");
		if(iconName == "dino") {
			if(loc != null) Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
			dossier.drawTexturedModalRect(guiLeft + (dossier.guiWidth-64)/2, guiTop + 30,64,64,64,64);
		}
		
		if(iconName == "mammal") {
			if(loc != null) Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
			dossier.drawTexturedModalRect(guiLeft + (dossier.guiWidth-64)/2, guiTop + 30,0,0,64,64);
		}
		
		if(iconName == "reptile") {
			if(loc != null) Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
			dossier.drawTexturedModalRect(guiLeft + (dossier.guiWidth-64)/2, guiTop + 30,0,64,64,64);
		}
		
		if(iconName == "other") {
			if(loc != null) Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
			dossier.drawTexturedModalRect(guiLeft + (dossier.guiWidth-64)/2, guiTop + 30,0,64,0,64);
		}
	}
	
	public LinkObj[] getLinkObjects() { return links; }
}
