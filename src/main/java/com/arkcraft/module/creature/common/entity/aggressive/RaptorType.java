package com.arkcraft.module.creature.common.entity.aggressive;

import java.util.Random;

/**
 * @author wildbill22
 */
public enum RaptorType
{
	ALBINO(0),
	BREEN_WHITE(1),
	CYAN_LGREEN(2),
	RAINBOW(3),
	GREEN_GREY(4),
	GREEN_TAN(5),
	GREEN_WHITE(6),
	GREY_GREY(7),
	LBROWN_TAN(8),
	RED_TAN(9),
	TAN_WHITE(10);

	private int type;
	public static final int numRaptors = 11;

	RaptorType(int id)
	{
		this.type = id;
	}

	public int getRaptorTypeInt()
	{
		return type;
	}

	public static int getRandomRaptorType()
	{
		return new Random().nextInt(RaptorType.numRaptors);
	}

	public String getReadableName()
	{
		switch (type)
		{
			case 1:
				return "Breen White";
			case 2:
				return "Cyan Light Green";
			case 3:
				return "Rainbow";
			case 4:
				return "Green Grey";
			case 5:
				return "Green Tan";
			case 6:
				return "Green White";
			case 7:
				return "Grey Grey";
			case 8:
				return "Light Brown Tan";
			case 9:
				return "Red Tan";
			case 10:
				return "Tan White";
			case 0:
			default:
				return "Albino";
		}
	}

	public String toString()
	{
		return super.toString().toLowerCase();
	}
}