package com.arkcraft.mod.core.book;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.core.entity.passive.EntityBrontosaurus;
import com.arkcraft.mod.core.entity.passive.EntityDodo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DClient extends DCommon {

	public static Minecraft mc;
	public static SmallFontRenderer fonts;
	public static BookDocument dossier;
	public static DossierInfo info;
	
	public void init() {
		mc = Minecraft.getMinecraft();
		fonts = new SmallFontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
		
		String lang = mc.getLanguageManager().getCurrentLanguage().getLanguageCode();
		registerPageClasses();
		registerModelClasses();
		GsonBuilder gBuilder = new GsonBuilder();
		gBuilder.registerTypeAdapter(BookDocument.class, new BookDeserializer());
		gBuilder.registerTypeAdapter(IPage.class, new PageDeserializer());
		BookDocument dossier_cl = readManual(gBuilder, "dossier/" + lang + "/dossier.json");
		dossier = dossier_cl != null ? dossier_cl : readManual(gBuilder, "dossier/en_US/dossier.json");
		info = new DossierInfo();
	}
	
	public BookDocument readManual(GsonBuilder gBuilder, String location) {
		Gson gson = gBuilder.create();
		ResourceLocation rloc = new ResourceLocation(Main.MODID, location);
		try {
			InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(rloc).getInputStream();
			Reader reader = new InputStreamReader(stream, "UTF-8");
			BookDocument doc = gson.fromJson(reader, BookDocument.class);
			return doc;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Override
	public void registerPageClasses() {
		PageData.addBookPage("dino", PageDino.class);
		PageData.addBookPage("text", PageText.class);
	}
	
	@Override
	public void registerModelClasses() {
		PageData.addModel("raptor", EntityRaptor.class);
		PageData.addModel("brontosaurus", EntityBrontosaurus.class);
		PageData.addModel("dodo", EntityDodo.class);
	}
	
}
