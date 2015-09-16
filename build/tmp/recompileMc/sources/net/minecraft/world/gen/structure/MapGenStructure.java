package net.minecraft.world.gen.structure;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;

public abstract class MapGenStructure extends MapGenBase
{
    private MapGenStructureData field_143029_e;
    /**
     * Used to store a list of all structures that have been recursively generated. Used so that during recursive
     * generation, the structure generator can avoid generating structures that intersect ones that have already been
     * placed.
     */
    protected Map structureMap = Maps.newHashMap();
    private static final String __OBFID = "CL_00000505";

    public abstract String getStructureName();

    protected final void func_180701_a(World worldIn, final int p_180701_2_, final int p_180701_3_, int p_180701_4_, int p_180701_5_, ChunkPrimer p_180701_6_)
    {
        this.func_143027_a(worldIn);

        if (!this.structureMap.containsKey(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_180701_2_, p_180701_3_))))
        {
            this.rand.nextInt();

            try
            {
                if (this.canSpawnStructureAtCoords(p_180701_2_, p_180701_3_))
                {
                    StructureStart structurestart = this.getStructureStart(p_180701_2_, p_180701_3_);
                    this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_180701_2_, p_180701_3_)), structurestart);
                    this.func_143026_a(p_180701_2_, p_180701_3_, structurestart);
                }
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception preparing structure feature");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Feature being prepared");
                crashreportcategory.addCrashSectionCallable("Is feature chunk", new Callable()
                {
                    private static final String __OBFID = "CL_00000506";
                    public String call()
                    {
                        return MapGenStructure.this.canSpawnStructureAtCoords(p_180701_2_, p_180701_3_) ? "True" : "False";
                    }
                });
                crashreportcategory.addCrashSection("Chunk location", String.format("%d,%d", new Object[] {Integer.valueOf(p_180701_2_), Integer.valueOf(p_180701_3_)}));
                crashreportcategory.addCrashSectionCallable("Chunk pos hash", new Callable()
                {
                    private static final String __OBFID = "CL_00000507";
                    public String call()
                    {
                        return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_180701_2_, p_180701_3_));
                    }
                });
                crashreportcategory.addCrashSectionCallable("Structure type", new Callable()
                {
                    private static final String __OBFID = "CL_00000508";
                    public String call()
                    {
                        return MapGenStructure.this.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    public boolean func_175794_a(World worldIn, Random p_175794_2_, ChunkCoordIntPair p_175794_3_)
    {
        this.func_143027_a(worldIn);
        int i = (p_175794_3_.chunkXPos << 4) + 8;
        int j = (p_175794_3_.chunkZPos << 4) + 8;
        boolean flag = false;
        Iterator iterator = this.structureMap.values().iterator();

        while (iterator.hasNext())
        {
            StructureStart structurestart = (StructureStart)iterator.next();

            if (structurestart.isSizeableStructure() && structurestart.func_175788_a(p_175794_3_) && structurestart.getBoundingBox().intersectsWith(i, j, i + 15, j + 15))
            {
                structurestart.generateStructure(worldIn, p_175794_2_, new StructureBoundingBox(i, j, i + 15, j + 15));
                structurestart.func_175787_b(p_175794_3_);
                flag = true;
                this.func_143026_a(structurestart.func_143019_e(), structurestart.func_143018_f(), structurestart);
            }
        }

        return flag;
    }

    public boolean func_175795_b(BlockPos p_175795_1_)
    {
        this.func_143027_a(this.worldObj);
        return this.func_175797_c(p_175795_1_) != null;
    }

    protected StructureStart func_175797_c(BlockPos p_175797_1_)
    {
        Iterator iterator = this.structureMap.values().iterator();

        while (iterator.hasNext())
        {
            StructureStart structurestart = (StructureStart)iterator.next();

            if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().func_175898_b(p_175797_1_))
            {
                Iterator iterator1 = structurestart.getComponents().iterator();

                while (iterator1.hasNext())
                {
                    StructureComponent structurecomponent = (StructureComponent)iterator1.next();

                    if (structurecomponent.getBoundingBox().func_175898_b(p_175797_1_))
                    {
                        return structurestart;
                    }
                }
            }
        }

        return null;
    }

    public boolean func_175796_a(World worldIn, BlockPos p_175796_2_)
    {
        this.func_143027_a(worldIn);
        Iterator iterator = this.structureMap.values().iterator();
        StructureStart structurestart;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            structurestart = (StructureStart)iterator.next();
        }
        while (!structurestart.isSizeableStructure() || !structurestart.getBoundingBox().func_175898_b(p_175796_2_));

        return true;
    }

    public BlockPos getClosestStrongholdPos(World worldIn, BlockPos p_180706_2_)
    {
        this.worldObj = worldIn;
        this.func_143027_a(worldIn);
        this.rand.setSeed(worldIn.getSeed());
        long i = this.rand.nextLong();
        long j = this.rand.nextLong();
        long k = (long)(p_180706_2_.getX() >> 4) * i;
        long l = (long)(p_180706_2_.getZ() >> 4) * j;
        this.rand.setSeed(k ^ l ^ worldIn.getSeed());
        this.func_180701_a(worldIn, p_180706_2_.getX() >> 4, p_180706_2_.getZ() >> 4, 0, 0, (ChunkPrimer)null);
        double d0 = Double.MAX_VALUE;
        BlockPos blockpos1 = null;
        Iterator iterator = this.structureMap.values().iterator();
        BlockPos blockpos2;
        double d1;

        while (iterator.hasNext())
        {
            StructureStart structurestart = (StructureStart)iterator.next();

            if (structurestart.isSizeableStructure())
            {
                StructureComponent structurecomponent = (StructureComponent)structurestart.getComponents().get(0);
                blockpos2 = structurecomponent.func_180776_a();
                d1 = blockpos2.distanceSq(p_180706_2_);

                if (d1 < d0)
                {
                    d0 = d1;
                    blockpos1 = blockpos2;
                }
            }
        }

        if (blockpos1 != null)
        {
            return blockpos1;
        }
        else
        {
            List list = this.getCoordList();

            if (list != null)
            {
                BlockPos blockpos3 = null;
                Iterator iterator1 = list.iterator();

                while (iterator1.hasNext())
                {
                    blockpos2 = (BlockPos)iterator1.next();
                    d1 = blockpos2.distanceSq(p_180706_2_);

                    if (d1 < d0)
                    {
                        d0 = d1;
                        blockpos3 = blockpos2;
                    }
                }

                return blockpos3;
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * Returns a list of other locations at which the structure generation has been run, or null if not relevant to this
     * structure generator.
     */
    protected List getCoordList()
    {
        return null;
    }

    private void func_143027_a(World worldIn)
    {
        if (this.field_143029_e == null)
        {
            this.field_143029_e = (MapGenStructureData)worldIn.getPerWorldStorage().loadData(MapGenStructureData.class, this.getStructureName());

            if (this.field_143029_e == null)
            {
                this.field_143029_e = new MapGenStructureData(this.getStructureName());
                worldIn.getPerWorldStorage().setData(this.getStructureName(), this.field_143029_e);
            }
            else
            {
                NBTTagCompound nbttagcompound = this.field_143029_e.func_143041_a();
                Iterator iterator = nbttagcompound.getKeySet().iterator();

                while (iterator.hasNext())
                {
                    String s = (String)iterator.next();
                    NBTBase nbtbase = nbttagcompound.getTag(s);

                    if (nbtbase.getId() == 10)
                    {
                        NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbtbase;

                        if (nbttagcompound1.hasKey("ChunkX") && nbttagcompound1.hasKey("ChunkZ"))
                        {
                            int i = nbttagcompound1.getInteger("ChunkX");
                            int j = nbttagcompound1.getInteger("ChunkZ");
                            StructureStart structurestart = MapGenStructureIO.func_143035_a(nbttagcompound1, worldIn);

                            if (structurestart != null)
                            {
                                this.structureMap.put(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)), structurestart);
                            }
                        }
                    }
                }
            }
        }
    }

    private void func_143026_a(int p_143026_1_, int p_143026_2_, StructureStart p_143026_3_)
    {
        this.field_143029_e.func_143043_a(p_143026_3_.func_143021_a(p_143026_1_, p_143026_2_), p_143026_1_, p_143026_2_);
        this.field_143029_e.markDirty();
    }

    protected abstract boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_);

    protected abstract StructureStart getStructureStart(int p_75049_1_, int p_75049_2_);
}