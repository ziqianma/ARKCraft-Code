package net.minecraft.world.chunk.storage;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.storage.IThreadedFileIO;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.ChunkDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO
{
    private static final Logger logger = LogManager.getLogger();
    private List chunksToRemove = Lists.newArrayList();
    private Set pendingAnvilChunksCoordinates = Sets.newHashSet();
    private Object syncLockObject = new Object();
    /** Save directory for chunks using the Anvil format */
    public final File chunkSaveLocation;
    private static final String __OBFID = "CL_00000384";

    public AnvilChunkLoader(File chunkSaveLocationIn)
    {
        this.chunkSaveLocation = chunkSaveLocationIn;
    }

    public boolean chunkExists(World world, int x, int z)
    {
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(x, z);

        synchronized (this.syncLockObject)
        {
            if (this.pendingAnvilChunksCoordinates.contains(chunkcoordintpair))
            {
                Iterator iter = this.chunksToRemove.iterator();
                while (iter.hasNext())
                {
                    PendingChunk pendingChunk = (PendingChunk)iter.next();
                    if (pendingChunk.chunkCoordinate.equals(chunkcoordintpair))
                    {
                        return true;
                    }
                }
            }
        }

        return RegionFileCache.createOrLoadRegionFile(this.chunkSaveLocation, x, z).chunkExists(x & 31, z & 31);
    }
    /**
     * Loads the specified(XZ) chunk into the specified world.
     */
    public Chunk loadChunk(World worldIn, int x, int z) throws IOException
    {
        Object[] data = this.loadChunk__Async(worldIn, x, z);

        if (data != null)
        {
            Chunk chunk = (Chunk) data[0];
            NBTTagCompound nbttagcompound = (NBTTagCompound) data[1];
            this.loadEntities(worldIn, nbttagcompound.getCompoundTag("Level"), chunk);
            return chunk;
        }

        return null;
    }

    public Object[] loadChunk__Async(World worldIn, int x, int z) throws IOException
    {
        NBTTagCompound nbttagcompound = null;
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(x, z);
        Object object = this.syncLockObject;

        synchronized (this.syncLockObject)
        {
            if (this.pendingAnvilChunksCoordinates.contains(chunkcoordintpair))
            {
                Iterator iter = this.chunksToRemove.iterator();
                while (iter.hasNext())
                {
                    PendingChunk pendingChunk = (PendingChunk)iter.next();
                    if (pendingChunk.chunkCoordinate.equals(chunkcoordintpair))
                    {
                        nbttagcompound = pendingChunk.nbtTags;
                        break;
                    }
                }
            }
        }

        if (nbttagcompound == null)
        {
            DataInputStream datainputstream = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, x, z);

            if (datainputstream == null)
            {
                return null;
            }

            nbttagcompound = CompressedStreamTools.read(datainputstream);
        }

        return this.checkedReadChunkFromNBT__Async(worldIn, x, z, nbttagcompound);
    }

    /**
     * Wraps readChunkFromNBT. Checks the coordinates and several NBT tags.
     *  
     * @param x Chunk X Coordinate
     * @param z Chunk Z Coordinate
     */
    protected Chunk checkedReadChunkFromNBT(World worldIn, int x, int z, NBTTagCompound p_75822_4_)
    {
        Object[] data = this.checkedReadChunkFromNBT__Async(worldIn, x, z, p_75822_4_);
        return data != null ? (Chunk)data[0] : null;
    }

    protected Object[] checkedReadChunkFromNBT__Async(World worldIn, int x, int z, NBTTagCompound p_75822_4_)
    {
        if (!p_75822_4_.hasKey("Level", 10))
        {
            logger.error("Chunk file at " + x + "," + z + " is missing level data, skipping");
            return null;
        }
        else if (!p_75822_4_.getCompoundTag("Level").hasKey("Sections", 9))
        {
            logger.error("Chunk file at " + x + "," + z + " is missing block data, skipping");
            return null;
        }
        else
        {
            Chunk chunk = this.readChunkFromNBT(worldIn, p_75822_4_.getCompoundTag("Level"));

            if (!chunk.isAtLocation(x, z))
            {
                logger.error("Chunk file at " + x + "," + z + " is in the wrong location; relocating. (Expected " + x + ", " + z + ", got " + chunk.xPosition + ", " + chunk.zPosition + ")");
                p_75822_4_.setInteger("xPos", x);
                p_75822_4_.setInteger("zPos", z);

                // Have to move tile entities since we don't load them at this stage
                NBTTagList _tileEntities = p_75822_4_.getCompoundTag("Level").getTagList("TileEntities", 10);

                if (_tileEntities != null)
                {
                    for (int te = 0; te < _tileEntities.tagCount(); te++)
                    {
                        NBTTagCompound _nbt = (NBTTagCompound) _tileEntities.getCompoundTagAt(te);
                        _nbt.setInteger("x", x * 16 + (_nbt.getInteger("x") - chunk.xPosition * 16));
                        _nbt.setInteger("z", z * 16 + (_nbt.getInteger("z") - chunk.zPosition * 16));
                    }
                }
                chunk = this.readChunkFromNBT(worldIn, p_75822_4_.getCompoundTag("Level"));
            }
            Object[] data = new Object[2];
            data[0] = chunk;
            data[1] = p_75822_4_;
            // event is fired in ChunkIOProvider.callStage2 since it must be fired after TE's load.
            // MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Load(chunk, par4NBTTagCompound));
            return data;
        }
    }

    public void saveChunk(World worldIn, Chunk chunkIn) throws MinecraftException, IOException
    {
        worldIn.checkSessionLock();

        try
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound.setTag("Level", nbttagcompound1);
            this.writeChunkToNBT(chunkIn, worldIn, nbttagcompound1);
            MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Save(chunkIn, nbttagcompound));
            this.addChunkToPending(chunkIn.getChunkCoordIntPair(), nbttagcompound);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    protected void addChunkToPending(ChunkCoordIntPair p_75824_1_, NBTTagCompound p_75824_2_)
    {
        Object object = this.syncLockObject;

        synchronized (this.syncLockObject)
        {
            if (this.pendingAnvilChunksCoordinates.contains(p_75824_1_))
            {
                for (int i = 0; i < this.chunksToRemove.size(); ++i)
                {
                    if (((AnvilChunkLoader.PendingChunk)this.chunksToRemove.get(i)).chunkCoordinate.equals(p_75824_1_))
                    {
                        this.chunksToRemove.set(i, new AnvilChunkLoader.PendingChunk(p_75824_1_, p_75824_2_));
                        return;
                    }
                }
            }

            this.chunksToRemove.add(new AnvilChunkLoader.PendingChunk(p_75824_1_, p_75824_2_));
            this.pendingAnvilChunksCoordinates.add(p_75824_1_);
            ThreadedFileIOBase.getThreadedIOInstance().queueIO(this);
        }
    }

    /**
     * Returns a boolean stating if the write was unsuccessful.
     */
    public boolean writeNextIO()
    {
        AnvilChunkLoader.PendingChunk pendingchunk = null;
        Object object = this.syncLockObject;

        synchronized (this.syncLockObject)
        {
            if (this.chunksToRemove.isEmpty())
            {
                return false;
            }

            pendingchunk = (AnvilChunkLoader.PendingChunk)this.chunksToRemove.remove(0);
            this.pendingAnvilChunksCoordinates.remove(pendingchunk.chunkCoordinate);
        }

        if (pendingchunk != null)
        {
            try
            {
                this.writeChunkNBTTags(pendingchunk);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }

        return true;
    }

    private void writeChunkNBTTags(AnvilChunkLoader.PendingChunk p_75821_1_) throws IOException
    {
        DataOutputStream dataoutputstream = RegionFileCache.getChunkOutputStream(this.chunkSaveLocation, p_75821_1_.chunkCoordinate.chunkXPos, p_75821_1_.chunkCoordinate.chunkZPos);
        CompressedStreamTools.write(p_75821_1_.nbtTags, dataoutputstream);
        dataoutputstream.close();
    }

    /**
     * Save extra data associated with this Chunk not normally saved during autosave, only during chunk unload.
     * Currently unused.
     */
    public void saveExtraChunkData(World worldIn, Chunk chunkIn) {}

    /**
     * Called every World.tick()
     */
    public void chunkTick() {}

    /**
     * Save extra data not associated with any Chunk.  Not saved during autosave, only during world unload.  Currently
     * unused.
     */
    public void saveExtraData()
    {
        while (this.writeNextIO())
        {
            ;
        }
    }

    /**
     * Writes the Chunk passed as an argument to the NBTTagCompound also passed, using the World argument to retrieve
     * the Chunk's last update time.
     */
    private void writeChunkToNBT(Chunk chunkIn, World worldIn, NBTTagCompound p_75820_3_)
    {
        p_75820_3_.setByte("V", (byte)1);
        p_75820_3_.setInteger("xPos", chunkIn.xPosition);
        p_75820_3_.setInteger("zPos", chunkIn.zPosition);
        p_75820_3_.setLong("LastUpdate", worldIn.getTotalWorldTime());
        p_75820_3_.setIntArray("HeightMap", chunkIn.getHeightMap());
        p_75820_3_.setBoolean("TerrainPopulated", chunkIn.isTerrainPopulated());
        p_75820_3_.setBoolean("LightPopulated", chunkIn.isLightPopulated());
        p_75820_3_.setLong("InhabitedTime", chunkIn.getInhabitedTime());
        ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
        NBTTagList nbttaglist = new NBTTagList();
        boolean flag = !worldIn.provider.getHasNoSky();
        ExtendedBlockStorage[] aextendedblockstorage1 = aextendedblockstorage;
        int i = aextendedblockstorage.length;
        NBTTagCompound nbttagcompound1;

        for (int j = 0; j < i; ++j)
        {
            ExtendedBlockStorage extendedblockstorage = aextendedblockstorage1[j];

            if (extendedblockstorage != null)
            {
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Y", (byte)(extendedblockstorage.getYLocation() >> 4 & 255));
                byte[] abyte = new byte[extendedblockstorage.getData().length];
                NibbleArray nibblearray = new NibbleArray();
                NibbleArray nibblearray1 = null;

                for (int k = 0; k < extendedblockstorage.getData().length; ++k)
                {
                    char c0 = extendedblockstorage.getData()[k];
                    int l = k & 15;
                    int i1 = k >> 8 & 15;
                    int j1 = k >> 4 & 15;

                    if (c0 >> 12 != 0)
                    {
                        if (nibblearray1 == null)
                        {
                            nibblearray1 = new NibbleArray();
                        }

                        nibblearray1.set(l, i1, j1, c0 >> 12);
                    }

                    abyte[k] = (byte)(c0 >> 4 & 255);
                    nibblearray.set(l, i1, j1, c0 & 15);
                }

                nbttagcompound1.setByteArray("Blocks", abyte);
                nbttagcompound1.setByteArray("Data", nibblearray.getData());

                if (nibblearray1 != null)
                {
                    nbttagcompound1.setByteArray("Add", nibblearray1.getData());
                }

                nbttagcompound1.setByteArray("BlockLight", extendedblockstorage.getBlocklightArray().getData());

                if (flag)
                {
                    nbttagcompound1.setByteArray("SkyLight", extendedblockstorage.getSkylightArray().getData());
                }
                else
                {
                    nbttagcompound1.setByteArray("SkyLight", new byte[extendedblockstorage.getBlocklightArray().getData().length]);
                }

                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        p_75820_3_.setTag("Sections", nbttaglist);
        p_75820_3_.setByteArray("Biomes", chunkIn.getBiomeArray());
        chunkIn.setHasEntities(false);
        NBTTagList nbttaglist1 = new NBTTagList();
        Iterator iterator;

        for (i = 0; i < chunkIn.getEntityLists().length; ++i)
        {
            iterator = chunkIn.getEntityLists()[i].iterator();

            while (iterator.hasNext())
            {
                Entity entity = (Entity)iterator.next();
                nbttagcompound1 = new NBTTagCompound();

                try
                {
                if (entity.writeToNBTOptional(nbttagcompound1))
                {
                    chunkIn.setHasEntities(true);
                    nbttaglist1.appendTag(nbttagcompound1);
                }
                }
                catch (Exception e)
                {
                    net.minecraftforge.fml.common.FMLLog.log(org.apache.logging.log4j.Level.ERROR, e,
                            "An Entity type %s has thrown an exception trying to write state. It will not persist. Report this to the mod author",
                            entity.getClass().getName());
                }
            }
        }

        p_75820_3_.setTag("Entities", nbttaglist1);
        NBTTagList nbttaglist2 = new NBTTagList();
        iterator = chunkIn.getTileEntityMap().values().iterator();

        while (iterator.hasNext())
        {
            TileEntity tileentity = (TileEntity)iterator.next();
            nbttagcompound1 = new NBTTagCompound();
            try
            {
            tileentity.writeToNBT(nbttagcompound1);
            nbttaglist2.appendTag(nbttagcompound1);
            }
            catch (Exception e)
            {
                net.minecraftforge.fml.common.FMLLog.log(org.apache.logging.log4j.Level.ERROR, e,
                        "A TileEntity type %s has throw an exception trying to write state. It will not persist. Report this to the mod author",
                        tileentity.getClass().getName());
            }
        }

        p_75820_3_.setTag("TileEntities", nbttaglist2);
        List list = worldIn.getPendingBlockUpdates(chunkIn, false);

        if (list != null)
        {
            long k1 = worldIn.getTotalWorldTime();
            NBTTagList nbttaglist3 = new NBTTagList();
            Iterator iterator1 = list.iterator();

            while (iterator1.hasNext())
            {
                NextTickListEntry nextticklistentry = (NextTickListEntry)iterator1.next();
                NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(nextticklistentry.getBlock());
                nbttagcompound2.setString("i", resourcelocation == null ? "" : resourcelocation.toString());
                nbttagcompound2.setInteger("x", nextticklistentry.position.getX());
                nbttagcompound2.setInteger("y", nextticklistentry.position.getY());
                nbttagcompound2.setInteger("z", nextticklistentry.position.getZ());
                nbttagcompound2.setInteger("t", (int)(nextticklistentry.scheduledTime - k1));
                nbttagcompound2.setInteger("p", nextticklistentry.priority);
                nbttaglist3.appendTag(nbttagcompound2);
            }

            p_75820_3_.setTag("TileTicks", nbttaglist3);
        }
    }

    /**
     * Reads the data stored in the passed NBTTagCompound and creates a Chunk with that data in the passed World.
     * Returns the created Chunk.
     */
    private Chunk readChunkFromNBT(World worldIn, NBTTagCompound p_75823_2_)
    {
        int i = p_75823_2_.getInteger("xPos");
        int j = p_75823_2_.getInteger("zPos");
        Chunk chunk = new Chunk(worldIn, i, j);
        chunk.setHeightMap(p_75823_2_.getIntArray("HeightMap"));
        chunk.setTerrainPopulated(p_75823_2_.getBoolean("TerrainPopulated"));
        chunk.setLightPopulated(p_75823_2_.getBoolean("LightPopulated"));
        chunk.setInhabitedTime(p_75823_2_.getLong("InhabitedTime"));
        NBTTagList nbttaglist = p_75823_2_.getTagList("Sections", 10);
        byte b0 = 16;
        ExtendedBlockStorage[] aextendedblockstorage = new ExtendedBlockStorage[b0];
        boolean flag = !worldIn.provider.getHasNoSky();

        for (int k = 0; k < nbttaglist.tagCount(); ++k)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(k);
            byte b1 = nbttagcompound1.getByte("Y");
            ExtendedBlockStorage extendedblockstorage = new ExtendedBlockStorage(b1 << 4, flag);
            byte[] abyte = nbttagcompound1.getByteArray("Blocks");
            NibbleArray nibblearray = new NibbleArray(nbttagcompound1.getByteArray("Data"));
            NibbleArray nibblearray1 = nbttagcompound1.hasKey("Add", 7) ? new NibbleArray(nbttagcompound1.getByteArray("Add")) : null;
            char[] achar = new char[abyte.length];

            for (int l = 0; l < achar.length; ++l)
            {
                int i1 = l & 15;
                int j1 = l >> 8 & 15;
                int k1 = l >> 4 & 15;
                int l1 = nibblearray1 != null ? nibblearray1.get(i1, j1, k1) : 0;
                achar[l] = (char)(l1 << 12 | (abyte[l] & 255) << 4 | nibblearray.get(i1, j1, k1));
            }

            extendedblockstorage.setData(achar);
            extendedblockstorage.setBlocklightArray(new NibbleArray(nbttagcompound1.getByteArray("BlockLight")));

            if (flag)
            {
                extendedblockstorage.setSkylightArray(new NibbleArray(nbttagcompound1.getByteArray("SkyLight")));
            }

            extendedblockstorage.removeInvalidBlocks();
            aextendedblockstorage[b1] = extendedblockstorage;
        }

        chunk.setStorageArrays(aextendedblockstorage);

        if (p_75823_2_.hasKey("Biomes", 7))
        {
            chunk.setBiomeArray(p_75823_2_.getByteArray("Biomes"));
        }

        // End this method here and split off entity loading to another method
        return chunk;
    }

    public void loadEntities(World worldIn, NBTTagCompound p_75823_2_, Chunk chunk)
    {
        NBTTagList nbttaglist1 = p_75823_2_.getTagList("Entities", 10);

        if (nbttaglist1 != null)
        {
            for (int i2 = 0; i2 < nbttaglist1.tagCount(); ++i2)
            {
                NBTTagCompound nbttagcompound2 = nbttaglist1.getCompoundTagAt(i2);
                Entity entity = EntityList.createEntityFromNBT(nbttagcompound2, worldIn);
                chunk.setHasEntities(true);

                if (entity != null)
                {
                    chunk.addEntity(entity);
                    Entity entity1 = entity;

                    for (NBTTagCompound nbttagcompound5 = nbttagcompound2; nbttagcompound5.hasKey("Riding", 10); nbttagcompound5 = nbttagcompound5.getCompoundTag("Riding"))
                    {
                        Entity entity2 = EntityList.createEntityFromNBT(nbttagcompound5.getCompoundTag("Riding"), worldIn);

                        if (entity2 != null)
                        {
                            chunk.addEntity(entity2);
                            entity1.mountEntity(entity2);
                        }

                        entity1 = entity2;
                    }
                }
            }
        }

        NBTTagList nbttaglist2 = p_75823_2_.getTagList("TileEntities", 10);

        if (nbttaglist2 != null)
        {
            for (int j2 = 0; j2 < nbttaglist2.tagCount(); ++j2)
            {
                NBTTagCompound nbttagcompound3 = nbttaglist2.getCompoundTagAt(j2);
                TileEntity tileentity = TileEntity.createAndLoadEntity(nbttagcompound3);

                if (tileentity != null)
                {
                    chunk.addTileEntity(tileentity);
                }
            }
        }

        if (p_75823_2_.hasKey("TileTicks", 9))
        {
            NBTTagList nbttaglist3 = p_75823_2_.getTagList("TileTicks", 10);

            if (nbttaglist3 != null)
            {
                for (int k2 = 0; k2 < nbttaglist3.tagCount(); ++k2)
                {
                    NBTTagCompound nbttagcompound4 = nbttaglist3.getCompoundTagAt(k2);
                    Block block;

                    if (nbttagcompound4.hasKey("i", 8))
                    {
                        block = Block.getBlockFromName(nbttagcompound4.getString("i"));
                    }
                    else
                    {
                        block = Block.getBlockById(nbttagcompound4.getInteger("i"));
                    }

                    worldIn.func_180497_b(new BlockPos(nbttagcompound4.getInteger("x"), nbttagcompound4.getInteger("y"), nbttagcompound4.getInteger("z")), block, nbttagcompound4.getInteger("t"), nbttagcompound4.getInteger("p"));
                }
            }
        }
    }

    static class PendingChunk
        {
            public final ChunkCoordIntPair chunkCoordinate;
            public final NBTTagCompound nbtTags;
            private static final String __OBFID = "CL_00000385";

            public PendingChunk(ChunkCoordIntPair p_i2002_1_, NBTTagCompound p_i2002_2_)
            {
                this.chunkCoordinate = p_i2002_1_;
                this.nbtTags = p_i2002_2_;
            }
        }
}