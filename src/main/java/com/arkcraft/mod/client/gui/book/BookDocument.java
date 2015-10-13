package com.arkcraft.mod.client.gui.book;

import com.arkcraft.mod.client.gui.book.pages.Page;
/***
 * 
 * @author Vastatio
 *
 */
public class BookDocument {
	
	public Page[] entries;

	public Page[] getEntries() { return entries; }
	public void setEntries(Page[] entries) { this.entries = entries; }
	
}
