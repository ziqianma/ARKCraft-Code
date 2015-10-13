package com.arkcraft.mod.common.items.weapons.handlers;

import java.util.Random;
import java.util.UUID;

import com.arkcraft.mod.common.items.weapons.component.RangedComponent;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IItemWeapon
{
	public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity);
	
	public UUID getUUID();
	
	public Random getItemRand();
	
	public RangedComponent getRangedComponent();
}