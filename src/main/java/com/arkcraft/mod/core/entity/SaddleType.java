package com.arkcraft.mod.core.entity;

import net.minecraft.item.Item;

import com.arkcraft.mod.core.items.ModItems;

public enum SaddleType {
    NONE(0),
    SMALL(1),
    MEDIUM(2),
    LARGE(3);
    
	private int id;
	public static final int numSaddles = 3;
    
    SaddleType(int id) {
        this.id = id;
    }

	public int getSaddleID() {
		return id;
	}
	
	public int getInventorySize() {
		switch (id) {
			case 1:
				return 9;
			case 2:
				return 18;
			case 3:
				return 27;
			default:
				return 0;
		}
	}
	
	public Item getSaddleType() {
		switch (id) {
		case 1:
			return ModItems.saddle_small;
		case 2:
			return ModItems.saddle_medium;
		case 3:
			return ModItems.saddle_large;
		default:
			return null;
		}
	}
	
	public String toString() {
		switch (id) {
		case 1:
			return "small";
		case 2:
			return "medium";
		case 3:
			return "large";
		default:
			return "none";
		}
	}
}