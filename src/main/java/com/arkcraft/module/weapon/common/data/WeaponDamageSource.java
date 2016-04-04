package com.arkcraft.module.weapon.common.data;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

import com.arkcraft.module.weapon.common.entity.EntityProjectile;

public class WeaponDamageSource extends EntityDamageSourceIndirect
{
    private EntityProjectile projectileEntity;
    private Entity thrower;

    public WeaponDamageSource(String s, EntityProjectile projectile, Entity entity)
    {
        super(s, projectile, entity);
        projectileEntity = projectile;
        thrower = entity;
    }

    public Entity getProjectile()
    {
        return projectileEntity;
    }

    @Override
    public Entity getEntity()
    {
        return thrower;
    }

    public static DamageSource causeProjectileWeaponDamage(EntityProjectile projectile, Entity entity)
    {
        return (new WeaponDamageSource("weapon", projectile, entity)).setProjectile();
    }
}