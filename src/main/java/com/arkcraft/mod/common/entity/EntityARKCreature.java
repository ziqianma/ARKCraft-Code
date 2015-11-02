package com.arkcraft.mod.common.entity;

import com.arkcraft.mod.common.creature.Creature;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityCreature;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

/**
 * @author gegy1000
 */
public class EntityARKCreature extends EntityCreature implements IEntityAdditionalSpawnData {
    private int torpor;
    private int stamina;
    private int hunger;
    private int water;
    private int creatureAge;

    private boolean taming;

    private Creature creature;

    public EntityARKCreature(World world) {
        super(world);
    }

    @Override
    public void entityInit() {
        super.entityInit();

        creature = ARKEntityRegistry.getCreature(this);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();

        if (!worldObj.isRemote)
        {
            this.dataWatcher.updateObject(25, creatureAge);
            this.dataWatcher.updateObject(26, torpor);
            this.dataWatcher.updateObject(27, stamina);
            this.dataWatcher.updateObject(28, hunger);
            this.dataWatcher.updateObject(29, water);

            if (creatureAge < creature.getGrowthTime()) {
                this.creatureAge++;

                updateHitbox();
            }
        }
        else
        {
            creatureAge = dataWatcher.getWatchableObjectInt(25);
            torpor = dataWatcher.getWatchableObjectInt(26);
            stamina = dataWatcher.getWatchableObjectInt(27);
            hunger = dataWatcher.getWatchableObjectInt(28);
            water = dataWatcher.getWatchableObjectInt(29);
        }
    }

    public void updateHitbox() {
        this.setSize(scaleByAge(creature.getBabySizeXZ(), creature.getAdultSizeXZ()), scaleByAge(creature.getBabySizeY(), creature.getAdultSizeY()));
    }

    public float scaleByAge(float baby, float adult) {
        int growthTime = creature.getGrowthTime();

        if (creatureAge > growthTime)
        {
            creatureAge = growthTime;
        }

        return (adult - baby) / growthTime * creatureAge + baby;
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();

        this.dataWatcher.addObject(25, new Integer(0)); // Creature Age
        this.dataWatcher.addObject(26, new Integer(0)); // Torpor
        this.dataWatcher.addObject(27, new Integer(0)); // Stamina
        this.dataWatcher.addObject(28, new Integer(0)); // Hunger
        this.dataWatcher.addObject(29, new Integer(0)); // Water
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(creatureAge);
        buffer.writeInt(torpor);
        buffer.writeInt(stamina);
        buffer.writeInt(water);
        buffer.writeInt(hunger);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        creatureAge = additionalData.readInt();
        torpor = additionalData.readInt();
        stamina = additionalData.readInt();
        water = additionalData.readInt();
        hunger = additionalData.readInt();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);

        this.creatureAge = nbt.getInteger("CreatureAge");
        this.torpor = nbt.getInteger("Torpor");
        this.stamina = nbt.getInteger("Stamina");
        this.hunger = nbt.getInteger("Hunger");
        this.water = nbt.getInteger("Water");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);

        nbt.setInteger("CreatureAge", creatureAge);
        nbt.setInteger("Torpor", torpor);
        nbt.setInteger("Stamina", stamina);
        nbt.setInteger("Hunger", hunger);
        nbt.setInteger("Water", water);
    }
}
