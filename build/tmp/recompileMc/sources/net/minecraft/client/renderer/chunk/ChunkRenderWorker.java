package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class ChunkRenderWorker implements Runnable
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final ChunkRenderDispatcher chunkRenderDispatcher;
    private final RegionRenderCacheBuilder regionRenderCacheBuilder;
    private static final String __OBFID = "CL_00002459";

    public ChunkRenderWorker(ChunkRenderDispatcher p_i46201_1_)
    {
        this(p_i46201_1_, (RegionRenderCacheBuilder)null);
    }

    public ChunkRenderWorker(ChunkRenderDispatcher p_i46202_1_, RegionRenderCacheBuilder p_i46202_2_)
    {
        this.chunkRenderDispatcher = p_i46202_1_;
        this.regionRenderCacheBuilder = p_i46202_2_;
    }

    public void run()
    {
        try
        {
            while (true)
            {
                this.processTask(this.chunkRenderDispatcher.getNextChunkUpdate());
            }
        }
        catch (InterruptedException interruptedexception)
        {
            LOGGER.debug("Stopping due to interrupt");
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Batching chunks");
            Minecraft.getMinecraft().crashed(Minecraft.getMinecraft().addGraphicsAndWorldToCrashReport(crashreport));
        }
    }

    protected void processTask(final ChunkCompileTaskGenerator p_178474_1_) throws InterruptedException
    {
        p_178474_1_.getLock().lock();
        label242:
        {
            try
            {
                if (p_178474_1_.getStatus() == ChunkCompileTaskGenerator.Status.PENDING)
                {
                    p_178474_1_.setStatus(ChunkCompileTaskGenerator.Status.COMPILING);
                    break label242;
                }

                if (!p_178474_1_.isFinished())
                {
                    LOGGER.warn("Chunk render task was " + p_178474_1_.getStatus() + " when I expected it to be pending; ignoring task");
                }
            }
            finally
            {
                p_178474_1_.getLock().unlock();
            }

            return;
        }
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();

        if (entity == null)
        {
            p_178474_1_.finish();
        }
        else
        {
            p_178474_1_.setRegionRenderCacheBuilder(this.getRegionRenderCacheBuilder());
            float f = (float)entity.posX;
            float f1 = (float)entity.posY + entity.getEyeHeight();
            float f2 = (float)entity.posZ;
            ChunkCompileTaskGenerator.Type type = p_178474_1_.getType();

            if (type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK)
            {
                p_178474_1_.getRenderChunk().rebuildChunk(f, f1, f2, p_178474_1_);
            }
            else if (type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY)
            {
                p_178474_1_.getRenderChunk().resortTransparency(f, f1, f2, p_178474_1_);
            }

            p_178474_1_.getLock().lock();

            try
            {
                if (p_178474_1_.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING)
                {
                    if (!p_178474_1_.isFinished())
                    {
                        LOGGER.warn("Chunk render task was " + p_178474_1_.getStatus() + " when I expected it to be compiling; aborting task");
                    }

                    this.freeRenderBuilder(p_178474_1_);
                    return;
                }

                p_178474_1_.setStatus(ChunkCompileTaskGenerator.Status.UPLOADING);
            }
            finally
            {
                p_178474_1_.getLock().unlock();
            }

            final CompiledChunk compiledchunk = p_178474_1_.getCompiledChunk();
            ArrayList arraylist = Lists.newArrayList();

            if (type == ChunkCompileTaskGenerator.Type.REBUILD_CHUNK)
            {
                EnumWorldBlockLayer[] aenumworldblocklayer = EnumWorldBlockLayer.values();
                int i = aenumworldblocklayer.length;

                for (int j = 0; j < i; ++j)
                {
                    EnumWorldBlockLayer enumworldblocklayer = aenumworldblocklayer[j];

                    if (compiledchunk.isLayerStarted(enumworldblocklayer))
                    {
                        arraylist.add(this.chunkRenderDispatcher.uploadChunk(enumworldblocklayer, p_178474_1_.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer), p_178474_1_.getRenderChunk(), compiledchunk));
                    }
                }
            }
            else if (type == ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY)
            {
                arraylist.add(this.chunkRenderDispatcher.uploadChunk(EnumWorldBlockLayer.TRANSLUCENT, p_178474_1_.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), p_178474_1_.getRenderChunk(), compiledchunk));
            }

            final ListenableFuture listenablefuture = Futures.allAsList(arraylist);
            p_178474_1_.addFinishRunnable(new Runnable()
            {
                private static final String __OBFID = "CL_00002458";
                public void run()
                {
                    listenablefuture.cancel(false);
                }
            });
            Futures.addCallback(listenablefuture, new FutureCallback()
            {
                private static final String __OBFID = "CL_00002457";
                public void onSuccessList(List p_178481_1_)
                {
                    ChunkRenderWorker.this.freeRenderBuilder(p_178474_1_);
                    p_178474_1_.getLock().lock();
                    label53:
                    {
                        try
                        {
                            if (p_178474_1_.getStatus() == ChunkCompileTaskGenerator.Status.UPLOADING)
                            {
                                p_178474_1_.setStatus(ChunkCompileTaskGenerator.Status.DONE);
                                break label53;
                            }

                            if (!p_178474_1_.isFinished())
                            {
                                ChunkRenderWorker.LOGGER.warn("Chunk render task was " + p_178474_1_.getStatus() + " when I expected it to be uploading; aborting task");
                            }
                        }
                        finally
                        {
                            p_178474_1_.getLock().unlock();
                        }

                        return;
                    }
                    p_178474_1_.getRenderChunk().setCompiledChunk(compiledchunk);
                }
                public void onFailure(Throwable p_onFailure_1_)
                {
                    ChunkRenderWorker.this.freeRenderBuilder(p_178474_1_);

                    if (!(p_onFailure_1_ instanceof CancellationException) && !(p_onFailure_1_ instanceof InterruptedException))
                    {
                        Minecraft.getMinecraft().crashed(CrashReport.makeCrashReport(p_onFailure_1_, "Rendering chunk"));
                    }
                }
                public void onSuccess(Object p_onSuccess_1_)
                {
                    this.onSuccessList((List)p_onSuccess_1_);
                }
            });
        }
    }

    private RegionRenderCacheBuilder getRegionRenderCacheBuilder() throws InterruptedException
    {
        return this.regionRenderCacheBuilder != null ? this.regionRenderCacheBuilder : this.chunkRenderDispatcher.allocateRenderBuilder();
    }

    private void freeRenderBuilder(ChunkCompileTaskGenerator p_178473_1_)
    {
        if (this.regionRenderCacheBuilder == null)
        {
            this.chunkRenderDispatcher.freeRenderBuilder(p_178473_1_.getRegionRenderCacheBuilder());
        }
    }
}