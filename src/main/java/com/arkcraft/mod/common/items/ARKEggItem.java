package com.arkcraft.mod.common.items;

import com.arkcraft.mod.common.entity.EntityDodoEgg;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

/**
 * @author BubbleTrouble
 */

public class ARKEggItem extends Item {
	
	public ARKEggItem() {
		this.setMaxStackSize(16);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    	if (!playerIn.capabilities.isCreativeMode) {
            --itemStackIn.stackSize;
        }
    	worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    	if (!worldIn.isRemote) {
    		worldIn.spawnEntityInWorld(new EntityDodoEgg(worldIn, playerIn));
    	}
    	playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
    	return itemStackIn;
	}
}