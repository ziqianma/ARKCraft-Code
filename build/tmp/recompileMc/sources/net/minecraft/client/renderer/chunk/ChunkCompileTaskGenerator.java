package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ChunkCompileTaskGenerator
{
    private final RenderChunk renderChunk;
    private final ReentrantLock lock = new ReentrantLock();
    private final List listFinishRunnables = Lists.newArrayList();
    private final ChunkCompileTaskGenerator.Type type;
    private RegionRenderCacheBuilder regionRenderCacheBuilder;
    private CompiledChunk compiledChunk;
    private ChunkCompileTaskGenerator.Status status;
    private boolean finished;
    private static final String __OBFID = "CL_00002466";

    public ChunkCompileTaskGenerator(RenderChunk p_i46208_1_, ChunkCompileTaskGenerator.Type p_i46208_2_)
    {
        this.status = ChunkCompileTaskGenerator.Status.PENDING;
        this.renderChunk = p_i46208_1_;
        this.type = p_i46208_2_;
    }

    public ChunkCompileTaskGenerator.Status getStatus()
    {
        return this.status;
    }

    public RenderChunk getRenderChunk()
    {
        return this.renderChunk;
    }

    public CompiledChunk getCompiledChunk()
    {
        return this.compiledChunk;
    }

    public void setCompiledChunk(CompiledChunk p_178543_1_)
    {
        this.compiledChunk = p_178543_1_;
    }

    public RegionRenderCacheBuilder getRegionRenderCacheBuilder()
    {
        return this.regionRenderCacheBuilder;
    }

    public void setRegionRenderCacheBuilder(RegionRenderCacheBuilder p_178541_1_)
    {
        this.regionRenderCacheBuilder = p_178541_1_;
    }

    public void setStatus(ChunkCompileTaskGenerator.Status p_178535_1_)
    {
        this.lock.lock();

        try
        {
            this.status = p_178535_1_;
        }
        finally
        {
            this.lock.unlock();
        }
    }

    public void finish()
    {
        this.lock.lock();

        try
        {
            this.finished = true;
            this.status = ChunkCompileTaskGenerator.Status.DONE;
            Iterator iterator = this.listFinishRunnables.iterator();

            while (iterator.hasNext())
            {
                Runnable runnable = (Runnable)iterator.next();
                runnable.run();
            }
        }
        finally
        {
            this.lock.unlock();
        }
    }

    public void addFinishRunnable(Runnable p_178539_1_)
    {
        this.lock.lock();

        try
        {
            this.listFinishRunnables.add(p_178539_1_);

            if (this.finished)
            {
                p_178539_1_.run();
            }
        }
        finally
        {
            this.lock.unlock();
        }
    }

    public ReentrantLock getLock()
    {
        return this.lock;
    }

    public ChunkCompileTaskGenerator.Type getType()
    {
        return this.type;
    }

    public boolean isFinished()
    {
        return this.finished;
    }

    @SideOnly(Side.CLIENT)
    public static enum Status
    {
        PENDING,
        COMPILING,
        UPLOADING,
        DONE;

        private static final String __OBFID = "CL_00002465";
    }

    @SideOnly(Side.CLIENT)
    public static enum Type
    {
        REBUILD_CHUNK,
        RESORT_TRANSPARENCY;

        private static final String __OBFID = "CL_00002464";
    }
}