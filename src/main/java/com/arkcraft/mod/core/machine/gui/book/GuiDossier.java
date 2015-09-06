package com.arkcraft.mod.core.machine.gui.book;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.w3c.dom.Document;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.machine.gui.PageButton;


/***
 * 
 * @author Vastatio
 * @version 1.0
 */
@SuppressWarnings({"unused"})
public class GuiDossier extends GuiScreen {

    private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/dino_book_gui.png");
	
	private ItemStack dossierItem;
	private Document manualXML;

	private int guiWidth = 256;
	private int guiHeight = 180;
	private int currentPage;
	private int maxPages;
	
	private PageButton nButton, prevButton;
	public SmallFontRenderer fonts = new SmallFontRenderer(mc.gameSettings, new ResourceLocation("minecraft:textures/font/ascii.png"), mc.renderEngine, false);
	
	public GuiDossier() {
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
	}
	
}
