package com.arkcraft.mod.core.book;

import java.util.HashMap;

import net.minecraft.entity.EntityLivingBase;

public class PageData {

	public static HashMap<String, Class<? extends IPage>> pageClasses = new HashMap<String, Class<? extends IPage>>();
	public static HashMap<String, Class<? extends EntityLivingBase>> entityClasses = new HashMap<String, Class<? extends EntityLivingBase>>();
	
	public static void addBookPage(String type, Class<? extends IPage> page) { pageClasses.put(type, page); }
	public static Class<? extends IPage> getPageClass(String type) { return pageClasses.get(type); }
	
	public static void addModel(String type, Class<? extends EntityLivingBase> modelClass) { entityClasses.put(type, modelClass); }
	public static Class<? extends EntityLivingBase> getModelClass(String type) { return entityClasses.get(type); }
}
