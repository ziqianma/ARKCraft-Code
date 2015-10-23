package com.arkcraft.mod.common.entity.player;

import com.arkcraft.mod.client.gui.InventoryBlueprints;
import com.arkcraft.mod.client.gui.InventoryPlayerCrafting;
import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.handlers.PlayerCraftingManager;
import com.arkcraft.mod.common.lib.LogHelper;
import com.arkcraft.mod.common.network.PlayerPoop;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/***
 * 
 * @author wildbill22
 *
 */
public class ARKPlayer implements IExtendedEntityProperties {
	public static final String EXT_PROP_NAME = "ARKPlayer";
	private final EntityPlayer player;
	private InventoryBlueprints inventoryBlueprints = new InventoryBlueprints("Blueprints", false, BLUEPRINT_SLOTS_COUNT);
	private InventoryPlayerCrafting inventoryPlayerCrafting = new InventoryPlayerCrafting("Crafting", false, INVENTORY_SLOTS_COUNT);
	
	// The extended player properties (anything below should be initialized in constructor and in NBT):
	private boolean canPoop;         // True if player can poop (timer sets this)

	public ARKPlayer(EntityPlayer player, World world) {
		// Initialize some stuff
		this.player = player;
		this.setCanPoop(false);
		inventoryBlueprints.setNumBlueprints(PlayerCraftingManager.getInstance().getNumRecipes());
	}

	/**
	 * Registers properties to player
	 * 
	 * @param player
	 */
	public static final void register(EntityPlayer player, World world) {
		player.registerExtendedProperties(ARKPlayer.EXT_PROP_NAME, new ARKPlayer(player, world));
	}

	/**
	 * 
	 * @param player
	 * @return properties of player
	 */
	public static final ARKPlayer get(EntityPlayer player) {
		return (ARKPlayer) player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();
		// ARK player properties
		properties.setBoolean("canPoop", canPoop());
		LogHelper.info("ARKPlayer saveNBTData: Player can " + (canPoop ? "" : "not") + " poop.");
		compound.setTag(EXT_PROP_NAME, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		if (properties == null)
			return;
		// ARK player properties 
		this.setCanPoop(properties.getBoolean("canPoop"));
		LogHelper.info("ARKPlayer loadNBTData: Player can " + (canPoop ? "" : "not") + " poop.");
	}

	/**
	 * Copies additional player data from the given ExtendedPlayer instance
	 * Avoids NBT disk I/O overhead when cloning a player after respawn
	 */
	public void copy(ARKPlayer props) {
		this.canPoop = props.canPoop;
	}
	
	@Override
	public void init(Entity entity, World world) {
	}

	public void syncClient(boolean all) {
		// Add network stuff here to sync client to server
	}

	@SuppressWarnings("unused")
	private EntityPlayer getPlayer() {
		return player;
	}

	public boolean canPoop() {
		return canPoop;
	}

	public void setCanPoop(boolean canPoop) {
		this.canPoop = canPoop;
	}

	public void Poop() {
		if (canPoop()) {
			if (player.worldObj.isRemote) {
				player.playSound(ARKCraft.MODID + ":" + "dodo_defficating", 1.0F, (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.2F + 1.0F);
				ARKCraft.modChannel.sendToServer(new PlayerPoop(true));
				LogHelper.info("Player is pooping!");
			}
			setCanPoop(false);
		}
		else {
			player.addChatMessage(new ChatComponentTranslation("chat.canNotPoop"));	
		}		
	}

	//----------------- End of Properties stuff, rest is for crafting -----------------
	
	// Constants for the inventory
	public static final int INVENTORY_SLOTS_COUNT = 10;
	public static final int TOTAL_SLOTS_COUNT = INVENTORY_SLOTS_COUNT;
	public static final int FIRST_INVENTORY_SLOT = 0;
	public static final int LAST_INVENTORY_SLOT = INVENTORY_SLOTS_COUNT - 1; 
	public static final int BLUEPRINT_SLOTS_COUNT = 20;
	public static final int FIRST_BLUEPRINT_SLOT = 10;
	
	public InventoryBlueprints getInventoryBlueprints() {
		return inventoryBlueprints;
	}

	public void setInventoryBlueprints(InventoryBlueprints inventoryBlueprints) {
		this.inventoryBlueprints = inventoryBlueprints;
	}

	public InventoryPlayerCrafting getInventoryPlayer() {
		return inventoryPlayerCrafting;
	}

	public void setInventoryPlayer(InventoryPlayerCrafting inventoryPlayer) {
		this.inventoryPlayerCrafting = inventoryPlayer;
	}
}
