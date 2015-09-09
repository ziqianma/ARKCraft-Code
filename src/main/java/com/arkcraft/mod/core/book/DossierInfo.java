package com.arkcraft.mod.core.book;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.arkcraft.mod.core.Main;

public class DossierInfo {

	BookData data = new BookData();
	
	public DossierInfo() {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		data = initManual(data, "dossier", side == Side.CLIENT ? DClient.dossier : null, "textures/items/dino_book.png");
	}
	
	public BookData initManual(BookData data, String unlocalizedName, BookDocument doc, String itemImage) {
		data.unlocalizedName = unlocalizedName;
		data.modID = Main.MODID;
		data.itemImage = new ResourceLocation(data.modID, itemImage);
		data.doc = doc;
		BookDataStore.addBook(data);
		return data;
	}
}
