package net.minecraft.client.renderer.block.model;

import javax.vecmath.AxisAngle4d;
import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class FaceBakery
{
    private static final double field_178418_a = 1.0D / Math.cos(0.39269908169872414D) - 1.0D;
    private static final double field_178417_b = 1.0D / Math.cos((Math.PI / 4D)) - 1.0D;
    private static final String __OBFID = "CL_00002490";

    public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade)
    {
        return makeBakedQuad(posFrom, posTo, face, sprite, facing, (net.minecraftforge.client.model.ITransformation)modelRotationIn, partRotation, uvLocked, shade);
    }

    public BakedQuad makeBakedQuad(Vector3f posFrom, Vector3f posTo, BlockPartFace face, TextureAtlasSprite sprite, EnumFacing facing, net.minecraftforge.client.model.ITransformation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade)
    {
        int[] aint = this.makeQuadVertexData(face, sprite, facing, this.getPositionsDiv16(posFrom, posTo), modelRotationIn, partRotation, uvLocked, shade);
        EnumFacing enumfacing1 = getFacingFromVertexData(aint);

        if (uvLocked)
        {
            this.func_178409_a(aint, enumfacing1, face.blockFaceUV, sprite);
        }

        if (partRotation == null)
        {
            this.func_178408_a(aint, enumfacing1);
        }

        return new BakedQuad(aint, face.tintIndex, enumfacing1);
    }

    private int[] makeQuadVertexData(BlockPartFace p_178405_1_, TextureAtlasSprite p_178405_2_, EnumFacing p_178405_3_, float[] p_178405_4_, ModelRotation p_178405_5_, BlockPartRotation p_178405_6_, boolean p_178405_7_, boolean shade)
    {
        return makeQuadVertexData(p_178405_1_, p_178405_2_, p_178405_3_, p_178405_4_, (net.minecraftforge.client.model.ITransformation)p_178405_5_, p_178405_6_, p_178405_7_, shade);
    }

    private int[] makeQuadVertexData(BlockPartFace p_178405_1_, TextureAtlasSprite p_178405_2_, EnumFacing p_178405_3_, float[] p_178405_4_, net.minecraftforge.client.model.ITransformation p_178405_5_, BlockPartRotation p_178405_6_, boolean p_178405_7_, boolean shade)
    {
        int[] aint = new int[28];

        for (int i = 0; i < 4; ++i)
        {
            this.fillVertexData(aint, i, p_178405_3_, p_178405_1_, p_178405_4_, p_178405_2_, p_178405_5_, p_178405_6_, p_178405_7_, shade);
        }

        return aint;
    }

    private int getFaceShadeColor(EnumFacing p_178413_1_)
    {
        float f = this.getFaceBrightness(p_178413_1_);
        int i = MathHelper.clamp_int((int)(f * 255.0F), 0, 255);
        return -16777216 | i << 16 | i << 8 | i;
    }

    private float getFaceBrightness(EnumFacing p_178412_1_)
    {
        switch (FaceBakery.SwitchEnumFacing.field_178400_a[p_178412_1_.ordinal()])
        {
            case 1:
                return 0.5F;
            case 2:
                return 1.0F;
            case 3:
            case 4:
                return 0.8F;
            case 5:
            case 6:
                return 0.6F;
            default:
                return 1.0F;
        }
    }

    private float[] getPositionsDiv16(Vector3f pos1, Vector3f pos2)
    {
        float[] afloat = new float[EnumFacing.values().length];
        afloat[EnumFaceDirection.Constants.field_179176_f] = pos1.x / 16.0F;
        afloat[EnumFaceDirection.Constants.field_179178_e] = pos1.y / 16.0F;
        afloat[EnumFaceDirection.Constants.field_179177_d] = pos1.z / 16.0F;
        afloat[EnumFaceDirection.Constants.field_179180_c] = pos2.x / 16.0F;
        afloat[EnumFaceDirection.Constants.field_179179_b] = pos2.y / 16.0F;
        afloat[EnumFaceDirection.Constants.field_179181_a] = pos2.z / 16.0F;
        return afloat;
    }

    private void fillVertexData(int[] faceData, int vertexIndex, EnumFacing facing, BlockPartFace partFace, float[] p_178402_5_, TextureAtlasSprite sprite, ModelRotation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade)
    {
        fillVertexData(faceData, vertexIndex, facing, partFace, p_178402_5_, sprite, (net.minecraftforge.client.model.ITransformation)modelRotationIn, partRotation, uvLocked, shade);
    }

    private void fillVertexData(int[] faceData, int vertexIndex, EnumFacing facing, BlockPartFace partFace, float[] p_178402_5_, TextureAtlasSprite sprite, net.minecraftforge.client.model.ITransformation modelRotationIn, BlockPartRotation partRotation, boolean uvLocked, boolean shade)
    {
        EnumFacing enumfacing1 = modelRotationIn.rotate(facing);
        int j = shade ? this.getFaceShadeColor(enumfacing1) : -1;
        EnumFaceDirection.VertexInformation vertexinformation = EnumFaceDirection.getFacing(facing).func_179025_a(vertexIndex);
        Vector3d vector3d = new Vector3d((double)p_178402_5_[vertexinformation.field_179184_a], (double)p_178402_5_[vertexinformation.field_179182_b], (double)p_178402_5_[vertexinformation.field_179183_c]);
        this.func_178407_a(vector3d, partRotation);
        int k = this.rotateVertex(vector3d, facing, vertexIndex, modelRotationIn, uvLocked);
        this.storeVertexData(faceData, k, vertexIndex, vector3d, j, sprite, partFace.blockFaceUV);
    }

    private void storeVertexData(int[] faceData, int storeIndex, int vertexIndex, Vector3d position, int shadeColor, TextureAtlasSprite sprite, BlockFaceUV faceUV)
    {
        int l = storeIndex * 7;
        faceData[l] = Float.floatToRawIntBits((float)position.x);
        faceData[l + 1] = Float.floatToRawIntBits((float)position.y);
        faceData[l + 2] = Float.floatToRawIntBits((float)position.z);
        faceData[l + 3] = shadeColor;
        faceData[l + 4] = Float.floatToRawIntBits(sprite.getInterpolatedU((double)faceUV.func_178348_a(vertexIndex)));
        faceData[l + 4 + 1] = Float.floatToRawIntBits(sprite.getInterpolatedV((double)faceUV.func_178346_b(vertexIndex)));
    }

    private void func_178407_a(Vector3d p_178407_1_, BlockPartRotation p_178407_2_)
    {
        if (p_178407_2_ != null)
        {
            Matrix4d matrix4d = this.getMatrixIdentity();
            Vector3d vector3d1 = new Vector3d(0.0D, 0.0D, 0.0D);

            switch (FaceBakery.SwitchEnumFacing.field_178399_b[p_178407_2_.axis.ordinal()])
            {
                case 1:
                    matrix4d.mul(this.getMatrixRotation(new AxisAngle4d(1.0D, 0.0D, 0.0D, (double)p_178407_2_.angle * 0.017453292519943295D)));
                    vector3d1.set(0.0D, 1.0D, 1.0D);
                    break;
                case 2:
                    matrix4d.mul(this.getMatrixRotation(new AxisAngle4d(0.0D, 1.0D, 0.0D, (double)p_178407_2_.angle * 0.017453292519943295D)));
                    vector3d1.set(1.0D, 0.0D, 1.0D);
                    break;
                case 3:
                    matrix4d.mul(this.getMatrixRotation(new AxisAngle4d(0.0D, 0.0D, 1.0D, (double)p_178407_2_.angle * 0.017453292519943295D)));
                    vector3d1.set(1.0D, 1.0D, 0.0D);
            }

            if (p_178407_2_.rescale)
            {
                if (Math.abs(p_178407_2_.angle) == 22.5F)
                {
                    vector3d1.scale(field_178418_a);
                }
                else
                {
                    vector3d1.scale(field_178417_b);
                }

                vector3d1.add(new Vector3d(1.0D, 1.0D, 1.0D));
            }
            else
            {
                vector3d1.set(new Vector3d(1.0D, 1.0D, 1.0D));
            }

            this.rotateScale(p_178407_1_, new Vector3d(p_178407_2_.origin), matrix4d, vector3d1);
        }
    }

    public int rotateVertex(Vector3d position, EnumFacing facing, int vertexIndex, ModelRotation modelRotationIn, boolean uvLocked)
    {
        return rotateVertex(position, facing, vertexIndex, (net.minecraftforge.client.model.ITransformation)modelRotationIn, uvLocked);
    }

    public int rotateVertex(Vector3d position, EnumFacing facing, int vertexIndex, net.minecraftforge.client.model.ITransformation modelRotationIn, boolean uvLocked)
    {
        if (modelRotationIn == ModelRotation.X0_Y0)
        {
            return vertexIndex;
        }
        else
        {
            net.minecraftforge.client.ForgeHooksClient.transform(position, modelRotationIn.getMatrix());
            return modelRotationIn.rotate(facing, vertexIndex);
        }
    }

    private void rotateScale(Vector3d position, Vector3d rotationOrigin, Matrix4d rotationMatrix, Vector3d scale)
    {
        position.sub(rotationOrigin);
        rotationMatrix.transform(position);
        position.x *= scale.x;
        position.y *= scale.y;
        position.z *= scale.z;
        position.add(rotationOrigin);
    }

    private Matrix4d getMatrixRotation(AxisAngle4d p_178416_1_)
    {
        Matrix4d matrix4d = this.getMatrixIdentity();
        matrix4d.setRotation(p_178416_1_);
        return matrix4d;
    }

    private Matrix4d getMatrixIdentity()
    {
        Matrix4d matrix4d = new Matrix4d();
        matrix4d.setIdentity();
        return matrix4d;
    }

    public static EnumFacing getFacingFromVertexData(int[] p_178410_0_)
    {
        Vector3f vector3f = new Vector3f(Float.intBitsToFloat(p_178410_0_[0]), Float.intBitsToFloat(p_178410_0_[1]), Float.intBitsToFloat(p_178410_0_[2]));
        Vector3f vector3f1 = new Vector3f(Float.intBitsToFloat(p_178410_0_[7]), Float.intBitsToFloat(p_178410_0_[8]), Float.intBitsToFloat(p_178410_0_[9]));
        Vector3f vector3f2 = new Vector3f(Float.intBitsToFloat(p_178410_0_[14]), Float.intBitsToFloat(p_178410_0_[15]), Float.intBitsToFloat(p_178410_0_[16]));
        Vector3f vector3f3 = new Vector3f();
        Vector3f vector3f4 = new Vector3f();
        Vector3f vector3f5 = new Vector3f();
        vector3f3.sub(vector3f, vector3f1);
        vector3f4.sub(vector3f2, vector3f1);
        vector3f5.cross(vector3f4, vector3f3);
        vector3f5.normalize();
        EnumFacing enumfacing = null;
        float f = 0.0F;
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j)
        {
            EnumFacing enumfacing1 = aenumfacing[j];
            Vec3i vec3i = enumfacing1.getDirectionVec();
            Vector3f vector3f6 = new Vector3f((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            float f1 = vector3f5.dot(vector3f6);

            if (f1 >= 0.0F && f1 > f)
            {
                f = f1;
                enumfacing = enumfacing1;
            }
        }

        if (enumfacing == null)
        {
            return EnumFacing.UP;
        }
        else
        {
            return enumfacing;
        }
    }

    public void func_178409_a(int[] p_178409_1_, EnumFacing p_178409_2_, BlockFaceUV p_178409_3_, TextureAtlasSprite p_178409_4_)
    {
        for (int i = 0; i < 4; ++i)
        {
            this.func_178401_a(i, p_178409_1_, p_178409_2_, p_178409_3_, p_178409_4_);
        }
    }

    private void func_178408_a(int[] p_178408_1_, EnumFacing p_178408_2_)
    {
        int[] aint1 = new int[p_178408_1_.length];
        System.arraycopy(p_178408_1_, 0, aint1, 0, p_178408_1_.length);
        float[] afloat = new float[EnumFacing.values().length];
        afloat[EnumFaceDirection.Constants.field_179176_f] = 999.0F;
        afloat[EnumFaceDirection.Constants.field_179178_e] = 999.0F;
        afloat[EnumFaceDirection.Constants.field_179177_d] = 999.0F;
        afloat[EnumFaceDirection.Constants.field_179180_c] = -999.0F;
        afloat[EnumFaceDirection.Constants.field_179179_b] = -999.0F;
        afloat[EnumFaceDirection.Constants.field_179181_a] = -999.0F;
        int j;
        float f2;

        for (int i = 0; i < 4; ++i)
        {
            j = 7 * i;
            float f = Float.intBitsToFloat(aint1[j]);
            float f1 = Float.intBitsToFloat(aint1[j + 1]);
            f2 = Float.intBitsToFloat(aint1[j + 2]);

            if (f < afloat[EnumFaceDirection.Constants.field_179176_f])
            {
                afloat[EnumFaceDirection.Constants.field_179176_f] = f;
            }

            if (f1 < afloat[EnumFaceDirection.Constants.field_179178_e])
            {
                afloat[EnumFaceDirection.Constants.field_179178_e] = f1;
            }

            if (f2 < afloat[EnumFaceDirection.Constants.field_179177_d])
            {
                afloat[EnumFaceDirection.Constants.field_179177_d] = f2;
            }

            if (f > afloat[EnumFaceDirection.Constants.field_179180_c])
            {
                afloat[EnumFaceDirection.Constants.field_179180_c] = f;
            }

            if (f1 > afloat[EnumFaceDirection.Constants.field_179179_b])
            {
                afloat[EnumFaceDirection.Constants.field_179179_b] = f1;
            }

            if (f2 > afloat[EnumFaceDirection.Constants.field_179181_a])
            {
                afloat[EnumFaceDirection.Constants.field_179181_a] = f2;
            }
        }

        EnumFaceDirection enumfacedirection = EnumFaceDirection.getFacing(p_178408_2_);

        for (j = 0; j < 4; ++j)
        {
            int i1 = 7 * j;
            EnumFaceDirection.VertexInformation vertexinformation = enumfacedirection.func_179025_a(j);
            f2 = afloat[vertexinformation.field_179184_a];
            float f3 = afloat[vertexinformation.field_179182_b];
            float f4 = afloat[vertexinformation.field_179183_c];
            p_178408_1_[i1] = Float.floatToRawIntBits(f2);
            p_178408_1_[i1 + 1] = Float.floatToRawIntBits(f3);
            p_178408_1_[i1 + 2] = Float.floatToRawIntBits(f4);

            for (int k = 0; k < 4; ++k)
            {
                int l = 7 * k;
                float f5 = Float.intBitsToFloat(aint1[l]);
                float f6 = Float.intBitsToFloat(aint1[l + 1]);
                float f7 = Float.intBitsToFloat(aint1[l + 2]);

                if (MathHelper.func_180185_a(f2, f5) && MathHelper.func_180185_a(f3, f6) && MathHelper.func_180185_a(f4, f7))
                {
                    p_178408_1_[i1 + 4] = aint1[l + 4];
                    p_178408_1_[i1 + 4 + 1] = aint1[l + 4 + 1];
                }
            }
        }
    }

    private void func_178401_a(int p_178401_1_, int[] p_178401_2_, EnumFacing p_178401_3_, BlockFaceUV p_178401_4_, TextureAtlasSprite p_178401_5_)
    {
        int j = 7 * p_178401_1_;
        float f = Float.intBitsToFloat(p_178401_2_[j]);
        float f1 = Float.intBitsToFloat(p_178401_2_[j + 1]);
        float f2 = Float.intBitsToFloat(p_178401_2_[j + 2]);

        if (f < -0.1F || f >= 1.1F)
        {
            f -= (float)MathHelper.floor_float(f);
        }

        if (f1 < -0.1F || f1 >= 1.1F)
        {
            f1 -= (float)MathHelper.floor_float(f1);
        }

        if (f2 < -0.1F || f2 >= 1.1F)
        {
            f2 -= (float)MathHelper.floor_float(f2);
        }

        float f3 = 0.0F;
        float f4 = 0.0F;

        switch (FaceBakery.SwitchEnumFacing.field_178400_a[p_178401_3_.ordinal()])
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

        int k = p_178401_4_.func_178345_c(p_178401_1_) * 7;
        p_178401_2_[k + 4] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedU((double)f3));
        p_178401_2_[k + 4 + 1] = Float.floatToRawIntBits(p_178401_5_.getInterpolatedV((double)f4));
    }

    @SideOnly(Side.CLIENT)

    static final class SwitchEnumFacing
        {
            static final int[] field_178400_a;

            static final int[] field_178399_b = new int[EnumFacing.Axis.values().length];
            private static final String __OBFID = "CL_00002489";

            static
            {
                try
                {
                    field_178399_b[EnumFacing.Axis.X.ordinal()] = 1;
                }
                catch (NoSuchFieldError var9)
                {
                    ;
                }

                try
                {
                    field_178399_b[EnumFacing.Axis.Y.ordinal()] = 2;
                }
                catch (NoSuchFieldError var8)
                {
                    ;
                }

                try
                {
                    field_178399_b[EnumFacing.Axis.Z.ordinal()] = 3;
                }
                catch (NoSuchFieldError var7)
                {
                    ;
                }

                field_178400_a = new int[EnumFacing.values().length];

                try
                {
                    field_178400_a[EnumFacing.DOWN.ordinal()] = 1;
                }
                catch (NoSuchFieldError var6)
                {
                    ;
                }

                try
                {
                    field_178400_a[EnumFacing.UP.ordinal()] = 2;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                try
                {
                    field_178400_a[EnumFacing.NORTH.ordinal()] = 3;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    field_178400_a[EnumFacing.SOUTH.ordinal()] = 4;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_178400_a[EnumFacing.WEST.ordinal()] = 5;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_178400_a[EnumFacing.EAST.ordinal()] = 6;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}