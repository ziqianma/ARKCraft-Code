package com.arkcraft.mod.client.gui.overlay;

import com.arkcraft.mod.common.ARKCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiOverlay extends Gui
{
    private static final ResourceLocation overlayTextures = new ResourceLocation(ARKCraft.MODID, "textures/gui/hud/overlay.png");
    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void overlay(RenderGameOverlayEvent event)
    {
        if (event.type == RenderGameOverlayEvent.ElementType.HEALTH)
        {
            event.setCanceled(true);

            ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

            mc.getTextureManager().bindTexture(overlayTextures);
            EntityPlayerSP player = mc.thePlayer;

            int scaledWidth = res.getScaledWidth();
            int scaledHeight = res.getScaledHeight();

            drawTexturedModalRect(scaledWidth - 20, scaledHeight - 20, 15, 0, 15, 15);
            drawTexturedModalRect(scaledWidth - 20, scaledHeight - 20, 0, 0, 15, 15 - getScaled((int) player.getHealth(), (int) player.getMaxHealth(), 15));

            drawTexturedModalRect(scaledWidth - 20, scaledHeight - 40, 45, 0, 15, 15);
            drawTexturedModalRect(scaledWidth - 20, scaledHeight - 40, 30, 0, 15, 15 - getScaled(player.getFoodStats().getFoodLevel(), 20, 15));

            if (player.getAir() < 300)
            {
                drawTexturedModalRect(scaledWidth - 20, scaledHeight - 60, 75, 0, 15, 15);
                drawTexturedModalRect(scaledWidth - 20, scaledHeight - 60, 60, 0, 15, 15 - getScaled(player.getAir(), 300, 15));
            }
        }
    }

    private int getScaled(int value, int maxValue, int scale)
    {
        return value != 0 && maxValue != 0 ? value * scale / maxValue : 0;
    }
}
