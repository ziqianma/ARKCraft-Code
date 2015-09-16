package net.minecraft.client.audio;

import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ITickableSound extends ISound, IUpdatePlayerListBox
{
    boolean isDonePlaying();
}