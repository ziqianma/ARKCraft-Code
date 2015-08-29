package com.arkcraft.mod.core.handler;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StatCollector;

import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;

public class BookDrawHandler {
	
	public enum DINO_NAMES {
		ANKLOSAURUS("Ankylosaurus", "Ankylosaurus Crassacutis", "ark.book.ankylo.temperment", 0, 0),
		BRONTOSAURUS("Brontosaurus", "Brontosaurus Lazarus", "ark.book.bronto.temperment", 0, 1),
		CARNOTAURUS("Carnotaurus", "Carnotaurus Pressor", "ark.book.carno.temperment", 1, 2),
		DILOPHOSAURUS("Dilophosaur", "Dilophosaurus Sputatrix", "ark.book.dilo.temperment", 1, 3),
		UTAHRAPTOR("Raptor", "Utahraptor Prime", "ark.book.utahraptor.temperment", 1, 4),
		TRICERATOPS("Triceratops", "Triceratops Styrax", "ark.book.triceratops.temperment", 0, 5),
		TREX("Tyrannosaurus-Rex", "Tyrannosaurus Dominum", "ark.book.trex.temperment", 1, 6),
		CARBONEMYS("Carbonemys", "Carbonemys Obibimus", "ark.book.carbonemys.temperment", 0, 7),
		ICHTHYOSAURUS("Ichthyosaurus", "Ichthyosaurus Curiosa", "ark.book.ichthy.temperment", 1, 8);
		
		String temperment;
		String species;
		String name;
		int id;
		int type;
		
		DINO_NAMES(String dinoName, String species, String temperment, int type, int id) {
			this.name = dinoName;
			this.id = id;
			this.type = type;
			this.species = species;
			this.temperment = temperment;
		}
		
		public String getName() { return name; }
		public int getID() { return id; }
		public String getTemperment() { return StatCollector.translateToLocal(temperment); }
		public String getSpecies() { return species; }
		
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
		 * @return Carnivore, Herbivore or Omnivore
		 */
		public String getPreference() {
			switch(type) {
				case 0: return "Herbivore";
				case 1: return "Carnivore";
				case 2: return "Omnivore";
				default: return null;
			}
		}
	}
	
	public static int left = 132;
	public static int right = 256;
	
	public static void drawPages(FontRenderer renderer, int mouseX, int mouseY, int currentPage) {
		if(currentPage == 0) {
			/* Page 1 */
			drawTitle(renderer, true, DINO_NAMES.UTAHRAPTOR);
			GuiInventory.drawEntityOnScreen(getLeft() + 14, 90, 20, -350f, -5F, new EntityRaptor(Minecraft.getMinecraft().theWorld, 1));
			drawDiet(renderer, getLeft() + 8, 140, DINO_NAMES.UTAHRAPTOR);
			drawSpecies(renderer, getLeft() + 8, 150, DINO_NAMES.UTAHRAPTOR);
			
			/* Page 2 */
			drawTitle(renderer, false, DINO_NAMES.TRICERATOPS);
			
		}
		else if(currentPage == 2) {
			
		}
	}
	
	public static void drawSpecies(FontRenderer renderer, int x, int y, DINO_NAMES dinoName) {
		GlStateManager.pushMatrix();
		GL11.glScalef(0.7f, 0.7f, 1);
		renderer.drawString("Species: " + dinoName.getSpecies(), x, y, Color.darkGray.getRGB());
		GlStateManager.popMatrix();
	}
	
	public static void drawDiet(FontRenderer renderer, int x, int y, DINO_NAMES dinoName) {
        GlStateManager.pushMatrix(); /* Start the rendering */
		GL11.glScalef(0.7f, 0.7f, 1); /* 70% the normal size */
		renderer.drawString("Diet: " + dinoName.getPreference(), x, y, Color.darkGray.getRGB()); 
        GlStateManager.popMatrix(); /* End the rendering */
	}
	
	
	
	public static void drawTitle(FontRenderer renderer, boolean left, DINO_NAMES dinoName) {
		boolean right = !left;
		if(left) renderer.drawString(dinoName.getName(), getLeft() - dinoName.getName().length(), 45, Color.darkGray.getRGB());
		if(right) renderer.drawString(dinoName.getName(), getRight() - dinoName.getName().length(), 45, Color.darkGray.getRGB());
	}
	
	public static int getLeft() { return left; }
	public static int getRight() { return right; }
}
