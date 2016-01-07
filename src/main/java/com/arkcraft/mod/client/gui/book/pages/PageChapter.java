package com.arkcraft.mod.client.gui.book.pages;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import com.arkcraft.mod.client.gui.book.GuiDossier;
import com.arkcraft.mod.client.gui.book.fonts.SmallFontRenderer;
import com.arkcraft.mod.common.ARKCraft;

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
			for(int i = 0; i < links.length; i++) {
				if(links[i] != null) {
					if(canTranslate) StatCollector.translateToLocal(links[i].getLinkText());
					ResourceLocation loc = links[i].getLinkIcon(ARKCraft.MODID);
					if(loc != null) {
						int iw = 32, ih = 32;
						Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
						/* Texture Draw Position is offset by 20 from the middle of the page. Height offset is 40 pixels from each selection */
						GuiDossier.drawModalRectWithCustomSizedTexture((guiLeft + (dossier.guiWidth-iw) / 2) - 20, guiTop + 20 + (i * iw + 8), 0, 0, iw, ih, iw, ih);
					}
				}
			}
		}
	}
	
	public String getTitle() { return title; }
	public LinkObj[] getLinkObjects() { return links; }
}
