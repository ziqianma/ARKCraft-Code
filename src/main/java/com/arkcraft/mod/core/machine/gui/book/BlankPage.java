package com.arkcraft.mod.core.machine.gui.book;

import org.w3c.dom.Element;

/**
 * A Blank Page. 
 */
public class BlankPage extends BookPage {

	@Override
	public void readPageFromXML(Element element) {}
	
	@Override
	public void renderContentLayer(int localWidth, int localHeight, boolean canTranslate) {}
}
