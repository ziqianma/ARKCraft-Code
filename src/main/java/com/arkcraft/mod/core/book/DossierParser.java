package com.arkcraft.mod.core.book;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import com.arkcraft.mod.core.Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DossierParser {

	public static BookDocument parseJSON(GsonBuilder builder, String location) {
		builder.registerTypeAdapter(BookDocument.class, new BookDeserializer());
		builder.registerTypeAdapter(IPage.class, new PageDeserializer());
		Gson gson = builder.create();
		InputStream stream = Main.class.getResourceAsStream(location);
		try {
			Reader reader = new InputStreamReader(stream, "UTF-8");
			BookDocument doc = gson.fromJson(reader, BookDocument.class);
			return doc;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
