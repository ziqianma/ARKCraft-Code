package com.arkcraft.mod.core.machine.gui.book;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StatCollector;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CategoryPage extends BookPage {

	String title;
	EntityLivingBase[] models;
	String[] modelText;
	
	@Override
	public void readPageFromXML(Element element) {
		NodeList nodes = element.getElementsByTagName("title");
		if(nodes != null) title = nodes.item(0).getTextContent();
		
		nodes = element.getElementsByTagName("chapter");
		modelText = new String[nodes.getLength()];
		models = new EntityLivingBase[nodes.getLength()];
		for(int i = 0; i < nodes.getLength(); i++) {
			NodeList children = nodes.item(i).getChildNodes();
			modelText[i] = children.item(1).getTextContent();
			Class<? extends EntityLivingBase> clazz = PageData.getEntityClass(children.item(3).getTextContent());
			try { models[i] = clazz.newInstance(); } catch(Exception e) { e.printStackTrace(); }
		}
	}
	
	@Override
	public void renderContentLayer(int localWidth, int localHeight, boolean canTranslate) {
		if(title != null) {
			if(canTranslate) StatCollector.translateToLocal(title);
			dossier.fonts.drawString("\u00a7n" + title, localWidth + 25 + dossier.fonts.getStringWidth(title) / 2, localHeight + 4, 0);
		}
		//TODO: Draw the models and the model text.
	}
	
	
}
