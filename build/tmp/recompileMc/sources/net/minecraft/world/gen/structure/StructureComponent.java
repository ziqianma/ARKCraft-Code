package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDoor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;

public abstract class StructureComponent
{
    protected StructureBoundingBox boundingBox;
    /** switches the Coordinate System base off the Bounding Box */
    protected EnumFacing coordBaseMode;
    /** The type ID of this component. */
    protected int componentType;
    private static final String __OBFID = "CL_00000511";

    public StructureComponent() {}

    protected StructureComponent(int p_i2091_1_)
    {
        this.componentType = p_i2091_1_;
    }

    public NBTTagCompound func_143010_b()
    {
        if (MapGenStructureIO.func_143036_a(this) == null) // Friendlier error then the Null String error below.
        {
            throw new RuntimeException("StructureComponent \"" + this.getClass().getName() + "\" missing ID Mapping, Modder see MapGenStructureIO");
        }
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setString("id", MapGenStructureIO.func_143036_a(this));
        nbttagcompound.setTag("BB", this.boundingBox.func_151535_h());
        nbttagcompound.setInteger("O", this.coordBaseMode == null ? -1 : this.coordBaseMode.getHorizontalIndex());
        nbttagcompound.setInteger("GD", this.componentType);
        this.writeStructureToNBT(nbttagcompound);
        return nbttagcompound;
    }

    /**
     * (abstract) Helper method to write subclass data to NBT
     */
    protected abstract void writeStructureToNBT(NBTTagCompound p_143012_1_);

    public void func_143009_a(World worldIn, NBTTagCompound p_143009_2_)
    {
        if (p_143009_2_.hasKey("BB"))
        {
            this.boundingBox = new StructureBoundingBox(p_143009_2_.getIntArray("BB"));
        }

        int i = p_143009_2_.getInteger("O");
        this.coordBaseMode = i == -1 ? null : EnumFacing.getHorizontal(i);
        this.componentType = p_143009_2_.getInteger("GD");
        this.readStructureFromNBT(p_143009_2_);
    }

    /**
     * (abstract) Helper method to read subclass data from NBT
     */
    protected abstract void readStructureFromNBT(NBTTagCompound p_143011_1_);

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent p_74861_1_, List p_74861_2_, Random p_74861_3_) {}

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public abstract boolean addComponentParts(World worldIn, Random p_74875_2_, StructureBoundingBox p_74875_3_);

    public StructureBoundingBox getBoundingBox()
    {
        return this.boundingBox;
    }

    /**
     * Returns the component type ID of this component.
     */
    public int getComponentType()
    {
        return this.componentType;
    }

    /**
     * Discover if bounding box can fit within the current bounding box object.
     */
    public static StructureComponent findIntersecting(List p_74883_0_, StructureBoundingBox p_74883_1_)
    {
        Iterator iterator = p_74883_0_.iterator();
        StructureComponent structurecomponent;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            structurecomponent = (StructureComponent)iterator.next();
        }
        while (structurecomponent.getBoundingBox() == null || !structurecomponent.getBoundingBox().intersectsWith(p_74883_1_));

        return structurecomponent;
    }

    public BlockPos func_180776_a()
    {
        return new BlockPos(this.boundingBox.func_180717_f());
    }

    /**
     * checks the entire StructureBoundingBox for Liquids
     */
    protected boolean isLiquidInStructureBoundingBox(World worldIn, StructureBoundingBox p_74860_2_)
    {
        int i = Math.max(this.boundingBox.minX - 1, p_74860_2_.minX);
        int j = Math.max(this.boundingBox.minY - 1, p_74860_2_.minY);
        int k = Math.max(this.boundingBox.minZ - 1, p_74860_2_.minZ);
        int l = Math.min(this.boundingBox.maxX + 1, p_74860_2_.maxX);
        int i1 = Math.min(this.boundingBox.maxY + 1, p_74860_2_.maxY);
        int j1 = Math.min(this.boundingBox.maxZ + 1, p_74860_2_.maxZ);
        int k1;
        int l1;

        for (k1 = i; k1 <= l; ++k1)
        {
            for (l1 = k; l1 <= j1; ++l1)
            {
                if (worldIn.getBlockState(new BlockPos(k1, j, l1)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }

                if (worldIn.getBlockState(new BlockPos(k1, i1, l1)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        for (k1 = i; k1 <= l; ++k1)
        {
            for (l1 = j; l1 <= i1; ++l1)
            {
                if (worldIn.getBlockState(new BlockPos(k1, l1, k)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }

                if (worldIn.getBlockState(new BlockPos(k1, l1, j1)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        for (k1 = k; k1 <= j1; ++k1)
        {
            for (l1 = j; l1 <= i1; ++l1)
            {
                if (worldIn.getBlockState(new BlockPos(i, l1, k1)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }

                if (worldIn.getBlockState(new BlockPos(l, l1, k1)).getBlock().getMaterial().isLiquid())
                {
                    return true;
                }
            }
        }

        return false;
    }

    protected int getXWithOffset(int p_74865_1_, int p_74865_2_)
    {
        if (this.coordBaseMode == null)
        {
            return p_74865_1_;
        }
        else
        {
            switch (StructureComponent.SwitchEnumFacing.field_176100_a[this.coordBaseMode.ordinal()])
            {
                case 1:
                case 2:
                    return this.boundingBox.minX + p_74865_1_;
                case 3:
                    return this.boundingBox.maxX - p_74865_2_;
                case 4:
                    return this.boundingBox.minX + p_74865_2_;
                default:
                    return p_74865_1_;
            }
        }
    }

    protected int getYWithOffset(int p_74862_1_)
    {
        return this.coordBaseMode == null ? p_74862_1_ : p_74862_1_ + this.boundingBox.minY;
    }

    protected int getZWithOffset(int p_74873_1_, int p_74873_2_)
    {
        if (this.coordBaseMode == null)
        {
            return p_74873_2_;
        }
        else
        {
            switch (StructureComponent.SwitchEnumFacing.field_176100_a[this.coordBaseMode.ordinal()])
            {
                case 1:
                    return this.boundingBox.maxZ - p_74873_2_;
                case 2:
                    return this.boundingBox.minZ + p_74873_2_;
                case 3:
                case 4:
                    return this.boundingBox.minZ + p_74873_1_;
                default:
                    return p_74873_2_;
            }
        }
    }

    /**
     * Returns the direction-shifted metadata for blocks that require orientation, e.g. doors, stairs, ladders.
     */
    protected int getMetadataWithOffset(Block p_151555_1_, int p_151555_2_)
    {
        if (p_151555_1_ == Blocks.rail)
        {
            if (this.coordBaseMode == EnumFacing.WEST || this.coordBaseMode == EnumFacing.EAST)
            {
                if (p_151555_2_ == 1)
                {
                    return 0;
                }

                return 1;
            }
        }
        else if (p_151555_1_ instanceof BlockDoor)
        {
            if (this.coordBaseMode == EnumFacing.SOUTH)
            {
                if (p_151555_2_ == 0)
                {
                    return 2;
                }

                if (p_151555_2_ == 2)
                {
                    return 0;
                }
            }
            else
            {
                if (this.coordBaseMode == EnumFacing.WEST)
                {
                    return p_151555_2_ + 1 & 3;
                }

                if (this.coordBaseMode == EnumFacing.EAST)
                {
                    return p_151555_2_ + 3 & 3;
                }
            }
        }
        else if (p_151555_1_ != Blocks.stone_stairs && p_151555_1_ != Blocks.oak_stairs && p_151555_1_ != Blocks.nether_brick_stairs && p_151555_1_ != Blocks.stone_brick_stairs && p_151555_1_ != Blocks.sandstone_stairs)
        {
            if (p_151555_1_ == Blocks.ladder)
            {
                if (this.coordBaseMode == EnumFacing.SOUTH)
                {
                    if (p_151555_2_ == EnumFacing.NORTH.getIndex())
                    {
                        return EnumFacing.SOUTH.getIndex();
                    }

                    if (p_151555_2_ == EnumFacing.SOUTH.getIndex())
                    {
                        return EnumFacing.NORTH.getIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.WEST)
                {
                    if (p_151555_2_ == EnumFacing.NORTH.getIndex())
                    {
                        return EnumFacing.WEST.getIndex();
                    }

                    if (p_151555_2_ == EnumFacing.SOUTH.getIndex())
                    {
                        return EnumFacing.EAST.getIndex();
                    }

                    if (p_151555_2_ == EnumFacing.WEST.getIndex())
                    {
                        return EnumFacing.NORTH.getIndex();
                    }

                    if (p_151555_2_ == EnumFacing.EAST.getIndex())
                    {
                        return EnumFacing.SOUTH.getIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.EAST)
                {
                    if (p_151555_2_ == EnumFacing.NORTH.getIndex())
                    {
                        return EnumFacing.EAST.getIndex();
                    }

                    if (p_151555_2_ == EnumFacing.SOUTH.getIndex())
                    {
                        return EnumFacing.WEST.getIndex();
                    }

                    if (p_151555_2_ == EnumFacing.WEST.getIndex())
                    {
                        return EnumFacing.NORTH.getIndex();
                    }

                    if (p_151555_2_ == EnumFacing.EAST.getIndex())
                    {
                        return EnumFacing.SOUTH.getIndex();
                    }
                }
            }
            else if (p_151555_1_ == Blocks.stone_button)
            {
                if (this.coordBaseMode == EnumFacing.SOUTH)
                {
                    if (p_151555_2_ == 3)
                    {
                        return 4;
                    }

                    if (p_151555_2_ == 4)
                    {
                        return 3;
                    }
                }
                else if (this.coordBaseMode == EnumFacing.WEST)
                {
                    if (p_151555_2_ == 3)
                    {
                        return 1;
                    }

                    if (p_151555_2_ == 4)
                    {
                        return 2;
                    }

                    if (p_151555_2_ == 2)
                    {
                        return 3;
                    }

                    if (p_151555_2_ == 1)
                    {
                        return 4;
                    }
                }
                else if (this.coordBaseMode == EnumFacing.EAST)
                {
                    if (p_151555_2_ == 3)
                    {
                        return 2;
                    }

                    if (p_151555_2_ == 4)
                    {
                        return 1;
                    }

                    if (p_151555_2_ == 2)
                    {
                        return 3;
                    }

                    if (p_151555_2_ == 1)
                    {
                        return 4;
                    }
                }
            }
            else if (p_151555_1_ != Blocks.tripwire_hook && !(p_151555_1_ instanceof BlockDirectional))
            {
                if (p_151555_1_ == Blocks.piston || p_151555_1_ == Blocks.sticky_piston || p_151555_1_ == Blocks.lever || p_151555_1_ == Blocks.dispenser)
                {
                    if (this.coordBaseMode == EnumFacing.SOUTH)
                    {
                        if (p_151555_2_ == EnumFacing.NORTH.getIndex() || p_151555_2_ == EnumFacing.SOUTH.getIndex())
                        {
                            return EnumFacing.getFront(p_151555_2_).getOpposite().getIndex();
                        }
                    }
                    else if (this.coordBaseMode == EnumFacing.WEST)
                    {
                        if (p_151555_2_ == EnumFacing.NORTH.getIndex())
                        {
                            return EnumFacing.WEST.getIndex();
                        }

                        if (p_151555_2_ == EnumFacing.SOUTH.getIndex())
                        {
                            return EnumFacing.EAST.getIndex();
                        }

                        if (p_151555_2_ == EnumFacing.WEST.getIndex())
                        {
                            return EnumFacing.NORTH.getIndex();
                        }

                        if (p_151555_2_ == EnumFacing.EAST.getIndex())
                        {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    }
                    else if (this.coordBaseMode == EnumFacing.EAST)
                    {
                        if (p_151555_2_ == EnumFacing.NORTH.getIndex())
                        {
                            return EnumFacing.EAST.getIndex();
                        }

                        if (p_151555_2_ == EnumFacing.SOUTH.getIndex())
                        {
                            return EnumFacing.WEST.getIndex();
                        }

                        if (p_151555_2_ == EnumFacing.WEST.getIndex())
                        {
                            return EnumFacing.NORTH.getIndex();
                        }

                        if (p_151555_2_ == EnumFacing.EAST.getIndex())
                        {
                            return EnumFacing.SOUTH.getIndex();
                        }
                    }
                }
            }
            else
            {
                EnumFacing enumfacing = EnumFacing.getHorizontal(p_151555_2_);

                if (this.coordBaseMode == EnumFacing.SOUTH)
                {
                    if (enumfacing == EnumFacing.SOUTH || enumfacing == EnumFacing.NORTH)
                    {
                        return enumfacing.getOpposite().getHorizontalIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.WEST)
                {
                    if (enumfacing == EnumFacing.NORTH)
                    {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }

                    if (enumfacing == EnumFacing.SOUTH)
                    {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }

                    if (enumfacing == EnumFacing.WEST)
                    {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }

                    if (enumfacing == EnumFacing.EAST)
                    {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                }
                else if (this.coordBaseMode == EnumFacing.EAST)
                {
                    if (enumfacing == EnumFacing.NORTH)
                    {
                        return EnumFacing.EAST.getHorizontalIndex();
                    }

                    if (enumfacing == EnumFacing.SOUTH)
                    {
                        return EnumFacing.WEST.getHorizontalIndex();
                    }

                    if (enumfacing == EnumFacing.WEST)
                    {
                        return EnumFacing.NORTH.getHorizontalIndex();
                    }

                    if (enumfacing == EnumFacing.EAST)
                    {
                        return EnumFacing.SOUTH.getHorizontalIndex();
                    }
                }
            }
        }
        else if (this.coordBaseMode == EnumFacing.SOUTH)
        {
            if (p_151555_2_ == 2)
            {
                return 3;
            }

            if (p_151555_2_ == 3)
            {
                return 2;
            }
        }
        else if (this.coordBaseMode == EnumFacing.WEST)
        {
            if (p_151555_2_ == 0)
            {
                return 2;
            }

            if (p_151555_2_ == 1)
            {
                return 3;
            }

            if (p_151555_2_ == 2)
            {
                return 0;
            }

            if (p_151555_2_ == 3)
            {
                return 1;
            }
        }
        else if (this.coordBaseMode == EnumFacing.EAST)
        {
            if (p_151555_2_ == 0)
            {
                return 2;
            }

            if (p_151555_2_ == 1)
            {
                return 3;
            }

            if (p_151555_2_ == 2)
            {
                return 1;
            }

            if (p_151555_2_ == 3)
            {
                return 0;
            }
        }

        return p_151555_2_;
    }

    protected void func_175811_a(World worldIn, IBlockState p_175811_2_, int p_175811_3_, int p_175811_4_, int p_175811_5_, StructureBoundingBox p_175811_6_)
    {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(p_175811_3_, p_175811_5_), this.getYWithOffset(p_175811_4_), this.getZWithOffset(p_175811_3_, p_175811_5_));

        if (p_175811_6_.func_175898_b(blockpos))
        {
            worldIn.setBlockState(blockpos, p_175811_2_, 2);
        }
    }

    protected IBlockState func_175807_a(World worldIn, int p_175807_2_, int p_175807_3_, int p_175807_4_, StructureBoundingBox p_175807_5_)
    {
        int l = this.getXWithOffset(p_175807_2_, p_175807_4_);
        int i1 = this.getYWithOffset(p_175807_3_);
        int j1 = this.getZWithOffset(p_175807_2_, p_175807_4_);
        return !p_175807_5_.func_175898_b(new BlockPos(l, i1, j1)) ? Blocks.air.getDefaultState() : worldIn.getBlockState(new BlockPos(l, i1, j1));
    }

    /**
     * arguments: (World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ)
     */
    protected void fillWithAir(World worldIn, StructureBoundingBox p_74878_2_, int p_74878_3_, int p_74878_4_, int p_74878_5_, int p_74878_6_, int p_74878_7_, int p_74878_8_)
    {
        for (int k1 = p_74878_4_; k1 <= p_74878_7_; ++k1)
        {
            for (int l1 = p_74878_3_; l1 <= p_74878_6_; ++l1)
            {
                for (int i2 = p_74878_5_; i2 <= p_74878_8_; ++i2)
                {
                    this.func_175811_a(worldIn, Blocks.air.getDefaultState(), l1, k1, i2, p_74878_2_);
                }
            }
        }
    }

    protected void func_175804_a(World worldIn, StructureBoundingBox p_175804_2_, int p_175804_3_, int p_175804_4_, int p_175804_5_, int p_175804_6_, int p_175804_7_, int p_175804_8_, IBlockState p_175804_9_, IBlockState p_175804_10_, boolean p_175804_11_)
    {
        for (int k1 = p_175804_4_; k1 <= p_175804_7_; ++k1)
        {
            for (int l1 = p_175804_3_; l1 <= p_175804_6_; ++l1)
            {
                for (int i2 = p_175804_5_; i2 <= p_175804_8_; ++i2)
                {
                    if (!p_175804_11_ || this.func_175807_a(worldIn, l1, k1, i2, p_175804_2_).getBlock().getMaterial() != Material.air)
                    {
                        if (k1 != p_175804_4_ && k1 != p_175804_7_ && l1 != p_175804_3_ && l1 != p_175804_6_ && i2 != p_175804_5_ && i2 != p_175804_8_)
                        {
                            this.func_175811_a(worldIn, p_175804_10_, l1, k1, i2, p_175804_2_);
                        }
                        else
                        {
                            this.func_175811_a(worldIn, p_175804_9_, l1, k1, i2, p_175804_2_);
                        }
                    }
                }
            }
        }
    }

    /**
     * arguments: World worldObj, StructureBoundingBox structBB, int minX, int minY, int minZ, int maxX, int maxY, int
     * maxZ, boolean alwaysreplace, Random rand, StructurePieceBlockSelector blockselector
     */
    protected void fillWithRandomizedBlocks(World worldIn, StructureBoundingBox p_74882_2_, int p_74882_3_, int p_74882_4_, int p_74882_5_, int p_74882_6_, int p_74882_7_, int p_74882_8_, boolean p_74882_9_, Random p_74882_10_, StructureComponent.BlockSelector p_74882_11_)
    {
        for (int k1 = p_74882_4_; k1 <= p_74882_7_; ++k1)
        {
            for (int l1 = p_74882_3_; l1 <= p_74882_6_; ++l1)
            {
                for (int i2 = p_74882_5_; i2 <= p_74882_8_; ++i2)
                {
                    if (!p_74882_9_ || this.func_175807_a(worldIn, l1, k1, i2, p_74882_2_).getBlock().getMaterial() != Material.air)
                    {
                        p_74882_11_.selectBlocks(p_74882_10_, l1, k1, i2, k1 == p_74882_4_ || k1 == p_74882_7_ || l1 == p_74882_3_ || l1 == p_74882_6_ || i2 == p_74882_5_ || i2 == p_74882_8_);
                        this.func_175811_a(worldIn, p_74882_11_.func_180780_a(), l1, k1, i2, p_74882_2_);
                    }
                }
            }
        }
    }

    protected void func_175805_a(World worldIn, StructureBoundingBox p_175805_2_, Random p_175805_3_, float p_175805_4_, int p_175805_5_, int p_175805_6_, int p_175805_7_, int p_175805_8_, int p_175805_9_, int p_175805_10_, IBlockState p_175805_11_, IBlockState p_175805_12_, boolean p_175805_13_)
    {
        for (int k1 = p_175805_6_; k1 <= p_175805_9_; ++k1)
        {
            for (int l1 = p_175805_5_; l1 <= p_175805_8_; ++l1)
            {
                for (int i2 = p_175805_7_; i2 <= p_175805_10_; ++i2)
                {
                    if (p_175805_3_.nextFloat() <= p_175805_4_ && (!p_175805_13_ || this.func_175807_a(worldIn, l1, k1, i2, p_175805_2_).getBlock().getMaterial() != Material.air))
                    {
                        if (k1 != p_175805_6_ && k1 != p_175805_9_ && l1 != p_175805_5_ && l1 != p_175805_8_ && i2 != p_175805_7_ && i2 != p_175805_10_)
                        {
                            this.func_175811_a(worldIn, p_175805_12_, l1, k1, i2, p_175805_2_);
                        }
                        else
                        {
                            this.func_175811_a(worldIn, p_175805_11_, l1, k1, i2, p_175805_2_);
                        }
                    }
                }
            }
        }
    }

    protected void func_175809_a(World worldIn, StructureBoundingBox p_175809_2_, Random p_175809_3_, float p_175809_4_, int p_175809_5_, int p_175809_6_, int p_175809_7_, IBlockState p_175809_8_)
    {
        if (p_175809_3_.nextFloat() < p_175809_4_)
        {
            this.func_175811_a(worldIn, p_175809_8_, p_175809_5_, p_175809_6_, p_175809_7_, p_175809_2_);
        }
    }

    protected void func_180777_a(World worldIn, StructureBoundingBox p_180777_2_, int p_180777_3_, int p_180777_4_, int p_180777_5_, int p_180777_6_, int p_180777_7_, int p_180777_8_, IBlockState p_180777_9_, boolean p_180777_10_)
    {
        float f = (float)(p_180777_6_ - p_180777_3_ + 1);
        float f1 = (float)(p_180777_7_ - p_180777_4_ + 1);
        float f2 = (float)(p_180777_8_ - p_180777_5_ + 1);
        float f3 = (float)p_180777_3_ + f / 2.0F;
        float f4 = (float)p_180777_5_ + f2 / 2.0F;

        for (int k1 = p_180777_4_; k1 <= p_180777_7_; ++k1)
        {
            float f5 = (float)(k1 - p_180777_4_) / f1;

            for (int l1 = p_180777_3_; l1 <= p_180777_6_; ++l1)
            {
                float f6 = ((float)l1 - f3) / (f * 0.5F);

                for (int i2 = p_180777_5_; i2 <= p_180777_8_; ++i2)
                {
                    float f7 = ((float)i2 - f4) / (f2 * 0.5F);

                    if (!p_180777_10_ || this.func_175807_a(worldIn, l1, k1, i2, p_180777_2_).getBlock().getMaterial() != Material.air)
                    {
                        float f8 = f6 * f6 + f5 * f5 + f7 * f7;

                        if (f8 <= 1.05F)
                        {
                            this.func_175811_a(worldIn, p_180777_9_, l1, k1, i2, p_180777_2_);
                        }
                    }
                }
            }
        }
    }

    /**
     * Deletes all continuous blocks from selected position upwards. Stops at hitting air.
     */
    protected void clearCurrentPositionBlocksUpwards(World worldIn, int p_74871_2_, int p_74871_3_, int p_74871_4_, StructureBoundingBox p_74871_5_)
    {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(p_74871_2_, p_74871_4_), this.getYWithOffset(p_74871_3_), this.getZWithOffset(p_74871_2_, p_74871_4_));

        if (p_74871_5_.func_175898_b(blockpos))
        {
            while (!worldIn.isAirBlock(blockpos) && blockpos.getY() < 255)
            {
                worldIn.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
                blockpos = blockpos.up();
            }
        }
    }

    protected void func_175808_b(World worldIn, IBlockState p_175808_2_, int p_175808_3_, int p_175808_4_, int p_175808_5_, StructureBoundingBox p_175808_6_)
    {
        int l = this.getXWithOffset(p_175808_3_, p_175808_5_);
        int i1 = this.getYWithOffset(p_175808_4_);
        int j1 = this.getZWithOffset(p_175808_3_, p_175808_5_);

        if (p_175808_6_.func_175898_b(new BlockPos(l, i1, j1)))
        {
            while ((worldIn.isAirBlock(new BlockPos(l, i1, j1)) || worldIn.getBlockState(new BlockPos(l, i1, j1)).getBlock().getMaterial().isLiquid()) && i1 > 1)
            {
                worldIn.setBlockState(new BlockPos(l, i1, j1), p_175808_2_, 2);
                --i1;
            }
        }
    }

    protected boolean func_180778_a(World worldIn, StructureBoundingBox p_180778_2_, Random p_180778_3_, int p_180778_4_, int p_180778_5_, int p_180778_6_, List p_180778_7_, int p_180778_8_)
    {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(p_180778_4_, p_180778_6_), this.getYWithOffset(p_180778_5_), this.getZWithOffset(p_180778_4_, p_180778_6_));

        if (p_180778_2_.func_175898_b(blockpos) && worldIn.getBlockState(blockpos).getBlock() != Blocks.chest)
        {
            IBlockState iblockstate = Blocks.chest.getDefaultState();
            worldIn.setBlockState(blockpos, Blocks.chest.correctFacing(worldIn, blockpos, iblockstate), 2);
            TileEntity tileentity = worldIn.getTileEntity(blockpos);

            if (tileentity instanceof TileEntityChest)
            {
                WeightedRandomChestContent.generateChestContents(p_180778_3_, p_180778_7_, (TileEntityChest)tileentity, p_180778_8_);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean func_175806_a(World worldIn, StructureBoundingBox p_175806_2_, Random p_175806_3_, int p_175806_4_, int p_175806_5_, int p_175806_6_, int p_175806_7_, List p_175806_8_, int p_175806_9_)
    {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(p_175806_4_, p_175806_6_), this.getYWithOffset(p_175806_5_), this.getZWithOffset(p_175806_4_, p_175806_6_));

        if (p_175806_2_.func_175898_b(blockpos) && worldIn.getBlockState(blockpos).getBlock() != Blocks.dispenser)
        {
            worldIn.setBlockState(blockpos, Blocks.dispenser.getStateFromMeta(this.getMetadataWithOffset(Blocks.dispenser, p_175806_7_)), 2);
            TileEntity tileentity = worldIn.getTileEntity(blockpos);

            if (tileentity instanceof TileEntityDispenser)
            {
                WeightedRandomChestContent.generateDispenserContents(p_175806_3_, p_175806_8_, (TileEntityDispenser)tileentity, p_175806_9_);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected void func_175810_a(World worldIn, StructureBoundingBox p_175810_2_, Random p_175810_3_, int p_175810_4_, int p_175810_5_, int p_175810_6_, EnumFacing p_175810_7_)
    {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(p_175810_4_, p_175810_6_), this.getYWithOffset(p_175810_5_), this.getZWithOffset(p_175810_4_, p_175810_6_));

        if (p_175810_2_.func_175898_b(blockpos))
        {
            ItemDoor.placeDoor(worldIn, blockpos, p_175810_7_.rotateYCCW(), Blocks.oak_door);
        }
    }

    public abstract static class BlockSelector
        {
            protected IBlockState field_151562_a;
            private static final String __OBFID = "CL_00000512";

            protected BlockSelector()
            {
                this.field_151562_a = Blocks.air.getDefaultState();
            }

            /**
             * picks Block Ids and Metadata (Silverfish)
             */
            public abstract void selectBlocks(Random p_75062_1_, int p_75062_2_, int p_75062_3_, int p_75062_4_, boolean p_75062_5_);

            public IBlockState func_180780_a()
            {
                return this.field_151562_a;
            }
        }

    static final class SwitchEnumFacing
        {
            static final int[] field_176100_a = new int[EnumFacing.values().length];
            private static final String __OBFID = "CL_00001969";

            static
            {
                try
                {
                    field_176100_a[EnumFacing.NORTH.ordinal()] = 1;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    field_176100_a[EnumFacing.SOUTH.ordinal()] = 2;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_176100_a[EnumFacing.WEST.ordinal()] = 3;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_176100_a[EnumFacing.EAST.ordinal()] = 4;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}