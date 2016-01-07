package com.arkcraft.module.core.client.gui.book.pages;

import com.arkcraft.module.core.client.gui.book.GuiDossier;
import com.arkcraft.module.core.client.gui.book.fonts.SmallFontRenderer;

/**
 * @author Vastatio
 */
public class Page implements IPage
{

    public Page() {}

    @Override
    public void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiDossier dossier) {}

    @Override
    public boolean equals(Object o) { return o == this; }
}
