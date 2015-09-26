package com.arkcraft.mod.core.items.weapons;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.mod.core.GlobalAdditions;

public class ItemCompoundBow extends ItemBow{

	public ItemCompoundBow(String name) {
		super();
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setMaxStackSize(1);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
	    return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack itemstack)
	{
		return EnumAction.BOW;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World w, EntityPlayer p) {
	
			 if (p.capabilities.isCreativeMode || p.inventory.hasItem(GlobalAdditions.tranq_arrow))
		        {
		            p.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		        }
			 if (p.capabilities.isCreativeMode || p.inventory.hasItem(GlobalAdditions.stone_arrow))
		        {
		            p.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		        }
			 if (p.capabilities.isCreativeMode || p.inventory.hasItem(GlobalAdditions.metal_arrow))
		        {
		            p.setItemInUse(stack, this.getMaxItemUseDuration(stack));
		        }
		return super.onItemRightClick(stack, w, p);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}
}