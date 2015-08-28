package com.arkcraft.mod.core.entity;

import com.arkcraft.mod.core.entity.passive.EntityDodo;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityDodoEgg extends EntityThrowable
{ 
    public EntityDodoEgg(World worldIn)
    {
        super(worldIn);
    }

    public EntityDodoEgg(World worldIn, EntityLivingBase p_i1780_2_)
    {
        super(worldIn, p_i1780_2_);
    }

    public EntityDodoEgg(World worldIn, double p_i1781_2_, double p_i1781_4_, double p_i1781_6_)
    {
        super(worldIn, p_i1781_2_, p_i1781_4_, p_i1781_6_);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition p_70184_1_)
    {
        if (p_70184_1_.entityHit != null)
        {
            p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
        }

        if (!this.worldObj.isRemote && this.rand.nextInt(8) == 0)
        {
            byte b0 = 1;

            if (this.rand.nextInt(32) == 0)
            {
                b0 = 4;
            }

            for (int i = 0; i < b0; ++i)
            {
            	EntityDodo entitydodo = new EntityDodo(this.worldObj);
            	entitydodo.setGrowingAge(-24000);
            	entitydodo.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                this.worldObj.spawnEntityInWorld(entitydodo);
            }
        }

        double d0 = 0.08D;

        for (int j = 0; j < 8; ++j)
        {
            this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, new int[] {Item.getIdFromItem(Items.egg)});
        }

        if (!this.worldObj.isRemote)
        {
            this.setDead();
        }
    }
}