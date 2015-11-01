package com.arkcraft.mod.common.items.weapons.handlers;

import com.arkcraft.mod.common.items.weapons.component.RangedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.Random;

public interface IItemWeapon
{
	public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity);
	
	public Random getItemRand();
	
	public RangedComponent getRangedComponent();
	
	public boolean getReach();
}