package com.arkcraft.module.core.common.entity.ai;

import com.arkcraft.module.creature.common.entity.DinoTameableTest;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;

public class EntityDinoAITargetNonTamed extends EntityAINearestAttackableTarget
{
    private DinoTameableTest theTameable;

    @SuppressWarnings("rawtypes")
    public EntityDinoAITargetNonTamed(DinoTameableTest thisTameable, Class target, boolean shouldCheckSite)
    {
        super(thisTameable, target, shouldCheckSite, false);
        this.theTameable = thisTameable;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return !this.theTameable.isTamed() && super.shouldExecute();
    }

}
