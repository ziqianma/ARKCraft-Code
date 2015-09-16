package com.arkcraft.mod.core.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.arkcraft.mod.core.entity.test.EntityProjectile;

public class EntityTranqAmmo extends EntityProjectile
{
	public EntityTranqAmmo(World world)
	{
		super(world);
		setSpeed();	
	}
	
	public EntityTranqAmmo(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
		setSpeed();	
	}
	
	public EntityTranqAmmo(World world, EntityLivingBase entityliving)
	{
		this(world);
		shootingEntity = entityliving;
		setPickupModeFromEntity(entityliving);
		setLocationAndAngles(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
		posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
		posY -= 0.1D;
		posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
		setPosition(posX, posY, posZ);
		motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F);
		motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		setSpeed();	
	}
	
	private void setSpeed()
    {
		 setThrowableHeading(this.motionX, this.motionY, this.motionZ, 3.0F, 1.0F);
    }
	
	/*
	@Override
	public void onEntityHit(Entity entity)
	{
		double vel = getTotalVelocity();
		int damage = MathHelper.ceiling_double_int(vel * (3D + extraDamage));
		if (getIsCritical())
		{
			damage += rand.nextInt(damage / 2 + 2);
		}
		DamageSource damagesource = null;
		if (shootingEntity == null)
		{
			damagesource = DamageSource.causeThrownDamage(this, this);
		} else
		{
			damagesource = DamageSource.causeThrownDamage(this, shootingEntity);
		}	
		if (entity.attackEntityFrom(damagesource, damage))
		{
			applyEntityHitEffects(entity);
			playHitSound();
			setDead();
		} else
		{
			bounceBack();
		}
	}	*/
	
	public void drawCritParticles()
    {
        for (int i = 0; i < 4; ++i)
        {
        	 this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double)i / 4.0D, this.posY + this.motionY * (double)i / 4.0D, this.posZ + this.motionZ * (double)i / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
        }
    }
	
	@Override
	public void playHitSound()
	{
		worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.9F));
	}
	
	@Override
	public boolean canBeCritical()
	{
		return true;
	}
	
	@Override
	public int getMaxArrowShake()
	{
		return 10;
	}
	
	@Override
	public float getGravity()
	{
		return 0.07F;
	}
	@Override
	protected float getGravityVelocity() 
	{
		return 0;
	}
	
}