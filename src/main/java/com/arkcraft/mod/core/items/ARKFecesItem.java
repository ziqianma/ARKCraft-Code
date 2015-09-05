package com.arkcraft.mod.core.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;

public class ARKFecesItem extends Item{
	
	public ARKFecesItem(String name) {
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setMaxStackSize(16);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
	
	}

	// ticks that this fertilizer will grow a crop
	public static int getItemGrowTime(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKFecesItem) {
				if (stack.getItem() == (Item) GlobalAdditions.dodo_feces)
					return 500;
				if (stack.getItem() == (Item) GlobalAdditions.player_feces)
					return 1000;
			}
		}
		return 0;
	}
}
