package com.arkcraft.mod.core.blocks;

import java.util.Random;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.lib.BALANCE;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
	
	// Called when bush is right clicked
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			for (int i = 0; i < BALANCE.PLANTS.BERRIES_MIN_PER_PICKING || i <= worldIn.rand.nextInt(BALANCE.PLANTS.BERRIES_MAX_PER_PICKING); i++) {
				Item itemPicked = getItemDropped(state, worldIn.rand, 0);
		        this.entityDropItem(worldIn, pos, playerIn, new ItemStack(itemPicked, 1, 0));
			}
		}
		return true;
    }
	
    /**
     * Drops an item at the position of the bush.
     */
    private void entityDropItem(World worldIn, BlockPos pos, EntityPlayer playerIn, ItemStack itemStackIn) {
        if (itemStackIn.stackSize != 0 && itemStackIn.getItem() != null) {
        	Float offset = worldIn.rand.nextFloat();
            EntityItem entityitem = new EntityItem(worldIn, pos.getX() + offset, pos.getY() + this.maxY, pos.getZ() + offset, itemStackIn);
            entityitem.setDefaultPickupDelay();
            if (playerIn.captureDrops)
            	playerIn.capturedDrops.add(entityitem);
            else
                worldIn.spawnEntityInWorld(entityitem);
        }
    }
	
	/*	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {}

    public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion)
    {
        return motion;
    }
    	*/
	@Override
	public boolean isOpaqueCube() { return false; }
	
	@Override
	public EnumWorldBlockLayer getBlockLayer() { return EnumWorldBlockLayer.CUTOUT; }
	
	@Override
    public int quantityDropped(Random random) { return random.nextInt(10) <= 5 ? 1 : 2; }
		
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) { return this.blockMaterial != Material.air; }
	    
	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) { return null; }
	 
	@Override
	public boolean isFullCube() { return false; }
	  	
}
