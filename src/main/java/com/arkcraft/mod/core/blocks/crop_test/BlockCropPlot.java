package com.arkcraft.mod.core.blocks.crop_test;

import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;

import com.arkcraft.mod.core.GlobalAdditions;

public class BlockCropPlot extends BlockCrops
{

    protected Item getSeed()
    {
        return GlobalAdditions.narcoBerrySeed;
    }

}