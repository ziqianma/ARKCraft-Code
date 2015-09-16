package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderTntMinecart extends RenderMinecart
{
    private static final String __OBFID = "CL_00001029";

    public RenderTntMinecart(RenderManager p_i46135_1_)
    {
        super(p_i46135_1_);
    }

    protected void func_180561_a(EntityMinecartTNT p_180561_1_, float p_180561_2_, IBlockState p_180561_3_)
    {
        int i = p_180561_1_.getFuseTicks();

        if (i > -1 && (float)i - p_180561_2_ + 1.0F < 10.0F)
        {
            float f1 = 1.0F - ((float)i - p_180561_2_ + 1.0F) / 10.0F;
            f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
            f1 *= f1;
            f1 *= f1;
            float f2 = 1.0F + f1 * 0.3F;
            GlStateManager.scale(f2, f2, f2);
        }

        super.func_180560_a(p_180561_1_, p_180561_2_, p_180561_3_);

        if (i > -1 && i / 5 % 2 == 0)
        {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 772);
            GlStateManager.color(1.0F, 1.0F, 1.0F, (1.0F - ((float)i - p_180561_2_ + 1.0F) / 100.0F) * 0.8F);
            GlStateManager.pushMatrix();
            blockrendererdispatcher.renderBlockBrightness(Blocks.tnt.getDefaultState(), 1.0F);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }
    }

    protected void func_180560_a(EntityMinecart p_180560_1_, float p_180560_2_, IBlockState p_180560_3_)
    {
        this.func_180561_a((EntityMinecartTNT)p_180560_1_, p_180560_2_, p_180560_3_);
    }
}