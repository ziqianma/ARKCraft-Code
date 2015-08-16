package com.arkcraft.mod.core.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumWorldBlockLayer;

import com.arkcraft.mod.core.GlobalAdditions;
/**
 * @author Vastatio
 */
public class ARKBush extends ARKBlock {

	public ARKBush(String name, float hardness) {
		super(Material.leaves, name, hardness);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		if(rand.nextInt(10) <= 4) return rand.nextInt(10) <= 5 ? GlobalAdditions.amarBerry : GlobalAdditions.narcoBerry;
		if(rand.nextInt(10) >= 4 && rand.nextInt(10) <= 8) return rand.nextInt(10) <= 5 ? GlobalAdditions.mejoBerry : GlobalAdditions.tintoBerry;
		if(rand.nextInt(10) <= 8) return GlobalAdditions.fiber;
		else { return GlobalAdditions.azulBerry; }
	}
	
	@Override
	public boolean isOpaqueCube() { return false; }
	
	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
    public int quantityDropped(Random random) {
		return random.nextInt(10) <= 5 ? 1 : 2;
	}
	
}
