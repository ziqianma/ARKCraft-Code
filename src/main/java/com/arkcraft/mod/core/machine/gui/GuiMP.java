package com.arkcraft.mod.core.machine.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.blocks.TileInventoryMP;

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
	public static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/mortar_and_pestle.png");
	private TileInventoryMP tileEntity;
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
	final int LEFT_BUTTON_XPOS = 30;
	final int LEFT_BUTTON_YPOS = 22;
//	final int LEFT_BUTTON_ICON_U = 176;   // texture position of the water bar
//	final int LEFT_BUTTON_ICON_V = 17;
	final int LEFT_BUTTON_WIDTH = 10;
	final int LEFT_BUTTON_HEIGHT = 8;

	final int RIGHT_BUTTON_XPOS = 64;
	final int RIGHT_BUTTON_YPOS = 22;
//	final int RIGHT_BUTTON_ICON_U = 176;   // texture position of the water bar
//	final int RIGHT_BUTTON_ICON_V = 17;
	final int RIGHT_BUTTON_WIDTH = 10;
	final int RIGHT_BUTTON_HEIGHT = 8;

	final int CRAFT_BUTTON_XPOS = 29;
	final int CRAFT_BUTTON_YPOS = 41;
//	final int CRAFT_BUTTON_ICON_U = 176;   // texture position of the water bar
//	final int CRAFT_BUTTON_ICON_V = 17;
	final int CRAFT_BUTTON_WIDTH = 45;
	final int CRAFT_BUTTON_HEIGHT = 11;

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
        		LEFT_BUTTON_WIDTH, LEFT_BUTTON_HEIGHT, "Prev");
        buttonList.add(buttonPrevRecipe);
        buttonNextRecipe = new GuiButton(buttonId++, guiLeft + RIGHT_BUTTON_XPOS, guiTop + RIGHT_BUTTON_YPOS, 
        		RIGHT_BUTTON_WIDTH, RIGHT_BUTTON_HEIGHT, "Next");
        buttonList.add(buttonNextRecipe);
        buttonCraftAll = new GuiButton(buttonId++, guiLeft + CRAFT_BUTTON_XPOS, guiTop + CRAFT_BUTTON_YPOS, 
        		CRAFT_BUTTON_WIDTH, CRAFT_BUTTON_HEIGHT, "Craft All");
        buttonList.add(buttonCraftAll);
    }

	/** Called when a button is pressed */
	@Override
    protected void actionPerformed(GuiButton button) {
		if (button == buttonPrevRecipe){
			tileEntity.selectPreveBlueprint();
		}
		else if (button == buttonNextRecipe){
			tileEntity.selectNextBlueprint();
		}
		else if (button == buttonCraftAll){
			tileEntity.setCraftAllPressed(true);;
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
        buttonPrevRecipe.visible = (currBlueprint > 0);
        buttonNextRecipe.visible = (currBlueprint < tileEntity.getNumBlueprints() - 1);
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
	public void onGuiClosed() { super.onGuiClosed(); }
	
	// abstract in super
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		this.fontRendererObj.drawString(name, (int)(xSize / 2) - (name.length() * 5 / 2), 5, Color.darkGray.getRGB());
		// Number being crafted
		this.fontRendererObj.drawString("Crafting " + tileEntity.getNumToBeCrafted() + " item(s)", 
				CRAFTING_TEXT_XPOS, CRAFTING_TEXT_YPOS, Color.darkGray.getRGB());

		List<String> hoveringText = new ArrayList<String>();

		// Add hovering text if the mouse is over the Craft all button
		if (isInRect(guiLeft + CRAFT_BUTTON_XPOS, guiTop + CRAFT_BUTTON_YPOS, CRAFT_BUTTON_WIDTH, CRAFT_BUTTON_HEIGHT, mouseX, mouseY)){
			hoveringText.add("Can craft " + tileEntity.getNumToBeCrafted() + " item.");
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
