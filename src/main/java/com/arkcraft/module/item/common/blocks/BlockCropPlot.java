package com.arkcraft.module.item.common.blocks;

import com.arkcraft.module.item.common.items.ARKCraftItems;
import com.arkcraft.module.item.common.tile.TileInventoryCropPlot;
import com.arkcraft.module.core.ARKCraft;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * @author wildbill22
 */
public class BlockCropPlot extends BlockContainer
{
    public static final int GROWTH_STAGES = 5; // 0 - 5
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, GROWTH_STAGES);
    private int renderType = 3; //default value
    private boolean isOpaque = false;
    private int ID;

    public BlockCropPlot(Material mat, int ID)
    {
        super(mat);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
//        this.setTickRandomly(true);
        float f = 0.5F;
        float f1 = 0.015625F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
        this.setHardness(0.5F);
        this.ID = ID;

        this.disableStats();
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileInventoryCropPlot();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos blockPos, IBlockState state, EntityPlayer playerIn, EnumFacing side,
                                    float hitX, float hitY, float hitZ)
    {
        if (!playerIn.isSneaking())
        {
            playerIn.openGui(ARKCraft.instance(), ID, worldIn, blockPos.getX(), blockPos.getY(), blockPos.getZ());
            return true;
        }
        return false;
    }

    public void setRenderType(int renderType) { this.renderType = renderType; }

    public int getRenderType() { return renderType; }

    public void setOpaque(boolean opaque) { opaque = isOpaque; }

    public boolean isOpaqueCube() { return isOpaque; }

    public boolean isFullCube() { return false; }

    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos)
    {
        return ARKCraftItems.item_crop_plot;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer() { return EnumWorldBlockLayer.CUTOUT; }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer) state.getValue(AGE)).intValue();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] { AGE });
    }

    /**
     * Returns randomly, about 1/2 of the fertilizer and berries
     */
    @Override
    public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileInventoryCropPlot)
        {
            TileInventoryCropPlot tiCropPlot = (TileInventoryCropPlot) tileEntity;
            for (int i = 0; i < TileInventoryCropPlot.FERTILIZER_SLOTS_COUNT; ++i)
            {
                if (rand.nextInt(2) == 0)
                {
                    ret.add(tiCropPlot.getStackInSlot(TileInventoryCropPlot.FIRST_FERTILIZER_SLOT + i));
                }
            }
            if (rand.nextInt(2) == 0)
            {
                ret.add(tiCropPlot.getStackInSlot(TileInventoryCropPlot.BERRY_SLOT));
            }
        }
        return ret;
    }
}
