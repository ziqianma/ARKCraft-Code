package com.arkcraft.module.core.common.creativetabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import com.arkcraft.module.blocks.common.items.ARKCraftItems;

public class ARKTabs extends CreativeTabs
{

	public ARKTabs(int i, String string)
	{
		super(i, string);
	}

	@Override
	public Item getTabIconItem()
	{
		return ARKCraftItems.dino_book;
	}

}
