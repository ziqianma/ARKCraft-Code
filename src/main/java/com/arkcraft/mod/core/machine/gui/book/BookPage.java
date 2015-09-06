package com.arkcraft.mod.core.machine.gui.book;

import org.w3c.dom.Element;

public abstract class BookPage {

	protected GuiDossier dossier;
	protected int side;
	
	public void init(GuiDossier dossier, int side) {
		this.dossier = dossier;
		this.side = side;
	}
	
	public abstract void readPageFromXML(Element element);
	public void renderBackgroundLayer(int localWidth, int localHeight) {}
	public abstract void renderContentLayer(int localWidth, int localHeight, boolean canTranslate);
	
}
