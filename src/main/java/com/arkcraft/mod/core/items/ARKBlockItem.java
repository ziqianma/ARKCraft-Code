package com.arkcraft.mod.core.items;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;

public class ARKBlockItem extends Item
{

    public ARKBlockItem(String name)
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
    	            else if (GlobalAdditions.crop_plot.canPlaceBlockAt(worldIn, blockpos1))
    	            {
    	                --stack.stackSize;
    	                worldIn.setBlockState(blockpos1, GlobalAdditions.crop_plot.getDefaultState());
    	                return true;
    	            }
    	            else
    	            {
    	                return false;
    	            }
    	        }
    	    }
}
    