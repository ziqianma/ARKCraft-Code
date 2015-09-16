package net.minecraft.block;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockRailBase extends Block
{
    protected final boolean isPowered;
    private static final String __OBFID = "CL_00000195";

    public static boolean isRailBlock(World worldIn, BlockPos pos)
    {
        return isRailBlock(worldIn.getBlockState(pos));
    }

    public static boolean isRailBlock(IBlockState state)
    {
        Block block = state.getBlock();
        return block instanceof BlockRailBase;
    }

    protected BlockRailBase(boolean isPowered)
    {
        super(Material.circuits);
        this.isPowered = isPowered;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabTransport);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
     *  
     * @param start The start vector
     * @param end The end vector
     */
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        BlockRailBase.EnumRailDirection enumraildirection = iblockstate.getBlock() == this ? (BlockRailBase.EnumRailDirection)iblockstate.getValue(this.getShapeProperty()) : null;

        if (enumraildirection != null && enumraildirection.isAscending())
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        }
    }

    public boolean isFullCube()
    {
        return false;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            state = this.func_176564_a(worldIn, pos, state, true);

            if (this.isPowered)
            {
                this.onNeighborBlockChange(worldIn, pos, state, this);
            }
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        if (!worldIn.isRemote)
        {
            BlockRailBase.EnumRailDirection enumraildirection = (BlockRailBase.EnumRailDirection)state.getValue(this.getShapeProperty());
            boolean flag = false;

            if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()))
            {
                flag = true;
            }

            if (enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_EAST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.east()))
            {
                flag = true;
            }
            else if (enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_WEST && !World.doesBlockHaveSolidTopSurface(worldIn, pos.west()))
            {
                flag = true;
            }
            else if (enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_NORTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.north()))
            {
                flag = true;
            }
            else if (enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH && !World.doesBlockHaveSolidTopSurface(worldIn, pos.south()))
            {
                flag = true;
            }

            if (flag)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
            else
            {
                this.onNeighborChangedInternal(worldIn, pos, state, neighborBlock);
            }
        }
    }

    protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {}

    protected IBlockState func_176564_a(World worldIn, BlockPos p_176564_2_, IBlockState p_176564_3_, boolean p_176564_4_)
    {
        return worldIn.isRemote ? p_176564_3_ : (new BlockRailBase.Rail(worldIn, p_176564_2_, p_176564_3_)).func_180364_a(worldIn.isBlockPowered(p_176564_2_), p_176564_4_).getBlockState();
    }

    public int getMobilityFlag()
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (((BlockRailBase.EnumRailDirection)state.getValue(this.getShapeProperty())).isAscending())
        {
            worldIn.notifyNeighborsOfStateChange(pos.up(), this);
        }

        if (this.isPowered)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);
        }
    }

    public abstract IProperty getShapeProperty();
    /* ======================================== FORGE START =====================================*/
    /**
     * Return true if the rail can make corners.
     * Used by placement logic.
     * @param world The world.
     * @param pod Block's position in world
     * @return True if the rail can make corners.
     */
    public boolean isFlexibleRail(IBlockAccess world, BlockPos pos)
    {
        return !this.isPowered;
    }

    /**
     * Returns true if the rail can make up and down slopes.
     * Used by placement logic.
     * @param world The world.
     * @param pod Block's position in world
     * @return True if the rail can make slopes.
     */
    public boolean canMakeSlopes(IBlockAccess world, BlockPos pos)
    {
        return true;
    }

    /**
     * Returns the max speed of the rail at the specified position.
     * @param world The world.
     * @param cart The cart on the rail, may be null.
     * @param pod Block's position in world
     * @return The max speed of the current rail.
     */
    public float getRailMaxSpeed(World world, net.minecraft.entity.item.EntityMinecart cart, BlockPos pos)
    {
        return 0.4f;
    }

    /**
     * This function is called by any minecart that passes over this rail.
     * It is called once per update tick that the minecart is on the rail.
     * @param world The world.
     * @param cart The cart on the rail.
     * @param pod Block's position in world
     */
    public void onMinecartPass(World world, net.minecraft.entity.item.EntityMinecart cart, BlockPos pos)
    {
    }

    /**
     * Rotate the block. For vanilla blocks this rotates around the axis passed in (generally, it should be the "face" that was hit).
     * Note: for mod blocks, this is up to the block and modder to decide. It is not mandated that it be a rotation around the
     * face, but could be a rotation to orient *to* that face, or a visiting of possible rotations.
     * The method should return true if the rotation was successful though.
     *
     * @param world The world
     * @param pos Block position in world
     * @param axis The axis to rotate around
     * @return True if the rotation was successful, False if the rotation failed, or is not possible
     */
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        IBlockState state = world.getBlockState(pos);
        for (IProperty prop : (java.util.Set<IProperty>)state.getProperties().keySet())
        {
            if (prop.getName().equals("shape"))
            {
                world.setBlockState(pos, state.cycleProperty(prop));
                return true;
            }
        }
        return false;
    }

    /* ======================================== FORGE END =====================================*/

    public static enum EnumRailDirection implements IStringSerializable
    {
        NORTH_SOUTH(0, "north_south"),
        EAST_WEST(1, "east_west"),
        ASCENDING_EAST(2, "ascending_east"),
        ASCENDING_WEST(3, "ascending_west"),
        ASCENDING_NORTH(4, "ascending_north"),
        ASCENDING_SOUTH(5, "ascending_south"),
        SOUTH_EAST(6, "south_east"),
        SOUTH_WEST(7, "south_west"),
        NORTH_WEST(8, "north_west"),
        NORTH_EAST(9, "north_east");
        private static final BlockRailBase.EnumRailDirection[] META_LOOKUP = new BlockRailBase.EnumRailDirection[values().length];
        private final int meta;
        private final String name;

        private static final String __OBFID = "CL_00002137";

        private EnumRailDirection(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMetadata()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.name;
        }

        public boolean isAscending()
        {
            return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
        }

        public static BlockRailBase.EnumRailDirection byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.name;
        }

        static
        {
            BlockRailBase.EnumRailDirection[] var0 = values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2)
            {
                BlockRailBase.EnumRailDirection var3 = var0[var2];
                META_LOOKUP[var3.getMetadata()] = var3;
            }
        }
    }

    public class Rail
    {
        private final World world;
        private final BlockPos pos;
        private final BlockRailBase block;
        private IBlockState state;
        private final boolean isPowered;
        private final List field_150657_g = Lists.newArrayList();
        private static final String __OBFID = "CL_00000196";
        private final boolean canMakeSlopes;

        public Rail(World worldIn, BlockPos pos, IBlockState state)
        {
            this.world = worldIn;
            this.pos = pos;
            this.state = state;
            this.block = (BlockRailBase)state.getBlock();
            BlockRailBase.EnumRailDirection enumraildirection = (BlockRailBase.EnumRailDirection)state.getValue(BlockRailBase.this.getShapeProperty());
            this.isPowered = !this.block.isFlexibleRail(worldIn, pos);
            canMakeSlopes = this.block.canMakeSlopes(worldIn, pos);
            this.func_180360_a(enumraildirection);
        }

        private void func_180360_a(BlockRailBase.EnumRailDirection p_180360_1_)
        {
            this.field_150657_g.clear();

            switch (BlockRailBase.SwitchEnumRailDirection.DIRECTION_LOOKUP[p_180360_1_.ordinal()])
            {
                case 1:
                    this.field_150657_g.add(this.pos.north());
                    this.field_150657_g.add(this.pos.south());
                    break;
                case 2:
                    this.field_150657_g.add(this.pos.west());
                    this.field_150657_g.add(this.pos.east());
                    break;
                case 3:
                    this.field_150657_g.add(this.pos.west());
                    this.field_150657_g.add(this.pos.east().up());
                    break;
                case 4:
                    this.field_150657_g.add(this.pos.west().up());
                    this.field_150657_g.add(this.pos.east());
                    break;
                case 5:
                    this.field_150657_g.add(this.pos.north().up());
                    this.field_150657_g.add(this.pos.south());
                    break;
                case 6:
                    this.field_150657_g.add(this.pos.north());
                    this.field_150657_g.add(this.pos.south().up());
                    break;
                case 7:
                    this.field_150657_g.add(this.pos.east());
                    this.field_150657_g.add(this.pos.south());
                    break;
                case 8:
                    this.field_150657_g.add(this.pos.west());
                    this.field_150657_g.add(this.pos.south());
                    break;
                case 9:
                    this.field_150657_g.add(this.pos.west());
                    this.field_150657_g.add(this.pos.north());
                    break;
                case 10:
                    this.field_150657_g.add(this.pos.east());
                    this.field_150657_g.add(this.pos.north());
            }
        }

        private void func_150651_b()
        {
            for (int i = 0; i < this.field_150657_g.size(); ++i)
            {
                BlockRailBase.Rail rail = this.findRailAt((BlockPos)this.field_150657_g.get(i));

                if (rail != null && rail.func_150653_a(this))
                {
                    this.field_150657_g.set(i, rail.pos);
                }
                else
                {
                    this.field_150657_g.remove(i--);
                }
            }
        }

        private boolean hasRailAt(BlockPos pos)
        {
            return BlockRailBase.isRailBlock(this.world, pos) || BlockRailBase.isRailBlock(this.world, pos.up()) || BlockRailBase.isRailBlock(this.world, pos.down());
        }

        private BlockRailBase.Rail findRailAt(BlockPos pos)
        {
            IBlockState iblockstate = this.world.getBlockState(pos);

            if (BlockRailBase.isRailBlock(iblockstate))
            {
                return BlockRailBase.this.new Rail(this.world, pos, iblockstate);
            }
            else
            {
                BlockPos blockpos1 = pos.up();
                iblockstate = this.world.getBlockState(blockpos1);

                if (BlockRailBase.isRailBlock(iblockstate))
                {
                    return BlockRailBase.this.new Rail(this.world, blockpos1, iblockstate);
                }
                else
                {
                    blockpos1 = pos.down();
                    iblockstate = this.world.getBlockState(blockpos1);
                    return BlockRailBase.isRailBlock(iblockstate) ? BlockRailBase.this.new Rail(this.world, blockpos1, iblockstate) : null;
                }
            }
        }

        private boolean func_150653_a(BlockRailBase.Rail p_150653_1_)
        {
            return this.func_180363_c(p_150653_1_.pos);
        }

        private boolean func_180363_c(BlockPos p_180363_1_)
        {
            for (int i = 0; i < this.field_150657_g.size(); ++i)
            {
                BlockPos blockpos1 = (BlockPos)this.field_150657_g.get(i);

                if (blockpos1.getX() == p_180363_1_.getX() && blockpos1.getZ() == p_180363_1_.getZ())
                {
                    return true;
                }
            }

            return false;
        }

        /**
         * Counts the number of rails adjacent to this rail.
         */
        protected int countAdjacentRails()
        {
            int i = 0;
            Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

            while (iterator.hasNext())
            {
                EnumFacing enumfacing = (EnumFacing)iterator.next();

                if (this.hasRailAt(this.pos.offset(enumfacing)))
                {
                    ++i;
                }
            }

            return i;
        }

        private boolean func_150649_b(BlockRailBase.Rail p_150649_1_)
        {
            return this.func_150653_a(p_150649_1_) || this.field_150657_g.size() != 2;
        }

        private void func_150645_c(BlockRailBase.Rail p_150645_1_)
        {
            this.field_150657_g.add(p_150645_1_.pos);
            BlockPos blockpos = this.pos.north();
            BlockPos blockpos1 = this.pos.south();
            BlockPos blockpos2 = this.pos.west();
            BlockPos blockpos3 = this.pos.east();
            boolean flag = this.func_180363_c(blockpos);
            boolean flag1 = this.func_180363_c(blockpos1);
            boolean flag2 = this.func_180363_c(blockpos2);
            boolean flag3 = this.func_180363_c(blockpos3);
            BlockRailBase.EnumRailDirection enumraildirection = null;

            if (flag || flag1)
            {
                enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            if (flag2 || flag3)
            {
                enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
            }

            if (!this.isPowered)
            {
                if (flag1 && flag3 && !flag && !flag2)
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                }

                if (flag1 && flag2 && !flag && !flag3)
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                }

                if (flag && flag2 && !flag1 && !flag3)
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
                }

                if (flag && flag3 && !flag1 && !flag2)
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
                }
            }

            if (enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH && canMakeSlopes)
            {
                if (BlockRailBase.isRailBlock(this.world, blockpos.up()))
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                }

                if (BlockRailBase.isRailBlock(this.world, blockpos1.up()))
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST && canMakeSlopes)
            {
                if (BlockRailBase.isRailBlock(this.world, blockpos3.up()))
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                }

                if (BlockRailBase.isRailBlock(this.world, blockpos2.up()))
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (enumraildirection == null)
            {
                enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            this.state = this.state.withProperty(this.block.getShapeProperty(), enumraildirection);
            this.world.setBlockState(this.pos, this.state, 3);
        }

        private boolean func_180361_d(BlockPos p_180361_1_)
        {
            BlockRailBase.Rail rail = this.findRailAt(p_180361_1_);

            if (rail == null)
            {
                return false;
            }
            else
            {
                rail.func_150651_b();
                return rail.func_150649_b(this);
            }
        }

        public BlockRailBase.Rail func_180364_a(boolean p_180364_1_, boolean p_180364_2_)
        {
            BlockPos blockpos = this.pos.north();
            BlockPos blockpos1 = this.pos.south();
            BlockPos blockpos2 = this.pos.west();
            BlockPos blockpos3 = this.pos.east();
            boolean flag2 = this.func_180361_d(blockpos);
            boolean flag3 = this.func_180361_d(blockpos1);
            boolean flag4 = this.func_180361_d(blockpos2);
            boolean flag5 = this.func_180361_d(blockpos3);
            BlockRailBase.EnumRailDirection enumraildirection = null;

            if ((flag2 || flag3) && !flag4 && !flag5)
            {
                enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            if ((flag4 || flag5) && !flag2 && !flag3)
            {
                enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
            }

            if (!this.isPowered)
            {
                if (flag3 && flag5 && !flag2 && !flag4)
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                }

                if (flag3 && flag4 && !flag2 && !flag5)
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                }

                if (flag2 && flag4 && !flag3 && !flag5)
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
                }

                if (flag2 && flag5 && !flag3 && !flag4)
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
                }
            }

            if (enumraildirection == null)
            {
                if (flag2 || flag3)
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                }

                if (flag4 || flag5)
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
                }

                if (!this.isPowered)
                {
                    if (p_180364_1_)
                    {
                        if (flag3 && flag5)
                        {
                            enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                        }

                        if (flag4 && flag3)
                        {
                            enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                        }

                        if (flag5 && flag2)
                        {
                            enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
                        }

                        if (flag2 && flag4)
                        {
                            enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
                        }
                    }
                    else
                    {
                        if (flag2 && flag4)
                        {
                            enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
                        }

                        if (flag5 && flag2)
                        {
                            enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
                        }

                        if (flag4 && flag3)
                        {
                            enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
                        }

                        if (flag3 && flag5)
                        {
                            enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
                        }
                    }
                }
            }

            if (enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH && canMakeSlopes)
            {
                if (BlockRailBase.isRailBlock(this.world, blockpos.up()))
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
                }

                if (BlockRailBase.isRailBlock(this.world, blockpos1.up()))
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST && canMakeSlopes)
            {
                if (BlockRailBase.isRailBlock(this.world, blockpos3.up()))
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
                }

                if (BlockRailBase.isRailBlock(this.world, blockpos2.up()))
                {
                    enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (enumraildirection == null)
            {
                enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            this.func_180360_a(enumraildirection);
            this.state = this.state.withProperty(this.block.getShapeProperty(), enumraildirection);

            if (p_180364_2_ || this.world.getBlockState(this.pos) != this.state)
            {
                this.world.setBlockState(this.pos, this.state, 3);

                for (int i = 0; i < this.field_150657_g.size(); ++i)
                {
                    BlockRailBase.Rail rail = this.findRailAt((BlockPos)this.field_150657_g.get(i));

                    if (rail != null)
                    {
                        rail.func_150651_b();

                        if (rail.func_150649_b(this))
                        {
                            rail.func_150645_c(this);
                        }
                    }
                }
            }

            return this;
        }

        public IBlockState getBlockState()
        {
            return this.state;
        }
    }

    static final class SwitchEnumRailDirection
        {
            static final int[] DIRECTION_LOOKUP = new int[BlockRailBase.EnumRailDirection.values().length];
            private static final String __OBFID = "CL_00002138";

            static
            {
                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.NORTH_SOUTH.ordinal()] = 1;
                }
                catch (NoSuchFieldError var10)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.EAST_WEST.ordinal()] = 2;
                }
                catch (NoSuchFieldError var9)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_EAST.ordinal()] = 3;
                }
                catch (NoSuchFieldError var8)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_WEST.ordinal()] = 4;
                }
                catch (NoSuchFieldError var7)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_NORTH.ordinal()] = 5;
                }
                catch (NoSuchFieldError var6)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 6;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.SOUTH_EAST.ordinal()] = 7;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.SOUTH_WEST.ordinal()] = 8;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.NORTH_WEST.ordinal()] = 9;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.NORTH_EAST.ordinal()] = 10;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}