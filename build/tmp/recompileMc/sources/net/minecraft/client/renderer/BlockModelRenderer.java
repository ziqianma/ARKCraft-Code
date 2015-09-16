package net.minecraft.client.renderer;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import static net.minecraft.client.renderer.BlockModelRenderer.Orientation.*;

@SideOnly(Side.CLIENT)
public class BlockModelRenderer
{
    private static final String __OBFID = "CL_00002518";

    public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn)
    {
        Block block = blockStateIn.getBlock();
        block.setBlockBoundsBasedOnState(blockAccessIn, blockPosIn);
        return this.renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, worldRendererIn, true);
    }

    public boolean renderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides)
    {
        boolean flag1 = Minecraft.isAmbientOcclusionEnabled() && blockStateIn.getBlock().getLightValue() == 0 && modelIn.isAmbientOcclusion();

        try
        {
            Block block = blockStateIn.getBlock();
            return flag1 ? this.renderModelAmbientOcclusion(blockAccessIn, modelIn, block, blockPosIn, worldRendererIn, checkSides) : this.renderModelStandard(blockAccessIn, modelIn, block, blockPosIn, worldRendererIn, checkSides);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block model");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, blockPosIn, blockStateIn);
            crashreportcategory.addCrashSection("Using AO", Boolean.valueOf(flag1));
            throw new ReportedException(crashreport);
        }
    }

    public boolean renderModelAmbientOcclusion(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides)
    {
        boolean flag1 = false;
        worldRendererIn.setBrightness(983055);
        float[] afloat = new float[EnumFacing.values().length * 2];
        BitSet bitset = new BitSet(3);
        BlockModelRenderer.AmbientOcclusionFace ambientocclusionface = new BlockModelRenderer.AmbientOcclusionFace();
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j)
        {
            EnumFacing enumfacing = aenumfacing[j];
            List list = modelIn.getFaceQuads(enumfacing);

            if (!list.isEmpty())
            {
                BlockPos blockpos1 = blockPosIn.offset(enumfacing);

                if (!checkSides || blockIn.shouldSideBeRendered(blockAccessIn, blockpos1, enumfacing))
                {
                    this.renderModelAmbientOcclusionQuads(blockAccessIn, blockIn, blockPosIn, worldRendererIn, list, afloat, bitset, ambientocclusionface);
                    flag1 = true;
                }
            }
        }

        List list1 = modelIn.getGeneralQuads();

        if (list1.size() > 0)
        {
            this.renderModelAmbientOcclusionQuads(blockAccessIn, blockIn, blockPosIn, worldRendererIn, list1, afloat, bitset, ambientocclusionface);
            flag1 = true;
        }

        return flag1;
    }

    public boolean renderModelStandard(IBlockAccess blockAccessIn, IBakedModel modelIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, boolean checkSides)
    {
        boolean flag1 = false;
        BitSet bitset = new BitSet(3);
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j)
        {
            EnumFacing enumfacing = aenumfacing[j];
            List list = modelIn.getFaceQuads(enumfacing);

            if (!list.isEmpty())
            {
                BlockPos blockpos1 = blockPosIn.offset(enumfacing);

                if (!checkSides || blockIn.shouldSideBeRendered(blockAccessIn, blockpos1, enumfacing))
                {
                    int k = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos1);
                    this.renderModelStandardQuads(blockAccessIn, blockIn, blockPosIn, enumfacing, k, false, worldRendererIn, list, bitset);
                    flag1 = true;
                }
            }
        }

        List list1 = modelIn.getGeneralQuads();

        if (list1.size() > 0)
        {
            this.renderModelStandardQuads(blockAccessIn, blockIn, blockPosIn, (EnumFacing)null, -1, true, worldRendererIn, list1, bitset);
            flag1 = true;
        }

        return flag1;
    }

    private void renderModelAmbientOcclusionQuads(IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, WorldRenderer worldRendererIn, List listQuadsIn, float[] quadBounds, BitSet boundsFlags, BlockModelRenderer.AmbientOcclusionFace aoFaceIn)
    {
        double d0 = (double)blockPosIn.getX();
        double d1 = (double)blockPosIn.getY();
        double d2 = (double)blockPosIn.getZ();
        Block.EnumOffsetType enumoffsettype = blockIn.getOffsetType();

        if (enumoffsettype != Block.EnumOffsetType.NONE)
        {
            long i = MathHelper.getPositionRandom(blockPosIn);
            d0 += ((double)((float)(i >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
            d2 += ((double)((float)(i >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;

            if (enumoffsettype == Block.EnumOffsetType.XYZ)
            {
                d1 += ((double)((float)(i >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
            }
        }

        for (Iterator iterator = listQuadsIn.iterator(); iterator.hasNext(); worldRendererIn.putPosition(d0, d1, d2))
        {
            BakedQuad bakedquad = (BakedQuad)iterator.next();
            this.fillQuadBounds(blockIn, bakedquad.getVertexData(), bakedquad.getFace(), quadBounds, boundsFlags);
            aoFaceIn.updateVertexBrightness(blockAccessIn, blockIn, blockPosIn, bakedquad.getFace(), quadBounds, boundsFlags);
            worldRendererIn.addVertexData(bakedquad.getVertexData());
            worldRendererIn.putBrightness4(aoFaceIn.vertexBrightness[0], aoFaceIn.vertexBrightness[1], aoFaceIn.vertexBrightness[2], aoFaceIn.vertexBrightness[3]);

            if (bakedquad.hasTintIndex())
            {
                int j = blockIn.colorMultiplier(blockAccessIn, blockPosIn, bakedquad.getTintIndex());

                if (EntityRenderer.anaglyphEnable)
                {
                    j = TextureUtil.anaglyphColor(j);
                }

                float f = (float)(j >> 16 & 255) / 255.0F;
                float f1 = (float)(j >> 8 & 255) / 255.0F;
                float f2 = (float)(j & 255) / 255.0F;
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[0] * f, aoFaceIn.vertexColorMultiplier[0] * f1, aoFaceIn.vertexColorMultiplier[0] * f2, 4);
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[1] * f, aoFaceIn.vertexColorMultiplier[1] * f1, aoFaceIn.vertexColorMultiplier[1] * f2, 3);
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[2] * f, aoFaceIn.vertexColorMultiplier[2] * f1, aoFaceIn.vertexColorMultiplier[2] * f2, 2);
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[3] * f, aoFaceIn.vertexColorMultiplier[3] * f1, aoFaceIn.vertexColorMultiplier[3] * f2, 1);
            }
            else
            {
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[0], aoFaceIn.vertexColorMultiplier[0], aoFaceIn.vertexColorMultiplier[0], 4);
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[1], aoFaceIn.vertexColorMultiplier[1], aoFaceIn.vertexColorMultiplier[1], 3);
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[2], aoFaceIn.vertexColorMultiplier[2], aoFaceIn.vertexColorMultiplier[2], 2);
                worldRendererIn.putColorMultiplier(aoFaceIn.vertexColorMultiplier[3], aoFaceIn.vertexColorMultiplier[3], aoFaceIn.vertexColorMultiplier[3], 1);
            }
        }
    }

    private void fillQuadBounds(Block blockIn, int[] vertexData, EnumFacing facingIn, float[] quadBounds, BitSet boundsFlags)
    {
        float f = 32.0F;
        float f1 = 32.0F;
        float f2 = 32.0F;
        float f3 = -32.0F;
        float f4 = -32.0F;
        float f5 = -32.0F;
        float f6;

        for (int i = 0; i < 4; ++i)
        {
            f6 = Float.intBitsToFloat(vertexData[i * 7]);
            float f7 = Float.intBitsToFloat(vertexData[i * 7 + 1]);
            float f8 = Float.intBitsToFloat(vertexData[i * 7 + 2]);
            f = Math.min(f, f6);
            f1 = Math.min(f1, f7);
            f2 = Math.min(f2, f8);
            f3 = Math.max(f3, f6);
            f4 = Math.max(f4, f7);
            f5 = Math.max(f5, f8);
        }

        if (quadBounds != null)
        {
            quadBounds[EnumFacing.WEST.getIndex()] = f;
            quadBounds[EnumFacing.EAST.getIndex()] = f3;
            quadBounds[EnumFacing.DOWN.getIndex()] = f1;
            quadBounds[EnumFacing.UP.getIndex()] = f4;
            quadBounds[EnumFacing.NORTH.getIndex()] = f2;
            quadBounds[EnumFacing.SOUTH.getIndex()] = f5;
            quadBounds[EnumFacing.WEST.getIndex() + EnumFacing.values().length] = 1.0F - f;
            quadBounds[EnumFacing.EAST.getIndex() + EnumFacing.values().length] = 1.0F - f3;
            quadBounds[EnumFacing.DOWN.getIndex() + EnumFacing.values().length] = 1.0F - f1;
            quadBounds[EnumFacing.UP.getIndex() + EnumFacing.values().length] = 1.0F - f4;
            quadBounds[EnumFacing.NORTH.getIndex() + EnumFacing.values().length] = 1.0F - f2;
            quadBounds[EnumFacing.SOUTH.getIndex() + EnumFacing.values().length] = 1.0F - f5;
        }

        float f9 = 1.0E-4F;
        f6 = 0.9999F;

        switch (BlockModelRenderer.SwitchEnumFacing.field_178290_a[facingIn.ordinal()])
        {
            case 1:
                boundsFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
                boundsFlags.set(0, (f1 < 1.0E-4F || blockIn.isFullCube()) && f1 == f4);
                break;
            case 2:
                boundsFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
                boundsFlags.set(0, (f4 > 0.9999F || blockIn.isFullCube()) && f1 == f4);
                break;
            case 3:
                boundsFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
                boundsFlags.set(0, (f2 < 1.0E-4F || blockIn.isFullCube()) && f2 == f5);
                break;
            case 4:
                boundsFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
                boundsFlags.set(0, (f5 > 0.9999F || blockIn.isFullCube()) && f2 == f5);
                break;
            case 5:
                boundsFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
                boundsFlags.set(0, (f < 1.0E-4F || blockIn.isFullCube()) && f == f3);
                break;
            case 6:
                boundsFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
                boundsFlags.set(0, (f3 > 0.9999F || blockIn.isFullCube()) && f == f3);
        }
    }

    private void renderModelStandardQuads(IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, EnumFacing faceIn, int brightnessIn, boolean ownBrightness, WorldRenderer worldRendererIn, List listQuadsIn, BitSet boundsFlags)
    {
        double d0 = (double)blockPosIn.getX();
        double d1 = (double)blockPosIn.getY();
        double d2 = (double)blockPosIn.getZ();
        Block.EnumOffsetType enumoffsettype = blockIn.getOffsetType();

        if (enumoffsettype != Block.EnumOffsetType.NONE)
        {
            int j = blockPosIn.getX();
            int k = blockPosIn.getZ();
            long l = (long)(j * 3129871) ^ (long)k * 116129781L;
            l = l * l * 42317861L + l * 11L;
            d0 += ((double)((float)(l >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
            d2 += ((double)((float)(l >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;

            if (enumoffsettype == Block.EnumOffsetType.XYZ)
            {
                d1 += ((double)((float)(l >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
            }
        }

        for (Iterator iterator = listQuadsIn.iterator(); iterator.hasNext(); worldRendererIn.putPosition(d0, d1, d2))
        {
            BakedQuad bakedquad = (BakedQuad)iterator.next();

            if (ownBrightness)
            {
                this.fillQuadBounds(blockIn, bakedquad.getVertexData(), bakedquad.getFace(), (float[])null, boundsFlags);
                brightnessIn = boundsFlags.get(0) ? blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn.offset(bakedquad.getFace())) : blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn);
            }

            worldRendererIn.addVertexData(bakedquad.getVertexData());
            worldRendererIn.putBrightness4(brightnessIn, brightnessIn, brightnessIn, brightnessIn);

            if (bakedquad.hasTintIndex())
            {
                int i1 = blockIn.colorMultiplier(blockAccessIn, blockPosIn, bakedquad.getTintIndex());

                if (EntityRenderer.anaglyphEnable)
                {
                    i1 = TextureUtil.anaglyphColor(i1);
                }

                float f = (float)(i1 >> 16 & 255) / 255.0F;
                float f1 = (float)(i1 >> 8 & 255) / 255.0F;
                float f2 = (float)(i1 & 255) / 255.0F;
                worldRendererIn.putColorMultiplier(f, f1, f2, 4);
                worldRendererIn.putColorMultiplier(f, f1, f2, 3);
                worldRendererIn.putColorMultiplier(f, f1, f2, 2);
                worldRendererIn.putColorMultiplier(f, f1, f2, 1);
            }
        }
    }

    public void renderModelBrightnessColor(IBakedModel p_178262_1_, float p_178262_2_, float p_178262_3_, float p_178262_4_, float p_178262_5_)
    {
        EnumFacing[] aenumfacing = EnumFacing.values();
        int i = aenumfacing.length;

        for (int j = 0; j < i; ++j)
        {
            EnumFacing enumfacing = aenumfacing[j];
            this.renderModelBrightnessColorQuads(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, p_178262_1_.getFaceQuads(enumfacing));
        }

        this.renderModelBrightnessColorQuads(p_178262_2_, p_178262_3_, p_178262_4_, p_178262_5_, p_178262_1_.getGeneralQuads());
    }

    public void renderModelBrightness(IBakedModel p_178266_1_, IBlockState p_178266_2_, float p_178266_3_, boolean p_178266_4_)
    {
        Block block = p_178266_2_.getBlock();
        block.setBlockBoundsForItemRender();
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        int i = block.getRenderColor(block.getStateForEntityRender(p_178266_2_));

        if (EntityRenderer.anaglyphEnable)
        {
            i = TextureUtil.anaglyphColor(i);
        }

        float f1 = (float)(i >> 16 & 255) / 255.0F;
        float f2 = (float)(i >> 8 & 255) / 255.0F;
        float f3 = (float)(i & 255) / 255.0F;

        if (!p_178266_4_)
        {
            GlStateManager.color(p_178266_3_, p_178266_3_, p_178266_3_, 1.0F);
        }

        this.renderModelBrightnessColor(p_178266_1_, p_178266_3_, f1, f2, f3);
    }

    private void renderModelBrightnessColorQuads(float p_178264_1_, float p_178264_2_, float p_178264_3_, float p_178264_4_, List p_178264_5_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        Iterator iterator = p_178264_5_.iterator();

        while (iterator.hasNext())
        {
            BakedQuad bakedquad = (BakedQuad)iterator.next();
            worldrenderer.startDrawingQuads();
            worldrenderer.setVertexFormat(DefaultVertexFormats.ITEM);
            worldrenderer.addVertexData(bakedquad.getVertexData());

            if (bakedquad.hasTintIndex())
            {
                worldrenderer.putColorRGB_F4(p_178264_2_ * p_178264_1_, p_178264_3_ * p_178264_1_, p_178264_4_ * p_178264_1_);
            }
            else
            {
                worldrenderer.putColorRGB_F4(p_178264_1_, p_178264_1_, p_178264_1_);
            }

            Vec3i vec3i = bakedquad.getFace().getDirectionVec();
            worldrenderer.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            tessellator.draw();
        }
    }

    @SideOnly(Side.CLIENT)
    class AmbientOcclusionFace
    {
        private final float[] vertexColorMultiplier = new float[4];
        private final int[] vertexBrightness = new int[4];
        private static final String __OBFID = "CL_00002515";

        public void updateVertexBrightness(IBlockAccess blockAccessIn, Block blockIn, BlockPos blockPosIn, EnumFacing facingIn, float[] quadBounds, BitSet boundsFlags)
        {
            BlockPos blockpos1 = boundsFlags.get(0) ? blockPosIn.offset(facingIn) : blockPosIn;
            BlockModelRenderer.EnumNeighborInfo enumneighborinfo = BlockModelRenderer.EnumNeighborInfo.getNeighbourInfo(facingIn);
            BlockPos blockpos2 = blockpos1.offset(enumneighborinfo.field_178276_g[0]);
            BlockPos blockpos3 = blockpos1.offset(enumneighborinfo.field_178276_g[1]);
            BlockPos blockpos4 = blockpos1.offset(enumneighborinfo.field_178276_g[2]);
            BlockPos blockpos5 = blockpos1.offset(enumneighborinfo.field_178276_g[3]);
            int i = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos2);
            int j = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos3);
            int k = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos4);
            int l = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos5);
            float f = blockAccessIn.getBlockState(blockpos2).getBlock().getAmbientOcclusionLightValue();
            float f1 = blockAccessIn.getBlockState(blockpos3).getBlock().getAmbientOcclusionLightValue();
            float f2 = blockAccessIn.getBlockState(blockpos4).getBlock().getAmbientOcclusionLightValue();
            float f3 = blockAccessIn.getBlockState(blockpos5).getBlock().getAmbientOcclusionLightValue();
            boolean flag = blockAccessIn.getBlockState(blockpos2.offset(facingIn)).getBlock().isTranslucent();
            boolean flag1 = blockAccessIn.getBlockState(blockpos3.offset(facingIn)).getBlock().isTranslucent();
            boolean flag2 = blockAccessIn.getBlockState(blockpos4.offset(facingIn)).getBlock().isTranslucent();
            boolean flag3 = blockAccessIn.getBlockState(blockpos5.offset(facingIn)).getBlock().isTranslucent();
            float f4;
            int i1;
            BlockPos blockpos6;

            if (!flag2 && !flag)
            {
                f4 = f;
                i1 = i;
            }
            else
            {
                blockpos6 = blockpos2.offset(enumneighborinfo.field_178276_g[2]);
                f4 = blockAccessIn.getBlockState(blockpos6).getBlock().getAmbientOcclusionLightValue();
                i1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos6);
            }

            float f5;
            int j1;

            if (!flag3 && !flag)
            {
                f5 = f;
                j1 = i;
            }
            else
            {
                blockpos6 = blockpos2.offset(enumneighborinfo.field_178276_g[3]);
                f5 = blockAccessIn.getBlockState(blockpos6).getBlock().getAmbientOcclusionLightValue();
                j1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos6);
            }

            float f6;
            int k1;

            if (!flag2 && !flag1)
            {
                f6 = f1;
                k1 = j;
            }
            else
            {
                blockpos6 = blockpos3.offset(enumneighborinfo.field_178276_g[2]);
                f6 = blockAccessIn.getBlockState(blockpos6).getBlock().getAmbientOcclusionLightValue();
                k1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos6);
            }

            float f7;
            int l1;

            if (!flag3 && !flag1)
            {
                f7 = f1;
                l1 = j;
            }
            else
            {
                blockpos6 = blockpos3.offset(enumneighborinfo.field_178276_g[3]);
                f7 = blockAccessIn.getBlockState(blockpos6).getBlock().getAmbientOcclusionLightValue();
                l1 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockpos6);
            }

            int i3 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn);

            if (boundsFlags.get(0) || !blockAccessIn.getBlockState(blockPosIn.offset(facingIn)).getBlock().isOpaqueCube())
            {
                i3 = blockIn.getMixedBrightnessForBlock(blockAccessIn, blockPosIn.offset(facingIn));
            }

            float f8 = boundsFlags.get(0) ? blockAccessIn.getBlockState(blockpos1).getBlock().getAmbientOcclusionLightValue() : blockAccessIn.getBlockState(blockPosIn).getBlock().getAmbientOcclusionLightValue();
            BlockModelRenderer.VertexTranslations vertextranslations = BlockModelRenderer.VertexTranslations.getVertexTranslations(facingIn);
            float f9;
            float f10;
            float f11;
            float f12;

            if (boundsFlags.get(1) && enumneighborinfo.field_178289_i)
            {
                f9 = (f3 + f + f5 + f8) * 0.25F;
                f10 = (f2 + f + f4 + f8) * 0.25F;
                f11 = (f2 + f1 + f6 + f8) * 0.25F;
                f12 = (f3 + f1 + f7 + f8) * 0.25F;
                float f13 = quadBounds[enumneighborinfo.field_178286_j[0].field_178229_m] * quadBounds[enumneighborinfo.field_178286_j[1].field_178229_m];
                float f14 = quadBounds[enumneighborinfo.field_178286_j[2].field_178229_m] * quadBounds[enumneighborinfo.field_178286_j[3].field_178229_m];
                float f15 = quadBounds[enumneighborinfo.field_178286_j[4].field_178229_m] * quadBounds[enumneighborinfo.field_178286_j[5].field_178229_m];
                float f16 = quadBounds[enumneighborinfo.field_178286_j[6].field_178229_m] * quadBounds[enumneighborinfo.field_178286_j[7].field_178229_m];
                float f17 = quadBounds[enumneighborinfo.field_178287_k[0].field_178229_m] * quadBounds[enumneighborinfo.field_178287_k[1].field_178229_m];
                float f18 = quadBounds[enumneighborinfo.field_178287_k[2].field_178229_m] * quadBounds[enumneighborinfo.field_178287_k[3].field_178229_m];
                float f19 = quadBounds[enumneighborinfo.field_178287_k[4].field_178229_m] * quadBounds[enumneighborinfo.field_178287_k[5].field_178229_m];
                float f20 = quadBounds[enumneighborinfo.field_178287_k[6].field_178229_m] * quadBounds[enumneighborinfo.field_178287_k[7].field_178229_m];
                float f21 = quadBounds[enumneighborinfo.field_178284_l[0].field_178229_m] * quadBounds[enumneighborinfo.field_178284_l[1].field_178229_m];
                float f22 = quadBounds[enumneighborinfo.field_178284_l[2].field_178229_m] * quadBounds[enumneighborinfo.field_178284_l[3].field_178229_m];
                float f23 = quadBounds[enumneighborinfo.field_178284_l[4].field_178229_m] * quadBounds[enumneighborinfo.field_178284_l[5].field_178229_m];
                float f24 = quadBounds[enumneighborinfo.field_178284_l[6].field_178229_m] * quadBounds[enumneighborinfo.field_178284_l[7].field_178229_m];
                float f25 = quadBounds[enumneighborinfo.field_178285_m[0].field_178229_m] * quadBounds[enumneighborinfo.field_178285_m[1].field_178229_m];
                float f26 = quadBounds[enumneighborinfo.field_178285_m[2].field_178229_m] * quadBounds[enumneighborinfo.field_178285_m[3].field_178229_m];
                float f27 = quadBounds[enumneighborinfo.field_178285_m[4].field_178229_m] * quadBounds[enumneighborinfo.field_178285_m[5].field_178229_m];
                float f28 = quadBounds[enumneighborinfo.field_178285_m[6].field_178229_m] * quadBounds[enumneighborinfo.field_178285_m[7].field_178229_m];
                this.vertexColorMultiplier[vertextranslations.field_178191_g] = f9 * f13 + f10 * f14 + f11 * f15 + f12 * f16;
                this.vertexColorMultiplier[vertextranslations.field_178200_h] = f9 * f17 + f10 * f18 + f11 * f19 + f12 * f20;
                this.vertexColorMultiplier[vertextranslations.field_178201_i] = f9 * f21 + f10 * f22 + f11 * f23 + f12 * f24;
                this.vertexColorMultiplier[vertextranslations.field_178198_j] = f9 * f25 + f10 * f26 + f11 * f27 + f12 * f28;
                int i2 = this.getAoBrightness(l, i, j1, i3);
                int j2 = this.getAoBrightness(k, i, i1, i3);
                int k2 = this.getAoBrightness(k, j, k1, i3);
                int l2 = this.getAoBrightness(l, j, l1, i3);
                this.vertexBrightness[vertextranslations.field_178191_g] = this.getVertexBrightness(i2, j2, k2, l2, f13, f14, f15, f16);
                this.vertexBrightness[vertextranslations.field_178200_h] = this.getVertexBrightness(i2, j2, k2, l2, f17, f18, f19, f20);
                this.vertexBrightness[vertextranslations.field_178201_i] = this.getVertexBrightness(i2, j2, k2, l2, f21, f22, f23, f24);
                this.vertexBrightness[vertextranslations.field_178198_j] = this.getVertexBrightness(i2, j2, k2, l2, f25, f26, f27, f28);
            }
            else
            {
                f9 = (f3 + f + f5 + f8) * 0.25F;
                f10 = (f2 + f + f4 + f8) * 0.25F;
                f11 = (f2 + f1 + f6 + f8) * 0.25F;
                f12 = (f3 + f1 + f7 + f8) * 0.25F;
                this.vertexBrightness[vertextranslations.field_178191_g] = this.getAoBrightness(l, i, j1, i3);
                this.vertexBrightness[vertextranslations.field_178200_h] = this.getAoBrightness(k, i, i1, i3);
                this.vertexBrightness[vertextranslations.field_178201_i] = this.getAoBrightness(k, j, k1, i3);
                this.vertexBrightness[vertextranslations.field_178198_j] = this.getAoBrightness(l, j, l1, i3);
                this.vertexColorMultiplier[vertextranslations.field_178191_g] = f9;
                this.vertexColorMultiplier[vertextranslations.field_178200_h] = f10;
                this.vertexColorMultiplier[vertextranslations.field_178201_i] = f11;
                this.vertexColorMultiplier[vertextranslations.field_178198_j] = f12;
            }
        }

        /**
         * Get ambient occlusion brightness
         */
        private int getAoBrightness(int p_147778_1_, int p_147778_2_, int p_147778_3_, int p_147778_4_)
        {
            if (p_147778_1_ == 0)
            {
                p_147778_1_ = p_147778_4_;
            }

            if (p_147778_2_ == 0)
            {
                p_147778_2_ = p_147778_4_;
            }

            if (p_147778_3_ == 0)
            {
                p_147778_3_ = p_147778_4_;
            }

            return p_147778_1_ + p_147778_2_ + p_147778_3_ + p_147778_4_ >> 2 & 16711935;
        }

        private int getVertexBrightness(int p_178203_1_, int p_178203_2_, int p_178203_3_, int p_178203_4_, float p_178203_5_, float p_178203_6_, float p_178203_7_, float p_178203_8_)
        {
            int i1 = (int)((float)(p_178203_1_ >> 16 & 255) * p_178203_5_ + (float)(p_178203_2_ >> 16 & 255) * p_178203_6_ + (float)(p_178203_3_ >> 16 & 255) * p_178203_7_ + (float)(p_178203_4_ >> 16 & 255) * p_178203_8_) & 255;
            int j1 = (int)((float)(p_178203_1_ & 255) * p_178203_5_ + (float)(p_178203_2_ & 255) * p_178203_6_ + (float)(p_178203_3_ & 255) * p_178203_7_ + (float)(p_178203_4_ & 255) * p_178203_8_) & 255;
            return i1 << 16 | j1;
        }
    }

    @SideOnly(Side.CLIENT)
    public static enum EnumNeighborInfo
    {
        // -- Forge Patch to Fix Top/Bottom Lighting Interpolation --
        // Forge PR - https://github.com/MinecraftForge/MinecraftForge/pull/1892
        // Mojang Bug - https://bugs.mojang.com/browse/MC-80148
        DOWN( new EnumFacing[]{ EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.5F, true,
            new Orientation[]{ FLIP_WEST, Orientation.SOUTH, FLIP_WEST, FLIP_SOUTH, Orientation.WEST, FLIP_SOUTH, Orientation.WEST, Orientation.SOUTH },
            new Orientation[]{ FLIP_WEST, Orientation.NORTH, FLIP_WEST, FLIP_NORTH, Orientation.WEST, Orientation.FLIP_NORTH, Orientation.WEST, Orientation.NORTH },
            new Orientation[]{ FLIP_EAST, Orientation.NORTH, FLIP_EAST, FLIP_NORTH, Orientation.EAST, Orientation.FLIP_NORTH, Orientation.EAST, Orientation.NORTH },
            new Orientation[]{ FLIP_EAST, Orientation.SOUTH, FLIP_EAST, FLIP_SOUTH, Orientation.EAST, FLIP_SOUTH, Orientation.EAST, Orientation.SOUTH } ),
        UP( new EnumFacing[]{ EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH }, 1.0F, true,
            new Orientation[]{ Orientation.EAST, Orientation.SOUTH, Orientation.EAST, FLIP_SOUTH, FLIP_EAST, FLIP_SOUTH, FLIP_EAST, Orientation.SOUTH },
            new Orientation[]{ Orientation.EAST, Orientation.NORTH, Orientation.EAST, FLIP_NORTH, FLIP_EAST, FLIP_NORTH, FLIP_EAST, Orientation.NORTH },
            new Orientation[]{ Orientation.WEST, Orientation.NORTH, Orientation.WEST, FLIP_NORTH, FLIP_WEST, FLIP_NORTH, FLIP_WEST, Orientation.NORTH },
            new Orientation[]{ Orientation.WEST, Orientation.SOUTH, Orientation.WEST, FLIP_SOUTH, FLIP_WEST, FLIP_SOUTH, FLIP_WEST, Orientation.SOUTH } ),
        NORTH(new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST}, 0.8F, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_WEST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_EAST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_EAST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_WEST}),
        SOUTH(new EnumFacing[]{EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP}, 0.8F, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.WEST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_WEST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.WEST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.WEST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.EAST}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_EAST, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.EAST, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.EAST}),
        WEST(new EnumFacing[]{EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.6F, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.SOUTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.SOUTH}),
        EAST(new EnumFacing[]{EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH}, 0.6F, true, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.SOUTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.FLIP_DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.DOWN, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.NORTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_NORTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.NORTH}, new BlockModelRenderer.Orientation[]{BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.SOUTH, BlockModelRenderer.Orientation.FLIP_UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.FLIP_SOUTH, BlockModelRenderer.Orientation.UP, BlockModelRenderer.Orientation.SOUTH});
        protected final EnumFacing[] field_178276_g;
        protected final float field_178288_h;
        protected final boolean field_178289_i;
        protected final BlockModelRenderer.Orientation[] field_178286_j;
        protected final BlockModelRenderer.Orientation[] field_178287_k;
        protected final BlockModelRenderer.Orientation[] field_178284_l;
        protected final BlockModelRenderer.Orientation[] field_178285_m;
        private static final BlockModelRenderer.EnumNeighborInfo[] field_178282_n = new BlockModelRenderer.EnumNeighborInfo[6];

        private static final String __OBFID = "CL_00002516";

        private EnumNeighborInfo(EnumFacing[] p_i46236_3_, float p_i46236_4_, boolean p_i46236_5_, BlockModelRenderer.Orientation[] p_i46236_6_, BlockModelRenderer.Orientation[] p_i46236_7_, BlockModelRenderer.Orientation[] p_i46236_8_, BlockModelRenderer.Orientation[] p_i46236_9_)
        {
            this.field_178276_g = p_i46236_3_;
            this.field_178288_h = p_i46236_4_;
            this.field_178289_i = p_i46236_5_;
            this.field_178286_j = p_i46236_6_;
            this.field_178287_k = p_i46236_7_;
            this.field_178284_l = p_i46236_8_;
            this.field_178285_m = p_i46236_9_;
        }

        public static BlockModelRenderer.EnumNeighborInfo getNeighbourInfo(EnumFacing p_178273_0_)
        {
            return field_178282_n[p_178273_0_.getIndex()];
        }

        static
        {
            field_178282_n[EnumFacing.DOWN.getIndex()] = DOWN;
            field_178282_n[EnumFacing.UP.getIndex()] = UP;
            field_178282_n[EnumFacing.NORTH.getIndex()] = NORTH;
            field_178282_n[EnumFacing.SOUTH.getIndex()] = SOUTH;
            field_178282_n[EnumFacing.WEST.getIndex()] = WEST;
            field_178282_n[EnumFacing.EAST.getIndex()] = EAST;
        }
    }

    @SideOnly(Side.CLIENT)
    public static enum Orientation
    {
        DOWN(EnumFacing.DOWN, false),
        UP(EnumFacing.UP, false),
        NORTH(EnumFacing.NORTH, false),
        SOUTH(EnumFacing.SOUTH, false),
        WEST(EnumFacing.WEST, false),
        EAST(EnumFacing.EAST, false),
        FLIP_DOWN(EnumFacing.DOWN, true),
        FLIP_UP(EnumFacing.UP, true),
        FLIP_NORTH(EnumFacing.NORTH, true),
        FLIP_SOUTH(EnumFacing.SOUTH, true),
        FLIP_WEST(EnumFacing.WEST, true),
        FLIP_EAST(EnumFacing.EAST, true);
        protected final int field_178229_m;

        private static final String __OBFID = "CL_00002513";

        private Orientation(EnumFacing p_i46233_3_, boolean p_i46233_4_)
        {
            this.field_178229_m = p_i46233_3_.getIndex() + (p_i46233_4_ ? EnumFacing.values().length : 0);
        }
    }

    @SideOnly(Side.CLIENT)

    static final class SwitchEnumFacing
        {
            static final int[] field_178290_a = new int[EnumFacing.values().length];
            private static final String __OBFID = "CL_00002517";

            static
            {
                try
                {
                    field_178290_a[EnumFacing.DOWN.ordinal()] = 1;
                }
                catch (NoSuchFieldError var6)
                {
                    ;
                }

                try
                {
                    field_178290_a[EnumFacing.UP.ordinal()] = 2;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                try
                {
                    field_178290_a[EnumFacing.NORTH.ordinal()] = 3;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    field_178290_a[EnumFacing.SOUTH.ordinal()] = 4;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_178290_a[EnumFacing.WEST.ordinal()] = 5;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_178290_a[EnumFacing.EAST.ordinal()] = 6;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }

    @SideOnly(Side.CLIENT)
    static enum VertexTranslations
    {
        DOWN(0, 1, 2, 3),
        UP(2, 3, 0, 1),
        NORTH(3, 0, 1, 2),
        SOUTH(0, 1, 2, 3),
        WEST(3, 0, 1, 2),
        EAST(1, 2, 3, 0);
        private final int field_178191_g;
        private final int field_178200_h;
        private final int field_178201_i;
        private final int field_178198_j;
        private static final BlockModelRenderer.VertexTranslations[] field_178199_k = new BlockModelRenderer.VertexTranslations[6];

        private static final String __OBFID = "CL_00002514";

        private VertexTranslations(int p_i46234_3_, int p_i46234_4_, int p_i46234_5_, int p_i46234_6_)
        {
            this.field_178191_g = p_i46234_3_;
            this.field_178200_h = p_i46234_4_;
            this.field_178201_i = p_i46234_5_;
            this.field_178198_j = p_i46234_6_;
        }

        public static BlockModelRenderer.VertexTranslations getVertexTranslations(EnumFacing p_178184_0_)
        {
            return field_178199_k[p_178184_0_.getIndex()];
        }

        static
        {
            field_178199_k[EnumFacing.DOWN.getIndex()] = DOWN;
            field_178199_k[EnumFacing.UP.getIndex()] = UP;
            field_178199_k[EnumFacing.NORTH.getIndex()] = NORTH;
            field_178199_k[EnumFacing.SOUTH.getIndex()] = SOUTH;
            field_178199_k[EnumFacing.WEST.getIndex()] = WEST;
            field_178199_k[EnumFacing.EAST.getIndex()] = EAST;
        }
    }
}