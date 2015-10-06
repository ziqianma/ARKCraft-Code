package com.arkcraft.mod.core.book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.book.button.PageButton;
import com.arkcraft.mod.core.book.fonts.SmallFontRenderer;
import com.arkcraft.mod.core.book.pages.Page;
import com.arkcraft.mod.core.book.proxy.DClient;
import com.arkcraft.mod.core.lib.LogHelper;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
/***
 * 
 * @author Vastatio
 *
 */
public class GuiDossier extends GuiScreen {

	private ItemStack dossierItem;
	public int guiWidth = 156;
	public int guiHeight = 220;
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
		LogHelper.info(data == null ? "Data in GuiDossier is null!"
				: "Data is not null in GuiDossier");
		dossier = data.getBookDocument();
		LogHelper.info(dossier == null ? "Dossier in GuiDossier is null!" : "Dossier is not null in GuiDossier");
		if (data.font != null)
			this.fonts = data.font;
		bookLeft = data.leftImage;
		bookRight = data.rightImage;
		this.bData = data;
	}
	
	@SuppressWarnings("unchecked")
	public void initGui() {
		LogHelper.info("initGui() is called!");
		currentPage = 0;
		maxPages = dossier.getEntries().length;
		updateContent();
		int x = (this.width - guiWidth) / 2;
		int y = (this.height - guiHeight) / 2;
		this.buttonList.add(this.nButton = new PageButton(1, x + guiWidth
				+ 26, y + guiHeight - 25, true));
		this.buttonList.add(this.prevButton = new PageButton(2, x - 45, y + guiHeight - 25,
				false));
		
	}

	protected void actionPerformed(GuiButton button) {
		LogHelper.info("actionPerformed() is called!");
		if (button.enabled) {
			if (button.id == 1 && currentPage != maxPages)
				currentPage += 2;
			if (button.id == 2 && currentPage != 0)
				currentPage -= 2;

			updateContent();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//LogHelper.info("CurrentPage: " + currentPage);
		LogHelper.info("Mouse X: " + mouseX + ", Mouse Y: " + mouseY);
		int x = (width / 2);
		int y = (height - this.guiHeight) / 2;

		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.mc.getTextureManager().bindTexture(bookRight);
		this.drawTexturedModalRect(x, y, 0, 0, this.guiWidth, this.guiHeight);

		GL11.glColor4f(1F, 1F, 1F, 1F);
		this.mc.getTextureManager().bindTexture(bookLeft);
		this.drawTexturedModalRect(x - guiWidth, y, 256 - this.guiWidth, 0, this.guiWidth,
				this.guiHeight);

		super.drawScreen(mouseX, mouseY, partialTicks);

		LogHelper.info(pageLeft == null ? "pageLeft is null!" : "pageLeft is not null");
		LogHelper.info(pageRight == null ? "pageRight is null!" : "pageRight is not null");
		if (pageLeft != null && pageRight != null) {
			LogHelper.info("Trying to draw the left page!");
			pageLeft.draw(x - guiWidth, y + 12, mouseX, mouseY, fonts,
					bData.canTranslate, this);

			LogHelper.info("Trying to draw the right page!");
			pageRight.draw(x, y + 12, mouseX, mouseY, fonts,
					bData.canTranslate, this);
		}

		nButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
		prevButton.drawButton(Minecraft.getMinecraft(), mouseX, mouseY);
	}

	void updateContent() {
		LogHelper.info("updateContent() is called!");
		if (maxPages % 2 == 1)
        {
            if (currentPage > maxPages)
                currentPage = maxPages;
        }
        else
        {
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
