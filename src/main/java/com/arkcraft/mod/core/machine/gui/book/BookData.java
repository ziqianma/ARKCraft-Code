package com.arkcraft.mod.core.machine.gui.book;

import net.minecraft.util.ResourceLocation;

import org.w3c.dom.Document;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.lib.LogHelper;

public class BookData {

	public String unlocalizedName = new String();
	public String modID = new String();
	
	public ResourceLocation leftImage = new ResourceLocation(Main.MODID, "textures/gui/dino_book_left.png");
	public ResourceLocation rightImage = new ResourceLocation(Main.MODID, "textures/gui/dino_book_right.png");

	public ResourceLocation itemImage = new ResourceLocation(Main.MODID, "textures/items/dino_book.png");
	/* FIXME Doc is null. */
	public Document doc = ManualReader.readManual("/assets/arkcraft/dossier/en_US/dinodossier.xml");
	public SmallFontRenderer font;
	public Boolean canTranslate = false;
	
	public Document getDoc() {
		LogHelper.info("getDoc() called");
		LogHelper.info(doc == null ? "Doc is null!" : "Doc is not null");
		return this.doc; 
	}
	
	public String getFullUnlocalizedName() {
		return this.unlocalizedName;
	}
}

