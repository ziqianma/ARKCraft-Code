package com.arkcraft.mod.core.book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.lib.LogHelper;
import com.arkcraft.mod.core.machine.gui.PageButton;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class GuiDossier extends GuiScreen {

	private ItemStack dossierItem;
	private int guiWidth = 128;
	private int guiHeight = 180;
	private int currentPage;
	private int maxPages;
	private BookDocument dossier;

	private PageButton nButton, prevButton;
	private SmallFontRenderer fonts = DClient.fonts;
	private static ResourceLocation bookRight;
	private static ResourceLocation bookLeft;
	private BookData bData;

	private Page pageLeft;
	private Page pageRight;

	public GuiDossier(ItemStack stack, BookData data) {
		LogHelper.info("GuiDossier constructor is called!");
		this.mc = Minecraft.getMinecraft();
		this.dossierItem = stack;
		currentPage = 0;
		LogHelper.info(data == null ? "Data in GuiDossier is null!"
				: "Data is not null in GuiDossier");
		dossier = data.getBookDocument();
		if (data.font != null)
			this.fonts = data.font;
		bookLeft = data.leftImage;
		bookRight = data.rightImage;
		this.bData = data;
	}

	@SuppressWarnings("unchecked")
	public void initGui() {
		LogHelper.info("initGui() is called!");
		maxPages = dossier.getEntries().length;
		updateContent();
		int xPos = (this.width - guiWidth) / 2;
		this.buttonList.add(this.prevButton = new PageButton(1, xPos - 45, 185,
				false));
		this.buttonList.add(this.nButton = new PageButton(2, xPos + guiWidth
				+ 26, 185, true));
	}

	protected void actionPerformed(GuiButton button) {
		LogHelper.info("actionPerformed() is called!");
		if (button.enabled) {
			if (button.id == 1)
				currentPage += 2;
			if (button.id == 2)
				currentPage -= 2;
			updateContent();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		LogHelper.info("drawScreen() in GuiDossier is called!");
		int x = (this.width / 2);
		int y = (height - this.guiHeight) / 2;

		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.mc.getTextureManager().bindTexture(bookRight);
		this.drawTexturedModalRect(x, y, 0, 0, this.guiWidth, this.guiHeight);

		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.mc.getTextureManager().bindTexture(bookLeft);
		x -= this.guiWidth;
		this.drawTexturedModalRect(x, y, 256 - this.guiWidth, 0, this.guiWidth,
				this.guiHeight);

		super.drawScreen(mouseX, mouseY, partialTicks);

		LogHelper.info(pageLeft == null ? "pageLeft is null!" : "pageLeft is not null");
		LogHelper.info(pageRight == null ? "pageRight is null!" : "pageRight is not null");
		if (pageLeft != null && pageRight != null) {
			LogHelper.info("Trying to draw the left page!");
			pageLeft.draw(x + 16, y + 12, mouseX, mouseY, fonts,
					bData.canTranslate, this);

			LogHelper.info("Trying to draw the right page!");
			pageRight.draw(x + 220, y + 12, mouseX, mouseY, fonts,
					bData.canTranslate, this);
		}

		nButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
		prevButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
	}

	void updateContent() {
		LogHelper.info("updateContent() is called!");
		if (maxPages % 2 == 1) {
			LogHelper.info("maxPages % 2 == 1");
			if (currentPage > maxPages)
				currentPage = maxPages;
		}
		else {
			if (currentPage >= maxPages)
				currentPage = maxPages - 2;
		}
		if (currentPage % 2 == 1)
				currentPage--;
		if (currentPage < 0)
				currentPage = 0;
		

		Page[] pages = dossier.getEntries();
		LogHelper.info(pages == null ? "Pages are null!"
				: "Pages are not null.");
		Page page = pages[currentPage];
		LogHelper
				.info(page == null ? "Page is null!" : "Page is not null.");
		if (page != null)
			pageLeft = page;

		page = pages[currentPage + 1];
		if (page != null)
			pageRight = page;
	}
	
	public Minecraft getMC() {
		return mc;
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

}