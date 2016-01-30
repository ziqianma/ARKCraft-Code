package com.arkcraft.module.creature.common.entity;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import com.arkcraft.module.core.common.container.inventory.InventorySaddle;
import com.arkcraft.module.core.common.creature.Creature;

/**
 * @author gegy1000
 */
public class EntityARKCreature extends EntityCreature implements IEntityAdditionalSpawnData, IInventory
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

    private float xp;

    private ItemStack[] inventory;
    private InventorySaddle saddle;
    private boolean isSaddled;

    private static final float XP_PER_CREATURE_LEVEL = 10.0F;

    private static final int DATA_WATCHER_AGE = 25;
    private static final int DATA_WATCHER_TORPOR = 26;
    private static final int DATA_WATCHER_STAMINA = 27;
    private static final int DATA_WATCHER_HUNGER = 28;
    private static final int DATA_WATCHER_LEVEL = 29;
    private static final int DATA_WATCHER_UNCONSCIOUS = 30;
    private static final int DATA_WATCHER_TAME_TIME = 31;
    private static final int DATA_WATCHER_XP = 32;
    private static final int DATA_WATCHER_SADDLED = 33;

    private LevelManager levelManager;

    private UUID owner;

    public EntityARKCreature(World world)
    {
        super(world);

        updateHitbox();
        inventory = new ItemStack[creature.getInventorySize()];
        saddle = new InventorySaddle();
    }

    @Override
    public void entityInit()
    {
        super.entityInit();

        creature = ARKEntityRegistry.getCreature(this);

        this.dataWatcher.addObject(DATA_WATCHER_AGE, 0);
        this.dataWatcher.addObject(DATA_WATCHER_TORPOR, 0);
        this.dataWatcher.addObject(DATA_WATCHER_STAMINA, 0);
        this.dataWatcher.addObject(DATA_WATCHER_HUNGER, 0);
        this.dataWatcher.addObject(DATA_WATCHER_LEVEL, 0);
        this.dataWatcher.addObject(DATA_WATCHER_UNCONSCIOUS, (byte) 0);
        this.dataWatcher.addObject(DATA_WATCHER_TAME_TIME, 0);
        this.dataWatcher.addObject(DATA_WATCHER_XP, 0.0F);
        this.dataWatcher.addObject(DATA_WATCHER_SADDLED, (byte) 0);

        levelManager.initDataWatcher();
    }

    @Override
    public void onDeath(DamageSource cause)
    {
        super.onDeath(cause);

        Entity killedBy = cause.getSourceOfDamage();

        //TODO projectiles and players

        if (killedBy instanceof EntityARKCreature)
        {
            ((EntityARKCreature) killedBy).addXP(creature.getKillXP());
        }
    }

    public void addXP(float xp)
    {
        this.xp += xp;

        int levels = (int) Math.floor(xp / XP_PER_CREATURE_LEVEL);
        levelUp(levels);
    }

    public void levelUp(int levels)
    {
        level += levels;

        level = Math.min(level, getMaxLevel());
    }

    public int getMaxLevel()
    {
        return baseLevel + 54;
    }

    private float getScaled(float value, float maxValue, float scale)
    {
        return maxValue != 0 && value != 0 ? value * scale / maxValue : 0;
    }

    public void increaseTorpor(int amount)
    {
        torpor += amount;

        if (torpor >= creature.getTorporKnockout()) //TODO higher level dinos take more to knock out?
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
            this.dataWatcher.updateObject(DATA_WATCHER_AGE, creatureAge);
            this.dataWatcher.updateObject(DATA_WATCHER_TORPOR, torpor);
            this.dataWatcher.updateObject(DATA_WATCHER_STAMINA, stamina);
            this.dataWatcher.updateObject(DATA_WATCHER_HUNGER, hunger);
            this.dataWatcher.updateObject(DATA_WATCHER_LEVEL, level);
            this.dataWatcher.updateObject(DATA_WATCHER_UNCONSCIOUS, (byte) (unconscious ? 1 : 0));
            this.dataWatcher.updateObject(DATA_WATCHER_TAME_TIME, tameTime);
            this.dataWatcher.updateObject(DATA_WATCHER_XP, xp);
            this.dataWatcher.updateObject(DATA_WATCHER_SADDLED, (byte) (isSaddled() ? 1 : 0));

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
            creatureAge = dataWatcher.getWatchableObjectInt(DATA_WATCHER_AGE);
            torpor = dataWatcher.getWatchableObjectInt(DATA_WATCHER_TORPOR);
            stamina = dataWatcher.getWatchableObjectInt(DATA_WATCHER_STAMINA);
            hunger = dataWatcher.getWatchableObjectInt(DATA_WATCHER_HUNGER);
            level = dataWatcher.getWatchableObjectInt(DATA_WATCHER_LEVEL);
            unconscious = dataWatcher.getWatchableObjectByte(DATA_WATCHER_UNCONSCIOUS) == 1;
            tameTime = dataWatcher.getWatchableObjectInt(DATA_WATCHER_TAME_TIME);
            xp = dataWatcher.getWatchableObjectFloat(DATA_WATCHER_XP);
            isSaddled = dataWatcher.getWatchableObjectByte(DATA_WATCHER_SADDLED) == 1;
        }
    }

    public boolean isSaddled()
    {
        return worldObj.isRemote ? isSaddled : saddle.getStackInSlot(0) != null;
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

        this.updateEntityAttributes();
        this.updateHitbox();
    }

    public void updateEntityAttributes()
    {
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(getCreatureAge());
    }

    public int getCreatureAge()
    {
        int healthBase = creature.getHealthBase();

        return creature.getHealthBase();
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

        if (owner != null)
        {
            nbt.setString("Owner", owner.toString());
        }

        NBTTagList itemsTag = nbt.getTagList("Items", 10);
        inventory = new ItemStack[getSizeInventory()];

        for (int i = 0; i < itemsTag.tagCount(); ++i)
        {
            NBTTagCompound slotTag = itemsTag.getCompoundTagAt(i);
            int j = slotTag.getByte("Slot") & 255;

            if (j >= 0 && j < inventory.length)
            {
                setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(slotTag));
            }
        }
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

        if (nbt.hasKey("Owner"))
        {
            owner = UUID.fromString(nbt.getString("Owner"));
        }

        NBTTagList items = new NBTTagList();

        for (int i = 0; i < inventory.length; ++i)
        {
            if (inventory[i] != null)
            {
                NBTTagCompound slotTag = new NBTTagCompound();
                slotTag.setByte("Slot", (byte) i);
                inventory[i].writeToNBT(slotTag);
                items.appendTag(slotTag);
            }
        }

        nbt.setTag("Items", items);
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        if (inventory[index] != null)
        {
            ItemStack itemstack;

            if (inventory[index].stackSize <= count)
            {
                itemstack = inventory[index];
                setInventorySlotContents(index, null);
                return itemstack;
            }
            else
            {
                itemstack = inventory[index].splitStack(count);

                if (inventory[index].stackSize == 0)
                {
                    setInventorySlotContents(index, null);
                }

                return itemstack;
            }
        }
        else
        {
            return null;
        }
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index)
    {
        if (inventory[index] != null)
        {
            ItemStack itemstack = inventory[index];
            setInventorySlotContents(index, null);
            return itemstack;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        inventory[index] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public void markDirty()
    {}

    @Override
    public boolean isUseableByPlayer(EntityPlayer player)
    {
        return isDead ? false : player.getDistanceSqToEntity(this) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player)
    {}

    @Override
    public void closeInventory(EntityPlayer player)
    {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return true;
    }

    @Override
    public int getField(int id)
    {
        return 0;
    }

    @Override
    public void setField(int id, int value)
    {

    }

    @Override
    public int getFieldCount()
    {
        return 0;
    }

    @Override
    public void clear()
    {
        for (int i = 0; i < getSizeInventory(); i++)
        {
            setInventorySlotContents(i, null);
        }
    }

    public Creature getCreature()
    {
        return creature;
    }
}
