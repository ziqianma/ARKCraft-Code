package com.arkcraft.module.item.common.guns;

import java.util.Random;

import net.minecraft.item.ItemStack;

public interface IItemWeapon
{
	public Random getItemRand();

	public boolean canScope(ItemStack stack);

	// public boolean attachment();

}