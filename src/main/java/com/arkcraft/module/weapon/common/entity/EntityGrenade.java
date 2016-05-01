package com.arkcraft.module.weapon.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityGrenade extends EntityProjectile
{
	double bounceFactor1;
	double bounceFactor = 0.8;
	int fuse = 120;
	boolean stopped = false;

	public EntityGrenade(World world)
	{
		super(world);
		setSize(0.5F, 0.5F);
		bounceFactor = 0.75;
	}

	public EntityGrenade(World w, double x, double y, double z)
	{
		super(w);
		setPosition(x, y, z);
	}

	public EntityGrenade(World world, EntityLivingBase entity)
	{
		super(world);

		setRotation(entity.rotationYaw, 0);
		// Set the velocity
		double xHeading = -MathHelper.sin((entity.rotationYaw * 3.141593F) / 180F);
		double zHeading = MathHelper.cos((entity.rotationYaw * 3.141593F) / 180F);
		motionX = 0.5 * xHeading * MathHelper.cos((entity.rotationPitch / 180F) * 3.141593F);
		motionY = -0.5 * MathHelper.sin((entity.rotationPitch / 180F) * 3.141593F);
		motionZ = 0.5 * zHeading * MathHelper.cos((entity.rotationPitch / 180F) * 3.141593F);

		// Set the position
		setPosition(entity.posX, entity.posY, entity.posZ);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (!worldObj.isRemote)
		{
			if (ticksExisted == fuse)
			{
				explode();
			}
		}

		if (!this.stopped)
		{
			double prevVelX = this.motionX;
			double prevVelY = this.motionY;
			double prevVelZ = this.motionZ;
			prevPosX = posX;
			prevPosY = posY;
			prevPosZ = posZ;
			moveEntity(motionX, motionY, motionZ);
			boolean collided = false;
			if (this.motionX != prevVelX)
			{
				this.motionX = -prevVelX;
				collided = true;
			}
			if (this.motionZ != prevVelZ)
			{
				this.motionZ = -prevVelZ;
				collided = true;
			}
			if (this.motionY != prevVelY)
			{
				this.motionY = -prevVelY;
				collided = true;
			}
			else
			{
				this.motionY -= 0.04;
			}
			if (collided)
			{
				this.motionX *= this.bounceFactor;
				this.motionY *= this.bounceFactor;
				this.motionZ *= this.bounceFactor;
			}
			this.motionX *= 1.0;
			this.motionY *= 0.99;
			this.motionZ *= 1.0;
			if (Math.abs(this.motionX) + Math.abs(this.motionY) + Math.abs(this.motionZ) < 0.2)
			{
				this.stopped = true;
				this.motionX = 0.0;
				this.motionY = 0.0;
				this.motionZ = 0.0;
			}
		}
	}

	private void explode()
	{
		this.worldObj.createExplosion(this, posX, posY, posZ, 4F, true);
		this.setDead();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbttagcompound)
	{
		nbttagcompound.setByte("Fuse", (byte) fuse);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbttagcompound)
	{
		fuse = nbttagcompound.getByte("Fuse");
	}

}
