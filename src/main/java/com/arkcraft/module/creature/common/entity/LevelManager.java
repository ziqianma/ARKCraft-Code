package com.arkcraft.module.creature.common.entity;

import net.minecraft.entity.DataWatcher;
import net.minecraft.nbt.NBTTagCompound;

public class LevelManager
{
    private int leveledHealth;
    private int leveledHunger;
    private int leveledStamina;
    private int leveledMeleeDamage;
    private int leveledSpeed;

    private EntityARKCreature entity;

    private static final int DATA_WATCHER_LEVELED_HEALTH = 34;
    private static final int DATA_WATCHER_LEVELED_HUNGER = 35;
    private static final int DATA_WATCHER_LEVELED_STAMINA = 36;
    private static final int DATA_WATCHER_LEVELED_MELEE_DAMAGE = 37;
    private static final int DATA_WATCHER_LEVELED_SPEED = 38;

    public LevelManager(EntityARKCreature entity)
    {
        this.entity = entity;
    }

    public void initDataWatcher()
    {
        DataWatcher dataWatcher = entity.getDataWatcher();

        dataWatcher.addObject(DATA_WATCHER_LEVELED_HEALTH, 0);
        dataWatcher.addObject(DATA_WATCHER_LEVELED_HUNGER, 0);
        dataWatcher.addObject(DATA_WATCHER_LEVELED_STAMINA, 0);
        dataWatcher.addObject(DATA_WATCHER_LEVELED_MELEE_DAMAGE, 0);
        dataWatcher.addObject(DATA_WATCHER_LEVELED_SPEED, 0);
    }

    public int getMaxHealth()
    {
        return entity.getCreature().getHealthBase() + leveledHealth;
    }

    public int getMaxHunger()
    {
        return entity.getCreature().getHungerBase() + leveledHunger;
    }

    public int getMaxStamina()
    {
        return entity.getCreature().getStaminaBase() + leveledStamina;
    }

    public int getMeleeDamage()
    {
        return entity.getCreature().getMeleeDamageBase() + leveledMeleeDamage;
    }

    public int getMaxSpeed()
    {
        return entity.getCreature().getSpeedBase() + leveledHealth;
    }

    public void increaseHealth(int amount)
    {
        leveledHealth += amount;

        entity.getDataWatcher().updateObject(DATA_WATCHER_LEVELED_HEALTH, leveledHealth);
    }

    public void increaseHunger(int amount)
    {
        leveledHunger += amount;

        entity.getDataWatcher().updateObject(DATA_WATCHER_LEVELED_HUNGER, leveledHunger);
    }

    public void increaseStamina(int amount)
    {
        leveledStamina += amount;

        entity.getDataWatcher().updateObject(DATA_WATCHER_LEVELED_STAMINA, leveledStamina);
    }

    public void increaseMeleeDamage(int amount)
    {
        leveledMeleeDamage += amount;

        entity.getDataWatcher().updateObject(DATA_WATCHER_LEVELED_MELEE_DAMAGE, leveledMeleeDamage);
    }

    public void increaseSpeed(int amount)
    {
        leveledSpeed += amount;

        entity.getDataWatcher().updateObject(DATA_WATCHER_LEVELED_SPEED, leveledSpeed);
    }

    public void readFromNBT(NBTTagCompound nbt)
    {
        leveledHealth = nbt.getInteger("LeveledHealth");
        leveledHunger = nbt.getInteger("LeveledHunger");
        leveledStamina = nbt.getInteger("LeveledStamina");
        leveledMeleeDamage = nbt.getInteger("LeveledMeleeDamage");
        leveledSpeed = nbt.getInteger("LeveledSpeed");
    }

    public void writeToNBT(NBTTagCompound nbt)
    {
        nbt.setInteger("LeveledHealth", leveledHealth);
        nbt.setInteger("LeveledHunger", leveledHunger);
        nbt.setInteger("LeveledStamina", leveledStamina);
        nbt.setInteger("LeveledMeleeDamage", leveledMeleeDamage);
        nbt.setInteger("LeveledSpeed", leveledSpeed);
    }
}
