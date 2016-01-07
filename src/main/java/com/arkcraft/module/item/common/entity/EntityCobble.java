package com.arkcraft.module.item.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityCobble extends EntityThrowable
{

    public EntityCobble(World w) { super(w); }

    public EntityCobble(World w, EntityLivingBase base) { super(w, base); }

    @Override
    protected void onImpact(MovingObjectPosition mop)
    {
        /* Damage on impact */
        float dmg = 2;
        if (mop.entityHit != null)
        {
            mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), dmg);
        }

        for (int i = 0; i < 4; i++)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
        }
        if (this.worldObj.isRemote)
        {
            this.setDead();
        }
    }

}