package com.arkcraft.mod.core.blocks.CropPlot;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IPlaceable
{
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos);
    public IBlockState getPlant(IBlockAccess world, BlockPos pos);
}