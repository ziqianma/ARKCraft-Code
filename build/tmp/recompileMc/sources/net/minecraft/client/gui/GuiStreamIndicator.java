package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiStreamIndicator
{
    private static final ResourceLocation locationStreamIndicator = new ResourceLocation("textures/gui/stream_indicator.png");
    private final Minecraft mc;
    private float field_152443_c = 1.0F;
    private int field_152444_d = 1;
    private static final String __OBFID = "CL_00001849";

    public GuiStreamIndicator(Minecraft mcIn)
    {
        this.mc = mcIn;
    }

    public void render(int p_152437_1_, int p_152437_2_)
    {
        if (this.mc.getTwitchStream().func_152934_n())
        {
            GlStateManager.enableBlend();
            int k = this.mc.getTwitchStream().func_152920_A();

            if (k > 0)
            {
                String s = "" + k;
                int l = this.mc.fontRendererObj.getStringWidth(s);
                boolean flag = true;
                int i1 = p_152437_1_ - l - 1;
                int j1 = p_152437_2_ + 20 - 1;
                int k1 = p_152437_2_ + 20 + this.mc.fontRendererObj.FONT_HEIGHT - 1;
                GlStateManager.disableTexture2D();
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                GlStateManager.color(0.0F, 0.0F, 0.0F, (0.65F + 0.35000002F * this.field_152443_c) / 2.0F);
                worldrenderer.startDrawingQuads();
                worldrenderer.addVertex((double)i1, (double)k1, 0.0D);
                worldrenderer.addVertex((double)p_152437_1_, (double)k1, 0.0D);
                worldrenderer.addVertex((double)p_152437_1_, (double)j1, 0.0D);
                worldrenderer.addVertex((double)i1, (double)j1, 0.0D);
                tessellator.draw();
                GlStateManager.enableTexture2D();
                this.mc.fontRendererObj.drawString(s, p_152437_1_ - l, p_152437_2_ + 20, 16777215);
            }

            this.render(p_152437_1_, p_152437_2_, this.func_152440_b(), 0);
            this.render(p_152437_1_, p_152437_2_, this.func_152438_c(), 17);
        }
    }

    private void render(int p_152436_1_, int p_152436_2_, int p_152436_3_, int p_152436_4_)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.65F + 0.35000002F * this.field_152443_c);
        this.mc.getTextureManager().bindTexture(locationStreamIndicator);
        float f = 150.0F;
        float f1 = 0.0F;
        float f2 = (float)p_152436_3_ * 0.015625F;
        float f3 = 1.0F;
        float f4 = (float)(p_152436_3_ + 16) * 0.015625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV((double)(p_152436_1_ - 16 - p_152436_4_), (double)(p_152436_2_ + 16), (double)f, (double)f1, (double)f4);
        worldrenderer.addVertexWithUV((double)(p_152436_1_ - p_152436_4_), (double)(p_152436_2_ + 16), (double)f, (double)f3, (double)f4);
        worldrenderer.addVertexWithUV((double)(p_152436_1_ - p_152436_4_), (double)(p_152436_2_ + 0), (double)f, (double)f3, (double)f2);
        worldrenderer.addVertexWithUV((double)(p_152436_1_ - 16 - p_152436_4_), (double)(p_152436_2_ + 0), (double)f, (double)f1, (double)f2);
        tessellator.draw();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private int func_152440_b()
    {
        return this.mc.getTwitchStream().isPaused() ? 16 : 0;
    }

    private int func_152438_c()
    {
        return this.mc.getTwitchStream().func_152929_G() ? 48 : 32;
    }

    public void func_152439_a()
    {
        if (this.mc.getTwitchStream().func_152934_n())
        {
            this.field_152443_c += 0.025F * (float)this.field_152444_d;

            if (this.field_152443_c < 0.0F)
            {
                this.field_152444_d *= -1;
                this.field_152443_c = 0.0F;
            }
            else if (this.field_152443_c > 1.0F)
            {
                this.field_152444_d *= -1;
                this.field_152443_c = 1.0F;
            }
        }
        else
        {
            this.field_152443_c = 1.0F;
            this.field_152444_d = 1;
        }
    }
}