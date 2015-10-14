package com.arkcraft.mod.common.blocks;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.ARKCraft;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ARKContainerBlock extends Block {

	private int renderType = 3; //default value
	private boolean isOpaque = false;
	private int ID;
	private boolean render = false;
	
	public ARKContainerBlock(String name, float hardness, Material mat, int ID) {
		super(mat);
		
		this.ID = ID;
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerBlock(this, name);
	}
	
	public void setRenderType(int renderType) { this.renderType = renderType; }
	public int getRenderType() { return renderType; }
	
	public void setOpaque(boolean opaque) { opaque = isOpaque; }
	public boolean isOpaqueCube() { return isOpaque; }
	
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
	
	public void setRenderAsNormalBlock(boolean b) { render = b; }
	public boolean renderAsNormalBlock() { return render; }
}
