package com.arkcraft.mod.core.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.lib.BALANCE;

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
		this.setMaxDamage(BALANCE.CROP_PLOT.SECONDS_FOR_SEED_TO_DECOMPOSE); // 5 minutes of damage at 1 a second
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
	}
	
	public static ItemStack getBerryForSeed(ItemStack stack) {
		if (stack != null) {
			if (stack.getItem() instanceof ARKSeedItem) {
				if (stack.getItem() == (Item) ModItems.amarBerrySeed)
					return new ItemStack(ModItems.amarBerry);
				if (stack.getItem() == (Item) ModItems.azulBerrySeed)
					return new ItemStack(ModItems.azulBerry);
				if (stack.getItem() == (Item) ModItems.mejoBerrySeed)
					return new ItemStack(ModItems.mejoBerry);
				if (stack.getItem() == (Item) ModItems.narcoBerrySeed)
					return new ItemStack(ModItems.narcoBerry);
				if (stack.getItem() == (Item) ModItems.tintoBerrySeed)
					return new ItemStack(ModItems.tintoBerry);
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
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer playerIn, List tooltip, boolean advanced) {
    	tooltip.add("Decomposes in " + (getMaxDamage() - itemStack.getItemDamage()) + " seconds");
    }
}
