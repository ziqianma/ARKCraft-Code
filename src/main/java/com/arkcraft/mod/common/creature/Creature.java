package com.arkcraft.mod.common.creature;

import com.arkcraft.mod.common.entity.EntityARKCreature;
import com.arkcraft.mod.common.entity.SaddleType;

/**
 * @author gegy1000
 */
public abstract class Creature
{
    public abstract String getName();

    public abstract Class<? extends EntityARKCreature> getEntityClass();

    public abstract boolean isTameable();

    public abstract SaddleType getSaddleType();

    public abstract int getGrowthTime();

    public abstract int getBabySizeXZ();
    public abstract int getBabySizeY();

    public abstract int getAdultSizeXZ();
    public abstract int getAdultSizeY();

    public abstract int getTorporKnockout();
    public abstract int getTorporLossSpeed();

    public abstract int getMeleeDamageBase();
    public abstract int getHealthBase();
    public abstract int getStaminaBase();
    public abstract int getHungerBase();
    public abstract int getWaterBase();
    public abstract int getOxygenBase();

    protected int fromDays(int days)
    {
        return days * 24000;
    }
}
