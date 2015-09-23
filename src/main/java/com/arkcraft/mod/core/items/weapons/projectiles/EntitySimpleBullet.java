package com.arkcraft.mod.core.items.weapons.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.arkcraft.mod.core.items.weapons.handlers.WeaponDamageSource;

public class EntitySimpleBullet extends EntityShootable
{
	
	public EntitySimpleBullet(World world)
	{
		super(world);
	//	inGround = false;
		
	}
	
	public EntitySimpleBullet(World world, double d, double d1, double d2)
	{
		this(world);
		setPosition(d, d1, d2);
	}
	
	public EntitySimpleBullet(World world, EntityLivingBase entityliving, float deviation)
	{
		this(world);
		thrower = entityliving;
		setLocationAndAngles(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
		posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
		posY -= 0.1D;
		posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
		setPosition(posX, posY, posZ);
		motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F);
		setThrowableHeading(motionX, motionY, motionZ, 5.0F, deviation);
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (inGround)
		{
			if (rand.nextInt(4) == 0)
			{
				worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
			}
			return;
		}
		double speed = getGravityVelocity();
		double amount = 16D;
		if (speed > 2.0D)
		{
			for (int i1 = 1; i1 < amount; i1++)
			{
				worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + (motionX * i1) / amount, posY + (motionY * i1) / amount, posZ + (motionZ * i1) / amount, 0.0D, 0.0D, 0.0D);
			}
			
			  if (!this.worldObj.isRemote)
		        {
		            this.setDead();
		        }
		}
	}	
	
	@Override
	public void onEntityHit(Entity entity)
	{
		float damage = 20F + extraDamage;
		DamageSource damagesource = null;
		if (thrower == null)
		{
			damagesource = WeaponDamageSource.causeProjectileWeaponDamage(this, this);
		} else
		{
			damagesource = WeaponDamageSource.causeProjectileWeaponDamage(this, thrower);
		}
		if (entity.attackEntityFrom(damagesource, damage))
		{
			playHitSound();
			setDead();
		}
	}
	
	@Override
	protected float getVelocity()
	{
	        return 0F;
	}
	@Override
	public boolean canBeCritical()
	{
		return true;
	}

//	@Override
//	protected void onImpact(MovingObjectPosition mop) {
//		/* Damage on impact */
//		float dmg = 5;
//		if(mop.entityHit != null) mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), dmg);
//		
//		for(int i = 0; i < 4; i++) this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
//		if(this.worldObj.isRemote) this.setDead();
//	}
}