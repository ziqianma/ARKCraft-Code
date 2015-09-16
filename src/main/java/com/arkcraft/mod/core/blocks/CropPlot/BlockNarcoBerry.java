package com.arkcraft.mod.core.blocks.CropPlot;

import com.arkcraft.mod.core.GlobalAdditions;

import net.minecraft.item.Item;

public class BlockNarcoBerry extends CropPlotCrops
{
    protected Item getSeed()
    {
        return GlobalAdditions.narcoBerrySeed;
    }

    protected Item getCrop()
    {
    	  return GlobalAdditions.narcoBerry;
    }
}