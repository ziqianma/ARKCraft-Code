package com.arkcraft.module.item.common.items.itemblock;

import net.minecraft.block.Block;

import com.arkcraft.module.core.GlobalAdditions;

public class ItemRefiningForge extends ItemBlockARK
{
	public ItemRefiningForge(Block block)
	{
		super(block);
		setCreativeTab(GlobalAdditions.tabARK);
		this.setMaxStackSize(1);
	}
}
