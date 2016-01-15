package com.arkcraft.module.item.common.items.weapons.handlers;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;

public class WeaponModAttributes extends SharedMonsterAttributes
{
	// TODO check necessity
	public static final BaseAttribute RELOAD_TIME = (new RangedAttribute((BaseAttribute) null,
			"arkcraft.reloadTime", 0D, 0D, Double.MAX_VALUE));
	public static final BaseAttribute CONSUME_ITEM = (new RangedAttribute((BaseAttribute) null,
			"arkcraft.consumeItem", 0D, 0D, Double.MAX_VALUE));

}