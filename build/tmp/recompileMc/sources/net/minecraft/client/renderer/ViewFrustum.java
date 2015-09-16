package net.minecraft.client.renderer;

import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ViewFrustum
{
    protected final RenderGlobal renderGlobal;
    protected final World world;
    protected int countChunksY;
    protected int countChunksX;
    protected int countChunksZ;
    public RenderChunk[] renderChunks;
    private static final String __OBFID = "CL_00002531";

    public ViewFrustum(World worldIn, int renderDistanceChunks, RenderGlobal p_i46246_3_, IRenderChunkFactory p_i46246_4_)
    {
        this.renderGlobal = p_i46246_3_;
        this.world = worldIn;
        this.setCountChunksXYZ(renderDistanceChunks);
        this.createRenderChunks(p_i46246_4_);
    }

    protected void createRenderChunks(IRenderChunkFactory p_178158_1_)
    {
        int i = this.countChunksX * this.countChunksY * this.countChunksZ;
        this.renderChunks = new RenderChunk[i];
        int j = 0;

        for (int k = 0; k < this.countChunksX; ++k)
        {
            for (int l = 0; l < this.countChunksY; ++l)
            {
                for (int i1 = 0; i1 < this.countChunksZ; ++i1)
                {
                    int j1 = (i1 * this.countChunksY + l) * this.countChunksX + k;
                    BlockPos blockpos = new BlockPos(k * 16, l * 16, i1 * 16);
                    this.renderChunks[j1] = p_178158_1_.makeRenderChunk(this.world, this.renderGlobal, blockpos, j++);
                }
            }
        }
    }

    public void deleteGlResources()
    {
        RenderChunk[] arenderchunk = this.renderChunks;
        int i = arenderchunk.length;

        for (int j = 0; j < i; ++j)
        {
            RenderChunk renderchunk = arenderchunk[j];
            renderchunk.deleteGlResources();
        }
    }

    protected void setCountChunksXYZ(int renderDistanceChunks)
    {
        int j = renderDistanceChunks * 2 + 1;
        this.countChunksX = j;
        this.countChunksY = 16;
        this.countChunksZ = j;
    }

    public void updateChunkPositions(double viewEntityX, double viewEntityZ)
    {
        int i = MathHelper.floor_double(viewEntityX) - 8;
        int j = MathHelper.floor_double(viewEntityZ) - 8;
        int k = this.countChunksX * 16;

        for (int l = 0; l < this.countChunksX; ++l)
        {
            int i1 = this.func_178157_a(i, k, l);

            for (int j1 = 0; j1 < this.countChunksZ; ++j1)
            {
                int k1 = this.func_178157_a(j, k, j1);

                for (int l1 = 0; l1 < this.countChunksY; ++l1)
                {
                    int i2 = l1 * 16;
                    RenderChunk renderchunk = this.renderChunks[(j1 * this.countChunksY + l1) * this.countChunksX + l];
                    BlockPos blockpos = new BlockPos(i1, i2, k1);

                    if (!blockpos.equals(renderchunk.getPosition()))
                    {
                        renderchunk.setPosition(blockpos);
                    }
                }
            }
        }
    }

    private int func_178157_a(int p_178157_1_, int p_178157_2_, int p_178157_3_)
    {
        int l = p_178157_3_ * 16;
        int i1 = l - p_178157_1_ + p_178157_2_ / 2;

        if (i1 < 0)
        {
            i1 -= p_178157_2_ - 1;
        }

        return l - i1 / p_178157_2_ * p_178157_2_;
    }

    public void markBlocksForUpdate(int p_178162_1_, int p_178162_2_, int p_178162_3_, int p_178162_4_, int p_178162_5_, int p_178162_6_)
    {
        int k1 = MathHelper.bucketInt(p_178162_1_, 16);
        int l1 = MathHelper.bucketInt(p_178162_2_, 16);
        int i2 = MathHelper.bucketInt(p_178162_3_, 16);
        int j2 = MathHelper.bucketInt(p_178162_4_, 16);
        int k2 = MathHelper.bucketInt(p_178162_5_, 16);
        int l2 = MathHelper.bucketInt(p_178162_6_, 16);

        for (int i3 = k1; i3 <= j2; ++i3)
        {
            int j3 = i3 % this.countChunksX;

            if (j3 < 0)
            {
                j3 += this.countChunksX;
            }

            for (int k3 = l1; k3 <= k2; ++k3)
            {
                int l3 = k3 % this.countChunksY;

                if (l3 < 0)
                {
                    l3 += this.countChunksY;
                }

                for (int i4 = i2; i4 <= l2; ++i4)
                {
                    int j4 = i4 % this.countChunksZ;

                    if (j4 < 0)
                    {
                        j4 += this.countChunksZ;
                    }

                    int k4 = (j4 * this.countChunksY + l3) * this.countChunksX + j3;
                    RenderChunk renderchunk = this.renderChunks[k4];
                    renderchunk.setNeedsUpdate(true);
                }
            }
        }
    }

    protected RenderChunk getRenderChunk(BlockPos p_178161_1_)
    {
        int i = MathHelper.bucketInt(p_178161_1_.getX(), 16);
        int j = MathHelper.bucketInt(p_178161_1_.getY(), 16);
        int k = MathHelper.bucketInt(p_178161_1_.getZ(), 16);

        if (j >= 0 && j < this.countChunksY)
        {
            i %= this.countChunksX;

            if (i < 0)
            {
                i += this.countChunksX;
            }

            k %= this.countChunksZ;

            if (k < 0)
            {
                k += this.countChunksZ;
            }

            int l = (k * this.countChunksY + j) * this.countChunksX + i;
            return this.renderChunks[l];
        }
        else
        {
            return null;
        }
    }
}