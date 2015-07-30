package com.arkcraft.mod.core.handler;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.machine.gui.ContainerSmithy;
import com.arkcraft.mod.core.machine.gui.GuiSmithy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GlobalAdditions.guiIDSmithy) {
			return ID == GlobalAdditions.guiIDSmithy && world.getBlockState(new BlockPos(x, y, z)).getBlock() == GlobalAdditions.smithy ? new ContainerSmithy(player.inventory, world, new BlockPos(x, y, z)) : null; 
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		
		if(ID == GlobalAdditions.guiIDSmithy) {
			return ID == GlobalAdditions.guiIDSmithy && world.getBlockState(new BlockPos(x, y, z)).getBlock() == GlobalAdditions.smithy ? new GuiSmithy(player.inventory, world, new BlockPos(x, y, z)) : null; 
		}
		return null;
	}
	
	
	
}
