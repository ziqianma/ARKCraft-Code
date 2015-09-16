package net.minecraft.client.resources.model;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum ModelRotation implements net.minecraftforge.client.model.IModelState, net.minecraftforge.client.model.ITransformation
{
    X0_Y0(0, 0),
    X0_Y90(0, 90),
    X0_Y180(0, 180),
    X0_Y270(0, 270),
    X90_Y0(90, 0),
    X90_Y90(90, 90),
    X90_Y180(90, 180),
    X90_Y270(90, 270),
    X180_Y0(180, 0),
    X180_Y90(180, 90),
    X180_Y180(180, 180),
    X180_Y270(180, 270),
    X270_Y0(270, 0),
    X270_Y90(270, 90),
    X270_Y180(270, 180),
    X270_Y270(270, 270);
    private static final Map mapRotations = Maps.newHashMap();
    private final int combinedXY;
    private final Matrix4d matrix4d;
    private final int quartersX;
    private final int quartersY;

    private static final String __OBFID = "CL_00002393";

    private static int combineXY(int p_177521_0_, int p_177521_1_)
    {
        return p_177521_0_ * 360 + p_177521_1_;
    }

    private ModelRotation(int p_i46087_3_, int p_i46087_4_)
    {
        this.combinedXY = combineXY(p_i46087_3_, p_i46087_4_);
        this.matrix4d = new Matrix4d();
        Matrix4d matrix4d = new Matrix4d();
        matrix4d.setIdentity();
        matrix4d.setRotation(new AxisAngle4d(1.0D, 0.0D, 0.0D, (double)((float)(-p_i46087_3_) * 0.017453292F)));
        this.quartersX = MathHelper.abs_int(p_i46087_3_ / 90);
        Matrix4d matrix4d1 = new Matrix4d();
        matrix4d1.setIdentity();
        matrix4d1.setRotation(new AxisAngle4d(0.0D, 1.0D, 0.0D, (double)((float)(-p_i46087_4_) * 0.017453292F)));
        this.quartersY = MathHelper.abs_int(p_i46087_4_ / 90);
        this.matrix4d.mul(matrix4d1, matrix4d);
    }

    public Matrix4d getMatrix4d()
    {
        return this.matrix4d;
    }

    public EnumFacing rotateFace(EnumFacing p_177523_1_)
    {
        EnumFacing enumfacing1 = p_177523_1_;
        int i;

        for (i = 0; i < this.quartersX; ++i)
        {
            enumfacing1 = enumfacing1.rotateAround(EnumFacing.Axis.X);
        }

        if (enumfacing1.getAxis() != EnumFacing.Axis.Y)
        {
            for (i = 0; i < this.quartersY; ++i)
            {
                enumfacing1 = enumfacing1.rotateAround(EnumFacing.Axis.Y);
            }
        }

        return enumfacing1;
    }

    public int rotateVertex(EnumFacing facing, int vertexIndex)
    {
        int j = vertexIndex;

        if (facing.getAxis() == EnumFacing.Axis.X)
        {
            j = (vertexIndex + this.quartersX) % 4;
        }

        EnumFacing enumfacing1 = facing;

        for (int k = 0; k < this.quartersX; ++k)
        {
            enumfacing1 = enumfacing1.rotateAround(EnumFacing.Axis.X);
        }

        if (enumfacing1.getAxis() == EnumFacing.Axis.Y)
        {
            j = (j + this.quartersY) % 4;
        }

        return j;
    }

    public static ModelRotation getModelRotation(int p_177524_0_, int p_177524_1_)
    {
        return (ModelRotation)mapRotations.get(Integer.valueOf(combineXY(MathHelper.normalizeAngle(p_177524_0_, 360), MathHelper.normalizeAngle(p_177524_1_, 360))));
    }

    static
    {
        ModelRotation[] var0 = values();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2)
        {
            ModelRotation var3 = var0[var2];
            mapRotations.put(Integer.valueOf(var3.combinedXY), var3);
        }
    }

    public net.minecraftforge.client.model.TRSRTransformation apply(net.minecraftforge.client.model.IModelPart part) { return new net.minecraftforge.client.model.TRSRTransformation(getMatrix()); }
    public javax.vecmath.Matrix4f getMatrix() { return net.minecraftforge.client.ForgeHooksClient.getMatrix(this); }
    public EnumFacing rotate(EnumFacing facing) { return rotateFace(facing); }
    public int rotate(EnumFacing facing, int vertexIndex) { return rotateVertex(facing, vertexIndex); }
}