package com.arkcraft.module.creature.client.gui;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.common.container.ContainerInventoryDodo;
import com.arkcraft.module.core.common.container.inventory.InventoryDino;
import com.arkcraft.module.creature.common.entity.passive.EntityDodo;

public class GuiInventoryDodo extends GuiContainer
{

    private InventoryDino invDodo;
    private EntityDodo dodo;
    // This is the resource location for the background image for the GUI
    private static final ResourceLocation texture = new ResourceLocation(ARKCraft.MODID, "textures/gui/dodo_gui.png");

    public GuiInventoryDodo(IInventory playerInv, IInventory invDodo, EntityDodo dodo)
    {
        super(new ContainerInventoryDodo(playerInv, invDodo, dodo));
        this.invDodo = (InventoryDino) invDodo;
        this.invDodo.setCustomName("Dodo Inventory");
        this.dodo = dodo;
        // Set the width and height of the gui.  Should match the size of the texture!
        xSize = 176;
        ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY)
    {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        GuiInventory.drawEntityOnScreen(k + 51, l + 60, 17, (float) (k + 51) - mX, (float) (l + 75 - 50) - mY, this.dodo);
    }

    // draw the foreground for the GUI - rendered after the slots, but before the dragged items and tooltips
    // renders relative to the top left corner of the background
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        final int LABEL_XPOS = 5;
        final int LABEL_YPOS = 5;
        fontRendererObj.drawString(invDodo.getDisplayName().getUnformattedText(), LABEL_XPOS, LABEL_YPOS, Color.darkGray.getRGB());
    }
}
