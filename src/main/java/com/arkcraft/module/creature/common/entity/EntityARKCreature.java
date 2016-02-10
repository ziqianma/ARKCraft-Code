package com.arkcraft.module.creature.common.entity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

import com.arkcraft.module.blocks.common.items.ItemDinosaurSaddle;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.GlobalAdditions.GUI;
import com.arkcraft.module.core.common.entity.data.ARKPlayer;
import com.arkcraft.module.core.common.handlers.GuiHandler;
import com.arkcraft.module.creature.common.entity.creature.Creature;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

/**
 * @author gegy1000, Lewis_McReu
 */
public class EntityARKCreature extends EntityAnimal implements IEntityAdditionalSpawnData, IInventory
{
	private static final int DATA_WATCHER_UNCONSCIOUS = 25, DATA_WATCHER_LEVEL = 26, DATA_WATCHER_TAMING_PROGRESS = 27,
			DATA_WATCHER_XP = 28;

	public static final int HEALTH = 0, DAMAGE = 1, STAMINA = 2, WEIGHT = 3, OXYGEN = 4, FOOD = 5, SPEED = 6;

	private static final int WEIGHT_PER_STACK = 10;

	private int oxygen, food, stamina, torpor, level, tamingProgress, xp, points = 0;

	private int maxHealth, maxWeight, maxOxygen, maxFood, maxDamage, maxSpeed, maxStamina, maxTorpor, baseLevel;

	private boolean unconscious, grownUp;

	private ItemStack[] inventory;
	private ItemStack saddle;

	private Creature creature;

	private UUID owner;
	private UUID tamer;

	public EntityARKCreature(World world)
	{
		super(world);
		grownUp = true;
		updateHitbox();
	}

	@Override
	public void entityInit()
	{
		super.entityInit();

		creature = ARKEntityRegistry.getCreature(this);
		if (!this.worldObj.isRemote)
		{
			points = 0;
			// Determine and set stats
			Random r = new Random();
			baseLevel = r.nextInt(90) + 1;
			level = baseLevel;
			distributeWildPoints(baseLevel);

			// set actual stats
			oxygen = maxOxygen;
			food = maxFood;
			stamina = maxStamina;
			torpor = 0;
			inventory = new ItemStack[maxWeight];
		}

		this.dataWatcher.addObject(DATA_WATCHER_UNCONSCIOUS, (byte) (unconscious ? 1 : 0));
		this.dataWatcher.addObject(DATA_WATCHER_LEVEL, level);
		this.dataWatcher.addObject(DATA_WATCHER_TAMING_PROGRESS, tamingProgress);
		this.dataWatcher.addObject(DATA_WATCHER_XP, xp);
	}

	private void distributeWildPoints(int points)
	{
		Random r = new Random();
		int div = points / 3;
		int maxIncrease = div;
		int healthInc = r.nextInt(maxIncrease);
		points -= healthInc;
		maxIncrease = div < points ? div : points;
		int weightInc = r.nextInt(maxIncrease);
		points -= weightInc;
		maxIncrease = div < points ? div : points;
		int speedInc = r.nextInt(maxIncrease);
		points -= speedInc;
		maxIncrease = div < points ? div : points;
		int oxygenInc = r.nextInt(maxIncrease);
		points -= oxygenInc;
		maxIncrease = div < points ? div : points;
		int torporInc = r.nextInt(maxIncrease);
		points -= torporInc;
		maxIncrease = div < points ? div : points;
		int damageInc = r.nextInt(maxIncrease);
		points -= damageInc;
		maxIncrease = div < points ? div : points;
		int staminaInc = r.nextInt(maxIncrease);
		points -= staminaInc;
		int foodInc = points;

		// TODO fix in comparison to ark
		maxHealth = (int) (creature.getBaseHealth() + creature.getWildHealthIncrease() * healthInc);
		maxWeight = (int) (creature.getBaseWeight() + creature.getWildWeightIncrease() * weightInc) / WEIGHT_PER_STACK;
		maxOxygen = (int) (creature.getBaseOxygen() + creature.getWildOxygenIncrease() * oxygenInc);
		maxFood = (int) (creature.getBaseFood() + creature.getWildFoodIncrease() * foodInc);
		maxDamage = (int) (100 + creature.getWildDamageIncrease() * damageInc);
		maxSpeed = 100;
		maxStamina = (int) (creature.getBaseStamina() + creature.getWildStaminaIncrease() * staminaInc);
		maxTorpor = (int) (creature.getBaseTorpor() + creature.getWildTorporIncrease() * torporInc);
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
		buffer.writeInt(baseLevel);
		buffer.writeInt(oxygen);
		buffer.writeInt(food);
		buffer.writeInt(stamina);
		buffer.writeInt(torpor);
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
		baseLevel = buffer.readInt();
		level = baseLevel;
		oxygen = buffer.readInt();
		food = buffer.readInt();
		stamina = buffer.readInt();
		torpor = buffer.readInt();
		inventory = new ItemStack[maxWeight];
	}

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

		Entity killedBy = cause.getSourceOfDamage();
		Entity indirect = cause.getEntity();

		Entity actualKiller;

		if (indirect != null && killedBy instanceof IProjectile)
		{
			actualKiller = indirect;
		}
		else
		{
			actualKiller = killedBy;
		}

		// TODO grant xp if creature is ridden (possible to kill while ridden)
		// TODO grant xp to owner nearby
		if (actualKiller instanceof EntityARKCreature)
		{
			if (((EntityARKCreature) killedBy)
					.isOwned()) ((EntityARKCreature) killedBy).addXP(creature.getBaseKillXP());
		}
		else if (actualKiller instanceof EntityPlayer)
		{
			ARKPlayer data = ARKPlayer.get((EntityPlayer) actualKiller);
			data.addXP(getKillXP());
		}
	}

	private int getKillXP()
	{
		// TODO Auto-generated method stub
		return creature.getBaseKillXP() * level * level;
	}

	private boolean isOwned()
	{
		return this.owner != null;
	}

	@Override
	public boolean interact(EntityPlayer player)
	{
		GuiHandler.rightClickedEntity = this;
		if (unconscious && !this.worldObj.isRemote && owner == null)
		{
			player.openGui(ARKCraft.instance, GUI.TAMING_GUI.getID(), this.worldObj, (int) this.posX, (int) this.posY,
					(int) this.posZ);
			return true;
		}
		return false;
	}

	public void addXP(float xp)
	{
		this.xp += xp;

		while (checkLevel())
		{
		}
	}

	private boolean checkLevel()
	{
		int level = this.level + 1;
		int xpNeeded = (int) (Math.log(level * level * level * level) * level * level * creature
				.getLevelingMultiplier());
		if (xp > xpNeeded)
		{
			this.level++;
			this.points++;
			xp = xp - xpNeeded;
			return true;
		}
		return false;
	}

	public int getMaxLevel()
	{
		return baseLevel + 59;
	}

	private float getScaled(float value, float maxValue, float scale)
	{
		return maxValue != 0 && value != 0 ? value * scale / maxValue : 0;
	}

	public void increaseTorpor(int amount, Entity shootingEntity)
	{
		torpor += amount;

		if (torpor >= maxTorpor) // DONE - all stats are randomly increased, so
									// fair chance for high level creatures to
									// have high torpor
		{
			if (shootingEntity instanceof EntityPlayer) this.tamer = shootingEntity.getUniqueID();
			this.unconscious = true;
			this.torpor = maxTorpor;
			this.tamingProgress = 0;
		}
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();

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

		if (!worldObj.isRemote)
		{
			this.dataWatcher.updateObject(DATA_WATCHER_UNCONSCIOUS, (byte) (unconscious ? 1 : 0));
			this.dataWatcher.updateObject(DATA_WATCHER_LEVEL, level);
			this.dataWatcher.updateObject(DATA_WATCHER_TAMING_PROGRESS, tamingProgress);
			this.dataWatcher.updateObject(DATA_WATCHER_XP, xp);
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

		}
		else
		{
			level = dataWatcher.getWatchableObjectInt(DATA_WATCHER_LEVEL);
			tamingProgress = dataWatcher.getWatchableObjectInt(DATA_WATCHER_TAMING_PROGRESS);
			unconscious = dataWatcher.getWatchableObjectByte(DATA_WATCHER_UNCONSCIOUS) == 1 ? true : false;
			xp = dataWatcher.getWatchableObjectInt(DATA_WATCHER_XP);
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
		this.setSize(scaleByAge(creature.getBabySizeXZ(), creature.getAdultSizeXZ()),
				scaleByAge(creature.getBabySizeY(), creature.getAdultSizeY()));
		if (ticksExisted >= creature.getGrowthTime()) grownUp = true;
	}

	public float scaleByAge(float baby, float adult)
	{
		int growthTime = ticksExisted > creature.getGrowthTime() ? creature.getGrowthTime() : ticksExisted;
		return (adult - baby) / creature.getGrowthTime() * growthTime + baby;
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
		getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth);
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(maxSpeed);
		getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(maxDamage);
		ItemStack[] inventoryOld = inventory;
		inventory = new ItemStack[maxWeight / WEIGHT_PER_STACK];
		for (int i = 0; i < inventoryOld.length; i++)
		{
			inventory[i] = inventoryOld[i];
		}
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
		this.baseLevel = nbt.getInteger("baseLevel");
		this.maxOxygen = nbt.getInteger("maxOxygen");
		this.maxSpeed = nbt.getInteger("maxSpeed");
		this.maxStamina = nbt.getInteger("maxStamina");
		this.maxTorpor = nbt.getInteger("maxTorpor");
		this.maxWeight = nbt.getInteger("maxWeight");

		this.food = nbt.getInteger("food");
		this.level = nbt.getInteger("level");
		this.oxygen = nbt.getInteger("oxygen");
		this.stamina = nbt.getInteger("stamina");
		this.torpor = nbt.getInteger("torpor");
		this.xp = nbt.getInteger("xp");
		inventory = new ItemStack[maxWeight];

		if (nbt.hasKey("owner")) owner = UUID.fromString(nbt.getString("owner"));

		if (nbt.hasKey("tamer"))
		{
			this.tamingProgress = nbt.getInteger("tamingProgress");
			this.unconscious = nbt.getBoolean("unconscious");
			this.tamer = UUID.fromString(nbt.getString("tamer"));
		}

		this.grownUp = nbt.getBoolean("grownUp");

		this.saddle = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("saddle"));
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

	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);

		nbt.setInteger("maxDamage", maxDamage);
		nbt.setInteger("maxFood", maxFood);
		nbt.setInteger("maxHealth", maxHealth);
		nbt.setInteger("baseLevel", baseLevel);
		nbt.setInteger("maxOxygen", maxOxygen);
		nbt.setInteger("maxSpeed", maxSpeed);
		nbt.setInteger("maxStamina", maxStamina);
		nbt.setInteger("maxTorpor", maxTorpor);
		nbt.setInteger("maxWeight", maxWeight);

		nbt.setInteger("food", food);
		nbt.setInteger("level", level);
		nbt.setInteger("oxygen", oxygen);
		nbt.setInteger("stamina", stamina);
		nbt.setInteger("torpor", torpor);
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
		return maxWeight;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return index < inventory.length ? inventory[index] : saddle;
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
		return index > inventory.length ? stack != null && stack
				.getItem() instanceof ItemDinosaurSaddle && ((ItemDinosaurSaddle) stack.getItem()).getSaddleType()
						.equals(creature.getSaddleType()) : true;
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
		saddle = null;
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

	public int getTamingProgress()
	{
		return tamingProgress;
	}

	public int getTamingProgressRequired()
	{
		// TODO Auto-generated method stub
		return (getBaseLevel() * 20);
	}

	public double getRelativeTamingProgress()
	{
		return (double) tamingProgress / (double) getTamingProgressRequired();
	}

	public int getTorpor()
	{
		return torpor;
	}

	public int getMaxTorpor()
	{
		return maxTorpor;
	}

	public double getRelativeTorpor()
	{
		return (double) torpor / (double) maxTorpor;
	}

	public double getRelativeOxygen()
	{
		return (double) oxygen / (double) maxOxygen;
	}

	public double getRelativeHealth()
	{
		return (double) this.getHealth() / (double) maxHealth;
	}

	public double getRelativeFood()
	{
		return (double) food / (double) maxFood;
	}

	public double getRelativeStamina()
	{
		return (double) stamina / (double) maxStamina;
	}

	public double getRelativeWeight()
	{
		return (double) countStacks() / (double) maxWeight;
	}

	public int countStacks()
	{
		int out = 0;
		for (ItemStack s : inventory)
		{
			if (s != null)
			{
				out++;
			}
		}
		return out;
	}

	public int getBaseLevel()
	{
		return baseLevel;
	}

	public int getMaxOxygen()
	{
		return maxOxygen;
	}

	public int getMaxDamage()
	{
		return maxDamage;
	}

	public int getMaxFood()
	{
		return maxFood;
	}

	public int getMaxSpeed()
	{
		return maxSpeed;
	}

	public int getMaxStamina()
	{
		return maxStamina;
	}

	public int getMaxWeight()
	{
		return maxWeight;
	}

	public int getOxygen()
	{
		return oxygen;
	}

	public int getFood()
	{
		return food;
	}

	public int getStamina()
	{
		return stamina;
	}

	public int getLevel()
	{
		return level;
	}

	public void increaseStat(int statIndex)
	{
		switch (statIndex)
		{
			case HEALTH:
				maxHealth += creature.getTamedHealthIncrease();
				break;
			case DAMAGE:
				maxDamage += creature.getTamedDamageIncrease();
				break;
			case FOOD:
				maxFood += creature.getTamedFoodIncrease();
				break;
			case OXYGEN:
				maxOxygen += creature.getTamedOxygenIncrease();
				break;
			case SPEED:
				maxSpeed += creature.getTamedSpeedIncrease();
				break;
			case STAMINA:
				maxStamina += creature.getTamedStaminaIncrease();
				break;
			case WEIGHT:
				maxWeight += creature.getTamedWeightIncrease();
				break;
		}
		updateEntityAttributes();
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable)
	{
		EntityARKCreature child = null;
		try
		{
			Class<? extends EntityARKCreature> cl = this.getClass();
			Constructor<? extends EntityARKCreature> co = cl.getConstructor(World.class);
			child = co.newInstance(this.worldObj);
		}
		catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			e.printStackTrace();
		}
		child.grownUp = false;
		child.updateHitbox();
		return child;
	}

	public Collection<Item> getFoodOptions()
	{
		return Collections.emptySet();
	}

	public void addFoodOption(Item food)
	{
	}
}
