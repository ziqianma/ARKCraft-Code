package com.arkcraft.mod.common.config;

import com.arkcraft.lib.DefaultBoolean;
import com.arkcraft.lib.DefaultInt;

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
public final class MOD1_BALANCE {

	public static class GEN {
		@DefaultInt(value = 5, minValue = 1, maxValue = 10, name = "Veins that can spawn per chunk", comment = "Default max veins that can spawn per chunk")
		public static int MAX_DEFAULT_ORE_VEIN_SPAWN_PER_CHUNK;
		@DefaultInt(value = 7, minValue = 2, maxValue = 10, name = "Ore blocks that spawn per vein", comment = "Default max ore blocks that can spawn per vein loaded")
		public static int MAX_DEFAULT_ORE_BLOCKS_SPAWN_PER_VEIN;
		@DefaultBoolean(value=true, name = "Include Minecraft hostile mobs", comment = "No skeleton, zombie, or creeper")
		public static boolean mcHostileMobs;
	}
	
	public static class DINO_PROPERTIES {
		@DefaultInt(value = 120, minValue = 30, maxValue = 3600, name = "Seconds to tame raptor.", comment = "Time need to tame")
		public static int SECONDS_TO_TAME_RAPTOR;		
		@DefaultInt(value = 15, minValue = 0, maxValue = 100, name = "Dino spawn probability", comment = "Percentage chance of spawning")
		public static int SPAWN_PROBABILITY;		
	}
}
