package com.arkcraft.mod.common.creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.arkcraft.mod.common.items.ARKCraftItems;

public class ARKTabs extends CreativeTabs {

	public ARKTabs(int i, String string) {
		super(i, string);
	}

	@Override
	public Item getTabIconItem() {
		return ARKCraftItems.narcotics;
	}

}
