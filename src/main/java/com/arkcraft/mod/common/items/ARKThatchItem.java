package com.arkcraft.mod.common.items;

import com.arkcraft.mod.GlobalAdditions;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

/***
 * 
 * @author wildbill22
 * Created so that it would use the default tooltip for it to work properly with the Compost Bin
 *
 */
public class ARKThatchItem extends Item{
	
	public ARKThatchItem(String name) {
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setMaxStackSize(64);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
	}
}
