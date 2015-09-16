package net.minecraft.entity.boss;

import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IBossDisplayData
{
    float getMaxHealth();

    float getHealth();

    IChatComponent getDisplayName();
}