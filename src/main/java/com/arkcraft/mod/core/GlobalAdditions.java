package com.arkcraft.mod.core;

import java.util.HashMap;
import java.util.Map;

import com.arkcraft.mod.core.blocks.ARKBlock;
import com.arkcraft.mod.core.blocks.ARKBush;
import com.arkcraft.mod.core.blocks.ARKContainerBlock;
import com.arkcraft.mod.core.creativetabs.ARKTabs;
import com.arkcraft.mod.core.entity.EntityCobble;
import com.arkcraft.mod.core.entity.EntityExplosive;
import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.core.entity.passive.EntityDodo;
import com.arkcraft.mod.core.handler.EntityHandler;
import com.arkcraft.mod.core.handler.GenerationHandler;
import com.arkcraft.mod.core.handler.GuiHandler;
import com.arkcraft.mod.core.handler.RecipeHandler;
import com.arkcraft.mod.core.items.ARKArmorItem;
import com.arkcraft.mod.core.items.ARKFood;
import com.arkcraft.mod.core.items.ARKItem;
import com.arkcraft.mod.core.items.ARKSaddle;
import com.arkcraft.mod.core.items.ARKSlingshot;
import com.arkcraft.mod.core.items.ARKWeapon;
import com.arkcraft.mod.core.items.ItemDossier;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * @author Vastatio
 */
public class GlobalAdditions {

	public static Map<String, Block> allBlocks = new HashMap<String, Block>();
	public static Map<String, Item> allItems = new HashMap<String, Item>();

	public static ARKFood tintoBerry, amarBerry, azulBerry, mejoBerry, narcoBerry, porkchop_raw, porkchop_cooked, primemeat_raw, primemeat_cooked;
	public static ARKBush berryBush;
	public static ARKItem cobble_ball, fiber, chitin, narcotics, tranq_arrow, explosive_ball, dodo_bag, dodo_egg, dodo_feather;
	public static ARKSlingshot slingshot;
	public static ARKSaddle saddle_small, saddle_medium, saddle_large;
	public static ARKArmorItem chitinHelm, chitinChest, chitinLegs, chitinBoots;
	public static ARKArmorItem clothHelm, clothChest, clothLegs, clothBoots;
	public static ARKArmorItem boneHelm, boneChest, boneLegs, boneBoots;
	public static ARKWeapon stoneSpear, ironPike;
	public static ARKBlock oreSurface;
	public static ItemDossier dinoBook;
	
	public static ArmorMaterial CLOTH = EnumHelper.addArmorMaterial("CLOTH_MAT", "CLOTH_MAT", 4, new int[] {1,2,1,1}, 15);
	public static ArmorMaterial CHITIN = EnumHelper.addArmorMaterial("CHITIN_MAT", "CHITIN_MAT", 16, new int[] { 3,7,6,3 } , 10);
	public static ArmorMaterial BONE = EnumHelper.addArmorMaterial("BONE_MAT", "BONE_MAT", 40, new int[] { 3, 8, 6, 3 }, 30);
	
	public static CreativeTabs tabARK = new ARKTabs(CreativeTabs.getNextID(), "tabARKCraft");
	
	public static ARKContainerBlock smithy, pestle;
	
	public enum GUI {
		SMITHY(0), PESTLE_AND_MORTAR(1), INV_DODO(2), BOOK_GUI(3);
		int id;
		GUI(int id) {
			this.id = id;
		}
		
		public int getID() { return id; }
	}
	
	public static void init() {
		// Food
		tintoBerry = addFood("tinto", 4, 0.3F, false, true, new PotionEffect(Potion.fireResistance.id, 60, 1));
		amarBerry = addFood("amar", 4, 0.3F, false, true, new PotionEffect(Potion.absorption.id, 100, 1));
		azulBerry = addFood("azul", 4, 0.3F, false, true, new PotionEffect(Potion.jump.id, 60, 1));
		mejoBerry = addFood("mejo", 4, 0.3F, false, true, new PotionEffect(Potion.resistance.id, 100, 1));
		narcoBerry = addFood("narco", 4, 0.3F, true, true, new PotionEffect(Potion.moveSpeed.id, 160, 1));
		porkchop_raw = addFood("porkchop_raw", 3, 0.3F, false, false);
		porkchop_cooked = addFood("porkchop_cooked", 6, 0.9F, false, false);
		primemeat_raw = addFood("primemeat_raw", 3, 0.3F, false, false);
		primemeat_cooked = addFood("primemeat_cooked", 8, 1.2F, false, false);
		
		// world generated
		berryBush = addBush("berryBush", 0.4F);
		
		// Weapons and tools
		cobble_ball = addItemWithTooltip("cobble_ball", EnumChatFormatting.GOLD + "A Rocky Road to Victory");
		explosive_ball = addItemWithTooltip("explosive_ball", EnumChatFormatting.RED + "A Rocky Road to Destruction");
		slingshot = addSlingshot("slingshot");
		narcotics = addItemWithTooltip("narcotics", EnumChatFormatting.RED + "A Knockout of a Drink");
		saddle_small = addSaddle("saddle_small");
		saddle_medium = addSaddle("saddle_medium");
		saddle_large = addSaddle("saddle_large");
		
		smithy = addContainer("smithy", 0.4F, Material.wood, GUI.SMITHY.getID(), false, false, 3);
		pestle = addContainer("mortar_and_pestle", 0.4F, Material.rock, GUI.PESTLE_AND_MORTAR.getID(), false, false, 3);
		
		oreSurface = addBlock(Material.rock, "oreSurface", 3.0F);
		
		stoneSpear = addWeapon("stoneSpear", ToolMaterial.STONE);
		ironPike = addWeapon("ironPike", ToolMaterial.IRON);
		
		// Regular Items
		fiber = addItem("fiber");
		chitin = addItem("chitin");
		tranq_arrow = addItem("tranq_arrow");
		dodo_bag = addItemWithTooltip("dodo_bag", "Backpack for the Dodo");
		dodo_egg = addItem("dodo_egg");
		dodo_feather = addItem("dodo_feather");

		dinoBook = addDossier("dinoBook", GUI.BOOK_GUI.getID(), EnumChatFormatting.GOLD + "Knowledge is Power");
		
		// Armor
		chitinHelm = addArmorItem("chitin_helm", CHITIN, "chitinArmor", 0);
		chitinChest = addArmorItem("chitin_chest", CHITIN, "chitinArmor", 1);
		chitinLegs = addArmorItem("chitin_legs", CHITIN, "chitinArmor", 2);
		chitinBoots = addArmorItem("chitin_boots", CHITIN, "chitinArmor", 3);
		clothHelm = addArmorItem("cloth_helm", CLOTH, "clothArmor", 0);
		clothChest = addArmorItem("cloth_chest", CLOTH, "clothArmor", 1);
		clothLegs = addArmorItem("cloth_legs", CLOTH, "clothArmor", 2);
		clothBoots = addArmorItem("cloth_boots", CLOTH, "clothArmor", 3);
		boneHelm = addArmorItem("bone_helm", BONE, "boneArmor", 0, true, EnumChatFormatting.DARK_RED + "Armor of the Ancients");
		boneChest = addArmorItem("bone_chest", BONE, "boneArmor", 1, true, EnumChatFormatting.DARK_RED + "Armor of the Ancients");
		boneLegs = addArmorItem("bone_legs", BONE, "boneArmor", 2, true, EnumChatFormatting.DARK_RED + "Armor of the Ancients");
		boneBoots = addArmorItem("bone_boots", BONE, "boneArmor", 3, true, EnumChatFormatting.DARK_RED + "Armor of the Ancients");
		
		// Other stuff
		RecipeHandler.registerVanillaCraftingRecipes();
		EntityHandler.registerModEntity(EntityExplosive.class, "Explosive Cobblestone Ball", 1, Main.instance, 64, 10, true);
		EntityHandler.registerModEntity(EntityCobble.class, "Cobblestone Ball", 2, Main.instance, 64, 10, true);
		
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
		
		EntityHandler.registerMonster(EntityRaptor.class, "raptor");
		EntityHandler.registerPassive(EntityDodo.class, "dodo");
		GenerationHandler.addOreToGen(oreSurface, 0); //5,5
	
	}
	
	protected static ARKBush addBush(String name, float hardness) {
		ARKBush b = new ARKBush(name, hardness);
		allBlocks.put(name, b);
		return b;
	}
	
	protected static ARKSlingshot addSlingshot(String name) {
		ARKSlingshot slingshot = new ARKSlingshot(name);
		allItems.put(name, slingshot);
		return slingshot;
	}
	
	protected static ARKBlock addBlock(Material m, String name, float hardness) {
		ARKBlock b = new ARKBlock(m, name, hardness);
		allBlocks.put(name, b);
		return b;
	}
	
	protected static ARKContainerBlock addContainer(String name, float hardness, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType) {
		ARKContainerBlock container = new ARKContainerBlock(name, hardness, mat, ID);
		container.setRenderAsNormalBlock(renderAsNormalBlock);
		container.setOpaque(isOpaque);
		container.setRenderType(renderType);
		allBlocks.put(name, container);
		return container;
	}
	
	protected static ARKItem addItem(String name) {
		ARKItem i = new ARKItem(name);
		allItems.put(name, i);
		return i;
	}
	
	protected static ARKFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible) {
		ARKFood f = new ARKFood(name, heal, sat, fav, alwaysEdible);
		allItems.put(name, f);
		return f;
	}
	
	protected static ItemDossier addDossier(String name, int guiID, String... tooltips) {
		ItemDossier dossier = new ItemDossier(name, guiID);
		dossier.addMoreInformation(tooltips);
		allItems.put(name, dossier);
		return dossier;
	}
	
	public static ARKItem addItemWithTooltip(String name, String... tooltips) {
		ARKItem item = new ARKItem(name, tooltips);
		allItems.put(name, item);
		return item;
	}
	
	public static ARKFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible, PotionEffect... effect) {
		ARKFood f = new ARKFood(name, heal, sat, fav, alwaysEdible, effect);
		allItems.put(name, f);
		return f;
			
	}
	
	public static ARKArmorItem addArmorItem(String name, ArmorMaterial mat, String armorTexName, int type) {
		return addArmorItem(name, mat, armorTexName, type, false);
	}
	
	public static ARKArmorItem addArmorItem(String name, ArmorMaterial mat, String armorTexName, int type, boolean golden) {
		return addArmorItem(name, mat, armorTexName, type, false, EnumChatFormatting.ITALIC + "Armor, Made to Fit");
	}
	
	public static ARKArmorItem addArmorItem(String name, ArmorMaterial mat, String armorTexName, int type, boolean golden, String... tooltips) {
		ARKArmorItem item = new ARKArmorItem(name, mat, armorTexName, type, golden, tooltips);
		allItems.put(name, item);
		return item;
	}
	
	public static ARKSaddle addSaddle(String name) {
		ARKSaddle item = new ARKSaddle(name);
		allItems.put(name, item);
		return item;
	}
	
	public static ARKWeapon addWeapon(String name, ToolMaterial mat) {
		ARKWeapon weapon = new ARKWeapon(name, mat);
		allItems.put(name, weapon);
		return weapon;
	}
	
	public static GlobalAdditions getInstance() { return new GlobalAdditions(); }
	public static Map<String, Block> getAllBlocks() { return allBlocks; }
	public static Map<String, Item> getAllItems() { return allItems; }
	
}
