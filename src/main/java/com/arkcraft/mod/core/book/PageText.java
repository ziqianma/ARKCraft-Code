package com.arkcraft.mod.core.book;

import net.minecraft.util.StatCollector;

public class PageText extends Page {

	public String text;
	
	@Override
	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiDossier dossier) {
		if(text != null) {
			if(canTranslate) StatCollector.translateToLocal(text);
			renderer.drawSplitString(text, guiLeft + 25 + renderer.getStringWidth(text), guiTop + 25, 104, 0);
		}
	}
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	
	
}
