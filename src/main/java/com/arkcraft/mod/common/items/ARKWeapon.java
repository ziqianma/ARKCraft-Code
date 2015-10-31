package com.arkcraft.mod.common.items;

import com.arkcraft.mod.GlobalAdditions;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.GameRegistry;
/**
 * @author Vastatio
 */
public class ARKWeapon extends ItemSword {

	public ARKWeapon(String name, ToolMaterial m) {
		super(m);
		this.setUnlocalizedName(name);
		this.setCreativeTab(GlobalAdditions.tabARK);
		this.setFull3D();
		GameRegistry.registerItem(this, name);
	}
	
}
