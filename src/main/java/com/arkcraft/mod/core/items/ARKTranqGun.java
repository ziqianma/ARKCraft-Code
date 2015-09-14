package com.arkcraft.mod.core.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.entity.EntityTranqAmmo;

public class ARKTranqGun extends Item{
	
	public ARKTranqGun(String name) {
		super();
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
	}
	
	@Override
	 public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	   {
	       if (!par3EntityPlayer.capabilities.isCreativeMode)
	       {
	           --par1ItemStack.stackSize;
	       }
	       par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
	       if (!par2World.isRemote)
	       {
	           par2World.spawnEntityInWorld(new EntityTranqAmmo(par2World, par3EntityPlayer));
	       }
	       return par1ItemStack;
	   }
}
