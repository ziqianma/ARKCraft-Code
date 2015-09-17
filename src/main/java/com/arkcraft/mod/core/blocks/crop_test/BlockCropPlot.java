package com.arkcraft.mod.core.blocks.crop_test;

import com.arkcraft.mod.core.GlobalAdditions;

import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;

public class BlockCropPlot extends BlockCrops
{
    protected Item getSeed()
    {
        return GlobalAdditions.narcoBerrySeed;
    }

}