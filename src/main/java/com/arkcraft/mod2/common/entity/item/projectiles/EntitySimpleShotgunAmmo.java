package com.arkcraft.mod2.common.entity.item.projectiles;

import com.arkcraft.mod2.common.items.weapons.component.RangedComponent;
import com.arkcraft.mod2.common.items.weapons.handlers.WeaponDamageSource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntitySimpleShotgunAmmo extends Test
{
	public EntitySimpleShotgunAmmo(World world)
	{
		super(world);
	}
	
	public EntitySimpleShotgunAmmo(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
	}
	
	
	public EntitySimpleShotgunAmmo(World worldIn, EntityLivingBase shooter)
	{
	        super(worldIn);
	        this.shootingEntity = shooter;

	        if (shooter instanceof EntityPlayer)
	        {
	            this.canBePickedUp = 0;
	        }

	        this.setSize(0.05F, 0.05F);
	        this.setLocationAndAngles(shooter.posX, shooter.posY + (double)shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
	        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
	        this.posY -= 0.10000000149011612D;
	        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
	        this.setPosition(this.posX, this.posY, this.posZ);
	        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
	        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
	        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
	        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 3F, 10.0F);
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
		
		if (ticksInAir > 5)
		{
			setDead();
		}

		float speed = 3F;
		if (speed == 3F)
		{
			worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
		}
	}
	
	@Override
	public void onEntityHit(Entity entity)
	{
		float damage = 10F;
		
		DamageSource damagesource;
		if (shootingEntity == null)
		{
			damagesource = WeaponDamageSource.causeThrownDamage(this, this);
		} else
		{
			damagesource = WeaponDamageSource.causeThrownDamage(this, shootingEntity);
		}
		
		int prevhurtrestime = entity.hurtResistantTime;
		if (entity.attackEntityFrom(damagesource, damage))
		{
			entity.hurtResistantTime = prevhurtrestime;
			applyEntityHitEffects(entity);
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
	
	public static void fireSpreadShot(World world, EntityLivingBase entityliving, RangedComponent item, ItemStack itemstack)
	{
		EntitySimpleShotgunAmmo entity;
		for (int i = 0; i < 2; i++)
		{
			entity = new EntitySimpleShotgunAmmo(world, entityliving);
			if (item != null && itemstack != null)
			{
				item.applyProjectileEnchantments(entity, itemstack);
			}
			world.spawnEntityInWorld(entity);
		}
	}
	
	public static void fireSpreadShot(World world, double x, double y, double z)
	{
		for (int i = 0; i < 2; i++)
		{
			world.spawnEntityInWorld(new EntitySimpleShotgunAmmo(world, x, y, z));
		}
	}
	
	public static void fireFromDispenser(World world, double d, double d1, double d2, int i, int j, int k)
	{
		for (int i1 = 0; i1 < 2; i1++)
		{
			EntitySimpleShotgunAmmo entityShotgunShot = new EntitySimpleShotgunAmmo(world, d, d1, d2);
			
			entityShotgunShot.setThrowableHeading(i, j, k, 3F, 10.0F);
			world.spawnEntityInWorld(entityShotgunShot);
		}
	}
}