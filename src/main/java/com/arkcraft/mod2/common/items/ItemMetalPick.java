package com.arkcraft.mod2.common.items;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

import com.google.common.collect.ImmutableSet;

public class ItemMetalPick extends ItemPickaxe{
	
	protected ItemMetalPick(ToolMaterial material) {
	
		super(material);
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack) {
	    return ImmutableSet.of("pickaxe", "axe");
	}

	@Override
	public float getStrVsBlock(ItemStack stack, Block block) {

    return block.getMaterial() != Material.iron && block.getMaterial() != Material.anvil && block.getMaterial() != Material.rock ? super.getStrVsBlock(stack, block) : this.efficiencyOnProperMaterial;
    
	}
	
	public boolean isArkTool() {
		return true;
	}
}
