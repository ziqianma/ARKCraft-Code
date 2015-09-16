package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ChunkRenderContainer
{
    private double viewEntityX;
    private double viewEntityY;
    private double viewEntityZ;
    protected List renderChunks = Lists.newArrayListWithCapacity(17424);
    protected boolean initialized;
    private static final String __OBFID = "CL_00002563";

    public void initialize(double p_178004_1_, double p_178004_3_, double p_178004_5_)
    {
        this.initialized = true;
        this.renderChunks.clear();
        this.viewEntityX = p_178004_1_;
        this.viewEntityY = p_178004_3_;
        this.viewEntityZ = p_178004_5_;
    }

    public void preRenderChunk(RenderChunk p_178003_1_)
    {
        BlockPos blockpos = p_178003_1_.getPosition();
        GlStateManager.translate((float)((double)blockpos.getX() - this.viewEntityX), (float)((double)blockpos.getY() - this.viewEntityY), (float)((double)blockpos.getZ() - this.viewEntityZ));
    }

    public void addRenderChunk(RenderChunk p_178002_1_, EnumWorldBlockLayer p_178002_2_)
    {
        this.renderChunks.add(p_178002_1_);
    }

    public abstract void renderChunkLayer(EnumWorldBlockLayer p_178001_1_);
}