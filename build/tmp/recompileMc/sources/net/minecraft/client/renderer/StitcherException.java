package net.minecraft.client.renderer;

import net.minecraft.client.renderer.texture.Stitcher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StitcherException extends RuntimeException
{
    private final Stitcher.Holder holder;
    private static final String __OBFID = "CL_00001057";

    public StitcherException(Stitcher.Holder p_i2344_1_, String p_i2344_2_)
    {
        super(p_i2344_2_);
        this.holder = p_i2344_1_;
    }
}