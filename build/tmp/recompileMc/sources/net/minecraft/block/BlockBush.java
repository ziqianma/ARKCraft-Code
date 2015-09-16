package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBush extends Block implements net.minecraftforge.common.IPlantable
{
    private static final String __OBFID = "CL_00000208";

    protected BlockBush(Material materialIn)
    {
        super(materialIn);
        this.setTickRandomly(true);
        float f = 0.2F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    protected BlockBush()
    {
        this(Material.plants);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.getBlockState(pos.down()).getBlock().canSustainPlant(worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
    }

    /**
     * is the block grass, dirt or farmland
     */
    protected boolean canPlaceBlockOn(Block ground)
    {
        return ground == Blocks.grass || ground == Blocks.dirt || ground == Blocks.farmland;
    }

    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
        this.checkAndDropBlock(worldIn, pos, state);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        this.checkAndDropBlock(worldIn, pos, state);
    }

    protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.canBlockStay(worldIn, pos, state))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        BlockPos down = pos.down();
        Block soil = worldIn.getBlockState(down).getBlock();
        if (state.getBlock() != this) return this.canPlaceBlockOn(soil); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
        return soil.canSustainPlant(worldIn, down, net.minecraft.util.EnumFacing.UP, this);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    @Override
    public net.minecraftforge.common.EnumPlantType getPlantType(net.minecraft.world.IBlockAccess world, BlockPos pos)
    {
        if (this == Blocks.wheat)          return net.minecraftforge.common.EnumPlantType.Crop;
        if (this == Blocks.carrots)        return net.minecraftforge.common.EnumPlantType.Crop;
        if (this == Blocks.potatoes)       return net.minecraftforge.common.EnumPlantType.Crop;
        if (this == Blocks.melon_stem)     return net.minecraftforge.common.EnumPlantType.Crop;
        if (this == Blocks.pumpkin_stem)   return net.minecraftforge.common.EnumPlantType.Crop;
        if (this == Blocks.deadbush)       return net.minecraftforge.common.EnumPlantType.Desert;
        if (this == Blocks.waterlily)      return net.minecraftforge.common.EnumPlantType.Water;
        if (this == Blocks.red_mushroom)   return net.minecraftforge.common.EnumPlantType.Cave;
        if (this == Blocks.brown_mushroom) return net.minecraftforge.common.EnumPlantType.Cave;
        if (this == Blocks.nether_wart)    return net.minecraftforge.common.EnumPlantType.Nether;
        if (this == Blocks.sapling)        return net.minecraftforge.common.EnumPlantType.Plains;
        if (this == Blocks.tallgrass)      return net.minecraftforge.common.EnumPlantType.Plains;
        if (this == Blocks.double_plant)   return net.minecraftforge.common.EnumPlantType.Plains;
        if (this == Blocks.red_flower)     return net.minecraftforge.common.EnumPlantType.Plains;
        if (this == Blocks.yellow_flower)  return net.minecraftforge.common.EnumPlantType.Plains;
        return net.minecraftforge.common.EnumPlantType.Plains;
    }

    @Override
    public IBlockState getPlant(net.minecraft.world.IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return getDefaultState();
        return state;
    }
}