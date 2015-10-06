package com.arkcraft.mod.core.blocks;

import java.util.Random;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.Main;

/***
 * 
 * @author wildbill22
 *
 */
public class BlockInventoryCropPlot extends BlockContainer {
	public static final int GROWTH_STAGES = 5; // 0 - 5
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, GROWTH_STAGES);
    private int renderType = 3; //default value
	private boolean isOpaque = false;
	private int ID;
	private boolean render = false;

    public BlockInventoryCropPlot(String name, float hardness, Material mat, int ID)
    {
    	super(mat);
        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)));
//        this.setTickRandomly(true);
        float f = 0.5F;
        float f1 = 0.015625F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
        this.setCreativeTab((CreativeTabs)null);
        this.setHardness(0.5F);
		this.ID = ID;
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerBlock(this, name);

        this.disableStats();
    }

	public TileEntity createNewTileEntity(World worldIn, int meta) {
	     return new TileInventoryCropPlot();
    }	
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos blockPos, IBlockState state, EntityPlayer playerIn, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		if(!playerIn.isSneaking()) {
			playerIn.openGui(Main.instance(), ID, worldIn, blockPos.getX(), blockPos.getY(), blockPos.getZ());
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

	@SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos){
		return Items.redstone;
	}

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state){
        return ((Integer)state.getValue(AGE)).intValue();
    }

    protected BlockState createBlockState(){
        return new BlockState(this, new IProperty[] {AGE});
    }

    /**
     * Returns randomly, about 1/2 of the fertilizer and berries
     */
    @Override
    public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
        java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
        Random rand = world instanceof World ? ((World)world).rand : new Random();
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileInventoryCropPlot) {
			TileInventoryCropPlot tiCropPlot = (TileInventoryCropPlot) tileEntity;
			for (int i = 0; i < TileInventoryCropPlot.FERTILIZER_SLOTS_COUNT; ++i){
				if (rand.nextInt(2) == 0)
					ret.add(tiCropPlot.getStack(TileInventoryCropPlot.FIRST_FERTILIZER_SLOT + i));
			}
			if (rand.nextInt(2) == 0)
				ret.add(tiCropPlot.getStack(TileInventoryCropPlot.BERRY_SLOT));
		}
        return ret;
    }
}
