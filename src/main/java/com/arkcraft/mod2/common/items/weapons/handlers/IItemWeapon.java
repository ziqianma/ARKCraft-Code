package com.arkcraft.mod2.common.items.weapons.handlers;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.arkcraft.mod2.common.items.weapons.component.RangedComponent;

public interface IItemWeapon
{
	public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity);
	
	public Random getItemRand();
	
	public RangedComponent getRangedComponent();
	
	public boolean ifCanScope();
	
	public boolean canOpenGui();
}