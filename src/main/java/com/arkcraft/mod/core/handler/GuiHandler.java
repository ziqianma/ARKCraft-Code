package com.arkcraft.mod.core.handler;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.machine.gui.ContainerSmithy;
import com.arkcraft.mod.core.machine.gui.GuiSmithy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/* GuiID == 0 ? GuiID == 1 for Smithy */
public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 1) return ID == 1 && world.getBlockState(new BlockPos(x, y, z)).getBlock() == GlobalAdditions.smithy ? new ContainerSmithy(player.inventory, world, new BlockPos(x, y, z)) : null; 
		if(ID == 2) return ID == 2 && world.getBlockState(new BlockPos(x, y, z)).getBlock() == GlobalAdditions.pestle_and_mortar ? new ContainerSmithy(player.inventory, world, new BlockPos(x, y, z)) : null;
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 1) return ID == 1 && world.getBlockState(new BlockPos(x, y, z)).getBlock() == GlobalAdditions.smithy ? new GuiSmithy(player.inventory, world, new BlockPos(x, y, z)) : null; 
		if(ID == 2) return ID == 2 && world.getBlockState(new BlockPos(x, y, z)).getBlock() == GlobalAdditions.pestle_and_mortar ? new GuiSmithy(player.inventory, world, new BlockPos(x, y, z)) : null;
		return null;
	}
	
	
	
}
