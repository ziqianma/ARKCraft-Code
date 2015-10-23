package com.arkcraft.mod.client.gui;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.player.ARKPlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/* Smithy */
/***
 * 
 * @author wildbill22
 *
 */
public class GuiPlayerCrafting extends GuiContainer {

	public String name = "Player Built-in Crafting";
	public static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/player_inventory_gui.png");
	private InventoryBlueprints inventoryBlueprints;
	private GuiButton [] buttonCraftOne;
//    private GuiButton buttonCraftAll;
	private int xButtonPressed;
	private int yButtonPressed;

	public GuiPlayerCrafting(InventoryPlayer invPlayer, EntityPlayer player) {
		super(new ContainerInventoryPlayerCrafting(invPlayer, player));
		inventoryBlueprints = ARKPlayer.get(player).getInventoryBlueprints();

		this.xSize = 175;
		this.ySize = 242;
	}
	
	// some [x,y] coordinates of graphical elements
	final int BLUEPRINT_WIDTH = 16;
	final int BLUEPRINT_HEIGHT = 16;

	final int CRAFT_BUTTON_XPOS = 19;
	final int CRAFT_BUTTON_YPOS = 41;
	final int CRAFT_BUTTON_WIDTH = 47;
	final int CRAFT_BUTTON_HEIGHT = 12;

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
	@SuppressWarnings("unchecked")
	@Override
    public void initGui(){
        super.initGui();
        
        int buttonId = 0;
        buttonList.clear();
        buttonCraftOne = new GuiButton[ContainerInventoryPlayerCrafting.BP_SLOT_COUNT];
		for(int row = 0; row < ContainerInventoryPlayerCrafting.NUM_ROWS_BP; row++) {
			for(int col = 0; col < ContainerInventoryPlayerCrafting.NUM_COLUMNS_BP; col++) {
				int i =  col + row * ContainerInventoryPlayerCrafting.NUM_COLUMNS_BP;
				int x = ContainerInventoryPlayerCrafting.BLUEPRINT_XPOS + col * 18;
				int y = ContainerInventoryPlayerCrafting.BLUEPRINT_YPOS + row * 18;
	            buttonCraftOne[i] = new GuiButton(buttonId++, guiLeft + x, guiTop + y, BLUEPRINT_WIDTH, BLUEPRINT_HEIGHT, "");
	            buttonList.add(buttonCraftOne[i]);
			}
        }
        
//      buttonCraftAll = new GuiButton(buttonId++, guiLeft + CRAFT_BUTTON_XPOS, guiTop + CRAFT_BUTTON_YPOS, 
//		CRAFT_BUTTON_WIDTH, CRAFT_BUTTON_HEIGHT, "Craft All");
//		buttonList.add(buttonCraftAll);
    }

	/** Called when a button is pressed */
	@Override
    protected void actionPerformed(GuiButton button) {
		for(int row = 0; row < ContainerInventoryPlayerCrafting.NUM_ROWS_BP; row++) {
			for(int col = 0; col < ContainerInventoryPlayerCrafting.NUM_COLUMNS_BP; col++) {
				int i =  col + row * ContainerInventoryPlayerCrafting.NUM_COLUMNS_BP;
				if (button == buttonCraftOne[i]){
					inventoryBlueprints.setCraftOnePressed(i, true, true); // and update server
					xButtonPressed = ContainerInventoryPlayerCrafting.BLUEPRINT_XPOS + col * 18;
					yButtonPressed = ContainerInventoryPlayerCrafting.BLUEPRINT_YPOS + row * 18;
				}
			}
		}
    }

	/**
     * Called from the main game loop to update the screen.
     * Can hide a button by setting the visible field
     */
	@Override
    public void updateScreen(){
        super.updateScreen();
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
	}
	
	// abstract in super
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);

		// Name of GUI at top
		this.fontRendererObj.drawString(name, (int)(xSize / 2) - (name.length() * 5 / 2), 5, Color.darkGray.getRGB());

		// Number being crafted
//		if (inventoryBlueprints.isCraftingOne())
//			this.fontRendererObj.drawString("Crafting one item", 
//					CRAFTING_TEXT_XPOS, CRAFTING_TEXT_YPOS, Color.darkGray.getRGB());
//		else if (inventoryBlueprints.isCraftingAll())
//			this.fontRendererObj.drawString("Crafting " + inventoryBlueprints.getNumToBeCrafted() + " item(s)", 
//					CRAFTING_TEXT_XPOS, CRAFTING_TEXT_YPOS, Color.darkGray.getRGB());

//		List<String> hoveringText = new ArrayList<String>();

		// Add hovering text if the mouse is over the Craft all button
//		if (isInRect(guiLeft + CRAFT_BUTTON_XPOS, guiTop + CRAFT_BUTTON_YPOS, CRAFT_BUTTON_WIDTH, CRAFT_BUTTON_HEIGHT, mouseX, mouseY)){
//			hoveringText.add("Can craft " + inventoryBlueprints.getNumToBeCrafted() + " item(s).");
//		}
		
		// If hoveringText is not empty draw the hovering text
//		if (!hoveringText.isEmpty()){
//			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
//		}

		// Draw the animation to show that the item is being crafted
		if (inventoryBlueprints.isCrafting()){
			double fraction = inventoryBlueprints.fractionCraftingRemainingForItem();
			if (fraction <= 0.01D)
				return;
			int color = 0x60EAA800;
			drawRect(xButtonPressed, yButtonPressed + (int)(fraction * BLUEPRINT_HEIGHT), 
					xButtonPressed + BLUEPRINT_WIDTH, yButtonPressed + BLUEPRINT_HEIGHT, color);
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
