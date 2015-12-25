package com.arkcraft.mod2.common.items;

import com.arkcraft.mod2.common.entity.item.projectiles.EntityBase;
import com.arkcraft.mod2.common.entity.item.projectiles.EntityGrenade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGrenade extends Item{
	
	public ItemGrenade() {
		super();
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
	{
		if (!entityplayer.inventory.hasItem(this)) return;
		
		int j = getMaxItemUseDuration(itemstack) - i;
		float f = j / 20F;
		f = (f * f + f * 2.0F) / 3F;
		if (f < 0.1F) return;
		if (f > 1.0F)
		{
			f = 1.0F;
		}

		if (entityplayer.capabilities.isCreativeMode || entityplayer.inventory.consumeInventoryItem(this))
		{
			world.playSoundAtEntity(entityplayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
			if (!world.isRemote)
			{
				EntityBase entiyGrenade = new EntityBase(world, entityplayer);
				world.spawnEntityInWorld(entiyGrenade);
			}
		}
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack itemstack)
	{
		return EnumAction.BLOCK;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 0x11940;
	}
	 
	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
		{
			if (entityplayer.inventory.hasItem(this))
			{
				entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
			}
			return itemstack;
		} 
	
}



