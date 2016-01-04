package com.arkcraft.module.core.common.entity;

import com.arkcraft.module.item.common.items.ARKCraftItems;
import net.minecraft.item.Item;

public enum SaddleType
{
    NONE(0),
    SMALL(1),
    MEDIUM(2),
    LARGE(3);

    private int id;
    public static final int numSaddles = 3;

    SaddleType(int id)
    {
        this.id = id;
    }

    public int getSaddleID()
    {
        return id;
    }

    // TODO: For now, there is just one size of dino inventory
    public int getInventorySize()
    {
        switch (id)
        {
            case 1:
                return 25;
            case 2:
                return 25;
            case 3:
                return 25;
            default:
                return 0;
        }
    }

    public Item getSaddleItem()
    {
        switch (id)
        {
            case 1:
                return ARKCraftItems.saddle_small;
            case 2:
                return ARKCraftItems.saddle_medium;
            case 3:
                return ARKCraftItems.saddle_large;
            default:
                return null;
        }
    }

    public String toString()
    {
        switch (id)
        {
            case 1:
                return "small";
            case 2:
                return "medium";
            case 3:
                return "large";
            default:
                return "none";
        }
    }
}