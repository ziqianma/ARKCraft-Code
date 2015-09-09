package com.arkcraft.mod.core.book;

import com.google.common.collect.HashBiMap;

public class BookDataStore {
	
	private static HashBiMap<String, BookData> data = HashBiMap.create();
    
	public static void addBook (BookData bd) {
        data.put(bd.getUnlocalizedName().substring(5), bd);
    }

    public static BookData getBookFromName (String fullBookName) {
        return data.get(fullBookName);
    }
}
