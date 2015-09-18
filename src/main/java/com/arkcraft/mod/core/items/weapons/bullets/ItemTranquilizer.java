package com.arkcraft.mod.core.items.weapons.bullets;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;

public class ItemTranquilizer extends Item {
	
	public ItemTranquilizer(String name) {
		super();
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
	}	
}
