package com.arkcraft.module.weapon.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.blocks.common.config.ModuleItemBalance;
import com.arkcraft.module.creature.common.entity.EntityARKCreature;

public class EntityTranquilizer extends EntityProjectile implements ITranquilizer
{
	public EntityTranquilizer(World world)
	{
		super(world);
		this.setDamage(ModuleItemBalance.WEAPONS.TRANQ_AMMO_DAMAGE);
	}

	public EntityTranquilizer(World world, double x, double y, double z)
	{
		this(world);
		setPosition(x, y, z);
		this.setDamage(ModuleItemBalance.WEAPONS.TRANQ_AMMO_DAMAGE);
	}

	public EntityTranquilizer(World worldIn, EntityLivingBase shooter, float speed, float inaccuracy)
	{
		super(worldIn, shooter, speed, inaccuracy);
		this.setDamage(ModuleItemBalance.WEAPONS.TRANQ_AMMO_DAMAGE);
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
	public void playHitSound()
	{
		worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F,
				1.0F / (rand.nextFloat() * 0.4F + 0.9F));
	}

	@Override
	public void applyTorpor(Entity entityHit)
	{
		// TODO apply torpor to player
		if (entityHit instanceof EntityARKCreature)
		{
			LogHelper.info("Torpor applied: " + ModuleItemBalance.WEAPONS.TRANQ_AMMO_TORPOR_TIME);
			((EntityARKCreature) entityHit)
					.increaseTorpor(ModuleItemBalance.WEAPONS.TRANQ_AMMO_TORPOR_TIME);
		}
	}
}