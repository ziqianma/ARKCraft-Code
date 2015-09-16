package net.minecraft.client.gui.spectator.categories;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.gui.spectator.ISpectatorMenuObject;
import net.minecraft.client.gui.spectator.ISpectatorMenuView;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TeleportToTeam implements ISpectatorMenuView, ISpectatorMenuObject
{
    private final List field_178672_a = Lists.newArrayList();
    private static final String __OBFID = "CL_00001920";

    public TeleportToTeam()
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        Iterator iterator = minecraft.theWorld.getScoreboard().getTeams().iterator();

        while (iterator.hasNext())
        {
            ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)iterator.next();
            this.field_178672_a.add(new TeleportToTeam.TeamSelectionObject(scoreplayerteam));
        }
    }

    public List func_178669_a()
    {
        return this.field_178672_a;
    }

    public IChatComponent func_178670_b()
    {
        return new ChatComponentText("Select a team to teleport to");
    }

    public void func_178661_a(SpectatorMenu p_178661_1_)
    {
        p_178661_1_.func_178647_a(this);
    }

    public IChatComponent func_178664_z_()
    {
        return new ChatComponentText("Teleport to team member");
    }

    public void func_178663_a(float p_178663_1_, int p_178663_2_)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 16.0F, 0.0F, 16, 16, 256.0F, 256.0F);
    }

    public boolean func_178662_A_()
    {
        Iterator iterator = this.field_178672_a.iterator();
        ISpectatorMenuObject ispectatormenuobject;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            ispectatormenuobject = (ISpectatorMenuObject)iterator.next();
        }
        while (!ispectatormenuobject.func_178662_A_());

        return true;
    }

    @SideOnly(Side.CLIENT)
    class TeamSelectionObject implements ISpectatorMenuObject
    {
        private final ScorePlayerTeam field_178676_b;
        private final ResourceLocation field_178677_c;
        private final List field_178675_d;
        private static final String __OBFID = "CL_00001919";

        public TeamSelectionObject(ScorePlayerTeam p_i45492_2_)
        {
            this.field_178676_b = p_i45492_2_;
            this.field_178675_d = Lists.newArrayList();
            Iterator iterator = p_i45492_2_.getMembershipCollection().iterator();

            while (iterator.hasNext())
            {
                String s = (String)iterator.next();
                NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().func_175104_a(s);

                if (networkplayerinfo != null)
                {
                    this.field_178675_d.add(networkplayerinfo);
                }
            }

            if (!this.field_178675_d.isEmpty())
            {
                String s1 = ((NetworkPlayerInfo)this.field_178675_d.get((new Random()).nextInt(this.field_178675_d.size()))).getGameProfile().getName();
                this.field_178677_c = AbstractClientPlayer.getLocationSkin(s1);
                AbstractClientPlayer.getDownloadImageSkin(this.field_178677_c, s1);
            }
            else
            {
                this.field_178677_c = DefaultPlayerSkin.getDefaultSkinLegacy();
            }
        }

        public void func_178661_a(SpectatorMenu p_178661_1_)
        {
            p_178661_1_.func_178647_a(new TeleportToPlayer(this.field_178675_d));
        }

        public IChatComponent func_178664_z_()
        {
            return new ChatComponentText(this.field_178676_b.func_96669_c());
        }

        public void func_178663_a(float p_178663_1_, int p_178663_2_)
        {
            int j = -1;
            String s = FontRenderer.getFormatFromString(this.field_178676_b.getColorPrefix());

            if (s.length() >= 2)
            {
                j = Minecraft.getMinecraft().fontRendererObj.getColorCode(s.charAt(1));
            }

            if (j >= 0)
            {
                float f1 = (float)(j >> 16 & 255) / 255.0F;
                float f2 = (float)(j >> 8 & 255) / 255.0F;
                float f3 = (float)(j & 255) / 255.0F;
                Gui.drawRect(1, 1, 15, 15, MathHelper.func_180183_b(f1 * p_178663_1_, f2 * p_178663_1_, f3 * p_178663_1_) | p_178663_2_ << 24);
            }

            Minecraft.getMinecraft().getTextureManager().bindTexture(this.field_178677_c);
            GlStateManager.color(p_178663_1_, p_178663_1_, p_178663_1_, (float)p_178663_2_ / 255.0F);
            Gui.drawScaledCustomSizeModalRect(2, 2, 8.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
            Gui.drawScaledCustomSizeModalRect(2, 2, 40.0F, 8.0F, 8, 8, 12, 12, 64.0F, 64.0F);
        }

        public boolean func_178662_A_()
        {
            return !this.field_178675_d.isEmpty();
        }
    }
}