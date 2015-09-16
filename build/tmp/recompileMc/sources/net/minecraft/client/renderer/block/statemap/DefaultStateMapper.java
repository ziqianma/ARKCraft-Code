package net.minecraft.client.renderer.block.statemap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class DefaultStateMapper extends StateMapperBase
{
    private static final String __OBFID = "CL_00002477";

    protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_)
    {
        return new ModelResourceLocation((ResourceLocation)Block.blockRegistry.getNameForObject(p_178132_1_.getBlock()), this.getPropertyString(p_178132_1_.getProperties()));
    }
}