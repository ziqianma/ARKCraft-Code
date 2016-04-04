package com.arkcraft.module.weapon.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySimpleShotgunAmmo extends EntityProjectile
{
	public EntitySimpleShotgunAmmo(World world)
	{
		super(world);
		this.setDamage(3);
	}

	public EntitySimpleShotgunAmmo(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
		this.setDamage(3);
	}

	public EntitySimpleShotgunAmmo(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy)
	{
		super(worldIn, shooter, speed, inaccuracy);
		this.setDamage(3);
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

		if (ticksInAir > 10)
		{
			setDead();
		}

		worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0.0D, 0.0D,
				0.0D);
	}

	@Override
	public void setKnockbackStrength(int knockBack)
	{
		this.knockbackStrength = 10;
	}

	@Override
	public void onGroundHit(MovingObjectPosition movingobjectposition)
	{
		worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		breakGlass(movingobjectposition);
		this.setDead();
	}

	public static void fireFromDispenser(World world, double d, double d1, double d2, int i, int j, int k)
	{
		for (int i1 = 0; i1 < 2; i1++)
		{
			EntitySimpleShotgunAmmo entityShotgunShot = new EntitySimpleShotgunAmmo(world, d, d1,
					d2);

			entityShotgunShot.setThrowableHeading(i, j, k, 3F, 10.0F);
			world.spawnEntityInWorld(entityShotgunShot);
		}
	}
}