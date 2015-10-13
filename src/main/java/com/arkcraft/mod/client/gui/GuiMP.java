package com.arkcraft.mod.client.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.arkcraft.mod.common.tile.TileInventoryMP;
import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.common.ARKCraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

/* Mortar and Pestle */
/***
 * 
 * @author Vastatio
 *
 */
public class GuiMP extends GuiContainer {

	public String name = "Mortar and Pestle";
	public static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/mortar_and_pestle.png");
	private TileInventoryMP tileEntity;
    private GuiButton buttonCraftOne;
    private GuiButton buttonCraftAll;
    private GuiButton buttonPrevRecipe;
    private GuiButton buttonNextRecipe;

	public GuiMP(InventoryPlayer invPlayer, TileInventoryMP tileInventoryMP) {
		super(new ContainerInventoryMP(invPlayer, tileInventoryMP));
		this.tileEntity = tileInventoryMP;
		this.xSize = 175;
		this.ySize = 165;
	}
	
	// some [x,y] coordinates of graphical elements
	final int BLUEPRINT_XPOS = 34;
	final int BLUEPRINT_YPOS = 18;
	final int BLUEPRINT_WIDTH = 16;
	final int BLUEPRINT_HEIGHT = 16;

	final int LEFT_BUTTON_XPOS = 9;
	final int LEFT_BUTTON_YPOS = BLUEPRINT_YPOS + 2;
//	final int LEFT_BUTTON_ICON_U = 176;   // texture position of the water bar
//	final int LEFT_BUTTON_ICON_V = 17;
	final int LEFT_BUTTON_WIDTH = 20;
	final int LEFT_BUTTON_HEIGHT = 12;

	final int RIGHT_BUTTON_XPOS = 54;
	final int RIGHT_BUTTON_YPOS = BLUEPRINT_YPOS + 2;
//	final int RIGHT_BUTTON_ICON_U = 176;   // texture position of the water bar
//	final int RIGHT_BUTTON_ICON_V = 17;
	final int RIGHT_BUTTON_WIDTH = 20;
	final int RIGHT_BUTTON_HEIGHT = 12;

	final int CRAFT_BUTTON_XPOS = 19;
	final int CRAFT_BUTTON_YPOS = 41;
//	final int CRAFT_BUTTON_ICON_U = 176;   // texture position of the water bar
//	final int CRAFT_BUTTON_ICON_V = 17;
	final int CRAFT_BUTTON_WIDTH = 47;
	final int CRAFT_BUTTON_HEIGHT = 12;

	final int CRAFTING_TEXT_XPOS = 80;
	final int CRAFTING_TEXT_YPOS = 22;

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
	@SuppressWarnings("unchecked")
	@Override
    public void initGui(){
        super.initGui();
        
        int buttonId = 0;
        buttonList.clear();
        buttonPrevRecipe = new GuiButton(buttonId++, guiLeft + LEFT_BUTTON_XPOS, guiTop + LEFT_BUTTON_YPOS, 
        		LEFT_BUTTON_WIDTH, LEFT_BUTTON_HEIGHT, "<<");
        buttonList.add(buttonPrevRecipe);
        buttonNextRecipe = new GuiButton(buttonId++, guiLeft + RIGHT_BUTTON_XPOS, guiTop + RIGHT_BUTTON_YPOS, 
        		RIGHT_BUTTON_WIDTH, RIGHT_BUTTON_HEIGHT, ">>");
        buttonList.add(buttonNextRecipe);
        buttonCraftAll = new GuiButton(buttonId++, guiLeft + CRAFT_BUTTON_XPOS, guiTop + CRAFT_BUTTON_YPOS, 
        		CRAFT_BUTTON_WIDTH, CRAFT_BUTTON_HEIGHT, "Craft All");
        buttonList.add(buttonCraftAll);
        buttonCraftOne = new GuiButton(buttonId++, guiLeft + BLUEPRINT_XPOS, guiTop + BLUEPRINT_YPOS, 
        		BLUEPRINT_WIDTH, BLUEPRINT_HEIGHT, "");
        buttonList.add(buttonCraftOne);
        
		this.tileEntity.setGuiOpen(true, false);
    }

	/** Called when a button is pressed */
	@Override
    protected void actionPerformed(GuiButton button) {
		if (button == buttonPrevRecipe){
			tileEntity.selectPrevBlueprint();
		}
		else if (button == buttonNextRecipe){
			tileEntity.selectNextBlueprint();
		}
		else if (button == buttonCraftAll){
			tileEntity.setCraftAllPressed(true, true); // and update server
		}
		else if (button == buttonCraftOne){
			tileEntity.setCraftOnePressed(true, true); // and update server
		}
    }

	/**
     * Called from the main game loop to update the screen.
     * Can hide a button by setting the visible field
     */
	@Override
    public void updateScreen(){
        super.updateScreen();
        
        int currBlueprint = tileEntity.getBlueprintSelected();
        boolean crafting = tileEntity.isCrafting();
        buttonPrevRecipe.visible = (currBlueprint > 0);
        buttonNextRecipe.visible = (currBlueprint < tileEntity.getNumBlueprints() - 1);
        buttonPrevRecipe.enabled = !crafting;
        buttonNextRecipe.enabled = !crafting;
        buttonCraftAll.enabled = !crafting;
    }
    
    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     * Can use GL11 to set colors and such (progress of crafting item)
     */
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
    	super.drawScreen(mouseX, mouseY, partialTicks);
    }

	@Override
	public void onGuiClosed() { 
		super.onGuiClosed();
		// Only set closed if no players have Gui open
		if (((ContainerInventoryMP)this.inventorySlots).getNumCrafters() == 0)
			this.tileEntity.setGuiOpen(false, true); // and update server
	}
	
	// abstract in super
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		this.fontRendererObj.drawString(name, (int)(xSize / 2) - (name.length() * 5 / 2), 5, Color.darkGray.getRGB());
		// Number being crafted
		if (tileEntity.isCraftingOne())
			this.fontRendererObj.drawString("Crafting one item", 
					CRAFTING_TEXT_XPOS, CRAFTING_TEXT_YPOS, Color.darkGray.getRGB());
		else if (tileEntity.isCraftingAll())
			this.fontRendererObj.drawString("Crafting " + tileEntity.getNumToBeCrafted() + " item(s)", 
					CRAFTING_TEXT_XPOS, CRAFTING_TEXT_YPOS, Color.darkGray.getRGB());

		List<String> hoveringText = new ArrayList<String>();

		// Add hovering text if the mouse is over the Craft all button
		if (isInRect(guiLeft + CRAFT_BUTTON_XPOS, guiTop + CRAFT_BUTTON_YPOS, CRAFT_BUTTON_WIDTH, CRAFT_BUTTON_HEIGHT, mouseX, mouseY)){
			hoveringText.add("Can craft " + tileEntity.getNumToBeCrafted() + " item(s).");
		}
		
		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()){
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
		}

		if (tileEntity.isCrafting()){
			double fraction = tileEntity.fractionCraftingRemainingForItem();
			if (fraction <= 0.01D)
				return;
			int x = BLUEPRINT_XPOS;
			int y = BLUEPRINT_YPOS;
			int color = 0x60EAA800;
			drawRect(x, y + (int)(fraction * BLUEPRINT_HEIGHT), x + BLUEPRINT_WIDTH, y + BLUEPRINT_HEIGHT, color);
		}
	}
	
	// abstract in super
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	// Returns true if the given x,y coordinates are within the given rectangle
	public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY){
		return ((mouseX >= x && mouseX <= x+xSize) && (mouseY >= y && mouseY <= y+ySize));
	}
}
