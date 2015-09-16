package net.minecraft.client.gui.spectator;

import java.util.List;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISpectatorMenuView
{
    List func_178669_a();

    IChatComponent func_178670_b();
}