package com.arkcraft.module.core.client.sound;

import com.arkcraft.module.core.ARKCraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.util.ResourceLocation;

public class SoundARKTheme implements ISound
{
    @Override
    public ResourceLocation getSoundLocation()
    {
        return new ResourceLocation(ARKCraft.MODID, "ark_theme");
    }

    @Override
    public boolean canRepeat()
    {
        return false;
    }

    @Override
    public int getRepeatDelay()
    {
        return 0;
    }

    @Override
    public float getVolume()
    {
        return 1.0F;
    }

    @Override
    public float getPitch()
    {
        return 1.0F;
    }

    @Override
    public float getXPosF()
    {
        return 0;
    }

    @Override
    public float getYPosF()
    {
        return 0;
    }

    @Override
    public float getZPosF()
    {
        return 0;
    }

    @Override
    public AttenuationType getAttenuationType()
    {
        return AttenuationType.NONE;
    }
}
