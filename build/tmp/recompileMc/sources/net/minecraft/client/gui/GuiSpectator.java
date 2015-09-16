package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuRecipient;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSpectator extends Gui implements ISpectatorMenuRecipient
{
    private static final ResourceLocation field_175267_f = new ResourceLocation("textures/gui/widgets.png");
    public static final ResourceLocation field_175269_a = new ResourceLocation("textures/gui/spectator_widgets.png");
    private final Minecraft field_175268_g;
    private long field_175270_h;
    private SpectatorMenu field_175271_i;
    private static final String __OBFID = "CL_00001940";

    public GuiSpectator(Minecraft mcIn)
    {
        this.field_175268_g = mcIn;
    }

    public void func_175260_a(int p_175260_1_)
    {
        this.field_175270_h = Minecraft.getSystemTime();

        if (this.field_175271_i != null)
        {
            this.field_175271_i.func_178644_b(p_175260_1_);
        }
        else
        {
            this.field_175271_i = new SpectatorMenu(this);
        }
    }

    private float func_175265_c()
    {
        long i = this.field_175270_h - Minecraft.getSystemTime() + 5000L;
        return MathHelper.clamp_float((float)i / 2000.0F, 0.0F, 1.0F);
    }

    public void renderTooltip(ScaledResolution p_175264_1_, float p_175264_2_)
    {
        if (this.field_175271_i != null)
        {
            float f1 = this.func_175265_c();

            if (f1 <= 0.0F)
            {
                this.field_175271_i.func_178641_d();
            }
            else
            {
                int i = p_175264_1_.getScaledWidth() / 2;
                float f2 = this.zLevel;
                this.zLevel = -90.0F;
                float f3 = (float)p_175264_1_.getScaledHeight() - 22.0F * f1;
                SpectatorDetails spectatordetails = this.field_175271_i.func_178646_f();
                this.func_175258_a(p_175264_1_, f1, i, f3, spectatordetails);
                this.zLevel = f2;
            }
        }
    }

    protected void func_175258_a(ScaledResolution p_175258_1_, float p_175258_2_, int p_175258_3_, float p_175258_4_, SpectatorDetails p_175258_5_)
    {
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, p_175258_2_);
        this.field_175268_g.getTextureManager().bindTexture(field_175267_f);
        this.drawTexturedModalRect((float)(p_175258_3_ - 91), p_175258_4_, 0, 0, 182, 22);

        if (p_175258_5_.func_178681_b() >= 0)
        {
            this.drawTexturedModalRect((float)(p_175258_3_ - 91 - 1 + p_175258_5_.func_178681_b() * 20), p_175258_4_ - 1.0F, 0, 22, 24, 22);
        }

        RenderHelper.enableGUIStandardItemLighting();

        for (int j = 0; j < 9; ++j)
        {
            this.func_175266_a(j, p_175258_1_.getScaledWidth() / 2 - 90 + j * 20 + 2, p_175258_4_ + 3.0F, p_175258_2_, p_175258_5_.func_178680_a(j));
        }

        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
    }

    private void func_175266_a(int p_175266_1_, int p_175266_2_, float p_175266_3_, float p_175266_4_, ISpectatorMenuObject p_175266_5_)
    {
        this.field_175268_g.getTextureManager().bindTexture(field_175269_a);

        if (p_175266_5_ != SpectatorMenu.field_178657_a)
        {
            int k = (int)(p_175266_4_ * 255.0F);
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)p_175266_2_, p_175266_3_, 0.0F);
            float f2 = p_175266_5_.func_178662_A_() ? 1.0F : 0.25F;
            GlStateManager.color(f2, f2, f2, p_175266_4_);
            p_175266_5_.func_178663_a(f2, k);
            GlStateManager.popMatrix();
            String s = String.valueOf(GameSettings.getKeyDisplayString(this.field_175268_g.gameSettings.keyBindsHotbar[p_175266_1_].getKeyCode()));

            if (k > 3 && p_175266_5_.func_178662_A_())
            {
                this.field_175268_g.fontRendererObj.drawStringWithShadow(s, (float)(p_175266_2_ + 19 - 2 - this.field_175268_g.fontRendererObj.getStringWidth(s)), p_175266_3_ + 6.0F + 3.0F, 16777215 + (k << 24));
            }
        }
    }

    public void func_175263_a(ScaledResolution p_175263_1_)
    {
        int i = (int)(this.func_175265_c() * 255.0F);

        if (i > 3 && this.field_175271_i != null)
        {
            ISpectatorMenuObject ispectatormenuobject = this.field_175271_i.func_178645_b();
            String s = ispectatormenuobject != SpectatorMenu.field_178657_a ? ispectatormenuobject.func_178664_z_().getFormattedText() : this.field_175271_i.func_178650_c().func_178670_b().getFormattedText();

            if (s != null)
            {
                int j = (p_175263_1_.getScaledWidth() - this.field_175268_g.fontRendererObj.getStringWidth(s)) / 2;
                int k = p_175263_1_.getScaledHeight() - 35;
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.field_175268_g.fontRendererObj.drawStringWithShadow(s, (float)j, (float)k, 16777215 + (i << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }
    }

    public void func_175257_a(SpectatorMenu p_175257_1_)
    {
        this.field_175271_i = null;
        this.field_175270_h = 0L;
    }

    public boolean func_175262_a()
    {
        return this.field_175271_i != null;
    }

    public void func_175259_b(int p_175259_1_)
    {
        int j;

        for (j = this.field_175271_i.func_178648_e() + p_175259_1_; j >= 0 && j <= 8 && (this.field_175271_i.func_178643_a(j) == SpectatorMenu.field_178657_a || !this.field_175271_i.func_178643_a(j).func_178662_A_()); j += p_175259_1_)
        {
            ;
        }

        if (j >= 0 && j <= 8)
        {
            this.field_175271_i.func_178644_b(j);
            this.field_175270_h = Minecraft.getSystemTime();
        }
    }

    public void func_175261_b()
    {
        this.field_175270_h = Minecraft.getSystemTime();

        if (this.func_175262_a())
        {
            int i = this.field_175271_i.func_178648_e();

            if (i != -1)
            {
                this.field_175271_i.func_178644_b(i);
            }
        }
        else
        {
            this.field_175271_i = new SpectatorMenu(this);
        }
    }
}