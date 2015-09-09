package com.arkcraft.mod.core.book;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class BookDeserializer implements JsonDeserializer<BookDocument> {

	@Override
	public BookDocument deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
		final JsonObject jObject = json.getAsJsonObject();
		
		IPage[] pages = context.deserialize(jObject.get("entries"), IPage[].class);
		BookDocument doc = new BookDocument();
		doc.setEntries(pages);
		
		return doc;
	}

	
}
