package com.arkcraft.mod.common.entity.item.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityGrenade extends EntityThrowable {

	public int explosionRadius = 2;

	public EntityGrenade(World w) {
		super(w);
	}

	public EntityGrenade(World w, EntityLivingBase par2) {
		super(w, par2);

	}

	public EntityGrenade(World w, double x, double y, double z) {
		super(w);
		setPosition(x, y, z);

	}
	
	public EntityGrenade(World world, EntityLivingBase entityliving, float f)
	{
		this(world);
	//	getThrower = entityliving;
		setLocationAndAngles(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
		posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
		posY -= 0.1D;
		posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
		setPosition(posX, posY, posZ);
		motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F);
		motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		setSpeed(f);
		
	}
	
	private void setSpeed(float f)
    {
		setThrowableHeading(motionX, motionY, motionZ, f * 1.1F, 2.0F);
    }

	@Override
	protected void onImpact(MovingObjectPosition par1) {
		/* Explosion done here */
		/* Will there be damage on impact? */
		
		/**
		 * if(mop.entityHit != null)
		 * mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this,
		 * this.getThrower()), dmg);
		 * 
		 * for(int i = 0; i < 4; i++)
		 * this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX,
		 * this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		 * 
		 */
		
		this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, (float) this.explosionRadius, true);
		if (worldObj.isRemote)
			this.setDead();
	}
}
