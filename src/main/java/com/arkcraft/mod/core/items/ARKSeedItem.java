package com.arkcraft.mod.core.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.mod.core.GlobalAdditions;

/***
 * 
 * @author wildbill22
 *
 */
public class ARKSeedItem extends Item {
	
	public ARKSeedItem(String name) {
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setMaxStackSize(16);
		this.setMaxDamage(300); // 5 minutes of damage at 1 a second
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
	}
	
	public static ItemStack getBerryForSeed(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKSeedItem) {
				if (stack.getItem() == (Item) GlobalAdditions.amarBerrySeed)
					return new ItemStack(GlobalAdditions.amarBerry);
				if (stack.getItem() == (Item) GlobalAdditions.azulBerrySeed)
					return new ItemStack(GlobalAdditions.azulBerry);
				if (stack.getItem() == (Item) GlobalAdditions.mejoBerrySeed)
					return new ItemStack(GlobalAdditions.mejoBerry);
				if (stack.getItem() == (Item) GlobalAdditions.narcoBerrySeed)
					return new ItemStack(GlobalAdditions.narcoBerry);
				if (stack.getItem() == (Item) GlobalAdditions.tintoBerrySeed)
					return new ItemStack(GlobalAdditions.tintoBerry);
			}
		}
		return null;
	}
	
    /**
     * allows items to add custom lines of information to the mouseover description
     *  
     * @param tooltip All lines to display in the Item's tooltip. This is a List of Strings.
     * @param advanced Whether the setting "Advanced tooltips" is enabled
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
    	tooltip.add("Decomposes in " + stack.getItemDamage() + " seconds");
    }
}
