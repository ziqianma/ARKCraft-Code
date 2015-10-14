package com.arkcraft.mod.common.items.weapons.projectiles;

import com.arkcraft.mod.common.items.weapons.component.RangedComponent;
import com.arkcraft.mod.common.items.weapons.handlers.WeaponDamageSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntitySimpleShotgunAmmo extends EntityShootable
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
	
	public EntitySimpleShotgunAmmo(World world, EntityLivingBase entityliving)
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
		setThrowableHeading(motionX, motionY, motionZ, 5.0F, 15.0F);
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (ticksInAir > 5)
		{
			setDead();
		}
	}
	
	@Override
	public void onEntityHit(Entity entity)
	{
		float damage = 10F + extraDamage;
		
		DamageSource damagesource;
		if (thrower == null)
		{
			damagesource = WeaponDamageSource.causeProjectileWeaponDamage(this, this);
		} else
		{
			damagesource = WeaponDamageSource.causeProjectileWeaponDamage(this, thrower);
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
			EntitySimpleShotgunAmmo entityblundershot = new EntitySimpleShotgunAmmo(world, d, d1, d2);
			
			entityblundershot.setThrowableHeading(i, j, k, 5.0F, 15.0F);
			world.spawnEntityInWorld(entityblundershot);
		}
	}
}