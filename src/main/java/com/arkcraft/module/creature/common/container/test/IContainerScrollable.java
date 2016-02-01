package com.arkcraft.module.creature.common.container.test;

public interface IContainerScrollable
{
	public int getScrollingOffset();

	public void scroll(int offset);

	public int getScrollableSlotsWidth();

	public int getScrollableSlotsHeight();

	public int getScrollableSlotsCount();

	public int getRequiredSlotsCount();
}
