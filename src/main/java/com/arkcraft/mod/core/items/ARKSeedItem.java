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
			if (stack.getItem() instanceof ARKFood) {
				if (stack.getItem() == (Item) GlobalAdditions.amarBerry)
					return new ItemStack(GlobalAdditions.amarBerrySeed);
				if (stack.getItem() == (Item) GlobalAdditions.azulBerry)
					return new ItemStack(GlobalAdditions.azulBerrySeed);
				if (stack.getItem() == (Item) GlobalAdditions.mejoBerry)
					return new ItemStack(GlobalAdditions.mejoBerrySeed);
				if (stack.getItem() == (Item) GlobalAdditions.narcoBerry)
					return new ItemStack(GlobalAdditions.narcoBerrySeed);
				if (stack.getItem() == (Item) GlobalAdditions.tintoBerry)
					return new ItemStack(GlobalAdditions.tintoBerrySeed);
			}
		}
		return null;
	}
}
