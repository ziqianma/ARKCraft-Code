package com.arkcraft.mod.core.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;

/***
 * 
 * @author wildbill22
 *
 */
public class ARKSeedItem extends Item {
	
	public ARKSeedItem(String name) {
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setMaxStackSize(16);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
	}
	
	public static ItemStack getBerryForSeed(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKSeedItem) {
				if (stack.getItem() == (Item) GlobalAdditions.amarBerrySeed)
					return new ItemStack(GlobalAdditions.amarBerry);
				if (stack.getItem() == (Item) GlobalAdditions.azulBerrySeed)
					return new ItemStack(GlobalAdditions.azulBerry);
				if (stack.getItem() == (Item) GlobalAdditions.mejoBerrySeed)
					return new ItemStack(GlobalAdditions.mejoBerry);
				if (stack.getItem() == (Item) GlobalAdditions.narcoBerrySeed)
					return new ItemStack(GlobalAdditions.narcoBerry);
				if (stack.getItem() == (Item) GlobalAdditions.tintoBerrySeed)
					return new ItemStack(GlobalAdditions.tintoBerry);
			}
		}
		return null;
	}
}
