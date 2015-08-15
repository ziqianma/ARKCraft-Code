package com.arkcraft.mod.core.container.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/***
 * 
 * @author Vastatio
 *
 */
@SideOnly(Side.CLIENT)
public class GuiSmithy extends GuiBase {
	
	public GuiSmithy(InventoryPlayer invPlayer, World world, BlockPos pos) {
		super(new ContainerSmithy(invPlayer, world, pos), "Smithy", "textures/gui/smithy.png", 182, 226);
		
	}
	
	public void onGuiClosed() { super.onGuiClosed(); }
	protected void drawGuiContainerForegroundLayer(int i, int j) { super.drawGuiContainerForegroundLayer(i, j); }
	protected void drawGuiContainerBackgroundLayer(float partTick, int mX, int mY) { super.drawGuiContainerBackgroundLayer(partTick, mX, mY); }

}
