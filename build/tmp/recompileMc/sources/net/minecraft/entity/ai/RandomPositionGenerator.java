package net.minecraft.entity.ai;

import java.util.Random;
import net.minecraft.entity.EntityCreature;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class RandomPositionGenerator
{
    /**
     * used to store a driection when the user passes a point to move towards or away from. WARNING: NEVER THREAD SAFE.
     * MULTIPLE findTowards and findAway calls, will share this var
     */
    private static Vec3 staticVector = new Vec3(0.0D, 0.0D, 0.0D);
    private static final String __OBFID = "CL_00001629";

    /**
     * finds a random target within par1(x,z) and par2 (y) blocks
     */
    public static Vec3 findRandomTarget(EntityCreature p_75463_0_, int p_75463_1_, int p_75463_2_)
    {
        /**
         * searches 10 blocks at random in a within par1(x,z) and par2 (y) distance, ignores those not in the direction
         * of par3Vec3, then points to the tile for which creature.getBlockPathWeight returns the highest number
         */
        return findRandomTargetBlock(p_75463_0_, p_75463_1_, p_75463_2_, (Vec3)null);
    }

    /**
     * finds a random target within par1(x,z) and par2 (y) blocks in the direction of the point par3
     */
    public static Vec3 findRandomTargetBlockTowards(EntityCreature p_75464_0_, int p_75464_1_, int p_75464_2_, Vec3 p_75464_3_)
    {
        staticVector = p_75464_3_.subtract(p_75464_0_.posX, p_75464_0_.posY, p_75464_0_.posZ);
        /**
         * searches 10 blocks at random in a within par1(x,z) and par2 (y) distance, ignores those not in the direction
         * of par3Vec3, then points to the tile for which creature.getBlockPathWeight returns the highest number
         */
        return findRandomTargetBlock(p_75464_0_, p_75464_1_, p_75464_2_, staticVector);
    }

    /**
     * finds a random target within par1(x,z) and par2 (y) blocks in the reverse direction of the point par3
     */
    public static Vec3 findRandomTargetBlockAwayFrom(EntityCreature p_75461_0_, int p_75461_1_, int p_75461_2_, Vec3 p_75461_3_)
    {
        staticVector = (new Vec3(p_75461_0_.posX, p_75461_0_.posY, p_75461_0_.posZ)).subtract(p_75461_3_);
        /**
         * searches 10 blocks at random in a within par1(x,z) and par2 (y) distance, ignores those not in the direction
         * of par3Vec3, then points to the tile for which creature.getBlockPathWeight returns the highest number
         */
        return findRandomTargetBlock(p_75461_0_, p_75461_1_, p_75461_2_, staticVector);
    }

    /**
     * searches 10 blocks at random in a within par1(x,z) and par2 (y) distance, ignores those not in the direction of
     * par3Vec3, then points to the tile for which creature.getBlockPathWeight returns the highest number
     */
    private static Vec3 findRandomTargetBlock(EntityCreature p_75462_0_, int p_75462_1_, int p_75462_2_, Vec3 p_75462_3_)
    {
        Random random = p_75462_0_.getRNG();
        boolean flag = false;
        int k = 0;
        int l = 0;
        int i1 = 0;
        float f = -99999.0F;
        boolean flag1;

        if (p_75462_0_.hasHome())
        {
            double d0 = p_75462_0_.func_180486_cf().distanceSq((double)MathHelper.floor_double(p_75462_0_.posX), (double)MathHelper.floor_double(p_75462_0_.posY), (double)MathHelper.floor_double(p_75462_0_.posZ)) + 4.0D;
            double d1 = (double)(p_75462_0_.getMaximumHomeDistance() + (float)p_75462_1_);
            flag1 = d0 < d1 * d1;
        }
        else
        {
            flag1 = false;
        }

        for (int l1 = 0; l1 < 10; ++l1)
        {
            int j1 = random.nextInt(2 * p_75462_1_ + 1) - p_75462_1_;
            int i2 = random.nextInt(2 * p_75462_2_ + 1) - p_75462_2_;
            int k1 = random.nextInt(2 * p_75462_1_ + 1) - p_75462_1_;

            if (p_75462_3_ == null || (double)j1 * p_75462_3_.xCoord + (double)k1 * p_75462_3_.zCoord >= 0.0D)
            {
                BlockPos blockpos;

                if (p_75462_0_.hasHome() && p_75462_1_ > 1)
                {
                    blockpos = p_75462_0_.func_180486_cf();

                    if (p_75462_0_.posX > (double)blockpos.getX())
                    {
                        j1 -= random.nextInt(p_75462_1_ / 2);
                    }
                    else
                    {
                        j1 += random.nextInt(p_75462_1_ / 2);
                    }

                    if (p_75462_0_.posZ > (double)blockpos.getZ())
                    {
                        k1 -= random.nextInt(p_75462_1_ / 2);
                    }
                    else
                    {
                        k1 += random.nextInt(p_75462_1_ / 2);
                    }
                }

                j1 += MathHelper.floor_double(p_75462_0_.posX);
                i2 += MathHelper.floor_double(p_75462_0_.posY);
                k1 += MathHelper.floor_double(p_75462_0_.posZ);
                blockpos = new BlockPos(j1, i2, k1);

                if (!flag1 || p_75462_0_.func_180485_d(blockpos))
                {
                    float f1 = p_75462_0_.func_180484_a(blockpos);

                    if (f1 > f)
                    {
                        f = f1;
                        k = j1;
                        l = i2;
                        i1 = k1;
                        flag = true;
                    }
                }
            }
        }

        if (flag)
        {
            return new Vec3((double)k, (double)l, (double)i1);
        }
        else
        {
            return null;
        }
    }
}