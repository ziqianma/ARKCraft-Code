package com.arkcraft.mod.common.items.weapons;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.items.weapons.component.ItemShooter;
import com.arkcraft.mod.common.items.weapons.component.RangedComponent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemLongneckRifle extends ItemShooter
{

	public ItemLongneckRifle(String name, RangedComponent rangedcomponent)
	{
		super(name, rangedcomponent);
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
	}
}
	
