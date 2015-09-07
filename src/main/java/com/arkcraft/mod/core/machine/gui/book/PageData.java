package com.arkcraft.mod.core.machine.gui.book;

import java.util.HashMap;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.core.entity.passive.EntityBrontosaurus;
import com.arkcraft.mod.core.entity.passive.EntityDodo;

public class PageData {

	public static HashMap<String, Class<? extends BookPage>> pageClasses = new HashMap<String, Class<? extends BookPage>>();
	public static HashMap<String, Class<? extends EntityLivingBase>> entityClasses = new HashMap<String, Class<? extends EntityLivingBase>>();
	public static HashMap<String, ResourceLocation> icons;
	
	public static Class<? extends BookPage> getPageClass(String type) {
		return pageClasses.get(type);
	}
	
	public static void registerPage(String type, Class<? extends BookPage> newClass) {
		pageClasses.put(type, newClass);
	}
	
	public void readPages() { initPages(); }
	
	public void initPages() {
		PageData.registerPage("dino", DinoPage.class);
		PageData.registerPage("blank", BlankPage.class);
		PageData.registerPage("text", TextPage.class);
		PageData.registerPage("category", CategoryPage.class);
		PageData.registerPage("title", TitlePage.class);
	}
	
	public static Class<? extends EntityLivingBase> getEntityClass(String type) { return entityClasses.get(type); }
	public static void registerEntityClass(String type, Class<? extends EntityLivingBase> newClass) { entityClasses.put(type, newClass); }
	
	public void readEntityClasses() { initEntityClasses(); }
	
	public void initEntityClasses() {
		PageData.registerEntityClass("raptor", EntityRaptor.class);
		PageData.registerEntityClass("brontosaurus", EntityBrontosaurus.class);
		PageData.registerEntityClass("dodo", EntityDodo.class);
	}
	
	public static ResourceLocation getIcon(String type) {
		return icons.get(type);
	}
	
	public static void registerIcon(String type, ResourceLocation location) {
		icons.put(type, location);
	}
	
	public void readIcons() { initIcons(); }
	
	public void initIcons() {
		PageData.registerIcon("dinoIcon", new ResourceLocation(Main.MODID, "arkcraft/dossier/dino_icon.png"));
	}
}
