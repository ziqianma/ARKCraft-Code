package com.arkcraft.mod.core.book;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.arkcraft.mod.core.Main;

public class DossierInfo {

	BookData data = new BookData();
	
	public DossierInfo() {
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		data = initManual(data, "dossier", "Knowledge is Power" + EnumChatFormatting.GOLD, side == Side.CLIENT ? DClient.dossier : null, "textures/items/dino_book.png");
	}
	
	public BookData initManual(BookData data, String unlocalizedName, String tooltip, BookDocument doc, String itemImage) {
		data.unlocalizedName = unlocalizedName;
		data.modID = Main.MODID;
		data.itemImage = new ResourceLocation(data.modID, itemImage);
		data.doc = doc;
		data.tooltip = tooltip;
		BookDataStore.addBook(data);
		return data;
	}
}
