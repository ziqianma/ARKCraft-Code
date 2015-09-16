package net.minecraft.client;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MinecraftError;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LoadingScreenRenderer implements IProgressUpdate
{
    private String message = "";
    /** A reference to the Minecraft object. */
    private Minecraft mc;
    /** The text currently displayed (i.e. the argument to the last call to printText or func_73722_d) */
    private String currentlyDisplayedText = "";
    /** The system's time represented in milliseconds. */
    private long systemTime = Minecraft.getSystemTime();
    private boolean field_73724_e;
    private ScaledResolution scaledResolution;
    private Framebuffer framebuffer;
    private static final String __OBFID = "CL_00000655";

    public LoadingScreenRenderer(Minecraft mcIn)
    {
        this.mc = mcIn;
        this.scaledResolution = new ScaledResolution(mcIn, mcIn.displayWidth, mcIn.displayHeight);
        this.framebuffer = new Framebuffer(mcIn.displayWidth, mcIn.displayHeight, false);
        this.framebuffer.setFramebufferFilter(9728);
    }

    /**
     * this string, followed by "working..." and then the "% complete" are the 3 lines shown. This resets progress to 0,
     * and the WorkingString to "working...".
     */
    public void resetProgressAndMessage(String p_73721_1_)
    {
        this.field_73724_e = false;
        this.displayString(p_73721_1_);
    }

    /**
     * Shows the 'Saving level' string.
     */
    public void displaySavingString(String message)
    {
        this.field_73724_e = true;
        this.displayString(message);
    }

    private void displayString(String p_73722_1_)
    {
        this.currentlyDisplayedText = p_73722_1_;

        if (!this.mc.running)
        {
            if (!this.field_73724_e)
            {
                throw new MinecraftError();
            }
        }
        else
        {
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.loadIdentity();

            if (OpenGlHelper.isFramebufferEnabled())
            {
                int i = this.scaledResolution.getScaleFactor();
                GlStateManager.ortho(0.0D, (double)(this.scaledResolution.getScaledWidth() * i), (double)(this.scaledResolution.getScaledHeight() * i), 0.0D, 100.0D, 300.0D);
            }
            else
            {
                ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
            }

            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0F, 0.0F, -200.0F);
        }
    }

    /**
     * Displays a string on the loading screen supposed to indicate what is being done currently.
     */
    public void displayLoadingString(String message)
    {
        if (!this.mc.running)
        {
            if (!this.field_73724_e)
            {
                throw new MinecraftError();
            }
        }
        else
        {
            this.systemTime = 0L;
            this.message = message;
            this.setLoadingProgress(-1);
            this.systemTime = 0L;
        }
    }

    /**
     * Updates the progress bar on the loading screen to the specified amount. Args: loadProgress
     */
    public void setLoadingProgress(int progress)
    {
        if (!this.mc.running)
        {
            if (!this.field_73724_e)
            {
                throw new MinecraftError();
            }
        }
        else
        {
            long j = Minecraft.getSystemTime();

            if (j - this.systemTime >= 100L)
            {
                this.systemTime = j;
                ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
                int k = scaledresolution.getScaleFactor();
                int l = scaledresolution.getScaledWidth();
                int i1 = scaledresolution.getScaledHeight();

                if (OpenGlHelper.isFramebufferEnabled())
                {
                    this.framebuffer.framebufferClear();
                }
                else
                {
                    GlStateManager.clear(256);
                }

                this.framebuffer.bindFramebuffer(false);
                GlStateManager.matrixMode(5889);
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 100.0D, 300.0D);
                GlStateManager.matrixMode(5888);
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0F, 0.0F, -200.0F);

                if (!OpenGlHelper.isFramebufferEnabled())
                {
                    GlStateManager.clear(16640);
                }

                try
                {
                if (!net.minecraftforge.fml.client.FMLClientHandler.instance().handleLoadingScreen(scaledresolution)) //FML Don't render while FML's pre-screen is rendering
                {
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                this.mc.getTextureManager().bindTexture(Gui.optionsBackground);
                float f = 32.0F;
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorOpaque_I(4210752);
                worldrenderer.addVertexWithUV(0.0D, (double)i1, 0.0D, 0.0D, (double)((float)i1 / f));
                worldrenderer.addVertexWithUV((double)l, (double)i1, 0.0D, (double)((float)l / f), (double)((float)i1 / f));
                worldrenderer.addVertexWithUV((double)l, 0.0D, 0.0D, (double)((float)l / f), 0.0D);
                worldrenderer.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
                tessellator.draw();

                if (progress >= 0)
                {
                    byte b0 = 100;
                    byte b1 = 2;
                    int j1 = l / 2 - b0 / 2;
                    int k1 = i1 / 2 + 16;
                    GlStateManager.disableTexture2D();
                    worldrenderer.startDrawingQuads();
                    worldrenderer.setColorOpaque_I(8421504);
                    worldrenderer.addVertex((double)j1, (double)k1, 0.0D);
                    worldrenderer.addVertex((double)j1, (double)(k1 + b1), 0.0D);
                    worldrenderer.addVertex((double)(j1 + b0), (double)(k1 + b1), 0.0D);
                    worldrenderer.addVertex((double)(j1 + b0), (double)k1, 0.0D);
                    worldrenderer.setColorOpaque_I(8454016);
                    worldrenderer.addVertex((double)j1, (double)k1, 0.0D);
                    worldrenderer.addVertex((double)j1, (double)(k1 + b1), 0.0D);
                    worldrenderer.addVertex((double)(j1 + progress), (double)(k1 + b1), 0.0D);
                    worldrenderer.addVertex((double)(j1 + progress), (double)k1, 0.0D);
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                }

                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.mc.fontRendererObj.drawStringWithShadow(this.currentlyDisplayedText, (float)((l - this.mc.fontRendererObj.getStringWidth(this.currentlyDisplayedText)) / 2), (float)(i1 / 2 - 4 - 16), 16777215);
                this.mc.fontRendererObj.drawStringWithShadow(this.message, (float)((l - this.mc.fontRendererObj.getStringWidth(this.message)) / 2), (float)(i1 / 2 - 4 + 8), 16777215);
                }
                }
                catch (java.io.IOException e)
                {
                    com.google.common.base.Throwables.propagate(e);
                } //FML End
                this.framebuffer.unbindFramebuffer();

                if (OpenGlHelper.isFramebufferEnabled())
                {
                    this.framebuffer.framebufferRender(l * k, i1 * k);
                }

                this.mc.updateDisplay();

                try
                {
                    Thread.yield();
                }
                catch (Exception exception)
                {
                    ;
                }
            }
        }
    }

    public void setDoneWorking() {}
}