package com.arkcraft.mod.common.container;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.GlobalAdditions;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ContainerBlockBase extends Block {

	private int guiID;
	
	public ContainerBlockBase(Material mat, String name, float hardness, int guiID) {
		super(mat);
		this.guiID = guiID;
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setHardness(hardness);
		this.setUnlocalizedName(name);
		GameRegistry.registerBlock(this, name);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
		if(player.isSneaking()) 
			return false;
		player.openGui(ARKCraft.instance, guiID, world, blockPos.getX(), blockPos.getY(), blockPos.getZ());
		return true;
	}
	
}
