package com.arkcraft.mod.core.book;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PageDeserializer implements JsonDeserializer<IPage> {

	@Override
	public IPage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		JsonObject jObject = json.getAsJsonObject();
		try {
			final IPage page = PageData.getPageClass(jObject.get("type").getAsString()).newInstance();
			if(page instanceof PageDino && page != null) {
				PageDino dino = (PageDino)page;
				dino.setTitle(jObject.get("title").getAsString());
				dino.setDiet(jObject.get("diet").getAsString());
				dino.setTemperance(jObject.get("temperance").getAsString());
				dino.setEntityModel(PageData.getModelClass(jObject.get("model").getAsString()).newInstance());
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
