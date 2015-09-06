package com.arkcraft.mod.core.machine.gui.book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StatCollector;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.arkcraft.mod.core.entity.passive.EntityDodo;

/**
 * 
 * @author Vastatio
 *
 */

public class DinoPage extends BookPage {

	/* Dino Name */
	String title;
	/* the entity to project below the name */
	EntityLivingBase entityToProject;
	/* Any more text that is not a description, that will be a textpage. */
	String text;
	
	@Override
	public void readPageFromXML(Element element) {
		NodeList nodes = element.getElementsByTagName("title");
		if(nodes != null) title = nodes.item(0).getTextContent();
		
		nodes = element.getElementsByTagName("text");
		if(nodes != null) text = nodes.item(0).getTextContent();
		
		nodes = element.getElementsByTagName("model");
		if(nodes != null) {
			Class<? extends EntityLivingBase> clazz = PageData.getEntityClass(nodes.item(0).getTextContent());
			if(clazz != null) {
				try {
					entityToProject = clazz.newInstance();
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void renderContentLayer(int localWidth, int localHeight, boolean canTranslate) {
		if(title != null) {
			if(canTranslate) title = StatCollector.translateToLocal(title);
			dossier.fonts.drawString("\u00a7n" + text, localWidth + 25 + dossier.fonts.getStringWidth(title) / 2, localHeight + 4, 0);
		}
		
		if(entityToProject != null) {
			GuiInventory.drawEntityOnScreen(localWidth + 50, localHeight + 15, 25, -350F, -5F, new EntityDodo(Minecraft.getMinecraft().theWorld));
		}
		
		if(text != null) {
			if(canTranslate) title = StatCollector.translateToLocal(title);
			dossier.fonts.drawSplitString(text, localWidth, localHeight, 178, 0);
		}
	}

}
