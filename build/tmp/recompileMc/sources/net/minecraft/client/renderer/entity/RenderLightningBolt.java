package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLightningBolt extends Render
{
    private static final String __OBFID = "CL_00001011";

    public RenderLightningBolt(RenderManager p_i46157_1_)
    {
        super(p_i46157_1_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(EntityLightningBolt entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 1);
        double[] adouble = new double[8];
        double[] adouble1 = new double[8];
        double d3 = 0.0D;
        double d4 = 0.0D;
        Random random = new Random(entity.boltVertex);

        for (int i = 7; i >= 0; --i)
        {
            adouble[i] = d3;
            adouble1[i] = d4;
            d3 += (double)(random.nextInt(11) - 5);
            d4 += (double)(random.nextInt(11) - 5);
        }

        for (int k1 = 0; k1 < 4; ++k1)
        {
            Random random1 = new Random(entity.boltVertex);

            for (int j = 0; j < 3; ++j)
            {
                int k = 7;
                int l = 0;

                if (j > 0)
                {
                    k = 7 - j;
                }

                if (j > 0)
                {
                    l = k - 2;
                }

                double d5 = adouble[k] - d3;
                double d6 = adouble1[k] - d4;

                for (int i1 = k; i1 >= l; --i1)
                {
                    double d7 = d5;
                    double d8 = d6;

                    if (j == 0)
                    {
                        d5 += (double)(random1.nextInt(11) - 5);
                        d6 += (double)(random1.nextInt(11) - 5);
                    }
                    else
                    {
                        d5 += (double)(random1.nextInt(31) - 15);
                        d6 += (double)(random1.nextInt(31) - 15);
                    }

                    worldrenderer.startDrawing(5);
                    float f2 = 0.5F;
                    worldrenderer.setColorRGBA_F(0.9F * f2, 0.9F * f2, 1.0F * f2, 0.3F);
                    double d9 = 0.1D + (double)k1 * 0.2D;

                    if (j == 0)
                    {
                        d9 *= (double)i1 * 0.1D + 1.0D;
                    }

                    double d10 = 0.1D + (double)k1 * 0.2D;

                    if (j == 0)
                    {
                        d10 *= (double)(i1 - 1) * 0.1D + 1.0D;
                    }

                    for (int j1 = 0; j1 < 5; ++j1)
                    {
                        double d11 = x + 0.5D - d9;
                        double d12 = z + 0.5D - d9;

                        if (j1 == 1 || j1 == 2)
                        {
                            d11 += d9 * 2.0D;
                        }

                        if (j1 == 2 || j1 == 3)
                        {
                            d12 += d9 * 2.0D;
                        }

                        double d13 = x + 0.5D - d10;
                        double d14 = z + 0.5D - d10;

                        if (j1 == 1 || j1 == 2)
                        {
                            d13 += d10 * 2.0D;
                        }

                        if (j1 == 2 || j1 == 3)
                        {
                            d14 += d10 * 2.0D;
                        }

                        worldrenderer.addVertex(d13 + d5, y + (double)(i1 * 16), d14 + d6);
                        worldrenderer.addVertex(d11 + d7, y + (double)((i1 + 1) * 16), d12 + d8);
                    }

                    tessellator.draw();
                }
            }
        }

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityLightningBolt entity)
    {
        return null;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityLightningBolt)entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.doRender((EntityLightningBolt)entity, x, y, z, p_76986_8_, partialTicks);
    }
}