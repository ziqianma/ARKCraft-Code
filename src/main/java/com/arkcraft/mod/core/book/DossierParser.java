package com.arkcraft.mod.core.book;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.lib.LogHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DossierParser {

	public static BookDocument parseJSON(GsonBuilder gBuilder, String location) {
		gBuilder = new GsonBuilder();
		gBuilder.registerTypeAdapter(BookDocument.class, new BookDeserializer());
		gBuilder.registerTypeAdapter(IPage.class, new PageDeserializer());
		Gson gson = gBuilder.create();
		ResourceLocation rloc = new ResourceLocation(Main.MODID, location);
		try {
			LogHelper.info("Trying to read in JSON File....");
			InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(rloc).getInputStream();
			LogHelper.info(stream == null ? "Stream is null!" : "Stream is not null.");
			Reader reader = new InputStreamReader(stream, "UTF-8");
			BookDocument doc = gson.fromJson(reader, BookDocument.class);
			return doc;
		}
		catch(Exception e) {
			LogHelper.info("Failed to read JSON file!");
			e.printStackTrace();
		}
		return null;
	}
	
}
