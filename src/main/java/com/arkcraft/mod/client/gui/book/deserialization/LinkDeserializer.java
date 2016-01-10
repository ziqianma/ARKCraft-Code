package com.arkcraft.mod.client.gui.book.deserialization;

import java.lang.reflect.Type;

import com.arkcraft.module.core.client.gui.book.pages.LinkObj;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;


public class LinkDeserializer implements JsonDeserializer<LinkObj>  {

	@Override
	public LinkObj deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject obj = json.getAsJsonObject();
		try {
			String linkText = obj.get("linkText").getAsString();
			String linkIcon = obj.get("linkIcon").getAsString();
			int linkTo = obj.get("linkTo").getAsInt();
			
		}
		catch(Exception e) {
			
		}
		return null;
	}
}
