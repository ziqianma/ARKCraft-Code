package com.arkcraft.mod.core.book;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IPage {
	
	@SideOnly(Side.CLIENT)
	void draw(int guiLeft, int guiTop, int mouseX, int mouseY, SmallFontRenderer renderer, boolean canTranslate, GuiDossier dossier);

	@Override
    boolean equals(Object o);
}
