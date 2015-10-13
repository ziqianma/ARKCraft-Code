package com.arkcraft.mod.common.items;

import java.util.List;

import com.arkcraft.mod.GlobalAdditions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSaddle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("all")
public class ARKSaddle extends ItemSaddle {
	
	public ARKSaddle(String name) {
		super();
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn,
			List tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		tooltip.add(EnumChatFormatting.ITALIC + "A Dino Rider's Dream");
	}
	
	
	
}
