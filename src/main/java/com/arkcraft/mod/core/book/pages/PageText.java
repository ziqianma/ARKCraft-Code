package com.arkcraft.mod.core.book.pages;

import com.arkcraft.mod.core.book.GuiDossier;
import com.arkcraft.mod.core.book.fonts.SmallFontRenderer;

import net.minecraft.util.StatCollector;

public class PageText extends Page {

	public String title;
	public String text;
	
	@Override
	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiDossier dossier) {
		if(title != null) {
			if(canTranslate) StatCollector.translateToLocal(title);
			renderer.drawString(title, guiLeft + (dossier.guiWidth-renderer.getStringWidth(title)) / 2, guiTop + 5, 0);
		} 
		
		if(text != null) {
			if(canTranslate) StatCollector.translateToLocal(text);
			renderer.drawSplitString(text, guiLeft + 25 + renderer.getStringWidth(text), guiTop + 25, dossier.guiWidth - 4, 0);
		}
	}
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	
}
