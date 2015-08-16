package com.arkcraft.mod.core.machine.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

/* Mortar and Pestle */
/***
 * 
 * @author Vastatio
 *
 */
public class GuiMP extends GuiBase {

	public GuiMP(InventoryPlayer invPlayer, World w, BlockPos pos) {
		super(new ContainerMP(invPlayer, w, pos), "Mortar and Pestle", "textures/gui/mortar_and_pestle.png", 177, 166);
	}
	
	public void onGuiClosed() { super.onGuiClosed(); }
	protected void drawGuiContainerForegroundLayer(int i, int j) { super.drawGuiContainerForegroundLayer(i, j); }
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) { super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY); }
}
