package com.arkcraft.mod.core.entity;

import net.minecraft.entity.EntityLivingBase;


public interface ITamable {

	public int torpor = 0;
	public int progress = 0;
	public boolean tamable = false;
	
	public int getTorpor(EntityLivingBase dino);
	public int getProgress(EntityLivingBase dino);
	public boolean isTamable(EntityLivingBase dino);
}
