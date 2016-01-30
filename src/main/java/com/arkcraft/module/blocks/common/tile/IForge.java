package com.arkcraft.module.blocks.common.tile;

import net.minecraft.inventory.IInventory;
import net.minecraft.server.gui.IUpdatePlayerListBox;

public interface IForge extends IInventory, IUpdatePlayerListBox
{
	public int getSlotCount();

	public boolean isBurning();

	public double getBurnFactor();
}
