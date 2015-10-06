package com.arkcraft.mod.core.creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.arkcraft.mod.core.items.ModItems;

public class ARKTabs extends CreativeTabs {

	public ARKTabs(int i, String string) {
		super(i, string);
	}

	@Override
	public Item getTabIconItem() {
		return ModItems.narcotics;
	}

}
