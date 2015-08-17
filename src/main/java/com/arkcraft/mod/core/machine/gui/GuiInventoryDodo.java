package com.arkcraft.mod.core.machine.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.IInventory;

import com.arkcraft.mod.core.entity.passive.EntityDodo;

public class GuiInventoryDodo extends GuiBase {

	private EntityDodo dodo;
	
	public GuiInventoryDodo(IInventory playerInv, IInventory invDodo, EntityDodo dodo) {
		super(new ContainerInventoryDodo(playerInv, invDodo, dodo, Minecraft.getMinecraft().thePlayer), "Dodo GUI", "textures/gui/dodo_gui.png", 176, 166);
		this.dodo = dodo;
	}

	public void onGuiClosed() { 
		super.onGuiClosed(); 
	}
	
	protected void drawGuiContainerForegroundLayer(int i, int j) { super.drawGuiContainerForegroundLayer(i, j); }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY) {
		super.drawGuiContainerBackgroundLayer(partTick, mX, mY); 
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        GuiInventory.drawEntityOnScreen(k + 51, l + 60, 17, (float)(k + 51) - mX, (float)(l + 75 - 50) - mY, this.dodo);
	}
	
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
