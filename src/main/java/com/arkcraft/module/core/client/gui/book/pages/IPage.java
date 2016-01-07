package com.arkcraft.module.core.client.gui.book.pages;

import com.arkcraft.module.core.client.gui.book.GuiDossier;
import com.arkcraft.module.core.client.gui.book.fonts.SmallFontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Vastatio
 */
public interface IPage
{

    @SideOnly(Side.CLIENT)
    void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiDossier dossier);

    @Override
    boolean equals(Object o);
}
