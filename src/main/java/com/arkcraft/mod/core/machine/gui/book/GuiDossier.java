package com.arkcraft.mod.core.machine.gui.book;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.opengl.GL11;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.lib.LogHelper;
import com.arkcraft.mod.core.machine.gui.PageButton;


/***
 * 
 * @author Vastatio
 * @version 1.0
 */
@SuppressWarnings({"unused"})
@SideOnly(Side.CLIENT)
public class GuiDossier extends GuiScreen {

    private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/dino_book_gui.png");
	
	private ItemStack dossierItem;

	private int guiWidth = 256;
	private int guiHeight = 180;
	private int currentPage;
	private int maxPages;
	private Document dossier;
	
	private PageButton nButton, prevButton;
	public SmallFontRenderer fonts = DClient.smallFontRenderer;
	private static ResourceLocation bookRight;
	private static ResourceLocation bookLeft;	
	private BookData bData;
	
	private BookPage pageLeft;
    private BookPage pageRight;
	
	public GuiDossier(ItemStack stack, BookData data) {
        this.mc = Minecraft.getMinecraft();
        this.dossierItem = stack;
        currentPage = 0; //Stack page
        LogHelper.info("Is data null?: " + data == null);
        dossier = data.getDoc();
        if (data.font != null) this.fonts = data.font;
        bookLeft = data.leftImage;
        bookRight = data.rightImage;
        this.bData = data;
	}
	
	@SuppressWarnings("unchecked")
	public void initGui() {
		maxPages = dossier.getElementsByTagName("page").getLength();
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
			
			NodeList nList = dossier.getElementsByTagName("page");
			Node node = nList.item(currentPage);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				Class<? extends BookPage> clazz = PageData.getPageClass(element.getAttribute("type"));
				if(clazz != null) {
					try {
						pageLeft = clazz.newInstance();
						pageLeft.init(this, 0);
						pageLeft.readPageFromXML(element);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
				else {
					pageLeft = null;
				}
			}
			
			//The right page! (currentPage + 1)
			node = nList.item(currentPage + 1);
			if(node != null && node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				Class<? extends BookPage> clazz = PageData.getPageClass(element.getAttribute("type"));
				if(clazz != null) {
					try {
						pageRight = clazz.newInstance();
						pageRight.init(this, 1);
						pageRight.readPageFromXML(element);
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
        	pageLeft.renderBackgroundLayer(localWidth + 16, localHeight + 12);
        	pageLeft.renderContentLayer(localWidth + 16, localHeight + 12, bData.canTranslate);
        }
        if(pageRight != null) {
        	pageRight.renderBackgroundLayer(localWidth + 220, localHeight + 12);
        	pageLeft.renderContentLayer(localWidth + 220, localHeight + 12, bData.canTranslate);
        }
        
	}
	
	public Minecraft getMC() {
        return mc;
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
	
}
