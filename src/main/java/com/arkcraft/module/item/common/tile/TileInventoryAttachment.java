package com.arkcraft.module.item.common.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.arkcraft.module.item.common.items.weapons.attachments.AttachmentType;
import com.arkcraft.module.item.common.items.weapons.attachments.ItemAttachment;
import com.arkcraft.module.item.common.items.weapons.ranged.ItemRangedWeapon;
import com.arkcraft.module.item.common.items.weapons.ranged.supporting.Flashable;
import com.arkcraft.module.item.common.items.weapons.ranged.supporting.HoloScopeable;
import com.arkcraft.module.item.common.items.weapons.ranged.supporting.Laserable;
import com.arkcraft.module.item.common.items.weapons.ranged.supporting.NonSupporting;
import com.arkcraft.module.item.common.items.weapons.ranged.supporting.Scopeable;
import com.arkcraft.module.item.common.items.weapons.ranged.supporting.Silenceable;

/**
 * @author BubbleTrouble
 * @author Lewis_McReu
 */
public class TileInventoryAttachment extends AbstractInventory
{
	private String name = "Attachment Inventory";

	/** The key used to store and retrieve the inventory from NBT */
	private static final String SAVE_KEY = "AttachmentInventory";
	public static final int INV_SIZE = 1;

	/** Provides NBT Tag Compound to reference */
	private final ItemStack invStack;

	public static TileInventoryAttachment create(ItemStack stack)
	{
		if (stack != null && stack.getItem() instanceof ItemRangedWeapon && !(stack.getItem() instanceof NonSupporting)) return new TileInventoryAttachment(
				stack);
		return null;
	}

	private TileInventoryAttachment(ItemStack stack)
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
		return 1;
	}

	@Override
	public void markDirty()
	{
		super.markDirty();
		for (int i = 0; i < getSizeInventory(); ++i)
		{
			if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0) inventory[i] = null;
		}
		writeToNBT(invStack.getTagCompound());
	}

	private boolean isInvOfType(AttachmentType type)
	{
		return inventory[0] != null && ((ItemAttachment) inventory[0].getItem()).getType().equals(
				type);
	}

	public boolean isScopePresent()
	{
		return isInvOfType(AttachmentType.SCOPE);
	}

	public boolean isFlashPresent()
	{
		return isInvOfType(AttachmentType.FLASH);
	}

	public boolean isLaserPresent()
	{
		return isInvOfType(AttachmentType.LASER);
	}

	public boolean isSilencerPresent()
	{
		return isInvOfType(AttachmentType.SILENCER);
	}

	public boolean isHoloScopePresent()
	{
		return isInvOfType(AttachmentType.HOLO_SCOPE);
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
		if (stack != null && stack.getItem() instanceof ItemAttachment)
		{
			ItemAttachment item = (ItemAttachment) stack.getItem();
			Item inv = invStack.getItem();
			switch (item.getType())
			{
				case SCOPE:
					return inv instanceof Scopeable;
				case HOLO_SCOPE:
					return inv instanceof HoloScopeable;
				case FLASH:
					return inv instanceof Flashable;
				case LASER:
					return inv instanceof Laserable;
				case SILENCER:
					return inv instanceof Silenceable;
			}
		}
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return inventory[slot];
	}

}