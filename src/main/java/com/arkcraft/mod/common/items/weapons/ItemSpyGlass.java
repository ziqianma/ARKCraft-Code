package com.arkcraft.mod.common.items.weapons;

import com.arkcraft.mod.common.items.weapons.component.ItemShooter;
import com.arkcraft.mod.common.items.weapons.component.RangedComponent;

public class ItemSpyGlass extends ItemShooter{
	public ItemSpyGlass(RangedComponent rangedcomponent)
	{
		super(rangedcomponent);
		this.setMaxStackSize(1);
    }
}
