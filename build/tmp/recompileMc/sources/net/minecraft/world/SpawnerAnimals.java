package net.minecraft.world;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import java.util.ArrayList;
import java.util.Collections;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.event.ForgeEventFactory;

public final class SpawnerAnimals
{
    private static final int MOB_COUNT_DIV = (int)Math.pow(17.0D, 2.0D);
    /** The 17x17 area around the player where mobs can spawn */
    private final Set eligibleChunksForSpawning = Sets.newHashSet();
    private static final String __OBFID = "CL_00000152";

    /**
     * adds all chunks within the spawn radius of the players to eligibleChunksForSpawning. pars: the world,
     * hostileCreatures, passiveCreatures. returns number of eligible chunks.
     */
    public int findChunksForSpawning(WorldServer p_77192_1_, boolean p_77192_2_, boolean p_77192_3_, boolean p_77192_4_)
    {
        if (!p_77192_2_ && !p_77192_3_)
        {
            return 0;
        }
        else
        {
            this.eligibleChunksForSpawning.clear();
            int i = 0;
            Iterator iterator = p_77192_1_.playerEntities.iterator();
            int k;
            int i1;

            while (iterator.hasNext())
            {
                EntityPlayer entityplayer = (EntityPlayer)iterator.next();

                if (!entityplayer.isSpectator())
                {
                    int j = MathHelper.floor_double(entityplayer.posX / 16.0D);
                    k = MathHelper.floor_double(entityplayer.posZ / 16.0D);
                    byte b0 = 8;

                    for (int l = -b0; l <= b0; ++l)
                    {
                        for (i1 = -b0; i1 <= b0; ++i1)
                        {
                            boolean flag3 = l == -b0 || l == b0 || i1 == -b0 || i1 == b0;
                            ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(l + j, i1 + k);

                            if (!this.eligibleChunksForSpawning.contains(chunkcoordintpair))
                            {
                                ++i;

                                if (!flag3 && p_77192_1_.getWorldBorder().contains(chunkcoordintpair))
                                {
                                    this.eligibleChunksForSpawning.add(chunkcoordintpair);
                                }
                            }
                        }
                    }
                }
            }

            int k3 = 0;
            BlockPos blockpos2 = p_77192_1_.getSpawnPoint();
            EnumCreatureType[] aenumcreaturetype = EnumCreatureType.values();
            k = aenumcreaturetype.length;

            for (int l3 = 0; l3 < k; ++l3)
            {
                EnumCreatureType enumcreaturetype = aenumcreaturetype[l3];

                if ((!enumcreaturetype.getPeacefulCreature() || p_77192_3_) && (enumcreaturetype.getPeacefulCreature() || p_77192_2_) && (!enumcreaturetype.getAnimal() || p_77192_4_))
                {
                    i1 = p_77192_1_.countEntities(enumcreaturetype, true);
                    int i4 = enumcreaturetype.getMaxNumberOfCreature() * i / MOB_COUNT_DIV;

                    if (i1 <= i4)
                    {
                        Iterator iterator1 = this.eligibleChunksForSpawning.iterator();
                        ArrayList<ChunkCoordIntPair> tmp = new ArrayList(eligibleChunksForSpawning);
                        Collections.shuffle(tmp);
                        iterator1 = tmp.iterator();
                        label115:

                        while (iterator1.hasNext())
                        {
                            ChunkCoordIntPair chunkcoordintpair1 = (ChunkCoordIntPair)iterator1.next();
                            BlockPos blockpos = getRandomChunkPosition(p_77192_1_, chunkcoordintpair1.chunkXPos, chunkcoordintpair1.chunkZPos);
                            int j1 = blockpos.getX();
                            int k1 = blockpos.getY();
                            int l1 = blockpos.getZ();
                            Block block = p_77192_1_.getBlockState(blockpos).getBlock();

                            if (!block.isNormalCube())
                            {
                                int i2 = 0;
                                int j2 = 0;

                                while (j2 < 3)
                                {
                                    int k2 = j1;
                                    int l2 = k1;
                                    int i3 = l1;
                                    byte b1 = 6;
                                    BiomeGenBase.SpawnListEntry spawnlistentry = null;
                                    IEntityLivingData ientitylivingdata = null;
                                    int j3 = 0;

                                    while (true)
                                    {
                                        if (j3 < 4)
                                        {
                                            label108:
                                            {
                                                k2 += p_77192_1_.rand.nextInt(b1) - p_77192_1_.rand.nextInt(b1);
                                                l2 += p_77192_1_.rand.nextInt(1) - p_77192_1_.rand.nextInt(1);
                                                i3 += p_77192_1_.rand.nextInt(b1) - p_77192_1_.rand.nextInt(b1);
                                                BlockPos blockpos1 = new BlockPos(k2, l2, i3);
                                                float f = (float)k2 + 0.5F;
                                                float f1 = (float)i3 + 0.5F;

                                                if (!p_77192_1_.func_175636_b((double)f, (double)l2, (double)f1, 24.0D) && blockpos2.distanceSq((double)f, (double)l2, (double)f1) >= 576.0D)
                                                {
                                                    if (spawnlistentry == null)
                                                    {
                                                        spawnlistentry = p_77192_1_.func_175734_a(enumcreaturetype, blockpos1);

                                                        if (spawnlistentry == null)
                                                        {
                                                            break label108;
                                                        }
                                                    }

                                                    if (p_77192_1_.func_175732_a(enumcreaturetype, spawnlistentry, blockpos1) && canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.func_180109_a(spawnlistentry.entityClass), p_77192_1_, blockpos1))
                                                    {
                                                        EntityLiving entityliving;

                                                        try
                                                        {
                                                            entityliving = (EntityLiving)spawnlistentry.entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {p_77192_1_});
                                                        }
                                                        catch (Exception exception)
                                                        {
                                                            exception.printStackTrace();
                                                            return k3;
                                                        }

                                                        entityliving.setLocationAndAngles((double)f, (double)l2, (double)f1, p_77192_1_.rand.nextFloat() * 360.0F, 0.0F);

                                                        Result canSpawn = ForgeEventFactory.canEntitySpawn(entityliving, p_77192_1_, f, l2, f1);
                                                        if (canSpawn == Result.ALLOW || (canSpawn == Result.DEFAULT && (entityliving.getCanSpawnHere() && entityliving.handleLavaMovement())))
                                                        {
                                                            if (!ForgeEventFactory.doSpecialSpawn(entityliving, p_77192_1_, f1, l2, f1))
                                                            ientitylivingdata = entityliving.func_180482_a(p_77192_1_.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);

                                                            if (entityliving.handleLavaMovement())
                                                            {
                                                                ++i2;
                                                                p_77192_1_.spawnEntityInWorld(entityliving);
                                                            }

                                                            if (i2 >= ForgeEventFactory.getMaxSpawnPackSize(entityliving))
                                                            {
                                                                continue label115;
                                                            }
                                                        }

                                                        k3 += i2;
                                                    }
                                                }

                                                ++j3;
                                                continue;
                                            }
                                        }

                                        ++j2;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return k3;
        }
    }

    protected static BlockPos getRandomChunkPosition(World worldIn, int x, int z)
    {
        Chunk chunk = worldIn.getChunkFromChunkCoords(x, z);
        int k = x * 16 + worldIn.rand.nextInt(16);
        int l = z * 16 + worldIn.rand.nextInt(16);
        int i1 = MathHelper.func_154354_b(chunk.getHeight(new BlockPos(k, 0, l)) + 1, 16);
        int j1 = worldIn.rand.nextInt(i1 > 0 ? i1 : chunk.getTopFilledSegment() + 16 - 1);
        return new BlockPos(k, j1, l);
    }

    public static boolean canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType p_180267_0_, World worldIn, BlockPos pos)
    {
        if (!worldIn.getWorldBorder().contains(pos))
        {
            return false;
        }
        else
        {
            Block block = worldIn.getBlockState(pos).getBlock();

            if (p_180267_0_ == EntityLiving.SpawnPlacementType.IN_WATER)
            {
                return block.getMaterial().isLiquid() && worldIn.getBlockState(pos.down()).getBlock().getMaterial().isLiquid() && !worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
            }
            else
            {
                BlockPos blockpos1 = pos.down();

                if (!worldIn.getBlockState(blockpos1).getBlock().canCreatureSpawn(worldIn, blockpos1, p_180267_0_))
                {
                    return false;
                }
                else
                {
                    Block block1 = worldIn.getBlockState(blockpos1).getBlock();
                    boolean flag = block1 != Blocks.bedrock && block1 != Blocks.barrier;
                    return flag && !block.isNormalCube() && !block.getMaterial().isLiquid() && !worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
                }
            }
        }
    }

    /**
     * Called during chunk generation to spawn initial creatures.
     */
    public static void performWorldGenSpawning(World worldIn, BiomeGenBase p_77191_1_, int p_77191_2_, int p_77191_3_, int p_77191_4_, int p_77191_5_, Random p_77191_6_)
    {
        List list = p_77191_1_.getSpawnableList(EnumCreatureType.CREATURE);

        if (!list.isEmpty())
        {
            while (p_77191_6_.nextFloat() < p_77191_1_.getSpawningChance())
            {
                BiomeGenBase.SpawnListEntry spawnlistentry = (BiomeGenBase.SpawnListEntry)WeightedRandom.getRandomItem(worldIn.rand, list);
                int i1 = spawnlistentry.minGroupCount + p_77191_6_.nextInt(1 + spawnlistentry.maxGroupCount - spawnlistentry.minGroupCount);
                IEntityLivingData ientitylivingdata = null;
                int j1 = p_77191_2_ + p_77191_6_.nextInt(p_77191_4_);
                int k1 = p_77191_3_ + p_77191_6_.nextInt(p_77191_5_);
                int l1 = j1;
                int i2 = k1;

                for (int j2 = 0; j2 < i1; ++j2)
                {
                    boolean flag = false;

                    for (int k2 = 0; !flag && k2 < 4; ++k2)
                    {
                        BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(j1, 0, k1));

                        if (canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos))
                        {
                            EntityLiving entityliving;

                            try
                            {
                                entityliving = (EntityLiving)spawnlistentry.entityClass.getConstructor(new Class[] {World.class}).newInstance(new Object[] {worldIn});
                            }
                            catch (Exception exception)
                            {
                                exception.printStackTrace();
                                continue;
                            }

                            entityliving.setLocationAndAngles((double)((float)j1 + 0.5F), (double)blockpos.getY(), (double)((float)k1 + 0.5F), p_77191_6_.nextFloat() * 360.0F, 0.0F);
                            worldIn.spawnEntityInWorld(entityliving);
                            ientitylivingdata = entityliving.func_180482_a(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), ientitylivingdata);
                            flag = true;
                        }

                        j1 += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5);

                        for (k1 += p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5); j1 < p_77191_2_ || j1 >= p_77191_2_ + p_77191_4_ || k1 < p_77191_3_ || k1 >= p_77191_3_ + p_77191_4_; k1 = i2 + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5))
                        {
                            j1 = l1 + p_77191_6_.nextInt(5) - p_77191_6_.nextInt(5);
                        }
                    }
                }
            }
        }
    }
}