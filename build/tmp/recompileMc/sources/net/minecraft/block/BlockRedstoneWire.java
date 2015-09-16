package net.minecraft.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRedstoneWire extends Block
{
    public static final PropertyEnum NORTH = PropertyEnum.create("north", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum EAST = PropertyEnum.create("east", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum SOUTH = PropertyEnum.create("south", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyEnum WEST = PropertyEnum.create("west", BlockRedstoneWire.EnumAttachPosition.class);
    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
    private boolean canProvidePower = true;
    /** List of blocks to update with redstone. */
    private final Set blocksNeedingUpdate = Sets.newHashSet();
    private static final String __OBFID = "CL_00000295";

    public BlockRedstoneWire()
    {
        super(Material.circuits);
        this.setDefaultState(this.blockState.getBaseState().withProperty(NORTH, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(EAST, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(SOUTH, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(WEST, BlockRedstoneWire.EnumAttachPosition.NONE).withProperty(POWER, Integer.valueOf(0)));
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        state = state.withProperty(WEST, this.getAttachPosition(worldIn, pos, EnumFacing.WEST));
        state = state.withProperty(EAST, this.getAttachPosition(worldIn, pos, EnumFacing.EAST));
        state = state.withProperty(NORTH, this.getAttachPosition(worldIn, pos, EnumFacing.NORTH));
        state = state.withProperty(SOUTH, this.getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
        return state;
    }

    private BlockRedstoneWire.EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction)
    {
        BlockPos blockpos1 = pos.offset(direction);
        Block block = worldIn.getBlockState(pos.offset(direction)).getBlock();

        if (!canRestoneConnect(worldIn, blockpos1, direction) && (block.isSolidFullCube() || !canRestoneConnect(worldIn, blockpos1.down(), null)))
        {
            Block block1 = worldIn.getBlockState(pos.up()).getBlock();
            return !block1.isSolidFullCube() && block.isSolidFullCube() && canRestoneConnect(worldIn, blockpos1.up(), null) ? BlockRedstoneWire.EnumAttachPosition.UP : BlockRedstoneWire.EnumAttachPosition.NONE;
        }
        else
        {
            return BlockRedstoneWire.EnumAttachPosition.SIDE;
        }
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
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        return iblockstate.getBlock() != this ? super.colorMultiplier(worldIn, pos, renderPass) : this.colorMultiplier(((Integer)iblockstate.getValue(POWER)).intValue());
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) || worldIn.getBlockState(pos.down()).getBlock() == Blocks.glowstone;
    }

    private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state)
    {
        state = this.calculateCurrentChanges(worldIn, pos, pos, state);
        ArrayList arraylist = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext())
        {
            BlockPos blockpos1 = (BlockPos)iterator.next();
            worldIn.notifyNeighborsOfStateChange(blockpos1, this);
        }

        return state;
    }

    private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state)
    {
        IBlockState iblockstate1 = state;
        int i = ((Integer)state.getValue(POWER)).intValue();
        byte b0 = 0;
        int l = this.getMaxCurrentStrength(worldIn, pos2, b0);
        this.canProvidePower = false;
        int j = worldIn.isBlockIndirectlyGettingPowered(pos1);
        this.canProvidePower = true;

        if (j > 0 && j > l - 1)
        {
            l = j;
        }

        int k = 0;
        Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

        while (iterator.hasNext())
        {
            EnumFacing enumfacing = (EnumFacing)iterator.next();
            BlockPos blockpos2 = pos1.offset(enumfacing);
            boolean flag = blockpos2.getX() != pos2.getX() || blockpos2.getZ() != pos2.getZ();

            if (flag)
            {
                k = this.getMaxCurrentStrength(worldIn, blockpos2, k);
            }

            if (worldIn.getBlockState(blockpos2).getBlock().isNormalCube() && !worldIn.getBlockState(pos1.up()).getBlock().isNormalCube())
            {
                if (flag && pos1.getY() >= pos2.getY())
                {
                    k = this.getMaxCurrentStrength(worldIn, blockpos2.up(), k);
                }
            }
            else if (!worldIn.getBlockState(blockpos2).getBlock().isNormalCube() && flag && pos1.getY() <= pos2.getY())
            {
                k = this.getMaxCurrentStrength(worldIn, blockpos2.down(), k);
            }
        }

        if (k > l)
        {
            l = k - 1;
        }
        else if (l > 0)
        {
            --l;
        }
        else
        {
            l = 0;
        }

        if (j > l - 1)
        {
            l = j;
        }

        if (i != l)
        {
            state = state.withProperty(POWER, Integer.valueOf(l));

            if (worldIn.getBlockState(pos1) == iblockstate1)
            {
                worldIn.setBlockState(pos1, state, 2);
            }

            this.blocksNeedingUpdate.add(pos1);
            EnumFacing[] aenumfacing = EnumFacing.values();
            int i1 = aenumfacing.length;

            for (int j1 = 0; j1 < i1; ++j1)
            {
                EnumFacing enumfacing1 = aenumfacing[j1];
                this.blocksNeedingUpdate.add(pos1.offset(enumfacing1));
            }
        }

        return state;
    }

    /**
     * Calls World.notifyNeighborsOfStateChange() for all neighboring blocks, but only if the given block is a redstone
     * wire.
     */
    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
    {
        if (worldIn.getBlockState(pos).getBlock() == this)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            EnumFacing[] aenumfacing = EnumFacing.values();
            int i = aenumfacing.length;

            for (int j = 0; j < i; ++j)
            {
                EnumFacing enumfacing = aenumfacing[j];
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }
        }
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            this.updateSurroundingRedstone(worldIn, pos, state);
            Iterator iterator = EnumFacing.Plane.VERTICAL.iterator();
            EnumFacing enumfacing;

            while (iterator.hasNext())
            {
                enumfacing = (EnumFacing)iterator.next();
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }

            iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext())
            {
                enumfacing = (EnumFacing)iterator.next();
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing));
            }

            iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext())
            {
                enumfacing = (EnumFacing)iterator.next();
                BlockPos blockpos1 = pos.offset(enumfacing);

                if (worldIn.getBlockState(blockpos1).getBlock().isNormalCube())
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos1.up());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos1.down());
                }
            }
        }
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (!worldIn.isRemote)
        {
            EnumFacing[] aenumfacing = EnumFacing.values();
            int i = aenumfacing.length;

            for (int j = 0; j < i; ++j)
            {
                EnumFacing enumfacing = aenumfacing[j];
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }

            this.updateSurroundingRedstone(worldIn, pos, state);
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();
            EnumFacing enumfacing1;

            while (iterator.hasNext())
            {
                enumfacing1 = (EnumFacing)iterator.next();
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext())
            {
                enumfacing1 = (EnumFacing)iterator.next();
                BlockPos blockpos1 = pos.offset(enumfacing1);

                if (worldIn.getBlockState(blockpos1).getBlock().isNormalCube())
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos1.up());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos1.down());
                }
            }
        }
    }

    private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength)
    {
        if (worldIn.getBlockState(pos).getBlock() != this)
        {
            return strength;
        }
        else
        {
            int j = ((Integer)worldIn.getBlockState(pos).getValue(POWER)).intValue();
            return j > strength ? j : strength;
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote)
        {
            if (this.canPlaceBlockAt(worldIn, pos))
            {
                this.updateSurroundingRedstone(worldIn, pos, state);
            }
            else
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     *  
     * @param fortune the level of the Fortune enchantment on the player's tool
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.redstone;
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return !this.canProvidePower ? 0 : this.isProvidingWeakPower(worldIn, pos, state, side);
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        if (!this.canProvidePower)
        {
            return 0;
        }
        else
        {
            int i = ((Integer)state.getValue(POWER)).intValue();

            if (i == 0)
            {
                return 0;
            }
            else if (side == EnumFacing.UP)
            {
                return i;
            }
            else
            {
                EnumSet enumset = EnumSet.noneOf(EnumFacing.class);
                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                while (iterator.hasNext())
                {
                    EnumFacing enumfacing1 = (EnumFacing)iterator.next();

                    if (this.func_176339_d(worldIn, pos, enumfacing1))
                    {
                        enumset.add(enumfacing1);
                    }
                }

                if (side.getAxis().isHorizontal() && enumset.isEmpty())
                {
                    return i;
                }
                else if (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY()))
                {
                    return i;
                }
                else
                {
                    return 0;
                }
            }
        }
    }

    private boolean func_176339_d(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        BlockPos blockpos1 = pos.offset(side);
        IBlockState iblockstate = worldIn.getBlockState(blockpos1);
        Block block = iblockstate.getBlock();
        boolean flag = block.isNormalCube();
        boolean flag1 = worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
        return !flag1 && flag && canRestoneConnect(worldIn, blockpos1.up(), null) ? true : (canRestoneConnect(worldIn, blockpos1, side) ? true : (block == Blocks.powered_repeater && iblockstate.getValue(BlockRedstoneDiode.FACING) == side ? true : !flag && canRestoneConnect(worldIn, blockpos1.down(), null)));
    }

    protected static boolean canRestoneConnect(IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() == Blocks.redstone_wire)
        {
            return true;
        }
        else if (Blocks.unpowered_repeater.isAssociated(state.getBlock()))
        {
            EnumFacing direction = (EnumFacing)state.getValue(BlockRedstoneRepeater.FACING);
            return direction == side || direction.getOpposite() == side;
        }
        else
        {
            return state.getBlock().canConnectRedstone(world, pos, side);
        }
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return this.canProvidePower;
    }

    @SideOnly(Side.CLIENT)
    private int colorMultiplier(int powerLevel)
    {
        float f = (float)powerLevel / 15.0F;
        float f1 = f * 0.6F + 0.4F;

        if (powerLevel == 0)
        {
            f1 = 0.3F;
        }

        float f2 = f * f * 0.7F - 0.5F;
        float f3 = f * f * 0.6F - 0.7F;

        if (f2 < 0.0F)
        {
            f2 = 0.0F;
        }

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        int j = MathHelper.clamp_int((int)(f1 * 255.0F), 0, 255);
        int k = MathHelper.clamp_int((int)(f2 * 255.0F), 0, 255);
        int l = MathHelper.clamp_int((int)(f3 * 255.0F), 0, 255);
        return -16777216 | j << 16 | k << 8 | l;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        int i = ((Integer)state.getValue(POWER)).intValue();

        if (i != 0)
        {
            double d0 = (double)pos.getX() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            double d1 = (double)((float)pos.getY() + 0.0625F);
            double d2 = (double)pos.getZ() + 0.5D + ((double)rand.nextFloat() - 0.5D) * 0.2D;
            float f = (float)i / 15.0F;
            float f1 = f * 0.6F + 0.4F;
            float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
            float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
            worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, (double)f1, (double)f2, (double)f3, new int[0]);
        }
    }

    @SideOnly(Side.CLIENT)
    public Item getItem(World worldIn, BlockPos pos)
    {
        return Items.redstone;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWER, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(POWER)).intValue();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {NORTH, EAST, SOUTH, WEST, POWER});
    }

    static enum EnumAttachPosition implements IStringSerializable
    {
        UP("up"),
        SIDE("side"),
        NONE("none");
        private final String name;

        private static final String __OBFID = "CL_00002070";

        private EnumAttachPosition(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.getName();
        }

        public String getName()
        {
            return this.name;
        }
    }
}