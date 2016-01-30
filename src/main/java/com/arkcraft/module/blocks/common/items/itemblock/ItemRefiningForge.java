package com.arkcraft.module.blocks.common.items.itemblock;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.arkcraft.module.blocks.common.blocks.ARKCraftBlocks;
import com.arkcraft.module.blocks.common.blocks.BlockRefiningForge;

public class ItemRefiningForge extends ItemBlockARK
{
	public ItemRefiningForge(Block block)
	{
		super(block);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else
		{
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);
			if (!flag)
			{
				pos = pos.up();
			}
			// BlockPos blockpos1 = pos.offset(enumfacing1); // like a bed,
			// placed vertically
			BlockPos blockpos1 = pos.up();
			boolean flag1 = block.isReplaceable(worldIn, blockpos1);
			boolean flag2 = worldIn.isAirBlock(pos) || flag;
			boolean flag3 = worldIn.isAirBlock(blockpos1) || flag1;

			if (playerIn.canPlayerEdit(pos, side, stack) && playerIn.canPlayerEdit(blockpos1, side,
					stack))
			{
				if (flag2 && flag3 && World.doesBlockHaveSolidTopSurface(worldIn, pos.down()))
				{
					IBlockState iblockstate1 = ARKCraftBlocks.refining_forge.onBlockPlaced(worldIn,
							blockpos1, side, hitX, hitY, hitZ, 0, playerIn).withProperty(
							BlockRefiningForge.PART, BlockRefiningForge.EnumPart.BOTTOM);
					if (worldIn.setBlockState(pos, iblockstate1, 3))
					{
						IBlockState iblockstate2 = iblockstate1.withProperty(
								BlockRefiningForge.PART, BlockRefiningForge.EnumPart.TOP);
						worldIn.setBlockState(blockpos1, iblockstate2, 3);
					}
					--stack.stackSize;
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
	}
}
