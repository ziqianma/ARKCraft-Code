package net.minecraft.block;

import com.google.common.base.Objects;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTripWireHook extends Block
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool ATTACHED = PropertyBool.create("attached");
    public static final PropertyBool SUSPENDED = PropertyBool.create("suspended");
    private static final String __OBFID = "CL_00000329";

    public BlockTripWireHook()
    {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, Boolean.valueOf(false)).withProperty(ATTACHED, Boolean.valueOf(false)).withProperty(SUSPENDED, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setTickRandomly(true);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.withProperty(SUSPENDED, Boolean.valueOf(!World.doesBlockHaveSolidTopSurface(worldIn, pos.down())));
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

    /**
     * Check whether this Block can be placed on the given side
     */
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
    {
        return side.getAxis().isHorizontal() && worldIn.isSideSolid(pos.offset(side.getOpposite()), side, true);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();
        EnumFacing enumfacing;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            enumfacing = (EnumFacing)iterator.next();
        }
        while (!worldIn.isSideSolid(pos.offset(enumfacing), enumfacing.getOpposite(), true));

        return true;
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = this.getDefaultState().withProperty(POWERED, Boolean.valueOf(false)).withProperty(ATTACHED, Boolean.valueOf(false)).withProperty(SUSPENDED, Boolean.valueOf(false));

        if (facing.getAxis().isHorizontal())
        {
            iblockstate = iblockstate.withProperty(FACING, facing);
        }

        return iblockstate;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        this.func_176260_a(worldIn, pos, state, false, false, -1, (IBlockState)null);
    }

    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (neighborBlock != this)
        {
            if (this.checkForDrop(worldIn, pos, state))
            {
                EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

                if (!worldIn.isSideSolid(pos.offset(enumfacing.getOpposite()), enumfacing, true))
                {
                    this.dropBlockAsItem(worldIn, pos, state, 0);
                    worldIn.setBlockToAir(pos);
                }
            }
        }
    }

    public void func_176260_a(World worldIn, BlockPos pos, IBlockState hookState, boolean p_176260_4_, boolean p_176260_5_, int p_176260_6_, IBlockState p_176260_7_)
    {
        EnumFacing enumfacing = (EnumFacing)hookState.getValue(FACING);
        boolean flag2 = ((Boolean)hookState.getValue(ATTACHED)).booleanValue();
        boolean flag3 = ((Boolean)hookState.getValue(POWERED)).booleanValue();
        boolean flag4 = !World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
        boolean flag5 = !p_176260_4_;
        boolean flag6 = false;
        int j = 0;
        IBlockState[] aiblockstate = new IBlockState[42];
        BlockPos blockpos1;

        for (int k = 1; k < 42; ++k)
        {
            blockpos1 = pos.offset(enumfacing, k);
            IBlockState iblockstate2 = worldIn.getBlockState(blockpos1);

            if (iblockstate2.getBlock() == Blocks.tripwire_hook)
            {
                if (iblockstate2.getValue(FACING) == enumfacing.getOpposite())
                {
                    j = k;
                }

                break;
            }

            if (iblockstate2.getBlock() != Blocks.tripwire && k != p_176260_6_)
            {
                aiblockstate[k] = null;
                flag5 = false;
            }
            else
            {
                if (k == p_176260_6_)
                {
                    iblockstate2 = (IBlockState)Objects.firstNonNull(p_176260_7_, iblockstate2);
                }

                boolean flag7 = !((Boolean)iblockstate2.getValue(BlockTripWire.DISARMED)).booleanValue();
                boolean flag8 = ((Boolean)iblockstate2.getValue(BlockTripWire.POWERED)).booleanValue();
                boolean flag9 = ((Boolean)iblockstate2.getValue(BlockTripWire.SUSPENDED)).booleanValue();
                flag5 &= flag9 == flag4;
                flag6 |= flag7 && flag8;
                aiblockstate[k] = iblockstate2;

                if (k == p_176260_6_)
                {
                    worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
                    flag5 &= flag7;
                }
            }
        }

        flag5 &= j > 1;
        flag6 &= flag5;
        IBlockState iblockstate3 = this.getDefaultState().withProperty(ATTACHED, Boolean.valueOf(flag5)).withProperty(POWERED, Boolean.valueOf(flag6));

        if (j > 0)
        {
            blockpos1 = pos.offset(enumfacing, j);
            EnumFacing enumfacing1 = enumfacing.getOpposite();
            worldIn.setBlockState(blockpos1, iblockstate3.withProperty(FACING, enumfacing1), 3);
            this.func_176262_b(worldIn, blockpos1, enumfacing1);
            this.func_180694_a(worldIn, blockpos1, flag5, flag6, flag2, flag3);
        }

        this.func_180694_a(worldIn, pos, flag5, flag6, flag2, flag3);

        if (!p_176260_4_)
        {
            worldIn.setBlockState(pos, iblockstate3.withProperty(FACING, enumfacing), 3);

            if (p_176260_5_)
            {
                this.func_176262_b(worldIn, pos, enumfacing);
            }
        }

        if (flag2 != flag5)
        {
            for (int l = 1; l < j; ++l)
            {
                BlockPos blockpos2 = pos.offset(enumfacing, l);
                IBlockState iblockstate4 = aiblockstate[l];

                if (iblockstate4 != null && worldIn.getBlockState(blockpos2).getBlock() != Blocks.air)
                {
                    worldIn.setBlockState(blockpos2, iblockstate4.withProperty(ATTACHED, Boolean.valueOf(flag5)), 3);
                }
            }
        }
    }

    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops to grow, etc.)
     */
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        this.func_176260_a(worldIn, pos, state, false, true, -1, (IBlockState)null);
    }

    private void func_180694_a(World worldIn, BlockPos p_180694_2_, boolean p_180694_3_, boolean p_180694_4_, boolean p_180694_5_, boolean p_180694_6_)
    {
        if (p_180694_4_ && !p_180694_6_)
        {
            worldIn.playSoundEffect((double)p_180694_2_.getX() + 0.5D, (double)p_180694_2_.getY() + 0.1D, (double)p_180694_2_.getZ() + 0.5D, "random.click", 0.4F, 0.6F);
        }
        else if (!p_180694_4_ && p_180694_6_)
        {
            worldIn.playSoundEffect((double)p_180694_2_.getX() + 0.5D, (double)p_180694_2_.getY() + 0.1D, (double)p_180694_2_.getZ() + 0.5D, "random.click", 0.4F, 0.5F);
        }
        else if (p_180694_3_ && !p_180694_5_)
        {
            worldIn.playSoundEffect((double)p_180694_2_.getX() + 0.5D, (double)p_180694_2_.getY() + 0.1D, (double)p_180694_2_.getZ() + 0.5D, "random.click", 0.4F, 0.7F);
        }
        else if (!p_180694_3_ && p_180694_5_)
        {
            worldIn.playSoundEffect((double)p_180694_2_.getX() + 0.5D, (double)p_180694_2_.getY() + 0.1D, (double)p_180694_2_.getZ() + 0.5D, "random.bowhit", 0.4F, 1.2F / (worldIn.rand.nextFloat() * 0.2F + 0.9F));
        }
    }

    private void func_176262_b(World worldIn, BlockPos p_176262_2_, EnumFacing p_176262_3_)
    {
        worldIn.notifyNeighborsOfStateChange(p_176262_2_, this);
        worldIn.notifyNeighborsOfStateChange(p_176262_2_.offset(p_176262_3_.getOpposite()), this);
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!this.canPlaceBlockAt(worldIn, pos))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
        else
        {
            return true;
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        float f = 0.1875F;

        switch (BlockTripWireHook.SwitchEnumFacing.FACING_LOOKUP[((EnumFacing)worldIn.getBlockState(pos).getValue(FACING)).ordinal()])
        {
            case 1:
                this.setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
                break;
            case 2:
                this.setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
                break;
            case 3:
                this.setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
                break;
            case 4:
                this.setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        boolean flag = ((Boolean)state.getValue(ATTACHED)).booleanValue();
        boolean flag1 = ((Boolean)state.getValue(POWERED)).booleanValue();

        if (flag || flag1)
        {
            this.func_176260_a(worldIn, pos, state, true, false, -1, (IBlockState)null);
        }

        if (flag1)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.offset(((EnumFacing)state.getValue(FACING)).getOpposite()), this);
        }

        super.breakBlock(worldIn, pos, state);
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0;
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return !((Boolean)state.getValue(POWERED)).booleanValue() ? 0 : (state.getValue(FACING) == side ? 15 : 0);
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0)).withProperty(ATTACHED, Boolean.valueOf((meta & 4) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        byte b0 = 0;
        int i = b0 | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();

        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            i |= 8;
        }

        if (((Boolean)state.getValue(ATTACHED)).booleanValue())
        {
            i |= 4;
        }

        return i;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, POWERED, ATTACHED, SUSPENDED});
    }

    static final class SwitchEnumFacing
        {
            static final int[] FACING_LOOKUP = new int[EnumFacing.values().length];
            private static final String __OBFID = "CL_00002050";

            static
            {
                try
                {
                    FACING_LOOKUP[EnumFacing.EAST.ordinal()] = 1;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    FACING_LOOKUP[EnumFacing.WEST.ordinal()] = 2;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    FACING_LOOKUP[EnumFacing.SOUTH.ordinal()] = 3;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    FACING_LOOKUP[EnumFacing.NORTH.ordinal()] = 4;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}