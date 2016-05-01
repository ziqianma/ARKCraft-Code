package com.arkcraft.module.creature.common.entity.creature;

import com.arkcraft.module.creature.common.entity.EntityARKCreature;
import com.arkcraft.module.creature.common.entity.SaddleType;
import com.arkcraft.module.creature.common.entity.test.EntityRaptor;

public class CreatureRaptor extends Creature
{
	// TODO all raptor stats taken from ARK - subject to change

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return "arkcraft.raptor";
	}

	@Override
	public Class<? extends EntityARKCreature> getEntityClass()
	{
		// TODO Auto-generated method stub
		return EntityRaptor.class;
	}

	@Override
	public TamingType getTameType()
	{
		// TODO Auto-generated method stub
		return TamingType.KNOCK_OUT;
	}

	@Override
	public SaddleType getSaddleType()
	{
		// TODO Auto-generated method stub
		return SaddleType.SMALL;
	}

	@Override
	public int getGrowthTime()
	{
		// TODO Auto-generated method stub
		return 12000;
	}

	@Override
	public int getBabySizeXZ()
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getBabySizeY()
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getAdultSizeXZ()
	{
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getAdultSizeY()
	{
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getTorporLossSpeed()
	{
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int getBaseKillXP()
	{
		// TODO Auto-generated method stub
		return 50;
	}

	@Override
	public int getBaseHealth()
	{
		// TODO Auto-generated method stub
		return 200;
	}

	@Override
	public int getBaseWeight()
	{
		// TODO Auto-generated method stub
		return 1400;
	}

	@Override
	public int getBaseOxygen()
	{
		// TODO Auto-generated method stub
		return 150;
	}

	@Override
	public int getBaseFood()
	{
		// TODO Auto-generated method stub
		return 1200;
	}

	@Override
	public int getBaseDamage()
	{
		// TODO Auto-generated method stub
		return 15;
	}

	@Override
	public int getBaseSpeed()
	{
		// TODO Auto-generated method stub
		return 480;
	}

	@Override
	public int getBaseStamina()
	{
		// TODO Auto-generated method stub
		return 150;
	}

	@Override
	public int getBaseTorpor()
	{
		// TODO Auto-generated method stub
		return 180;
	}

	@Override
	public double getWildHealthIncrease()
	{
		// TODO Auto-generated method stub
		return 40;
	}

	@Override
	public double getWildWeightIncrease()
	{
		// TODO Auto-generated method stub
		return 2.8;
	}

	@Override
	public double getWildOxygenIncrease()
	{
		// TODO Auto-generated method stub
		return 15;
	}

	@Override
	public double getWildFoodIncrease()
	{
		// TODO Auto-generated method stub
		return 120;
	}

	@Override
	public double getWildDamageIncrease()
	{
		// TODO Auto-generated method stub
		return 0.75;
	}

	@Override
	public double getWildStaminaIncrease()
	{
		// TODO Auto-generated method stub
		return 15;
	}

	@Override
	public double getWildTorporIncrease()
	{
		// TODO Auto-generated method stub
		return 10.8;
	}

	@Override
	public double getTamedHealthIncrease()
	{
		// TODO Auto-generated method stub
		return 0.0621;
	}

	@Override
	public double getTamedWeightIncrease()
	{
		// TODO Auto-generated method stub
		return 0.04;
	}

	@Override
	public double getTamedOxygenIncrease()
	{
		// TODO Auto-generated method stub
		return 0.1;
	}

	@Override
	public double getTamedFoodIncrease()
	{
		// TODO Auto-generated method stub
		return 0.1;
	}

	@Override
	public double getTamedDamageIncrease()
	{
		// TODO Auto-generated method stub
		return 0.02;
	}

	@Override
	public double getTamedSpeedIncrease()
	{
		// TODO Auto-generated method stub
		return 0.03;
	}

	@Override
	public double getTamedStaminaIncrease()
	{
		// TODO Auto-generated method stub
		return 0.1;
	}

	@Override
	public double getTamedTorporIncrease()
	{
		// TODO Auto-generated method stub
		return 0.05;
	}
}
