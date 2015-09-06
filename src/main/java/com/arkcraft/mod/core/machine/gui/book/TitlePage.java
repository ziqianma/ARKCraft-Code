package com.arkcraft.mod.core.machine.gui.book;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TitlePage extends BookPage {

	String title;
	ResourceLocation icon;
	
	
	@Override
	public void readPageFromXML(Element element) {
		NodeList nodes = element.getElementsByTagName("title");
		if(nodes != null) title = nodes.item(0).getTextContent();
		
		nodes = element.getElementsByTagName("icon");
		if(nodes != null) icon = PageData.getIcon(nodes.item(0).getTextContent());
	}

	@Override
	public void renderContentLayer(int localWidth, int localHeight, boolean canTranslate) {
		if(title != null) {
			if(canTranslate) StatCollector.translateToLocal(title);
			dossier.fonts.drawString("\u00a7n" + title, localWidth + 25 + dossier.fonts.getStringWidth(title) / 2, localHeight + 4, 0);
		}
		
		//TODO: decide on x, y values for the icons. Running out of time!
	}

	
}
