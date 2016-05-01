package com.arkcraft.module.weapon.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityMetalArrow extends EntityArkArrow
{
	public EntityMetalArrow(World worldIn)
	{
		super(worldIn);
		this.setDamage(5);
	}

	public EntityMetalArrow(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		this.setDamage(5);
	}

	public EntityMetalArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float speed, float inaccuracy)
	{
		super(worldIn, shooter, target, speed, inaccuracy);
		this.setDamage(5);
	}

	public EntityMetalArrow(World worldIn, EntityLivingBase shooter, float speed)
	{
		super(worldIn, shooter, speed);
		this.setDamage(5);
	}

	public EntityMetalArrow(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy)
	{
		super(worldIn, shooter, speed, inaccuracy);
		this.setDamage(5);
	}
}