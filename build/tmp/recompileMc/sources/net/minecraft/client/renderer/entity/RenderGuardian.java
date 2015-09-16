package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderGuardian extends RenderLiving
{
    private static final ResourceLocation field_177114_e = new ResourceLocation("textures/entity/guardian.png");
    private static final ResourceLocation field_177116_j = new ResourceLocation("textures/entity/guardian_elder.png");
    private static final ResourceLocation field_177117_k = new ResourceLocation("textures/entity/guardian_beam.png");
    int field_177115_a;
    private static final String __OBFID = "CL_00002443";

    public RenderGuardian(RenderManager p_i46171_1_)
    {
        super(p_i46171_1_, new ModelGuardian(), 0.5F);
        this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
    }

    public boolean shouldRenderGuardian(EntityGuardian p_177113_1_, ICamera p_177113_2_, double p_177113_3_, double p_177113_5_, double p_177113_7_)
    {
        if (super.shouldRenderLiving(p_177113_1_, p_177113_2_, p_177113_3_, p_177113_5_, p_177113_7_))
        {
            return true;
        }
        else
        {
            if (p_177113_1_.func_175474_cn())
            {
                EntityLivingBase entitylivingbase = p_177113_1_.getTargetedEntity();

                if (entitylivingbase != null)
                {
                    Vec3 vec3 = this.func_177110_a(entitylivingbase, (double)entitylivingbase.height * 0.5D, 1.0F);
                    Vec3 vec31 = this.func_177110_a(p_177113_1_, (double)p_177113_1_.getEyeHeight(), 1.0F);

                    if (p_177113_2_.isBoundingBoxInFrustum(AxisAlignedBB.fromBounds(vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord, vec3.zCoord)))
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    private Vec3 func_177110_a(EntityLivingBase p_177110_1_, double p_177110_2_, float p_177110_4_)
    {
        double d1 = p_177110_1_.lastTickPosX + (p_177110_1_.posX - p_177110_1_.lastTickPosX) * (double)p_177110_4_;
        double d2 = p_177110_2_ + p_177110_1_.lastTickPosY + (p_177110_1_.posY - p_177110_1_.lastTickPosY) * (double)p_177110_4_;
        double d3 = p_177110_1_.lastTickPosZ + (p_177110_1_.posZ - p_177110_1_.lastTickPosZ) * (double)p_177110_4_;
        return new Vec3(d1, d2, d3);
    }

    public void func_177109_a(EntityGuardian p_177109_1_, double p_177109_2_, double p_177109_4_, double p_177109_6_, float p_177109_8_, float p_177109_9_)
    {
        if (this.field_177115_a != ((ModelGuardian)this.mainModel).func_178706_a())
        {
            this.mainModel = new ModelGuardian();
            this.field_177115_a = ((ModelGuardian)this.mainModel).func_178706_a();
        }

        super.doRender((EntityLiving)p_177109_1_, p_177109_2_, p_177109_4_, p_177109_6_, p_177109_8_, p_177109_9_);
        EntityLivingBase entitylivingbase = p_177109_1_.getTargetedEntity();

        if (entitylivingbase != null)
        {
            float f2 = p_177109_1_.func_175477_p(p_177109_9_);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.bindTexture(field_177117_k);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            float f3 = 240.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f3, f3);
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            float f4 = (float)p_177109_1_.worldObj.getTotalWorldTime() + p_177109_9_;
            float f5 = f4 * 0.5F % 1.0F;
            float f6 = p_177109_1_.getEyeHeight();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)p_177109_2_, (float)p_177109_4_ + f6, (float)p_177109_6_);
            Vec3 vec3 = this.func_177110_a(entitylivingbase, (double)entitylivingbase.height * 0.5D, p_177109_9_);
            Vec3 vec31 = this.func_177110_a(p_177109_1_, (double)f6, p_177109_9_);
            Vec3 vec32 = vec3.subtract(vec31);
            double d3 = vec32.lengthVector() + 1.0D;
            vec32 = vec32.normalize();
            float f7 = (float)Math.acos(vec32.yCoord);
            float f8 = (float)Math.atan2(vec32.zCoord, vec32.xCoord);
            GlStateManager.rotate((((float)Math.PI / 2F) + -f8) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f7 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            byte b0 = 1;
            double d4 = (double)f4 * 0.05D * (1.0D - (double)(b0 & 1) * 2.5D);
            worldrenderer.startDrawingQuads();
            float f9 = f2 * f2;
            worldrenderer.setColorRGBA(64 + (int)(f9 * 240.0F), 32 + (int)(f9 * 192.0F), 128 - (int)(f9 * 64.0F), 255);
            double d5 = (double)b0 * 0.2D;
            double d6 = d5 * 1.41D;
            double d7 = 0.0D + Math.cos(d4 + 2.356194490192345D) * d6;
            double d8 = 0.0D + Math.sin(d4 + 2.356194490192345D) * d6;
            double d9 = 0.0D + Math.cos(d4 + (Math.PI / 4D)) * d6;
            double d10 = 0.0D + Math.sin(d4 + (Math.PI / 4D)) * d6;
            double d11 = 0.0D + Math.cos(d4 + 3.9269908169872414D) * d6;
            double d12 = 0.0D + Math.sin(d4 + 3.9269908169872414D) * d6;
            double d13 = 0.0D + Math.cos(d4 + 5.497787143782138D) * d6;
            double d14 = 0.0D + Math.sin(d4 + 5.497787143782138D) * d6;
            double d15 = 0.0D + Math.cos(d4 + Math.PI) * d5;
            double d16 = 0.0D + Math.sin(d4 + Math.PI) * d5;
            double d17 = 0.0D + Math.cos(d4 + 0.0D) * d5;
            double d18 = 0.0D + Math.sin(d4 + 0.0D) * d5;
            double d19 = 0.0D + Math.cos(d4 + (Math.PI / 2D)) * d5;
            double d20 = 0.0D + Math.sin(d4 + (Math.PI / 2D)) * d5;
            double d21 = 0.0D + Math.cos(d4 + (Math.PI * 3D / 2D)) * d5;
            double d22 = 0.0D + Math.sin(d4 + (Math.PI * 3D / 2D)) * d5;
            double d23 = 0.0D;
            double d24 = 0.4999D;
            double d25 = (double)(-1.0F + f5);
            double d26 = d3 * (0.5D / d5) + d25;
            worldrenderer.addVertexWithUV(d15, d3, d16, d24, d26);
            worldrenderer.addVertexWithUV(d15, 0.0D, d16, d24, d25);
            worldrenderer.addVertexWithUV(d17, 0.0D, d18, d23, d25);
            worldrenderer.addVertexWithUV(d17, d3, d18, d23, d26);
            worldrenderer.addVertexWithUV(d19, d3, d20, d24, d26);
            worldrenderer.addVertexWithUV(d19, 0.0D, d20, d24, d25);
            worldrenderer.addVertexWithUV(d21, 0.0D, d22, d23, d25);
            worldrenderer.addVertexWithUV(d21, d3, d22, d23, d26);
            double d27 = 0.0D;

            if (p_177109_1_.ticksExisted % 2 == 0)
            {
                d27 = 0.5D;
            }

            worldrenderer.addVertexWithUV(d7, d3, d8, 0.5D, d27 + 0.5D);
            worldrenderer.addVertexWithUV(d9, d3, d10, 1.0D, d27 + 0.5D);
            worldrenderer.addVertexWithUV(d13, d3, d14, 1.0D, d27);
            worldrenderer.addVertexWithUV(d11, d3, d12, 0.5D, d27);
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }

    protected void func_177112_a(EntityGuardian p_177112_1_, float p_177112_2_)
    {
        if (p_177112_1_.isElder())
        {
            GlStateManager.scale(2.35F, 2.35F, 2.35F);
        }
    }

    protected ResourceLocation func_177111_a(EntityGuardian p_177111_1_)
    {
        return p_177111_1_.isElder() ? field_177116_j : field_177114_e;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(EntityLiving entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.func_177109_a((EntityGuardian)entity, x, y, z, p_76986_8_, partialTicks);
    }

    public boolean shouldRenderLiving(EntityLiving p_177104_1_, ICamera p_177104_2_, double p_177104_3_, double p_177104_5_, double p_177104_7_)
    {
        return this.shouldRenderGuardian((EntityGuardian)p_177104_1_, p_177104_2_, p_177104_3_, p_177104_5_, p_177104_7_);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
    {
        this.func_177112_a((EntityGuardian)p_77041_1_, p_77041_2_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(EntityLivingBase entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.func_177109_a((EntityGuardian)entity, x, y, z, p_76986_8_, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_177111_a((EntityGuardian)entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.func_177109_a((EntityGuardian)entity, x, y, z, p_76986_8_, partialTicks);
    }

    public boolean shouldRender(Entity entity, ICamera camera, double camX, double camY, double camZ)
    {
        return this.shouldRenderGuardian((EntityGuardian)entity, camera, camX, camY, camZ);
    }
}