package com.arkcraft.module.creature.common.entity.aggressive;

import java.util.Random;

/**
 * @author wildbill22
 * @author Lewis_McReu
 */
public enum RaptorType
{
	ALBINO(0),
	BLACK_GREY(1),
	BLACK_WHITE(2),
	BROWN_WHITE(3),
	GREEN_ORANGE(4),
	GREEN_RED_WHITE(5),
	GREEN_RED(6),
	GREY_BLUE(7),
	GREY_YELLOW(8),
	ORANGE(9),
	STRIPED(10);

	private int type;

	RaptorType(int id)
	{
		type = id;
	}

	public int getRaptorTypeInt()
	{
		return type;
	}

	public static RaptorType getRandomRaptorType()
	{
		return RaptorType.values()[new Random().nextInt(RaptorType.values().length)];
	}

	public String getReadableName()
	{
		// TODO switch case or some string fiddling
		return toString();
	}

	public String getResourceName()
	{
		return "raptor_" + toString() + ".png";
	}

	public String toString()
	{
		return this.name().toLowerCase();
	}
}