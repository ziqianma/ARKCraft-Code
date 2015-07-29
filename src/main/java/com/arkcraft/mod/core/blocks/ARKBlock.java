package com.arkcraft.mod.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;
/**
 * @author Vastatio
 */
/* This is the base class for all generic blocks */
public class ARKBlock extends Block {

	public ARKBlock(Material m, String name, float hardness) {
		super(m);
		this.setHardness(hardness);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setUnlocalizedName(name);
		/* Registers with the name, not the unlocalized tile. name. */
		GameRegistry.registerBlock(this, this.getUnlocalizedName().substring(5));
	}
}
