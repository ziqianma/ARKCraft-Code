package net.minecraft.enchantment;

import net.minecraft.util.WeightedRandom;

public class EnchantmentData extends WeightedRandom.Item
{
    /** Enchantment object associated with this EnchantmentData */
    public final Enchantment enchantmentobj;
    /** Enchantment level associated with this EnchantmentData */
    public final int enchantmentLevel;
    private static final String __OBFID = "CL_00000115";

    public EnchantmentData(Enchantment enchantmentObj, int enchLevel)
    {
        super(enchantmentObj.getWeight());
        this.enchantmentobj = enchantmentObj;
        this.enchantmentLevel = enchLevel;
    }
}