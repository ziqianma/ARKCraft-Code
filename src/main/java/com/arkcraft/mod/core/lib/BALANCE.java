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
		@DefaultInt(value = 1, minValue = 0, maxValue = 5, name = "Minimum berries per pick", comment = "Minimum number of berries you get when picking")
		public static int BERRIES_MIN_PER_PICKING;
		@DefaultInt(value = 5, minValue = 1, maxValue = 10, name = "Maximum berries per pick", comment = "Maximum number of berries you get when picking")
		public static int BERRIES_MAX_PER_PICKING;

	}
	
	public static class GEN {
		@DefaultInt(value = 5, minValue = 1, maxValue = 10, name = "Default max veins that can spawn per chunk", comment = "Default max veins that can spawn per chunk")
		public static int MAX_DEFAULT_ORE_VEIN_SPAWN_PER_CHUNK;
		@DefaultInt(value = 7, minValue = 2, maxValue = 10, name = "Default max ore blocks that can spawn per vein loaded", comment = "Default max ore blocks that can spawn per vein loaded")
		public static int MAX_DEFAULT_ORE_BLOCKS_SPAWN_PER_VEIN;
	}
	
	public static class PLAYER {
		//minValue 1min, maxValue, 5mins, default value 2:30mins
		@DefaultInt(value = 1500, minValue = 600, maxValue = 3000, name = "Default max ticks between player poops.", comment = "Ticks between the player poops inside the game")
		public static int TICKS_BETWEEN_PLAYER_POOP;
	}
}
