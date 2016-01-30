package com.arkcraft.module.blocks.common.items;

import net.minecraft.item.Item;

public class ItemCrystal extends Item
{
    public ItemCrystal()
    {
        super();
        this.setMaxStackSize(16);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    /*
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    	{
    		boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
    	    BlockPos blockpos1 = flag ? pos : pos.offset(side);

    	        if (!playerIn.canPlayerEdit(blockpos1, side, stack))
    	        {
    	            return false;
    	        }
    	        else
    	        {
    	            Block block = worldIn.getBlockState(blockpos1).getBlock();

    	            if (!worldIn.canBlockBePlaced(block, blockpos1, false, side, (Entity)null, stack))
    	            {
    	                return false;
    	            }
    	            else if (ARKCraftBlocks.crop_plot.canPlaceBlockAt(worldIn, blockpos1))
    	            {
    	                --stack.stackSize;
    	                worldIn.setBlockState(blockpos1, ARKCraftBlocks.crop_plot.getDefaultState());
    	                return true;
    	            }
    	            else
    	            {
    	                return false;
    	            }
    	        }
    	    }
    	    */
}
    