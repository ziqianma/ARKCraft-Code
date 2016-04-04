package com.arkcraft.module.weapon.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityAdvancedBullet extends EntityProjectile
{
	public EntityAdvancedBullet(World world)
	{
		super(world);
		setDamage(14);
	}

	public EntityAdvancedBullet(World world, double d, double d1, double d2)
	{
		this(world);
		setPosition(d, d1, d2);
		setDamage(14);
	}

	public EntityAdvancedBullet(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy)
	{
		super(worldIn, shooter, speed, inaccuracy);
		setDamage(14);
	}

	@Override
	public float getGravity()
	{
		return 0.005F;
	}

	@Override
	public float getAirResistance()
	{
		return 0.98F;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (secsInAir > 1)
		{
			setDead();
		}

		worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0.0D, 0.0D,
				0.0D);

	}

	@Override
	public void onGroundHit(MovingObjectPosition movingobjectposition)
	{
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		breakGlass(movingobjectposition);
		this.setDead();
	}

}