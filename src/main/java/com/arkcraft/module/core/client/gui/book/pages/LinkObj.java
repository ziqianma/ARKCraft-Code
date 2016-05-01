package com.arkcraft.module.core.client.gui.book.pages;

import net.minecraft.util.ResourceLocation;

public class LinkObj
{

    public String linkText;
    public int linkTo;
    public String linkIcon;

    public String getLinkText() { return linkText; }

    public ResourceLocation getLinkIcon(String modid) { return new ResourceLocation(modid, linkIcon); }

    public String getLinkIconPath(String modid) { return modid + ":" + linkIcon; }

    public int getLinkPage() { return linkTo; }
}
