package com.arkcraft.module.item.common.entity.item.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.arkcraft.module.item.common.items.weapons.handlers.WeaponDamageSource;

public class EntitySimpleRifleAmmo extends EntityProjectile
{

	public EntitySimpleRifleAmmo(World world)
	{
		super(world);
	}

	public EntitySimpleRifleAmmo(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
	}

	public EntitySimpleRifleAmmo(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy)
	{
		super(worldIn, shooter, speed, inaccuracy);
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

		worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0.0D, 0.0D,
				0.0D);

	}

	@Override
	public void onEntityHit(Entity entity)
	{
		float damage = 15F;
		DamageSource damagesource = null;
		if (shootingEntity == null)
		{
			damagesource = WeaponDamageSource.causeThrownDamage(this, this);
		}
		else
		{
			damagesource = WeaponDamageSource.causeThrownDamage(this, shootingEntity);
		}
		if (entity.attackEntityFrom(damagesource, damage))
		{
			playHitSound();
			setDead();
		}
	}

	@Override
	public void onGroundHit(MovingObjectPosition movingobjectposition)
	{
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		this.setDead();
	}
}