package com.arkcraft.mod.core.book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.machine.gui.PageButton;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class GuiDossier extends GuiScreen {
	
	private ItemStack dossierItem;
	private int guiWidth = 256;
	private int guiHeight = 180;
	private int currentPage;
	private int maxPages;
	private BookDocument dossier;
	
	private PageButton nButton, prevButton;
	private SmallFontRenderer fonts = DClient.fonts;
	private static ResourceLocation bookRight;
	private static ResourceLocation bookLeft;
	private BookData bData;
	
	private IPage pageLeft;
	private IPage pageRight;
	
	public GuiDossier(ItemStack stack, BookData data) {
		this.mc = Minecraft.getMinecraft();
		this.dossierItem = stack;
		currentPage = 0;
		dossier = data.getBookDocument();
		if(data.font != null) this.fonts = data.font;
		bookLeft = data.leftImage;
		bookRight = data.rightImage;
		this.bData = data;
	}
	
	@SuppressWarnings("unchecked")
	public void initGui() {
		maxPages = dossier.getEntries().length;
		updateText();
		int xPos = (this.width) / 2;
		this.buttonList.add(this.prevButton = new PageButton(1, xPos + 20, 180, false));
		this.buttonList.add(this.nButton = new PageButton(2, xPos + guiWidth - 36, 180, true));
	}
	
	protected void actionPerformed(GuiButton button) {
		if(button.enabled) {
			if(button.id == 1) currentPage += 2;
			if(button.id == 2) currentPage -= 2;
			
			updateText();
		}
	}
	
	public void updateText() {
		if(maxPages % 2 == 1) {
			if(currentPage > maxPages) currentPage = maxPages;
			else {
				if(currentPage >= maxPages) currentPage = maxPages - 2;
			}
			if(currentPage % 2 == 1) currentPage--;
			if(currentPage < 0) currentPage = 0;
			
			IPage[] pages = dossier.getEntries();
			IPage page = pages[currentPage];
			
			Class<? extends IPage> clazz = page.getType();
			if(clazz != null) {
				try {
					pageLeft = clazz.newInstance();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			else {
				pageLeft = null;
			}
			page = pages[currentPage+1];
			if(page != null) {
				Class<? extends IPage> newClass = page.getType();
				if(newClass != null) {
					try {
						pageRight = newClass.newInstance();
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				else {
					pageLeft = null;
				}
			}
			else {
				pageRight = null;
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        /* Book Right drawing! */
        this.mc.getTextureManager().bindTexture(bookRight);
        int localWidth = (this.width / 2);
        byte localHeight = 8;
        this.drawTexturedModalRect(localWidth, localHeight, 0, 0, this.guiWidth, this.guiHeight);
        
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        /* Book Left Drawing! */
        this.mc.getTextureManager().bindTexture(bookLeft);
        localWidth = localWidth - this.guiWidth;
        this.drawTexturedModalRect(localWidth, localHeight, 256 - this.guiWidth, 0, this.guiWidth, this.guiHeight);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
        
        if(pageLeft != null) {
        	pageLeft.draw(localWidth + 16, localHeight + 12, mouseX, mouseY, DClient.fonts, bData.canTranslate, this);
        }
        if(pageRight != null) {
        	pageLeft.draw(localWidth + 220, localHeight + 12, mouseX, mouseY, DClient.fonts, bData.canTranslate, this);
        }
        
        nButton.drawButton(mc, mouseX, mouseY);
        prevButton.drawButton(mc, mouseX, mouseY);
	}
	
	public Minecraft getMC() {
        return mc;
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
	
}
