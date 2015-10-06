package com.arkcraft.mod.core.machine.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.book.button.PageButton;
import com.arkcraft.mod.core.handlers.BookDrawHandler;
import com.arkcraft.mod.core.lib.LogHelper;

/**
 * @author BubbleTrouble / Vastatio (Page Content)
 */

@SuppressWarnings({"unchecked", "unused"})
public class GuiDosierScreen extends GuiScreen {

    private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/dino_book_gui.png");

	private int guiWidth = 256;
	private int guiHeight = 180;
	private int x;
	private int y;
	private int currentPage;
	private int oldPage;
	private int maxPages;
	public PageButton bButton;
	public PageButton nButton;
	public PageButton cat_bButton;
	private GuiButton dinos, reptiles, mammals, other;
	
	public enum CATEGORY {
		DINOS(0),
		REPTILES(1),
		MAMMALS(2),
		OTHER(3);
		
		int id;
		CATEGORY(int id) {
			this.id = id;
		}
		
		public int getID() { return id; }
	}
	
	private CATEGORY currentCategory;
	
	@Override
	public void initGui() {
		currentPage = 0;
		oldPage = 0;
		maxPages = 10;
		x = (width - guiWidth) / 2;
		y = (height - guiHeight) / 2;

		bButton = new PageButton(1, x + 20, y + guiHeight - 20, false);
		nButton = new PageButton(2, x + guiWidth - 36, y + guiHeight - 20, true);
		dinos = new GuiButton(3, x + 15, (y + (guiHeight / 2)) - 20, 100, 20, "Dinos");
		cat_bButton = new PageButton(4, x + 15, y + 15, false);
		reptiles = new GuiButton(5, x + 15, y + guiHeight - 40, 100, 20, "Reptiles");
		mammals = new GuiButton(6, (x + (guiWidth / 2)) + 15, (y + (guiHeight / 2)) - 20, 100, 20, "Mammals");
		other = new GuiButton(7, (x + (guiWidth / 2)) + 15, y + guiHeight - 40, 100, 20, "Other");
		this.buttonList.add(bButton);
		this.buttonList.add(nButton);
		this.buttonList.add(dinos);
		this.buttonList.add(cat_bButton);
		this.buttonList.add(reptiles);
		this.buttonList.add(mammals);
		this.buttonList.add(other);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.mc.getTextureManager().bindTexture(texture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		LogHelper.info("CurrentPage: " + currentPage);
		this.drawTexturedModalRect(x, y, 0, 0, guiWidth, guiHeight);
		BookDrawHandler.drawPages(this.fontRendererObj, mouseX, mouseY, currentPage, currentCategory, this);
		if(currentCategory != null) {
			bButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
			nButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
			if(currentPage == 0) cat_bButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
		}
		if(currentCategory == null) {
			dinos.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
			reptiles.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
			mammals.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
			other.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {

			if (button.id == 1 && currentPage != 0 && currentCategory != null) currentPage -= 2;
			if (button.id == 2 && currentPage != maxPages && currentCategory != null) currentPage += 2;
			if(button.id == 3 && currentCategory == null) currentCategory = CATEGORY.DINOS;
			if(button.id == 4 && currentCategory != null) currentCategory = null;
			updateContent(button);
		}
	}

	void updateContent(GuiButton button) {
		if (maxPages % 2 == 1) {
			if (currentPage > maxPages) currentPage = maxPages;
		} else {
			if (currentPage > maxPages) currentPage = maxPages;
		}
		if (currentPage % 2 == 1) currentPage--;
		if (currentPage < 0) currentPage = 0;
		
		oldPage = currentPage;
	}
}
