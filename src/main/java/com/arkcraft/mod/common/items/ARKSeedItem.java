package com.arkcraft.mod.common.items;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.lib.BALANCE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

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
				if (stack.getItem() == (Item) ARKCraftItems.amarBerrySeed)
					return new ItemStack(ARKCraftItems.amarBerry);
				if (stack.getItem() == (Item) ARKCraftItems.azulBerrySeed)
					return new ItemStack(ARKCraftItems.azulBerry);
				if (stack.getItem() == (Item) ARKCraftItems.mejoBerrySeed)
					return new ItemStack(ARKCraftItems.mejoBerry);
				if (stack.getItem() == (Item) ARKCraftItems.narcoBerrySeed)
					return new ItemStack(ARKCraftItems.narcoBerry);
				if (stack.getItem() == (Item) ARKCraftItems.tintoBerrySeed)
					return new ItemStack(ARKCraftItems.tintoBerry);
				if (stack.getItem() == (Item) ARKCraftItems.stimBerrySeed)
					return new ItemStack(ARKCraftItems.stimBerrySeed);
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
