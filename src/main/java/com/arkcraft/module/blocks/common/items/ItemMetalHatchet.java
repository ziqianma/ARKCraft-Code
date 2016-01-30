package com.arkcraft.module.blocks.common.items;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class ItemMetalHatchet extends ItemAxe
{

    protected ItemMetalHatchet(ToolMaterial material)
    {

        super(material);
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack)
    {
        return ImmutableSet.of("pickaxe", "axe");
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block block)
    {

        return block.getMaterial() != Material.wood && block.getMaterial() != Material.vine && block.getMaterial() != Material.plants ?
                super.getStrVsBlock(stack, block) : this.efficiencyOnProperMaterial;
    }

    public boolean isArkTool()
    {
        return true;
    }

}
