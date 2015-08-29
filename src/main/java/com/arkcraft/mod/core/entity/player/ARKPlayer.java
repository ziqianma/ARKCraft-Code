package com.arkcraft.mod.core.entity.player;

import com.arkcraft.mod.core.lib.LogHelper;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
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
	
	// The extended player properties (anything below should be initialized in constructor and in NBT):
	private boolean isPooping; // Probably isn't need, but added as an example

	public ARKPlayer(EntityPlayer player, World world) {
		// Initialize some stuff
		this.player = player;
		this.setPooping(false);
	}

	/**
	 * Registers Dragon properties to player
	 * 
	 * @param player
	 */
	public static final void register(EntityPlayer player, World world) {
		player.registerExtendedProperties(ARKPlayer.EXT_PROP_NAME, new ARKPlayer(player, world));
	}

	/**
	 * 
	 * @param player
	 * @return DragonPlayer property of player
	 */
	public static final ARKPlayer get(EntityPlayer player) {
		return (ARKPlayer) player.getExtendedProperties(EXT_PROP_NAME);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = new NBTTagCompound();
		// ARK player properties
		properties.setBoolean("isPooping", isPooping());
		LogHelper.info("ARKPlayer saveNBTData: Player is " + (isPooping ? "" : "not") + " pooping.");
		compound.setTag(EXT_PROP_NAME, properties);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		if (properties == null)
			return;
		// ARK player properties 
		this.setPooping(properties.getBoolean("isPooping"));
		LogHelper.info("ARKPlayer loadNBTData: Player is " + (isPooping ? "" : "not") + " pooping.");
	}

	/**
	 * Copies additional player data from the given ExtendedPlayer instance
	 * Avoids NBT disk I/O overhead when cloning a player after respawn
	 */
	public void copy(ARKPlayer props) {
		this.isPooping = props.isPooping;
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

	public boolean isPooping() {
		return isPooping;
	}

	public void setPooping(boolean isPooping) {
		this.isPooping = isPooping;
	}
}
