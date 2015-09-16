package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class RenderLiving extends RendererLivingEntity
{
    private static final String __OBFID = "CL_00001015";

    public RenderLiving(RenderManager p_i46153_1_, ModelBase p_i46153_2_, float p_i46153_3_)
    {
        super(p_i46153_1_, p_i46153_2_, p_i46153_3_);
    }

    /**
     * Test if the entity name must be rendered
     */
    protected boolean canRenderName(EntityLiving targetEntity)
    {
        return super.canRenderName(targetEntity) && (targetEntity.getAlwaysRenderNameTagForRender() || targetEntity.hasCustomName() && targetEntity == this.renderManager.pointedEntity);
    }

    public boolean shouldRenderLiving(EntityLiving p_177104_1_, ICamera p_177104_2_, double p_177104_3_, double p_177104_5_, double p_177104_7_)
    {
        if (super.shouldRender(p_177104_1_, p_177104_2_, p_177104_3_, p_177104_5_, p_177104_7_))
        {
            return true;
        }
        else if (p_177104_1_.getLeashed() && p_177104_1_.getLeashedToEntity() != null)
        {
            Entity entity = p_177104_1_.getLeashedToEntity();
            return p_177104_2_.isBoundingBoxInFrustum(entity.getEntityBoundingBox());
        }
        else
        {
            return false;
        }
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(EntityLiving entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        super.doRender((EntityLivingBase)entity, x, y, z, p_76986_8_, partialTicks);
        this.renderLeash(entity, x, y, z, p_76986_8_, partialTicks);
    }

    public void func_177105_a(EntityLiving p_177105_1_, float p_177105_2_)
    {
        int i = p_177105_1_.getBrightnessForRender(p_177105_2_);
        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
    }

    /**
     * Gets the value between start and end according to pct
     */
    private double interpolateValue(double start, double end, double pct)
    {
        return start + (end - start) * pct;
    }

    protected void renderLeash(EntityLiving p_110827_1_, double p_110827_2_, double p_110827_4_, double p_110827_6_, float p_110827_8_, float p_110827_9_)
    {
        Entity entity = p_110827_1_.getLeashedToEntity();

        if (entity != null)
        {
            p_110827_4_ -= (1.6D - (double)p_110827_1_.height) * 0.5D;
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            double d3 = this.interpolateValue((double)entity.prevRotationYaw, (double)entity.rotationYaw, (double)(p_110827_9_ * 0.5F)) * 0.01745329238474369D;
            double d4 = this.interpolateValue((double)entity.prevRotationPitch, (double)entity.rotationPitch, (double)(p_110827_9_ * 0.5F)) * 0.01745329238474369D;
            double d5 = Math.cos(d3);
            double d6 = Math.sin(d3);
            double d7 = Math.sin(d4);

            if (entity instanceof EntityHanging)
            {
                d5 = 0.0D;
                d6 = 0.0D;
                d7 = -1.0D;
            }

            double d8 = Math.cos(d4);
            double d9 = this.interpolateValue(entity.prevPosX, entity.posX, (double)p_110827_9_) - d5 * 0.7D - d6 * 0.5D * d8;
            double d10 = this.interpolateValue(entity.prevPosY + (double)entity.getEyeHeight() * 0.7D, entity.posY + (double)entity.getEyeHeight() * 0.7D, (double)p_110827_9_) - d7 * 0.5D - 0.25D;
            double d11 = this.interpolateValue(entity.prevPosZ, entity.posZ, (double)p_110827_9_) - d6 * 0.7D + d5 * 0.5D * d8;
            double d12 = this.interpolateValue((double)p_110827_1_.prevRenderYawOffset, (double)p_110827_1_.renderYawOffset, (double)p_110827_9_) * 0.01745329238474369D + (Math.PI / 2D);
            d5 = Math.cos(d12) * (double)p_110827_1_.width * 0.4D;
            d6 = Math.sin(d12) * (double)p_110827_1_.width * 0.4D;
            double d13 = this.interpolateValue(p_110827_1_.prevPosX, p_110827_1_.posX, (double)p_110827_9_) + d5;
            double d14 = this.interpolateValue(p_110827_1_.prevPosY, p_110827_1_.posY, (double)p_110827_9_);
            double d15 = this.interpolateValue(p_110827_1_.prevPosZ, p_110827_1_.posZ, (double)p_110827_9_) + d6;
            p_110827_2_ += d5;
            p_110827_6_ += d6;
            double d16 = (double)((float)(d9 - d13));
            double d17 = (double)((float)(d10 - d14));
            double d18 = (double)((float)(d11 - d15));
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            boolean flag = true;
            double d19 = 0.025D;
            worldrenderer.startDrawing(5);
            int i;
            float f2;

            for (i = 0; i <= 24; ++i)
            {
                if (i % 2 == 0)
                {
                    worldrenderer.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
                }
                else
                {
                    worldrenderer.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
                }

                f2 = (float)i / 24.0F;
                worldrenderer.addVertex(p_110827_2_ + d16 * (double)f2 + 0.0D, p_110827_4_ + d17 * (double)(f2 * f2 + f2) * 0.5D + (double)((24.0F - (float)i) / 18.0F + 0.125F), p_110827_6_ + d18 * (double)f2);
                worldrenderer.addVertex(p_110827_2_ + d16 * (double)f2 + 0.025D, p_110827_4_ + d17 * (double)(f2 * f2 + f2) * 0.5D + (double)((24.0F - (float)i) / 18.0F + 0.125F) + 0.025D, p_110827_6_ + d18 * (double)f2);
            }

            tessellator.draw();
            worldrenderer.startDrawing(5);

            for (i = 0; i <= 24; ++i)
            {
                if (i % 2 == 0)
                {
                    worldrenderer.setColorRGBA_F(0.5F, 0.4F, 0.3F, 1.0F);
                }
                else
                {
                    worldrenderer.setColorRGBA_F(0.35F, 0.28F, 0.21000001F, 1.0F);
                }

                f2 = (float)i / 24.0F;
                worldrenderer.addVertex(p_110827_2_ + d16 * (double)f2 + 0.0D, p_110827_4_ + d17 * (double)(f2 * f2 + f2) * 0.5D + (double)((24.0F - (float)i) / 18.0F + 0.125F) + 0.025D, p_110827_6_ + d18 * (double)f2);
                worldrenderer.addVertex(p_110827_2_ + d16 * (double)f2 + 0.025D, p_110827_4_ + d17 * (double)(f2 * f2 + f2) * 0.5D + (double)((24.0F - (float)i) / 18.0F + 0.125F), p_110827_6_ + d18 * (double)f2 + 0.025D);
            }

            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
        }
    }

    /**
     * Test if the entity name must be rendered
     */
    protected boolean canRenderName(EntityLivingBase targetEntity)
    {
        return this.canRenderName((EntityLiving)targetEntity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(EntityLivingBase entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.doRender((EntityLiving)entity, x, y, z, p_76986_8_, partialTicks);
    }

    protected boolean canRenderName(Entity entity)
    {
        return this.canRenderName((EntityLiving)entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.doRender((EntityLiving)entity, x, y, z, p_76986_8_, partialTicks);
    }

    public boolean shouldRender(Entity entity, ICamera camera, double camX, double camY, double camZ)
    {
        return this.shouldRenderLiving((EntityLiving)entity, camera, camX, camY, camZ);
    }
}