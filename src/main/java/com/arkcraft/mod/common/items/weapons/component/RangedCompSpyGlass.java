package com.arkcraft.mod.common.items.weapons.component;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RangedCompSpyGlass extends RangedComponent
{
	public RangedCompSpyGlass()
	{
		super(RangedSpecs.SHOTGUN);

	}
	
	@Override
	public float getMaxZoom()
	{
		return 0.50f;
	}



	@Override
	public void effectReloadDone(ItemStack itemstack, World world,
			EntityPlayer entityplayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fire(ItemStack itemstack, World world,
			EntityPlayer entityplayer, int i) {
	
		return;
	}

	@Override
	public void effectPlayer(ItemStack itemstack, EntityPlayer entityplayer,
			World world) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void effectShoot(World world, double x, double y, double z,
			float yaw, float pitch) {
		// TODO Auto-generated method stub
		return;
	}

}
