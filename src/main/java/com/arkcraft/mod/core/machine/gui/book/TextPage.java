package com.arkcraft.mod.core.machine.gui.book;

import net.minecraft.util.StatCollector;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TextPage extends BookPage {

	private String text;
	
	@Override
	public void readPageFromXML(Element element) {
		/* Create a list of nodes that have the <text> tag */
		NodeList nodes = element.getElementsByTagName("text");
		if(nodes != null) text = nodes.item(0).getTextContent();
	}
	
	@Override
	public void renderContentLayer(int localWidth, int localHeight, boolean canTranslate) {
		if(canTranslate) text = StatCollector.translateToLocal(text);
		dossier.fonts.drawSplitString(text, localWidth, localHeight, 178, 0);
	}
}
