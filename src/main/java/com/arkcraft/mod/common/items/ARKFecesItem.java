package com.arkcraft.mod.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class ARKFecesItem extends Item{
	
	public ARKFecesItem() {
		this.setMaxStackSize(1);
	}

	// seconds that this fertilizer will grow a crop
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
