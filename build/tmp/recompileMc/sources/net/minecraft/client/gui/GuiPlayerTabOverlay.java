package net.minecraft.client.gui;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPlayerTabOverlay extends Gui
{
    private static final Ordering field_175252_a = Ordering.from(new GuiPlayerTabOverlay.PlayerComparator(null));
    private final Minecraft field_175250_f;
    private final GuiIngame field_175251_g;
    private IChatComponent footer;
    private IChatComponent header;
    private long field_175253_j;
    private boolean field_175254_k;
    private static final String __OBFID = "CL_00001943";

    public GuiPlayerTabOverlay(Minecraft mcIn, GuiIngame p_i45529_2_)
    {
        this.field_175250_f = mcIn;
        this.field_175251_g = p_i45529_2_;
    }

    public String func_175243_a(NetworkPlayerInfo p_175243_1_)
    {
        return p_175243_1_.func_178854_k() != null ? p_175243_1_.func_178854_k().getFormattedText() : ScorePlayerTeam.formatPlayerName(p_175243_1_.getPlayerTeam(), p_175243_1_.getGameProfile().getName());
    }

    public void func_175246_a(boolean p_175246_1_)
    {
        if (p_175246_1_ && !this.field_175254_k)
        {
            this.field_175253_j = Minecraft.getSystemTime();
        }

        this.field_175254_k = p_175246_1_;
    }

    public void func_175249_a(int p_175249_1_, Scoreboard p_175249_2_, ScoreObjective p_175249_3_)
    {
        NetHandlerPlayClient nethandlerplayclient = this.field_175250_f.thePlayer.sendQueue;
        List list = field_175252_a.sortedCopy(nethandlerplayclient.func_175106_d());
        int j = 0;
        int k = 0;
        Iterator iterator = list.iterator();
        int l;

        while (iterator.hasNext())
        {
            NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo)iterator.next();
            l = this.field_175250_f.fontRendererObj.getStringWidth(this.func_175243_a(networkplayerinfo));
            j = Math.max(j, l);

            if (p_175249_3_ != null && p_175249_3_.getRenderType() != IScoreObjectiveCriteria.EnumRenderType.HEARTS)
            {
                l = this.field_175250_f.fontRendererObj.getStringWidth(" " + p_175249_2_.getValueFromObjective(networkplayerinfo.getGameProfile().getName(), p_175249_3_).getScorePoints());
                k = Math.max(k, l);
            }
        }

        list = list.subList(0, Math.min(list.size(), 80));
        int j3 = list.size();
        int k3 = j3;

        for (l = 1; k3 > 20; k3 = (j3 + l - 1) / l)
        {
            ++l;
        }

        boolean flag = this.field_175250_f.isIntegratedServerRunning() || this.field_175250_f.getNetHandler().getNetworkManager().getIsencrypted();
        int i1;

        if (p_175249_3_ != null)
        {
            if (p_175249_3_.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS)
            {
                i1 = 90;
            }
            else
            {
                i1 = k;
            }
        }
        else
        {
            i1 = 0;
        }

        int j1 = Math.min(l * ((flag ? 9 : 0) + j + i1 + 13), p_175249_1_ - 50) / l;
        int k1 = p_175249_1_ / 2 - (j1 * l + (l - 1) * 5) / 2;
        int l1 = 10;
        int i2 = j1 * l + (l - 1) * 5;
        List list1 = null;
        List list2 = null;
        Iterator iterator1;
        String s;

        if (this.header != null)
        {
            list1 = this.field_175250_f.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), p_175249_1_ - 50);

            for (iterator1 = list1.iterator(); iterator1.hasNext(); i2 = Math.max(i2, this.field_175250_f.fontRendererObj.getStringWidth(s)))
            {
                s = (String)iterator1.next();
            }
        }

        if (this.footer != null)
        {
            list2 = this.field_175250_f.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), p_175249_1_ - 50);

            for (iterator1 = list2.iterator(); iterator1.hasNext(); i2 = Math.max(i2, this.field_175250_f.fontRendererObj.getStringWidth(s)))
            {
                s = (String)iterator1.next();
            }
        }

        int j2;

        if (list1 != null)
        {
            drawRect(p_175249_1_ / 2 - i2 / 2 - 1, l1 - 1, p_175249_1_ / 2 + i2 / 2 + 1, l1 + list1.size() * this.field_175250_f.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (iterator1 = list1.iterator(); iterator1.hasNext(); l1 += this.field_175250_f.fontRendererObj.FONT_HEIGHT)
            {
                s = (String)iterator1.next();
                j2 = this.field_175250_f.fontRendererObj.getStringWidth(s);
                this.field_175250_f.fontRendererObj.drawStringWithShadow(s, (float)(p_175249_1_ / 2 - j2 / 2), (float)l1, -1);
            }

            ++l1;
        }

        drawRect(p_175249_1_ / 2 - i2 / 2 - 1, l1 - 1, p_175249_1_ / 2 + i2 / 2 + 1, l1 + k3 * 9, Integer.MIN_VALUE);

        for (int l3 = 0; l3 < j3; ++l3)
        {
            int i4 = l3 / k3;
            j2 = l3 % k3;
            int k2 = k1 + i4 * j1 + i4 * 5;
            int l2 = l1 + j2 * 9;
            drawRect(k2, l2, k2 + j1, l2 + 8, 553648127);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

            if (l3 < list.size())
            {
                NetworkPlayerInfo networkplayerinfo1 = (NetworkPlayerInfo)list.get(l3);
                String s1 = this.func_175243_a(networkplayerinfo1);

                if (flag)
                {
                    this.field_175250_f.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
                    Gui.drawScaledCustomSizeModalRect(k2, l2, 8.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
                    EntityPlayer entityplayer = this.field_175250_f.theWorld.getPlayerEntityByUUID(networkplayerinfo1.getGameProfile().getId());

                    if (entityplayer != null && entityplayer.func_175148_a(EnumPlayerModelParts.HAT))
                    {
                        Gui.drawScaledCustomSizeModalRect(k2, l2, 40.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
                    }

                    k2 += 9;
                }

                if (networkplayerinfo1.getGameType() == WorldSettings.GameType.SPECTATOR)
                {
                    s1 = EnumChatFormatting.ITALIC + s1;
                    this.field_175250_f.fontRendererObj.drawStringWithShadow(s1, (float)k2, (float)l2, -1862270977);
                }
                else
                {
                    this.field_175250_f.fontRendererObj.drawStringWithShadow(s1, (float)k2, (float)l2, -1);
                }

                if (p_175249_3_ != null && networkplayerinfo1.getGameType() != WorldSettings.GameType.SPECTATOR)
                {
                    int j4 = k2 + j + 1;
                    int i3 = j4 + i1;

                    if (i3 - j4 > 5)
                    {
                        this.func_175247_a(p_175249_3_, l2, networkplayerinfo1.getGameProfile().getName(), j4, i3, networkplayerinfo1);
                    }
                }

                this.func_175245_a(j1, k2 - (flag ? 9 : 0), l2, networkplayerinfo1);
            }
        }

        if (list2 != null)
        {
            l1 += k3 * 9 + 1;
            drawRect(p_175249_1_ / 2 - i2 / 2 - 1, l1 - 1, p_175249_1_ / 2 + i2 / 2 + 1, l1 + list2.size() * this.field_175250_f.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

            for (iterator1 = list2.iterator(); iterator1.hasNext(); l1 += this.field_175250_f.fontRendererObj.FONT_HEIGHT)
            {
                s = (String)iterator1.next();
                j2 = this.field_175250_f.fontRendererObj.getStringWidth(s);
                this.field_175250_f.fontRendererObj.drawStringWithShadow(s, (float)(p_175249_1_ / 2 - j2 / 2), (float)l1, -1);
            }
        }
    }

    protected void func_175245_a(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo p_175245_4_)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.field_175250_f.getTextureManager().bindTexture(icons);
        byte b0 = 0;
        boolean flag = false;
        byte b1;

        if (p_175245_4_.getResponseTime() < 0)
        {
            b1 = 5;
        }
        else if (p_175245_4_.getResponseTime() < 150)
        {
            b1 = 0;
        }
        else if (p_175245_4_.getResponseTime() < 300)
        {
            b1 = 1;
        }
        else if (p_175245_4_.getResponseTime() < 600)
        {
            b1 = 2;
        }
        else if (p_175245_4_.getResponseTime() < 1000)
        {
            b1 = 3;
        }
        else
        {
            b1 = 4;
        }

        this.zLevel += 100.0F;
        this.drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0 + b0 * 10, 176 + b1 * 8, 10, 8);
        this.zLevel -= 100.0F;
    }

    private void func_175247_a(ScoreObjective p_175247_1_, int p_175247_2_, String p_175247_3_, int p_175247_4_, int p_175247_5_, NetworkPlayerInfo p_175247_6_)
    {
        int l = p_175247_1_.getScoreboard().getValueFromObjective(p_175247_3_, p_175247_1_).getScorePoints();

        if (p_175247_1_.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS)
        {
            this.field_175250_f.getTextureManager().bindTexture(icons);

            if (this.field_175253_j == p_175247_6_.func_178855_p())
            {
                if (l < p_175247_6_.func_178835_l())
                {
                    p_175247_6_.func_178846_a(Minecraft.getSystemTime());
                    p_175247_6_.func_178844_b((long)(this.field_175251_g.getUpdateCounter() + 20));
                }
                else if (l > p_175247_6_.func_178835_l())
                {
                    p_175247_6_.func_178846_a(Minecraft.getSystemTime());
                    p_175247_6_.func_178844_b((long)(this.field_175251_g.getUpdateCounter() + 10));
                }
            }

            if (Minecraft.getSystemTime() - p_175247_6_.func_178847_n() > 1000L || this.field_175253_j != p_175247_6_.func_178855_p())
            {
                p_175247_6_.func_178836_b(l);
                p_175247_6_.func_178857_c(l);
                p_175247_6_.func_178846_a(Minecraft.getSystemTime());
            }

            p_175247_6_.func_178843_c(this.field_175253_j);
            p_175247_6_.func_178836_b(l);
            int i1 = MathHelper.ceiling_float_int((float)Math.max(l, p_175247_6_.func_178860_m()) / 2.0F);
            int j1 = Math.max(MathHelper.ceiling_float_int((float)(l / 2)), Math.max(MathHelper.ceiling_float_int((float)(p_175247_6_.func_178860_m() / 2)), 10));
            boolean flag = p_175247_6_.func_178858_o() > (long)this.field_175251_g.getUpdateCounter() && (p_175247_6_.func_178858_o() - (long)this.field_175251_g.getUpdateCounter()) / 3L % 2L == 1L;

            if (i1 > 0)
            {
                float f = Math.min((float)(p_175247_5_ - p_175247_4_ - 4) / (float)j1, 9.0F);

                if (f > 3.0F)
                {
                    int k1;

                    for (k1 = i1; k1 < j1; ++k1)
                    {
                        this.drawTexturedModalRect((float)p_175247_4_ + (float)k1 * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                    }

                    for (k1 = 0; k1 < i1; ++k1)
                    {
                        this.drawTexturedModalRect((float)p_175247_4_ + (float)k1 * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);

                        if (flag)
                        {
                            if (k1 * 2 + 1 < p_175247_6_.func_178860_m())
                            {
                                this.drawTexturedModalRect((float)p_175247_4_ + (float)k1 * f, (float)p_175247_2_, 70, 0, 9, 9);
                            }

                            if (k1 * 2 + 1 == p_175247_6_.func_178860_m())
                            {
                                this.drawTexturedModalRect((float)p_175247_4_ + (float)k1 * f, (float)p_175247_2_, 79, 0, 9, 9);
                            }
                        }

                        if (k1 * 2 + 1 < l)
                        {
                            this.drawTexturedModalRect((float)p_175247_4_ + (float)k1 * f, (float)p_175247_2_, k1 >= 10 ? 160 : 52, 0, 9, 9);
                        }

                        if (k1 * 2 + 1 == l)
                        {
                            this.drawTexturedModalRect((float)p_175247_4_ + (float)k1 * f, (float)p_175247_2_, k1 >= 10 ? 169 : 61, 0, 9, 9);
                        }
                    }
                }
                else
                {
                    float f1 = MathHelper.clamp_float((float)l / 20.0F, 0.0F, 1.0F);
                    int l1 = (int)((1.0F - f1) * 255.0F) << 16 | (int)(f1 * 255.0F) << 8;
                    String s1 = "" + (float)l / 2.0F;

                    if (p_175247_5_ - this.field_175250_f.fontRendererObj.getStringWidth(s1 + "hp") >= p_175247_4_)
                    {
                        s1 = s1 + "hp";
                    }

                    this.field_175250_f.fontRendererObj.drawStringWithShadow(s1, (float)((p_175247_5_ + p_175247_4_) / 2 - this.field_175250_f.fontRendererObj.getStringWidth(s1) / 2), (float)p_175247_2_, l1);
                }
            }
        }
        else
        {
            String s2 = EnumChatFormatting.YELLOW + "" + l;
            this.field_175250_f.fontRendererObj.drawStringWithShadow(s2, (float)(p_175247_5_ - this.field_175250_f.fontRendererObj.getStringWidth(s2)), (float)p_175247_2_, 16777215);
        }
    }

    public void setFooter(IChatComponent p_175248_1_)
    {
        this.footer = p_175248_1_;
    }

    public void setHeader(IChatComponent p_175244_1_)
    {
        this.header = p_175244_1_;
    }

    @SideOnly(Side.CLIENT)
    static class PlayerComparator implements Comparator
        {
            private static final String __OBFID = "CL_00001941";

            private PlayerComparator() {}

            public int func_178952_a(NetworkPlayerInfo p_178952_1_, NetworkPlayerInfo p_178952_2_)
            {
                ScorePlayerTeam scoreplayerteam = p_178952_1_.getPlayerTeam();
                ScorePlayerTeam scoreplayerteam1 = p_178952_2_.getPlayerTeam();
                return ComparisonChain.start().compareTrueFirst(p_178952_1_.getGameType() != WorldSettings.GameType.SPECTATOR, p_178952_2_.getGameType() != WorldSettings.GameType.SPECTATOR).compare(scoreplayerteam != null ? scoreplayerteam.getRegisteredName() : "", scoreplayerteam1 != null ? scoreplayerteam1.getRegisteredName() : "").compare(p_178952_1_.getGameProfile().getName(), p_178952_2_.getGameProfile().getName()).result();
            }

            public int compare(Object p_compare_1_, Object p_compare_2_)
            {
                return this.func_178952_a((NetworkPlayerInfo)p_compare_1_, (NetworkPlayerInfo)p_compare_2_);
            }

            PlayerComparator(Object p_i45528_1_)
            {
                this();
            }
        }
}