package com.arkcraft.mod.core.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityTranqAmmo extends EntityThrowable
{
    public EntityTranqAmmo(World par1World)
    {
        super(par1World);
    }
    public EntityTranqAmmo(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World, par2EntityLivingBase);
    }
    public EntityTranqAmmo(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }
    /**
    * Called when this EntityThrowable hits a block or entity.
    */
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition)
    {
        if (par1MovingObjectPosition.entityHit != null)
        {
            byte b0 = 0;
            if (par1MovingObjectPosition.entityHit instanceof EntityBlaze)
            {
                b0 = 3;
            }
            par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), b0);
        }
        if (!this.worldObj.isRemote)
        {
            this.setDead();
        }
    }
    
    @Override
    protected float getGravityVelocity() 
    {
    	return 0;
    }
    
}