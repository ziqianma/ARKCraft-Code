package com.arkcraft.module.core.common.entity.ai;

import com.arkcraft.module.core.common.entity.DinoTameableTest;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

/**
 * @author wildbill22
 */
public class EntityDinoAIOwnerHurtTarget extends EntityAITarget
{
    DinoTameableTest theEntityTameable;
    EntityLivingBase theTarget;
    private int lastAttackerTime;

    public EntityDinoAIOwnerHurtTarget(DinoTameableTest entity)
    {
        super(entity, false);
        this.theEntityTameable = entity;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.theEntityTameable.isTamed())
        {
            return false;
        }
        else
        {
            EntityLivingBase entitylivingbase = this.theEntityTameable.getOwnerEntity();
            if (entitylivingbase == null)
            {
                return false;
            }
            else
            {
                this.theTarget = entitylivingbase.getLastAttacker();
                int i = entitylivingbase.getLastAttackerTime();
                return i != this.lastAttackerTime && this.isSuitableTarget(this.theTarget, false)
                        && this.theEntityTameable.func_142018_a(this.theTarget, entitylivingbase);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.theTarget);
        EntityLivingBase entitylivingbase = this.theEntityTameable.getOwnerEntity();
        if (entitylivingbase != null)
        {
            this.lastAttackerTime = entitylivingbase.getLastAttackerTime();
        }
        super.startExecuting();
    }
}
