package net.minecraft.client.renderer.tileentity;

import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TileEntityBeaconRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation beaconBeam = new ResourceLocation("textures/entity/beacon_beam.png");
    private static final String __OBFID = "CL_00000962";

    /**
     * Renders the TileEntityBeacon, including the beam segments.
     */
    public void renderBeacon(TileEntityBeacon p_180536_1_, double p_180536_2_, double p_180536_4_, double p_180536_6_, float p_180536_8_, int p_180536_9_)
    {
        float f1 = p_180536_1_.shouldBeamRender();
        GlStateManager.alphaFunc(516, 0.1F);

        if (f1 > 0.0F)
        {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            List list = p_180536_1_.func_174907_n();
            int j = 0;

            for (int k = 0; k < list.size(); ++k)
            {
                TileEntityBeacon.BeamSegment beamsegment = (TileEntityBeacon.BeamSegment)list.get(k);
                int l = j + beamsegment.func_177264_c();
                this.bindTexture(beaconBeam);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
                GlStateManager.disableLighting();
                GlStateManager.disableCull();
                GlStateManager.disableBlend();
                GlStateManager.depthMask(true);
                GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
                float f2 = (float)p_180536_1_.getWorld().getTotalWorldTime() + p_180536_8_;
                float f3 = -f2 * 0.2F - (float)MathHelper.floor_float(-f2 * 0.1F);
                double d3 = (double)f2 * 0.025D * -1.5D;
                worldrenderer.startDrawingQuads();
                double d4 = 0.2D;
                double d5 = 0.5D + Math.cos(d3 + 2.356194490192345D) * d4;
                double d6 = 0.5D + Math.sin(d3 + 2.356194490192345D) * d4;
                double d7 = 0.5D + Math.cos(d3 + (Math.PI / 4D)) * d4;
                double d8 = 0.5D + Math.sin(d3 + (Math.PI / 4D)) * d4;
                double d9 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * d4;
                double d10 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * d4;
                double d11 = 0.5D + Math.cos(d3 + 5.497787143782138D) * d4;
                double d12 = 0.5D + Math.sin(d3 + 5.497787143782138D) * d4;
                double d13 = 0.0D;
                double d14 = 1.0D;
                double d15 = (double)(-1.0F + f3);
                double d16 = (double)((float)beamsegment.func_177264_c() * f1) * (0.5D / d4) + d15;
                worldrenderer.setColorRGBA_F(beamsegment.func_177263_b()[0], beamsegment.func_177263_b()[1], beamsegment.func_177263_b()[2], 0.125F);
                worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double)l, p_180536_6_ + d6, d14, d16);
                worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double)j, p_180536_6_ + d6, d14, d15);
                worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double)j, p_180536_6_ + d8, d13, d15);
                worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double)l, p_180536_6_ + d8, d13, d16);
                worldrenderer.addVertexWithUV(p_180536_2_ + d11, p_180536_4_ + (double)l, p_180536_6_ + d12, d14, d16);
                worldrenderer.addVertexWithUV(p_180536_2_ + d11, p_180536_4_ + (double)j, p_180536_6_ + d12, d14, d15);
                worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double)j, p_180536_6_ + d10, d13, d15);
                worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double)l, p_180536_6_ + d10, d13, d16);
                worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double)l, p_180536_6_ + d8, d14, d16);
                worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double)j, p_180536_6_ + d8, d14, d15);
                worldrenderer.addVertexWithUV(p_180536_2_ + d11, p_180536_4_ + (double)j, p_180536_6_ + d12, d13, d15);
                worldrenderer.addVertexWithUV(p_180536_2_ + d11, p_180536_4_ + (double)l, p_180536_6_ + d12, d13, d16);
                worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double)l, p_180536_6_ + d10, d14, d16);
                worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double)j, p_180536_6_ + d10, d14, d15);
                worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double)j, p_180536_6_ + d6, d13, d15);
                worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double)l, p_180536_6_ + d6, d13, d16);
                tessellator.draw();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.depthMask(false);
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_F(beamsegment.func_177263_b()[0], beamsegment.func_177263_b()[1], beamsegment.func_177263_b()[2], 0.125F);
                d3 = 0.2D;
                d4 = 0.2D;
                d5 = 0.8D;
                d6 = 0.2D;
                d7 = 0.2D;
                d8 = 0.8D;
                d9 = 0.8D;
                d10 = 0.8D;
                d11 = 0.0D;
                d12 = 1.0D;
                d13 = (double)(-1.0F + f3);
                d14 = (double)((float)beamsegment.func_177264_c() * f1) + d13;
                worldrenderer.addVertexWithUV(p_180536_2_ + d3, p_180536_4_ + (double)l, p_180536_6_ + d4, d12, d14);
                worldrenderer.addVertexWithUV(p_180536_2_ + d3, p_180536_4_ + (double)j, p_180536_6_ + d4, d12, d13);
                worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double)j, p_180536_6_ + d6, d11, d13);
                worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double)l, p_180536_6_ + d6, d11, d14);
                worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double)l, p_180536_6_ + d10, d12, d14);
                worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double)j, p_180536_6_ + d10, d12, d13);
                worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double)j, p_180536_6_ + d8, d11, d13);
                worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double)l, p_180536_6_ + d8, d11, d14);
                worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double)l, p_180536_6_ + d6, d12, d14);
                worldrenderer.addVertexWithUV(p_180536_2_ + d5, p_180536_4_ + (double)j, p_180536_6_ + d6, d12, d13);
                worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double)j, p_180536_6_ + d10, d11, d13);
                worldrenderer.addVertexWithUV(p_180536_2_ + d9, p_180536_4_ + (double)l, p_180536_6_ + d10, d11, d14);
                worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double)l, p_180536_6_ + d8, d12, d14);
                worldrenderer.addVertexWithUV(p_180536_2_ + d7, p_180536_4_ + (double)j, p_180536_6_ + d8, d12, d13);
                worldrenderer.addVertexWithUV(p_180536_2_ + d3, p_180536_4_ + (double)j, p_180536_6_ + d4, d11, d13);
                worldrenderer.addVertexWithUV(p_180536_2_ + d3, p_180536_4_ + (double)l, p_180536_6_ + d4, d11, d14);
                tessellator.draw();
                GlStateManager.enableLighting();
                GlStateManager.enableTexture2D();
                GlStateManager.depthMask(true);
                j = l;
            }
        }
    }

    public void renderTileEntityAt(TileEntity p_180535_1_, double posX, double posZ, double p_180535_6_, float p_180535_8_, int p_180535_9_)
    {
        this.renderBeacon((TileEntityBeacon)p_180535_1_, posX, posZ, p_180535_6_, p_180535_8_, p_180535_9_);
    }
}