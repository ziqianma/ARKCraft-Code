package com.arkcraft.module.weapon.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityArkArrow extends EntityProjectile
{
	public EntityArkArrow(World worldIn)
	{
		super(worldIn);
		init(null);
	}

	public EntityArkArrow(World worldIn, double x, double y, double z)
	{
		super(worldIn);
		init(null);
		this.setPosition(x, y, z);
	}

	public EntityArkArrow(World worldIn, EntityLivingBase shooter, float speed)
	{
		super(worldIn, shooter, speed);
		init(shooter);
	}

	@Override
	public void onEntityHit(Entity entityHit)
	{
		super.onEntityHit(entityHit);
	}

	public EntityArkArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float speed, float inaccuracy)
	{
		super(worldIn);
		this.shootingEntity = shooter;
		init(shooter);

		this.posY = shooter.posY + (double) shooter.getEyeHeight() - 0.10000000149011612D;
		double d0 = target.posX - shooter.posX;
		double d1 = target.getEntityBoundingBox().minY + (double) (target.height / 3.0F) - this.posY;
		double d2 = target.posZ - shooter.posZ;
		double d3 = (double) MathHelper.sqrt_double(d0 * d0 + d2 * d2);

		if (d3 >= 1.0E-7D)
		{
			float f2 = (float) (Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
			float f3 = (float) (-(Math.atan2(d1, d3) * 180.0D / Math.PI));
			double d4 = d0 / d3;
			double d5 = d2 / d3;
			this.setLocationAndAngles(shooter.posX + d4, this.posY, shooter.posZ + d5, f2, f3);
			float f4 = (float) (d3 * 0.20000000298023224D);
			this.setThrowableHeading(d0, d1 + (double) f4, d2, speed, inaccuracy);
		}
	}

	public EntityArkArrow(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy)
	{
		super(worldIn, shooter, speed, inaccuracy);
		init(shooter);
	}

	private void init(EntityLivingBase shooter)
	{
		this.renderDistanceWeight = 10.0D;
		this.setSize(0.5F, 0.5F);
		if (shooter instanceof EntityPlayer)
		{
			this.canBePickedUp = 1;
		}
	}
}
