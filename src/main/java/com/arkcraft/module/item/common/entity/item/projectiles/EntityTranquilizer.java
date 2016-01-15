package com.arkcraft.module.item.common.entity.item.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.common.entity.EntityTameableDinosaur;
import com.arkcraft.module.item.common.config.ModuleItemBalance;
import com.arkcraft.module.item.common.items.weapons.handlers.WeaponDamageSource;

public class EntityTranquilizer extends EntityProjectile
{

	public EntityTranquilizer(World world)
	{
		super(world);
	}

	public EntityTranquilizer(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
	}

	public EntityTranquilizer(World worldIn, EntityLivingBase shooter)
	{
		super(worldIn);
		this.shootingEntity = shooter;

		if (shooter instanceof EntityPlayer)
		{
			this.canBePickedUp = 1;
		}

		this.setSize(0.05F, 0.05F);
		this.setLocationAndAngles(shooter.posX, shooter.posY + (double) shooter.getEyeHeight(),
				shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
		this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.posY -= 0.10000000149011612D;
		this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
		this.setPosition(this.posX, this.posY, this.posZ);
		this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper
				.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper
				.cos(this.rotationPitch / 180.0F * (float) Math.PI));
		this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
		this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 3.2F, 1.0F);
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
		LogHelper.info(this.getTotalVelocity());
		float speed = 3F;
		if (speed == 3F)
		{
			worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0.0D,
					0.0D, 0.0D);
		}
	}

	@Override
	public void playHitSound()
	{
		worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F,
				1.0F / (rand.nextFloat() * 0.4F + 0.9F));
	}

	@Override
	public void onEntityHit(Entity entity)
	{

		float damage = (float) (ModuleItemBalance.WEAPONS.TRANQ_AMMO_DAMAGE);
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
		if (entity instanceof EntityTameableDinosaur)
		{
			((EntityTameableDinosaur) entity)
					.increaseTorpor(ModuleItemBalance.WEAPONS.TRANQ_AMMO_TORPOR_TIME);
		}
	}
}