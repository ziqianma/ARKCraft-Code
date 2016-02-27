package com.arkcraft.module.items.common.general.potions;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class ARKCraftPotionEffects
{

	public static Potion Torpor;
	public static int TorporID = 30;

	public static void init()
	{
		Torpor = (new TorporEffect(TorporID, new ResourceLocation("torpor"), true, 0))
				.setIconIndex(0, 0).setPotionName("Topor");
		// Breathe = (new PotionEffectDarkness(BreatheID, true,
		// 0)).setIconIndex(0, 0).setPotionName("");
	}
}