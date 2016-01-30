package com.arkcraft.module.blocks.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @author Vastatio
 */
/* This is the core class for all generic blocks */
public class BlockARKBase extends Block
{

    public BlockARKBase(Material m, float hardness)
    {
        super(m);
        this.setHardness(hardness);
    }
}
