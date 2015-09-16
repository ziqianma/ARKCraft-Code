package net.minecraft.client.renderer.texture;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IIconCreator
{
    void registerSprites(TextureMap iconRegistry);
}