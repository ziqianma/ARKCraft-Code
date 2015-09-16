package net.minecraft.client.renderer;

import java.awt.image.BufferedImage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IImageBuffer
{
    BufferedImage parseUserSkin(BufferedImage p_78432_1_);

    void skinAvailable();
}