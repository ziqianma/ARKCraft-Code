package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class StructureOceanMonument extends MapGenStructure
{
    private int field_175800_f;
    private int field_175801_g;
    public static final List field_175802_d = Arrays.asList(new BiomeGenBase[] {BiomeGenBase.ocean, BiomeGenBase.deepOcean, BiomeGenBase.river, BiomeGenBase.frozenOcean, BiomeGenBase.frozenRiver});
    private static final List field_175803_h = Lists.newArrayList();
    private static final String __OBFID = "CL_00001996";

    public StructureOceanMonument()
    {
        this.field_175800_f = 32;
        this.field_175801_g = 5;
    }

    public StructureOceanMonument(Map p_i45608_1_)
    {
        this();
        Iterator iterator = p_i45608_1_.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry entry = (Entry)iterator.next();

            if (((String)entry.getKey()).equals("spacing"))
            {
                this.field_175800_f = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.field_175800_f, 1);
            }
            else if (((String)entry.getKey()).equals("separation"))
            {
                this.field_175801_g = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.field_175801_g, 1);
            }
        }
    }

    public String getStructureName()
    {
        return "Monument";
    }

    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_)
    {
        int k = p_75047_1_;
        int l = p_75047_2_;

        if (p_75047_1_ < 0)
        {
            p_75047_1_ -= this.field_175800_f - 1;
        }

        if (p_75047_2_ < 0)
        {
            p_75047_2_ -= this.field_175800_f - 1;
        }

        int i1 = p_75047_1_ / this.field_175800_f;
        int j1 = p_75047_2_ / this.field_175800_f;
        Random random = this.worldObj.setRandomSeed(i1, j1, 10387313);
        i1 *= this.field_175800_f;
        j1 *= this.field_175800_f;
        i1 += (random.nextInt(this.field_175800_f - this.field_175801_g) + random.nextInt(this.field_175800_f - this.field_175801_g)) / 2;
        j1 += (random.nextInt(this.field_175800_f - this.field_175801_g) + random.nextInt(this.field_175800_f - this.field_175801_g)) / 2;

        if (k == i1 && l == j1)
        {
            if (this.worldObj.getWorldChunkManager().func_180300_a(new BlockPos(k * 16 + 8, 64, l * 16 + 8), (BiomeGenBase)null) != BiomeGenBase.deepOcean)
            {
                return false;
            }

            boolean flag = this.worldObj.getWorldChunkManager().areBiomesViable(k * 16 + 8, l * 16 + 8, 29, field_175802_d);

            if (flag)
            {
                return true;
            }
        }

        return false;
    }

    protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_)
    {
        return new StructureOceanMonument.StartMonument(this.worldObj, this.rand, p_75049_1_, p_75049_2_);
    }

    public List func_175799_b()
    {
        return field_175803_h;
    }

    static
    {
        field_175803_h.add(new BiomeGenBase.SpawnListEntry(EntityGuardian.class, 1, 2, 4));
    }

    public static class StartMonument extends StructureStart
        {
            private Set field_175791_c = Sets.newHashSet();
            private boolean field_175790_d;
            private static final String __OBFID = "CL_00001995";

            public StartMonument() {}

            public StartMonument(World worldIn, Random p_i45607_2_, int p_i45607_3_, int p_i45607_4_)
            {
                super(p_i45607_3_, p_i45607_4_);
                this.func_175789_b(worldIn, p_i45607_2_, p_i45607_3_, p_i45607_4_);
            }

            private void func_175789_b(World worldIn, Random p_175789_2_, int p_175789_3_, int p_175789_4_)
            {
                p_175789_2_.setSeed(worldIn.getSeed());
                long k = p_175789_2_.nextLong();
                long l = p_175789_2_.nextLong();
                long i1 = (long)p_175789_3_ * k;
                long j1 = (long)p_175789_4_ * l;
                p_175789_2_.setSeed(i1 ^ j1 ^ worldIn.getSeed());
                int k1 = p_175789_3_ * 16 + 8 - 29;
                int l1 = p_175789_4_ * 16 + 8 - 29;
                EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(p_175789_2_);
                this.components.add(new StructureOceanMonumentPieces.MonumentBuilding(p_175789_2_, k1, l1, enumfacing));
                this.updateBoundingBox();
                this.field_175790_d = true;
            }

            /**
             * Keeps iterating Structure Pieces and spawning them until the checks tell it to stop
             */
            public void generateStructure(World worldIn, Random p_75068_2_, StructureBoundingBox p_75068_3_)
            {
                if (!this.field_175790_d)
                {
                    this.components.clear();
                    this.func_175789_b(worldIn, p_75068_2_, this.func_143019_e(), this.func_143018_f());
                }

                super.generateStructure(worldIn, p_75068_2_, p_75068_3_);
            }

            public boolean func_175788_a(ChunkCoordIntPair p_175788_1_)
            {
                return this.field_175791_c.contains(p_175788_1_) ? false : super.func_175788_a(p_175788_1_);
            }

            public void func_175787_b(ChunkCoordIntPair p_175787_1_)
            {
                super.func_175787_b(p_175787_1_);
                this.field_175791_c.add(p_175787_1_);
            }

            public void func_143022_a(NBTTagCompound p_143022_1_)
            {
                super.func_143022_a(p_143022_1_);
                NBTTagList nbttaglist = new NBTTagList();
                Iterator iterator = this.field_175791_c.iterator();

                while (iterator.hasNext())
                {
                    ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)iterator.next();
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    nbttagcompound1.setInteger("X", chunkcoordintpair.chunkXPos);
                    nbttagcompound1.setInteger("Z", chunkcoordintpair.chunkZPos);
                    nbttaglist.appendTag(nbttagcompound1);
                }

                p_143022_1_.setTag("Processed", nbttaglist);
            }

            public void func_143017_b(NBTTagCompound p_143017_1_)
            {
                super.func_143017_b(p_143017_1_);

                if (p_143017_1_.hasKey("Processed", 9))
                {
                    NBTTagList nbttaglist = p_143017_1_.getTagList("Processed", 10);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i)
                    {
                        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                        this.field_175791_c.add(new ChunkCoordIntPair(nbttagcompound1.getInteger("X"), nbttagcompound1.getInteger("Z")));
                    }
                }
            }
        }
}