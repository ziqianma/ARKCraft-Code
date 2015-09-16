package net.minecraft.client.audio;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundEventAccessorComposite implements ISoundEventAccessor
{
    /** A composite (List) of ISoundEventAccessors */
    private final List soundPool = Lists.newArrayList();
    private final Random rnd = new Random();
    private final ResourceLocation soundLocation;
    private final SoundCategory category;
    private double eventPitch;
    private double eventVolume;
    private static final String __OBFID = "CL_00001146";

    public SoundEventAccessorComposite(ResourceLocation soundLocation, double pitch, double volume, SoundCategory category)
    {
        this.soundLocation = soundLocation;
        this.eventVolume = volume;
        this.eventPitch = pitch;
        this.category = category;
    }

    public int getWeight()
    {
        int i = 0;
        ISoundEventAccessor isoundeventaccessor;

        for (Iterator iterator = this.soundPool.iterator(); iterator.hasNext(); i += isoundeventaccessor.getWeight())
        {
            isoundeventaccessor = (ISoundEventAccessor)iterator.next();
        }

        return i;
    }

    public SoundPoolEntry cloneEntry()
    {
        int i = this.getWeight();

        if (!this.soundPool.isEmpty() && i != 0)
        {
            int j = this.rnd.nextInt(i);
            Iterator iterator = this.soundPool.iterator();
            ISoundEventAccessor isoundeventaccessor;

            do
            {
                if (!iterator.hasNext())
                {
                    return SoundHandler.missing_sound;
                }

                isoundeventaccessor = (ISoundEventAccessor)iterator.next();
                j -= isoundeventaccessor.getWeight();
            }
            while (j >= 0);

            SoundPoolEntry soundpoolentry = (SoundPoolEntry)isoundeventaccessor.cloneEntry();
            soundpoolentry.setPitch(soundpoolentry.getPitch() * this.eventPitch);
            soundpoolentry.setVolume(soundpoolentry.getVolume() * this.eventVolume);
            return soundpoolentry;
        }
        else
        {
            return SoundHandler.missing_sound;
        }
    }

    public void addSoundToEventPool(ISoundEventAccessor p_148727_1_)
    {
        this.soundPool.add(p_148727_1_);
    }

    public ResourceLocation getSoundEventLocation()
    {
        return this.soundLocation;
    }

    public SoundCategory getSoundCategory()
    {
        return this.category;
    }
}