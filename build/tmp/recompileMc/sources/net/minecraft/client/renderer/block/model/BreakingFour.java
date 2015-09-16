package net.minecraft.client.renderer.block.model;

import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BreakingFour extends BakedQuad
{
    private final TextureAtlasSprite texture;
    private static final String __OBFID = "CL_00002492";

    public BreakingFour(BakedQuad p_i46217_1_, TextureAtlasSprite p_i46217_2_)
    {
        super(Arrays.copyOf(p_i46217_1_.getVertexData(), p_i46217_1_.getVertexData().length), p_i46217_1_.tintIndex, FaceBakery.getFacingFromVertexData(p_i46217_1_.getVertexData()));
        this.texture = p_i46217_2_;
        this.func_178217_e();
    }

    private void func_178217_e()
    {
        for (int i = 0; i < 4; ++i)
        {
            this.func_178216_a(i);
        }
    }

    private void func_178216_a(int p_178216_1_)
    {
        int j = 7 * p_178216_1_;
        float f = Float.intBitsToFloat(this.vertexData[j]);
        float f1 = Float.intBitsToFloat(this.vertexData[j + 1]);
        float f2 = Float.intBitsToFloat(this.vertexData[j + 2]);
        float f3 = 0.0F;
        float f4 = 0.0F;

        switch (BreakingFour.SwitchEnumFacing.field_178419_a[this.face.ordinal()])
        {
            case 1:
                f3 = f * 16.0F;
                f4 = (1.0F - f2) * 16.0F;
                break;
            case 2:
                f3 = f * 16.0F;
                f4 = f2 * 16.0F;
                break;
            case 3:
                f3 = (1.0F - f) * 16.0F;
                f4 = (1.0F - f1) * 16.0F;
                break;
            case 4:
                f3 = f * 16.0F;
                f4 = (1.0F - f1) * 16.0F;
                break;
            case 5:
                f3 = f2 * 16.0F;
                f4 = (1.0F - f1) * 16.0F;
                break;
            case 6:
                f3 = (1.0F - f2) * 16.0F;
                f4 = (1.0F - f1) * 16.0F;
        }

        this.vertexData[j + 4] = Float.floatToRawIntBits(this.texture.getInterpolatedU((double)f3));
        this.vertexData[j + 4 + 1] = Float.floatToRawIntBits(this.texture.getInterpolatedV((double)f4));
    }

    @SideOnly(Side.CLIENT)

    static final class SwitchEnumFacing
        {
            static final int[] field_178419_a = new int[EnumFacing.values().length];
            private static final String __OBFID = "CL_00002491";

            static
            {
                try
                {
                    field_178419_a[EnumFacing.DOWN.ordinal()] = 1;
                }
                catch (NoSuchFieldError var6)
                {
                    ;
                }

                try
                {
                    field_178419_a[EnumFacing.UP.ordinal()] = 2;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                try
                {
                    field_178419_a[EnumFacing.NORTH.ordinal()] = 3;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    field_178419_a[EnumFacing.SOUTH.ordinal()] = 4;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_178419_a[EnumFacing.WEST.ordinal()] = 5;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_178419_a[EnumFacing.EAST.ordinal()] = 6;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}