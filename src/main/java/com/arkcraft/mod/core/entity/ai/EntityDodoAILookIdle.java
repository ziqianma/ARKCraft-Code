package com.arkcraft.mod.core.entity.ai;

import com.arkcraft.mod.core.entity.passive.EntityDodo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

/***
 * 
 * @author wildbill22
 *
 */
public class EntityDodoAILookIdle extends EntityAIBase {
    /** The entity that is looking idle. */
    private EntityLiving idleEntity;
    /** X offset to look at */
    private double lookX;
    /** Z offset to look at */
    private double lookZ;
    /** A decrementing tick that stops the entity from being idle once it reaches 0. */
    private int idleTime;

    public EntityDodoAILookIdle(EntityLiving p_i1647_1_) {
        this.idleEntity = p_i1647_1_;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return this.idleEntity.getRNG().nextFloat() < 0.02F;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return this.idleTime >= 0;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        double d0 = (Math.PI * 2D) * this.idleEntity.getRNG().nextDouble();
        this.lookX = Math.cos(d0);
        this.lookZ = Math.sin(d0);
        this.idleTime = 20 + this.idleEntity.getRNG().nextInt(20);
    	((EntityDodo)this.idleEntity).setEyesOpen(true);
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        --this.idleTime;
        this.idleEntity.getLookHelper().setLookPosition(this.idleEntity.posX + this.lookX, this.idleEntity.posY + (double)this.idleEntity.getEyeHeight(), this.idleEntity.posZ + this.lookZ, 10.0F, (float)this.idleEntity.getVerticalFaceSpeed());
        if (this.idleTime == 20) {
        	((EntityDodo)this.idleEntity).setEyesOpen(false);
//        	LogHelper.info("EntityDodoAILookIdle: Closed eyes");
        } else if (this.idleTime <= 10) {
        	((EntityDodo)this.idleEntity).setEyesOpen(true);
//        	LogHelper.info("EntityDodoAILookIdle: Opened eyes");
        }
    }
}
