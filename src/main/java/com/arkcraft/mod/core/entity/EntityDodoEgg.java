package com.arkcraft.mod.core.entity;

import com.arkcraft.mod.core.entity.passive.EntityDodo;
import com.arkcraft.mod.core.items.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityDodoEgg extends EntityThrowable
{ 
    public EntityDodoEgg(World w)
    {
        super(w);
    }

    public EntityDodoEgg(World w, EntityLivingBase base)
    {
        super(w, base);
    }

    public EntityDodoEgg(World w, double x, double y, double z)
    {
        super(w, x, y, z);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition pos)
    {
        if (pos.entityHit != null)
        {
        	pos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 0.0F);
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

        @SuppressWarnings("unused")
        double d0 = 0.08D;

        for (int j = 0; j < 8; ++j)
        {
//        	worldObj.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, this.posX, this.posY, this.posZ, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, ((double)this.rand.nextFloat() - 0.5D) * 0.08D, new int[] {Item.getIdFromItem(ModItems.dodo_egg)});
        }
       

        if (!this.worldObj.isRemote)
        {
            this.setDead();
        }
    }
}