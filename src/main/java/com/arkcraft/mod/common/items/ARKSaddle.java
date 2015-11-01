package com.arkcraft.mod.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSaddle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

@SuppressWarnings("all")
public class ARKSaddle extends ItemSaddle {
	
	public ARKSaddle() {
		super();
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn,
			List tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add(EnumChatFormatting.ITALIC + "A Dino Rider's Dream");
	}
}
