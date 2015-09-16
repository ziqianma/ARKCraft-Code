package net.minecraft.pathfinding;

import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class PathNavigateGround extends PathNavigate
{
    protected WalkNodeProcessor field_179695_a;
    private boolean field_179694_f;
    private static final String __OBFID = "CL_00002246";

    public PathNavigateGround(EntityLiving p_i45875_1_, World worldIn)
    {
        super(p_i45875_1_, worldIn);
    }

    protected PathFinder func_179679_a()
    {
        this.field_179695_a = new WalkNodeProcessor();
        this.field_179695_a.func_176175_a(true);
        return new PathFinder(this.field_179695_a);
    }

    /**
     * If on ground or swimming and can swim
     */
    protected boolean canNavigate()
    {
        return this.theEntity.onGround || this.func_179684_h() && this.isInLiquid() || this.theEntity.isRiding() && this.theEntity instanceof EntityZombie && this.theEntity.ridingEntity instanceof EntityChicken;
    }

    protected Vec3 getEntityPosition()
    {
        return new Vec3(this.theEntity.posX, (double)this.func_179687_p(), this.theEntity.posZ);
    }

    private int func_179687_p()
    {
        if (this.theEntity.isInWater() && this.func_179684_h())
        {
            int i = (int)this.theEntity.getEntityBoundingBox().minY;
            Block block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
            int j = 0;

            do
            {
                if (block != Blocks.flowing_water && block != Blocks.water)
                {
                    return i;
                }

                ++i;
                block = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), i, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
                ++j;
            }
            while (j <= 16);

            return (int)this.theEntity.getEntityBoundingBox().minY;
        }
        else
        {
            return (int)(this.theEntity.getEntityBoundingBox().minY + 0.5D);
        }
    }

    /**
     * Trims path data from the end to the first sun covered block
     */
    protected void removeSunnyPath()
    {
        super.removeSunnyPath();

        if (this.field_179694_f)
        {
            if (this.worldObj.canSeeSky(new BlockPos(MathHelper.floor_double(this.theEntity.posX), (int)(this.theEntity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(this.theEntity.posZ))))
            {
                return;
            }

            for (int i = 0; i < this.currentPath.getCurrentPathLength(); ++i)
            {
                PathPoint pathpoint = this.currentPath.getPathPointFromIndex(i);

                if (this.worldObj.canSeeSky(new BlockPos(pathpoint.xCoord, pathpoint.yCoord, pathpoint.zCoord)))
                {
                    this.currentPath.setCurrentPathLength(i - 1);
                    return;
                }
            }
        }
    }

    /**
     * Returns true when an entity of specified size could safely walk in a straight line between the two points. Args:
     * pos1, pos2, entityXSize, entityYSize, entityZSize
     */
    protected boolean isDirectPathBetweenPoints(Vec3 p_75493_1_, Vec3 p_75493_2_, int p_75493_3_, int p_75493_4_, int p_75493_5_)
    {
        int l = MathHelper.floor_double(p_75493_1_.xCoord);
        int i1 = MathHelper.floor_double(p_75493_1_.zCoord);
        double d0 = p_75493_2_.xCoord - p_75493_1_.xCoord;
        double d1 = p_75493_2_.zCoord - p_75493_1_.zCoord;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 < 1.0E-8D)
        {
            return false;
        }
        else
        {
            double d3 = 1.0D / Math.sqrt(d2);
            d0 *= d3;
            d1 *= d3;
            p_75493_3_ += 2;
            p_75493_5_ += 2;

            if (!this.func_179683_a(l, (int)p_75493_1_.yCoord, i1, p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, d0, d1))
            {
                return false;
            }
            else
            {
                p_75493_3_ -= 2;
                p_75493_5_ -= 2;
                double d4 = 1.0D / Math.abs(d0);
                double d5 = 1.0D / Math.abs(d1);
                double d6 = (double)(l * 1) - p_75493_1_.xCoord;
                double d7 = (double)(i1 * 1) - p_75493_1_.zCoord;

                if (d0 >= 0.0D)
                {
                    ++d6;
                }

                if (d1 >= 0.0D)
                {
                    ++d7;
                }

                d6 /= d0;
                d7 /= d1;
                int j1 = d0 < 0.0D ? -1 : 1;
                int k1 = d1 < 0.0D ? -1 : 1;
                int l1 = MathHelper.floor_double(p_75493_2_.xCoord);
                int i2 = MathHelper.floor_double(p_75493_2_.zCoord);
                int j2 = l1 - l;
                int k2 = i2 - i1;

                do
                {
                    if (j2 * j1 <= 0 && k2 * k1 <= 0)
                    {
                        return true;
                    }

                    if (d6 < d7)
                    {
                        d6 += d4;
                        l += j1;
                        j2 = l1 - l;
                    }
                    else
                    {
                        d7 += d5;
                        i1 += k1;
                        k2 = i2 - i1;
                    }
                }
                while (this.func_179683_a(l, (int)p_75493_1_.yCoord, i1, p_75493_3_, p_75493_4_, p_75493_5_, p_75493_1_, d0, d1));

                return false;
            }
        }
    }

    private boolean func_179683_a(int p_179683_1_, int p_179683_2_, int p_179683_3_, int p_179683_4_, int p_179683_5_, int p_179683_6_, Vec3 p_179683_7_, double p_179683_8_, double p_179683_10_)
    {
        int k1 = p_179683_1_ - p_179683_4_ / 2;
        int l1 = p_179683_3_ - p_179683_6_ / 2;

        if (!this.func_179692_b(k1, p_179683_2_, l1, p_179683_4_, p_179683_5_, p_179683_6_, p_179683_7_, p_179683_8_, p_179683_10_))
        {
            return false;
        }
        else
        {
            for (int i2 = k1; i2 < k1 + p_179683_4_; ++i2)
            {
                for (int j2 = l1; j2 < l1 + p_179683_6_; ++j2)
                {
                    double d2 = (double)i2 + 0.5D - p_179683_7_.xCoord;
                    double d3 = (double)j2 + 0.5D - p_179683_7_.zCoord;

                    if (d2 * p_179683_8_ + d3 * p_179683_10_ >= 0.0D)
                    {
                        Block block = this.worldObj.getBlockState(new BlockPos(i2, p_179683_2_ - 1, j2)).getBlock();
                        Material material = block.getMaterial();

                        if (material == Material.air)
                        {
                            return false;
                        }

                        if (material == Material.water && !this.theEntity.isInWater())
                        {
                            return false;
                        }

                        if (material == Material.lava)
                        {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private boolean func_179692_b(int p_179692_1_, int p_179692_2_, int p_179692_3_, int p_179692_4_, int p_179692_5_, int p_179692_6_, Vec3 p_179692_7_, double p_179692_8_, double p_179692_10_)
    {
        Iterator iterator = BlockPos.getAllInBox(new BlockPos(p_179692_1_, p_179692_2_, p_179692_3_), new BlockPos(p_179692_1_ + p_179692_4_ - 1, p_179692_2_ + p_179692_5_ - 1, p_179692_3_ + p_179692_6_ - 1)).iterator();

        while (iterator.hasNext())
        {
            BlockPos blockpos = (BlockPos)iterator.next();
            double d2 = (double)blockpos.getX() + 0.5D - p_179692_7_.xCoord;
            double d3 = (double)blockpos.getZ() + 0.5D - p_179692_7_.zCoord;

            if (d2 * p_179692_8_ + d3 * p_179692_10_ >= 0.0D)
            {
                Block block = this.worldObj.getBlockState(blockpos).getBlock();

                if (!block.isPassable(this.worldObj, blockpos))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public void func_179690_a(boolean p_179690_1_)
    {
        this.field_179695_a.func_176176_c(p_179690_1_);
    }

    public boolean func_179689_e()
    {
        return this.field_179695_a.func_176173_e();
    }

    public void func_179688_b(boolean p_179688_1_)
    {
        this.field_179695_a.func_176172_b(p_179688_1_);
    }

    public void func_179691_c(boolean p_179691_1_)
    {
        this.field_179695_a.func_176175_a(p_179691_1_);
    }

    public boolean func_179686_g()
    {
        return this.field_179695_a.func_176179_b();
    }

    public void func_179693_d(boolean p_179693_1_)
    {
        this.field_179695_a.func_176178_d(p_179693_1_);
    }

    public boolean func_179684_h()
    {
        return this.field_179695_a.func_176174_d();
    }

    public void func_179685_e(boolean p_179685_1_)
    {
        this.field_179694_f = p_179685_1_;
    }
}