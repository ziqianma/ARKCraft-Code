package com.arkcraft.module.core.client.event;

import com.arkcraft.module.core.client.gui.GuiMainMenuOverride;
import com.arkcraft.module.core.client.sound.SoundARKTheme;
import net.ilexiconn.llibrary.client.gui.GuiLLibraryMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CoreClientEventHandler
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