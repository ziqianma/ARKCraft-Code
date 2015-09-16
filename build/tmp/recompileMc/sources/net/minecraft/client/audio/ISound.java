package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISound
{
    ResourceLocation getSoundLocation();

    boolean canRepeat();

    int getRepeatDelay();

    float getVolume();

    float getPitch();

    float getXPosF();

    float getYPosF();

    float getZPosF();

    ISound.AttenuationType getAttenuationType();

    @SideOnly(Side.CLIENT)
    public static enum AttenuationType
    {
        NONE(0),
        LINEAR(2);
        private final int type;

        private static final String __OBFID = "CL_00001126";

        private AttenuationType(int p_i45110_3_)
        {
            this.type = p_i45110_3_;
        }

        public int getTypeInt()
        {
            return this.type;
        }
    }
}