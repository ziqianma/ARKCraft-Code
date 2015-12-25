package com.arkcraft.mod2.common.entity.item.projectiles;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBase extends Test
{
	double bounceFactor;
	int fuse = 120;
	
    public EntityBase(World world)
    {
        super(world);
        setSize(0.5F, 0.5F);
        bounceFactor = 0.75;
    }

	public EntityBase(World w, double x, double y, double z) {
		super(w);
		setPosition(x, y, z);
	}
	
    public EntityBase(World world, EntityLivingBase entity)
    {
        super(world);

    	setRotation(entity.rotationYaw, 0);
        // Set the velocity
    	double xHeading = -MathHelper.sin((entity.rotationYaw * 3.141593F) / 180F);
    	double zHeading = MathHelper.cos((entity.rotationYaw * 3.141593F) / 180F);
        motionX = 0.5*xHeading*MathHelper.cos((entity.rotationPitch / 180F) * 3.141593F);
        motionY = -0.5*MathHelper.sin((entity.rotationPitch / 180F) * 3.141593F);
        motionZ = 0.5*zHeading*MathHelper.cos((entity.rotationPitch / 180F) * 3.141593F);
    	
        // Set the position
        setPosition(entity.posX+xHeading*0.8, entity.posY, entity.posZ+zHeading*0.8);
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
                        
    }
    
    @Override
    public void onUpdate()
    {
    	super.onUpdate();
    	
    	 if (onGround)
    	 {
    	    motionY *= -1.0D;
    	 }
    	
    	double prevVelX = motionX;
    	double prevVelY = motionY;
    	double prevVelZ = motionZ;
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        moveEntity(motionX, motionY, motionZ);
        
        // Take into account bouncing (normal displacement just sets them to 0)
        if(motionX!=prevVelX)
        {
        	motionX = -bounceFactor*prevVelX;
        }
        if(motionY!=prevVelY)
        {
        	motionY = -bounceFactor*prevVelY;     
        }
        
        if(motionZ!=prevVelZ)
        {
        	motionZ = -bounceFactor*prevVelZ;
        }
        else
        {
        	motionY -= 0.04;
        }            
        
        // Air friction
        motionX *= 0.99;
        motionY *= 0.99;
        motionZ *= 0.99;
        
        if(!worldObj.isRemote)
		{
			if(ticksExisted == fuse)
			explode();
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
        nbttagcompound.setByte("Fuse", (byte)fuse);
    }
    @Override
    public void readEntityFromNBT(NBTTagCompound nbttagcompound)
    {
        fuse = nbttagcompound.getByte("Fuse");
    }
    
    
}
