package com.arkcraft.mod.core.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;

public class ARKFecesItem extends Item{
	
	public ARKFecesItem(String name) {
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setMaxStackSize(16);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
	
	}

}
