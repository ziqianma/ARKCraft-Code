package com.arkcraft.module.item.common.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.EnumSkyBlock;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.item.common.handlers.ForgeHandler;
import com.arkcraft.module.item.common.handlers.ForgeRecipe;

public class TileInventoryForge extends TileEntity implements IForge
{
	private ItemStack[] itemStacks = new ItemStack[getSlotCount()];

	/** the currently active recipes */
	private Map<ForgeRecipe, Integer> activeRecipes = new HashMap<ForgeRecipe, Integer>();
	/** the ticks burning left */
	private int burningTicks;

	public TileInventoryForge()
	{
		super();
	}

	/**
	 * return the remaining burn time of the fuel
	 * 
	 * @return seconds remaining
	 */
	public int secondsOfFuelRemaining()
	{
		return burningTicks / 20; // 20 ticks per second
	}

	// This method is called every tick to update the tile entity, i.e.
	// - see if the fuel has run out, and if so turn the furnace "off" and
	// slowly uncook the current item (if any)
	// - see if any of the items have finished smelting
	// It runs both on the server and the client.
	@Override
	public void update()
	{
		List<ForgeRecipe> possibleRecipes = ForgeHandler.findPossibleRecipes(this);
		updateBurning(possibleRecipes);
		//LogHelper.info(burningTicks);
		if (this.isBurning() && possibleRecipes.size() > 0)
		{
			Iterator<Entry<ForgeRecipe, Integer>> it = activeRecipes.entrySet().iterator();
			while (it.hasNext())
			{
				Entry<ForgeRecipe, Integer> e = it.next();
				if (!possibleRecipes.contains(e.getKey()))
				{
					it.remove();
				}
			}
			for (ForgeRecipe r : possibleRecipes)
			{
				activeRecipes.putIfAbsent(r, 0);
			}

			updateCookTimes();

			updateInventory();

			markDirty();
		}
		else clearActiveRecipes();
		if (worldObj.isRemote)
		{
			worldObj.markBlockForUpdate(pos);
		}
		worldObj.checkLightFor(EnumSkyBlock.BLOCK, pos);
	}

	private void updateBurning(List<ForgeRecipe> possibleRecipes)
	{
		if (burningTicks < 1)
		{
			for (int i = 0; i < itemStacks.length; i++)
			{
				ItemStack stack = itemStacks[i];
				if (stack != null && ForgeHandler.isValidFuel(stack.getItem()) && possibleRecipes
						.size() > 0)
				{
					if (!worldObj.isRemote)
					{
						stack.stackSize--;
						if (stack.stackSize == 0) itemStacks[i] = null;
					}
					this.burningTicks += ForgeHandler.getBurnTime(stack.getItem());
					break;
				}
			}
		}
		if (burningTicks > 0) this.burningTicks--;

	}

	private void updateInventory()
	{
		Iterator<Entry<ForgeRecipe, Integer>> it = activeRecipes.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<ForgeRecipe, Integer> e = it.next();
			if (e.getValue() >= (e.getKey().getBurnTime() * this.getBurnFactor() * 20))
			{
				it.remove();
				ForgeRecipe r = e.getKey();
				List<Item> input = new ArrayList<Item>(r.getInput());
				Item output = r.getOutput();
				int outputStack = -1;
				for (int i = 0; i < itemStacks.length; i++)
				{
					if (itemStacks[i] != null && itemStacks[i].getItem().equals(output) && itemStacks[i].stackSize < this
							.getInventoryStackLimit())
					{
						outputStack = i;
						break;
					}
				}
				if (outputStack == -1)
				{
					for (int i = 0; i < itemStacks.length; i++)
					{
						if (itemStacks[i] == null)
						{
							outputStack = i;
							break;
						}
					}
				}
				if (outputStack != -1)
				{
					for (int i = 0; i < this.itemStacks.length; i++)
					{
						ItemStack stack = itemStacks[i];
						if (stack != null)
						{
							while (input.remove(stack.getItem()) && stack.stackSize > 0)
							{
								stack.stackSize--;
							}
							if (stack.stackSize == 0) itemStacks[i] = null;
						}
					}
					if (itemStacks[outputStack] == null)
					{
						ItemStack s = new ItemStack(output);
						itemStacks[outputStack] = s;
					}
					else
					{
						itemStacks[outputStack].stackSize++;
					}
				}
			}
		}
	}

	private void updateCookTimes()
	{
		Iterator<Entry<ForgeRecipe, Integer>> it = activeRecipes.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<ForgeRecipe, Integer> e = it.next();
			activeRecipes.put(e.getKey(), e.getValue() + 1);
		}
	}

	// Gets the number of slots in the inventory
	@Override
	public int getSizeInventory()
	{
		return itemStacks.length;
	}

	// Gets the stack in the given slot
	@Override
	public ItemStack getStackInSlot(int i)
	{
		return itemStacks[i];
	}

	/**
	 * Removes some of the units from itemstack in the given slot, and returns
	 * as a separate itemstack
	 * 
	 * @param slotIndex
	 *            the slot number to remove the items from
	 * @param count
	 *            the number of units to remove
	 * @return a new itemstack containing the units removed from the slot
	 */
	@Override
	public ItemStack decrStackSize(int slotIndex, int count)
	{
		ItemStack itemStackInSlot = getStackInSlot(slotIndex);
		if (itemStackInSlot == null) return null;

		ItemStack itemStackRemoved;
		if (itemStackInSlot.stackSize <= count)
		{
			itemStackRemoved = itemStackInSlot;
			setInventorySlotContents(slotIndex, null);
		}
		else
		{
			itemStackRemoved = itemStackInSlot.splitStack(count);
			if (itemStackInSlot.stackSize == 0)
			{
				setInventorySlotContents(slotIndex, null);
			}
		}
		markDirty();
		return itemStackRemoved;
	}

	// overwrites the stack in the given slotIndex with the given stack
	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack itemstack)
	{
		itemStacks[slotIndex] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
		{
			itemstack.stackSize = getInventoryStackLimit();
		}
		markDirty();
	}

	// This is the maximum number if items allowed in each slot
	// This only affects things such as hoppers trying to insert items you need
	// to use the container to enforce this for players
	// inserting items via the gui
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	// Return true if the given player is able to use this block. In this case
	// it checks that
	// 1) the world tileentity hasn't been replaced in the meantime, and
	// 2) the player isn't too far away from the centre of the block
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if (this.worldObj.getTileEntity(this.pos) != this) return false;
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET,
				pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}

	// ------------------------------

	// This is where you save any data that you don't want to lose when the tile
	// entity unloads
	// In this case, it saves the state of the furnace (burn time etc) and the
	// itemstacks stored in the fuel, input, and output slots
	@Override
	public void writeToNBT(NBTTagCompound parentNBT)
	{
		super.writeToNBT(parentNBT); // The super call is required to
										// save and load the tiles
										// location

		// // Save the stored item stacks

		// to use an analogy with Java, this code generates an array of hashmaps
		// The itemStack in each slot is converted to an NBTTagCompound, which
		// is effectively a hashmap of key->value pairs such
		// as slot=1, id=2353, count=1, etc
		// Each of these NBTTagCompound are then inserted into NBTTagList, which
		// is similar to an array.
		NBTTagList dataForAllSlots = new NBTTagList();
		for (int i = 0; i < this.itemStacks.length; ++i)
		{
			if (this.itemStacks[i] != null)
			{
				NBTTagCompound dataForThisSlot = new NBTTagCompound();
				dataForThisSlot.setByte("Slot", (byte) i);
				this.itemStacks[i].writeToNBT(dataForThisSlot);
				dataForAllSlots.appendTag(dataForThisSlot);
			}
		}
		// the array of hashmaps is then inserted into the parent hashmap for
		// the container
		parentNBT.setTag("Items", dataForAllSlots);

		// Save everything else
		parentNBT.setInteger("burnTime", this.burningTicks);
		NBTTagList nbtList = new NBTTagList();
		for (Entry<ForgeRecipe, Integer> e : activeRecipes.entrySet())
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger("cookTime", e.getValue());
			nbt.setString("recipeKey", e.getKey().toString());
			nbtList.appendTag(nbt);
		}
		parentNBT.setTag("activeRecipes", nbtList);
	}

	// This is where you load the data that you saved in writeToNBT
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt); // The super call is required to save
								// and load the tiles location
		final byte NBT_TYPE_COMPOUND = 10; // See NBTBase.createNewByType() for
											// a listing
		NBTTagList dataForAllSlots = nbt.getTagList("Items", NBT_TYPE_COMPOUND);

		Arrays.fill(itemStacks, null); // set all slots to empty
		for (int i = 0; i < dataForAllSlots.tagCount(); ++i)
		{
			NBTTagCompound dataForOneSlot = dataForAllSlots.getCompoundTagAt(i);
			byte slotNumber = dataForOneSlot.getByte("Slot");
			if (slotNumber >= 0 && slotNumber < this.itemStacks.length)
			{
				this.itemStacks[slotNumber] = ItemStack.loadItemStackFromNBT(dataForOneSlot);
			}
		}

		// Load everything else. Trim the arrays (or pad with 0) to make sure
		// they have the correct number of elements
		this.burningTicks = nbt.getInteger("burnTime");
		NBTTagList nbtList = nbt.getTagList("activeRecipes", NBT_TYPE_COMPOUND);
		for (int i = 0; i < nbtList.tagCount(); i++)
		{
			NBTTagCompound nbtR = nbtList.getCompoundTagAt(i);
			int cookTime = nbtR.getInteger("cookTime");
			ForgeRecipe r = ForgeHandler.getForgeRecipe(nbtR.getString("recipeKey"));
			this.activeRecipes.put(r, cookTime);
		}
	}

	public void clearActiveRecipes()
	{
		this.activeRecipes = new HashMap<ForgeRecipe, Integer>();
	}

	// When the world loads from disk, the server needs to send the TileEntity
	// information to the client
	// it uses getDescriptionPacket() and onDataPacket() to do this
	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		return new S35PacketUpdateTileEntity(this.pos, 0, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		readFromNBT(pkt.getNbtCompound());
	}

	// ------------------------

	// set all slots to empty
	@Override
	public void clear()
	{
		Arrays.fill(itemStacks, null);
	}

	// will add a key for this container to the lang file so we can name it in
	// the GUI
	@Override
	public String getName()
	{
		return "container.inventory_refining_forge.name";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	// standard code to look up what the human-readable name is
	@Override
	public IChatComponent getDisplayName()
	{
		return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(
				this.getName());
	}

	// Fields are used to send non-inventory information from the server to
	// interested clients
	// The container code caches the fields and sends the client any fields
	// which have changed.
	// The field ID is limited to byte, and the field value is limited to short.
	// (if you use more than this, they get cast down
	// in the network packets)
	// If you need more than this, or shorts are too small, use a custom packet
	// in your container instead.

	// private static final byte COOK_FIELD_ID = 0;
	// private static final byte FIRST_BURN_TIME_REMAINING_FIELD_ID = 1;
	// private static final byte FIRST_BURN_TIME_INITIAL_FIELD_ID =
	// FIRST_BURN_TIME_REMAINING_FIELD_ID + (byte) FUEL_SLOTS_COUNT;
	// private static final byte NUMBER_OF_FIELDS =
	// FIRST_BURN_TIME_INITIAL_FIELD_ID + (byte) FUEL_SLOTS_COUNT;

	@Override
	public int getField(int id)
	{
		// if (id == COOK_FIELD_ID) return cookTime;
		// if (id >= FIRST_BURN_TIME_REMAINING_FIELD_ID && id <
		// FIRST_BURN_TIME_REMAINING_FIELD_ID + FUEL_SLOTS_COUNT) { return
		// burnTimeRemaining[id - FIRST_BURN_TIME_REMAINING_FIELD_ID]; }
		// if (id >= FIRST_BURN_TIME_INITIAL_FIELD_ID && id <
		// FIRST_BURN_TIME_INITIAL_FIELD_ID + FUEL_SLOTS_COUNT) { return
		// burnTimeInitialValue[id - FIRST_BURN_TIME_INITIAL_FIELD_ID]; }
		// System.err.println("Invalid field ID in TileInventorySmelting.getField:"
		// + id);
		return 0;
	}

	@Override
	public void setField(int id, int value)
	{
		// if (id == COOK_FIELD_ID)
		// {
		// cookTime = (short) value;
		// }
		// else if (id >= FIRST_BURN_TIME_REMAINING_FIELD_ID && id <
		// FIRST_BURN_TIME_REMAINING_FIELD_ID + FUEL_SLOTS_COUNT)
		// {
		// burnTimeRemaining[id - FIRST_BURN_TIME_REMAINING_FIELD_ID] = value;
		// }
		// else if (id >= FIRST_BURN_TIME_INITIAL_FIELD_ID && id <
		// FIRST_BURN_TIME_INITIAL_FIELD_ID + FUEL_SLOTS_COUNT)
		// {
		// burnTimeInitialValue[id - FIRST_BURN_TIME_INITIAL_FIELD_ID] = value;
		// }
		// else
		// {
		// System.err.println("Invalid field ID in TileInventorySmelting.setField:"
		// + id);
		// }
	}

	@Override
	public int getFieldCount()
	{
		return 0;
		// return NUMBER_OF_FIELDS;
	}

	// -----------------------------------------------------------------------------------------------------------
	// The following methods are not needed for this example but are part of
	// IInventory so they must be implemented

	// Unused unless your container specifically uses it.
	// Return true if the given stack is allowed to go in the given slot
	@Override
	public boolean isItemValidForSlot(int slotIndex, ItemStack itemstack)
	{
		return true;
	}

	/**
	 * This method removes the entire contents of the given slot and returns it.
	 * Used by containers such as crafting tables which return any items in
	 * their slots when you close the GUI
	 * 
	 * @param slotIndex
	 * @return
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int slotIndex)
	{
		ItemStack itemStack = getStackInSlot(slotIndex);
		if (itemStack != null) setInventorySlotContents(slotIndex, null);
		return itemStack;
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
	public int getSlotCount()
	{
		return 8;
	}

	@Override
	public boolean isBurning()
	{
		return this.burningTicks > 0;
	}

	@Override
	public double getBurnFactor()
	{
		return 20d;
	}

}