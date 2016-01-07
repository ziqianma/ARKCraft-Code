package com.arkcraft.module.item.common.tile;

import com.arkcraft.module.item.common.items.ARKCraftItems;
import com.arkcraft.module.item.common.items.weapons.handlers.IItemWeapon;
import com.arkcraft.module.item.common.items.weapons.component.RangedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IChatComponent;

import java.util.Random;

public class TileInventoryAttachment implements IItemWeapon, IInventory
{
    private String name = "Inventory Item";

    /**
     * Provides NBT Tag Compound to reference
     */
    private final ItemStack invItem;

    /**
     * Defining your inventory size this way is handy
     */
    public static final int INV_SIZE = 8;

    /**
     * Inventory's size must be same as number of slots you add to the Container class
     */
    private ItemStack[] inventory = new ItemStack[INV_SIZE];

    /**
     * @param stack - the ItemStack to which this inventory belongs
     */
    public TileInventoryAttachment(ItemStack stack)
    {
        invItem = stack;

        // Create a new NBT Tag Compound if one doesn't already exist, or you will crash
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }
        // note that it's okay to use stack instead of invItem right there
        // both reference the same memory location, so whatever you change using
        // either reference will change in the other

        // Read the inventory contents from NBT
        readFromNBT(stack.getTagCompound());
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null)
        {
            if (stack.stackSize > amount)
            {
                stack = stack.splitStack(amount);
                // Don't forget this line or your inventory will not be saved!
                markDirty();
            }
            else
            {
                // this method also calls onInventoryChanged, so we don't need to call it again
                setInventorySlotContents(slot, null);
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
        ItemStack stack = getStackInSlot(slot);
        setInventorySlotContents(slot, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slotIndex, ItemStack itemstack)
    {
        inventory[slotIndex] = itemstack;
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit())
        {
            itemstack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    /**
     * This method doesn't seem to do what it claims to do, as
     * items can still be left-clicked and placed in the inventory
     * even when this returns false
     */
    /*
	public boolean allowScoping()
	{
		return false; 
	}	*/
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        if (itemstack.getItem() == ARKCraftItems.scope)
        {
            return ifCanScope() == true;
            //	LogHelper("Itemis Vailid");

        }
        // Don't want to be able to store the inventory item within itself
        // Bad things will happen, like losing your inventory
        // Actually, this needs a custom Slot to work
        else
        {
            return !(itemstack.getItem() == ARKCraftItems.longneck_rifle);
        }
    }


    @Override
    public boolean ifCanScope()
    {
        return false;
    }

    /**
     * A custom method to read our inventory from an ItemStack's NBT compound
     */
    public void readFromNBT(NBTTagCompound compound)
    {
        // Gets the custom taglist we wrote to this compound, if any
        // 1.7.2+ change to compound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);
        final byte NBT_TYPE_COMPOUND = 10;       // See NBTBase.createNewByType() for a listing
        NBTTagList items = compound.getTagList("Items", NBT_TYPE_COMPOUND);
        //	NBTTagList items = NBTTagCompound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);
        //	NBTTagList dataForAllSlots = nbtTagCompound.getTagList("Items", NBT_TYPE_COMPOUND);

        for (int i = 0; i < items.tagCount(); ++i)
        {
            // 1.7.2+ change to items.getCompoundTagAt(i)
            NBTTagCompound item = (NBTTagCompound) items.getCompoundTagAt(i);
            int slot = item.getInteger("Slot");

            // Just double-checking that the saved slot index is within our inventory array bounds
            if (slot >= 0 && slot < getSizeInventory())
            {
                inventory[slot] = ItemStack.loadItemStackFromNBT(item);
            }
        }
    }

    /**
     * A custom method to write our inventory to an ItemStack's NBT compound
     */
    public void writeToNBT(NBTTagCompound tagcompound)
    {
        // Create a new NBT Tag List to store itemstacks as NBT Tags
        NBTTagList items = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i)
        {
            // Only write stacks that contain items
            if (getStackInSlot(i) != null)
            {
                // Make a new NBT Tag Compound to write the itemstack and slot index to
                NBTTagCompound item = new NBTTagCompound();
                item.setInteger("Slot", i);
                // Writes the itemstack in slot(i) to the Tag Compound we just made
                getStackInSlot(i).writeToNBT(item);

                // add the tag compound to our tag list
                items.appendTag(item);
            }
        }
        // Add the TagList to the ItemStack's Tag Compound with the name "ItemInventory"
        tagcompound.setTag("ItemInventory", items);
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
    public IChatComponent getDisplayName()
    {
        return null;
    }

    @Override
    public void markDirty()
    {
        for (int i = 0; i < getSizeInventory(); ++i)
        {
            if (getStackInSlot(i) != null && getStackInSlot(i).stackSize == 0)
            {
                inventory[i] = null;
            }
        }

        // This line here does the work:
        writeToNBT(invItem.getTagCompound());
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

    }

    @Override
    public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player,
                                     Entity entity)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Random getItemRand()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RangedComponent getRangedComponent()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean canOpenGui()
    {
        // TODO Auto-generated method stub
        return false;
    }
}