package com.arkcraft.mod.core.book.pages;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.StatCollector;

import com.arkcraft.mod.core.book.GuiDossier;
import com.arkcraft.mod.core.book.fonts.SmallFontRenderer;
import com.arkcraft.mod.core.lib.LogHelper;
/***
 * 
 * @author Vastatio
 *
 */
public class PageDino extends Page {

	public String title;
	public EntityLivingBase model;
	public String temperance;
	public String diet;
	//public String description;
	
	@Override
	public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiDossier dossier) {
		LogHelper.info("DinoPage draw() called!");
		LogHelper.info(model == null ? "Model is null!" : "Model is not null.");
		LogHelper.info(title == null ? "Title is null!" : "Title is not null. Its value is: " + title);
		LogHelper.info(temperance == null ? "Temperance is null!" : "Temperance is not null. Its value is: " + temperance);
		LogHelper.info(diet == null ? "Diet is null!" : "Diet is not null. Its value is: " + diet);
		
		if(model != null) {
			int size = 25;
			GuiInventory.drawEntityOnScreen(guiLeft + (dossier.guiWidth-size)/2, guiTop + 15, size, -350F, -5F, model);
		}
		
		if(title != null) {
			if(canTranslate) StatCollector.translateToLocal(title);
			renderer.drawString("\u00a7n" + title, guiLeft + (dossier.guiWidth-renderer.getStringWidth(title)) / 2, guiTop + 5, 0);
		}
		
		if(temperance != null) {
			if(canTranslate) StatCollector.translateToLocal(diet);
			renderer.drawString("Diet: " + diet, guiLeft + (dossier.guiWidth-renderer.getStringWidth("Diet: " + diet)) / 2, guiTop + 75, 0);
		}
		
		if(diet != null) {
			if(canTranslate) StatCollector.translateToLocal(temperance);
			renderer.drawString("Temperance: " + temperance, guiLeft + (dossier.guiWidth-renderer.getStringWidth("Temperance: " + temperance)) / 2, guiTop + 85, 0);
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
