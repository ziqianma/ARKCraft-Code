package com.arkcraft.mod.core.items.weapons.handlers;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class WeaponModAttributes extends SharedMonsterAttributes
{
	public static final BaseAttribute	IGNORE_ARMOUR_DAMAGE	= (new RangedAttribute((BaseAttribute)null, "weaponmod.ignoreArmour", 0D, 0D, Double.MAX_VALUE));
	public static final BaseAttribute	WEAPON_KNOCKBACK		= (new RangedAttribute((BaseAttribute)null,"weaponmod.knockback", 0.4D, 0D, Double.MAX_VALUE));
	public static final BaseAttribute	ATTACK_SPEED			= (new RangedAttribute((BaseAttribute)null,"weaponmod.attackSpeed", 0D, -Double.MAX_VALUE, Double.MAX_VALUE));
	public static final BaseAttribute	RELOAD_TIME				= (new RangedAttribute((BaseAttribute)null,"weaponmod.reloadTime", 0D, 0D, Double.MAX_VALUE));
	public static final BaseAttribute	WEAPON_REACH			= (new RangedAttribute((BaseAttribute)null,"weaponmod.reach", 0D, 0D, Double.MAX_VALUE));
	
}