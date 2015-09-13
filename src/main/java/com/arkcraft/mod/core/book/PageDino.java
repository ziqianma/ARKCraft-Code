package com.arkcraft.mod.core.book;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StatCollector;

import com.arkcraft.mod.core.lib.LogHelper;

public class PageDino extends Page {

	public String title;
	public EntityLivingBase model;
	public String temperance;
	public String diet;

	@Override
	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiDossier dossier) {
		LogHelper.info("DinoPage draw() called!");
		LogHelper.info(model == null ? "Model is null!" : "Model is not null.");
		LogHelper.info(title == null ? "Title is null!" : "Title is not null. Its value is: " + title);
		LogHelper.info(temperance == null ? "Temperance is null!" : "Temperance is not null. Its value is: " + temperance);
		LogHelper.info(diet == null ? "Diet is null!" : "Diet is not null. Its value is: " + diet);
		
		if(model != null) //GuiInventory.drawEntityOnScreen(guiLeft + 50, guiTop + 15, 25, -350F, -5F, model);
		
		if(title != null) {
			if(canTranslate) StatCollector.translateToLocal(title);
			renderer.drawString("\u00a7n" + title, guiLeft + 22 + renderer.getStringWidth(title) / 2, guiTop + 5, 0);
		}
		
		if(temperance != null) {
			if(canTranslate) StatCollector.translateToLocal(diet);
			renderer.drawString("Diet: " + diet, guiLeft + 22 + renderer.getStringWidth(diet) / 2, guiTop + 57, 0);
		}
		
		if(diet != null) {
			if(canTranslate) StatCollector.translateToLocal(temperance);
			renderer.drawString("Temperance: " + temperance, guiLeft + 22 + renderer.getStringWidth(temperance) / 2, guiTop + 65, 0);
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
