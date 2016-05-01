package com.arkcraft.module.core.client.gui.book;

import com.arkcraft.lib.LogHelper;
import com.google.common.collect.HashBiMap;

/**
 * @author Vastatio
 */
public class BookDataStore
{

    private static HashBiMap<String, BookData> data = HashBiMap.create();

    public static void addBook(BookData bd)
    {
        LogHelper.info("Adding a book with the name: " + bd.getUnlocalizedName());
        data.put(bd.getUnlocalizedName(), bd);
    }

    public static BookData getBookFromName(String fullBookName)
    {
        LogHelper.info("Retrieving a book with the name: " + fullBookName);
        return data.get(fullBookName);
    }
}
