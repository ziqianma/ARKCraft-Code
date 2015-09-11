package com.arkcraft.mod.core.book;

import com.arkcraft.mod.core.lib.LogHelper;
import com.google.common.collect.HashBiMap;

public class BookDataStore {
	
	private static HashBiMap<String, BookData> data = HashBiMap.create();
    
	public static void addBook (BookData bd) {
		LogHelper.info("Adding a book with the name: " +  bd.getUnlocalizedName());
        data.put(bd.getUnlocalizedName(), bd);
    }

    public static BookData getBookFromName (String fullBookName) {
        LogHelper.info("Retrieving a book with the name: " + fullBookName);
    	return data.get(fullBookName);
    }
}
