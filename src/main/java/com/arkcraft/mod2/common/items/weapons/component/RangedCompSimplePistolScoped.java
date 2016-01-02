package com.arkcraft.mod2.common.items.weapons.component;

public class RangedCompSimplePistolScoped extends RangedCompSimplePistol
{
	public RangedCompSimplePistolScoped()
	{
		super();
	}

	@Override
	public boolean ifCanScope() {
		return true;
	}
}