package com.arkcraft.mod.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.entity.EntityDodoEgg;

/**
 * @author BubbleTrouble
 */

public class ARKEggItem extends Item {
	
	public ARKEggItem(String name) {
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setMaxStackSize(16);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
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