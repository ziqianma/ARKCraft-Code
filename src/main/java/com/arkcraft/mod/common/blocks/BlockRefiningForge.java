package com.arkcraft.mod.common.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.items.ARKCraftItems;
import com.arkcraft.mod.common.tile.TileInventoryForge;

public class BlockRefiningForge extends BlockContainer {
    private int renderType = 3; //default value
	private boolean isOpaque = false;
	private int ID;
	private boolean render = false;

    public BlockRefiningForge(Material mat, int ID){
    	super(mat);
        this.setHardness(0.5F);
		this.ID = ID;
    }

	public TileEntity createNewTileEntity(World worldIn, int meta) {
	     return new TileInventoryForge();
    }	
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos blockPos, IBlockState state, EntityPlayer playerIn, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if(!playerIn.isSneaking()) {
			playerIn.openGui(ARKCraft.instance(), ID, worldIn, blockPos.getX(), blockPos.getY(), blockPos.getZ());
			return true;
		}
		return false;
	}
	
    public void setRenderType(int renderType) { this.renderType = renderType; }
	public int getRenderType() { return renderType; }
		
	public void setOpaque(boolean opaque) { opaque = isOpaque; }
	public boolean isOpaqueCube() { return isOpaque; }
	

	public void setRenderAsNormalBlock(boolean b) { render = b; }
	public boolean renderAsNormalBlock() { return render; }

	@Override
    public boolean isFullCube() { return false; }

    /**
     * Returns randomly, about 1/2 of the recipe items
     */

    @SideOnly(Side.CLIENT)
	@Override
    public Item getItem(World worldIn, BlockPos pos) {
        return ARKCraftItems.item_refining_forge;
    }

}