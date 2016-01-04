package com.arkcraft.module.item.client.gui;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.item.common.container.ContainerInventoryCropPlot;
import com.arkcraft.module.item.common.tile.TileInventoryCropPlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wildbill22
 */
@SideOnly(Side.CLIENT)
public class GUICropPlot extends GuiContainer
{

    public static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/crop_plot_gui.png");
    private TileInventoryCropPlot tileEntity;

    public GUICropPlot(InventoryPlayer invPlayer, TileInventoryCropPlot tileInventoryCropPlot)
    {
        super(new ContainerInventoryCropPlot(invPlayer, tileInventoryCropPlot));
        this.tileEntity = tileInventoryCropPlot;

        // Width and height of the gui:
        this.xSize = 176;
        this.ySize = 166;
    }

    // some [x,y] coordinates of graphical elements
    final int WATER_BAR_XPOS = 10;
    final int WATER_BAR_YPOS = 7;
    final int WATER_BAR_ICON_U = 176;   // texture position of the water bar
    final int WATER_BAR_ICON_V = 17;
    final int WATER_BAR_WIDTH = 12;
    final int WATER_BAR_HEIGHT = 41;

    final int ARROW_XPOS = 68;
    final int ARROW_YPOS = 17;
    final int ARROW_ICON_U = 177;   // texture position of arrow icon
    final int ARROW_ICON_V = 0;
    final int ARROW_WIDTH = 22;
    final int ARROW_HEIGHT = 16;

    // Arrow that shows grow time complete
    final int FLAME_XPOS = 45;
    final int FLAME_YPOS = 36;
    final int FLAME_ICON_U = 178;   // texture position of flame icon
    final int FLAME_ICON_V = 19;
    final int FLAME_WIDTH = 13;
    final int FLAME_HEIGHT = 14;

    public void onGuiClosed() { super.onGuiClosed(); }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        // Display GUI name:
        final int LABEL_XPOS = 44;
        final int LABEL_YPOS = 5;
        fontRendererObj.drawString(tileEntity.getDisplayName().getUnformattedText(), LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());

        List<String> hoveringText = new ArrayList<String>();

        // If the mouse is over the display text add the growth stage bar hovering text
        if (isInRect(guiLeft + LABEL_XPOS, guiTop + LABEL_YPOS, 50, 8, mouseX, mouseY))
        {
            hoveringText.add("Growth Stage is: ");
            int growPercentage = (int) (tileEntity.getGrowthStage());
            hoveringText.add(growPercentage + ".");
        }

        // If the mouse is over the water progress bar add the progress bar hovering text
        if (isInRect(guiLeft + WATER_BAR_XPOS, guiTop + WATER_BAR_YPOS, WATER_BAR_WIDTH, WATER_BAR_HEIGHT, mouseX, mouseY))
        {
            hoveringText.add("Water Time:");
            int growPercentage = (int) (tileEntity.fractionWaterLevelRemaining() * 100);
            hoveringText.add(growPercentage + "%");
        }

        // If the mouse is over the growth progress bar add the progress bar hovering text
        if (isInRect(guiLeft + ARROW_XPOS, guiTop + ARROW_YPOS, ARROW_WIDTH, ARROW_HEIGHT, mouseX, mouseY))
        {
            hoveringText.add("Growing a berry:");
            int growPercentage = (int) (tileEntity.fractionOfGrowTimeComplete() * 100);
            hoveringText.add(growPercentage + "%");
        }

        // If the mouse is over the ??? progress bar add the progress bar hovering text
        // TODO: So what is the flame supposed to show?
//		if (isInRect(guiLeft + FLAME_XPOS, guiTop + FLAME_YPOS, FLAME_WIDTH, FLAME_HEIGHT, mouseX, mouseY)){
//			hoveringText.add("Progress:");
//			int growPercentage =(int)(tileEntity.fractionOfGrowTimeComplete() * 100);
//			hoveringText.add(growPercentage + "%");
//		}

        // If the mouse is over one of the fertilizer slots add the burn time indicator hovering text
//		for (int i = 0; i < TileInventoryCropPlot.FERTILIZER_SLOTS_COUNT; ++i) {
//			if (isInRect(guiLeft + 44 + 18 * i, guiTop + ContainerInventoryCropPlot.FERTILIZER_SLOT_YPOS, 16, 16, mouseX, mouseY)) {
//				hoveringText.add("Fertilizer Time:");
//				hoveringText.add(tileEntity.secondsOfFertilizerRemaining(i) + "s");
//			}
//		}

        // If hoveringText is not empty draw the hovering text
        if (!hoveringText.isEmpty())
        {
            drawHoveringText(hoveringText, mouseX - guiLeft, mouseY - guiTop, fontRendererObj);
        }
    }

    protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY)
    {
        // Draw the GUI
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        // get water progress as a double between 0 and 1, and draw it
        double waterProgress = tileEntity.fractionWaterLevelRemaining();
        int iconHeight = (int) (waterProgress * WATER_BAR_HEIGHT);
        int iconYOffset = WATER_BAR_HEIGHT - iconHeight;
        drawTexturedModalRect(guiLeft + WATER_BAR_XPOS, guiTop + WATER_BAR_YPOS + iconYOffset,
                WATER_BAR_ICON_U, WATER_BAR_ICON_V + iconYOffset, WATER_BAR_WIDTH, iconHeight);

        // get grow arrow as a double between 0 and 1, and draw it
        double growProgress = tileEntity.fractionOfGrowTimeComplete();
        drawTexturedModalRect(guiLeft + ARROW_XPOS, guiTop + ARROW_YPOS, ARROW_ICON_U, ARROW_ICON_V,
                (int) (growProgress * ARROW_WIDTH), ARROW_HEIGHT);
    }

    // Returns true if the given x,y coordinates are within the given rectangle
    public static boolean isInRect(int x, int y, int xSize, int ySize, int mouseX, int mouseY)
    {
        return ((mouseX >= x && mouseX <= x + xSize) && (mouseY >= y && mouseY <= y + ySize));
    }
}
