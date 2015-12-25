package com.arkcraft.mod2.common.entity.item.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import com.arkcraft.mod2.common.items.ARKCraftItems;
import com.arkcraft.mod2.common.items.weapons.handlers.WeaponDamageSource;

public class EntitySpear extends Test
{
	public EntitySpear(World world)
	{
		super(world);
	}
	
	public EntitySpear(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
	}
	
	public EntitySpear(World world, EntityLivingBase entityliving, float speed)
	{
		this(world);
		shootingEntity = entityliving;
		if (entityliving instanceof EntityPlayer)
	        {
	            this.canBePickedUp = 1;
	        }
		setLocationAndAngles(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
		posX -= MathHelper.cos((rotationYaw / 180F) * 3.141593F) * 0.16F;
		posY -= 0.1D;
		posZ -= MathHelper.sin((rotationYaw / 180F) * 3.141593F) * 0.16F;
		setPosition(posX, posY, posZ);
		motionX = -MathHelper.sin((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		motionY = -MathHelper.sin((rotationPitch / 180F) * 3.141593F);
		motionZ = MathHelper.cos((rotationYaw / 180F) * 3.141593F) * MathHelper.cos((rotationPitch / 180F) * 3.141593F);
		setThrowableHeading(motionX, motionY, motionZ, speed * 1.2F, 2.0F);
		
	}
	
	@Override
	public void onEntityHit(Entity entity)
	{
		float damage = 15F ;
		DamageSource damagesource = null;
		if (shootingEntity == null)
		{
			damagesource = WeaponDamageSource.causeThrownDamage(this, this);
		} else
		{
			damagesource = WeaponDamageSource.causeThrownDamage(this, shootingEntity);
		}
		if (entity.attackEntityFrom(damagesource, damage))
		{
			if (entity instanceof EntityLivingBase && worldObj.isRemote)
			{
				((EntityLivingBase) entity).setArrowCountInEntity(((EntityLivingBase) entity).getArrowCountInEntity() + 1);
			}
			applyEntityHitEffects(entity);
			playHitSound();
			setDead();
			} else
			{
				bounceBack();
			}
	}
	
	@Override
	public void playHitSound()
	{
		worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.9F));
	}
	
	@Override
	public float getGravity()
	{
		return 0.03F;
	}

	@Override
	public int getMaxArrowShake()
	{
		return 4;
	}
	
	@Override
	public ItemStack getPickupItem()
	{
		return new ItemStack(ARKCraftItems.spear, 1);
	}
}