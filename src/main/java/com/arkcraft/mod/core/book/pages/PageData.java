package com.arkcraft.mod.core.book.pages;

import java.util.HashMap;

import net.minecraft.entity.EntityLivingBase;

public class PageData {

	public static HashMap<String, Class<? extends Page>> pageClasses = new HashMap<String, Class<? extends Page>>();
	public static HashMap<String, Class<? extends EntityLivingBase>> entityClasses = new HashMap<String, Class<? extends EntityLivingBase>>();
	
	public static void addBookPage(String type, Class<? extends Page> page) { pageClasses.put(type, page); }
	public static Class<? extends Page> getPageClass(String type) { return pageClasses.get(type); }
	
	public static void addModel(String type, Class<? extends EntityLivingBase> modelClass) { entityClasses.put(type, modelClass); }
	public static Class<? extends EntityLivingBase> getModelClass(String type) { return entityClasses.get(type); }
}
