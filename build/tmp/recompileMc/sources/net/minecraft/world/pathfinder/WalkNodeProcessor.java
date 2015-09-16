package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessor extends NodeProcessor
{
    private boolean field_176180_f;
    private boolean field_176181_g;
    private boolean field_176183_h;
    private boolean field_176184_i;
    private boolean field_176182_j;
    private static final String __OBFID = "CL_00001965";

    public void func_176162_a(IBlockAccess p_176162_1_, Entity p_176162_2_)
    {
        super.func_176162_a(p_176162_1_, p_176162_2_);
        this.field_176182_j = this.field_176183_h;
    }

    public void func_176163_a()
    {
        super.func_176163_a();
        this.field_176183_h = this.field_176182_j;
    }

    public PathPoint func_176161_a(Entity p_176161_1_)
    {
        int i;

        if (this.field_176184_i && p_176161_1_.isInWater())
        {
            i = (int)p_176161_1_.getEntityBoundingBox().minY;

            for (Block block = this.field_176169_a.getBlockState(new BlockPos(MathHelper.floor_double(p_176161_1_.posX), i, MathHelper.floor_double(p_176161_1_.posZ))).getBlock(); block == Blocks.flowing_water || block == Blocks.water; block = this.field_176169_a.getBlockState(new BlockPos(MathHelper.floor_double(p_176161_1_.posX), i, MathHelper.floor_double(p_176161_1_.posZ))).getBlock())
            {
                ++i;
            }

            this.field_176183_h = false;
        }
        else
        {
            i = MathHelper.floor_double(p_176161_1_.getEntityBoundingBox().minY + 0.5D);
        }

        return this.func_176159_a(MathHelper.floor_double(p_176161_1_.getEntityBoundingBox().minX), i, MathHelper.floor_double(p_176161_1_.getEntityBoundingBox().minZ));
    }

    public PathPoint func_176160_a(Entity p_176160_1_, double p_176160_2_, double p_176160_4_, double p_176160_6_)
    {
        return this.func_176159_a(MathHelper.floor_double(p_176160_2_ - (double)(p_176160_1_.width / 2.0F)), MathHelper.floor_double(p_176160_4_), MathHelper.floor_double(p_176160_6_ - (double)(p_176160_1_.width / 2.0F)));
    }

    public int func_176164_a(PathPoint[] p_176164_1_, Entity p_176164_2_, PathPoint p_176164_3_, PathPoint p_176164_4_, float p_176164_5_)
    {
        int i = 0;
        byte b0 = 0;

        if (this.func_176177_a(p_176164_2_, p_176164_3_.xCoord, p_176164_3_.yCoord + 1, p_176164_3_.zCoord) == 1)
        {
            b0 = 1;
        }

        PathPoint pathpoint2 = this.func_176171_a(p_176164_2_, p_176164_3_.xCoord, p_176164_3_.yCoord, p_176164_3_.zCoord + 1, b0);
        PathPoint pathpoint3 = this.func_176171_a(p_176164_2_, p_176164_3_.xCoord - 1, p_176164_3_.yCoord, p_176164_3_.zCoord, b0);
        PathPoint pathpoint4 = this.func_176171_a(p_176164_2_, p_176164_3_.xCoord + 1, p_176164_3_.yCoord, p_176164_3_.zCoord, b0);
        PathPoint pathpoint5 = this.func_176171_a(p_176164_2_, p_176164_3_.xCoord, p_176164_3_.yCoord, p_176164_3_.zCoord - 1, b0);

        if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(p_176164_4_) < p_176164_5_)
        {
            p_176164_1_[i++] = pathpoint2;
        }

        if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(p_176164_4_) < p_176164_5_)
        {
            p_176164_1_[i++] = pathpoint3;
        }

        if (pathpoint4 != null && !pathpoint4.visited && pathpoint4.distanceTo(p_176164_4_) < p_176164_5_)
        {
            p_176164_1_[i++] = pathpoint4;
        }

        if (pathpoint5 != null && !pathpoint5.visited && pathpoint5.distanceTo(p_176164_4_) < p_176164_5_)
        {
            p_176164_1_[i++] = pathpoint5;
        }

        return i;
    }

    private PathPoint func_176171_a(Entity p_176171_1_, int p_176171_2_, int p_176171_3_, int p_176171_4_, int p_176171_5_)
    {
        PathPoint pathpoint = null;
        int i1 = this.func_176177_a(p_176171_1_, p_176171_2_, p_176171_3_, p_176171_4_);

        if (i1 == 2)
        {
            return this.func_176159_a(p_176171_2_, p_176171_3_, p_176171_4_);
        }
        else
        {
            if (i1 == 1)
            {
                pathpoint = this.func_176159_a(p_176171_2_, p_176171_3_, p_176171_4_);
            }

            if (pathpoint == null && p_176171_5_ > 0 && i1 != -3 && i1 != -4 && this.func_176177_a(p_176171_1_, p_176171_2_, p_176171_3_ + p_176171_5_, p_176171_4_) == 1)
            {
                pathpoint = this.func_176159_a(p_176171_2_, p_176171_3_ + p_176171_5_, p_176171_4_);
                p_176171_3_ += p_176171_5_;
            }

            if (pathpoint != null)
            {
                int j1 = 0;
                int k1;

                for (k1 = 0; p_176171_3_ > 0; pathpoint = this.func_176159_a(p_176171_2_, p_176171_3_, p_176171_4_))
                {
                    k1 = this.func_176177_a(p_176171_1_, p_176171_2_, p_176171_3_ - 1, p_176171_4_);

                    if (this.field_176183_h && k1 == -1)
                    {
                        return null;
                    }

                    if (k1 != 1)
                    {
                        break;
                    }

                    if (j1++ >= p_176171_1_.getMaxFallHeight())
                    {
                        return null;
                    }

                    --p_176171_3_;

                    if (p_176171_3_ <= 0)
                    {
                        return null;
                    }
                }

                if (k1 == -2)
                {
                    return null;
                }
            }

            return pathpoint;
        }
    }

    private int func_176177_a(Entity p_176177_1_, int p_176177_2_, int p_176177_3_, int p_176177_4_)
    {
        return func_176170_a(this.field_176169_a, p_176177_1_, p_176177_2_, p_176177_3_, p_176177_4_, this.field_176168_c, this.field_176165_d, this.field_176166_e, this.field_176183_h, this.field_176181_g, this.field_176180_f);
    }

    public static int func_176170_a(IBlockAccess p_176170_0_, Entity p_176170_1_, int p_176170_2_, int p_176170_3_, int p_176170_4_, int p_176170_5_, int p_176170_6_, int p_176170_7_, boolean p_176170_8_, boolean p_176170_9_, boolean p_176170_10_)
    {
        boolean flag3 = false;
        BlockPos blockpos = new BlockPos(p_176170_1_);

        for (int k1 = p_176170_2_; k1 < p_176170_2_ + p_176170_5_; ++k1)
        {
            for (int l1 = p_176170_3_; l1 < p_176170_3_ + p_176170_6_; ++l1)
            {
                for (int i2 = p_176170_4_; i2 < p_176170_4_ + p_176170_7_; ++i2)
                {
                    BlockPos blockpos1 = new BlockPos(k1, l1, i2);
                    Block block = p_176170_0_.getBlockState(blockpos1).getBlock();

                    if (block.getMaterial() != Material.air)
                    {
                        if (block != Blocks.trapdoor && block != Blocks.iron_trapdoor)
                        {
                            if (block != Blocks.flowing_water && block != Blocks.water)
                            {
                                if (!p_176170_10_ && block instanceof BlockDoor && block.getMaterial() == Material.wood)
                                {
                                    return 0;
                                }
                            }
                            else
                            {
                                if (p_176170_8_)
                                {
                                    return -1;
                                }

                                flag3 = true;
                            }
                        }
                        else
                        {
                            flag3 = true;
                        }

                        if (p_176170_1_.worldObj.getBlockState(blockpos1).getBlock() instanceof BlockRailBase)
                        {
                            if (!(p_176170_1_.worldObj.getBlockState(blockpos).getBlock() instanceof BlockRailBase) && !(p_176170_1_.worldObj.getBlockState(blockpos.down()).getBlock() instanceof BlockRailBase))
                            {
                                return -3;
                            }
                        }
                        else if (!block.isPassable(p_176170_0_, blockpos1) && (!p_176170_9_ || !(block instanceof BlockDoor) || block.getMaterial() != Material.wood))
                        {
                            if (block instanceof BlockFence || block instanceof BlockFenceGate || block instanceof BlockWall)
                            {
                                return -3;
                            }

                            if (block == Blocks.trapdoor || block == Blocks.iron_trapdoor)
                            {
                                return -4;
                            }

                            Material material = block.getMaterial();

                            if (material != Material.lava)
                            {
                                return 0;
                            }

                            if (!p_176170_1_.isInLava())
                            {
                                return -2;
                            }
                        }
                    }
                }
            }
        }

        return flag3 ? 2 : 1;
    }

    public void func_176175_a(boolean p_176175_1_)
    {
        this.field_176180_f = p_176175_1_;
    }

    public void func_176172_b(boolean p_176172_1_)
    {
        this.field_176181_g = p_176172_1_;
    }

    public void func_176176_c(boolean p_176176_1_)
    {
        this.field_176183_h = p_176176_1_;
    }

    public void func_176178_d(boolean p_176178_1_)
    {
        this.field_176184_i = p_176178_1_;
    }

    public boolean func_176179_b()
    {
        return this.field_176180_f;
    }

    public boolean func_176174_d()
    {
        return this.field_176184_i;
    }

    public boolean func_176173_e()
    {
        return this.field_176183_h;
    }
}