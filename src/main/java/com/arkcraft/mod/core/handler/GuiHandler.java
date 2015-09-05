package com.arkcraft.mod.core.handler;

import java.util.Iterator;
import java.util.List;

import com.arkcraft.mod.core.GlobalAdditions.GUI;
import com.arkcraft.mod.core.blocks.crop_test.ContainerInventoryCropPlot;
import com.arkcraft.mod.core.blocks.crop_test.TileInventoryCropPlot;
import com.arkcraft.mod.core.entity.DinoTameable;
import com.arkcraft.mod.core.entity.passive.EntityDodo;
import com.arkcraft.mod.core.lib.LogHelper;
import com.arkcraft.mod.core.machine.gui.ContainerInventoryDodo;
import com.arkcraft.mod.core.machine.gui.ContainerInventoryTaming;
import com.arkcraft.mod.core.machine.gui.ContainerMP;
import com.arkcraft.mod.core.machine.gui.ContainerSmithy;
import com.arkcraft.mod.core.machine.gui.GUICropPlot;
import com.arkcraft.mod.core.machine.gui.GUITaming;
import com.arkcraft.mod.core.machine.gui.GuiDosierScreen;
import com.arkcraft.mod.core.machine.gui.GuiInventoryDodo;
import com.arkcraft.mod.core.machine.gui.GuiMP;
import com.arkcraft.mod.core.machine.gui.GuiSmithy;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (world.isRemote)
			LogHelper.info("GuiHandler: getServerGuiElement called from client");
		else
			LogHelper.info("GuiHandler: getServerGuiElement called from server");
		if (ID == GUI.SMITHY.getID()) 
			return new ContainerSmithy(player.inventory, world, new BlockPos(x, y, z)); 
		if (ID == GUI.PESTLE_AND_MORTAR.getID()) 
			return new ContainerMP(player.inventory, world, new BlockPos(x, y, z));
		if (ID == GUI.CROP_PLOT.getID()) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryCropPlot)
				return new ContainerInventoryCropPlot(player.inventory, (TileInventoryCropPlot) tileEntity);
			else {
				LogHelper.info("GuiHandler - getServerGuiElement: TileEntityCropPlot not found!");
			}
		}
		if (ID == GUI.INV_DODO.getID()) {
			Entity entity = getEntityAt(player, x, y, z);
			if (entity != null && entity instanceof EntityDodo) 
				return new ContainerInventoryDodo(player.inventory, ((EntityDodo)entity).invDodo, (EntityDodo)entity);
			else
				LogHelper.error("GuiHandler - getServerGuiElement: Did not find entity with inventory!");
		}
		if (ID == GUI.TAMING_GUI.getID()) {
			Entity entity = getEntityAt(player, x, y, z);
			if (entity != null && entity instanceof DinoTameable) 
				return new ContainerInventoryTaming(player.inventory, ((DinoTameable)entity).invTaming);
			else
				LogHelper.error("GuiHandler - getServerGuiElement: Did not find entity to tame!");			
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (world.isRemote)
			LogHelper.info("GuiHandler: getClientGuiElement called from client");
		else
			LogHelper.info("GuiHandler: getClientGuiElement called from server");
		if (ID == GUI.SMITHY.getID()) 
			return new GuiSmithy(player.inventory, world, new BlockPos(x, y, z));
		if (ID == GUI.PESTLE_AND_MORTAR.getID()) 
			return new GuiMP(player.inventory, world, new BlockPos(x, y, z));
		if (ID == GUI.CROP_PLOT.getID()) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryCropPlot)			
				return new GUICropPlot(player.inventory, (TileInventoryCropPlot) tileEntity);
			else {
				LogHelper.info("GuiHandler - getClientGuiElement: TileEntityCropPlot not found!");				
			}
		}
		if (ID == GUI.BOOK_GUI.ordinal()) 
			return new GuiDosierScreen();
		if (ID == GUI.INV_DODO.getID()) {
			Entity entity = getEntityAt(player, x, y, z);
			if (entity != null && entity instanceof EntityDodo) 
				return new GuiInventoryDodo(player.inventory, ((EntityDodo)entity).invDodo, (EntityDodo)entity);
			else
				LogHelper.error("GuiHandler - getClientGuiElement: Did not find entity with inventory!");
		}
		if (ID == GUI.TAMING_GUI.getID()) {
			Entity entity = getEntityAt(player, x, y, z);
			if (entity != null && entity instanceof DinoTameable) 
				return new GUITaming(player.inventory, ((DinoTameable)entity).invTaming, player);
			else
				LogHelper.error("GuiHandler - getClientGuiElement: Did not find entity to tame!");			
		}
		return null;
	}
	
	private Entity getEntityAt(EntityPlayer player, int x, int y, int z) {
	    AxisAlignedBB targetBox = new AxisAlignedBB(x, y, z, x+1, y+1, z+1);
    	@SuppressWarnings("rawtypes")
		List entities = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, targetBox);
        @SuppressWarnings("rawtypes")
		Iterator iterator = entities.iterator();
        while (iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            if (entity instanceof EntityDodo || entity instanceof DinoTameable) {
            	LogHelper.info("GuiHandler: Found entity!");
            	return entity;
            }
        }
        return null;
	}
	
}
