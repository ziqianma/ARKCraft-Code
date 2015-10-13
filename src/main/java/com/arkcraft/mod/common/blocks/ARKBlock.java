package com.arkcraft.mod.common.blocks;

import com.arkcraft.mod.GlobalAdditions;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
		/* Registers with the name, not the unlocalized tile. name. (substring(5)) */
		/* Automatically registers the block with the GameRegistry */
		GameRegistry.registerBlock(this, this.getUnlocalizedName().substring(5));
	}
}
