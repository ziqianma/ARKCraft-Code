package com.arkcraft.mod.core.items.weapons.bullets;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;

public class ItemSimpleBullet extends Item {
		
	public ItemSimpleBullet(String name) {
		super();
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
	}	
}
