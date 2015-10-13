package com.arkcraft.mod.common.items.weapons.bullets;

import com.arkcraft.mod.GlobalAdditions;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectile extends Item {
	
	public ItemProjectile(String name) {
		super();
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
	}	
}
