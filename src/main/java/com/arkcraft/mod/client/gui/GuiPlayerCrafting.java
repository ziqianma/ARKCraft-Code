package com.arkcraft.mod.client.gui;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.container.ContainerInventoryPlayerCrafting;
import com.arkcraft.mod.common.container.inventory.InventoryBlueprints;
import com.arkcraft.mod.common.entity.player.ARKPlayer;
import com.arkcraft.mod.common.lib.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/* Player Crafting */
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

	public GuiPlayerCrafting(InventoryPlayer invPlayer, EntityPlayer player) {
		super(new ContainerInventoryPlayerCrafting(invPlayer, player));
		inventoryBlueprints = ARKPlayer.get(player).getInventoryBlueprints();
		LogHelper.info("GuiPlayerCrafting: Constructor called on " + FMLCommonHandler.instance().getEffectiveSide());

		this.xSize = 175;
		this.ySize = 242;
	}
	
	// some [x,y] coordinates of graphical elements
	final int BLUEPRINT_WIDTH = 16;
	final int BLUEPRINT_HEIGHT = 16;

//	final int CRAFT_BUTTON_XPOS = 19;
//	final int CRAFT_BUTTON_YPOS = 41;
//	final int CRAFT_BUTTON_WIDTH = 47;
//	final int CRAFT_BUTTON_HEIGHT = 12;

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
	@SuppressWarnings("unchecked")
	@Override
    public void initGui(){
        super.initGui();
        
        // Add all the buttons that allow you to craft from blueprints
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
    }

	/** Called when a button is pressed */
	@Override
    protected void actionPerformed(GuiButton button) {
		for(int row = 0; row < ContainerInventoryPlayerCrafting.NUM_ROWS_BP; row++) {
			for(int col = 0; col < ContainerInventoryPlayerCrafting.NUM_COLUMNS_BP; col++) {
				int i =  col + row * ContainerInventoryPlayerCrafting.NUM_COLUMNS_BP;
				if (button == buttonCraftOne[i]){
					inventoryBlueprints.setCraftOnePressed(true, i, true); // and update server
					inventoryBlueprints.setxButtonPressed(ContainerInventoryPlayerCrafting.BLUEPRINT_XPOS + col * 18);
					inventoryBlueprints.setyButtonPressed(ContainerInventoryPlayerCrafting.BLUEPRINT_YPOS + row * 18);
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

		List<String> hoveringText = new ArrayList<String>();

		// Add hovering text if the mouse is over a button
		for(int i = 0, row = 0; row < ContainerInventoryPlayerCrafting.NUM_ROWS_BP; row++) {
			for(int col = 0; col < ContainerInventoryPlayerCrafting.NUM_COLUMNS_BP; col++, i++) {
				if (i >= inventoryBlueprints.getNumBlueprints()){
					// Exit loops
					row = ContainerInventoryPlayerCrafting.NUM_ROWS_BP;
					break;					
				}
				int x = guiLeft + ContainerInventoryPlayerCrafting.BLUEPRINT_XPOS + col * 18;
				int y = guiTop + ContainerInventoryPlayerCrafting.BLUEPRINT_YPOS + row * 18;
				if (isInRect(x, y, BLUEPRINT_WIDTH, BLUEPRINT_HEIGHT, mouseX, mouseY)){
					ItemStack stack = inventoryBlueprints.getStackInSlot(i);
					String itemName = stack.getItem().getItemStackDisplayName(stack);
					// TODO: Disable buttons if the item can't be crafted!
					hoveringText.add(itemName + " - Can craft " + inventoryBlueprints.getNumToBeCrafted(i));
					// Exit loops
					row = ContainerInventoryPlayerCrafting.NUM_ROWS_BP;
					break;
				}
			}
		}
		
		// If hoveringText is not empty draw the hovering text
		if (!hoveringText.isEmpty()){
			drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
		}

		// Draw the animation to show that the item is being crafted
		if (inventoryBlueprints.isCrafting()){
			double fraction = inventoryBlueprints.fractionCraftingRemainingForItem();
			if (fraction <= 0.01D)
				return;
			int color = 0x60EAA800;
			int x = inventoryBlueprints.getxButtonPressed();
			int y = inventoryBlueprints.getyButtonPressed();
			drawRect(x, y + (int)(fraction * BLUEPRINT_HEIGHT),	x + BLUEPRINT_WIDTH, y + BLUEPRINT_HEIGHT, color);
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
