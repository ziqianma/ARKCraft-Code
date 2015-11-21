package com.arkcraft.mod.client.gui.book;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.mod.client.gui.book.fonts.SmallFontRenderer;
import com.arkcraft.mod.common.ARKCraft;
import com.google.gson.GsonBuilder;

import net.minecraft.util.ResourceLocation;
/***
 * 
 * @author Vastatio
 *
 */
public class BookData {
	
	public GsonBuilder gBuilder = new GsonBuilder();
	public BookDocument doc = DossierParser.parseJSON(gBuilder, "dossier/en_US/dossier.json");
	public String modID = new String();
	public String unlocalizedName = new String();
	public String tooltip = new String();
	
	public ResourceLocation leftImage = new ResourceLocation(ARKCraft.MODID, "textures/gui/dino_book_left.png");
	public ResourceLocation rightImage = new ResourceLocation(ARKCraft.MODID, "textures/gui/dino_book_right.png");
	public ResourceLocation itemImage = new ResourceLocation(ARKCraft.MODID, "textures/items/dino_book.png");
	
	public SmallFontRenderer font;
	public Boolean canTranslate = false;
	
	public BookDocument getBookDocument() {
		LogHelper.info(doc == null ? "BookDocument in BookData is null!" : "BookDocument in BookData is not null.");
		return this.doc; 
	}
	public String getUnlocalizedName() { return unlocalizedName; }
	public GsonBuilder getGsonBuilder() { return gBuilder; }
	
}
