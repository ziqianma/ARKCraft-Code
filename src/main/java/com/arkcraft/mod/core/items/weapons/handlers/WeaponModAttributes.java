package com.arkcraft.mod.core.items.weapons.handlers;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class WeaponModAttributes extends SharedMonsterAttributes
{
	public static final BaseAttribute	RELOAD_TIME	= (new RangedAttribute((BaseAttribute)null,"Main.reloadTime", 0D, 0D, Double.MAX_VALUE));
	
}