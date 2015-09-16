package net.minecraft.client.renderer.chunk;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RegionRenderCache;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderChunk
{
    private World world;
    private final RenderGlobal renderGlobal;
    public static int renderChunksUpdated;
    private BlockPos position;
    public CompiledChunk compiledChunk;
    private final ReentrantLock lockCompileTask;
    private final ReentrantLock lockCompiledChunk;
    private ChunkCompileTaskGenerator compileTask;
    private final int index;
    private final FloatBuffer modelviewMatrix;
    private final VertexBuffer[] vertexBuffers;
    public AxisAlignedBB boundingBox;
    private int frameIndex;
    private boolean needsUpdate;
    private static final String __OBFID = "CL_00002452";

    public RenderChunk(World worldIn, RenderGlobal renderGlobalIn, BlockPos blockPosIn, int indexIn)
    {
        this.compiledChunk = CompiledChunk.DUMMY;
        this.lockCompileTask = new ReentrantLock();
        this.lockCompiledChunk = new ReentrantLock();
        this.compileTask = null;
        this.modelviewMatrix = GLAllocation.createDirectFloatBuffer(16);
        this.vertexBuffers = new VertexBuffer[EnumWorldBlockLayer.values().length];
        this.frameIndex = -1;
        this.needsUpdate = true;
        this.world = worldIn;
        this.renderGlobal = renderGlobalIn;
        this.index = indexIn;

        if (!blockPosIn.equals(this.getPosition()))
        {
            this.setPosition(blockPosIn);
        }

        if (OpenGlHelper.useVbo())
        {
            for (int j = 0; j < EnumWorldBlockLayer.values().length; ++j)
            {
                this.vertexBuffers[j] = new VertexBuffer(DefaultVertexFormats.BLOCK);
            }
        }
    }

    public boolean setFrameIndex(int frameIndexIn)
    {
        if (this.frameIndex == frameIndexIn)
        {
            return false;
        }
        else
        {
            this.frameIndex = frameIndexIn;
            return true;
        }
    }

    public VertexBuffer getVertexBufferByLayer(int p_178565_1_)
    {
        return this.vertexBuffers[p_178565_1_];
    }

    public void setPosition(BlockPos p_178576_1_)
    {
        this.stopCompileTask();
        this.position = p_178576_1_;
        this.boundingBox = new AxisAlignedBB(p_178576_1_, p_178576_1_.add(16, 16, 16));
        this.initModelviewMatrix();
    }

    public void resortTransparency(float p_178570_1_, float p_178570_2_, float p_178570_3_, ChunkCompileTaskGenerator p_178570_4_)
    {
        CompiledChunk compiledchunk = p_178570_4_.getCompiledChunk();

        if (compiledchunk.getState() != null && !compiledchunk.isLayerEmpty(EnumWorldBlockLayer.TRANSLUCENT))
        {
            this.preRenderBlocks(p_178570_4_.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), this.position);
            p_178570_4_.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT).setVertexState(compiledchunk.getState());
            this.postRenderBlocks(EnumWorldBlockLayer.TRANSLUCENT, p_178570_1_, p_178570_2_, p_178570_3_, p_178570_4_.getRegionRenderCacheBuilder().getWorldRendererByLayer(EnumWorldBlockLayer.TRANSLUCENT), compiledchunk);
        }
    }

    public void rebuildChunk(float p_178581_1_, float p_178581_2_, float p_178581_3_, ChunkCompileTaskGenerator p_178581_4_)
    {
        CompiledChunk compiledchunk = new CompiledChunk();
        boolean flag = true;
        BlockPos blockpos = this.position;
        BlockPos blockpos1 = blockpos.add(15, 15, 15);
        p_178581_4_.getLock().lock();
        RegionRenderCache regionrendercache;

        try
        {
            if (p_178581_4_.getStatus() != ChunkCompileTaskGenerator.Status.COMPILING)
            {
                return;
            }

            regionrendercache = createRegionRenderCache(this.world, blockpos.add(-1, -1, -1), blockpos1.add(1, 1, 1), 1);
            p_178581_4_.setCompiledChunk(compiledchunk);
        }
        finally
        {
            p_178581_4_.getLock().unlock();
        }

        VisGraph visgraph = new VisGraph();

        if (!regionrendercache.extendedLevelsInChunkCache())
        {
            ++renderChunksUpdated;
            Iterator iterator = BlockPos.getAllInBoxMutable(blockpos, blockpos1).iterator();

            while (iterator.hasNext())
            {
                BlockPos.MutableBlockPos mutableblockpos = (BlockPos.MutableBlockPos)iterator.next();
                IBlockState iblockstate = regionrendercache.getBlockState(mutableblockpos);
                Block block = iblockstate.getBlock();

                if (block.isOpaqueCube())
                {
                    visgraph.func_178606_a(mutableblockpos);
                }

                if (block.hasTileEntity(iblockstate))
                {
                    TileEntity tileentity = regionrendercache.getTileEntity(new BlockPos(mutableblockpos));

                    if (tileentity != null && TileEntityRendererDispatcher.instance.hasSpecialRenderer(tileentity))
                    {
                        compiledchunk.addTileEntity(tileentity);
                    }
                }

                for(EnumWorldBlockLayer enumworldblocklayer1 : EnumWorldBlockLayer.values()) {
                    if(!block.canRenderInLayer(enumworldblocklayer1)) continue;
                    net.minecraftforge.client.ForgeHooksClient.setRenderLayer(enumworldblocklayer1);
                int i = enumworldblocklayer1.ordinal();

                if (block.getRenderType() != -1)
                {
                    WorldRenderer worldrenderer = p_178581_4_.getRegionRenderCacheBuilder().getWorldRendererByLayerId(i);

                    if (!compiledchunk.isLayerStarted(enumworldblocklayer1))
                    {
                        compiledchunk.setLayerStarted(enumworldblocklayer1);
                        this.preRenderBlocks(worldrenderer, blockpos);
                    }

                    if (Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(iblockstate, mutableblockpos, regionrendercache, worldrenderer))
                    {
                        compiledchunk.setLayerUsed(enumworldblocklayer1);
                    }
                }
                }
            }

            EnumWorldBlockLayer[] aenumworldblocklayer = EnumWorldBlockLayer.values();
            int j = aenumworldblocklayer.length;

            for (int k = 0; k < j; ++k)
            {
                EnumWorldBlockLayer enumworldblocklayer = aenumworldblocklayer[k];

                if (compiledchunk.isLayerStarted(enumworldblocklayer))
                {
                    this.postRenderBlocks(enumworldblocklayer, p_178581_1_, p_178581_2_, p_178581_3_, p_178581_4_.getRegionRenderCacheBuilder().getWorldRendererByLayer(enumworldblocklayer), compiledchunk);
                }
            }
        }

        compiledchunk.setVisibility(visgraph.computeVisibility());
    }

    protected void finishCompileTask()
    {
        this.lockCompileTask.lock();

        try
        {
            if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE)
            {
                this.compileTask.finish();
                this.compileTask = null;
            }
        }
        finally
        {
            this.lockCompileTask.unlock();
        }
    }

    public ReentrantLock getLockCompileTask()
    {
        return this.lockCompileTask;
    }

    public ChunkCompileTaskGenerator makeCompileTaskChunk()
    {
        this.lockCompileTask.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator;

        try
        {
            this.finishCompileTask();
            this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
            chunkcompiletaskgenerator = this.compileTask;
        }
        finally
        {
            this.lockCompileTask.unlock();
        }

        return chunkcompiletaskgenerator;
    }

    public ChunkCompileTaskGenerator makeCompileTaskTransparency()
    {
        this.lockCompileTask.lock();
        ChunkCompileTaskGenerator chunkcompiletaskgenerator;

        try
        {
            if (this.compileTask == null || this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.PENDING)
            {
                if (this.compileTask != null && this.compileTask.getStatus() != ChunkCompileTaskGenerator.Status.DONE)
                {
                    this.compileTask.finish();
                    this.compileTask = null;
                }

                this.compileTask = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY);
                this.compileTask.setCompiledChunk(this.compiledChunk);
                chunkcompiletaskgenerator = this.compileTask;
                return chunkcompiletaskgenerator;
            }

            chunkcompiletaskgenerator = null;
        }
        finally
        {
            this.lockCompileTask.unlock();
        }

        return chunkcompiletaskgenerator;
    }

    private void preRenderBlocks(WorldRenderer p_178573_1_, BlockPos pos)
    {
        p_178573_1_.startDrawing(7);
        p_178573_1_.setVertexFormat(DefaultVertexFormats.BLOCK);
        p_178573_1_.setTranslation((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()));
    }

    private void postRenderBlocks(EnumWorldBlockLayer p_178584_1_, float p_178584_2_, float p_178584_3_, float p_178584_4_, WorldRenderer p_178584_5_, CompiledChunk p_178584_6_)
    {
        if (p_178584_1_ == EnumWorldBlockLayer.TRANSLUCENT && !p_178584_6_.isLayerEmpty(p_178584_1_))
        {
            p_178584_6_.setState(p_178584_5_.getVertexState(p_178584_2_, p_178584_3_, p_178584_4_));
        }

        p_178584_5_.finishDrawing();
    }

    private void initModelviewMatrix()
    {
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        float f = 1.000001F;
        GlStateManager.translate(-8.0F, -8.0F, -8.0F);
        GlStateManager.scale(f, f, f);
        GlStateManager.translate(8.0F, 8.0F, 8.0F);
        GlStateManager.getFloat(2982, this.modelviewMatrix);
        GlStateManager.popMatrix();
    }

    public void multModelviewMatrix()
    {
        GlStateManager.multMatrix(this.modelviewMatrix);
    }

    public CompiledChunk getCompiledChunk()
    {
        return this.compiledChunk;
    }

    public void setCompiledChunk(CompiledChunk p_178580_1_)
    {
        this.lockCompiledChunk.lock();

        try
        {
            this.compiledChunk = p_178580_1_;
        }
        finally
        {
            this.lockCompiledChunk.unlock();
        }
    }

    public void stopCompileTask()
    {
        this.finishCompileTask();
        this.compiledChunk = CompiledChunk.DUMMY;
    }

    public void deleteGlResources()
    {
        this.stopCompileTask();
        this.world = null;

        for (int i = 0; i < EnumWorldBlockLayer.values().length; ++i)
        {
            if (this.vertexBuffers[i] != null)
            {
                this.vertexBuffers[i].deleteGlBuffers();
            }
        }
    }

    public BlockPos getPosition()
    {
        return this.position;
    }

    public boolean isCompileTaskPending()
    {
        this.lockCompileTask.lock();
        boolean flag;

        try
        {
            flag = this.compileTask == null || this.compileTask.getStatus() == ChunkCompileTaskGenerator.Status.PENDING;
        }
        finally
        {
            this.lockCompileTask.unlock();
        }

        return flag;
    }

    public void setNeedsUpdate(boolean p_178575_1_)
    {
        this.needsUpdate = p_178575_1_;
    }

    public boolean isNeedsUpdate()
    {
        return this.needsUpdate;
    }

    /* ======================================== FORGE START =====================================*/
    /**
     * Creates a new RegionRenderCache instance.<br>
     * Extending classes can change the behavior of the cache, allowing to visually change
     * blocks (schematics etc).
     *
     * @see RegionRenderCache
     * @param world The world to cache.
     * @param from The starting position of the chunk minus one on each axis.
     * @param to The ending position of the chunk plus one on each axis.
     * @param subtract Padding used internally by the RegionRenderCache constructor to make
     *                 the cache a 20x20x20 cube, for a total of 8000 states in the cache.
     * @return new RegionRenderCache instance
     */
    protected RegionRenderCache createRegionRenderCache(World world, BlockPos from, BlockPos to, int subtract)
    {
        return new RegionRenderCache(world, from, to, subtract);
    }
    /* ========================================= FORGE END ======================================*/
}