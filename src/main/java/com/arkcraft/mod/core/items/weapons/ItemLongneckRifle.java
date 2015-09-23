package com.arkcraft.mod.core.items.weapons;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.items.weapons.component.ItemShooter;
import com.arkcraft.mod.core.items.weapons.component.RangedCompSimplePistol;

public class ItemLongneckRifle extends ItemShooter{

	public ItemLongneckRifle(String name) {
		super(name, new RangedCompSimplePistol());
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
	}
}
	
