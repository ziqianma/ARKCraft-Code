package com.arkcraft.mod.core.items.weapons.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityStoneArrow extends EntityArrow{
	
	 public EntityStoneArrow(World worldIn)
	    {
	        super(worldIn);
	        this.renderDistanceWeight = 10.0D;
	        this.setSize(0.5F, 0.5F);
	    }
	 	
	    public EntityStoneArrow(World worldIn, double x, double y, double z)
	    {
	        super(worldIn);
	        this.renderDistanceWeight = 10.0D;
	        this.setSize(0.5F, 0.5F);
	        this.setPosition(x, y, z);
	    }

	    public EntityStoneArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_)
	    {
	        super(worldIn);
	        this.renderDistanceWeight = 10.0D;
	        this.shootingEntity = shooter;

	        if (shooter instanceof EntityPlayer)
	        {
	            this.canBePickedUp = 1;
	        }

	        this.posY = shooter.posY + (double)shooter.getEyeHeight() - 0.10000000149011612D;
	        double d0 = p_i1755_3_.posX - shooter.posX;
	        double d1 = p_i1755_3_.getEntityBoundingBox().minY + (double)(p_i1755_3_.height / 3.0F) - this.posY;
	        double d2 = p_i1755_3_.posZ - shooter.posZ;
	        double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);

	        if (d3 >= 1.0E-7D)
	        {
	            float f2 = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
	            float f3 = (float)(-(Math.atan2(d1, d3) * 180.0D / Math.PI));
	            double d4 = d0 / d3;
	            double d5 = d2 / d3;
	            this.setLocationAndAngles(shooter.posX + d4, this.posY, shooter.posZ + d5, f2, f3);
	            float f4 = (float)(d3 * 0.20000000298023224D);
	            this.setThrowableHeading(d0, d1 + (double)f4, d2, p_i1755_4_, p_i1755_5_);
	        }
	    }

	    public EntityStoneArrow(World worldIn, EntityLivingBase shooter, float p_i1756_3_)
	    {
	        super(worldIn);
	        this.renderDistanceWeight = 10.0D;
	        this.shootingEntity = shooter;

	        if (shooter instanceof EntityPlayer)
	        {
	            this.canBePickedUp = 1;
	        }

	        this.setSize(0.5F, 0.5F);
	        this.setLocationAndAngles(shooter.posX, shooter.posY + (double)shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
	        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
	        this.posY -= 0.10000000149011612D;
	        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
	        this.setPosition(this.posX, this.posY, this.posZ);
	        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
	        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
	        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
	        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, p_i1756_3_ * 1.5F, 1.0F);
	    }

	    protected void entityInit()
	    {
	        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
	    }
}