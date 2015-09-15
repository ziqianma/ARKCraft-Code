package com.arkcraft.mod.core.book;

import net.minecraft.util.ResourceLocation;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.book.fonts.SmallFontRenderer;
import com.arkcraft.mod.core.lib.LogHelper;
import com.google.gson.GsonBuilder;

public class BookData {
	
	public GsonBuilder gBuilder = new GsonBuilder();
	public BookDocument doc = DossierParser.parseJSON(gBuilder, "dossier/en_US/dossier.json");
	public String modID = new String();
	public String unlocalizedName = new String();
	public String tooltip = new String();
	
	public ResourceLocation leftImage = new ResourceLocation(Main.MODID, "textures/gui/dino_book_left.png");
	public ResourceLocation rightImage = new ResourceLocation(Main.MODID, "textures/gui/dino_book_right.png");
	public ResourceLocation itemImage = new ResourceLocation(Main.MODID, "textures/items/dino_book.png");
	
	public SmallFontRenderer font;
	public Boolean canTranslate = false;
	
	public BookDocument getBookDocument() {
		LogHelper.info(doc == null ? "BookDocument in BookData is null!" : "BookDocument in BookData is not null.");
		return this.doc; 
	}
	public String getUnlocalizedName() { return unlocalizedName; }
	public GsonBuilder getGsonBuilder() { return gBuilder; }
	
}
