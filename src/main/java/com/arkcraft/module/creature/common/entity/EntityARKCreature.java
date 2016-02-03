package com.arkcraft.module.creature.common.entity;

import io.netty.buffer.ByteBuf;

import java.util.Random;
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

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.GlobalAdditions.GUI;
import com.arkcraft.module.core.common.handlers.GuiHandler;
import com.arkcraft.module.creature.common.entity.creature.Creature;

/**
 * @author gegy1000, Lewis_McReu
 */
public class EntityARKCreature extends EntityCreature implements
		IEntityAdditionalSpawnData, IInventory
{
	private int health, weight, oxygen, food, damage, speed, stamina, torpor,
			level, tamingProgress, xp;

	private int maxHealth, maxWeight, maxOxygen, maxFood, maxDamage, maxSpeed,
			maxStamina, maxTorpor, maxLevel;

	private boolean unconscious, grownUp;

	private ItemStack[] inventory;
	private ItemStack saddle;

	private Creature creature;

	private UUID owner;
	private UUID tamer;

	public EntityARKCreature(World world)
	{
		super(world);
		updateHitbox();
		inventory = new ItemStack[creature.getInventorySize()];
	}

	@Override
	public void entityInit()
	{
		super.entityInit();

		LogHelper.info("init");

		creature = ARKEntityRegistry.getCreature(this);

		// Determine and set stats
		Random r = new Random();

		maxLevel = r.nextInt(90) + 1;
		int points = maxLevel;
		int healthInc = r.nextInt(points);
		points -= healthInc;
		int weightInc = r.nextInt(points);
		points -= weightInc;
		int oxygenInc = r.nextInt(points);
		points -= oxygenInc;
		int torporInc = r.nextInt(points);
		points -= torporInc;
		int damageInc = r.nextInt(points);
		points -= damageInc;
		int speedInc = r.nextInt(points);
		points -= speedInc;
		int staminaInc = r.nextInt(points);
		points -= staminaInc;
		int foodInc = points;

		maxHealth = creature.getBaseHealth() + creature.getWildHealthIncrease() * healthInc;
		maxWeight = creature.getBaseWeight() + creature.getWildWeightIncrease() * weightInc;
		maxOxygen = creature.getBaseOxygen() + creature.getWildOxygenIncrease() * oxygenInc;
		maxFood = creature.getBaseFood() + creature.getWildFoodIncrease() * foodInc;
		maxDamage = creature.getBaseDamage() + creature.getWildDamageIncrease() * damageInc;
		maxSpeed = creature.getBaseSpeed() + creature.getWildSpeedIncrease() * speedInc;
		maxStamina = creature.getBaseStamina() + creature
				.getWildStaminaIncrease() * staminaInc;
		maxTorpor = creature.getBaseTorpor() + creature.getWildTorporIncrease() * torporInc;

		// set actual stats
		health = maxHealth;
		weight = 0;
		oxygen = maxOxygen;
		food = maxFood;
		damage = maxDamage;
		speed = maxSpeed;
		stamina = maxStamina;
		torpor = 0;
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeInt(maxHealth);
		buffer.writeInt(maxWeight);
		buffer.writeInt(maxOxygen);
		buffer.writeInt(maxFood);
		buffer.writeInt(maxDamage);
		buffer.writeInt(maxSpeed);
		buffer.writeInt(maxStamina);
		buffer.writeInt(maxTorpor);
	}

	@Override
	public void readSpawnData(ByteBuf buffer)
	{
		maxHealth = buffer.readInt();
		maxWeight = buffer.readInt();
		maxOxygen = buffer.readInt();
		maxFood = buffer.readInt();
		maxDamage = buffer.readInt();
		maxSpeed = buffer.readInt();
		maxStamina = buffer.readInt();
		maxTorpor = buffer.readInt();
	}

	// private int torpor;
	// private int stamina;
	// private int hunger;
	// private int creatureAge;
	// private int tamingProgress;
	//
	// private int level;
	// private int baseLevel;
	//
	// private boolean unconscious;
	//
	// private Creature creature;
	//
	// private float xp;
	//
	// private ItemStack[] inventory;
	// private ItemStack saddle;
	// private boolean isSaddled;

	private static final float XP_PER_CREATURE_LEVEL = 10.0F;

	private static final int DATA_WATCHER_AGE = 25;
	private static final int DATA_WATCHER_TORPOR = 26;
	private static final int DATA_WATCHER_STAMINA = 27;
	private static final int DATA_WATCHER_HUNGER = 28;
	private static final int DATA_WATCHER_LEVEL = 29;
	private static final int DATA_WATCHER_UNCONSCIOUS = 30;
	private static final int DATA_WATCHER_TAMING_PROGRESS = 31;
	private static final int DATA_WATCHER_XP = 32;
	private static final int DATA_WATCHER_SADDLED = 33;

	private LevelManager levelManager;

	// private UUID owner;
	// public EntityPlayer playerTaming;

	// public EntityARKCreature(World world)
	// {
	// super(world);
	// updateHitbox();
	// inventory = new ItemStack[creature.getInventorySize()];
	// }

	// @Override
	// public void entityInit()
	// {
	// super.entityInit();
	//
	// creature = ARKEntityRegistry.getCreature(this);
	//
	// this.dataWatcher.addObject(DATA_WATCHER_AGE, 0);
	// this.dataWatcher.addObject(DATA_WATCHER_TORPOR, 0);
	// this.dataWatcher.addObject(DATA_WATCHER_STAMINA, 0);
	// this.dataWatcher.addObject(DATA_WATCHER_HUNGER, 0);
	// this.dataWatcher.addObject(DATA_WATCHER_LEVEL, 0);
	// this.dataWatcher.addObject(DATA_WATCHER_UNCONSCIOUS, (byte) 0);
	// this.dataWatcher.addObject(DATA_WATCHER_TAMING_PROGRESS, 0);
	// // this.dataWatcher.addObject(DATA_WATCHER_XP, 0.0F);
	// // this.dataWatcher.addObject(DATA_WATCHER_SADDLED, (byte) 0);
	//
	// // levelManager.initDataWatcher();
	// }

	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);

		LogHelper.info("death");

		Entity killedBy = cause.getSourceOfDamage();

		// TODO projectiles and players

		if (killedBy instanceof EntityARKCreature)
		{
			((EntityARKCreature) killedBy).addXP(creature.getKillXP());
		}
		else if (killedBy instanceof EntityPlayer)
		{
			// TODO ARKPlayer data update
		}
	}

	@Override
	protected boolean interact(EntityPlayer player)
	{
		GuiHandler.rightClickedEntity = this;
		if (unconscious && !this.worldObj.isRemote && owner == null)
		{
			player.openGui(ARKCraft.instance, GUI.TAMING_GUI.getID(),
					this.worldObj, (int) this.posX, (int) this.posY,
					(int) this.posZ);
			return true;
		}
		return false;
	}

	public void addXP(float xp)
	{
		this.xp += xp;

		int levels = (int) Math.floor(xp / XP_PER_CREATURE_LEVEL);
		levelUp(levels);
	}

	private void levelUp(int levels)
	{
		level += levels;

		level = Math.min(level, getMaxLevel());
	}

	public int getMaxLevel()
	{
		return maxLevel + 54;
	}

	private float getScaled(float value, float maxValue, float scale)
	{
		return maxValue != 0 && value != 0 ? value * scale / maxValue : 0;
	}

	public void increaseTorpor(int amount, Entity shootingEntity)
	{
		torpor += amount;
		LogHelper.info("Torpor: " + torpor);

		if (torpor >= creature.getTorporKnockout()) // TODO higher level dinos
													// take more to knock out?
		{
			if (shootingEntity instanceof EntityPlayer) this.tamer = shootingEntity
					.getUniqueID();
			this.unconscious = true;
			this.tamingProgress = 0;
		}
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		// if (!worldObj.isRemote)
		{
			// this.dataWatcher.updateObject(DATA_WATCHER_AGE, creatureAge);
			// this.dataWatcher.updateObject(DATA_WATCHER_TORPOR, torpor);
			// this.dataWatcher.updateObject(DATA_WATCHER_STAMINA, stamina);
			// this.dataWatcher.updateObject(DATA_WATCHER_HUNGER, hunger);
			// this.dataWatcher.updateObject(DATA_WATCHER_LEVEL, level);
			// this.dataWatcher.updateObject(DATA_WATCHER_UNCONSCIOUS,
			// (byte) (unconscious ? 1 : 0));
			// this.dataWatcher.updateObject(DATA_WATCHER_TAMING_PROGRESS,
			// tamingProgress);
			// this.dataWatcher.updateObject(DATA_WATCHER_XP, xp);
			// this.dataWatcher.updateObject(DATA_WATCHER_SADDLED, (byte)
			// (isSaddled() ? 1 : 0));

			if (!grownUp)
			{
				updateHitbox();
			}

			if (torpor > 0) torpor -= creature.getTorporLossSpeed();

			if (unconscious && torpor <= 0)
			{
				unconscious = false;
				this.tamer = null;
				torpor = 0;
				tamingProgress = 0;
			}
		}
		// else
		{
			// creatureAge =
			// dataWatcher.getWatchableObjectInt(DATA_WATCHER_AGE);
			// torpor = dataWatcher.getWatchableObjectInt(DATA_WATCHER_TORPOR);
			// stamina =
			// dataWatcher.getWatchableObjectInt(DATA_WATCHER_STAMINA);
			// hunger = dataWatcher.getWatchableObjectInt(DATA_WATCHER_HUNGER);
			// level = dataWatcher.getWatchableObjectInt(DATA_WATCHER_LEVEL);
			// unconscious = dataWatcher
			// .getWatchableObjectByte(DATA_WATCHER_UNCONSCIOUS) == 1;
			// tamingProgress = dataWatcher
			// .getWatchableObjectInt(DATA_WATCHER_TAMING_PROGRESS);
			// xp = dataWatcher.getWatchableObjectFloat(DATA_WATCHER_XP);
			// isSaddled =
			// dataWatcher.getWatchableObjectByte(DATA_WATCHER_SADDLED) == 1;
		}
	}

	public boolean isSaddled()
	{
		return saddle != null;
	}

	public void updateHitbox()
	{
		this.setSize(
				scaleByAge(creature.getBabySizeXZ(), creature.getAdultSizeXZ()),
				scaleByAge(creature.getBabySizeY(), creature.getAdultSizeY()));
		if (ticksExisted >= creature.getGrowthTime()) grownUp = true;
	}

	public float scaleByAge(float baby, float adult)
	{
		int growthTime = ticksExisted > creature.getGrowthTime() ? creature
				.getGrowthTime() : ticksExisted;
		return (adult - baby) / creature.getGrowthTime() * growthTime + baby;
	}

	@Override
	public void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		LogHelper.info("attributes");
		this.updateEntityAttributes();
		this.updateHitbox();
	}

	public void updateEntityAttributes()
	{
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(
				maxHealth);
	}

	// @Override
	// public void writeSpawnData(ByteBuf buffer)
	// {
	// buffer.writeInt(creatureAge);
	// buffer.writeInt(torpor);
	// buffer.writeInt(stamina);
	// buffer.writeInt(hunger);
	// buffer.writeInt(baseLevel);
	// buffer.writeInt(level);
	// buffer.writeBoolean(unconscious);
	// buffer.writeInt(tamingProgress);
	// }
	//
	// @Override
	// public void readSpawnData(ByteBuf additionalData)
	// {
	// creatureAge = additionalData.readInt();
	// torpor = additionalData.readInt();
	// stamina = additionalData.readInt();
	// hunger = additionalData.readInt();
	// baseLevel = additionalData.readInt();
	// level = additionalData.readInt();
	// unconscious = additionalData.readBoolean();
	// tamingProgress = additionalData.readInt();
	// }

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);

		this.maxDamage = nbt.getInteger("maxDamage");
		this.maxFood = nbt.getInteger("maxFood");
		this.maxHealth = nbt.getInteger("maxHealth");
		this.maxLevel = nbt.getInteger("maxLevel");
		this.maxOxygen = nbt.getInteger("maxOxygen");
		this.maxSpeed = nbt.getInteger("maxSpeed");
		this.maxStamina = nbt.getInteger("maxStamina");
		this.maxTorpor = nbt.getInteger("maxTorpor");
		this.maxWeight = nbt.getInteger("maxWeight");

		this.damage = nbt.getInteger("damage");
		this.food = nbt.getInteger("food");
		this.health = nbt.getInteger("health");
		this.level = nbt.getInteger("level");
		this.oxygen = nbt.getInteger("oxygen");
		this.speed = nbt.getInteger("speed");
		this.stamina = nbt.getInteger("stamina");
		this.torpor = nbt.getInteger("torpor");
		this.weight = nbt.getInteger("weight");
		this.xp = nbt.getInteger("xp");

		this.tamingProgress = nbt.getInteger("tamingProgress");
		this.unconscious = nbt.getBoolean("unconscious");
		this.tamer = UUID.fromString(nbt.getString("tamer"));

		this.grownUp = nbt.getBoolean("grownUp");

		this.saddle = ItemStack.loadItemStackFromNBT(nbt
				.getCompoundTag("saddle"));
		NBTTagList inv = nbt.getTagList("creatureInventory", 10);
		for (int i = 0; i < inv.tagCount(); i++)
		{
			NBTTagCompound comp = inv.getCompoundTagAt(i);
			NBTTagCompound comp2 = comp.getCompoundTag("stack");
			if (comp2 != null)
			{
				int index = comp.getInteger("index");
				ItemStack stack = ItemStack.loadItemStackFromNBT(comp2);
				inventory[index] = stack;
			}
		}

		owner = UUID.fromString(nbt.getString("owner"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);

		nbt.setInteger("maxDamage", maxDamage);
		nbt.setInteger("maxFood", maxFood);
		nbt.setInteger("maxHealth", maxHealth);
		nbt.setInteger("maxLevel", maxLevel);
		nbt.setInteger("maxOxygen", maxOxygen);
		nbt.setInteger("maxSpeed", maxSpeed);
		nbt.setInteger("maxStamina", maxStamina);
		nbt.setInteger("maxTorpor", maxTorpor);
		nbt.setInteger("maxWeight", maxWeight);

		nbt.setInteger("damage", damage);
		nbt.setInteger("food", food);
		nbt.setInteger("health", health);
		nbt.setInteger("level", level);
		nbt.setInteger("oxygen", oxygen);
		nbt.setInteger("speed", speed);
		nbt.setInteger("stamina", stamina);
		nbt.setInteger("torpor", torpor);
		nbt.setInteger("weight", weight);
		nbt.setInteger("xp", xp);

		if (owner != null) nbt.setString("owner", owner.toString());
		else if (tamer != null)
		{
			nbt.setInteger("tamingProgress", tamingProgress);
			nbt.setString("tamer", tamer.toString());
		}

		nbt.setBoolean("unconscious", unconscious);
		nbt.setBoolean("grownUp", grownUp);

		if (saddle != null)
		{
			NBTTagCompound sad = new NBTTagCompound();
			saddle.writeToNBT(sad);
			nbt.setTag("saddle", sad);
		}

		NBTTagList inv = new NBTTagList();
		for (int i = 0; i < inventory.length; i++)
		{
			if (inventory[i] != null)
			{
				NBTTagCompound comp = new NBTTagCompound();
				comp.setInteger("index", i);
				NBTTagCompound comp2 = new NBTTagCompound();
				inventory[i].writeToNBT(comp2);
				comp.setTag("stack", comp2);
				inv.appendTag(comp);
			}
		}

		nbt.setTag("creatureInventory", inv);
	}

	@Override
	public int getSizeInventory()
	{
		return creature.getInventorySize();
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		// TODO possibly return stack in saddle slot
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
	{
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return isDead ? false : player.getDistanceSqToEntity(this) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{
	}

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

	public void setSitting(boolean b)
	{
		// TODO Auto-generated method stub

	}

	public int getTorpor()
	{
		return torpor;
	}

	public int getTamingProgress()
	{
		return tamingProgress;
	}

	public int getMaxTorpor()
	{
		return maxTorpor;
	}

	public int getTamingProgressRequired()
	{
		return (getMaxLevel() * 100) / getMaxLevel();
	}
}
