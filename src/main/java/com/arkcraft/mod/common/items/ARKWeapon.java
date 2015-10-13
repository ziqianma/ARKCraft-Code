package com.arkcraft.mod.common.items;

import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.GlobalAdditions;
/**
 * @author Vastatio
 */
public class ARKWeapon extends ItemSword {

	public ARKWeapon(String name, ToolMaterial m) {
		super(m);
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
	}
	
}
