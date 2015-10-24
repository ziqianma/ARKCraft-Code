package com.arkcraft.mod.client.gui;

import com.arkcraft.mod.common.lib.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/***
 * 
 * @author wildbill22
 *
 */
public class InventoryPlayerCrafting extends InventoryBasic {
	
	public InventoryPlayerCrafting(String title, boolean customName, int slotCount) {
		super(title, customName, slotCount);
	}

    public void loadInventoryFromNBT(NBTTagCompound nbt)  {
		final byte NBT_TYPE_COMPOUND = 10;  
		NBTTagList dataForAllItems = nbt.getTagList("Items", NBT_TYPE_COMPOUND);
		loadInventoryFromNBT(dataForAllItems);
    }
    
    public void loadInventoryFromNBT(NBTTagList nbt)  {
        int i;
        for (i = 0; i < this.getSizeInventory(); ++i) {
            this.setInventorySlotContents(i, (ItemStack)null);
        }
        for (i = 0; i < nbt.tagCount(); ++i) {
            NBTTagCompound nbttagcompound = nbt.getCompoundTagAt(i);
            int j = nbttagcompound.getByte("Slot") & 255;

            if (j >= 0 && j < this.getSizeInventory()) {
                this.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
            }
        }
    }

    public void saveInventoryToNBT(NBTTagCompound nbt) {
        NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            ItemStack itemstack = this.getStackInSlot(i);
            if (itemstack != null) {
                NBTTagCompound nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Slot", (byte)i);
                itemstack.writeToNBT(nbttagcompound);
                nbttaglist.appendTag(nbttagcompound);
                LogHelper.info("DinoInventory: Saved a " + itemstack.getItem() + " to inventory.");
            }
        }
		nbt.setTag("Items", nbttaglist);
    }

    /**
     * Do not give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        return super.isUseableByPlayer(player);
    }

    public void openInventory(EntityPlayer player)  {
        super.openInventory(player);
    }

    public void closeInventory(EntityPlayer player) {
        super.closeInventory(player);
    }    
    
	public ItemStack[] getItemStacks() {
		ItemStack[] blueprintStacks = new ItemStack[getSizeInventory()];
		for (int i = 0; i < getSizeInventory(); i++)
            blueprintStacks[i] = getStackInSlot(i);
		return blueprintStacks;
	}
}
