package com.arkcraft.mod.core.book;

import java.lang.reflect.Type;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PageDeserializer implements JsonDeserializer<IPage> {

	@Override
	public IPage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		JsonObject jObject = json.getAsJsonObject();
		try {
			IPage page = PageData.getPageClass(jObject.get("type").getAsString()).newInstance();
			if(page instanceof PageDino && page != null) {
				PageDino dino = (PageDino)page;
				dino.setTitle(jObject.get("title").getAsString());
				dino.setDiet(jObject.get("diet").getAsString());
				dino.setTemperance(jObject.get("temperance").getAsString());
				Class<? extends EntityLivingBase> clazz = PageData.getModelClass(jObject.get("model").getAsString());
				if(clazz != null) {
					try {
						EntityLivingBase model = clazz.getConstructor(World.class).newInstance(Minecraft.getMinecraft().theWorld);			
						dino.setEntityModel(model);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				return dino;
			}
			else if(page instanceof PageText && page != null) {
				PageText text = (PageText)page;
				text.setText(jObject.get("text").getAsString());
				return text;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
