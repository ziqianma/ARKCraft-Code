package com.arkcraft.mod.core.machine.gui.book;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import org.w3c.dom.Document;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.lib.LogHelper;

public class DossierInfo {
	
	BookData dossier = new BookData();
	
	public DossierInfo() {
		LogHelper.info("DossierInfo - Constructor Called");
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		dossier = initManual(dossier, "dossier", side == Side.CLIENT ? DClient.dossier : null, "textures/items/dino_book.png");
	}
	
	public BookData initManual(BookData data, String unlocName, Document xmlDoc, String itemImage) {
		data.unlocalizedName = unlocName;
		data.modID = Main.MODID;
		data.itemImage = new ResourceLocation(data.modID, itemImage);
		data.doc = xmlDoc;
		BookDataStore.addBook(data);
		LogHelper.info(data == null ? "DossierInfo - Data is null!" : "DossierInfo - Data is not null.");
		return data;
	}
}
