package net.minecraft.world.gen;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;

public class MapGenBase
{
    /** The number of Chunks to gen-check in any given direction. */
    protected int range = 8;
    /** The RNG used by the MapGen classes. */
    protected Random rand = new Random();
    /** This world object. */
    protected World worldObj;
    private static final String __OBFID = "CL_00000394";

    public void func_175792_a(IChunkProvider p_175792_1_, World worldIn, int p_175792_3_, int p_175792_4_, ChunkPrimer p_175792_5_)
    {
        int k = this.range;
        this.worldObj = worldIn;
        this.rand.setSeed(worldIn.getSeed());
        long l = this.rand.nextLong();
        long i1 = this.rand.nextLong();

        for (int j1 = p_175792_3_ - k; j1 <= p_175792_3_ + k; ++j1)
        {
            for (int k1 = p_175792_4_ - k; k1 <= p_175792_4_ + k; ++k1)
            {
                long l1 = (long)j1 * l;
                long i2 = (long)k1 * i1;
                this.rand.setSeed(l1 ^ i2 ^ worldIn.getSeed());
                this.func_180701_a(worldIn, j1, k1, p_175792_3_, p_175792_4_, p_175792_5_);
            }
        }
    }

    protected void func_180701_a(World worldIn, int p_180701_2_, int p_180701_3_, int p_180701_4_, int p_180701_5_, ChunkPrimer p_180701_6_) {}
}