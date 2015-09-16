package net.minecraft.client.gui.spectator;

import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISpectatorMenuObject
{
    void func_178661_a(SpectatorMenu p_178661_1_);

    IChatComponent func_178664_z_();

    void func_178663_a(float p_178663_1_, int p_178663_2_);

    boolean func_178662_A_();
}