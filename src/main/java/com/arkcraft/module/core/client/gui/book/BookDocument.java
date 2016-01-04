package com.arkcraft.module.core.client.gui.book;

import com.arkcraft.module.core.client.gui.book.pages.Page;

/**
 * @author Vastatio
 */
public class BookDocument
{

    public Page[] entries;

    public Page[] getEntries() { return entries; }

    public void setEntries(Page[] entries) { this.entries = entries; }

}
