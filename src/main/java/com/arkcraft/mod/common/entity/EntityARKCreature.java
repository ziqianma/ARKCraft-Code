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
public class EntityARKCreature extends EntityCreature implements IEntityAdditionalSpawnData
{
    private int torpor;
    private int stamina;
    private int hunger;
    private int creatureAge;
    private int tameTime;

    private int level;
    private int baseLevel;

    private boolean unconscious;

    private Creature creature;

    public EntityARKCreature(World world)
    {
        super(world);
        updateHitbox();
    }

    @Override
    public void entityInit()
    {
        super.entityInit();

        creature = ARKEntityRegistry.getCreature(this);
    }

    public void increaseTorpor(int amount)
    {
        torpor += amount;

        if (torpor >= creature.getTorporKnockout())
        {
            this.unconscious = true;
            this.tameTime = 0;
        }
    }

    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!worldObj.isRemote)
        {
            this.dataWatcher.updateObject(25, creatureAge);
            this.dataWatcher.updateObject(26, torpor);
            this.dataWatcher.updateObject(27, stamina);
            this.dataWatcher.updateObject(28, hunger);
            this.dataWatcher.updateObject(29, level);
            this.dataWatcher.updateObject(30, (byte) (unconscious ? 1 : 0));
            this.dataWatcher.updateObject(31, tameTime);

            if (creatureAge < creature.getGrowthTime())
            {
                this.creatureAge++;

                updateHitbox();
            }

            torpor -= creature.getTorporLossSpeed();

            if (unconscious && torpor <= 0)
            {
                unconscious = false;
                tameTime = 0;
            }
        }
        else
        {
            creatureAge = dataWatcher.getWatchableObjectInt(25);
            torpor = dataWatcher.getWatchableObjectInt(26);
            stamina = dataWatcher.getWatchableObjectInt(27);
            hunger = dataWatcher.getWatchableObjectInt(28);
            level = dataWatcher.getWatchableObjectInt(29);
            unconscious = dataWatcher.getWatchableObjectByte(30) == 1;
        }
    }

    public void updateHitbox()
    {
        this.setSize(scaleByAge(creature.getBabySizeXZ(), creature.getAdultSizeXZ()), scaleByAge(creature.getBabySizeY(), creature.getAdultSizeY()));
    }

    public float scaleByAge(float baby, float adult)
    {
        int growthTime = creature.getGrowthTime();

        if (creatureAge > growthTime)
        {
            creatureAge = growthTime;
        }

        return (adult - baby) / growthTime * creatureAge + baby;
    }

    @Override
    public void applyEntityAttributes()
    {
        super.applyEntityAttributes();

        this.dataWatcher.addObject(25, new Integer(0)); // Creature Age
        this.dataWatcher.addObject(26, new Integer(0)); // Torpor
        this.dataWatcher.addObject(27, new Integer(0)); // Stamina
        this.dataWatcher.addObject(28, new Integer(0)); // Hunger
        this.dataWatcher.addObject(29, new Integer(0)); // Level
        this.dataWatcher.addObject(30, new Byte((byte) 0)); // Unconscious?
        this.dataWatcher.addObject(31, new Integer(0)); // Tame Time
    }

    @Override
    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeInt(creatureAge);
        buffer.writeInt(torpor);
        buffer.writeInt(stamina);
        buffer.writeInt(hunger);
        buffer.writeInt(baseLevel);
        buffer.writeInt(level);
        buffer.writeBoolean(unconscious);
        buffer.writeInt(tameTime);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData)
    {
        creatureAge = additionalData.readInt();
        torpor = additionalData.readInt();
        stamina = additionalData.readInt();
        hunger = additionalData.readInt();
        baseLevel = additionalData.readInt();
        level = additionalData.readInt();
        unconscious = additionalData.readBoolean();
        tameTime = additionalData.readInt();
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt)
    {
        super.readEntityFromNBT(nbt);

        this.creatureAge = nbt.getInteger("CreatureAge");
        this.torpor = nbt.getInteger("Torpor");
        this.stamina = nbt.getInteger("Stamina");
        this.hunger = nbt.getInteger("Hunger");
        this.level = nbt.getInteger("Level");
        this.baseLevel = nbt.getInteger("BaseLevel");
        this.unconscious = nbt.getBoolean("Unconscious");
        this.tameTime = nbt.getInteger("TameTime");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt)
    {
        super.writeEntityToNBT(nbt);

        nbt.setInteger("CreatureAge", creatureAge);
        nbt.setInteger("Torpor", torpor);
        nbt.setInteger("Stamina", stamina);
        nbt.setInteger("Hunger", hunger);
        nbt.setInteger("Level", level);
        nbt.setInteger("BaseLevel", baseLevel);
        nbt.setBoolean("Unconscous", unconscious);
        nbt.setInteger("TameTime", tameTime);
    }
}
