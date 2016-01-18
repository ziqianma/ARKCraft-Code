package com.arkcraft.module.item.common.entity.item.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityMetalArrow extends EntityArkArrow
{
	public EntityMetalArrow(World worldIn)
	{
		super(worldIn);
	}

	public EntityMetalArrow(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}

	public EntityMetalArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float speed, float inaccuracy)
	{
		super(worldIn, shooter, target, speed, inaccuracy);
	}

	public EntityMetalArrow(World worldIn, EntityLivingBase shooter, float speed)
	{
		super(worldIn, shooter, speed);
	}

	public EntityMetalArrow(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy)
	{
		super(worldIn, shooter, speed, inaccuracy);
	}
}