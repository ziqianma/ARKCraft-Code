package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MobAppearance extends EntityFX
{
    private EntityLivingBase field_174844_a;
    private static final String __OBFID = "CL_00002594";

    protected MobAppearance(World worldIn, double p_i46283_2_, double p_i46283_4_, double p_i46283_6_)
    {
        super(worldIn, p_i46283_2_, p_i46283_4_, p_i46283_6_, 0.0D, 0.0D, 0.0D);
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
        this.motionX = this.motionY = this.motionZ = 0.0D;
        this.particleGravity = 0.0F;
        this.particleMaxAge = 30;
    }

    public int getFXLayer()
    {
        return 3;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (this.field_174844_a == null)
        {
            EntityGuardian entityguardian = new EntityGuardian(this.worldObj);
            entityguardian.func_175465_cm();
            this.field_174844_a = entityguardian;
        }
    }

    public void func_180434_a(WorldRenderer p_180434_1_, Entity p_180434_2_, float p_180434_3_, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
    {
        if (this.field_174844_a != null)
        {
            RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
            rendermanager.setRenderPosition(EntityFX.interpPosX, EntityFX.interpPosY, EntityFX.interpPosZ);
            float f6 = 0.42553192F;
            float f7 = ((float)this.particleAge + p_180434_3_) / (float)this.particleMaxAge;
            GlStateManager.depthMask(true);
            GlStateManager.enableBlend();
            GlStateManager.enableDepth();
            GlStateManager.blendFunc(770, 771);
            float f8 = 240.0F;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f8, f8);
            GlStateManager.pushMatrix();
            float f9 = 0.05F + 0.5F * MathHelper.sin(f7 * (float)Math.PI);
            GlStateManager.color(1.0F, 1.0F, 1.0F, f9);
            GlStateManager.translate(0.0F, 1.8F, 0.0F);
            GlStateManager.rotate(180.0F - p_180434_2_.rotationYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(60.0F - 150.0F * f7 - p_180434_2_.rotationPitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.4F, -1.5F);
            GlStateManager.scale(f6, f6, f6);
            this.field_174844_a.rotationYaw = this.field_174844_a.prevRotationYaw = 0.0F;
            this.field_174844_a.rotationYawHead = this.field_174844_a.prevRotationYawHead = 0.0F;
            rendermanager.renderEntityWithPosYaw(this.field_174844_a, 0.0D, 0.0D, 0.0D, 0.0F, p_180434_3_);
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
        }
    }

    @SideOnly(Side.CLIENT)
    public static class Factory implements IParticleFactory
        {
            private static final String __OBFID = "CL_00002593";

            public EntityFX getEntityFX(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_)
            {
                return new MobAppearance(worldIn, p_178902_3_, p_178902_5_, p_178902_7_);
            }
        }
}