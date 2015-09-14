package com.arkcraft.mod.core.entity;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.items.ARKWeaponThrowable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityStoneSpear extends EntityThrowable {
    public Entity shootingEntity;
    /** 1 if the player can pick up the arrow */
    public int canBePickedUp;

	public EntityStoneSpear(World world, double p_i1778_2_, double p_i1778_4_, double p_i1778_6_) {
		super(world, p_i1778_2_, p_i1778_4_, p_i1778_6_);
	}
	
	public EntityStoneSpear(World par1World, EntityLivingBase par2EntityLivingBase) {
		super(par1World, par2EntityLivingBase);
	}
	
	public EntityStoneSpear(World par1World) {
		super(par1World);
	}
	
	// Added for attack from EntityAISpearAttack
    public EntityStoneSpear(World world, EntityLivingBase attacker, EntityLivingBase target, float p_i1755_4_, float p_i1755_5_)
    {
        super(world);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = attacker;

        if (attacker instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }

        this.posY = attacker.posY + (double)attacker.getEyeHeight() - 0.10000000149011612D;
        double d0 = target.posX - attacker.posX;
        double d1 = target.getBoundingBox().minY + (double)(target.height / 3.0F) - this.posY;
        double d2 = target.posZ - attacker.posZ;
        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);

        if (d3 >= 1.0E-7D)
        {
            float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
            float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
            double d4 = d0 / d3;
            double d5 = d2 / d3;
            this.setLocationAndAngles(attacker.posX + d4, this.posY, attacker.posZ + d5, f2, f3);
            float f4 = (float)d3 * 0.2F;
            this.setThrowableHeading(d0, d1 + (double)f4, d2, p_i1755_4_, p_i1755_5_);
        }
    }

	@Override
	protected void onImpact(MovingObjectPosition movObjPos) {
//		if (!this.worldObj.isRemote)
//			LogHelper.info("EntitySpear landed!");
			
		if (movObjPos != null) {
			if (movObjPos.entityHit instanceof Entity) {
				movObjPos.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 
						(float) ARKWeaponThrowable.spearDamage);
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
	
    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer player) {
//        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0)
    	if (!this.worldObj.isRemote && this.inGround) {
            boolean flag = this.canBePickedUp == 1 || this.canBePickedUp == 2 && player.capabilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !player.inventory.addItemStackToInventory(new ItemStack(GlobalAdditions.stoneSpear, 1))) {
                flag = false;
            }

            if (flag) {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }
}