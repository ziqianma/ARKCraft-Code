package com.arkcraft.module.item.common.items;

import com.arkcraft.module.item.common.items.weapons.handlers.IItemWeapon;
import com.arkcraft.module.item.common.items.weapons.component.RangedComponent;
import net.minecraft.item.Item;

import java.util.Random;

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
    public Random getItemRand()
    {
        return null;
    }

    @Override
    public RangedComponent getRangedComponent()
    {
        return null;
    }

    @Override
    public boolean canOpenGui()
    {

        return false;
    }
}
