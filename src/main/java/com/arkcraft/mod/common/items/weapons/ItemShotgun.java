package com.arkcraft.mod.common.items.weapons;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.handlers.IExtendedReach;
import com.arkcraft.mod.common.items.weapons.component.ItemShooter;
import com.arkcraft.mod.common.items.weapons.component.RangedComponent;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemShotgun extends ItemShooter implements IExtendedReach{

	public ItemShotgun(String name, RangedComponent rangedcomponent)
	{
		super(name, rangedcomponent);
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
	}
	
	@Override
	public boolean getReach() { return true; }

}
	
