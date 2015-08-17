package com.arkcraft.mod.core.machine.gui;

import com.arkcraft.mod.core.lib.LogHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class CopyOfDinoInventory extends InventoryBasic {
	
	public CopyOfDinoInventory(String title, boolean customName, int slotCount) {
		super(title, customName, slotCount);
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

    public NBTTagList saveInventoryToNBT() {
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
        return nbttaglist;
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
}
