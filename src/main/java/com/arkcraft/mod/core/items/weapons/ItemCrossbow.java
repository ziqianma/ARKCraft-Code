package com.arkcraft.mod.core.items.weapons;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.items.weapons.handlers.ItemShooter;
import com.arkcraft.mod.core.items.weapons.handlers.RangedCompCrossbow;

public class ItemCrossbow extends ItemShooter{

	public ItemCrossbow(String name) {
		super(name, new RangedCompCrossbow());
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
	}
}
	
