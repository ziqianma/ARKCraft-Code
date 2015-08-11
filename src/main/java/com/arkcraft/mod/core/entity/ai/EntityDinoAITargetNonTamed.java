package com.arkcraft.mod.core.entity.ai;

import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import com.arkcraft.mod.core.entity.DinoTameable;

public class EntityDinoAITargetNonTamed extends EntityAINearestAttackableTarget {
    private DinoTameable theTameable;

    @SuppressWarnings("rawtypes")
	public EntityDinoAITargetNonTamed(DinoTameable thisTameable, Class target, boolean shouldCheckSite) {
        super(thisTameable, target, shouldCheckSite, false);
        this.theTameable = thisTameable;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return !this.theTameable.isTamed() && super.shouldExecute();
    }

}
