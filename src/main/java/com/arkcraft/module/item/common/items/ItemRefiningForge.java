package com.arkcraft.module.item.common.items;

import net.minecraft.block.Block;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.GlobalAdditions;

public class ItemRefiningForge extends ItemBlock
{
	public ItemRefiningForge(Block block)
	{
		super(block);
		setCreativeTab(GlobalAdditions.tabARK);
		setUnlocalizedName("item_refining_forge");
		this.setMaxStackSize(1);
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is
	 * pressed. Args: itemStack, world, entityPlayer
	 */

	// public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World
	// worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float
	// hitZ)
	// {
	// boolean flag =
	// worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
	// BlockPos blockpos1 = flag ? pos : pos.offset(side);
	//
	// if (!playerIn.canPlayerEdit(blockpos1, side, stack))
	// {
	// return false;
	// }
	// else
	// {
	// Block block = worldIn.getBlockState(blockpos1).getBlock();
	//
	// if (!worldIn.canBlockBePlaced(block, blockpos1, false, side, (Entity)
	// null, stack))
	// {
	// return false;
	// }
	// else if (ARKCraftBlocks.refining_forge.canPlaceBlockAt(worldIn,
	// blockpos1))
	// {
	// --stack.stackSize;
	// worldIn.setBlockState(blockpos1,
	// ARKCraftBlocks.refining_forge.getDefaultState());
	// return true;
	// }
	// else
	// {
	// return false;
	// }
	// }
	// }
}
