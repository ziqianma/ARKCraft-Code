package com.arkcraft.module.blocks.common.container;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ContainerBlockSmithy extends ContainerBlockBase
{

    public ContainerBlockSmithy(String name, float hardness) { super(Material.anvil, name, hardness, 1); }

    public int getRenderType() { return 3; }

    public boolean isOpaqueCube() { return false; }

    public boolean renderAsNormalBlock() { return false; }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos,
                                    IBlockState state, EntityPlayer playerIn, EnumFacing side,
                                    float hitX, float hitY, float hitZ)
    {
        return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
    }

}
