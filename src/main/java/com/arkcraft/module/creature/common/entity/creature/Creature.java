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

	public abstract int getTorporKnockout();

	public abstract int getTorporLossSpeed();

	public abstract int getMeleeDamageBase();

	public abstract int getHealthBase();

	public abstract int getStaminaBase();

	public abstract int getHungerBase();

	public abstract int getSpeedBase();

	public abstract float getKillXP();

	protected int fromDays(int days)
	{
		return days * 24000;
	}

	public abstract int getInventorySize();

	public abstract int getBaseHealth();

	public abstract int getWildHealthIncrease();

	public abstract int getBaseWeight();

	public abstract int getWildWeightIncrease();

	public abstract int getBaseOxygen();

	public abstract int getWildOxygenIncrease();

	public abstract int getBaseFood();

	public abstract int getWildFoodIncrease();

	public abstract int getBaseDamage();

	public abstract int getWildDamageIncrease();

	public abstract int getBaseSpeed();

	public abstract int getWildSpeedIncrease();

	public abstract int getBaseStamina();

	public abstract int getWildStaminaIncrease();

	public abstract int getBaseTorpor();

	public abstract int getWildTorporIncrease();
}