package com.arkcraft.module.item.common.tile;

import net.minecraft.inventory.IInventory;
import net.minecraft.server.gui.IUpdatePlayerListBox;

public interface IForge extends IInventory, IUpdatePlayerListBox
{
	public int getSlotCount();

	public void setBurning(boolean burning);

	public boolean isBurning();

	public int getBurnFactor();
}
