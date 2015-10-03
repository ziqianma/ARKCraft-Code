package com.arkcraft.mod.core.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.mod.core.GlobalAdditions;

public class ARKFecesItem extends Item{
	
	public ARKFecesItem(String name) {
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setMaxStackSize(1);
		GameRegistry.registerItem(this, this.getUnlocalizedName().substring(5));
	
	}

	// ticks that this fertilizer will grow a crop
	public static int getItemGrowTime(ItemStack itemStack) {
		return itemStack.getMaxDamage() - itemStack.getItemDamage();
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
