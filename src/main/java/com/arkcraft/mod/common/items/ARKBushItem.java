package com.arkcraft.mod.common.items;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.blocks.ARKCraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ARKBushItem extends Item
{
    public ARKBushItem(String name)
    {
    	super();
    	this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setMaxStackSize(1);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
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
    	            else if (ARKCraftBlocks.berry_bush.canPlaceBlockAt(worldIn, blockpos1))
    	            {
    	                --stack.stackSize;
    	                worldIn.setBlockState(blockpos1, ARKCraftBlocks.berry_bush.getDefaultState());
    	                return true;
    	            }
    	            else
    	            {
    	                return false;
    	            }
    	        }
    	    }
}
    