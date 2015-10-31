package com.arkcraft.mod.common.blocks;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.items.ARKCraftItems;
import com.arkcraft.mod.common.tile.TileInventorySmithy;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/***
 * 
 * @author wildbill22
 *
 */
public class BlockInventorySmithy extends BlockContainer {
    private int renderType = 3; //default value
	private boolean isOpaque = false;
	private int ID;
	private boolean render = false;
    public static final PropertyEnum PART = PropertyEnum.create("part", BlockInventorySmithy.EnumPartType.class);
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);

    public BlockInventorySmithy(String name, float hardness, Material mat, int ID){
    	super(mat);
        this.setHardness(0.5F);
		this.ID = ID;
		this.setUnlocalizedName(name);
//		this.setCreativeTab(GlobalAdditions.tabARK); // There is now an item for this
		GameRegistry.registerBlock(this, name);
    }

	public TileEntity createNewTileEntity(World worldIn, int meta) {
	     return new TileInventorySmithy();
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
    @Override
    public java.util.List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune){
        java.util.List<ItemStack> ret = super.getDrops(world, pos, state, fortune);
        Random rand = world instanceof World ? ((World)world).rand : new Random();
		TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileInventorySmithy) {
			TileInventorySmithy tiSmithy = (TileInventorySmithy) tileEntity;
			for (int i = 0; i < TileInventorySmithy.INVENTORY_SLOTS_COUNT; ++i){
				if (rand.nextInt(2) == 0)
					ret.add(tiSmithy.getStackInSlot(TileInventorySmithy.FIRST_INVENTORY_SLOT + i));
			}
		}
        return ret;
    }

    // ---------------- Stuff for multiblock ------------------------

    @SideOnly(Side.CLIENT)
	@Override
    public Item getItem(World worldIn, BlockPos pos) {
        return ARKCraftItems.item_smithy;
    }

	@Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (player.capabilities.isCreativeMode && state.getValue(PART) == BlockInventorySmithy.EnumPartType.LEFT) {
            BlockPos blockpos1 = pos.offset(((EnumFacing)state.getValue(FACING)).getOpposite());
            if (worldIn.getBlockState(blockpos1).getBlock() == this){
                worldIn.setBlockToAir(blockpos1);
            }
        }
    }

	@Override
    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.setSmithyBounds();
    }
    
    private void setSmithyBounds(){
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Called when a neighboring block changes.
     */
	@Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

        if (state.getValue(PART) == BlockInventorySmithy.EnumPartType.LEFT) {
//            if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this) { // Original, from bed
            if (worldIn.getBlockState(pos.offset(enumfacing.rotateY())).getBlock() != this) {
                worldIn.setBlockToAir(pos);
            }
        }
//        else if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this) { // Original, from bed
        else if (worldIn.getBlockState(pos.offset(enumfacing.rotateYCCW())).getBlock() != this) {
            worldIn.setBlockToAir(pos);
            if (!worldIn.isRemote) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
        }
    }
    
    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(PART) == BlockInventorySmithy.EnumPartType.LEFT ? null : ARKCraftItems.item_smithy;
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     *  
     * @param chance The chance that each Item is actually spawned (1.0 = always, 0.0 = never)
     * @param fortune The player's fortune level
     */
	@Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (state.getValue(PART) == BlockInventorySmithy.EnumPartType.RIGHT) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
        }
    }

    @SideOnly(Side.CLIENT)
	@Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.SOLID;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
	@Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
        return (meta & 8) > 0 ? this.getDefaultState().withProperty(PART, BlockInventorySmithy.EnumPartType.LEFT).withProperty(FACING, enumfacing) 
        		: this.getDefaultState().withProperty(PART, BlockInventorySmithy.EnumPartType.RIGHT).withProperty(FACING, enumfacing);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
	@Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
	@Override
    public int getMetaFromState(IBlockState state) {
        byte b0 = 0;
        int i = b0 | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
        if (state.getValue(PART) == BlockInventorySmithy.EnumPartType.LEFT){
            i |= 8;
        }
        return i;
    }
    
	@Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {FACING, PART});
    }

    public static enum EnumPartType implements IStringSerializable  {
        LEFT("left"),
        RIGHT("right");
        private final String name;

        private EnumPartType(String name){
            this.name = name;
        }

        public String toString(){
            return this.name;
        }

        public String getName(){
            return this.name;
        }
    }
}
