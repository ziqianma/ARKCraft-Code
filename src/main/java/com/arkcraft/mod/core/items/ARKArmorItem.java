package com.arkcraft.mod.core.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.Main;

public class ARKArmorItem extends ItemArmor {

	public String texName;
	
	public ARKArmorItem(String name, ArmorMaterial mat, String texName, int type) {
		super(mat, 0, type);
		this.setUnlocalizedName(name);
		this.texName = texName;
		this.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(this, name);
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type) {
		return Main.MODID + ":textures/armor/" + this.texName + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
	}
	
}
