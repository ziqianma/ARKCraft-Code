package com.arkcraft.module.weapon.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityStoneArrow extends EntityArkArrow
{
	public EntityStoneArrow(World worldIn)
	{
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
		this.setDamage(3);
	}

	public EntityStoneArrow(World worldIn, double x, double y, double z)
	{
		super(worldIn);
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
		this.setPosition(x, y, z);
		this.setDamage(3);
	}

	public EntityStoneArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float speed, float accuracy)
	{
		super(worldIn, shooter, target, speed, accuracy);
		this.setDamage(3);
	}

	public EntityStoneArrow(World worldIn, EntityLivingBase shooter, float speed)
	{
		super(worldIn, shooter, speed);
		this.setDamage(3);
	}

	public EntityStoneArrow(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy)
	{
		super(worldIn, shooter, speed, inaccuracy);
	}
}