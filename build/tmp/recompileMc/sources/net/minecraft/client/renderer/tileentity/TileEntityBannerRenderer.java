package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityBannerRenderer extends TileEntitySpecialRenderer
{
    private static final Map field_178466_c = Maps.newHashMap();
    private static final ResourceLocation BANNERTEXTURES = new ResourceLocation("textures/entity/banner_base.png");
    private ModelBanner bannerModel = new ModelBanner();
    private static final String __OBFID = "CL_00002473";

    public void renderTileEntityBanner(TileEntityBanner entityBanner, double x, double y, double z, float p_180545_8_, int p_180545_9_)
    {
        boolean flag = entityBanner.getWorld() != null;
        boolean flag1 = !flag || entityBanner.getBlockType() == Blocks.standing_banner;
        int j = flag ? entityBanner.getBlockMetadata() : 0;
        long k = flag ? entityBanner.getWorld().getTotalWorldTime() : 0L;
        GlStateManager.pushMatrix();
        float f1 = 0.6666667F;
        float f3;

        if (flag1)
        {
            GlStateManager.translate((float)x + 0.5F, (float)y + 0.75F * f1, (float)z + 0.5F);
            float f2 = (float)(j * 360) / 16.0F;
            GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
            this.bannerModel.bannerStand.showModel = true;
        }
        else
        {
            f3 = 0.0F;

            if (j == 2)
            {
                f3 = 180.0F;
            }

            if (j == 4)
            {
                f3 = 90.0F;
            }

            if (j == 5)
            {
                f3 = -90.0F;
            }

            GlStateManager.translate((float)x + 0.5F, (float)y - 0.25F * f1, (float)z + 0.5F);
            GlStateManager.rotate(-f3, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -0.3125F, -0.4375F);
            this.bannerModel.bannerStand.showModel = false;
        }

        BlockPos blockpos = entityBanner.getPos();
        f3 = (float)(blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13) + (float)k + p_180545_8_;
        this.bannerModel.bannerSlate.rotateAngleX = (-0.0125F + 0.01F * MathHelper.cos(f3 * (float)Math.PI * 0.02F)) * (float)Math.PI;
        GlStateManager.enableRescaleNormal();
        ResourceLocation resourcelocation = this.func_178463_a(entityBanner);

        if (resourcelocation != null)
        {
            this.bindTexture(resourcelocation);
            GlStateManager.pushMatrix();
            GlStateManager.scale(f1, -f1, -f1);
            this.bannerModel.func_178687_a();
            GlStateManager.popMatrix();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    private ResourceLocation func_178463_a(TileEntityBanner p_178463_1_)
    {
        String s = p_178463_1_.func_175116_e();

        if (s.isEmpty())
        {
            return null;
        }
        else
        {
            TileEntityBannerRenderer.TimedBannerTexture timedbannertexture = (TileEntityBannerRenderer.TimedBannerTexture)field_178466_c.get(s);

            if (timedbannertexture == null)
            {
                if (field_178466_c.size() >= 256)
                {
                    long i = System.currentTimeMillis();
                    Iterator iterator = field_178466_c.keySet().iterator();

                    while (iterator.hasNext())
                    {
                        String s1 = (String)iterator.next();
                        TileEntityBannerRenderer.TimedBannerTexture timedbannertexture1 = (TileEntityBannerRenderer.TimedBannerTexture)field_178466_c.get(s1);

                        if (i - timedbannertexture1.field_178472_a > 60000L)
                        {
                            Minecraft.getMinecraft().getTextureManager().deleteTexture(timedbannertexture1.field_178471_b);
                            iterator.remove();
                        }
                    }

                    if (field_178466_c.size() >= 256)
                    {
                        return null;
                    }
                }

                List list1 = p_178463_1_.getPatternList();
                List list = p_178463_1_.getColorList();
                ArrayList arraylist = Lists.newArrayList();
                Iterator iterator1 = list1.iterator();

                while (iterator1.hasNext())
                {
                    TileEntityBanner.EnumBannerPattern enumbannerpattern = (TileEntityBanner.EnumBannerPattern)iterator1.next();
                    arraylist.add("textures/entity/banner/" + enumbannerpattern.getPatternName() + ".png");
                }

                timedbannertexture = new TileEntityBannerRenderer.TimedBannerTexture(null);
                timedbannertexture.field_178471_b = new ResourceLocation(s);
                Minecraft.getMinecraft().getTextureManager().loadTexture(timedbannertexture.field_178471_b, new LayeredColorMaskTexture(BANNERTEXTURES, arraylist, list));
                field_178466_c.put(s, timedbannertexture);
            }

            timedbannertexture.field_178472_a = System.currentTimeMillis();
            return timedbannertexture.field_178471_b;
        }
    }

    public void renderTileEntityAt(TileEntity p_180535_1_, double posX, double posZ, double p_180535_6_, float p_180535_8_, int p_180535_9_)
    {
        this.renderTileEntityBanner((TileEntityBanner)p_180535_1_, posX, posZ, p_180535_6_, p_180535_8_, p_180535_9_);
    }

    @SideOnly(Side.CLIENT)
    static class TimedBannerTexture
        {
            public long field_178472_a;
            public ResourceLocation field_178471_b;
            private static final String __OBFID = "CL_00002471";

            private TimedBannerTexture() {}

            TimedBannerTexture(Object p_i46209_1_)
            {
                this();
            }
        }
}