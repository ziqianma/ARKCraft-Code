package com.arkcraft.mod.core.handler;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiInventory;

import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;

public class BookDrawHandler {
	
	public enum DINO_NAMES {
		ANKLOSAURUS("Ankylosaurus", 0, 0),
		BRONTOSAURUS("Brontosaurus", 0, 1),
		CARNOTAURUS("Carnotaurus", 1, 2),
		DILOPHOSAURUS("Dilophosaurus", 1, 3),
		UTAHRAPTORS("Utahraptor", 1, 4),
		TRICERATOPS("Triceratops", 0, 5),
		TREX("Tyrannosaurus-Rex", 1, 6),
		CARBONEMYS("Carbonemys", 0, 7),
		ICHTHYOSAURUS("Ichthyosaurus", 1, 8);
		
		
		String name;
		int id;
		int type;
		
		DINO_NAMES(String dinoName, int type, int id) {
			this.name = dinoName;
			this.id = id;
			this.type = type;
		}
		
		public String getName() { return name; }
		public int getID() { return id; }
		
		/***
		 * 
		 * @return Passive or Aggresive
		 */
		public String getType() {
			switch(type) {
				case 0: return "Passive";
				case 1: return "Aggresive";
				case 2: return "Passive";
				default: return null;
			}
		}
		
		/***
		 * 
		 * @return Carnivore, Herbivore or Piscivore
		 */
		public String getPreference() {
			switch(type) {
				case 0: return "Herbivore";
				case 1: return "Carnivore";
				case 2: return "Piscivore";
				default: return null;
			}
		}
	}
	
	public static int left = 132;
	public static int right = 256;
	
	public static void drawPages(FontRenderer renderer, int mouseX, int mouseY, int currentPage) {
		if(currentPage == 0) {
			/* Page 1 */
			drawTitle(renderer, true, DINO_NAMES.UTAHRAPTORS);
			GuiInventory.drawEntityOnScreen(getLeft() + 14, 90, 20, -350F, 10, new EntityRaptor(Minecraft.getMinecraft().theWorld, 1));
			drawDiet(renderer, getLeft() - 26, 110, DINO_NAMES.UTAHRAPTORS);
			
			/* Page 2 */
			drawTitle(renderer, false, DINO_NAMES.TRICERATOPS);
			
		}
		else if(currentPage == 2) {
			
		}
	}
	
	public static void drawDiet(FontRenderer renderer, int x, int y, DINO_NAMES dinoName) {
		renderer.FONT_HEIGHT = 5;
		renderer.drawString("Diet: " + dinoName.getPreference(), x, y, Color.darkGray.getRGB()); 
		renderer.FONT_HEIGHT = 9;
	}
	
	public static void drawTitle(FontRenderer renderer, boolean left, DINO_NAMES dinoName) {
		boolean right = !left;
		if(left) renderer.drawString(dinoName.getName(), getLeft() - dinoName.getName().length(), 45, Color.darkGray.getRGB());
		if(right) renderer.drawString(dinoName.getName(), getRight() - dinoName.getName().length(), 45, Color.darkGray.getRGB());
	}
	
	public static int getLeft() { return left; }
	public static int getRight() { return right; }
}
