package com.arkcraft.module.blocks.common.general;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.module.blocks.common.tile.TileInventoryForge;
import com.arkcraft.module.core.ARKCraft;

public class BlockRefiningForge extends BlockContainer
{
	private int ID;

	public BlockRefiningForge(Material material, int ID)
	{
		super(material);
		this.setCreativeTab(CreativeTabs.tabBlock);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH)
				.withProperty(BURNING, false).withProperty(PART, EnumPart.BOTTOM));
		this.ID = ID;
	}

	// Called when the block is placed or loaded client side to get the tile
	// entity for the block
	// Should return a new instance of the tile entity for the block
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		IBlockState state = getStateFromMeta(meta);
		if (state.getValue(PART).equals(EnumPart.BOTTOM)) return new TileInventoryForge();
		return null;
	}

	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if (player.capabilities.isCreativeMode && state.getValue(PART) == EnumPart.BOTTOM)
		{
			BlockPos blockpos1 = pos.up();
			if (worldIn.getBlockState(blockpos1).getBlock() == this)
			{
				worldIn.setBlockToAir(blockpos1);
			}
		}
		else if (player.capabilities.isCreativeMode && state.getValue(PART) == EnumPart.TOP)
		{
			BlockPos blockpos1 = pos.down();
			if (worldIn.getBlockState(blockpos1).getBlock() == this)
			{
				worldIn.setBlockToAir(blockpos1);
			}
		}
	}

	@Override
	public String getHarvestTool(IBlockState state)
	{
		return null;
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING,
				placer.getHorizontalFacing().getOpposite());
	}

	// Called when the block is right clicked
	// In this block it is used to open the blocks gui when right clicked by a
	// player
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		// Uses the gui handler registered to your mod to open the gui for the
		// given gui id
		// open on the server side only (not sure why you shouldn't open client
		// side too... vanilla doesn't, so we better not either)
		if (worldIn.isRemote) return true;
		if (state.getValue(PART).equals(EnumPart.TOP)) pos = pos.down();
		playerIn.openGui(ARKCraft.instance(), ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	// This is where you can do something when the block is broken. In this case
	// drop the inventory's contents
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if (state.getValue(PART).equals(EnumPart.TOP)) pos = pos.down();
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof IInventory)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory) tileEntity);
		}

		// if (inventory != null){
		// // For each slot in the inventory
		// for (int i = 0; i < inventory.getSizeInventory(); i++){
		// // If the slot is not empty
		// if (inventory.getStackInSlot(i) != null)
		// {
		// // Create a new entity item with the item stack in the slot
		// EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5,
		// pos.getY() + 0.5, pos.getZ() + 0.5, inventory.getStackInSlot(i));
		//
		// // Apply some random motion to the item
		// float multiplier = 0.1f;
		// float motionX = worldIn.rand.nextFloat() - 0.5f;
		// float motionY = worldIn.rand.nextFloat() - 0.5f;
		// float motionZ = worldIn.rand.nextFloat() - 0.5f;
		//
		// item.motionX = motionX * multiplier;
		// item.motionY = motionY * multiplier;
		// item.motionZ = motionZ * multiplier;
		//
		// // Spawn the item in the world
		// worldIn.spawnEntityInWorld(item);
		// }
		// }
		//
		// // Clear the inventory so nothing else (such as another mod) can do
		// anything with the items
		// inventory.clear();
		// }

		// Super MUST be called last because it removes the tile entity
		super.breakBlock(worldIn, pos, state);
	}

	// ------------------------------------------------------------
	// The code below isn't necessary for illustrating the Inventory Furnace
	// concepts, it's just used for rendering.
	// For more background information see MBE03
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		if (state.getValue(PART).equals(EnumPart.TOP)) pos = pos.down();
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof TileInventoryForge)
		{
			TileInventoryForge tileInventoryForge = (TileInventoryForge) tileEntity;
			return state.withProperty(BURNING, tileInventoryForge.isBurning());
		}
		return state;
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		EnumFacing enumfacing = EnumFacing.getHorizontal(meta);
		int metaOld = meta;
		EnumPart part = (metaOld & 8) > 0 ? EnumPart.TOP : EnumPart.BOTTOM;
		return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(PART, part);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		byte b0 = 0;
		int i = b0 | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
		if (state.getValue(PART).equals(EnumPart.TOP)) i |= 8;
		return i;
	}

	// necessary to define which properties your blocks use
	// will also affect the variants listed in the blockstates model file. See
	// MBE03 for more info.
	@Override
	protected BlockState createBlockState()
	{
		return new BlockState(this, new IProperty[] { BURNING, FACING, PART });
	}

	public static final PropertyBool BURNING = PropertyBool.create("burning");
	public static final PropertyDirection FACING = PropertyDirection.create("facing",
			EnumFacing.Plane.HORIZONTAL);
	public static final PropertyEnum PART = PropertyEnum.create("part", EnumPart.class);

	public static enum EnumPart implements IStringSerializable
	{
		TOP("top"), BOTTOM("bottom");

		private final String name;

		private EnumPart(String name)
		{
			this.name = name;
		}

		public String toString()
		{
			return this.name;
		}

		public String getName()
		{
			return this.name;
		}
	}

	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos)
	{
		int lightValue = 0;
		IBlockState blockState = getActualState(getDefaultState(), world, pos);
		boolean burning = (Boolean) blockState.getValue(BURNING);

		if (burning)
		{
			lightValue = 15;
		}
		else
		{
			// linearly interpolate the light value depending on how many slots
			// are burning
			lightValue = 0;
		}
		lightValue = MathHelper.clamp_int(lightValue, 0, 15);
		return lightValue;
	}

	// the block will render in the SOLID layer. See
	// http://greyminecraftcoder.blogspot.co.at/2014/12/block-rendering-18.html
	// for more information.
	@SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer()
	{
		return EnumWorldBlockLayer.SOLID;
	}

	// used by the renderer to control lighting and visibility of other blocks.
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isOpaqueCube()
	{
		return false;
	}

	// used by the renderer to control lighting and visibility of other blocks,
	// also by
	// (eg) wall or fence to control whether the fence joins itself to this
	// block
	// set to false because this block doesn't fill the entire 1x1x1 space
	@Override
	public boolean isFullCube()
	{
		return false;
	}

	// render using a BakedModel
	// not strictly required because the default (super method) is 3.
	@Override
	public int getRenderType()
	{
		return 3;
	}
}