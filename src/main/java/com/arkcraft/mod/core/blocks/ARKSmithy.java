package com.arkcraft.mod.core.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.Main;

public class ARKSmithy extends Block {

	public ARKSmithy(String name, float hardness) {
		super(Material.anvil);
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerBlock(this, name);
	}

	public int getRenderType() { return 3; }
	public boolean isOpaqueCube() { return false; }
	public boolean renderAsNormalBlock() { return false; }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos,
			IBlockState state, EntityPlayer playerIn, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if(!playerIn.isSneaking()) {
			playerIn.openGui(Main.instance, GlobalAdditions.guiIDSmithy, worldIn, pos.getX(), pos.getY(), pos.getZ());
			return true;
		}
		return false;
	}

}
