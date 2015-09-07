package com.arkcraft.mod.core.machine.gui.book;

import net.minecraft.util.ResourceLocation;

import org.w3c.dom.Document;

import com.arkcraft.mod.core.Main;

public class BookData {

	public String unlocalizedName = new String();
	public String modID = new String();
	
	public ResourceLocation leftImage = new ResourceLocation(Main.MODID, "textures/gui/dino_book_left.png");
	public ResourceLocation rightImage = new ResourceLocation(Main.MODID, "textures/gui/dino_book_right.png");

	public ResourceLocation itemImage = new ResourceLocation(Main.MODID, "textures/items/dino_book.png");
	public Document doc = ManualReader.readManual("/assets/arkcraft/dossier/en_US/dinodossier.xml");
	public SmallFontRenderer font;
	public Boolean canTranslate = false;
	
	public Document getDoc() { return this.doc; }
	
	public String getFullUnlocalizedName() {
		return this.modID + ":" + this.unlocalizedName;
	}
}

