package net.minecraft.client.renderer.entity.layers;

import java.util.Random;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerArrow implements LayerRenderer
{
    private final RendererLivingEntity field_177168_a;
    private static final String __OBFID = "CL_00002426";

    public LayerArrow(RendererLivingEntity p_i46124_1_)
    {
        this.field_177168_a = p_i46124_1_;
    }

    public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
    {
        int i = p_177141_1_.getArrowCountInEntity();

        if (i > 0)
        {
            EntityArrow entityarrow = new EntityArrow(p_177141_1_.worldObj, p_177141_1_.posX, p_177141_1_.posY, p_177141_1_.posZ);
            Random random = new Random((long)p_177141_1_.getEntityId());
            RenderHelper.disableStandardItemLighting();

            for (int j = 0; j < i; ++j)
            {
                GlStateManager.pushMatrix();
                ModelRenderer modelrenderer = this.field_177168_a.getMainModel().getRandomModelBox(random);
                ModelBox modelbox = (ModelBox)modelrenderer.cubeList.get(random.nextInt(modelrenderer.cubeList.size()));
                modelrenderer.postRender(0.0625F);
                float f7 = random.nextFloat();
                float f8 = random.nextFloat();
                float f9 = random.nextFloat();
                float f10 = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * f7) / 16.0F;
                float f11 = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * f8) / 16.0F;
                float f12 = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * f9) / 16.0F;
                GlStateManager.translate(f10, f11, f12);
                f7 = f7 * 2.0F - 1.0F;
                f8 = f8 * 2.0F - 1.0F;
                f9 = f9 * 2.0F - 1.0F;
                f7 *= -1.0F;
                f8 *= -1.0F;
                f9 *= -1.0F;
                float f13 = MathHelper.sqrt_float(f7 * f7 + f9 * f9);
                entityarrow.prevRotationYaw = entityarrow.rotationYaw = (float)(Math.atan2((double)f7, (double)f9) * 180.0D / Math.PI);
                entityarrow.prevRotationPitch = entityarrow.rotationPitch = (float)(Math.atan2((double)f8, (double)f13) * 180.0D / Math.PI);
                double d0 = 0.0D;
                double d1 = 0.0D;
                double d2 = 0.0D;
                this.field_177168_a.func_177068_d().renderEntityWithPosYaw(entityarrow, d0, d1, d2, 0.0F, p_177141_4_);
                GlStateManager.popMatrix();
            }

            RenderHelper.enableStandardItemLighting();
        }
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }
}