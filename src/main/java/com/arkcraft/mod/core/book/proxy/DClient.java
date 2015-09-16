package com.arkcraft.mod.core.book.proxy;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.book.BookDocument;
import com.arkcraft.mod.core.book.DossierInfo;
import com.arkcraft.mod.core.book.deserialization.BookDeserializer;
import com.arkcraft.mod.core.book.deserialization.EntityLivingBaseDeserializer;
import com.arkcraft.mod.core.book.deserialization.PageDeserializer;
import com.arkcraft.mod.core.book.fonts.SmallFontRenderer;
import com.arkcraft.mod.core.book.pages.Page;
import com.arkcraft.mod.core.book.pages.PageChapter;
import com.arkcraft.mod.core.book.pages.PageData;
import com.arkcraft.mod.core.book.pages.PageDino;
import com.arkcraft.mod.core.book.pages.PageText;
import com.arkcraft.mod.core.book.pages.PageTitle;
import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.core.entity.passive.EntityBrontosaurus;
import com.arkcraft.mod.core.entity.passive.EntityDodo;
import com.arkcraft.mod.core.lib.LogHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
/***
 * 
 * @author Vastatio
 *
 */
public class DClient extends DCommon {

	public static Minecraft mc;
	public static SmallFontRenderer fonts;
	public static BookDocument dossier;
	public static DossierInfo info;
	
	public void init() {
		info = new DossierInfo();
		mc = Minecraft.getMinecraft();
		fonts = new SmallFontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.renderEngine, false);
		registerPageClasses();
		registerModelClasses();
		GsonBuilder gBuilder = info.data.getGsonBuilder();
		gBuilder.registerTypeAdapter(BookDocument.class, new BookDeserializer());
		gBuilder.registerTypeAdapter(Page.class, new PageDeserializer());
		gBuilder.registerTypeAdapter(EntityLivingBase.class, new EntityLivingBaseDeserializer());
		String lang = mc.getLanguageManager().getCurrentLanguage().getLanguageCode();
		BookDocument dossier_cl = readManual(gBuilder, "dossier/" + lang + "/dossier.json");
		dossier = dossier_cl != null ? dossier_cl : readManual(gBuilder, "dossier/en_US/dossier.json");
		if(dossier != null) info.data.doc = dossier;
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
		LogHelper.info("Registering page classes!");
		PageData.addBookPage("dino", PageDino.class);
		PageData.addBookPage("text", PageText.class);
		PageData.addBookPage("title", PageTitle.class);
		PageData.addBookPage("chapter", PageChapter.class);
	}
	
	@Override
	public void registerModelClasses() {
		LogHelper.info("Registering model classes!");
		PageData.addModel("raptor", EntityRaptor.class);
		PageData.addModel("brontosaurus", EntityBrontosaurus.class);
		PageData.addModel("dodo", EntityDodo.class);
	}
	
}
