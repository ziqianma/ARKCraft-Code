package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderDebug;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.ChunkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk
{
    private static final Logger logger = LogManager.getLogger();
    /**
     * Used to store block IDs, block MSBs, Sky-light maps, Block-light maps, and metadata. Each entry corresponds to a
     * logical segment of 16x16x16 blocks, stacked vertically.
     */
    private final ExtendedBlockStorage[] storageArrays;
    /** Contains a 16x16 mapping on the X/Z plane of the biome ID to which each colum belongs. */
    private final byte[] blockBiomeArray;
    /** A map, similar to heightMap, that tracks how far down precipitation can fall. */
    private final int[] precipitationHeightMap;
    /** Which columns need their skylightMaps updated. */
    private final boolean[] updateSkylightColumns;
    /** Whether or not this Chunk is currently loaded into the World */
    private boolean isChunkLoaded;
    /** Reference to the World object. */
    private final World worldObj;
    private final int[] heightMap;
    /** The x coordinate of the chunk. */
    public final int xPosition;
    /** The z coordinate of the chunk. */
    public final int zPosition;
    private boolean isGapLightingUpdated;
    /** A Map of ChunkPositions to TileEntities in this chunk */
    private final Map chunkTileEntityMap;
    /** Array of Lists containing the entities in this Chunk. Each List represents a 16 block subchunk. */
    private final ClassInheritanceMultiMap[] entityLists;
    /** Boolean value indicating if the terrain is populated. */
    private boolean isTerrainPopulated;
    private boolean isLightPopulated;
    private boolean field_150815_m;
    /** Set to true if the chunk has been modified and needs to be updated internally. */
    private boolean isModified;
    /** Whether this Chunk has any Entities and thus requires saving on every tick */
    private boolean hasEntities;
    /** The time according to World.worldTime when this chunk was last saved */
    private long lastSaveTime;
    /** Lowest value in the heightmap. */
    private int heightMapMinimum;
    /** the cumulative number of ticks players have been in this chunk */
    private long inhabitedTime;
    /** Contains the current round-robin relight check index, and is implied as the relight check location as well. */
    private int queuedLightChecks;
    private ConcurrentLinkedQueue field_177447_w;
    private static final String __OBFID = "CL_00000373";

    public Chunk(World worldIn, int x, int z)
    {
        this.storageArrays = new ExtendedBlockStorage[16];
        this.blockBiomeArray = new byte[256];
        this.precipitationHeightMap = new int[256];
        this.updateSkylightColumns = new boolean[256];
        this.chunkTileEntityMap = Maps.newHashMap();
        this.queuedLightChecks = 4096;
        this.field_177447_w = Queues.newConcurrentLinkedQueue();
        this.entityLists = (ClassInheritanceMultiMap[])(new ClassInheritanceMultiMap[16]);
        this.worldObj = worldIn;
        this.xPosition = x;
        this.zPosition = z;
        this.heightMap = new int[256];

        for (int k = 0; k < this.entityLists.length; ++k)
        {
            this.entityLists[k] = new ClassInheritanceMultiMap(Entity.class);
        }

        Arrays.fill(this.precipitationHeightMap, -999);
        Arrays.fill(this.blockBiomeArray, (byte) - 1);
    }

    public Chunk(World worldIn, ChunkPrimer primer, int x, int z)
    {
        this(worldIn, x, z);
        short short1 = 256;
        boolean flag = !worldIn.provider.getHasNoSky();

        for (int k = 0; k < 16; ++k)
        {
            for (int l = 0; l < 16; ++l)
            {
                for (int i1 = 0; i1 < short1; ++i1)
                {
                    int j1 = k * short1 * 16 | l * short1 | i1;
                    IBlockState iblockstate = primer.getBlockState(j1);

                    if (iblockstate.getBlock().getMaterial() != Material.air)
                    {
                        int k1 = i1 >> 4;

                        if (this.storageArrays[k1] == null)
                        {
                            this.storageArrays[k1] = new ExtendedBlockStorage(k1 << 4, flag);
                        }

                        this.storageArrays[k1].set(k, i1 & 15, l, iblockstate);
                    }
                }
            }
        }
    }

    /**
     * Checks whether the chunk is at the X/Z location specified
     */
    public boolean isAtLocation(int x, int z)
    {
        return x == this.xPosition && z == this.zPosition;
    }

    public int getHeight(BlockPos pos)
    {
        return this.getHeight(pos.getX() & 15, pos.getZ() & 15);
    }

    /**
     * Returns the value in the height map at this x, z coordinate in the chunk
     */
    public int getHeight(int x, int z)
    {
        return this.heightMap[z << 4 | x];
    }

    /**
     * Returns the topmost ExtendedBlockStorage instance for this Chunk that actually contains a block.
     */
    public int getTopFilledSegment()
    {
        for (int i = this.storageArrays.length - 1; i >= 0; --i)
        {
            if (this.storageArrays[i] != null)
            {
                return this.storageArrays[i].getYLocation();
            }
        }

        return 0;
    }

    /**
     * Returns the ExtendedBlockStorage array for this Chunk.
     */
    public ExtendedBlockStorage[] getBlockStorageArray()
    {
        return this.storageArrays;
    }

    /**
     * Generates the height map for a chunk from scratch
     */
    @SideOnly(Side.CLIENT)
    protected void generateHeightMap()
    {
        int i = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;

        for (int j = 0; j < 16; ++j)
        {
            int k = 0;

            while (k < 16)
            {
                this.precipitationHeightMap[j + (k << 4)] = -999;
                int l = i + 16;

                while (true)
                {
                    if (l > 0)
                    {
                        Block block = this.getBlock0(j, l - 1, k);

                        if (getBlockLightOpacity(j, l - 1, k) == 0)
                        {
                            --l;
                            continue;
                        }

                        this.heightMap[k << 4 | j] = l;

                        if (l < this.heightMapMinimum)
                        {
                            this.heightMapMinimum = l;
                        }
                    }

                    ++k;
                    break;
                }
            }
        }

        this.isModified = true;
    }

    /**
     * Generates the initial skylight map for the chunk upon generation or load.
     */
    public void generateSkylightMap()
    {
        int i = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;

        for (int j = 0; j < 16; ++j)
        {
            int k = 0;

            while (k < 16)
            {
                this.precipitationHeightMap[j + (k << 4)] = -999;
                int l = i + 16;

                while (true)
                {
                    if (l > 0)
                    {
                        if (this.getBlockLightOpacity(j, l - 1, k) == 0)
                        {
                            --l;
                            continue;
                        }

                        this.heightMap[k << 4 | j] = l;

                        if (l < this.heightMapMinimum)
                        {
                            this.heightMapMinimum = l;
                        }
                    }

                    if (!this.worldObj.provider.getHasNoSky())
                    {
                        l = 15;
                        int i1 = i + 16 - 1;

                        do
                        {
                            int j1 = this.getBlockLightOpacity(j, i1, k);

                            if (j1 == 0 && l != 15)
                            {
                                j1 = 1;
                            }

                            l -= j1;

                            if (l > 0)
                            {
                                ExtendedBlockStorage extendedblockstorage = this.storageArrays[i1 >> 4];

                                if (extendedblockstorage != null)
                                {
                                    extendedblockstorage.setExtSkylightValue(j, i1 & 15, k, l);
                                    this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + j, i1, (this.zPosition << 4) + k));
                                }
                            }

                            --i1;
                        }
                        while (i1 > 0 && l > 0);
                    }

                    ++k;
                    break;
                }
            }
        }

        this.isModified = true;
    }

    /**
     * Propagates a given sky-visible block's light value downward and upward to neighboring blocks as necessary.
     */
    private void propagateSkylightOcclusion(int x, int z)
    {
        this.updateSkylightColumns[x + z * 16] = true;
        this.isGapLightingUpdated = true;
    }

    private void recheckGaps(boolean p_150803_1_)
    {
        this.worldObj.theProfiler.startSection("recheckGaps");

        if (this.worldObj.isAreaLoaded(new BlockPos(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8), 16))
        {
            for (int i = 0; i < 16; ++i)
            {
                for (int j = 0; j < 16; ++j)
                {
                    if (this.updateSkylightColumns[i + j * 16])
                    {
                        this.updateSkylightColumns[i + j * 16] = false;
                        int k = this.getHeight(i, j);
                        int l = this.xPosition * 16 + i;
                        int i1 = this.zPosition * 16 + j;
                        int j1 = Integer.MAX_VALUE;
                        Iterator iterator;
                        EnumFacing enumfacing;

                        for (iterator = EnumFacing.Plane.HORIZONTAL.iterator(); iterator.hasNext(); j1 = Math.min(j1, this.worldObj.getChunksLowestHorizon(l + enumfacing.getFrontOffsetX(), i1 + enumfacing.getFrontOffsetZ())))
                        {
                            enumfacing = (EnumFacing)iterator.next();
                        }

                        this.checkSkylightNeighborHeight(l, i1, j1);
                        iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                        while (iterator.hasNext())
                        {
                            enumfacing = (EnumFacing)iterator.next();
                            this.checkSkylightNeighborHeight(l + enumfacing.getFrontOffsetX(), i1 + enumfacing.getFrontOffsetZ(), k);
                        }

                        if (p_150803_1_)
                        {
                            this.worldObj.theProfiler.endSection();
                            return;
                        }
                    }
                }
            }

            this.isGapLightingUpdated = false;
        }

        this.worldObj.theProfiler.endSection();
    }

    /**
     * Checks the height of a block next to a sky-visible block and schedules a lighting update as necessary.
     */
    private void checkSkylightNeighborHeight(int x, int p_76599_2_, int z)
    {
        int l = this.worldObj.getHorizon(new BlockPos(x, 0, p_76599_2_)).getY();

        if (l > z)
        {
            this.updateSkylightNeighborHeight(x, p_76599_2_, z, l + 1);
        }
        else if (l < z)
        {
            this.updateSkylightNeighborHeight(x, p_76599_2_, l, z + 1);
        }
    }

    private void updateSkylightNeighborHeight(int x, int z, int startY, int endY)
    {
        if (endY > startY && this.worldObj.isAreaLoaded(new BlockPos(x, 0, z), 16))
        {
            for (int i1 = startY; i1 < endY; ++i1)
            {
                this.worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, i1, z));
            }

            this.isModified = true;
        }
    }

    /**
     * Initiates the recalculation of both the block-light and sky-light for a given block inside a chunk.
     */
    private void relightBlock(int x, int y, int z)
    {
        int l = this.heightMap[z << 4 | x] & 255;
        int i1 = l;

        if (y > l)
        {
            i1 = y;
        }

        while (i1 > 0 && this.getBlockLightOpacity(x, i1 - 1, z) == 0)
        {
            --i1;
        }

        if (i1 != l)
        {
            this.worldObj.markBlocksDirtyVertical(x + this.xPosition * 16, z + this.zPosition * 16, i1, l);
            this.heightMap[z << 4 | x] = i1;
            int j1 = this.xPosition * 16 + x;
            int k1 = this.zPosition * 16 + z;
            int l1;
            int i2;

            if (!this.worldObj.provider.getHasNoSky())
            {
                ExtendedBlockStorage extendedblockstorage;

                if (i1 < l)
                {
                    for (l1 = i1; l1 < l; ++l1)
                    {
                        extendedblockstorage = this.storageArrays[l1 >> 4];

                        if (extendedblockstorage != null)
                        {
                            extendedblockstorage.setExtSkylightValue(x, l1 & 15, z, 15);
                            this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, l1, (this.zPosition << 4) + z));
                        }
                    }
                }
                else
                {
                    for (l1 = l; l1 < i1; ++l1)
                    {
                        extendedblockstorage = this.storageArrays[l1 >> 4];

                        if (extendedblockstorage != null)
                        {
                            extendedblockstorage.setExtSkylightValue(x, l1 & 15, z, 0);
                            this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, l1, (this.zPosition << 4) + z));
                        }
                    }
                }

                l1 = 15;

                while (i1 > 0 && l1 > 0)
                {
                    --i1;
                    i2 = this.getBlockLightOpacity(x, i1, z);

                    if (i2 == 0)
                    {
                        i2 = 1;
                    }

                    l1 -= i2;

                    if (l1 < 0)
                    {
                        l1 = 0;
                    }

                    ExtendedBlockStorage extendedblockstorage1 = this.storageArrays[i1 >> 4];

                    if (extendedblockstorage1 != null)
                    {
                        extendedblockstorage1.setExtSkylightValue(x, i1 & 15, z, l1);
                    }
                }
            }

            l1 = this.heightMap[z << 4 | x];
            i2 = l;
            int j2 = l1;

            if (l1 < l)
            {
                i2 = l1;
                j2 = l;
            }

            if (l1 < this.heightMapMinimum)
            {
                this.heightMapMinimum = l1;
            }

            if (!this.worldObj.provider.getHasNoSky())
            {
                Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                while (iterator.hasNext())
                {
                    EnumFacing enumfacing = (EnumFacing)iterator.next();
                    this.updateSkylightNeighborHeight(j1 + enumfacing.getFrontOffsetX(), k1 + enumfacing.getFrontOffsetZ(), i2, j2);
                }

                this.updateSkylightNeighborHeight(j1, k1, i2, j2);
            }

            this.isModified = true;
        }
    }

    public int getBlockLightOpacity(BlockPos pos)
    {
        return this.getBlock(pos).getLightOpacity(worldObj, pos);
    }

    private int getBlockLightOpacity(int x, int y, int z)
    {
        return getBlockLightOpacity(new BlockPos((xPosition << 4) + x, y, (zPosition << 4) + z));
    }

    /**
     * Returns the block corresponding to the given coordinates inside a chunk.
     */
    private Block getBlock0(int x, int y, int z)
    {
        Block block = Blocks.air;

        if (y >= 0 && y >> 4 < this.storageArrays.length)
        {
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[y >> 4];

            if (extendedblockstorage != null)
            {
                try
                {
                    block = extendedblockstorage.getBlockByExtId(x, y & 15, z);
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block");
                    throw new ReportedException(crashreport);
                }
            }
        }

        return block;
    }

    public Block getBlock(final int x, final int y, final int z)
    {
        try
        {
            return this.getBlock0(x & 15, y, z & 15);
        }
        catch (ReportedException reportedexception)
        {
            CrashReportCategory crashreportcategory = reportedexception.getCrashReport().makeCategory("Block being got");
            crashreportcategory.addCrashSectionCallable("Location", new Callable()
            {
                private static final String __OBFID = "CL_00000374";
                public String call()
                {
                    return CrashReportCategory.getCoordinateInfo(new BlockPos(Chunk.this.xPosition * 16 + x, y, Chunk.this.zPosition * 16 + z));
                }
            });
            throw reportedexception;
        }
    }

    public Block getBlock(final BlockPos pos)
    {
        try
        {
            return this.getBlock0(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
        }
        catch (ReportedException reportedexception)
        {
            CrashReportCategory crashreportcategory = reportedexception.getCrashReport().makeCategory("Block being got");
            crashreportcategory.addCrashSectionCallable("Location", new Callable()
            {
                private static final String __OBFID = "CL_00002011";
                public String call()
                {
                    return CrashReportCategory.getCoordinateInfo(pos);
                }
            });
            throw reportedexception;
        }
    }

    public IBlockState getBlockState(final BlockPos pos)
    {
        if (this.worldObj.getWorldType() == WorldType.DEBUG_WORLD)
        {
            IBlockState iblockstate = null;

            if (pos.getY() == 60)
            {
                iblockstate = Blocks.barrier.getDefaultState();
            }

            if (pos.getY() == 70)
            {
                iblockstate = ChunkProviderDebug.func_177461_b(pos.getX(), pos.getZ());
            }

            return iblockstate == null ? Blocks.air.getDefaultState() : iblockstate;
        }
        else
        {
            try
            {
                if (pos.getY() >= 0 && pos.getY() >> 4 < this.storageArrays.length)
                {
                    ExtendedBlockStorage extendedblockstorage = this.storageArrays[pos.getY() >> 4];

                    if (extendedblockstorage != null)
                    {
                        int j = pos.getX() & 15;
                        int k = pos.getY() & 15;
                        int i = pos.getZ() & 15;
                        return extendedblockstorage.get(j, k, i);
                    }
                }

                return Blocks.air.getDefaultState();
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block state");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
                crashreportcategory.addCrashSectionCallable("Location", new Callable()
                {
                    private static final String __OBFID = "CL_00002010";
                    public String call()
                    {
                        return CrashReportCategory.getCoordinateInfo(pos);
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    /**
     * Return the metadata corresponding to the given coordinates inside a chunk.
     */
    private int getBlockMetadata(int x, int y, int z)
    {
        if (y >> 4 >= this.storageArrays.length)
        {
            return 0;
        }
        else
        {
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[y >> 4];
            return extendedblockstorage != null ? extendedblockstorage.getExtBlockMetadata(x, y & 15, z) : 0;
        }
    }

    public int getBlockMetadata(BlockPos pos)
    {
        return this.getBlockMetadata(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
    }

    public IBlockState setBlockState(BlockPos pos, IBlockState state)
    {
        int i = pos.getX() & 15;
        int j = pos.getY();
        int k = pos.getZ() & 15;
        int l = k << 4 | i;

        if (j >= this.precipitationHeightMap[l] - 1)
        {
            this.precipitationHeightMap[l] = -999;
        }

        int i1 = this.heightMap[l];
        IBlockState iblockstate1 = this.getBlockState(pos);

        if (iblockstate1 == state)
        {
            return null;
        }
        else
        {
            Block block = state.getBlock();
            Block block1 = iblockstate1.getBlock();
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
            boolean flag = false;

            if (extendedblockstorage == null)
            {
                if (block == Blocks.air)
                {
                    return null;
                }

                extendedblockstorage = this.storageArrays[j >> 4] = new ExtendedBlockStorage(j >> 4 << 4, !this.worldObj.provider.getHasNoSky());
                flag = j >= i1;
            }

            int j1 = block.getLightOpacity(this.worldObj, pos);

            extendedblockstorage.set(i, j & 15, k, state);

            //if (block1 != block)
            {
                if (!this.worldObj.isRemote)
                {
                    if (iblockstate1.getBlock() != state.getBlock()) //Only fire block breaks when the block changes.
                    block1.breakBlock(this.worldObj, pos, iblockstate1);
                    TileEntity te = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                    if (te != null && te.shouldRefresh(this.worldObj, pos, iblockstate1, state)) this.worldObj.removeTileEntity(pos);
                }
                else if (block1.hasTileEntity(iblockstate1))
                {
                    TileEntity te = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);
                    if (te != null && te.shouldRefresh(this.worldObj, pos, iblockstate1, state))
                    this.worldObj.removeTileEntity(pos);
                }
            }

            if (extendedblockstorage.getBlockByExtId(i, j & 15, k) != block)
            {
                return null;
            }
            else
            {
                if (flag)
                {
                    this.generateSkylightMap();
                }
                else
                {
                    int k1 = block.getLightOpacity(this.worldObj, pos);

                    if (j1 > 0)
                    {
                        if (j >= i1)
                        {
                            this.relightBlock(i, j + 1, k);
                        }
                    }
                    else if (j == i1 - 1)
                    {
                        this.relightBlock(i, j, k);
                    }

                    if (j1 != k1 && (j1 < k1 || this.getLightFor(EnumSkyBlock.SKY, pos) > 0 || this.getLightFor(EnumSkyBlock.BLOCK, pos) > 0))
                    {
                        this.propagateSkylightOcclusion(i, k);
                    }
                }

                TileEntity tileentity;

                if (!this.worldObj.isRemote && block1 != block)
                {
                    block.onBlockAdded(this.worldObj, pos, state);
                }

                if (block.hasTileEntity(state))
                {
                    tileentity = this.getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK);

                    if (tileentity == null)
                    {
                        tileentity = block.createTileEntity(this.worldObj, state);
                        this.worldObj.setTileEntity(pos, tileentity);
                    }

                    if (tileentity != null)
                    {
                        tileentity.updateContainingBlockInfo();
                    }
                }

                this.isModified = true;
                return iblockstate1;
            }
        }
    }

    public int getLightFor(EnumSkyBlock p_177413_1_, BlockPos pos)
    {
        int i = pos.getX() & 15;
        int j = pos.getY();
        int k = pos.getZ() & 15;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
        return extendedblockstorage == null ? (this.canSeeSky(pos) ? p_177413_1_.defaultLightValue : 0) : (p_177413_1_ == EnumSkyBlock.SKY ? (this.worldObj.provider.getHasNoSky() ? 0 : extendedblockstorage.getExtSkylightValue(i, j & 15, k)) : (p_177413_1_ == EnumSkyBlock.BLOCK ? extendedblockstorage.getExtBlocklightValue(i, j & 15, k) : p_177413_1_.defaultLightValue));
    }

    public void setLightFor(EnumSkyBlock p_177431_1_, BlockPos pos, int value)
    {
        int j = pos.getX() & 15;
        int k = pos.getY();
        int l = pos.getZ() & 15;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[k >> 4];

        if (extendedblockstorage == null)
        {
            extendedblockstorage = this.storageArrays[k >> 4] = new ExtendedBlockStorage(k >> 4 << 4, !this.worldObj.provider.getHasNoSky());
            this.generateSkylightMap();
        }

        this.isModified = true;

        if (p_177431_1_ == EnumSkyBlock.SKY)
        {
            if (!this.worldObj.provider.getHasNoSky())
            {
                extendedblockstorage.setExtSkylightValue(j, k & 15, l, value);
            }
        }
        else if (p_177431_1_ == EnumSkyBlock.BLOCK)
        {
            extendedblockstorage.setExtBlocklightValue(j, k & 15, l, value);
        }
    }

    public int setLight(BlockPos pos, int p_177443_2_)
    {
        int j = pos.getX() & 15;
        int k = pos.getY();
        int l = pos.getZ() & 15;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[k >> 4];

        if (extendedblockstorage == null)
        {
            return !this.worldObj.provider.getHasNoSky() && p_177443_2_ < EnumSkyBlock.SKY.defaultLightValue ? EnumSkyBlock.SKY.defaultLightValue - p_177443_2_ : 0;
        }
        else
        {
            int i1 = this.worldObj.provider.getHasNoSky() ? 0 : extendedblockstorage.getExtSkylightValue(j, k & 15, l);
            i1 -= p_177443_2_;
            int j1 = extendedblockstorage.getExtBlocklightValue(j, k & 15, l);

            if (j1 > i1)
            {
                i1 = j1;
            }

            return i1;
        }
    }

    /**
     * Adds an entity to the chunk. Args: entity
     */
    public void addEntity(Entity entityIn)
    {
        this.hasEntities = true;
        int i = MathHelper.floor_double(entityIn.posX / 16.0D);
        int j = MathHelper.floor_double(entityIn.posZ / 16.0D);

        if (i != this.xPosition || j != this.zPosition)
        {
            logger.warn("Wrong location! (" + i + ", " + j + ") should be (" + this.xPosition + ", " + this.zPosition + "), " + entityIn, new Object[] {entityIn});
            entityIn.setDead();
        }

        int k = MathHelper.floor_double(entityIn.posY / 16.0D);

        if (k < 0)
        {
            k = 0;
        }

        if (k >= this.entityLists.length)
        {
            k = this.entityLists.length - 1;
        }

        MinecraftForge.EVENT_BUS.post(new EntityEvent.EnteringChunk(entityIn, this.xPosition, this.zPosition, entityIn.chunkCoordX, entityIn.chunkCoordZ));
        entityIn.addedToChunk = true;
        entityIn.chunkCoordX = this.xPosition;
        entityIn.chunkCoordY = k;
        entityIn.chunkCoordZ = this.zPosition;
        this.entityLists[k].add(entityIn);
    }

    /**
     * removes entity using its y chunk coordinate as its index
     */
    public void removeEntity(Entity p_76622_1_)
    {
        this.removeEntityAtIndex(p_76622_1_, p_76622_1_.chunkCoordY);
    }

    /**
     * Removes entity at the specified index from the entity array.
     */
    public void removeEntityAtIndex(Entity p_76608_1_, int p_76608_2_)
    {
        if (p_76608_2_ < 0)
        {
            p_76608_2_ = 0;
        }

        if (p_76608_2_ >= this.entityLists.length)
        {
            p_76608_2_ = this.entityLists.length - 1;
        }

        this.entityLists[p_76608_2_].remove(p_76608_1_);
    }

    public boolean canSeeSky(BlockPos pos)
    {
        int i = pos.getX() & 15;
        int j = pos.getY();
        int k = pos.getZ() & 15;
        return j >= this.heightMap[k << 4 | i];
    }

    private TileEntity createNewTileEntity(BlockPos pos)
    {
        Block block = this.getBlock(pos);
        IBlockState state = block.getStateFromMeta(this.getBlockMetadata(pos));
        return !block.hasTileEntity(state) ? null : block.createTileEntity(this.worldObj, state);
    }

    public TileEntity getTileEntity(BlockPos pos, Chunk.EnumCreateEntityType p_177424_2_)
    {
        TileEntity tileentity = (TileEntity)this.chunkTileEntityMap.get(pos);

        if (tileentity != null && tileentity.isInvalid())
        {
            chunkTileEntityMap.remove(pos);
            tileentity = null;
        }

        if (tileentity == null)
        {
            if (p_177424_2_ == Chunk.EnumCreateEntityType.IMMEDIATE)
            {
                tileentity = this.createNewTileEntity(pos);
                this.worldObj.setTileEntity(pos, tileentity);
            }
            else if (p_177424_2_ == Chunk.EnumCreateEntityType.QUEUED)
            {
                this.field_177447_w.add(pos);
            }
        }

        return tileentity;
    }

    public void addTileEntity(TileEntity tileEntityIn)
    {
        this.addTileEntity(tileEntityIn.getPos(), tileEntityIn);

        if (this.isChunkLoaded)
        {
            this.worldObj.addTileEntity(tileEntityIn);
        }
    }

    public void addTileEntity(BlockPos pos, TileEntity tileEntityIn)
    {
        tileEntityIn.setWorldObj(this.worldObj);
        tileEntityIn.setPos(pos);

        if (this.getBlock(pos).hasTileEntity(getBlock(pos).getStateFromMeta(this.getBlockMetadata(pos))))
        {
            if (this.chunkTileEntityMap.containsKey(pos))
            {
                ((TileEntity)this.chunkTileEntityMap.get(pos)).invalidate();
            }

            tileEntityIn.validate();
            this.chunkTileEntityMap.put(pos, tileEntityIn);
        }
    }

    public void removeTileEntity(BlockPos pos)
    {
        if (this.isChunkLoaded)
        {
            TileEntity tileentity = (TileEntity)this.chunkTileEntityMap.remove(pos);

            if (tileentity != null)
            {
                tileentity.invalidate();
            }
        }
    }

    /**
     * Called when this Chunk is loaded by the ChunkProvider
     */
    public void onChunkLoad()
    {
        this.isChunkLoaded = true;
        this.worldObj.addTileEntities(this.chunkTileEntityMap.values());

        for (int i = 0; i < this.entityLists.length; ++i)
        {
            Iterator iterator = this.entityLists[i].iterator();

            while (iterator.hasNext())
            {
                Entity entity = (Entity)iterator.next();
                entity.onChunkLoad();
            }

            this.worldObj.loadEntities(this.entityLists[i]);
        }
        MinecraftForge.EVENT_BUS.post(new ChunkEvent.Load(this));
    }

    /**
     * Called when this Chunk is unloaded by the ChunkProvider
     */
    public void onChunkUnload()
    {
        this.isChunkLoaded = false;
        Iterator iterator = this.chunkTileEntityMap.values().iterator();

        while (iterator.hasNext())
        {
            TileEntity tileentity = (TileEntity)iterator.next();
            this.worldObj.markTileEntityForRemoval(tileentity);
        }

        for (int i = 0; i < this.entityLists.length; ++i)
        {
            this.worldObj.unloadEntities(this.entityLists[i]);
        }
        MinecraftForge.EVENT_BUS.post(new ChunkEvent.Unload(this));
    }

    /**
     * Sets the isModified flag for this Chunk
     */
    public void setChunkModified()
    {
        this.isModified = true;
    }

    /**
     * Fills the given list of all entities that intersect within the given bounding box that aren't the passed entity.
     */
    public void getEntitiesWithinAABBForEntity(Entity entityIn, AxisAlignedBB aabb, List listToFill, Predicate p_177414_4_)
    {
        int i = MathHelper.floor_double((aabb.minY - World.MAX_ENTITY_RADIUS) / 16.0D);
        int j = MathHelper.floor_double((aabb.maxY + World.MAX_ENTITY_RADIUS) / 16.0D);
        i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);

        for (int k = i; k <= j; ++k)
        {
            Iterator iterator = this.entityLists[k].iterator();

            while (iterator.hasNext())
            {
                Entity entity1 = (Entity)iterator.next();

                if (entity1 != entityIn && entity1.getEntityBoundingBox().intersectsWith(aabb) && (p_177414_4_ == null || p_177414_4_.apply(entity1)))
                {
                    listToFill.add(entity1);
                    Entity[] aentity = entity1.getParts();

                    if (aentity != null)
                    {
                        for (int l = 0; l < aentity.length; ++l)
                        {
                            entity1 = aentity[l];

                            if (entity1 != entityIn && entity1.getEntityBoundingBox().intersectsWith(aabb) && (p_177414_4_ == null || p_177414_4_.apply(entity1)))
                            {
                                listToFill.add(entity1);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets all entities that can be assigned to the specified class.
     */
    public void getEntitiesOfTypeWithinAAAB(Class entityClass, AxisAlignedBB aabb, List listToFill, Predicate p_177430_4_)
    {
        int i = MathHelper.floor_double((aabb.minY - World.MAX_ENTITY_RADIUS) / 16.0D);
        int j = MathHelper.floor_double((aabb.maxY + World.MAX_ENTITY_RADIUS) / 16.0D);
        i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);

        for (int k = i; k <= j; ++k)
        {
            Iterator iterator = this.entityLists[k].func_180215_b(entityClass).iterator();

            while (iterator.hasNext())
            {
                Entity entity = (Entity)iterator.next();

                if (entity.getEntityBoundingBox().intersectsWith(aabb) && (p_177430_4_ == null || p_177430_4_.apply(entity)))
                {
                    listToFill.add(entity);
                }
            }
        }
    }

    /**
     * Returns true if this Chunk needs to be saved
     */
    public boolean needsSaving(boolean p_76601_1_)
    {
        if (p_76601_1_)
        {
            if (this.hasEntities && this.worldObj.getTotalWorldTime() != this.lastSaveTime || this.isModified)
            {
                return true;
            }
        }
        else if (this.hasEntities && this.worldObj.getTotalWorldTime() >= this.lastSaveTime + 600L)
        {
            return true;
        }

        return this.isModified;
    }

    public Random getRandomWithSeed(long seed)
    {
        return new Random(this.worldObj.getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ seed);
    }

    public boolean isEmpty()
    {
        return false;
    }

    public void populateChunk(IChunkProvider p_76624_1_, IChunkProvider p_76624_2_, int p_76624_3_, int p_76624_4_)
    {
        boolean flag = p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ - 1);
        boolean flag1 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_);
        boolean flag2 = p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ + 1);
        boolean flag3 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_);
        boolean flag4 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ - 1);
        boolean flag5 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ + 1);
        boolean flag6 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ + 1);
        boolean flag7 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ - 1);

        if (flag1 && flag2 && flag5)
        {
            if (!this.isTerrainPopulated)
            {
                p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_);
            }
            else
            {
                p_76624_1_.func_177460_a(p_76624_2_, this, p_76624_3_, p_76624_4_);
            }
        }

        Chunk chunk;

        if (flag3 && flag2 && flag6)
        {
            chunk = p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_);

            if (!chunk.isTerrainPopulated)
            {
                p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_);
            }
            else
            {
                p_76624_1_.func_177460_a(p_76624_2_, chunk, p_76624_3_ - 1, p_76624_4_);
            }
        }

        if (flag && flag1 && flag7)
        {
            chunk = p_76624_1_.provideChunk(p_76624_3_, p_76624_4_ - 1);

            if (!chunk.isTerrainPopulated)
            {
                p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_ - 1);
            }
            else
            {
                p_76624_1_.func_177460_a(p_76624_2_, chunk, p_76624_3_, p_76624_4_ - 1);
            }
        }

        if (flag4 && flag && flag3)
        {
            chunk = p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_ - 1);

            if (!chunk.isTerrainPopulated)
            {
                p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_ - 1);
            }
            else
            {
                p_76624_1_.func_177460_a(p_76624_2_, chunk, p_76624_3_ - 1, p_76624_4_ - 1);
            }
        }
    }

    public BlockPos getPrecipitationHeight(BlockPos pos)
    {
        int i = pos.getX() & 15;
        int j = pos.getZ() & 15;
        int k = i | j << 4;
        BlockPos blockpos1 = new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());

        if (blockpos1.getY() == -999)
        {
            int l = this.getTopFilledSegment() + 15;
            blockpos1 = new BlockPos(pos.getX(), l, pos.getZ());
            int i1 = -1;

            while (blockpos1.getY() > 0 && i1 == -1)
            {
                Block block = this.getBlock(blockpos1);
                Material material = block.getMaterial();

                if (!material.blocksMovement() && !material.isLiquid())
                {
                    blockpos1 = blockpos1.down();
                }
                else
                {
                    i1 = blockpos1.getY() + 1;
                }
            }

            this.precipitationHeightMap[k] = i1;
        }

        return new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
    }

    public void func_150804_b(boolean p_150804_1_)
    {
        if (this.isGapLightingUpdated && !this.worldObj.provider.getHasNoSky() && !p_150804_1_)
        {
            this.recheckGaps(this.worldObj.isRemote);
        }

        this.field_150815_m = true;

        if (!this.isLightPopulated && this.isTerrainPopulated)
        {
            this.func_150809_p();
        }

        while (!this.field_177447_w.isEmpty())
        {
            BlockPos blockpos = (BlockPos)this.field_177447_w.poll();
            Block block = this.getBlock(blockpos);
            IBlockState state = block.getStateFromMeta(this.getBlockMetadata(blockpos));

            if (this.getTileEntity(blockpos, Chunk.EnumCreateEntityType.CHECK) == null && block.hasTileEntity(state))
            {
                TileEntity tileentity = this.createNewTileEntity(blockpos);
                this.worldObj.setTileEntity(blockpos, tileentity);
                this.worldObj.markBlockRangeForRenderUpdate(blockpos, blockpos);
            }
        }
    }

    public boolean isPopulated()
    {
        return this.field_150815_m && this.isTerrainPopulated && this.isLightPopulated;
    }

    /**
     * Gets a ChunkCoordIntPair representing the Chunk's position.
     */
    public ChunkCoordIntPair getChunkCoordIntPair()
    {
        return new ChunkCoordIntPair(this.xPosition, this.zPosition);
    }

    /**
     * Returns whether the ExtendedBlockStorages containing levels (in blocks) from arg 1 to arg 2 are fully empty
     * (true) or not (false).
     */
    public boolean getAreLevelsEmpty(int startY, int endY)
    {
        if (startY < 0)
        {
            startY = 0;
        }

        if (endY >= 256)
        {
            endY = 255;
        }

        for (int k = startY; k <= endY; k += 16)
        {
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[k >> 4];

            if (extendedblockstorage != null && !extendedblockstorage.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    public void setStorageArrays(ExtendedBlockStorage[] newStorageArrays)
    {
        if (this.storageArrays.length != newStorageArrays.length)
        {
            logger.warn("Could not set level chunk sections, array length is " + newStorageArrays.length + " instead of " + this.storageArrays.length);
        }
        else
        {
            for (int i = 0; i < this.storageArrays.length; ++i)
            {
                this.storageArrays[i] = newStorageArrays[i];
            }
        }
    }

    /**
     * Initialize this chunk with new binary data.
     */
    @SideOnly(Side.CLIENT)
    public void fillChunk(byte[] p_177439_1_, int p_177439_2_, boolean p_177439_3_)
    {
        Iterator iterator = chunkTileEntityMap.values().iterator();
        while(iterator.hasNext())
        {
            TileEntity tileEntity = (TileEntity)iterator.next();
            tileEntity.updateContainingBlockInfo();
            tileEntity.getBlockMetadata();
            tileEntity.getBlockType();
        }

        int j = 0;
        boolean flag1 = !this.worldObj.provider.getHasNoSky();
        int k;

        for (k = 0; k < this.storageArrays.length; ++k)
        {
            if ((p_177439_2_ & 1 << k) != 0)
            {
                if (this.storageArrays[k] == null)
                {
                    this.storageArrays[k] = new ExtendedBlockStorage(k << 4, flag1);
                }

                char[] achar = this.storageArrays[k].getData();

                for (int l = 0; l < achar.length; ++l)
                {
                    achar[l] = (char)((p_177439_1_[j + 1] & 255) << 8 | p_177439_1_[j] & 255);
                    j += 2;
                }
            }
            else if (p_177439_3_ && this.storageArrays[k] != null)
            {
                this.storageArrays[k] = null;
            }
        }

        NibbleArray nibblearray;

        for (k = 0; k < this.storageArrays.length; ++k)
        {
            if ((p_177439_2_ & 1 << k) != 0 && this.storageArrays[k] != null)
            {
                nibblearray = this.storageArrays[k].getBlocklightArray();
                System.arraycopy(p_177439_1_, j, nibblearray.getData(), 0, nibblearray.getData().length);
                j += nibblearray.getData().length;
            }
        }

        if (flag1)
        {
            for (k = 0; k < this.storageArrays.length; ++k)
            {
                if ((p_177439_2_ & 1 << k) != 0 && this.storageArrays[k] != null)
                {
                    nibblearray = this.storageArrays[k].getSkylightArray();
                    System.arraycopy(p_177439_1_, j, nibblearray.getData(), 0, nibblearray.getData().length);
                    j += nibblearray.getData().length;
                }
            }
        }

        if (p_177439_3_)
        {
            System.arraycopy(p_177439_1_, j, this.blockBiomeArray, 0, this.blockBiomeArray.length);
            int i1 = j + this.blockBiomeArray.length;
        }

        for (k = 0; k < this.storageArrays.length; ++k)
        {
            if (this.storageArrays[k] != null && (p_177439_2_ & 1 << k) != 0)
            {
                this.storageArrays[k].removeInvalidBlocks();
            }
        }

        this.isLightPopulated = true;
        this.isTerrainPopulated = true;
        this.generateHeightMap();
        List<TileEntity> invalidList = new java.util.ArrayList<TileEntity>();
        iterator = this.chunkTileEntityMap.values().iterator();

        while (iterator.hasNext())
        {
            TileEntity tileentity = (TileEntity)iterator.next();
            if (tileentity.shouldRefresh(this.worldObj, tileentity.getPos(), tileentity.getBlockType().getStateFromMeta(tileentity.getBlockMetadata()), getBlockState(tileentity.getPos())))
                invalidList.add(tileentity);
            tileentity.updateContainingBlockInfo();
        }

        for (TileEntity te : invalidList) te.invalidate();
    }

    public BiomeGenBase getBiome(BlockPos pos, WorldChunkManager chunkManager)
    {
        int i = pos.getX() & 15;
        int j = pos.getZ() & 15;
        int k = this.blockBiomeArray[j << 4 | i] & 255;
        BiomeGenBase biomegenbase;

        if (k == 255)
        {
            biomegenbase = chunkManager.func_180300_a(pos, BiomeGenBase.plains);
            k = biomegenbase.biomeID;
            this.blockBiomeArray[j << 4 | i] = (byte)(k & 255);
        }

        biomegenbase = BiomeGenBase.getBiome(k);
        return biomegenbase == null ? BiomeGenBase.plains : biomegenbase;
    }

    /**
     * Returns an array containing a 16x16 mapping on the X/Z of block positions in this Chunk to biome IDs.
     */
    public byte[] getBiomeArray()
    {
        return this.blockBiomeArray;
    }

    /**
     * Accepts a 256-entry array that contains a 16x16 mapping on the X/Z plane of block positions in this Chunk to
     * biome IDs.
     */
    public void setBiomeArray(byte[] biomeArray)
    {
        if (this.blockBiomeArray.length != biomeArray.length)
        {
            logger.warn("Could not set level chunk biomes, array length is " + biomeArray.length + " instead of " + this.blockBiomeArray.length);
        }
        else
        {
            for (int i = 0; i < this.blockBiomeArray.length; ++i)
            {
                this.blockBiomeArray[i] = biomeArray[i];
            }
        }
    }

    /**
     * Resets the relight check index to 0 for this Chunk.
     */
    public void resetRelightChecks()
    {
        this.queuedLightChecks = 0;
    }

    /**
     * Called once-per-chunk-per-tick, and advances the round-robin relight check index by up to 8 blocks at a time. In
     * a worst-case scenario, can potentially take up to 25.6 seconds, calculated via (4096/8)/20, to re-check all
     * blocks in a chunk, which may explain lagging light updates on initial world generation.
     */
    public void enqueueRelightChecks()
    {
        BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);

        for (int i = 0; i < 8; ++i)
        {
            if (this.queuedLightChecks >= 4096)
            {
                return;
            }

            int j = this.queuedLightChecks % 16;
            int k = this.queuedLightChecks / 16 % 16;
            int l = this.queuedLightChecks / 256;
            ++this.queuedLightChecks;

            for (int i1 = 0; i1 < 16; ++i1)
            {
                BlockPos blockpos1 = blockpos.add(k, (j << 4) + i1, l);
                boolean flag = i1 == 0 || i1 == 15 || k == 0 || k == 15 || l == 0 || l == 15;

                if (this.storageArrays[j] == null && flag || this.storageArrays[j] != null && this.storageArrays[j].getBlockByExtId(k, i1, l).getMaterial() == Material.air)
                {
                    EnumFacing[] aenumfacing = EnumFacing.values();
                    int j1 = aenumfacing.length;

                    for (int k1 = 0; k1 < j1; ++k1)
                    {
                        EnumFacing enumfacing = aenumfacing[k1];
                        BlockPos blockpos2 = blockpos1.offset(enumfacing);

                        if (this.worldObj.getBlockState(blockpos2).getBlock().getLightValue() > 0)
                        {
                            this.worldObj.checkLight(blockpos2);
                        }
                    }

                    this.worldObj.checkLight(blockpos1);
                }
            }
        }
    }

    public void func_150809_p()
    {
        this.isTerrainPopulated = true;
        this.isLightPopulated = true;
        BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);

        if (!this.worldObj.provider.getHasNoSky())
        {
            if (this.worldObj.isAreaLoaded(blockpos.add(-1, 0, -1), blockpos.add(16, 63, 16)))
            {
                label42:

                for (int i = 0; i < 16; ++i)
                {
                    for (int j = 0; j < 16; ++j)
                    {
                        if (!this.func_150811_f(i, j))
                        {
                            this.isLightPopulated = false;
                            break label42;
                        }
                    }
                }

                if (this.isLightPopulated)
                {
                    Iterator iterator = EnumFacing.Plane.HORIZONTAL.iterator();

                    while (iterator.hasNext())
                    {
                        EnumFacing enumfacing = (EnumFacing)iterator.next();
                        int k = enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 16 : 1;
                        this.worldObj.getChunkFromBlockCoords(blockpos.offset(enumfacing, k)).func_180700_a(enumfacing.getOpposite());
                    }

                    this.func_177441_y();
                }
            }
            else
            {
                this.isLightPopulated = false;
            }
        }
    }

    private void func_177441_y()
    {
        for (int i = 0; i < this.updateSkylightColumns.length; ++i)
        {
            this.updateSkylightColumns[i] = true;
        }

        this.recheckGaps(false);
    }

    private void func_180700_a(EnumFacing p_180700_1_)
    {
        if (this.isTerrainPopulated)
        {
            int i;

            if (p_180700_1_ == EnumFacing.EAST)
            {
                for (i = 0; i < 16; ++i)
                {
                    this.func_150811_f(15, i);
                }
            }
            else if (p_180700_1_ == EnumFacing.WEST)
            {
                for (i = 0; i < 16; ++i)
                {
                    this.func_150811_f(0, i);
                }
            }
            else if (p_180700_1_ == EnumFacing.SOUTH)
            {
                for (i = 0; i < 16; ++i)
                {
                    this.func_150811_f(i, 15);
                }
            }
            else if (p_180700_1_ == EnumFacing.NORTH)
            {
                for (i = 0; i < 16; ++i)
                {
                    this.func_150811_f(i, 0);
                }
            }
        }
    }

    private boolean func_150811_f(int x, int z)
    {
        BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
        int k = this.getTopFilledSegment();
        boolean flag = false;
        boolean flag1 = false;
        int l;
        BlockPos blockpos1;

        for (l = k + 16 - 1; l > 63 || l > 0 && !flag1; --l)
        {
            blockpos1 = blockpos.add(x, l, z);
            int i1 = this.getBlockLightOpacity(blockpos1);

            if (i1 == 255 && l < 63)
            {
                flag1 = true;
            }

            if (!flag && i1 > 0)
            {
                flag = true;
            }
            else if (flag && i1 == 0 && !this.worldObj.checkLight(blockpos1))
            {
                return false;
            }
        }

        for (; l > 0; --l)
        {
            blockpos1 = blockpos.add(x, l, z);

            if (this.getBlock(blockpos1).getLightValue() > 0)
            {
                this.worldObj.checkLight(blockpos1);
            }
        }

        return true;
    }

    public boolean isLoaded()
    {
        return this.isChunkLoaded;
    }

    @SideOnly(Side.CLIENT)
    public void setChunkLoaded(boolean loaded)
    {
        this.isChunkLoaded = loaded;
    }

    public World getWorld()
    {
        return this.worldObj;
    }

    public int[] getHeightMap()
    {
        return this.heightMap;
    }

    public void setHeightMap(int[] newHeightMap)
    {
        if (this.heightMap.length != newHeightMap.length)
        {
            logger.warn("Could not set level chunk heightmap, array length is " + newHeightMap.length + " instead of " + this.heightMap.length);
        }
        else
        {
            for (int i = 0; i < this.heightMap.length; ++i)
            {
                this.heightMap[i] = newHeightMap[i];
            }
        }
    }

    public Map getTileEntityMap()
    {
        return this.chunkTileEntityMap;
    }

    public ClassInheritanceMultiMap[] getEntityLists()
    {
        return this.entityLists;
    }

    public boolean isTerrainPopulated()
    {
        return this.isTerrainPopulated;
    }

    public void setTerrainPopulated(boolean terrainPopulated)
    {
        this.isTerrainPopulated = terrainPopulated;
    }

    public boolean isLightPopulated()
    {
        return this.isLightPopulated;
    }

    public void setLightPopulated(boolean lightPopulated)
    {
        this.isLightPopulated = lightPopulated;
    }

    public void setModified(boolean modified)
    {
        this.isModified = modified;
    }

    public void setHasEntities(boolean hasEntitiesIn)
    {
        this.hasEntities = hasEntitiesIn;
    }

    public void setLastSaveTime(long saveTime)
    {
        this.lastSaveTime = saveTime;
    }

    public int getLowestHeight()
    {
        return this.heightMapMinimum;
    }

    public long getInhabitedTime()
    {
        return this.inhabitedTime;
    }

    public void setInhabitedTime(long newInhabitedTime)
    {
        this.inhabitedTime = newInhabitedTime;
    }

    public static enum EnumCreateEntityType
    {
        IMMEDIATE,
        QUEUED,
        CHECK;

        private static final String __OBFID = "CL_00002009";
    }

    /**
     * Removes the tile entity at the specified position, only if it's
     * marked as invalid.
     */
    public void removeInvalidTileEntity(BlockPos pos)
    {
        if (isChunkLoaded)
        {
            TileEntity entity = (TileEntity)chunkTileEntityMap.get(pos);
            if (entity != null && entity.isInvalid())
            {
                chunkTileEntityMap.remove(pos);
            }
        }
    }
}