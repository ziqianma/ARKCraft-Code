package com.arkcraft.module.item.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.item.common.items.ARKCraftItems;
import com.arkcraft.module.item.common.items.weapons.ranged.ItemRangedWeapon;

public class TileInventoryAttachment extends AbstractInventory
{
	private String name = "Attachment Inventory";

	/** The key used to store and retrieve the inventory from NBT */
	private static final String SAVE_KEY = "AttachmentInventory";
	public static final int INV_SIZE = 10;// TODO

	/** Provides NBT Tag Compound to reference */
	private final ItemStack invStack;
	public boolean activate_scoping;
	public boolean activate_flashlight;

	public TileInventoryAttachment(ItemStack stack)
	{
		inventory = new ItemStack[INV_SIZE];
		this.invStack = stack;
		if (invStack != null && !invStack.hasTagCompound())
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

	@Override
	public void markDirty()
	{
		super.markDirty();
		for (int i = 0; i < getSizeInventory(); ++i)
		{
			if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0) inventory[i] = null;
		}

		if (isScopePresent())
		{
			activate_scoping = true;
		}
		else if (isFlashPresent())
		{
			activate_flashlight = true;
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

	public boolean isFlashPresent()
	{
		for (ItemStack stack : inventory)
		{
			if (stack != null && stack.getItem() == ARKCraftItems.flash_light) return true;
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
	protected String getNbtKey()
	{
		return SAVE_KEY;
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return !(stack.getItem() instanceof ItemRangedWeapon);
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return inventory[slot];
	}

}