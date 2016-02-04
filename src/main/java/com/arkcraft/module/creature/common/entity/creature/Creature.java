package com.arkcraft.module.creature.common.entity.creature;

import com.arkcraft.module.creature.common.entity.EntityARKCreature;
import com.arkcraft.module.creature.common.entity.SaddleType;

/**
 * @author gegy1000
 */
public abstract class Creature
{
	public abstract String getName();

	public abstract Class<? extends EntityARKCreature> getEntityClass();

	public abstract TamingType getTameType();

	public abstract SaddleType getSaddleType();

	public abstract int getGrowthTime();

	public abstract int getBabySizeXZ();

	public abstract int getBabySizeY();

	public abstract int getAdultSizeXZ();

	public abstract int getAdultSizeY();

	public abstract int getTorporLossSpeed();

	public abstract int getKillXP();

	protected int fromDays(int days)
	{
		return days * 24000;
	}

	public abstract int getInventorySize();

	public abstract int getBaseHealth();

	public abstract int getBaseWeight();

	public abstract int getBaseOxygen();

	public abstract int getBaseFood();

	public abstract int getBaseDamage();

	public abstract int getBaseSpeed();

	public abstract int getBaseStamina();

	public abstract int getBaseTorpor();

	public abstract int getTamedHealthIncrease();

	public abstract int getTamedWeightIncrease();

	public abstract int getTamedOxygenIncrease();

	public abstract int getTamedFoodIncrease();

	public abstract int getTamedDamageIncrease();

	public abstract int getTamedSpeedIncrease();

	public abstract int getTamedStaminaIncrease();

	public abstract int getTamedTorporIncrease();

	public abstract int getWildHealthIncrease();

	public abstract int getWildWeightIncrease();

	public abstract int getWildOxygenIncrease();

	public abstract int getWildFoodIncrease();

	public abstract int getWildDamageIncrease();

	public abstract int getWildStaminaIncrease();

	public abstract int getWildTorporIncrease();
}