package net.minecraft.world.chunk.storage;

import com.google.common.collect.Lists;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import net.minecraft.server.MinecraftServer;

public class RegionFile
{
    private static final byte[] emptySector = new byte[4096];
    private final File fileName;
    private RandomAccessFile dataFile;
    private final int[] offsets = new int[1024];
    private final int[] chunkTimestamps = new int[1024];
    private List sectorFree;
    /** McRegion sizeDelta */
    private int sizeDelta;
    private long lastModified;
    private static final String __OBFID = "CL_00000381";

    public RegionFile(File fileNameIn)
    {
        this.fileName = fileNameIn;
        this.sizeDelta = 0;

        try
        {
            if (fileNameIn.exists())
            {
                this.lastModified = fileNameIn.lastModified();
            }

            this.dataFile = new RandomAccessFile(fileNameIn, "rw");
            int i;

            if (this.dataFile.length() < 4096L)
            {
                for (i = 0; i < 1024; ++i)
                {
                    this.dataFile.writeInt(0);
                }

                for (i = 0; i < 1024; ++i)
                {
                    this.dataFile.writeInt(0);
                }

                this.sizeDelta += 8192;
            }

            if ((this.dataFile.length() & 4095L) != 0L)
            {
                for (i = 0; (long)i < (this.dataFile.length() & 4095L); ++i)
                {
                    this.dataFile.write(0);
                }
            }

            i = (int)this.dataFile.length() / 4096;
            this.sectorFree = Lists.newArrayListWithCapacity(i);
            int j;

            for (j = 0; j < i; ++j)
            {
                this.sectorFree.add(Boolean.valueOf(true));
            }

            this.sectorFree.set(0, Boolean.valueOf(false));
            this.sectorFree.set(1, Boolean.valueOf(false));
            this.dataFile.seek(0L);
            int k;

            for (j = 0; j < 1024; ++j)
            {
                k = this.dataFile.readInt();
                this.offsets[j] = k;

                if (k != 0 && (k >> 8) + (k & 255) <= this.sectorFree.size())
                {
                    for (int l = 0; l < (k & 255); ++l)
                    {
                        this.sectorFree.set((k >> 8) + l, Boolean.valueOf(false));
                    }
                }
            }

            for (j = 0; j < 1024; ++j)
            {
                k = this.dataFile.readInt();
                this.chunkTimestamps[j] = k;
            }
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    // This is a copy (sort of) of the method below it, make sure they stay in sync
    public synchronized boolean chunkExists(int x, int z)
    {
        if (this.outOfBounds(x, z)) return false;

        try
        {
            int offset = this.getOffset(x, z);

            if (offset == 0) return false;

            int sectorNumber = offset >> 8;
            int numSectors = offset & 255;

            if (sectorNumber + numSectors > this.sectorFree.size()) return false;

            this.dataFile.seek((long)(sectorNumber * 4096));
            int length = this.dataFile.readInt();

            if (length > 4096 * numSectors || length <= 0) return false;

            byte version = this.dataFile.readByte();

            if (version == 1 || version == 2) return true;
        }
        catch (IOException ioexception)
        {
            return false;
        }

        return false;
    }

    /**
     * Returns an uncompressed chunk stream from the region file.
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     */
    public synchronized DataInputStream getChunkDataInputStream(int x, int z)
    {
        if (this.outOfBounds(x, z))
        {
            return null;
        }
        else
        {
            try
            {
                int k = this.getOffset(x, z);

                if (k == 0)
                {
                    return null;
                }
                else
                {
                    int l = k >> 8;
                    int i1 = k & 255;

                    if (l + i1 > this.sectorFree.size())
                    {
                        return null;
                    }
                    else
                    {
                        this.dataFile.seek((long)(l * 4096));
                        int j1 = this.dataFile.readInt();

                        if (j1 > 4096 * i1)
                        {
                            return null;
                        }
                        else if (j1 <= 0)
                        {
                            return null;
                        }
                        else
                        {
                            byte b0 = this.dataFile.readByte();
                            byte[] abyte;

                            if (b0 == 1)
                            {
                                abyte = new byte[j1 - 1];
                                this.dataFile.read(abyte);
                                return new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(abyte))));
                            }
                            else if (b0 == 2)
                            {
                                abyte = new byte[j1 - 1];
                                this.dataFile.read(abyte);
                                return new DataInputStream(new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(abyte))));
                            }
                            else
                            {
                                return null;
                            }
                        }
                    }
                }
            }
            catch (IOException ioexception)
            {
                return null;
            }
        }
    }

    /**
     * Returns an output stream used to write chunk data. Data is on disk when the returned stream is closed.
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     */
    public DataOutputStream getChunkDataOutputStream(int x, int z)
    {
        return this.outOfBounds(x, z) ? null : new DataOutputStream(new DeflaterOutputStream(new RegionFile.ChunkBuffer(x, z)));
    }

    /**
     * args: x, z, data, length - write chunk data at (x, z) to disk
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     * @param data The chunk data to write
     * @param length The length of the data
     */
    protected synchronized void write(int x, int z, byte[] data, int length)
    {
        try
        {
            int l = this.getOffset(x, z);
            int i1 = l >> 8;
            int j1 = l & 255;
            int k1 = (length + 5) / 4096 + 1;

            if (k1 >= 256)
            {
                return;
            }

            if (i1 != 0 && j1 == k1)
            {
                this.write(i1, data, length);
            }
            else
            {
                int l1;

                for (l1 = 0; l1 < j1; ++l1)
                {
                    this.sectorFree.set(i1 + l1, Boolean.valueOf(true));
                }

                l1 = this.sectorFree.indexOf(Boolean.valueOf(true));
                int i2 = 0;
                int j2;

                if (l1 != -1)
                {
                    for (j2 = l1; j2 < this.sectorFree.size(); ++j2)
                    {
                        if (i2 != 0)
                        {
                            if (((Boolean)this.sectorFree.get(j2)).booleanValue())
                            {
                                ++i2;
                            }
                            else
                            {
                                i2 = 0;
                            }
                        }
                        else if (((Boolean)this.sectorFree.get(j2)).booleanValue())
                        {
                            l1 = j2;
                            i2 = 1;
                        }

                        if (i2 >= k1)
                        {
                            break;
                        }
                    }
                }

                if (i2 >= k1)
                {
                    i1 = l1;
                    this.setOffset(x, z, l1 << 8 | k1);

                    for (j2 = 0; j2 < k1; ++j2)
                    {
                        this.sectorFree.set(i1 + j2, Boolean.valueOf(false));
                    }

                    this.write(i1, data, length);
                }
                else
                {
                    this.dataFile.seek(this.dataFile.length());
                    i1 = this.sectorFree.size();

                    for (j2 = 0; j2 < k1; ++j2)
                    {
                        this.dataFile.write(emptySector);
                        this.sectorFree.add(Boolean.valueOf(false));
                    }

                    this.sizeDelta += 4096 * k1;
                    this.write(i1, data, length);
                    this.setOffset(x, z, i1 << 8 | k1);
                }
            }

            this.setChunkTimestamp(x, z, (int)(MinecraftServer.getCurrentTimeMillis() / 1000L));
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    /**
     * args: sectorNumber, data, length - write the chunk data to this RegionFile
     */
    private void write(int sectorNumber, byte[] data, int length) throws IOException
    {
        this.dataFile.seek((long)(sectorNumber * 4096));
        this.dataFile.writeInt(length + 1);
        this.dataFile.writeByte(2);
        this.dataFile.write(data, 0, length);
    }

    /**
     * args: x, z - check region bounds
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     */
    private boolean outOfBounds(int x, int z)
    {
        return x < 0 || x >= 32 || z < 0 || z >= 32;
    }

    /**
     * args: x, z - get chunk's offset in region file
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     */
    private int getOffset(int x, int z)
    {
        return this.offsets[x + z * 32];
    }

    /**
     * args: x, z, - true if chunk has been saved / converted
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     */
    public boolean isChunkSaved(int x, int z)
    {
        return this.getOffset(x, z) != 0;
    }

    /**
     * args: x, z, offset - sets the chunk's offset in the region file
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     * @param offset The chunk offset
     */
    private void setOffset(int x, int z, int offset) throws IOException
    {
        this.offsets[x + z * 32] = offset;
        this.dataFile.seek((long)((x + z * 32) * 4));
        this.dataFile.writeInt(offset);
    }

    /**
     * args: x, z, timestamp - sets the chunk's write timestamp
     *  
     * @param x Chunk X coordinate
     * @param z Chunk Z coordinate
     * @param timestamp The chunk's write timestamp
     */
    private void setChunkTimestamp(int x, int z, int timestamp) throws IOException
    {
        this.chunkTimestamps[x + z * 32] = timestamp;
        this.dataFile.seek((long)(4096 + (x + z * 32) * 4));
        this.dataFile.writeInt(timestamp);
    }

    /**
     * close this RegionFile and prevent further writes
     */
    public void close() throws IOException
    {
        if (this.dataFile != null)
        {
            this.dataFile.close();
        }
    }

    class ChunkBuffer extends ByteArrayOutputStream
    {
        private int chunkX;
        private int chunkZ;
        private static final String __OBFID = "CL_00000382";

        public ChunkBuffer(int x, int z)
        {
            super(8096);
            this.chunkX = x;
            this.chunkZ = z;
        }

        public void close() throws IOException
        {
            RegionFile.this.write(this.chunkX, this.chunkZ, this.buf, this.count);
        }
    }
}