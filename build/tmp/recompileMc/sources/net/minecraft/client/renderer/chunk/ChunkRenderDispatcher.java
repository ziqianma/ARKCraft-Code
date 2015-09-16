package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.VertexBufferUploader;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ChunkRenderDispatcher
{
    private static final Logger logger = LogManager.getLogger();
    private static final ThreadFactory threadFactory = (new ThreadFactoryBuilder()).setNameFormat("Chunk Batcher %d").setDaemon(true).build();
    private final List listThreadedWorkers = Lists.newArrayList();
    private final BlockingQueue queueChunkUpdates = Queues.newArrayBlockingQueue(100);
    private final BlockingQueue queueFreeRenderBuilders = Queues.newArrayBlockingQueue(5);
    private final WorldVertexBufferUploader worldVertexUploader = new WorldVertexBufferUploader();
    private final VertexBufferUploader vertexUploader = new VertexBufferUploader();
    private final Queue queueChunkUploads = Queues.newArrayDeque();
    private final ChunkRenderWorker renderWorker;
    private static final String __OBFID = "CL_00002463";

    public ChunkRenderDispatcher()
    {
        int i;

        for (i = 0; i < 2; ++i)
        {
            ChunkRenderWorker chunkrenderworker = new ChunkRenderWorker(this);
            Thread thread = threadFactory.newThread(chunkrenderworker);
            thread.start();
            this.listThreadedWorkers.add(chunkrenderworker);
        }

        for (i = 0; i < 5; ++i)
        {
            this.queueFreeRenderBuilders.add(new RegionRenderCacheBuilder());
        }

        this.renderWorker = new ChunkRenderWorker(this, new RegionRenderCacheBuilder());
    }

    public String getDebugInfo()
    {
        return String.format("pC: %03d, pU: %1d, aB: %1d", new Object[] {Integer.valueOf(this.queueChunkUpdates.size()), Integer.valueOf(this.queueChunkUploads.size()), Integer.valueOf(this.queueFreeRenderBuilders.size())});
    }

    public boolean runChunkUploads(long p_178516_1_)
    {
        boolean flag = false;
        long j;

        do
        {
            boolean flag1 = false;
            Queue queue = this.queueChunkUploads;

            synchronized (this.queueChunkUploads)
            {
                if (!this.queueChunkUploads.isEmpty())
                {
                    ((ListenableFutureTask)this.queueChunkUploads.poll()).run();
                    flag1 = true;
                    flag = true;
                }
            }

            if (p_178516_1_ == 0L || !flag1)
            {
                break;
            }

            j = p_178516_1_ - System.nanoTime();
        }
        while (j >= 0L && j <= 1000000000L);

        return flag;
    }

    public boolean updateChunkLater(RenderChunk p_178507_1_)
    {
        p_178507_1_.getLockCompileTask().lock();
        boolean flag1;

        try
        {
            final ChunkCompileTaskGenerator chunkcompiletaskgenerator = p_178507_1_.makeCompileTaskChunk();
            chunkcompiletaskgenerator.addFinishRunnable(new Runnable()
            {
                private static final String __OBFID = "CL_00002462";
                public void run()
                {
                    ChunkRenderDispatcher.this.queueChunkUpdates.remove(chunkcompiletaskgenerator);
                }
            });
            boolean flag = this.queueChunkUpdates.offer(chunkcompiletaskgenerator);

            if (!flag)
            {
                chunkcompiletaskgenerator.finish();
            }

            flag1 = flag;
        }
        finally
        {
            p_178507_1_.getLockCompileTask().unlock();
        }

        return flag1;
    }

    public boolean updateChunkNow(RenderChunk p_178505_1_)
    {
        p_178505_1_.getLockCompileTask().lock();
        boolean flag;

        try
        {
            ChunkCompileTaskGenerator chunkcompiletaskgenerator = p_178505_1_.makeCompileTaskChunk();

            try
            {
                this.renderWorker.processTask(chunkcompiletaskgenerator);
            }
            catch (InterruptedException interruptedexception)
            {
                ;
            }

            flag = true;
        }
        finally
        {
            p_178505_1_.getLockCompileTask().unlock();
        }

        return flag;
    }

    public void stopChunkUpdates()
    {
        this.clearChunkUpdates();

        while (this.runChunkUploads(0L))
        {
            ;
        }

        ArrayList arraylist = Lists.newArrayList();

        while (arraylist.size() != 5)
        {
            try
            {
                arraylist.add(this.allocateRenderBuilder());
            }
            catch (InterruptedException interruptedexception)
            {
                ;
            }
        }

        this.queueFreeRenderBuilders.addAll(arraylist);
    }

    public void freeRenderBuilder(RegionRenderCacheBuilder p_178512_1_)
    {
        this.queueFreeRenderBuilders.add(p_178512_1_);
    }

    public RegionRenderCacheBuilder allocateRenderBuilder() throws InterruptedException
    {
        return (RegionRenderCacheBuilder)this.queueFreeRenderBuilders.take();
    }

    public ChunkCompileTaskGenerator getNextChunkUpdate() throws InterruptedException
    {
        return (ChunkCompileTaskGenerator)this.queueChunkUpdates.take();
    }

    public boolean updateTransparencyLater(RenderChunk p_178509_1_)
    {
        p_178509_1_.getLockCompileTask().lock();
        boolean flag;

        try
        {
            final ChunkCompileTaskGenerator chunkcompiletaskgenerator = p_178509_1_.makeCompileTaskTransparency();

            if (chunkcompiletaskgenerator != null)
            {
                chunkcompiletaskgenerator.addFinishRunnable(new Runnable()
                {
                    private static final String __OBFID = "CL_00002461";
                    public void run()
                    {
                        ChunkRenderDispatcher.this.queueChunkUpdates.remove(chunkcompiletaskgenerator);
                    }
                });
                flag = this.queueChunkUpdates.offer(chunkcompiletaskgenerator);
                return flag;
            }

            flag = true;
        }
        finally
        {
            p_178509_1_.getLockCompileTask().unlock();
        }

        return flag;
    }

    public ListenableFuture uploadChunk(final EnumWorldBlockLayer p_178503_1_, final WorldRenderer p_178503_2_, final RenderChunk p_178503_3_, final CompiledChunk p_178503_4_)
    {
        if (Minecraft.getMinecraft().isCallingFromMinecraftThread())
        {
            if (OpenGlHelper.useVbo())
            {
                this.uploadVertexBuffer(p_178503_2_, p_178503_3_.getVertexBufferByLayer(p_178503_1_.ordinal()));
            }
            else
            {
                this.uploadDisplayList(p_178503_2_, ((ListedRenderChunk)p_178503_3_).getDisplayList(p_178503_1_, p_178503_4_), p_178503_3_);
            }

            p_178503_2_.setTranslation(0.0D, 0.0D, 0.0D);
            return Futures.immediateFuture((Object)null);
        }
        else
        {
            ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(new Runnable()
            {
                private static final String __OBFID = "CL_00002460";
                public void run()
                {
                    ChunkRenderDispatcher.this.uploadChunk(p_178503_1_, p_178503_2_, p_178503_3_, p_178503_4_);
                }
            }, (Object)null);
            Queue queue = this.queueChunkUploads;

            synchronized (this.queueChunkUploads)
            {
                this.queueChunkUploads.add(listenablefuturetask);
                return listenablefuturetask;
            }
        }
    }

    private void uploadDisplayList(WorldRenderer p_178510_1_, int p_178510_2_, RenderChunk p_178510_3_)
    {
        GL11.glNewList(p_178510_2_, GL11.GL_COMPILE);
        GlStateManager.pushMatrix();
        p_178510_3_.multModelviewMatrix();
        this.worldVertexUploader.draw(p_178510_1_, p_178510_1_.getByteIndex());
        GlStateManager.popMatrix();
        GL11.glEndList();
    }

    private void uploadVertexBuffer(WorldRenderer p_178506_1_, VertexBuffer p_178506_2_)
    {
        this.vertexUploader.setVertexBuffer(p_178506_2_);
        this.vertexUploader.draw(p_178506_1_, p_178506_1_.getByteIndex());
    }

    public void clearChunkUpdates()
    {
        this.queueChunkUpdates.clear();
    }
}