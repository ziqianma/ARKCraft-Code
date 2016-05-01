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

	public abstract int getBaseKillXP();

	protected int fromDays(int days)
	{
		return days * 24000;
	}

	public abstract int getBaseHealth();

	public abstract int getBaseWeight();

	public abstract int getBaseOxygen();

	public abstract int getBaseFood();

	/**
	 * Returns the base damage of the creature, to be multiplied by their
	 * upgraded stat (%)
	 */
	public abstract int getBaseDamage();

	/**
	 * Returns the base speed of the creature, to be multiplied by their
	 * upgraded stat (%)
	 */
	public abstract int getBaseSpeed();

	public abstract int getBaseStamina();

	public abstract int getBaseTorpor();

	public abstract double getTamedHealthIncrease();

	public abstract double getTamedWeightIncrease();

	public abstract double getTamedOxygenIncrease();

	public abstract double getTamedFoodIncrease();

	public abstract double getTamedDamageIncrease();

	public abstract double getTamedSpeedIncrease();

	public abstract double getTamedStaminaIncrease();

	public abstract double getTamedTorporIncrease();

	public abstract double getWildHealthIncrease();

	public abstract double getWildWeightIncrease();

	public abstract double getWildOxygenIncrease();

	public abstract double getWildFoodIncrease();

	public abstract double getWildDamageIncrease();

	public abstract double getWildStaminaIncrease();

	public abstract double getWildTorporIncrease();

	/**
	 * returns the multiplier for the xp needed to level up
	 */
	public double getLevelingMultiplier()
	{
		return 1;
	}
}