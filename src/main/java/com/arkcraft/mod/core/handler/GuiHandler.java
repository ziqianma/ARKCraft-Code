package com.arkcraft.mod.core.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.machine.gui.ContainerInventoryDodo;
import com.arkcraft.mod.core.machine.gui.ContainerSmithy;
import com.arkcraft.mod.core.machine.gui.GuiInventoryDodo;
import com.arkcraft.mod.core.machine.gui.GuiSmithy;
import com.arkcraft.mod.core.machine.tileentity.TileEntityInventoryDodo;

/* GuiID == 0 ? GuiID == 1 for Smithy */
public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GlobalAdditions.guiIDSmithy) return ID == GlobalAdditions.guiIDSmithy ? new ContainerSmithy(player.inventory, world, new BlockPos(x, y, z)) : null; 
		if(ID == GlobalAdditions.guiIDPestleAndMortar) return ID == GlobalAdditions.guiIDPestleAndMortar && world.getBlockState(new BlockPos(x, y, z)).getBlock() == GlobalAdditions.pestle_and_mortar ? new ContainerSmithy(player.inventory, world, new BlockPos(x, y, z)) : null;
		
		TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
		if(te instanceof TileEntityInventoryDodo) {
			return new ContainerInventoryDodo(player.inventory, (TileEntityInventoryDodo)te);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GlobalAdditions.guiIDSmithy) return ID == GlobalAdditions.guiIDSmithy ? new GuiSmithy(player.inventory, world, new BlockPos(x, y, z)) : null; 
		if(ID == GlobalAdditions.guiIDPestleAndMortar) return ID == GlobalAdditions.guiIDPestleAndMortar && world.getBlockState(new BlockPos(x, y, z)).getBlock() == GlobalAdditions.pestle_and_mortar ? new GuiSmithy(player.inventory, world, new BlockPos(x, y, z)) : null;
		
		TileEntity te = world.getTileEntity(new BlockPos(x,y,z));
		if(te instanceof TileEntityInventoryDodo) {
			return new GuiInventoryDodo(player.inventory, (TileEntityInventoryDodo)te);
		}
		return null;
	}
	
	
	
}
