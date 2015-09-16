package net.minecraft.client.renderer.entity;

import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderEntityItem extends Render
{
    private final RenderItem field_177080_a;
    private Random field_177079_e = new Random();
    private static final String __OBFID = "CL_00002442";

    public RenderEntityItem(RenderManager p_i46167_1_, RenderItem p_i46167_2_)
    {
        super(p_i46167_1_);
        this.field_177080_a = p_i46167_2_;
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    private int func_177077_a(EntityItem p_177077_1_, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_)
    {
        ItemStack itemstack = p_177077_1_.getEntityItem();
        Item item = itemstack.getItem();

        if (item == null)
        {
            return 0;
        }
        else
        {
            boolean flag = p_177077_9_.isGui3d();
            int i = this.func_177078_a(itemstack);
            float f1 = 0.25F;
            float f2 = shouldBob() ? MathHelper.sin(((float)p_177077_1_.getAge() + p_177077_8_) / 10.0F + p_177077_1_.hoverStart) * 0.1F + 0.1F : 0.0F;
            GlStateManager.translate((float)p_177077_2_, (float)p_177077_4_ + f2 + 0.25F, (float)p_177077_6_);
            float f3;

            if (flag || this.renderManager.options != null && this.renderManager.options.fancyGraphics)
            {
                f3 = (((float)p_177077_1_.getAge() + p_177077_8_) / 20.0F + p_177077_1_.hoverStart) * (180F / (float)Math.PI);
                GlStateManager.rotate(f3, 0.0F, 1.0F, 0.0F);
            }

            if (!flag)
            {
                f3 = -0.0F * (float)(i - 1) * 0.5F;
                float f4 = -0.0F * (float)(i - 1) * 0.5F;
                float f5 = -0.046875F * (float)(i - 1) * 0.5F;
                GlStateManager.translate(f3, f4, f5);
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            return i;
        }
    }

    protected int func_177078_a(ItemStack p_177078_1_)
    {
        byte b0 = 1;

        if (p_177078_1_.stackSize > 48)
        {
            b0 = 5;
        }
        else if (p_177078_1_.stackSize > 32)
        {
            b0 = 4;
        }
        else if (p_177078_1_.stackSize > 16)
        {
            b0 = 3;
        }
        else if (p_177078_1_.stackSize > 1)
        {
            b0 = 2;
        }

        return b0;
    }

    public void func_177075_a(EntityItem p_177075_1_, double p_177075_2_, double p_177075_4_, double p_177075_6_, float p_177075_8_, float p_177075_9_)
    {
        ItemStack itemstack = p_177075_1_.getEntityItem();
        this.field_177079_e.setSeed(187L);
        boolean flag = false;

        if (this.bindEntityTexture(p_177075_1_))
        {
            this.renderManager.renderEngine.getTexture(this.func_177076_a(p_177075_1_)).setBlurMipmap(false, false);
            flag = true;
        }

        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        IBakedModel ibakedmodel = this.field_177080_a.getItemModelMesher().getItemModel(itemstack);
        int i = this.func_177077_a(p_177075_1_, p_177075_2_, p_177075_4_, p_177075_6_, p_177075_9_, ibakedmodel);

        for (int j = 0; j < i; ++j)
        {
            if (ibakedmodel.isGui3d())
            {
                GlStateManager.pushMatrix();

                if (j > 0)
                {
                    float f2 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f3 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f4 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    GlStateManager.translate(f2, f3, f4);
                }

                GlStateManager.scale(0.5F, 0.5F, 0.5F);
                this.field_177080_a.renderItem(itemstack, ibakedmodel);
                GlStateManager.popMatrix();
            }
            else
            {
                // Makes items offset when in 3D, like when in 2D, looks much better. Considered a vanilla bug...
                if (j > 0 && shouldSpreadItems())
                {
                    float f2 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f3 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    float f4 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
                    GlStateManager.translate(f2, f3, 0);
                }
                this.field_177080_a.renderItem(itemstack, ibakedmodel);
                GlStateManager.translate(0.0F, 0.0F, 0.046875F);
            }
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        this.bindEntityTexture(p_177075_1_);

        if (flag)
        {
            this.renderManager.renderEngine.getTexture(this.func_177076_a(p_177075_1_)).restoreLastBlurMipmap();
        }

        super.doRender(p_177075_1_, p_177075_2_, p_177075_4_, p_177075_6_, p_177075_8_, p_177075_9_);
    }

    protected ResourceLocation func_177076_a(EntityItem p_177076_1_)
    {
        return TextureMap.locationBlocksTexture;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_177076_a((EntityItem)entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.func_177075_a((EntityItem)entity, x, y, z, p_76986_8_, partialTicks);
    }

    /*==================================== FORGE START ===========================================*/

    /**
     * Items should spread out when rendered in 3d?
     * @return
     */
    public boolean shouldSpreadItems()
    {
        return true;
    }

    /**
     * Items should have a bob effect
     * @return
     */
    public boolean shouldBob()
    {
        return true;
    }
    /*==================================== FORGE END =============================================*/
}