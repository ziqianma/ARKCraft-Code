package net.minecraft.world.gen.feature;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class WorldGenBigTree extends WorldGenAbstractTree
{
    private Random field_175949_k;
    private World world;
    private BlockPos field_175947_m;
    int heightLimit;
    int height;
    double heightAttenuation;
    double field_175944_d;
    double field_175945_e;
    double leafDensity;
    int field_175943_g;
    int field_175950_h;
    /** Sets the distance limit for how far away the generator will populate leaves from the base leaf node. */
    int leafDistanceLimit;
    List field_175948_j;
    private static final String __OBFID = "CL_00000400";

    public WorldGenBigTree(boolean p_i2008_1_)
    {
        super(p_i2008_1_);
        this.field_175947_m = BlockPos.ORIGIN;
        this.heightAttenuation = 0.618D;
        this.field_175944_d = 0.381D;
        this.field_175945_e = 1.0D;
        this.leafDensity = 1.0D;
        this.field_175943_g = 1;
        this.field_175950_h = 12;
        this.leafDistanceLimit = 4;
    }

    /**
     * Generates a list of leaf nodes for the tree, to be populated by generateLeaves.
     */
    void generateLeafNodeList()
    {
        this.height = (int)((double)this.heightLimit * this.heightAttenuation);

        if (this.height >= this.heightLimit)
        {
            this.height = this.heightLimit - 1;
        }

        int i = (int)(1.382D + Math.pow(this.leafDensity * (double)this.heightLimit / 13.0D, 2.0D));

        if (i < 1)
        {
            i = 1;
        }

        int j = this.field_175947_m.getY() + this.height;
        int k = this.heightLimit - this.leafDistanceLimit;
        this.field_175948_j = Lists.newArrayList();
        this.field_175948_j.add(new WorldGenBigTree.FoliageCoordinates(this.field_175947_m.up(k), j));

        for (; k >= 0; --k)
        {
            float f = this.layerSize(k);

            if (f >= 0.0F)
            {
                for (int l = 0; l < i; ++l)
                {
                    double d0 = this.field_175945_e * (double)f * ((double)this.field_175949_k.nextFloat() + 0.328D);
                    double d1 = (double)(this.field_175949_k.nextFloat() * 2.0F) * Math.PI;
                    double d2 = d0 * Math.sin(d1) + 0.5D;
                    double d3 = d0 * Math.cos(d1) + 0.5D;
                    BlockPos blockpos = this.field_175947_m.add(d2, (double)(k - 1), d3);
                    BlockPos blockpos1 = blockpos.up(this.leafDistanceLimit);

                    if (this.func_175936_a(blockpos, blockpos1) == -1)
                    {
                        int i1 = this.field_175947_m.getX() - blockpos.getX();
                        int j1 = this.field_175947_m.getZ() - blockpos.getZ();
                        double d4 = (double)blockpos.getY() - Math.sqrt((double)(i1 * i1 + j1 * j1)) * this.field_175944_d;
                        int k1 = d4 > (double)j ? j : (int)d4;
                        BlockPos blockpos2 = new BlockPos(this.field_175947_m.getX(), k1, this.field_175947_m.getZ());

                        if (this.func_175936_a(blockpos2, blockpos) == -1)
                        {
                            this.field_175948_j.add(new WorldGenBigTree.FoliageCoordinates(blockpos, blockpos2.getY()));
                        }
                    }
                }
            }
        }
    }

    void func_180712_a(BlockPos p_180712_1_, float p_180712_2_, Block p_180712_3_)
    {
        int i = (int)((double)p_180712_2_ + 0.618D);

        for (int j = -i; j <= i; ++j)
        {
            for (int k = -i; k <= i; ++k)
            {
                if (Math.pow((double)Math.abs(j) + 0.5D, 2.0D) + Math.pow((double)Math.abs(k) + 0.5D, 2.0D) <= (double)(p_180712_2_ * p_180712_2_))
                {
                    BlockPos blockpos1 = p_180712_1_.add(j, 0, k);
                    net.minecraft.block.state.IBlockState state = this.world.getBlockState(blockpos1);

                    if (state.getBlock().isAir(this.world, blockpos1) || state.getBlock().isLeaves(this.world, blockpos1))
                    {
                        this.func_175905_a(this.world, blockpos1, p_180712_3_, 0);
                    }
                }
            }
        }
    }

    /**
     * Gets the rough size of a layer of the tree.
     */
    float layerSize(int p_76490_1_)
    {
        if ((float)p_76490_1_ < (float)this.heightLimit * 0.3F)
        {
            return -1.0F;
        }
        else
        {
            float f = (float)this.heightLimit / 2.0F;
            float f1 = f - (float)p_76490_1_;
            float f2 = MathHelper.sqrt_float(f * f - f1 * f1);

            if (f1 == 0.0F)
            {
                f2 = f;
            }
            else if (Math.abs(f1) >= f)
            {
                return 0.0F;
            }

            return f2 * 0.5F;
        }
    }

    float leafSize(int p_76495_1_)
    {
        return p_76495_1_ >= 0 && p_76495_1_ < this.leafDistanceLimit ? (p_76495_1_ != 0 && p_76495_1_ != this.leafDistanceLimit - 1 ? 3.0F : 2.0F) : -1.0F;
    }

    void func_175940_a(BlockPos p_175940_1_)
    {
        for (int i = 0; i < this.leafDistanceLimit; ++i)
        {
            this.func_180712_a(p_175940_1_.up(i), this.leafSize(i), Blocks.leaves);
        }
    }

    void func_175937_a(BlockPos p_175937_1_, BlockPos p_175937_2_, Block p_175937_3_)
    {
        BlockPos blockpos2 = p_175937_2_.add(-p_175937_1_.getX(), -p_175937_1_.getY(), -p_175937_1_.getZ());
        int i = this.func_175935_b(blockpos2);
        float f = (float)blockpos2.getX() / (float)i;
        float f1 = (float)blockpos2.getY() / (float)i;
        float f2 = (float)blockpos2.getZ() / (float)i;

        for (int j = 0; j <= i; ++j)
        {
            BlockPos blockpos3 = p_175937_1_.add((double)(0.5F + (float)j * f), (double)(0.5F + (float)j * f1), (double)(0.5F + (float)j * f2));
            BlockLog.EnumAxis enumaxis = this.func_175938_b(p_175937_1_, blockpos3);
            this.func_175903_a(this.world, blockpos3, p_175937_3_.getDefaultState().withProperty(BlockLog.LOG_AXIS, enumaxis));
        }
    }

    private int func_175935_b(BlockPos p_175935_1_)
    {
        int i = MathHelper.abs_int(p_175935_1_.getX());
        int j = MathHelper.abs_int(p_175935_1_.getY());
        int k = MathHelper.abs_int(p_175935_1_.getZ());
        return k > i && k > j ? k : (j > i ? j : i);
    }

    private BlockLog.EnumAxis func_175938_b(BlockPos p_175938_1_, BlockPos p_175938_2_)
    {
        BlockLog.EnumAxis enumaxis = BlockLog.EnumAxis.Y;
        int i = Math.abs(p_175938_2_.getX() - p_175938_1_.getX());
        int j = Math.abs(p_175938_2_.getZ() - p_175938_1_.getZ());
        int k = Math.max(i, j);

        if (k > 0)
        {
            if (i == k)
            {
                enumaxis = BlockLog.EnumAxis.X;
            }
            else if (j == k)
            {
                enumaxis = BlockLog.EnumAxis.Z;
            }
        }

        return enumaxis;
    }

    void func_175941_b()
    {
        Iterator iterator = this.field_175948_j.iterator();

        while (iterator.hasNext())
        {
            WorldGenBigTree.FoliageCoordinates foliagecoordinates = (WorldGenBigTree.FoliageCoordinates)iterator.next();
            this.func_175940_a(foliagecoordinates);
        }
    }

    /**
     * Indicates whether or not a leaf node requires additional wood to be added to preserve integrity.
     */
    boolean leafNodeNeedsBase(int p_76493_1_)
    {
        return (double)p_76493_1_ >= (double)this.heightLimit * 0.2D;
    }

    void func_175942_c()
    {
        BlockPos blockpos = this.field_175947_m;
        BlockPos blockpos1 = this.field_175947_m.up(this.height);
        Block block = Blocks.log;
        this.func_175937_a(blockpos, blockpos1, block);

        if (this.field_175943_g == 2)
        {
            this.func_175937_a(blockpos.east(), blockpos1.east(), block);
            this.func_175937_a(blockpos.east().south(), blockpos1.east().south(), block);
            this.func_175937_a(blockpos.south(), blockpos1.south(), block);
        }
    }

    void func_175939_d()
    {
        Iterator iterator = this.field_175948_j.iterator();

        while (iterator.hasNext())
        {
            WorldGenBigTree.FoliageCoordinates foliagecoordinates = (WorldGenBigTree.FoliageCoordinates)iterator.next();
            int i = foliagecoordinates.func_177999_q();
            BlockPos blockpos = new BlockPos(this.field_175947_m.getX(), i, this.field_175947_m.getZ());

            if (this.leafNodeNeedsBase(i - this.field_175947_m.getY()))
            {
                this.func_175937_a(blockpos, foliagecoordinates, Blocks.log);
            }
        }
    }

    int func_175936_a(BlockPos p_175936_1_, BlockPos p_175936_2_)
    {
        BlockPos blockpos2 = p_175936_2_.add(-p_175936_1_.getX(), -p_175936_1_.getY(), -p_175936_1_.getZ());
        int i = this.func_175935_b(blockpos2);
        float f = (float)blockpos2.getX() / (float)i;
        float f1 = (float)blockpos2.getY() / (float)i;
        float f2 = (float)blockpos2.getZ() / (float)i;

        if (i == 0)
        {
            return -1;
        }
        else
        {
            for (int j = 0; j <= i; ++j)
            {
                BlockPos blockpos3 = p_175936_1_.add((double)(0.5F + (float)j * f), (double)(0.5F + (float)j * f1), (double)(0.5F + (float)j * f2));

                if (!this.isReplaceable(world, blockpos3))
                {
                    return j;
                }
            }

            return -1;
        }
    }

    public void func_175904_e()
    {
        this.leafDistanceLimit = 5;
    }

    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_)
    {
        this.world = worldIn;
        this.field_175947_m = p_180709_3_;
        this.field_175949_k = new Random(p_180709_2_.nextLong());

        if (this.heightLimit == 0)
        {
            this.heightLimit = 5 + this.field_175949_k.nextInt(this.field_175950_h);
        }

        if (!this.validTreeLocation())
        {
            this.world = null; //Fix vanilla Mem leak, holds latest world
            return false;
        }
        else
        {
            this.generateLeafNodeList();
            this.func_175941_b();
            this.func_175942_c();
            this.func_175939_d();
            this.world = null; //Fix vanilla Mem leak, holds latest world
            return true;
        }
    }

    /**
     * Returns a boolean indicating whether or not the current location for the tree, spanning basePos to to the height
     * limit, is valid.
     */
    private boolean validTreeLocation()
    {
        BlockPos down = this.field_175947_m.down();
        net.minecraft.block.state.IBlockState state = this.world.getBlockState(down);
        boolean isSoil = state.getBlock().canSustainPlant(this.world, down, net.minecraft.util.EnumFacing.UP, ((net.minecraft.block.BlockSapling)Blocks.sapling));

        if (!isSoil)
        {
            return false;
        }
        else
        {
            int i = this.func_175936_a(this.field_175947_m, this.field_175947_m.up(this.heightLimit - 1));

            if (i == -1)
            {
                return true;
            }
            else if (i < 6)
            {
                return false;
            }
            else
            {
                this.heightLimit = i;
                return true;
            }
        }
    }

    static class FoliageCoordinates extends BlockPos
        {
            private final int field_178000_b;
            private static final String __OBFID = "CL_00002001";

            public FoliageCoordinates(BlockPos p_i45635_1_, int p_i45635_2_)
            {
                super(p_i45635_1_.getX(), p_i45635_1_.getY(), p_i45635_1_.getZ());
                this.field_178000_b = p_i45635_2_;
            }

            public int func_177999_q()
            {
                return this.field_178000_b;
            }
        }
}