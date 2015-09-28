package com.arkcraft.mod.core.blocks.crop_test;

import java.util.Random;

import net.minecraft.block.Block;
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
import net.minecraft.util.MathHelper;
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
        this.setTickRandomly(true);
        float f = 0.5F;
        float f1 = 0.015625F;
        this.setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
        this.setCreativeTab((CreativeTabs)null);
        this.setHardness(0.0F);
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

	@Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand){
        super.updateTick(worldIn, pos, state, rand);
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9){
            int i = ((Integer)state.getValue(AGE)).intValue();

            if (i < 11){
                float f = getGrowthChance(this, worldIn, pos);

                if (rand.nextInt((int)(25.0F / f) + 1) == 0){
                    worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i + 1)), 2);
                }
            }
        }
    }
	
    public void grow(World worldIn, BlockPos pos, IBlockState state) {
        int i = ((Integer)state.getValue(AGE)).intValue() + MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);

        if (i > GROWTH_STAGES) {
            i = GROWTH_STAGES;
        }
        worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i)), 2);
    }
    
    protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos) {
        float f = 1.0F;
        BlockPos blockpos1 = pos.down();

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j){
                float f1 = 0.0F;
                IBlockState iblockstate = worldIn.getBlockState(blockpos1.add(i, 0, j));

                if (iblockstate.getBlock().canSustainPlant(worldIn, blockpos1.add(i, 0, j), net.minecraft.util.EnumFacing.UP, 
                		(net.minecraftforge.common.IPlantable)blockIn)) {
                    f1 = 1.0F;
                    if (iblockstate.getBlock().isFertile(worldIn, blockpos1.add(i, 0, j))){
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }
                f += f1;
            }
        }
        BlockPos blockpos2 = pos.north();
        BlockPos blockpos3 = pos.south();
        BlockPos blockpos4 = pos.west();
        BlockPos blockpos5 = pos.east();
        boolean flag = blockIn == worldIn.getBlockState(blockpos4).getBlock() || blockIn == worldIn.getBlockState(blockpos5).getBlock();
        boolean flag1 = blockIn == worldIn.getBlockState(blockpos2).getBlock() || blockIn == worldIn.getBlockState(blockpos3).getBlock();

        if (flag && flag1) {
            f /= 2.0F;
        }
        else
        {
            boolean flag2 = blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos5.north()).getBlock() 
            		|| blockIn == worldIn.getBlockState(blockpos5.south()).getBlock() 
            		|| blockIn == worldIn.getBlockState(blockpos4.south()).getBlock();
            if (flag2)
            {
                f /= 2.0F;
            }
        }
        return f;
    }    
	protected Item getSeed()
    {
        return GlobalAdditions.narcoBerrySeed;
    }
	
	@SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos){
		return Items.redstone;
	}

    /**
     * Whether this IGrowable can grow
     */
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return ((Integer)state.getValue(AGE)).intValue() < 11;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        this.grow(worldIn, pos, state);
    }

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
        return ((Integer)state.getValue(AGE)).intValue();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {AGE});
    }

    @Override
    public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
        int age = ((Integer)state.getValue(AGE)).intValue();
        Random rand = world instanceof World ? ((World)world).rand : new Random();

        if (age >= 11)
        {
        	@SuppressWarnings("unused")
            int k = 3 + fortune;

            for (int i = 0; i < 3 + fortune; ++i)
            {
                if (rand.nextInt(15) <= age)
                {
                    ret.add(new ItemStack(this.getSeed(), 1, 0));
                }
            }
        }
        return ret;
    }
}
