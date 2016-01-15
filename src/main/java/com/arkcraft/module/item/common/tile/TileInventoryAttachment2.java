package com.arkcraft.module.item.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.item.common.items.ARKCraftItems;
import com.arkcraft.module.item.common.items.weapons.guns.ItemRangedWeapon;

public class TileInventoryAttachment2 extends AbstractInventory
{
	private String name = "Attachment Inventory";

	/** The key used to store and retrieve the inventory from NBT */
	private static final String SAVE_KEY = "AttachmentInventory";
	public static final int INV_SIZE = 10;// TODO

	/** Provides NBT Tag Compound to reference */
	private final ItemStack invStack;
	public boolean activate_scoping;

	public TileInventoryAttachment2(ItemStack stack)
	{
		inventory = new ItemStack[INV_SIZE];
		this.invStack = stack;
		if (!invStack.hasTagCompound())
		{
			invStack.setTagCompound(new NBTTagCompound());
		}
		readFromNBT(invStack.getTagCompound());
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public boolean hasCustomName()
	{
		return name.length() > 0;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	/**
	 * For inventories stored in ItemStacks, it is critical to implement this
	 * method in order to write the inventory to the ItemStack's NBT whenever it
	 * changes.
	 */
	@Override
	public void markDirty()
	{

		/*
		 * else { activate_scoping = false; }
		 */

		for (int i = 0; i < getSizeInventory(); ++i)
		{
			if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0) inventory[i] = null;
		}

		if (isScopePresent())
		{
			activate_scoping = true;
		}

		writeToNBT(invStack.getTagCompound());
	}

	public boolean isScopePresent()
	{
		for (ItemStack stack : inventory)
		{
			if (stack != null && stack.getItem() == ARKCraftItems.scope) return true;
		}

		return false;
	}

	public String getModel()
	{
		String jsonPath = ARKCraft.MODID + ":%s";
		if (isScopePresent()) jsonPath = jsonPath + "_scoped";
		return jsonPath;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return player.getHeldItem() == invStack;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack)
	{
		return !(stack.getItem() instanceof ItemRangedWeapon);
	}

	@Override
	protected String getNbtKey()
	{
		return SAVE_KEY;
	}

	@Override
	public void writeToNBT(NBTTagCompound compound)
	{
		String key = getNbtKey();
		if (key == null || key.equals("")) { return; }
		NBTTagList items = new NBTTagList();
		for (int i = 0; i < getSizeInventory(); ++i)
		{
			if (getStackInSlot(i) != null)
			{
				NBTTagCompound item = new NBTTagCompound();
				item.setByte("Slot", (byte) i);
				getStackInSlot(i).writeToNBT(item);
				items.appendTag(item);
			}
		}
		compound.setTag(key, items);
	}

	/**
	 * Loads this inventory from NBT; must be called manually Fails silently if
	 * {@link #getNbtKey} returns null or an empty string
	 */
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		String key = getNbtKey();
		if (key == null || key.equals("")) { return; }
		NBTTagList items = compound.getTagList(key, compound.getId());
		for (int i = 0; i < items.tagCount(); ++i)
		{
			NBTTagCompound item = items.getCompoundTagAt(i);
			byte slot = item.getByte("Slot");
			if (slot >= 0 && slot < getSizeInventory())
			{
				inventory[slot] = ItemStack.loadItemStackFromNBT(item);
			}
		}
	}
}