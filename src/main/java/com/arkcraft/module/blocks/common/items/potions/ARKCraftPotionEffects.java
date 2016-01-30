package com.arkcraft.module.blocks.common.items.potions;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class ARKCraftPotionEffects
{

    public static Potion Topor;
    public static int ToporID = 30;

    public static void init()
    {
        Topor = (new ToporEffect(ToporID, new ResourceLocation("topor"), true, 0)).setIconIndex(0, 0).setPotionName("Topor");
        //	Breathe = (new PotionEffectDarkness(BreatheID, true, 0)).setIconIndex(0, 0).setPotionName("");

    }
}