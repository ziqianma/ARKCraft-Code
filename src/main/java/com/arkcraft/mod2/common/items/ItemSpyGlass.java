package com.arkcraft.mod2.common.items;

import java.util.Random;

import com.arkcraft.mod2.common.items.weapons.component.RangedComponent;
import com.arkcraft.mod2.common.items.weapons.handlers.IItemWeapon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemSpyGlass extends Item implements IItemWeapon
{
	public ItemSpyGlass()
	{
		super();
		this.setMaxStackSize(1);
    }
	
	@Override
	public boolean ifCanScope() 
	{
		return true;
	}

	@Override
	public Random getItemRand() {
		return null;
	}

	@Override
	public RangedComponent getRangedComponent() {
		return null;
	}

	@Override
	public boolean canOpenGui() {

		return false;
	}
}
