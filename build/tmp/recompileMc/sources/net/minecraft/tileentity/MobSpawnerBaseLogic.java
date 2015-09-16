package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MobSpawnerBaseLogic
{
    /** The delay to spawn. */
    private int spawnDelay = 20;
    private String mobID = "Pig";
    /** List of minecart to spawn. */
    private final List minecartToSpawn = Lists.newArrayList();
    private MobSpawnerBaseLogic.WeightedRandomMinecart randomEntity;
    /** The rotation of the mob inside the mob spawner */
    private double mobRotation;
    /** the previous rotation of the mob inside the mob spawner */
    private double prevMobRotation;
    private int minSpawnDelay = 200;
    private int maxSpawnDelay = 800;
    private int spawnCount = 4;
    /** Cached instance of the entity to render inside the spawner. */
    private Entity cachedEntity;
    private int maxNearbyEntities = 6;
    /** The distance from which a player activates the spawner. */
    private int activatingRangeFromPlayer = 16;
    /** The range coefficient for spawning entities around. */
    private int spawnRange = 4;
    private static final String __OBFID = "CL_00000129";

    /**
     * Gets the entity name that should be spawned.
     */
    private String getEntityNameToSpawn()
    {
        if (this.getRandomEntity() == null)
        {
            if (this.mobID.equals("Minecart"))
            {
                this.mobID = "MinecartRideable";
            }

            return this.mobID;
        }
        else
        {
            return this.getRandomEntity().entityType;
        }
    }

    public void setEntityName(String p_98272_1_)
    {
        this.mobID = p_98272_1_;
    }

    /**
     * Returns true if there's a player close enough to this mob spawner to activate it.
     */
    private boolean isActivated()
    {
        BlockPos blockpos = this.func_177221_b();
        return this.getSpawnerWorld().func_175636_b((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, (double)this.activatingRangeFromPlayer);
    }

    public void updateSpawner()
    {
        if (this.isActivated())
        {
            BlockPos blockpos = this.func_177221_b();
            double d2;

            if (this.getSpawnerWorld().isRemote)
            {
                double d0 = (double)((float)blockpos.getX() + this.getSpawnerWorld().rand.nextFloat());
                double d1 = (double)((float)blockpos.getY() + this.getSpawnerWorld().rand.nextFloat());
                d2 = (double)((float)blockpos.getZ() + this.getSpawnerWorld().rand.nextFloat());
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
                this.getSpawnerWorld().spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);

                if (this.spawnDelay > 0)
                {
                    --this.spawnDelay;
                }

                this.prevMobRotation = this.mobRotation;
                this.mobRotation = (this.mobRotation + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0D;
            }
            else
            {
                if (this.spawnDelay == -1)
                {
                    this.resetTimer();
                }

                if (this.spawnDelay > 0)
                {
                    --this.spawnDelay;
                    return;
                }

                boolean flag = false;

                for (int i = 0; i < this.spawnCount; ++i)
                {
                    Entity entity = EntityList.createEntityByName(this.getEntityNameToSpawn(), this.getSpawnerWorld());

                    if (entity == null)
                    {
                        return;
                    }

                    int j = this.getSpawnerWorld().getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), (double)(blockpos.getX() + 1), (double)(blockpos.getY() + 1), (double)(blockpos.getZ() + 1))).expand((double)this.spawnRange, (double)this.spawnRange, (double)this.spawnRange)).size();

                    if (j >= this.maxNearbyEntities)
                    {
                        this.resetTimer();
                        return;
                    }

                    d2 = (double)blockpos.getX() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                    double d3 = (double)(blockpos.getY() + this.getSpawnerWorld().rand.nextInt(3) - 1);
                    double d4 = (double)blockpos.getZ() + (this.getSpawnerWorld().rand.nextDouble() - this.getSpawnerWorld().rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                    EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
                    entity.setLocationAndAngles(d2, d3, d4, this.getSpawnerWorld().rand.nextFloat() * 360.0F, 0.0F);

                    if (entityliving == null || entityliving.getCanSpawnHere() && entityliving.handleLavaMovement())
                    {
                        this.func_180613_a(entity, true);
                        this.getSpawnerWorld().playAuxSFX(2004, blockpos, 0);

                        if (entityliving != null)
                        {
                            entityliving.spawnExplosionParticle();
                        }

                        flag = true;
                    }
                }

                if (flag)
                {
                    this.resetTimer();
                }
            }
        }
    }

    private Entity func_180613_a(Entity p_180613_1_, boolean p_180613_2_)
    {
        if (this.getRandomEntity() != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            p_180613_1_.writeToNBTOptional(nbttagcompound);
            Iterator iterator = this.getRandomEntity().field_98222_b.getKeySet().iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                NBTBase nbtbase = this.getRandomEntity().field_98222_b.getTag(s);
                nbttagcompound.setTag(s, nbtbase.copy());
            }

            p_180613_1_.readFromNBT(nbttagcompound);

            if (p_180613_1_.worldObj != null && p_180613_2_)
            {
                p_180613_1_.worldObj.spawnEntityInWorld(p_180613_1_);
            }

            NBTTagCompound nbttagcompound2;

            for (Entity entity1 = p_180613_1_; nbttagcompound.hasKey("Riding", 10); nbttagcompound = nbttagcompound2)
            {
                nbttagcompound2 = nbttagcompound.getCompoundTag("Riding");
                Entity entity2 = EntityList.createEntityByName(nbttagcompound2.getString("id"), p_180613_1_.worldObj);

                if (entity2 != null)
                {
                    NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                    entity2.writeToNBTOptional(nbttagcompound1);
                    Iterator iterator1 = nbttagcompound2.getKeySet().iterator();

                    while (iterator1.hasNext())
                    {
                        String s1 = (String)iterator1.next();
                        NBTBase nbtbase1 = nbttagcompound2.getTag(s1);
                        nbttagcompound1.setTag(s1, nbtbase1.copy());
                    }

                    entity2.readFromNBT(nbttagcompound1);
                    entity2.setLocationAndAngles(entity1.posX, entity1.posY, entity1.posZ, entity1.rotationYaw, entity1.rotationPitch);

                    if (p_180613_1_.worldObj != null && p_180613_2_)
                    {
                        p_180613_1_.worldObj.spawnEntityInWorld(entity2);
                    }

                    entity1.mountEntity(entity2);
                }

                entity1 = entity2;
            }
        }
        else if (p_180613_1_ instanceof EntityLivingBase && p_180613_1_.worldObj != null && p_180613_2_)
        {
            ((EntityLiving)p_180613_1_).func_180482_a(p_180613_1_.worldObj.getDifficultyForLocation(new BlockPos(p_180613_1_)), (IEntityLivingData)null);
            p_180613_1_.worldObj.spawnEntityInWorld(p_180613_1_);
        }

        return p_180613_1_;
    }

    private void resetTimer()
    {
        if (this.maxSpawnDelay <= this.minSpawnDelay)
        {
            this.spawnDelay = this.minSpawnDelay;
        }
        else
        {
            int i = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(i);
        }

        if (this.minecartToSpawn.size() > 0)
        {
            this.setRandomEntity((MobSpawnerBaseLogic.WeightedRandomMinecart)WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.minecartToSpawn));
        }

        this.func_98267_a(1);
    }

    public void readFromNBT(NBTTagCompound p_98270_1_)
    {
        this.mobID = p_98270_1_.getString("EntityId");
        this.spawnDelay = p_98270_1_.getShort("Delay");
        this.minecartToSpawn.clear();

        if (p_98270_1_.hasKey("SpawnPotentials", 9))
        {
            NBTTagList nbttaglist = p_98270_1_.getTagList("SpawnPotentials", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                this.minecartToSpawn.add(new MobSpawnerBaseLogic.WeightedRandomMinecart(nbttaglist.getCompoundTagAt(i)));
            }
        }

        if (p_98270_1_.hasKey("SpawnData", 10))
        {
            this.setRandomEntity(new MobSpawnerBaseLogic.WeightedRandomMinecart(p_98270_1_.getCompoundTag("SpawnData"), this.mobID));
        }
        else
        {
            this.setRandomEntity((MobSpawnerBaseLogic.WeightedRandomMinecart)null);
        }

        if (p_98270_1_.hasKey("MinSpawnDelay", 99))
        {
            this.minSpawnDelay = p_98270_1_.getShort("MinSpawnDelay");
            this.maxSpawnDelay = p_98270_1_.getShort("MaxSpawnDelay");
            this.spawnCount = p_98270_1_.getShort("SpawnCount");
        }

        if (p_98270_1_.hasKey("MaxNearbyEntities", 99))
        {
            this.maxNearbyEntities = p_98270_1_.getShort("MaxNearbyEntities");
            this.activatingRangeFromPlayer = p_98270_1_.getShort("RequiredPlayerRange");
        }

        if (p_98270_1_.hasKey("SpawnRange", 99))
        {
            this.spawnRange = p_98270_1_.getShort("SpawnRange");
        }

        if (this.getSpawnerWorld() != null)
        {
            this.cachedEntity = null;
        }
    }

    public void writeToNBT(NBTTagCompound p_98280_1_)
    {
        p_98280_1_.setString("EntityId", this.getEntityNameToSpawn());
        p_98280_1_.setShort("Delay", (short)this.spawnDelay);
        p_98280_1_.setShort("MinSpawnDelay", (short)this.minSpawnDelay);
        p_98280_1_.setShort("MaxSpawnDelay", (short)this.maxSpawnDelay);
        p_98280_1_.setShort("SpawnCount", (short)this.spawnCount);
        p_98280_1_.setShort("MaxNearbyEntities", (short)this.maxNearbyEntities);
        p_98280_1_.setShort("RequiredPlayerRange", (short)this.activatingRangeFromPlayer);
        p_98280_1_.setShort("SpawnRange", (short)this.spawnRange);

        if (this.getRandomEntity() != null)
        {
            p_98280_1_.setTag("SpawnData", this.getRandomEntity().field_98222_b.copy());
        }

        if (this.getRandomEntity() != null || this.minecartToSpawn.size() > 0)
        {
            NBTTagList nbttaglist = new NBTTagList();

            if (this.minecartToSpawn.size() > 0)
            {
                Iterator iterator = this.minecartToSpawn.iterator();

                while (iterator.hasNext())
                {
                    MobSpawnerBaseLogic.WeightedRandomMinecart weightedrandomminecart = (MobSpawnerBaseLogic.WeightedRandomMinecart)iterator.next();
                    nbttaglist.appendTag(weightedrandomminecart.func_98220_a());
                }
            }
            else
            {
                nbttaglist.appendTag(this.getRandomEntity().func_98220_a());
            }

            p_98280_1_.setTag("SpawnPotentials", nbttaglist);
        }
    }

    /**
     * Sets the delay to minDelay if parameter given is 1, else return false.
     */
    public boolean setDelayToMin(int p_98268_1_)
    {
        if (p_98268_1_ == 1 && this.getSpawnerWorld().isRemote)
        {
            this.spawnDelay = this.minSpawnDelay;
            return true;
        }
        else
        {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    public Entity func_180612_a(World worldIn)
    {
        if (this.cachedEntity == null)
        {
            Entity entity = EntityList.createEntityByName(this.getEntityNameToSpawn(), worldIn);

            if (entity != null)
            {
                entity = this.func_180613_a(entity, false);
                this.cachedEntity = entity;
            }
        }

        return this.cachedEntity;
    }

    private MobSpawnerBaseLogic.WeightedRandomMinecart getRandomEntity()
    {
        return this.randomEntity;
    }

    public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart p_98277_1_)
    {
        this.randomEntity = p_98277_1_;
    }

    public abstract void func_98267_a(int p_98267_1_);

    public abstract World getSpawnerWorld();

    public abstract BlockPos func_177221_b();

    @SideOnly(Side.CLIENT)
    public double getMobRotation()
    {
        return this.mobRotation;
    }

    @SideOnly(Side.CLIENT)
    public double getPrevMobRotation()
    {
        return this.prevMobRotation;
    }

    public class WeightedRandomMinecart extends WeightedRandom.Item
    {
        private final NBTTagCompound field_98222_b;
        private final String entityType;
        private static final String __OBFID = "CL_00000130";

        public WeightedRandomMinecart(NBTTagCompound p_i1945_2_)
        {
            this(p_i1945_2_.getCompoundTag("Properties"), p_i1945_2_.getString("Type"), p_i1945_2_.getInteger("Weight"));
        }

        public WeightedRandomMinecart(NBTTagCompound p_i1946_2_, String p_i1946_3_)
        {
            this(p_i1946_2_, p_i1946_3_, 1);
        }

        private WeightedRandomMinecart(NBTTagCompound p_i45757_2_, String p_i45757_3_, int p_i45757_4_)
        {
            super(p_i45757_4_);

            if (p_i45757_3_.equals("Minecart"))
            {
                if (p_i45757_2_ != null)
                {
                    p_i45757_3_ = EntityMinecart.EnumMinecartType.byNetworkID(p_i45757_2_.getInteger("Type")).getName();
                }
                else
                {
                    p_i45757_3_ = "MinecartRideable";
                }
            }

            this.field_98222_b = p_i45757_2_;
            this.entityType = p_i45757_3_;
        }

        public NBTTagCompound func_98220_a()
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("Properties", this.field_98222_b);
            nbttagcompound.setString("Type", this.entityType);
            nbttagcompound.setInteger("Weight", this.itemWeight);
            return nbttagcompound;
        }
    }
}