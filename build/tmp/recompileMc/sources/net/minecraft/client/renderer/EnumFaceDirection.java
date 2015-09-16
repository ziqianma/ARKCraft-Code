package net.minecraft.client.renderer;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum EnumFaceDirection
{
    DOWN(new EnumFaceDirection.VertexInformation[]{new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179176_f, EnumFaceDirection.Constants.field_179178_e, EnumFaceDirection.Constants.field_179181_a, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179176_f, EnumFaceDirection.Constants.field_179178_e, EnumFaceDirection.Constants.field_179177_d, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179180_c, EnumFaceDirection.Constants.field_179178_e, EnumFaceDirection.Constants.field_179177_d, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179180_c, EnumFaceDirection.Constants.field_179178_e, EnumFaceDirection.Constants.field_179181_a, null)}),
    UP(new EnumFaceDirection.VertexInformation[]{new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179176_f, EnumFaceDirection.Constants.field_179179_b, EnumFaceDirection.Constants.field_179177_d, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179176_f, EnumFaceDirection.Constants.field_179179_b, EnumFaceDirection.Constants.field_179181_a, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179180_c, EnumFaceDirection.Constants.field_179179_b, EnumFaceDirection.Constants.field_179181_a, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179180_c, EnumFaceDirection.Constants.field_179179_b, EnumFaceDirection.Constants.field_179177_d, null)}),
    NORTH(new EnumFaceDirection.VertexInformation[]{new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179180_c, EnumFaceDirection.Constants.field_179179_b, EnumFaceDirection.Constants.field_179177_d, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179180_c, EnumFaceDirection.Constants.field_179178_e, EnumFaceDirection.Constants.field_179177_d, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179176_f, EnumFaceDirection.Constants.field_179178_e, EnumFaceDirection.Constants.field_179177_d, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179176_f, EnumFaceDirection.Constants.field_179179_b, EnumFaceDirection.Constants.field_179177_d, null)}),
    SOUTH(new EnumFaceDirection.VertexInformation[]{new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179176_f, EnumFaceDirection.Constants.field_179179_b, EnumFaceDirection.Constants.field_179181_a, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179176_f, EnumFaceDirection.Constants.field_179178_e, EnumFaceDirection.Constants.field_179181_a, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179180_c, EnumFaceDirection.Constants.field_179178_e, EnumFaceDirection.Constants.field_179181_a, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179180_c, EnumFaceDirection.Constants.field_179179_b, EnumFaceDirection.Constants.field_179181_a, null)}),
    WEST(new EnumFaceDirection.VertexInformation[]{new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179176_f, EnumFaceDirection.Constants.field_179179_b, EnumFaceDirection.Constants.field_179177_d, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179176_f, EnumFaceDirection.Constants.field_179178_e, EnumFaceDirection.Constants.field_179177_d, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179176_f, EnumFaceDirection.Constants.field_179178_e, EnumFaceDirection.Constants.field_179181_a, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179176_f, EnumFaceDirection.Constants.field_179179_b, EnumFaceDirection.Constants.field_179181_a, null)}),
    EAST(new EnumFaceDirection.VertexInformation[]{new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179180_c, EnumFaceDirection.Constants.field_179179_b, EnumFaceDirection.Constants.field_179181_a, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179180_c, EnumFaceDirection.Constants.field_179178_e, EnumFaceDirection.Constants.field_179181_a, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179180_c, EnumFaceDirection.Constants.field_179178_e, EnumFaceDirection.Constants.field_179177_d, null), new EnumFaceDirection.VertexInformation(EnumFaceDirection.Constants.field_179180_c, EnumFaceDirection.Constants.field_179179_b, EnumFaceDirection.Constants.field_179177_d, null)});
    private static final EnumFaceDirection[] facings = new EnumFaceDirection[6];
    private final EnumFaceDirection.VertexInformation[] vertexInfos;

    private static final String __OBFID = "CL_00002562";

    public static EnumFaceDirection getFacing(EnumFacing p_179027_0_)
    {
        return facings[p_179027_0_.getIndex()];
    }

    private EnumFaceDirection(EnumFaceDirection.VertexInformation ... p_i46272_3_)
    {
        this.vertexInfos = p_i46272_3_;
    }

    public EnumFaceDirection.VertexInformation func_179025_a(int p_179025_1_)
    {
        return this.vertexInfos[p_179025_1_];
    }

    static
    {
        facings[EnumFaceDirection.Constants.field_179178_e] = DOWN;
        facings[EnumFaceDirection.Constants.field_179179_b] = UP;
        facings[EnumFaceDirection.Constants.field_179177_d] = NORTH;
        facings[EnumFaceDirection.Constants.field_179181_a] = SOUTH;
        facings[EnumFaceDirection.Constants.field_179176_f] = WEST;
        facings[EnumFaceDirection.Constants.field_179180_c] = EAST;
    }

    @SideOnly(Side.CLIENT)
    public static final class Constants
        {
            public static final int field_179181_a = EnumFacing.SOUTH.getIndex();
            public static final int field_179179_b = EnumFacing.UP.getIndex();
            public static final int field_179180_c = EnumFacing.EAST.getIndex();
            public static final int field_179177_d = EnumFacing.NORTH.getIndex();
            public static final int field_179178_e = EnumFacing.DOWN.getIndex();
            public static final int field_179176_f = EnumFacing.WEST.getIndex();
            private static final String __OBFID = "CL_00002560";
        }

    @SideOnly(Side.CLIENT)
    public static class VertexInformation
        {
            public final int field_179184_a;
            public final int field_179182_b;
            public final int field_179183_c;
            private static final String __OBFID = "CL_00002559";

            private VertexInformation(int p_i46270_1_, int p_i46270_2_, int p_i46270_3_)
            {
                this.field_179184_a = p_i46270_1_;
                this.field_179182_b = p_i46270_2_;
                this.field_179183_c = p_i46270_3_;
            }

            VertexInformation(int p_i46271_1_, int p_i46271_2_, int p_i46271_3_, Object p_i46271_4_)
            {
                this(p_i46271_1_, p_i46271_2_, p_i46271_3_);
            }
        }
}