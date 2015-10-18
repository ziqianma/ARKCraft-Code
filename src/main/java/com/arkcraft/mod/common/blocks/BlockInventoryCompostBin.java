package com.arkcraft.mod.common.blocks;

import java.util.Random;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.tile.TileInventoryCompostBin;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockInventoryCompostBin extends BlockContainer{
	
	private int renderType = 3; //default value
	private boolean isOpaque = false;
	private int ID;
	private boolean render = false;
	
	public BlockInventoryCompostBin(String name, float hardness, Material mat, int ID) {
		super(mat);
		this.ID = ID;
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerBlock(this, name);
	}
	

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos blockPos, IBlockState state, EntityPlayer playerIn, EnumFacing side,	
			float hitX, float hitY, float hitZ) {
		if(!playerIn.isSneaking()) {
            if (!worldIn.isRemote) {
    			playerIn.openGui(ARKCraft.instance(), ID, worldIn, blockPos.getX(), blockPos.getY(), blockPos.getZ());
            	return true;
            }
		}
		return false;
	}
	
	public TileEntity createNewTileEntity(World worldIn, int meta) {
	     return new TileInventoryCompostBin();
	}	
	
	public void setRenderType(int renderType) { this.renderType = renderType; }
	public int getRenderType() { return renderType; }
	
	public void setOpaque(boolean opaque) { opaque = isOpaque; }
	public boolean isOpaqueCube() { return isOpaque; }
	
	
	public void setRenderAsNormalBlock(boolean b) { render = b; }
	public boolean renderAsNormalBlock() { return render; }
	
    /**
     * Returns randomly, about 1/2 of the recipe items
     */
    @Override
    public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
        java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
        Random rand = world instanceof World ? ((World)world).rand : new Random();
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileInventoryCompostBin) {
			TileInventoryCompostBin tiCB = (TileInventoryCompostBin) tileEntity;
			for (int i = 0; i < TileInventoryCompostBin.COMPOST_SLOTS_COUNT; ++i){
				if (rand.nextInt(2) == 0)
					ret.add(tiCB.getStackInSlot(TileInventoryCompostBin.FIRST_COMPOST_SLOT + i));
			}
		}
        return ret;
    }
}
