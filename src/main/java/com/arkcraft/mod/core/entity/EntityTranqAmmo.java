package com.arkcraft.mod.core.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityTranqAmmo extends EntityProjectile{
	
	public EntityTranqAmmo(World world)
	{
		super(world);
	}
	
	public EntityTranqAmmo(World world, double d, double d1, double d2)
	{
		this(world);
		setPosition(d, d1, d2);
	}
	
	public EntityTranqAmmo(World world, EntityLivingBase entityliving)
	{
		this(world);
		shootingEntity = entityliving;
		setLocationAndAngles(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
		posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
		posY -= 0.1D;
		posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
		setPosition(posX, posY, posZ);
		motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F);
	}
	

	protected void onImpact(MovingObjectPosition mop) {
		/* Damage on impact */
		float dmg = 2;
		if(mop.entityHit != null) mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), dmg);
		
		for(int i = 0; i < 4; i++) this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		if(this.worldObj.isRemote) this.setDead();
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (inGround)
		{
			if (rand.nextInt(4) == 0)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
			}
			return;
		}
		double speed = getTotalVelocity();
		double amount = 16D;
		if (speed > 2.0D)
		{
			for (int i1 = 1; i1 < amount; i1++)
			{
				worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + (motionX * i1) / amount, posY + (motionY * i1) / amount, posZ + (motionZ * i1) / amount, 0.0D, 0.0D, 0.0D);
			}
		}
	}
	
	@Override
	public boolean aimRotation()
	{
		return false;
	}
	
	@Override
	public int getMaxLifetime()
	{
		return 200;
	}
	
	@Override
	public float getAirResistance()
	{
		return 0.98F;
	}
	
	@Override
	public float getGravity()
	{
		return getTotalVelocity() < 3F ? 0.07F : 0F;
	}
	
	@Override
	public int getMaxArrowShake()
	{
		return 0;
	}
}
