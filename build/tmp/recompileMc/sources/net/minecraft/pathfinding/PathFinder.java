package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.NodeProcessor;

public class PathFinder
{
    /** The path being generated */
    private Path path = new Path();
    /** Selection of path points to add to the path */
    private PathPoint[] pathOptions = new PathPoint[32];
    private NodeProcessor field_176190_c;
    private static final String __OBFID = "CL_00000576";

    public PathFinder(NodeProcessor p_i45557_1_)
    {
        this.field_176190_c = p_i45557_1_;
    }

    public PathEntity func_176188_a(IBlockAccess p_176188_1_, Entity p_176188_2_, Entity p_176188_3_, float p_176188_4_)
    {
        return this.func_176189_a(p_176188_1_, p_176188_2_, p_176188_3_.posX, p_176188_3_.getEntityBoundingBox().minY, p_176188_3_.posZ, p_176188_4_);
    }

    public PathEntity func_180782_a(IBlockAccess p_180782_1_, Entity p_180782_2_, BlockPos p_180782_3_, float p_180782_4_)
    {
        return this.func_176189_a(p_180782_1_, p_180782_2_, (double)((float)p_180782_3_.getX() + 0.5F), (double)((float)p_180782_3_.getY() + 0.5F), (double)((float)p_180782_3_.getZ() + 0.5F), p_180782_4_);
    }

    private PathEntity func_176189_a(IBlockAccess p_176189_1_, Entity p_176189_2_, double p_176189_3_, double p_176189_5_, double p_176189_7_, float p_176189_9_)
    {
        this.path.clearPath();
        this.field_176190_c.func_176162_a(p_176189_1_, p_176189_2_);
        PathPoint pathpoint = this.field_176190_c.func_176161_a(p_176189_2_);
        PathPoint pathpoint1 = this.field_176190_c.func_176160_a(p_176189_2_, p_176189_3_, p_176189_5_, p_176189_7_);
        PathEntity pathentity = this.func_176187_a(p_176189_2_, pathpoint, pathpoint1, p_176189_9_);
        this.field_176190_c.func_176163_a();
        return pathentity;
    }

    private PathEntity func_176187_a(Entity p_176187_1_, PathPoint p_176187_2_, PathPoint p_176187_3_, float p_176187_4_)
    {
        p_176187_2_.totalPathDistance = 0.0F;
        p_176187_2_.distanceToNext = p_176187_2_.distanceToSquared(p_176187_3_);
        p_176187_2_.distanceToTarget = p_176187_2_.distanceToNext;
        this.path.clearPath();
        this.path.addPoint(p_176187_2_);
        PathPoint pathpoint2 = p_176187_2_;

        while (!this.path.isPathEmpty())
        {
            PathPoint pathpoint3 = this.path.dequeue();

            if (pathpoint3.equals(p_176187_3_))
            {
                return this.createEntityPath(p_176187_2_, p_176187_3_);
            }

            if (pathpoint3.distanceToSquared(p_176187_3_) < pathpoint2.distanceToSquared(p_176187_3_))
            {
                pathpoint2 = pathpoint3;
            }

            pathpoint3.visited = true;
            int i = this.field_176190_c.func_176164_a(this.pathOptions, p_176187_1_, pathpoint3, p_176187_3_, p_176187_4_);

            for (int j = 0; j < i; ++j)
            {
                PathPoint pathpoint4 = this.pathOptions[j];
                float f1 = pathpoint3.totalPathDistance + pathpoint3.distanceToSquared(pathpoint4);

                if (f1 < p_176187_4_ * 2.0F && (!pathpoint4.isAssigned() || f1 < pathpoint4.totalPathDistance))
                {
                    pathpoint4.previous = pathpoint3;
                    pathpoint4.totalPathDistance = f1;
                    pathpoint4.distanceToNext = pathpoint4.distanceToSquared(p_176187_3_);

                    if (pathpoint4.isAssigned())
                    {
                        this.path.changeDistance(pathpoint4, pathpoint4.totalPathDistance + pathpoint4.distanceToNext);
                    }
                    else
                    {
                        pathpoint4.distanceToTarget = pathpoint4.totalPathDistance + pathpoint4.distanceToNext;
                        this.path.addPoint(pathpoint4);
                    }
                }
            }
        }

        if (pathpoint2 == p_176187_2_)
        {
            return null;
        }
        else
        {
            return this.createEntityPath(p_176187_2_, pathpoint2);
        }
    }

    /**
     * Returns a new PathEntity for a given start and end point
     */
    private PathEntity createEntityPath(PathPoint p_75853_1_, PathPoint p_75853_2_)
    {
        int i = 1;
        PathPoint pathpoint2;

        for (pathpoint2 = p_75853_2_; pathpoint2.previous != null; pathpoint2 = pathpoint2.previous)
        {
            ++i;
        }

        PathPoint[] apathpoint = new PathPoint[i];
        pathpoint2 = p_75853_2_;
        --i;

        for (apathpoint[i] = p_75853_2_; pathpoint2.previous != null; apathpoint[i] = pathpoint2)
        {
            pathpoint2 = pathpoint2.previous;
            --i;
        }

        return new PathEntity(apathpoint);
    }
}