package com.arkcraft.mod.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.machine.gui.ContainerInventoryDodo;
import com.arkcraft.mod.core.machine.gui.ContainerSmithy;
import com.arkcraft.mod.core.machine.gui.GuiInventoryDodo;
import com.arkcraft.mod.core.machine.gui.GuiSmithy;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GlobalAdditions.guiIDSmithy) return new ContainerSmithy(player.inventory, world, new BlockPos(x, y, z)); 
		if(ID == GlobalAdditions.guiIDPestleAndMortar) return new ContainerSmithy(player.inventory, world, new BlockPos(x, y, z));
		if(ID == GlobalAdditions.guiIDInvDodo) return new ContainerInventoryDodo(player.inventory, world);
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GlobalAdditions.guiIDSmithy) return ID == GlobalAdditions.guiIDSmithy ? new GuiSmithy(player.inventory, world, new BlockPos(x, y, z)) : null; 
		if(ID == GlobalAdditions.guiIDPestleAndMortar) return ID == GlobalAdditions.guiIDPestleAndMortar && world.getBlockState(new BlockPos(x, y, z)).getBlock() == GlobalAdditions.pestle_and_mortar ? new GuiSmithy(player.inventory, world, new BlockPos(x, y, z)) : null;
		if(ID == GlobalAdditions.guiIDInvDodo) return new GuiInventoryDodo(player.inventory, world);
		return null;
	}
	
	
	
}
