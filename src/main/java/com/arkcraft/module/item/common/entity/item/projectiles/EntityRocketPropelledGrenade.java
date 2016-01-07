package com.arkcraft.module.item.common.entity.item.projectiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityRocketPropelledGrenade extends Test
{
    public float explosionRadius = 100F;

    public EntityRocketPropelledGrenade(World world)
    {
        super(world);
    }

    public EntityRocketPropelledGrenade(World world, double x, double y, double z)
    {
        this(world);
        setPosition(x, y, z);
    }

    public EntityRocketPropelledGrenade(World worldIn, EntityLivingBase shooter, float speed)
    {
        super(worldIn);
        this.shootingEntity = shooter;

        if (shooter instanceof EntityPlayer)
        {
            this.canBePickedUp = 0;
        }

        this.setSize(0.05F, 0.05F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + (double) shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
        setThrowableHeading(motionX, motionY, motionZ, speed * 1.7F, 1.5F);
    }

    @Override
    public float getGravity()
    {
        return 0.0035F;
    }

    @Override
    public float getAirResistance()
    {
        return 0.99F;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        double amount = 16D;
        float speed = 1F;
        if (speed == 1F)
        {
            for (int i1 = 1; i1 < amount; i1++)
            {
                worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX + (motionX * i1) / amount, posY + (motionY * i1) / amount, posZ + (motionZ * i1) / amount, 0.0D, 0.0D, 0.0D);
            }
        }
/*		if(onGround)
        {
			BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
			if (this.worldObj.isAirBlock(blockpos))
            {
                this.worldObj.setBlockState(blockpos, Blocks.fire.getDefaultState());
            }
		}	*/
    }

    @Override
    public void onEntityHit(Entity entity)
    {
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius, true);
        this.setDead();
    }

    @Override
    public void onGroundHit(MovingObjectPosition movingobjectposition)
    {
        this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius, true);
        this.setDead();
    }
}