package com.arkcraft.mod.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * @author Vastatio
 */
/* For some reason, minecraft doesnt put args inside its list. //GEGY: This is just decompilation*/
@SuppressWarnings("all")
public class ARKItem extends Item {

	public String[] tooltips;

	public ARKItem() {
	}

	public ARKItem(String... tooltips) {
		this.tooltips = tooltips;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn,
			List tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		if(tooltips != null) for (int i = 0; i < tooltips.length; i++) tooltip.add(tooltips[i]);
		if(tooltips == null) tooltip.add(EnumChatFormatting.GOLD + "Material of the Ages");
	}

	// ticks that this food will keep a dino unconscious
	public static int getItemTorporTime(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKFood) {
				if (stack.getItem() == (Item) ARKCraftItems.narcotics)
					return 1000;
			}
		}
		return 0;
	}
}
