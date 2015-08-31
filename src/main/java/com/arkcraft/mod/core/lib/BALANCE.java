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

		@DefaultInt(value = 5, minValue = 1, maxValue = 10, name = "Maximum Surface Crystals that can spawn", comment = "How many Veins can spawn")
		public static int MAXIMUM_SURFACE_CYRSTAL_VEIN_SPAWN;
	}
}
