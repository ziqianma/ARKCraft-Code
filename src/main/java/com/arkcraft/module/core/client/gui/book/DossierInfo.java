package com.arkcraft.module.core.client.gui.book;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.blocks.common.items.ARKCraftItems;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.client.gui.book.proxy.DClient;

/**
 * @author Vastatio
 */
public class DossierInfo
{

	public BookData data = new BookData();

	public DossierInfo()
	{
		Side side = FMLCommonHandler.instance().getEffectiveSide();
		data = initManual(data, ARKCraftItems.dino_book.getUnlocalizedName(),
				"Knowledge is Power" + EnumChatFormatting.GOLD,
				side == Side.CLIENT ? DClient.dossier : null, "textures/items/dino_book.png");
	}

	public BookData initManual(BookData data, String unlocalizedName, String tooltip, BookDocument doc, String itemImage)
	{
		LogHelper.info("Unlocalized Name is: " + unlocalizedName);
		data.unlocalizedName = unlocalizedName;
		data.modID = ARKCraft.MODID;
		data.itemImage = new ResourceLocation(data.modID, itemImage);
		data.doc = doc;
		data.tooltip = tooltip;
		BookDataStore.addBook(data);
		return data;
	}
}
