package net.minecraft.client.renderer.block.model;

import javax.vecmath.Vector3f;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockPartRotation
{
    public final Vector3f origin;
    public final EnumFacing.Axis axis;
    public final float angle;
    public final boolean rescale;
    private static final String __OBFID = "CL_00002506";

    public BlockPartRotation(Vector3f p_i46229_1_, EnumFacing.Axis p_i46229_2_, float p_i46229_3_, boolean p_i46229_4_)
    {
        this.origin = p_i46229_1_;
        this.axis = p_i46229_2_;
        this.angle = p_i46229_3_;
        this.rescale = p_i46229_4_;
    }
}