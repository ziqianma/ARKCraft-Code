package com.arkcraft.module.core.common.entity.data;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.common.network.PlayerPoop;
import com.arkcraft.module.core.common.network.SyncPlayerData;
import com.arkcraft.module.crafting.common.config.ModuleItemBalance;
import com.arkcraft.module.crafting.common.handlers.PlayerCraftingManager;
import com.arkcraft.module.crafting.common.inventory.InventoryBlueprints;
import com.arkcraft.module.crafting.common.inventory.InventoryPlayerCrafting;

/**
 * @author wildbill22, Lewis_McReu
 */
public class ARKPlayer implements IExtendedEntityProperties
{
	// TODO
	private static final int healthIncrease = 10, staminaIncrease = 10,
			oxygenIncrease = 20, foodIncrease = 10, waterIncrease = 10,
			damageIncrease = 5, speedIncrease = 2, maxTorpor = 200,
			maxLevel = 94;

	public static final String EXT_PROP_NAME = "ARKPlayer";
	private final EntityPlayer player;

	// The extended player properties (anything below should be initialized in
	// constructor and in NBT):
	private boolean canPoop; // True if player can poop (timer sets this)
	// actual stats
	private int health, oxygen, food, water, damage, speed, stamina, torpor,
			xp, level;
	// max stats
	private int maxHealth, maxOxygen, maxFood, maxWater, maxDamage, maxSpeed,
			maxStamina;

	public ARKPlayer(EntityPlayer player, World world)
	{
		// Initialize some stuff
		this.player = player;
		this.setCanPoop(false);
		this.water = 20;
		this.torpor = 0;
		this.stamina = 20;
	}

	/**
	 * Registers properties to player
	 *
	 * @param player
	 */
	public static final void register(EntityPlayer player, World world)
	{
		player.registerExtendedProperties(ARKPlayer.EXT_PROP_NAME,
				new ARKPlayer(player, world));
	}

	/**
	 * @param player
	 * @return properties of player
	 */
	public static final ARKPlayer get(EntityPlayer player)
	{
		return (ARKPlayer) player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound properties = new NBTTagCompound();
		// ARK player properties
		properties.setBoolean("canPoop", canPoop);
		properties.setInteger("health", health);
		properties.setInteger("oxygen", oxygen);
		properties.setInteger("food", food);
		properties.setInteger("water", water);
		properties.setInteger("damage", damage);
		properties.setInteger("speed", speed);
		properties.setInteger("stamina", stamina);
		properties.setInteger("torpor", torpor);
		properties.setInteger("xp", xp);
		properties.setInteger("level", level);

		properties.setInteger("maxHealth", maxHealth);
		properties.setInteger("maxOxygen", maxOxygen);
		properties.setInteger("maxFood", maxFood);
		properties.setInteger("maxWater", maxWater);
		properties.setInteger("maxDamage", maxDamage);
		properties.setInteger("maxSpeed", maxSpeed);
		properties.setInteger("maxStamina", maxStamina);

		compound.setTag(EXT_PROP_NAME, properties);
		inventoryPlayerCrafting.saveInventoryToNBT(compound);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound properties = compound.getCompoundTag(EXT_PROP_NAME);
		if (properties == null) { return; }
		// ARK player properties
		canPoop = properties.getBoolean("canPoop");
		water = properties.getInteger("water");
		torpor = properties.getInteger("torpor");
		stamina = properties.getInteger("stamina");

		inventoryPlayerCrafting.loadInventoryFromNBT(compound);
	}

	public void setWater(int water)
	{
		this.water = water;
		syncClient(player, false);
	}

	public void setTorpor(int torpor)
	{
		this.torpor = torpor;
		syncClient(player, false);
	}

	public void setStamina(int stamina)
	{
		this.stamina = stamina;
		syncClient(player, false);
	}

	public int getWater()
	{
		return water;
	}

	public int getTorpor()
	{
		return torpor;
	}

	public int getStamina()
	{
		return stamina;
	}

	/**
	 * Copies additional player data from the given ExtendedPlayer instance
	 * Avoids NBT disk I/O overhead when cloning a player after respawn
	 */
	public void copy(ARKPlayer props)
	{
		this.canPoop = props.canPoop;
		this.torpor = props.torpor;
		this.water = props.water;
		this.stamina = props.stamina;
	}

	@Override
	public void init(Entity entity, World world)
	{
	}

	public void syncClient(EntityPlayer player, boolean all)
	{
		if (player instanceof EntityPlayerMP)
		{
			ARKCraft.modChannel.sendTo(new SyncPlayerData(all, this),
					(EntityPlayerMP) player);
		}
	}

	@SuppressWarnings("unused")
	private EntityPlayer getPlayer()
	{
		return player;
	}

	// --------- Pooping -----------------
	public boolean canPoop()
	{
		return canPoop;
	}

	public void setCanPoop(boolean canPoop)
	{
		this.canPoop = canPoop;
	}

	public void poop()
	{
		if (canPoop())
		{
			if (player.worldObj.isRemote)
			{
				player.playSound(
						ARKCraft.MODID + ":" + "dodo_defficating",
						1.0F,
						(player.worldObj.rand.nextFloat() - player.worldObj.rand
								.nextFloat()) * 0.2F + 1.0F);
				ARKCraft.modChannel.sendToServer(new PlayerPoop(true));
				LogHelper.info("Player is pooping!");
			}
			setCanPoop(false);
		}
		else
		{
			player.addChatMessage(new ChatComponentTranslation(
					"chat.canNotPoop"));
		}
	}

	// ----------------- End of Properties stuff, rest is for crafting
	// -----------------

	// Inventory for Crafting
	private InventoryPlayerCrafting inventoryPlayerCrafting = new InventoryPlayerCrafting(
			"Crafting", false, INVENTORY_SLOTS_COUNT);
	private InventoryBlueprints inventoryBlueprints = new InventoryBlueprints(
			"Blueprints", false, BLUEPRINT_SLOTS_COUNT,
			PlayerCraftingManager.getInstance(), inventoryPlayerCrafting,
			(short) ModuleItemBalance.PLAYER_CRAFTING.CRAFT_TIME_FOR_ITEM);

	// Constants for the inventory
	public static final int BLUEPRINT_SLOTS_COUNT = 20;
	public static final int FIRST_BLUEPRINT_SLOT = 0;
	public static final int INVENTORY_SLOTS_COUNT = 10;
	public static final int FIRST_INVENTORY_SLOT = 0;
	public static final int LAST_INVENTORY_SLOT = INVENTORY_SLOTS_COUNT - 1;

	public InventoryBlueprints getInventoryBlueprints()
	{
		return inventoryBlueprints;
	}

	public void setInventoryBlueprints(InventoryBlueprints inventoryBlueprints)
	{
		this.inventoryBlueprints = inventoryBlueprints;
	}

	public InventoryPlayerCrafting getInventoryPlayer()
	{
		return inventoryPlayerCrafting;
	}

	public void setInventoryPlayer(InventoryPlayerCrafting inventoryPlayer)
	{
		this.inventoryPlayerCrafting = inventoryPlayer;
	}

	public void addXP(int killXP)
	{
		// TODO Auto-generated method stub

	}
}
