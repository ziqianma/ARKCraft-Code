package com.arkcraft.module.item.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.arkcraft.module.item.common.tile.TileFlashlight;

public class BlockFlashlight extends BlockContainer
{
	public BlockFlashlight()
	{
		super(Material.air);
		this.setLightLevel(0.8F);
		// this.setUnlocalizedName("light_source");
		this.setBlockBounds(0, 0, 0, 0, 0, 0);
		setTickRandomly(true);
	}

	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	@Override
	public boolean isFullCube()
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileFlashlight();
	}
}