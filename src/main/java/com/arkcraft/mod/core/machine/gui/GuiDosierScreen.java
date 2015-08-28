package com.arkcraft.mod.core.machine.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;

public class GuiDosierScreen extends GuiScreen {

    private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/dino_book_gui.png");

	private int guiWidth = 256;
	private int guiHeight = 180;
	private int x;
	private int y;
	private int currentPage;
	private int oldPage;
	private int maxPages;
	private PageButton bButton;
	private PageButton nButton;

	@Override
	public void initGui() {
		currentPage = 0;
		oldPage = 0;
		maxPages = 10;
		x = (width - guiWidth) / 2;
		y = (height - guiHeight) / 2;

		bButton = new PageButton(1, x + 20, y + guiHeight - 20, false);
		nButton = new PageButton(2, x + guiWidth - 36, y + guiHeight - 20, true);
		this.buttonList.add(bButton);
		this.buttonList.add(nButton);

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.mc.getTextureManager().bindTexture(texture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.drawTexturedModalRect(x, y, 0, 0, guiWidth, guiHeight);

	}

	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {

			if (button.id == 1 && currentPage != 0) currentPage -= 2;
			if (button.id == 2 && currentPage != maxPages) currentPage += 2;

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
