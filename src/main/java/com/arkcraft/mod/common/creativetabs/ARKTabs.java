package com.arkcraft.mod.common.creativetabs;

import com.arkcraft.mod2.common.items.ARKCraftItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ARKTabs extends CreativeTabs {

	public ARKTabs(int i, String string) {
		super(i, string);
	}

	@Override
	public Item getTabIconItem() {
		return ARKCraftItems.narcotics;
	}

}
