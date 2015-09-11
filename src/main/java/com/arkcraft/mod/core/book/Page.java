package com.arkcraft.mod.core.book;


public class Page implements IPage {
	
	public Page() {}
	
	@Override
	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiDossier dossier) {}
	
	@Override
	public boolean equals(Object o) { return o == this; }
}
