package com.arkcraft.mod.core.proxy;

import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.mod.core.items.weapons.component.RangedComponent;
import com.arkcraft.mod.core.items.weapons.handlers.IItemWeapon;

@SideOnly(Side.CLIENT)
public class ClientEventHandler
{	
	@SubscribeEvent
	public void onFOVUpdateEvent(FOVUpdateEvent e)
	{
		if (e.entity.isUsingItem() && e.entity.getItemInUse().getItem() instanceof IItemWeapon)
		{
			RangedComponent rc = ((IItemWeapon) e.entity.getItemInUse().getItem()).getRangedComponent();
			if (rc != null && RangedComponent.isReadyToFire(e.entity.getItemInUse()))
			{
				e.newfov = e.fov * rc.getFOVMultiplier(e.entity.getItemInUseDuration());
			}
		}
	}
}