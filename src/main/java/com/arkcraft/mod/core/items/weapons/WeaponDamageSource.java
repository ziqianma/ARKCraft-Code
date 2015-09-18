package com.arkcraft.mod.core.items.weapons;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

import com.arkcraft.mod.core.items.weapons.projectiles.EntityProjectile;

public class WeaponDamageSource extends EntityDamageSourceIndirect
{
	private EntityProjectile	projectileEntity;
	private Entity				shooterEntity;
	
	public WeaponDamageSource(String s, EntityProjectile projectile, Entity entity)
	{
		super(s, projectile, entity);
		projectileEntity = projectile;
		shooterEntity = entity;
	}
	
	public Entity getProjectile()
	{
		return projectileEntity;
	}
	
	@Override
	public Entity getEntity()
	{
		return shooterEntity;
	}
	
	public static DamageSource causeProjectileWeaponDamage(EntityProjectile projectile, Entity entity)
	{
		return (new WeaponDamageSource("weapon", projectile, entity)).setProjectile();
	}
}