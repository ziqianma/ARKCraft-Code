package com.arkcraft.mod.core.entity;

import com.arkcraft.mod.core.items.ARKTranqAmmo;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityTranqAmmo extends EntityThrowable
{
    public EntityTranqAmmo(World par1World)
    {
        super(par1World);
        setSpeed();
    }
    public EntityTranqAmmo(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World, par2EntityLivingBase);
        setSpeed();
    }
    public EntityTranqAmmo(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
        setSpeed();
    }
    
    private void setSpeed()
    {
    	setThrowableHeading(this.motionX, this.motionY, this.motionZ, 3.0F, 1.0F);
    }
    
    /**
    * Called when this EntityThrowable hits a block or entity.
    */
    protected void onImpact(MovingObjectPosition movObjPos)
    {
    	if (movObjPos != null) {
			if (movObjPos.entityHit instanceof Entity) {
				movObjPos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 
						(float) ARKTranqAmmo.tranqArrowDamage);
			}
			else {
				// Do something for instance if it hits a tree
				return;  // no crit and not dead
			}
		}
		for (int i = 0; i < 4; ++i)	{
			this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
		}
		// Get rid of the used spear
		if (!this.worldObj.isRemote)
			this.setDead();
	}
    
}
    
//    @Override
//    protected float getGravityVelocity() 
//    {
//    	return 0;
//    }   

