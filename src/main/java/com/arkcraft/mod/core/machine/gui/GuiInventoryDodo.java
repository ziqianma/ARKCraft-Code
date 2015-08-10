package com.arkcraft.mod.core.machine.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;

import com.arkcraft.mod.core.machine.tileentity.TileEntityInventoryDodo;

public class GuiInventoryDodo extends GuiBase {

	@SuppressWarnings("unused")
	private TileEntityInventoryDodo invDodo;
	
	public GuiInventoryDodo(InventoryPlayer invPlayer, TileEntityInventoryDodo invDodo, BlockPos pos) {
		super(new ContainerInventoryDodo(invPlayer, invDodo), "Dodo GUI", "textures/gui/dodo_gui.png", 176, 166);
		this.invDodo = invDodo;
	}
	
	public void onGuiClosed() { super.onGuiClosed(); }
	protected void drawGuiContainerForegroundLayer(int i, int j) { super.drawGuiContainerForegroundLayer(i, j); }
	protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY) { super.drawGuiContainerBackgroundLayer(partTick, mX, mY); }

}
