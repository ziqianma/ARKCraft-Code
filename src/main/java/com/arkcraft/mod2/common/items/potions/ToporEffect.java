package com.arkcraft.mod2.common.items.potions;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToporEffect extends Potion {
	
	public ToporEffect(int ID, ResourceLocation location,  boolean isBad, int color)
	{
		super(ID, location, isBad, color);
	}

	public boolean isInstant() {
		return false;
	}

	@Override
	public Potion setIconIndex(int par1, int par2) {
		super.setIconIndex(par1, par2);
		return this;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasStatusIcon() {
		return false;
	}

}