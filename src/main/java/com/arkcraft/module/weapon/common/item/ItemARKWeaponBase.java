package com.arkcraft.module.weapon.common.item;

import net.minecraft.item.ItemSword;

/**
 * @author Vastatio
 */
public class ItemARKWeaponBase extends ItemSword
{
    public ItemARKWeaponBase(ToolMaterial m)
    {
        super(m);
        this.setFull3D();
    }
}
