package com.arkcraft.mod.client.event;

import com.arkcraft.mod.client.gui.GuiMainMenuOverride;
import com.arkcraft.mod.client.sound.SoundARKTheme;
import com.arkcraft.mod.common.items.weapons.component.RangedComponent;
import com.arkcraft.mod.common.items.weapons.handlers.IItemWeapon;
import net.ilexiconn.llibrary.client.gui.GuiLLibraryMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
	private Minecraft mc = Minecraft.getMinecraft();
	private ISound music;

	@SubscribeEvent
	public void clientTick(TickEvent.ClientTickEvent event)
	{
		if (mc.currentScreen instanceof GuiMainMenuOverride && !mc.getSoundHandler().isSoundPlaying(music))
		{
			music = new SoundARKTheme();
			mc.getSoundHandler().playSound(music);
		}
	}

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

	@SubscribeEvent
	public void guiOpen(GuiOpenEvent event)
	{
		if (event.gui instanceof GuiMainMenu || event.gui instanceof GuiLLibraryMainMenu || event.gui instanceof GuiMainMenuOverride)
		{
			event.gui = new GuiMainMenuOverride();
		}
	}

	@SubscribeEvent
	public void playSound(PlaySoundEvent event)
	{
		if (!(event.name.contains("ark_theme")) && mc.currentScreen instanceof GuiMainMenuOverride)
		{
			event.result = null;
		}
	}
}