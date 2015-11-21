package com.arkcraft.mod2.common.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CommonEventHandler
{
    @SubscribeEvent
    public void damagePlayerFromPunching(PlayerEvent.BreakSpeed event)
    {
        EntityPlayer player = event.entityPlayer;

        if (player.getHeldItem() == null)
        {
            player.attackEntityFrom(DamageSource.generic, 1.0F); //TODO new damage source
        }
    }
}