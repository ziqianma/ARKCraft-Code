package com.arkcraft.mod.core.book;

import net.minecraft.util.ResourceLocation;

import com.arkcraft.mod.core.Main;
import com.google.gson.GsonBuilder;

public class BookData {

	public BookDocument doc = DossierParser.parseJSON(new GsonBuilder(), "/assets/arkcraft/dossier/en_US/dossier.json");
	public String modID = new String();
	public String unlocalizedName = new String();
	public String tooltip = new String();
	
	public ResourceLocation leftImage = new ResourceLocation(Main.MODID, "textures/gui/dino_book_left.png");
	public ResourceLocation rightImage = new ResourceLocation(Main.MODID, "textures/gui/dino_book_right.png");
	public ResourceLocation itemImage = new ResourceLocation(Main.MODID, "textures/items/dino_book.png");
	
	public SmallFontRenderer font;
	public Boolean canTranslate = false;
	
	public BookDocument getBookDocument() { return this.doc; }
	public String getUnlocalizedName() { return unlocalizedName; }
	
}
