package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockRailPowered extends BlockRailBase
{
    public static final PropertyEnum SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate()
    {
        private static final String __OBFID = "CL_00002080";
        public boolean apply(BlockRailBase.EnumRailDirection direction)
        {
            return direction != BlockRailBase.EnumRailDirection.NORTH_EAST && direction != BlockRailBase.EnumRailDirection.NORTH_WEST && direction != BlockRailBase.EnumRailDirection.SOUTH_EAST && direction != BlockRailBase.EnumRailDirection.SOUTH_WEST;
        }
        public boolean apply(Object p_apply_1_)
        {
            return this.apply((BlockRailBase.EnumRailDirection)p_apply_1_);
        }
    });
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    private static final String __OBFID = "CL_00000288";

    protected BlockRailPowered()
    {
        super(true);
        this.setDefaultState(this.blockState.getBaseState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH).withProperty(POWERED, Boolean.valueOf(false)));
    }

    protected boolean func_176566_a(World worldIn, BlockPos pos, IBlockState state, boolean p_176566_4_, int p_176566_5_)
    {
        if (p_176566_5_ >= 8)
        {
            return false;
        }
        else
        {
            int j = pos.getX();
            int k = pos.getY();
            int l = pos.getZ();
            boolean flag1 = true;
            BlockRailBase.EnumRailDirection enumraildirection = (BlockRailBase.EnumRailDirection)state.getValue(SHAPE);

            switch (BlockRailPowered.SwitchEnumRailDirection.DIRECTION_LOOKUP[enumraildirection.ordinal()])
            {
                case 1:
                    if (p_176566_4_)
                    {
                        ++l;
                    }
                    else
                    {
                        --l;
                    }

                    break;
                case 2:
                    if (p_176566_4_)
                    {
                        --j;
                    }
                    else
                    {
                        ++j;
                    }

                    break;
                case 3:
                    if (p_176566_4_)
                    {
                        --j;
                    }
                    else
                    {
                        ++j;
                        ++k;
                        flag1 = false;
                    }

                    enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
                    break;
                case 4:
                    if (p_176566_4_)
                    {
                        --j;
                        ++k;
                        flag1 = false;
                    }
                    else
                    {
                        ++j;
                    }

                    enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
                    break;
                case 5:
                    if (p_176566_4_)
                    {
                        ++l;
                    }
                    else
                    {
                        --l;
                        ++k;
                        flag1 = false;
                    }

                    enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
                    break;
                case 6:
                    if (p_176566_4_)
                    {
                        ++l;
                        ++k;
                        flag1 = false;
                    }
                    else
                    {
                        --l;
                    }

                    enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
            }

            return this.func_176567_a(worldIn, new BlockPos(j, k, l), p_176566_4_, p_176566_5_, enumraildirection) ? true : flag1 && this.func_176567_a(worldIn, new BlockPos(j, k - 1, l), p_176566_4_, p_176566_5_, enumraildirection);
        }
    }

    protected boolean func_176567_a(World worldIn, BlockPos p_176567_2_, boolean p_176567_3_, int distance, BlockRailBase.EnumRailDirection p_176567_5_)
    {
        IBlockState iblockstate = worldIn.getBlockState(p_176567_2_);

        if (iblockstate.getBlock() != this)
        {
            return false;
        }
        else
        {
            BlockRailBase.EnumRailDirection enumraildirection = (BlockRailBase.EnumRailDirection)iblockstate.getValue(SHAPE);
            return p_176567_5_ == BlockRailBase.EnumRailDirection.EAST_WEST && (enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH || enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_NORTH || enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_SOUTH) ? false : (p_176567_5_ == BlockRailBase.EnumRailDirection.NORTH_SOUTH && (enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST || enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_EAST || enumraildirection == BlockRailBase.EnumRailDirection.ASCENDING_WEST) ? false : (((Boolean)iblockstate.getValue(POWERED)).booleanValue() ? (worldIn.isBlockPowered(p_176567_2_) ? true : this.func_176566_a(worldIn, p_176567_2_, iblockstate, p_176567_3_, distance + 1)) : false));
        }
    }

    protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
    {
        boolean flag = ((Boolean)state.getValue(POWERED)).booleanValue();
        boolean flag1 = worldIn.isBlockPowered(pos) || this.func_176566_a(worldIn, pos, state, true, 0) || this.func_176566_a(worldIn, pos, state, false, 0);

        if (flag1 != flag)
        {
            worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(flag1)), 3);
            worldIn.notifyNeighborsOfStateChange(pos.down(), this);

            if (((BlockRailBase.EnumRailDirection)state.getValue(SHAPE)).isAscending())
            {
                worldIn.notifyNeighborsOfStateChange(pos.up(), this);
            }
        }
    }

    public IProperty getShapeProperty()
    {
        return SHAPE;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta & 7)).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        byte b0 = 0;
        int i = b0 | ((BlockRailBase.EnumRailDirection)state.getValue(SHAPE)).getMetadata();

        if (((Boolean)state.getValue(POWERED)).booleanValue())
        {
            i |= 8;
        }

        return i;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {SHAPE, POWERED});
    }

    static final class SwitchEnumRailDirection
        {
            static final int[] DIRECTION_LOOKUP = new int[BlockRailBase.EnumRailDirection.values().length];
            private static final String __OBFID = "CL_00002079";

            static
            {
                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.NORTH_SOUTH.ordinal()] = 1;
                }
                catch (NoSuchFieldError var6)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.EAST_WEST.ordinal()] = 2;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_EAST.ordinal()] = 3;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_WEST.ordinal()] = 4;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_NORTH.ordinal()] = 5;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    DIRECTION_LOOKUP[BlockRailBase.EnumRailDirection.ASCENDING_SOUTH.ordinal()] = 6;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}