package com.arkcraft.mod.core.items.weapons;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.items.weapons.component.ItemShooter;
import com.arkcraft.mod.core.items.weapons.component.RangedComponent;

public class ItemCrossbow extends ItemShooter{

	public ItemCrossbow(String name, RangedComponent rangedcomponent)
	{
		super(name, rangedcomponent);
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
	}
}
	
