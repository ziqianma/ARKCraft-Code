package com.arkcraft.mod.common.items;

import com.arkcraft.mod.common.entity.item.projectiles.EntityGrenade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;

public class ItemGrenade extends Item{
	
	public ItemGrenade() {
		super();
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
    	if (!playerIn.capabilities.isCreativeMode) {
            --itemStackIn.stackSize;
        }
    	worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
    	if (!worldIn.isRemote) {
    		worldIn.spawnEntityInWorld(new EntityGrenade(worldIn, playerIn));
    	}
    	playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
    	return itemStackIn;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack itemstack)
	{
		return EnumAction.BOW;
	}
	
	 public int getMaxItemUseDuration(ItemStack stack)
	 {
	        return 72000;
	 }
	
}



