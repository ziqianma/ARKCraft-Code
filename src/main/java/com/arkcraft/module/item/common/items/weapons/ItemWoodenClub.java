package com.arkcraft.module.item.common.items.weapons;

import net.minecraft.item.ItemSword;

public class ItemWoodenClub extends ItemSword
{

    public ItemWoodenClub(ToolMaterial m)
    {
        super(m);
        this.setMaxStackSize(1);
        this.setFull3D();
    }
}
