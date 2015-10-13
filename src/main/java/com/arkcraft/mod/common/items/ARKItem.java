package com.arkcraft.mod.common.items;

import java.util.List;

import com.arkcraft.mod.GlobalAdditions;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author Vastatio
 */
/* For some reason, minecraft doesnt put args inside its list. */
@SuppressWarnings("all")
public class ARKItem extends Item {

	public String[] tooltips;

	public ARKItem(String name) {
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
	}

	public ARKItem(String name, String... tooltips) {
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
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
