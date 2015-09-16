package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFish extends Render
{
    private static final ResourceLocation field_110792_a = new ResourceLocation("textures/particle/particles.png");
    private static final String __OBFID = "CL_00000996";

    public RenderFish(RenderManager p_i46175_1_)
    {
        super(p_i46175_1_);
    }

    public void func_180558_a(EntityFishHook p_180558_1_, double p_180558_2_, double p_180558_4_, double p_180558_6_, float p_180558_8_, float p_180558_9_)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_180558_2_, (float)p_180558_4_, (float)p_180558_6_);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        this.bindEntityTexture(p_180558_1_);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        byte b0 = 1;
        byte b1 = 2;
        float f2 = (float)(b0 * 8 + 0) / 128.0F;
        float f3 = (float)(b0 * 8 + 8) / 128.0F;
        float f4 = (float)(b1 * 8 + 0) / 128.0F;
        float f5 = (float)(b1 * 8 + 8) / 128.0F;
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.5F;
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        worldrenderer.startDrawingQuads();
        worldrenderer.setNormal(0.0F, 1.0F, 0.0F);
        worldrenderer.addVertexWithUV((double)(0.0F - f7), (double)(0.0F - f8), 0.0D, (double)f2, (double)f5);
        worldrenderer.addVertexWithUV((double)(f6 - f7), (double)(0.0F - f8), 0.0D, (double)f3, (double)f5);
        worldrenderer.addVertexWithUV((double)(f6 - f7), (double)(1.0F - f8), 0.0D, (double)f3, (double)f4);
        worldrenderer.addVertexWithUV((double)(0.0F - f7), (double)(1.0F - f8), 0.0D, (double)f2, (double)f4);
        tessellator.draw();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        if (p_180558_1_.angler != null)
        {
            float f9 = p_180558_1_.angler.getSwingProgress(p_180558_9_);
            float f10 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
            Vec3 vec3 = new Vec3(-0.36D, 0.03D, 0.35D);
            vec3 = vec3.rotatePitch(-(p_180558_1_.angler.prevRotationPitch + (p_180558_1_.angler.rotationPitch - p_180558_1_.angler.prevRotationPitch) * p_180558_9_) * (float)Math.PI / 180.0F);
            vec3 = vec3.rotateYaw(-(p_180558_1_.angler.prevRotationYaw + (p_180558_1_.angler.rotationYaw - p_180558_1_.angler.prevRotationYaw) * p_180558_9_) * (float)Math.PI / 180.0F);
            vec3 = vec3.rotateYaw(f10 * 0.5F);
            vec3 = vec3.rotatePitch(-f10 * 0.7F);
            double d3 = p_180558_1_.angler.prevPosX + (p_180558_1_.angler.posX - p_180558_1_.angler.prevPosX) * (double)p_180558_9_ + vec3.xCoord;
            double d4 = p_180558_1_.angler.prevPosY + (p_180558_1_.angler.posY - p_180558_1_.angler.prevPosY) * (double)p_180558_9_ + vec3.yCoord;
            double d5 = p_180558_1_.angler.prevPosZ + (p_180558_1_.angler.posZ - p_180558_1_.angler.prevPosZ) * (double)p_180558_9_ + vec3.zCoord;
            double d6 = (double)p_180558_1_.angler.getEyeHeight();

            if (this.renderManager.options != null && this.renderManager.options.thirdPersonView > 0 || p_180558_1_.angler != Minecraft.getMinecraft().thePlayer)
            {
                float f11 = (p_180558_1_.angler.prevRenderYawOffset + (p_180558_1_.angler.renderYawOffset - p_180558_1_.angler.prevRenderYawOffset) * p_180558_9_) * (float)Math.PI / 180.0F;
                double d7 = (double)MathHelper.sin(f11);
                double d9 = (double)MathHelper.cos(f11);
                double d11 = 0.35D;
                double d13 = 0.8D;
                d3 = p_180558_1_.angler.prevPosX + (p_180558_1_.angler.posX - p_180558_1_.angler.prevPosX) * (double)p_180558_9_ - d9 * 0.35D - d7 * 0.8D;
                d4 = p_180558_1_.angler.prevPosY + d6 + (p_180558_1_.angler.posY - p_180558_1_.angler.prevPosY) * (double)p_180558_9_ - 0.45D;
                d5 = p_180558_1_.angler.prevPosZ + (p_180558_1_.angler.posZ - p_180558_1_.angler.prevPosZ) * (double)p_180558_9_ - d7 * 0.35D + d9 * 0.8D;
                d6 = p_180558_1_.angler.isSneaking() ? -0.1875D : 0.0D;
            }

            double d16 = p_180558_1_.prevPosX + (p_180558_1_.posX - p_180558_1_.prevPosX) * (double)p_180558_9_;
            double d8 = p_180558_1_.prevPosY + (p_180558_1_.posY - p_180558_1_.prevPosY) * (double)p_180558_9_ + 0.25D;
            double d10 = p_180558_1_.prevPosZ + (p_180558_1_.posZ - p_180558_1_.prevPosZ) * (double)p_180558_9_;
            double d12 = (double)((float)(d3 - d16));
            double d14 = (double)((float)(d4 - d8)) + d6;
            double d15 = (double)((float)(d5 - d10));
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            worldrenderer.startDrawing(3);
            worldrenderer.setColorOpaque_I(0);
            byte b2 = 16;

            for (int i = 0; i <= b2; ++i)
            {
                float f12 = (float)i / (float)b2;
                worldrenderer.addVertex(p_180558_2_ + d12 * (double)f12, p_180558_4_ + d14 * (double)(f12 * f12 + f12) * 0.5D + 0.25D, p_180558_6_ + d15 * (double)f12);
            }

            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            super.doRender(p_180558_1_, p_180558_2_, p_180558_4_, p_180558_6_, p_180558_8_, p_180558_9_);
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityFishHook entity)
    {
        return field_110792_a;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityFishHook)entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.func_180558_a((EntityFishHook)entity, x, y, z, p_76986_8_, partialTicks);
    }
}