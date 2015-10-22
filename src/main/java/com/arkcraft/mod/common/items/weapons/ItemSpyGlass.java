package com.arkcraft.mod.common.items.weapons;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.items.weapons.component.ItemShooter;
import com.arkcraft.mod.common.items.weapons.component.RangedComponent;


public class ItemSpyGlass extends ItemShooter{

	public ItemSpyGlass(String name, RangedComponent rangedcomponent)
	{
		super(name, rangedcomponent);
    	this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setMaxStackSize(1);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
    }

}
