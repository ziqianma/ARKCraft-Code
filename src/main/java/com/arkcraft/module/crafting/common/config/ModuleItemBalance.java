package com.arkcraft.module.crafting.common.config;

import com.arkcraft.lib.DefaultBoolean;
import com.arkcraft.lib.DefaultDouble;
import com.arkcraft.lib.DefaultInt;

/**
 * @author wildbill22
 */

/**
 * All constants which could be used to balance the mod should be stored here
 * Add more classes inside the BALANCE class as needed
 *
 */
public final class ModuleItemBalance
{

	public static class PLANTS
	{
		@DefaultInt(value = 1, minValue = 0, maxValue = 5, name = "Min berries per pick", comment = "Minimum number of berries you get when picking")
		public static int BERRIES_MIN_PER_PICKING;
		@DefaultInt(value = 5, minValue = 1, maxValue = 10, name = "Max Berries per pick", comment = "Maximum number of berries you get when picking")
		public static int BERRIES_MAX_PER_PICKING;
	}

	public static class PLAYER
	{
		// minValue 1min, maxValue, 5mins, default value 2:30mins
		@DefaultInt(value = 1500, minValue = 600, maxValue = 3000, name = "Ticks between player poops.", comment = "Ticks between the player poops inside the game")
		public static int TICKS_BETWEEN_PLAYER_POOP;
	}

	public static class WEAPONS
	{
		@DefaultBoolean(value = true, name = "Include Spear", comment = "Include Spear")
		public static boolean SPEAR;
		@DefaultDouble(value = 10D, minValue = 8.0D, maxValue = 20.0D, name = "Spear Damage", comment = "Spear Damage")
		public static double SPEAR_DAMAGE;
		@DefaultDouble(value = 0.2D, minValue = 0.0D, maxValue = 10.0D, name = "Tranq Ammo Damage", comment = "Tranq Ammo Damage")
		public static double TRANQ_AMMO_DAMAGE;
		@DefaultInt(value = 50, minValue = 5, maxValue = 1000, name = "Torpor time for tranq gun ammo.", comment = "Adds seconds of torpor to dino when hit")
		public static int TRANQ_AMMO_TORPOR_TIME;
		@DefaultDouble(value = 0.2D, minValue = 0.0D, maxValue = 10.0D, name = "Tranq Arrow Damage", comment = "Tranq Arrow Damage")
		public static double TRANQ_ARROW_DAMAGE;
		@DefaultInt(value = 100, minValue = 5, maxValue = 1000, name = "Torpor time for tranq arrow.", comment = "Adds seconds of torpor to dino when hit")
		public static int TRANQ_ARROW_TORPOR_TIME;

		@DefaultBoolean(value = true, name = "Include Simple Pistol", comment = "Include Simple Pistol")
		public static boolean SIMPLE_PISTOL;
		@DefaultDouble(value = 2.5, minValue = 0.2, maxValue = 2.5, name = "Time to reload pistol.", comment = "Seconds to reload")
		public static double SIMPLE_PISTOL_RELOAD;

		@DefaultBoolean(value = true, name = "Include Longneck Rifle", comment = "Include  Longneck Rifle")
		public static boolean LONGNECK_RIFLE;
		@DefaultDouble(value = 4.0, minValue = 0.5, maxValue = 5.0, name = "Time to reload longneck rifle.", comment = "Seconds to reload")
		public static double LONGNECK_RIFLE_RELOAD;

		@DefaultBoolean(value = true, name = "Include Shotgun", comment = "Include Shotgun")
		public static boolean SHOTGUN;
		@DefaultDouble(value = 3, minValue = 0.2, maxValue = 5.0, name = "Time to reload shotgun.", comment = "Seconds to reload")
		public static double SHOTGUN_RELOAD;

		@DefaultBoolean(value = true, name = "Include Fabricated Pistol", comment = "Include Fabricated Pistol")
		public static boolean FABRICATED_PISTOL;
		@DefaultDouble(value = 3.0, minValue = 0.2, maxValue = 5.0, name = "Time to reload fabricated_pistol.", comment = "Seconds to reload")
		public static double FABRICATED_PISTOL_RELOAD;

		@DefaultBoolean(value = true, name = "Include Rocket Launcher", comment = "Include Rocket Launcher")
		public static boolean ROCKET_LAUNCHER;
		@DefaultDouble(value = 2.5, minValue = 1.0, maxValue = 5.0, name = "Time to reload rocket_laucher.", comment = "Seconds to reload")
		public static double ROCKET_LAUNCHER_RELOAD;

		@DefaultBoolean(value = true, name = "Include Crossbow", comment = "Include Crossbow")
		public static boolean CROSSBOW;
		@DefaultDouble(value = 1.0, minValue = 0.2, maxValue = 5.0, name = "Time to reload crossbow.", comment = "Seconds to reload")
		public static double CROSSBOW_RELOAD;
	}

	public static class CROP_PLOT
	{
		// minValue 60 seconds, maxValue 1 hour, default value 10 mins
		@DefaultInt(value = 600, minValue = 60, maxValue = 3600, name = "Seconds until fruitling.", comment = "Time from seeded to fruitling in crop plot")
		public static int SECONDS_UNTIL_FRUITLING;
		@DefaultInt(value = 300, minValue = 60, maxValue = 3600, name = "Seconds to decompose seed.", comment = "Time for seed to decompose in crop plot")
		public static int SECONDS_FOR_SEED_TO_DECOMPOSE;
		@DefaultInt(value = 600, minValue = 60, maxValue = 3600, name = "Seconds to decompose player feces.", comment = "Time for feces to decompose in crop plot")
		public static int SECONDS_FOR_PLAYER_FECES_TO_DECOMPOSE;
		@DefaultInt(value = 600, minValue = 60, maxValue = 3600, name = "Seconds to decompose small feces.", comment = "Time for feces to decompose in crop plot")
		public static int SECONDS_FOR_SMALL_FECES_TO_DECOMPOSE;
		@DefaultInt(value = 4800, minValue = 60, maxValue = 28800, name = "Seconds to decompose fertilizer.", comment = "Time for fertilizer to decompose in crop plot")
		public static int SECONDS_FOR_FERTILIZER_TO_DECOMPOSE;
		@DefaultInt(value = 1080, minValue = 60, maxValue = 3600, name = "Seconds of watering.", comment = "Time for one bucket to provide water in crop plot")
		public static int SECONDS_OF_WATER_PER_BUCKET;
		@DefaultInt(value = 45, minValue = 10, maxValue = 600, name = "Seconds to grow a berry.", comment = "Time for one berry to grow in crop plot")
		public static int GROW_TIME_FOR_BERRY;
	}

	public static class MORTAR_AND_PESTLE
	{
		@DefaultInt(value = 16, minValue = 1, maxValue = 60, name = "Seconds to craft an item.", comment = "Time to craft item in mortar and pestle")
		public static int CRAFT_TIME_FOR_ITEM;
	}

	public static class PLAYER_CRAFTING
	{
		@DefaultInt(value = 8, minValue = 1, maxValue = 60, name = "Seconds to craft an item.", comment = "Time to craft item in player built-in crafting")
		public static int CRAFT_TIME_FOR_ITEM;
	}

	public static class COMPOST_BIN
	{
		@DefaultInt(value = 30, minValue = 15, maxValue = 600, name = "Thatch composting time.", comment = "Seconds of composting provided")
		public static int COMPOST_TIME_FOR_THATCH;
		@DefaultInt(value = 60, minValue = 15, maxValue = 600, name = "Feces composting time.", comment = "Seconds to compost feces into fertilizer")
		public static int COMPOST_TIME_FOR_FECES;
	}

	public static class REFINING_FORGE
	{
		@DefaultInt(value = 30, minValue = 15, maxValue = 600, name = "Metal smelting time.", comment = "Seconds of smelting")
		public static int METAL_SMELTING_TIME;
		@DefaultInt(value = 60, minValue = 15, maxValue = 600, name = "Feces composting time.", comment = "Seconds to compost feces into fertilizer")
		public static int COMPOST_TIME_FOR_FECES;
	}

	public static class EXPLOSIVE_DEVICE
	{
		@DefaultInt(value = 5, minValue = 0, maxValue = 10, name = "Wire range.", comment = "Wire range")
		public static int WIRE_RANGE;
	}
}
