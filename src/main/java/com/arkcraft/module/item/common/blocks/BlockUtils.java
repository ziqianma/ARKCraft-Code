package com.arkcraft.module.item.common.blocks;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

import com.arkcraft.lib.LogHelper;

public class BlockUtils
{
	public static void getCardinalDirection(BlockPos pos, EntityLivingBase placer)
	{
		int blockX = pos.getX();
		int blockZ = pos.getZ();
		int placerX = placer.getPosition().getX();
		int placerZ = placer.getPosition().getZ();

		Vec3 v = placer.getLookVec();

		LogHelper.info(blockX - placerX);
		LogHelper.info(blockZ - placerZ);
	}
}
