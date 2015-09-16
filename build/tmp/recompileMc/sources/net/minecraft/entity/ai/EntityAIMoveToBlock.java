package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class EntityAIMoveToBlock extends EntityAIBase
{
    private final EntityCreature field_179495_c;
    private final double field_179492_d;
    protected int field_179496_a;
    private int field_179493_e;
    private int field_179490_f;
    /** Block to move to */
    protected BlockPos destinationBlock;
    private boolean field_179491_g;
    private int field_179497_h;
    private static final String __OBFID = "CL_00002252";

    public EntityAIMoveToBlock(EntityCreature p_i45888_1_, double p_i45888_2_, int p_i45888_4_)
    {
        this.destinationBlock = BlockPos.ORIGIN;
        this.field_179495_c = p_i45888_1_;
        this.field_179492_d = p_i45888_2_;
        this.field_179497_h = p_i45888_4_;
        this.setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.field_179496_a > 0)
        {
            --this.field_179496_a;
            return false;
        }
        else
        {
            this.field_179496_a = 200 + this.field_179495_c.getRNG().nextInt(200);
            return this.func_179489_g();
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.field_179493_e >= -this.field_179490_f && this.field_179493_e <= 1200 && this.func_179488_a(this.field_179495_c.worldObj, this.destinationBlock);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.field_179495_c.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.field_179492_d);
        this.field_179493_e = 0;
        this.field_179490_f = this.field_179495_c.getRNG().nextInt(this.field_179495_c.getRNG().nextInt(1200) + 1200) + 1200;
    }

    /**
     * Resets the task
     */
    public void resetTask() {}

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (this.field_179495_c.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0D)
        {
            this.field_179491_g = false;
            ++this.field_179493_e;

            if (this.field_179493_e % 40 == 0)
            {
                this.field_179495_c.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.field_179492_d);
            }
        }
        else
        {
            this.field_179491_g = true;
            --this.field_179493_e;
        }
    }

    protected boolean func_179487_f()
    {
        return this.field_179491_g;
    }

    private boolean func_179489_g()
    {
        int i = this.field_179497_h;
        boolean flag = true;
        BlockPos blockpos = new BlockPos(this.field_179495_c);

        for (int j = 0; j <= 1; j = j > 0 ? -j : 1 - j)
        {
            for (int k = 0; k < i; ++k)
            {
                for (int l = 0; l <= k; l = l > 0 ? -l : 1 - l)
                {
                    for (int i1 = l < k && l > -k ? k : 0; i1 <= k; i1 = i1 > 0 ? -i1 : 1 - i1)
                    {
                        BlockPos blockpos1 = blockpos.add(l, j - 1, i1);

                        if (this.field_179495_c.func_180485_d(blockpos1) && this.func_179488_a(this.field_179495_c.worldObj, blockpos1))
                        {
                            this.destinationBlock = blockpos1;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    protected abstract boolean func_179488_a(World worldIn, BlockPos p_179488_2_);
}