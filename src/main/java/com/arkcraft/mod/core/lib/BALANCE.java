package com.arkcraft.mod.core.lib;

/***
 * 
 * @author wildbill22
 *
 */
/**
 * All constants which could be used to balance the mod should be stored here
 * Add more classes inside the BALANCE class as needed
 *
 */
public final class BALANCE {

	public static class PLANTS {
		@DefaultInt(value = 1, minValue = 0, maxValue = 5, name = "Min berries per pick", comment = "Minimum number of berries you get when picking")
		public static int BERRIES_MIN_PER_PICKING;
		@DefaultInt(value = 5, minValue = 1, maxValue = 10, name = "Max Berries per pick", comment = "Maximum number of berries you get when picking")
		public static int BERRIES_MAX_PER_PICKING;

	}
	
	public static class GEN {
		@DefaultInt(value = 5, minValue = 1, maxValue = 10, name = "Veins that can spawn per chunk", comment = "Default max veins that can spawn per chunk")
		public static int MAX_DEFAULT_ORE_VEIN_SPAWN_PER_CHUNK;
		@DefaultInt(value = 7, minValue = 2, maxValue = 10, name = "Ore blocks that spawn per vein", comment = "Default max ore blocks that can spawn per vein loaded")
		public static int MAX_DEFAULT_ORE_BLOCKS_SPAWN_PER_VEIN;
	}
	
	public static class PLAYER {
		//minValue 1min, maxValue, 5mins, default value 2:30mins
		@DefaultInt(value = 1500, minValue = 600, maxValue = 3000, name = "Ticks between player poops.", comment = "Ticks between the player poops inside the game")
		public static int TICKS_BETWEEN_PLAYER_POOP;
	}
		
	public static class WEAPONS {
		@DefaultBoolean(value=true, name = "Include Spear", comment = "Include Spear")
		public static boolean SPEAR;
		@DefaultDouble(value = 10D, minValue = 8.0D, maxValue = 20.0D, name = "Spear Damage", 
				comment = "Spear Damage")
		public static double SPEAR_DAMAGE;
		@DefaultDouble(value = 3.5D, minValue = 1.0D, maxValue = 10.0D, name = "Tranq Ammo Damage", 
				comment = "Tranq Ammo Damage")
		public static double TRANQ_ARROW_DAMAGE;
		
		@DefaultBoolean(value=true, name = "Include Simple Pistol", comment = "Include Simple Pistol")
		public static boolean SIMPLE_PISTOL;
		@DefaultInt(value = 5, minValue = 1, maxValue = 10, name = "Time to reload pistol.", comment = "Seconds to reload")
		public static int SIMPLE_PISTOL_RELOAD;
		
		@DefaultBoolean(value=true, name = "Include Longneck Rifle", comment = "Include  Longneck Rifle")
		public static boolean LONGNECK_RIFLE;
		@DefaultInt(value = 25, minValue = 5, maxValue = 50, name = "Time to reload longneck rifle.", comment = "Seconds to reload")
		public static int LONGNECK_RIFLE_RELOAD;
		
		@DefaultBoolean(value=true, name = "Include Shotgun", comment = "Include Shotgun")
		public static boolean SHOTGUN;
		@DefaultInt(value = 15, minValue = 5, maxValue = 30, name = "Time to reload shotgun.", comment = "Seconds to reload")
		public static int SHOTGUN_RELOAD;	
		
		@DefaultBoolean(value=true, name = "Include Tranq Gun", comment = "Include Tranq Gun")
		public static boolean TRANQ_GUN;
		@DefaultInt(value = 25, minValue = 5, maxValue = 50, name = "Time to reload tranq_gun.", comment = "Seconds to reload")
		public static int TRANQ_GUN_RELOAD;
		
		@DefaultBoolean(value=true, name = "Include Rocket Launcher", comment = "Include Rocket Launcher")
		public static boolean ROCKET_LAUNCHER;
		@DefaultInt(value = 50, minValue = 40, maxValue = 75, name = "Time to reload rocket_laucher.", comment = "Seconds to reload")
		public static int ROCKET_LAUNCHER_RELOAD;
		
		@DefaultBoolean(value=true, name = "Include Crossbow", comment = "Include Crossbow")
		public static boolean CROSSBOW;
		@DefaultInt(value = 20, minValue = 5, maxValue = 50, name = "Time to reload crossbow.", comment = "Seconds to reload")
		public static int CROSSBOW_RELOAD;
	}
}
