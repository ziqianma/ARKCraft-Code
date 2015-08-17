package com.arkcraft.mod.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import com.arkcraft.mod.core.GlobalAdditions.GUI;
import com.arkcraft.mod.core.entity.passive.EntityDodo;
import com.arkcraft.mod.core.lib.LogHelper;
import com.arkcraft.mod.core.machine.gui.ContainerInventoryDodo;
import com.arkcraft.mod.core.machine.gui.ContainerMP;
import com.arkcraft.mod.core.machine.gui.ContainerSmithy;
import com.arkcraft.mod.core.machine.gui.GuiInventoryDodo2;
import com.arkcraft.mod.core.machine.gui.GuiSmithy;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GUI.SMITHY.getID()) return new ContainerSmithy(player.inventory, world, new BlockPos(x, y, z)); 
		if(ID == GUI.PESTLE_AND_MORTAR.getID()) return new ContainerMP(player.inventory, world, new BlockPos(x, y, z));
		if(ID == GUI.INV_DODO.getID()) {
			Entity entity = getEntityLookingAt(player);
			if(entity != null && entity instanceof EntityDodo) 
				return new ContainerInventoryDodo(player.inventory, ((EntityDodo)entity).invDodo, (EntityDodo)entity, player);
			else
				LogHelper.error("GuiHandler - getServerGuiElement: Did not find entity with inventory!");
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == GUI.SMITHY.getID()) return new GuiSmithy(player.inventory, world, new BlockPos(x, y, z));
		if(ID == GUI.PESTLE_AND_MORTAR.getID()) return new GuiSmithy(player.inventory, world, new BlockPos(x, y, z));
		if(ID == GUI.INV_DODO.getID()) {
			Entity entity = getEntityLookingAt(player);
			if(entity != null && entity instanceof EntityDodo) 
				return new GuiInventoryDodo2(player.inventory, ((EntityDodo)entity).invDodo, (EntityDodo)entity);
			else
				LogHelper.error("GuiHandler - getClientGuiElement: Did not find entity with inventory!");
		}
		return null;
	}
	
	private Entity getEntityLookingAt(EntityPlayer player) {
		MovingObjectPosition mop = Minecraft.getMinecraft().objectMouseOver;
		if (mop != null)
			return mop.entityHit;
		else
			return null;
	}

//	private Entity getEntityAt(EntityPlayer player, int x, int y, int z) {
//	    AxisAlignedBB targetBox = new AxisAlignedBB(x-0.5D, y-0.0D, z-0.5D, x+0.5D, y+1.5D, z+0.5D);
//    	@SuppressWarnings("rawtypes")
//		List entities = player.worldObj.getEntitiesWithinAABBExcludingEntity(player, targetBox);
//        @SuppressWarnings("rawtypes")
//		Iterator iterator = entities.iterator();
//        while (iterator.hasNext()) {
//            Entity entity = (Entity)iterator.next();
//            if (entity instanceof EntityDodo) {
//            	LogHelper.info("GuiHandler: Found Dodo with chest!");
//            	return entity;
//            }
//        }
//        return null;
//	}
}
