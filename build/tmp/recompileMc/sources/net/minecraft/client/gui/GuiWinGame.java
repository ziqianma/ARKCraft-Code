package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.Charsets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class GuiWinGame extends GuiScreen
{
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation field_146576_f = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation field_146577_g = new ResourceLocation("textures/misc/vignette.png");
    private int field_146581_h;
    private List field_146582_i;
    private int field_146579_r;
    private float field_146578_s = 0.5F;
    private static final String __OBFID = "CL_00000719";

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        ++this.field_146581_h;
        float f = (float)(this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;

        if ((float)this.field_146581_h > f)
        {
            this.sendRespawnPacket();
        }
    }

    /**
     * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            this.sendRespawnPacket();
        }
    }

    private void sendRespawnPacket()
    {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
        this.mc.displayGuiScreen((GuiScreen)null);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return true;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        if (this.field_146582_i == null)
        {
            this.field_146582_i = Lists.newArrayList();

            try
            {
                String s = "";
                String s1 = "" + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + EnumChatFormatting.GREEN + EnumChatFormatting.AQUA;
                short short1 = 274;
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(this.mc.getResourceManager().getResource(new ResourceLocation("texts/end.txt")).getInputStream(), Charsets.UTF_8));
                Random random = new Random(8124371L);
                int i;

                while ((s = bufferedreader.readLine()) != null)
                {
                    String s2;
                    String s3;

                    for (s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername()); s.contains(s1); s = s2 + EnumChatFormatting.WHITE + EnumChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + s3)
                    {
                        i = s.indexOf(s1);
                        s2 = s.substring(0, i);
                        s3 = s.substring(i + s1.length());
                    }

                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, short1));
                    this.field_146582_i.add("");
                }

                for (i = 0; i < 8; ++i)
                {
                    this.field_146582_i.add("");
                }

                bufferedreader = new BufferedReader(new InputStreamReader(this.mc.getResourceManager().getResource(new ResourceLocation("texts/credits.txt")).getInputStream(), Charsets.UTF_8));

                while ((s = bufferedreader.readLine()) != null)
                {
                    s = s.replaceAll("PLAYERNAME", this.mc.getSession().getUsername());
                    s = s.replaceAll("\t", "    ");
                    this.field_146582_i.addAll(this.mc.fontRendererObj.listFormattedStringToWidth(s, short1));
                    this.field_146582_i.add("");
                }

                this.field_146579_r = this.field_146582_i.size() * 12;
            }
            catch (Exception exception)
            {
                logger.error("Couldn\'t load credits", exception);
            }
        }
    }

    private void drawWinGameScreen(int p_146575_1_, int p_146575_2_, float p_146575_3_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
        worldrenderer.startDrawingQuads();
        worldrenderer.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int k = this.width;
        float f1 = 0.0F - ((float)this.field_146581_h + p_146575_3_) * 0.5F * this.field_146578_s;
        float f2 = (float)this.height - ((float)this.field_146581_h + p_146575_3_) * 0.5F * this.field_146578_s;
        float f3 = 0.015625F;
        float f4 = ((float)this.field_146581_h + p_146575_3_ - 0.0F) * 0.02F;
        float f5 = (float)(this.field_146579_r + this.height + this.height + 24) / this.field_146578_s;
        float f6 = (f5 - 20.0F - ((float)this.field_146581_h + p_146575_3_)) * 0.005F;

        if (f6 < f4)
        {
            f4 = f6;
        }

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        f4 *= f4;
        f4 = f4 * 96.0F / 255.0F;
        worldrenderer.setColorOpaque_F(f4, f4, f4);
        worldrenderer.addVertexWithUV(0.0D, (double)this.height, (double)this.zLevel, 0.0D, (double)(f1 * f3));
        worldrenderer.addVertexWithUV((double)k, (double)this.height, (double)this.zLevel, (double)((float)k * f3), (double)(f1 * f3));
        worldrenderer.addVertexWithUV((double)k, 0.0D, (double)this.zLevel, (double)((float)k * f3), (double)(f2 * f3));
        worldrenderer.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, 0.0D, (double)(f2 * f3));
        tessellator.draw();
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawWinGameScreen(mouseX, mouseY, partialTicks);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        short short1 = 274;
        int k = this.width / 2 - short1 / 2;
        int l = this.height + 50;
        float f1 = -((float)this.field_146581_h + partialTicks) * this.field_146578_s;
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, f1, 0.0F);
        this.mc.getTextureManager().bindTexture(field_146576_f);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(k, l, 0, 0, 155, 44);
        this.drawTexturedModalRect(k + 155, l, 0, 45, 155, 44);
        worldrenderer.setColorOpaque_I(16777215);
        int i1 = l + 200;
        int j1;

        for (j1 = 0; j1 < this.field_146582_i.size(); ++j1)
        {
            if (j1 == this.field_146582_i.size() - 1)
            {
                float f2 = (float)i1 + f1 - (float)(this.height / 2 - 6);

                if (f2 < 0.0F)
                {
                    GlStateManager.translate(0.0F, -f2, 0.0F);
                }
            }

            if ((float)i1 + f1 + 12.0F + 8.0F > 0.0F && (float)i1 + f1 < (float)this.height)
            {
                String s = (String)this.field_146582_i.get(j1);

                if (s.startsWith("[C]"))
                {
                    this.fontRendererObj.drawStringWithShadow(s.substring(3), (float)(k + (short1 - this.fontRendererObj.getStringWidth(s.substring(3))) / 2), (float)i1, 16777215);
                }
                else
                {
                    this.fontRendererObj.fontRandom.setSeed((long)j1 * 4238972211L + (long)(this.field_146581_h / 4));
                    this.fontRendererObj.drawStringWithShadow(s, (float)k, (float)i1, 16777215);
                }
            }

            i1 += 12;
        }

        GlStateManager.popMatrix();
        this.mc.getTextureManager().bindTexture(field_146577_g);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0, 769);
        worldrenderer.startDrawingQuads();
        worldrenderer.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        j1 = this.width;
        int k1 = this.height;
        worldrenderer.addVertexWithUV(0.0D, (double)k1, (double)this.zLevel, 0.0D, 1.0D);
        worldrenderer.addVertexWithUV((double)j1, (double)k1, (double)this.zLevel, 1.0D, 1.0D);
        worldrenderer.addVertexWithUV((double)j1, 0.0D, (double)this.zLevel, 1.0D, 0.0D);
        worldrenderer.addVertexWithUV(0.0D, 0.0D, (double)this.zLevel, 0.0D, 0.0D);
        tessellator.draw();
        GlStateManager.disableBlend();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}