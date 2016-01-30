package com.arkcraft.module.weapon.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.weapon.common.item.ranged.ItemRangedWeapon;
import com.arkcraft.module.weapon.common.network.ReloadFinished;

public class CommonEventHandler
{
	public static void init()
	{
		CommonEventHandler handler = new CommonEventHandler();
		FMLCommonHandler.instance().bus().register(handler);
		MinecraftForge.EVENT_BUS.register(handler);
	}

	public static int reloadTicks = 0;

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent evt)
	{
		EntityPlayer p = evt.player;
		ItemStack stack = p.getCurrentEquippedItem();
		if (stack != null && stack.getItem() instanceof ItemRangedWeapon)
		{
			ItemRangedWeapon w = (ItemRangedWeapon) stack.getItem();
			if (w.isReloading(stack))
			{
				LogHelper.info(w.getReloadDuration());
				if (++reloadTicks >= w.getReloadDuration())
				{
					LogHelper.info(reloadTicks);
					if (!p.worldObj.isRemote)
					{
						w.setReloading(stack, p, false);
						reloadTicks = 0;
						w.hasAmmoAndConsume(stack, p);
						w.effectReloadDone(stack, p.worldObj, p);
						ARKCraft.modChannel.sendTo(new ReloadFinished(), (EntityPlayerMP) p);
					}
				}
			}
		}
	}
}
