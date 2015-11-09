package com.arkcraft.mod.common.handlers;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.container.ContainerInventoryCompostBin;
import com.arkcraft.mod.common.container.ContainerInventoryCropPlot;
import com.arkcraft.mod.common.container.ContainerInventoryDino;
import com.arkcraft.mod.common.container.ContainerInventoryDodo;
import com.arkcraft.mod.common.container.ContainerInventoryMP;
import com.arkcraft.mod.common.container.ContainerInventoryPlayerCrafting;
import com.arkcraft.mod.common.container.ContainerInventorySmithy;
import com.arkcraft.mod.common.container.ContainerInventoryTaming;
import com.arkcraft.mod.client.gui.GUICompostBin;
import com.arkcraft.mod.client.gui.GUICropPlot;
import com.arkcraft.mod.client.gui.GUITaming;
import com.arkcraft.mod.client.gui.GuiInventoryDino;
import com.arkcraft.mod.client.gui.GuiInventoryDodo;
import com.arkcraft.mod.client.gui.GuiMP;
import com.arkcraft.mod.client.gui.GuiPlayerCrafting;
import com.arkcraft.mod.client.gui.GuiSmithy;
import com.arkcraft.mod.client.gui.book.BookData;
import com.arkcraft.mod.client.gui.book.BookDataStore;
import com.arkcraft.mod.client.gui.book.GuiDossier;
import com.arkcraft.mod.common.entity.EntityTameableDinosaur;
import com.arkcraft.mod.common.entity.passive.EntityDodo;
import com.arkcraft.mod.common.lib.LogHelper;
import com.arkcraft.mod.common.tile.TileInventoryCompostBin;
import com.arkcraft.mod.common.tile.TileInventoryCropPlot;
import com.arkcraft.mod.common.tile.TileInventoryMP;
import com.arkcraft.mod.common.tile.TileInventorySmithy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.util.Iterator;
import java.util.List;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (world.isRemote)
			LogHelper.info("GuiHandler: getServerGuiElement called from client");
		else
			LogHelper.info("GuiHandler: getServerGuiElement called from server");
		if (ID == GlobalAdditions.GUI.SMITHY.getID()) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventorySmithy)
				return new ContainerInventorySmithy(player.inventory, (TileInventorySmithy) tileEntity);
			else {
				LogHelper.info("GuiHandler - getServerGuiElement: TileEntitySmithy not found!");
			}
		}
		else if (ID == GlobalAdditions.GUI.PESTLE_AND_MORTAR.getID()) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryMP)
				return new ContainerInventoryMP(player.inventory, (TileInventoryMP) tileEntity);
			else {
				LogHelper.info("GuiHandler - getServerGuiElement: TileEntityMP not found!");
			}
		}
		else if (ID == GlobalAdditions.GUI.COMPOST_BIN.getID()) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryCompostBin)			
				return new ContainerInventoryCompostBin(player.inventory, (TileInventoryCompostBin) tileEntity);
			else {
				LogHelper.info("GuiHandler - getClientGuiElement: TileEntityCompostBin not found!");				
			}
		}
		else if (ID == GlobalAdditions.GUI.CROP_PLOT.getID()) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryCropPlot)
				return new ContainerInventoryCropPlot(player.inventory, (TileInventoryCropPlot) tileEntity);
			else {
				LogHelper.info("GuiHandler - getServerGuiElement: TileEntityCropPlot not found!");
			}
		}
		else if (ID == GlobalAdditions.GUI.INV_DODO.getID()) {
			Entity entity = getEntityAt(player, x, y, z);
			if (entity != null && entity instanceof EntityDodo)
				return new ContainerInventoryDodo(player.inventory, ((EntityDodo)entity).invDodo, (EntityDodo)entity);
			else
				LogHelper.error("GuiHandler - getServerGuiElement: Did not find entity with inventory!");
		}
//		else if (ID == GlobalAdditions.GUI.SCOPE_GUI.getID()) {
//			Entity entity = getEntityAt(player, x, y, z);
//			if (entity != null && entity instanceof EntityDodo)
//				return null;
//			else
//				LogHelper.error("GuiHandler - getServerGuiElement: Did not find entity with inventory!");
//		}
		else if (ID == GlobalAdditions.GUI.TAMING_GUI.getID()) {
			Entity entity = getEntityAt(player, x, y, z);
			if (entity != null && entity instanceof EntityTameableDinosaur) {
				EntityTameableDinosaur dino = (EntityTameableDinosaur)entity;
				dino.setSitting(true);
				if (dino.invTaming.getTorporTime() == 0)
					dino.invTaming.setTorporTime((short) 60);
				return new ContainerInventoryTaming(player.inventory, dino.invTaming, player);
			}
			else
				LogHelper.error("GuiHandler - getServerGuiElement: Did not find entity to tame!");			
		}
		else if (ID == GlobalAdditions.GUI.PLAYER.getID()) {
			return new ContainerInventoryPlayerCrafting(player.inventory, player);
		}
		else if (ID == GlobalAdditions.GUI.TAMED_DINO.getID()) {
			Entity entity = getEntityAt(player, x, y, z);
			if (entity != null && entity instanceof EntityTameableDinosaur) {
				EntityTameableDinosaur dino = (EntityTameableDinosaur)entity;
				return new ContainerInventoryDino(player.inventory, dino.invTamedDino, dino);
			}
			else
				LogHelper.error("GuiHandler - getServerGuiElement: Did not find tamed dino!");			
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (world.isRemote)
			LogHelper.info("GuiHandler: getClientGuiElement called from client");
		else
			LogHelper.info("GuiHandler: getClientGuiElement called from server");
		if (ID == GlobalAdditions.GUI.SMITHY.getID()) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventorySmithy)			
				return new GuiSmithy(player.inventory, (TileInventorySmithy) tileEntity);
			else {
				LogHelper.info("GuiHandler - getClientGuiElement: TileEntitySmithy not found!");				
			}			
		}
		else if (ID == GlobalAdditions.GUI.PESTLE_AND_MORTAR.getID()) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryMP)			
				return new GuiMP(player.inventory, (TileInventoryMP) tileEntity);
			else {
				LogHelper.info("GuiHandler - getClientGuiElement: TileEntityMP not found!");				
			}			
		}
		else if (ID == GlobalAdditions.GUI.CROP_PLOT.getID()) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryCropPlot)			
				return new GUICropPlot(player.inventory, (TileInventoryCropPlot) tileEntity);
			else {
				LogHelper.info("GuiHandler - getClientGuiElement: TileEntityCropPlot not found!");				
			}
		}
		else if (ID == GlobalAdditions.GUI.COMPOST_BIN.getID()) {
			BlockPos xyz = new BlockPos(x, y, z);
			TileEntity tileEntity = world.getTileEntity(xyz);
			if (tileEntity instanceof TileInventoryCompostBin)			
				return new GUICompostBin(player.inventory, (TileInventoryCompostBin) tileEntity);
			else {
				LogHelper.info("GuiHandler - getClientGuiElement: TileEntityCompostBin not found!");				
			}
		}
		else if(ID == GlobalAdditions.GUI.BOOK_GUI.getID()) {
			LogHelper.info("GuiHandler - getClientGuiElement(): GuiDossier book trying to open.");
			ItemStack stack = player.getCurrentEquippedItem();
			if(stack == null) LogHelper.info("Stack in GuiHandler is null!");
			if(stack != null && stack.getUnlocalizedName() == null) LogHelper.info("Stack in GuiHandler is null!");
			return new GuiDossier(stack, GuiHandler.getBookDataFromStack(stack));
		}
		else if (ID == GlobalAdditions.GUI.INV_DODO.getID()) {
			Entity entity = getEntityAt(player, x, y, z);
			if (entity != null && entity instanceof EntityDodo) 
				return new GuiInventoryDodo(player.inventory, ((EntityDodo)entity).invDodo, (EntityDodo)entity);
			else
				LogHelper.error("GuiHandler - getClientGuiElement: Did not find entity with inventory!");
		}
//		else if (ID == GlobalAdditions.GUI.SCOPE_GUI.getID()) {
//			Entity entity = getEntityAt(player, x, y, z);
//			if (entity != null && entity instanceof EntityDodo)
//				return null;
//			else
//				LogHelper.error("GuiHandler - getServerGuiElement: Did not find entity with inventory!");
//		}
		else if (ID == GlobalAdditions.GUI.TAMING_GUI.getID()) {
			Entity entity = getEntityAt(player, x, y, z);
			if (entity != null && entity instanceof EntityTameableDinosaur) {
				EntityTameableDinosaur dino = (EntityTameableDinosaur)entity;
				dino.setSitting(true);
				if (dino.invTaming.getTorporTime() == 0)
					dino.invTaming.setTorporTime((short) 60);
				return new GUITaming(player.inventory, ((EntityTameableDinosaur)entity).invTaming, player);
			}
			else
				LogHelper.error("GuiHandler - getClientGuiElement: Did not find entity to tame!");			
		}
		else if (ID == GlobalAdditions.GUI.PLAYER.getID()) {
			return new GuiPlayerCrafting(player.inventory, player);			
		}
		else if (ID == GlobalAdditions.GUI.TAMED_DINO.getID()) {
			Entity entity = getEntityAt(player, x, y, z);
			if (entity != null && entity instanceof EntityTameableDinosaur) {
				EntityTameableDinosaur dino = (EntityTameableDinosaur)entity;
				return new GuiInventoryDino(player.inventory, ((EntityTameableDinosaur)entity).invTamedDino, dino);
			}
			else
				LogHelper.error("GuiHandler - getClientGuiElement: Did not find tamed dino!");			
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
            if (entity instanceof EntityDodo || entity instanceof EntityTameableDinosaur) {
            	LogHelper.info("GuiHandler: Found entity!");
            	return entity;
            }
        }
        return null;
	}
	
	private static BookData getBookDataFromStack(ItemStack stack) {
		return BookDataStore.getBookFromName(stack.getUnlocalizedName());
	}
	
}
