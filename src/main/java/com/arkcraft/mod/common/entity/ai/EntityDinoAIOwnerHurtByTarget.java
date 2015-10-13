package com.arkcraft.mod.common.entity.ai;

import com.arkcraft.mod.common.entity.DinoTameableTest;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;

/***
 * 
 * @author wildbill22
 *
 */
public class EntityDinoAIOwnerHurtByTarget  extends EntityAITarget {
    DinoTameableTest theDefendingTameable;
    EntityLivingBase theOwnerAttacker;
    private int revengeTimer;

    public EntityDinoAIOwnerHurtByTarget(DinoTameableTest entity) {
        super(entity, false);
        this.theDefendingTameable = entity;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (!this.theDefendingTameable.isTamed()) {
            return false;
        }
        else {
            EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwnerEntity();
            if (entitylivingbase == null) {
                return false;
            } else {
                this.theOwnerAttacker = entitylivingbase.getAITarget();
                int i = entitylivingbase.getRevengeTimer();
                return i != this.revengeTimer && this.isSuitableTarget(this.theOwnerAttacker, false) 
                		&& this.theDefendingTameable.func_142018_a(this.theOwnerAttacker, entitylivingbase);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theOwnerAttacker);
        EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwnerEntity();
        if (entitylivingbase != null) {
            this.revengeTimer = entitylivingbase.getRevengeTimer();
        }
        super.startExecuting();
    }
}
