package com.arkcraft.module.weapon.init;

import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.module.weapon.common.block.BlockFlashlight;

public class Blocks
{
	public static BlockFlashlight block_flashlight;

	public Blocks()
	{
		init();
	}

	private void init()
	{
		block_flashlight = new BlockFlashlight();
		GameRegistry.registerBlock(block_flashlight, "block_flashlight");
	}
}
