package com.arkcraft.mod.core.machine.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.arkcraft.mod.core.Main;

/**
 * @author BubbleTrouble
 */

public class PageButton extends GuiButton{

	@SuppressWarnings("unused")
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation texture = new ResourceLocation(Main.MODID, "textures/gui/dino_book_gui.png");
    private final boolean nextPage;

    public PageButton(int id, int x, int y, boolean nextPage) {
        super(id, x, y, 16, 11,"");
        this.nextPage = nextPage;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
        	 boolean var4 = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
             GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
             mc.getTextureManager().bindTexture(texture);
             int var5 = 7;
             int var6 = 187;

             if (var4)
             {
                 var5 += 16;
             }

             if (this.nextPage)
             {
                 var6 += 11;
             }

             this.drawTexturedModalRect(this.xPosition, this.yPosition, var5, var6, 16, 11);
         }
     }
}


