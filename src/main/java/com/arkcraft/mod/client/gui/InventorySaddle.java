package com.arkcraft.mod.client.gui;

import com.arkcraft.mod.common.entity.EntityTameableDinosaur;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

/***
 * 
 * @author wildbill22
 *
 */
public class InventorySaddle extends InventoryBasic {
	/** The dino whose inventory this is. */
	public EntityTameableDinosaur entityDino;

	public InventorySaddle(String title, boolean customName, int slotCount) {
		super(title, customName, slotCount);
	}

    public InventorySaddle(EntityTameableDinosaur entityDino) {
		super("Dino Saddle", false, 1);
		this.entityDino = entityDino;
	}

    /**
     * Do not give this method the name canInteractWith because it clashes with Container
     */
    public boolean isUseableByPlayer(EntityPlayer player) {
        return super.isUseableByPlayer(player);
    }

    public void openInventory(EntityPlayer player)  {
        super.openInventory(player);
//        LogHelper.info("InventorySaddle: Opened!");
		if (entityDino.isSaddled()){
			this.setInventorySlotContents(0, new ItemStack(entityDino.saddleType.getSaddleItem()) );
		}
    }

    public void closeInventory(EntityPlayer player) {
//        LogHelper.info("InventorySaddle: Closed!");
    	if (this.getStackInSlot(0) != null)
    		this.entityDino.setSaddled(true);
    	else
    		this.entityDino.setSaddled(false);
        super.closeInventory(player);
    }    
}
