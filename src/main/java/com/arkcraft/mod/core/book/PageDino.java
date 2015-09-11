package com.arkcraft.mod.core.book;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StatCollector;

public class PageDino extends Page {

	public String title;
	public EntityLivingBase model;
	public String temperance;
	public String diet;

	@Override
	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiDossier dossier) {
		if(title != null) {
			if(canTranslate) StatCollector.translateToLocal(title);
			renderer.drawString("\u00a7n" + title, guiLeft + 25 + renderer.getStringWidth(title) / 2, guiTop + 5, 0);
		}
		
		if(model != null) {
			float angleX = -350F;
			float angleY = -5F;
			int size = 25;
			GuiInventory.drawEntityOnScreen(guiLeft + 50, guiTop + 15, size, angleX, angleY, model);
		}
		
		if(temperance != null) {
			if(canTranslate) StatCollector.translateToLocal(temperance);
			renderer.drawString(temperance, guiLeft + 25 + renderer.getStringWidth(temperance), guiTop + 22, 0);
		}
		
		if(diet != null) {
			if(canTranslate) StatCollector.translateToLocal(diet);
			renderer.drawString(temperance, guiLeft + 25 + renderer.getStringWidth(diet), guiTop + 27, 0);
		}
	}
	
	public String getTitle() { return title; }
	public String getTemperance() { return temperance; }
	public String getDiet() { return diet; }
	public EntityLivingBase getEntityModel() { return model; }
	
	public void setTitle(String title) { this.title = title; }
	public void setTemperance(String temperance) { this.temperance = temperance; }
	public void setDiet(String diet) { this.diet = diet; }
	public void setEntityModel(EntityLivingBase model) { this.model = model; }
	
}
