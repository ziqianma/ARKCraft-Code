package com.arkcraft.mod.core;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import com.arkcraft.mod.core.blocks.ARKBlock;
import com.arkcraft.mod.core.blocks.ARKBush;
import com.arkcraft.mod.core.blocks.ARKContainerBlock;
import com.arkcraft.mod.core.blocks.crop_test.ARKCropPlotBlock;
import com.arkcraft.mod.core.book.Dossier;
import com.arkcraft.mod.core.creativetabs.ARKTabs;
import com.arkcraft.mod.core.entity.EntityCobble;
import com.arkcraft.mod.core.entity.EntityDodoEgg;
import com.arkcraft.mod.core.entity.EntityExplosive;
import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.core.entity.passive.EntityBrontosaurus;
import com.arkcraft.mod.core.entity.passive.EntityDodo;
import com.arkcraft.mod.core.handlers.EntityHandler;
import com.arkcraft.mod.core.handlers.GenerationHandler;
import com.arkcraft.mod.core.handlers.GuiHandler;
import com.arkcraft.mod.core.handlers.RecipeHandler;
import com.arkcraft.mod.core.items.ARKArmorItem;
import com.arkcraft.mod.core.items.ARKBlockItem;
import com.arkcraft.mod.core.items.ARKEggItem;
import com.arkcraft.mod.core.items.ARKFecesItem;
import com.arkcraft.mod.core.items.ARKFood;
import com.arkcraft.mod.core.items.ARKItem;
import com.arkcraft.mod.core.items.ARKSaddle;
import com.arkcraft.mod.core.items.ARKSeedItem;
import com.arkcraft.mod.core.items.ARKSlingshot;
import com.arkcraft.mod.core.items.ARKWeapon;
import com.arkcraft.mod.core.items.ARKWeaponThrowable;
import com.arkcraft.mod.core.items.weapons.ItemCompoundBow;
import com.arkcraft.mod.core.items.weapons.ItemSpear;
import com.arkcraft.mod.core.items.weapons.ItemTranqGun;
import com.arkcraft.mod.core.items.weapons.bullets.ItemProjectile;
import com.arkcraft.mod.core.items.weapons.projectiles.EntitySpear;
import com.arkcraft.mod.core.items.weapons.projectiles.EntityTranquilizer;
import com.arkcraft.mod.core.lib.KeyBindings;

/**
 * @author Vastatio
 */
public class GlobalAdditions {

	public static Map<String, Block> allBlocks = new HashMap<String, Block>();
	public static Map<String, Item> allItems = new HashMap<String, Item>();

	public static ARKFood tintoBerry, amarBerry, azulBerry, mejoBerry, narcoBerry, porkchop_raw, porkchop_cooked, primemeat_raw, primemeat_cooked;
	public static ARKSeedItem tintoBerrySeed, amarBerrySeed, azulBerrySeed, mejoBerrySeed, narcoBerrySeed;
	public static ARKBush berryBush;
	public static ARKItem cobble_ball, fiber, chitin, narcotics, explosive_ball, dodo_bag, dodo_feather;
	public static ARKSlingshot slingshot;
	public static ARKFecesItem dodo_feces, player_feces;
	public static ARKEggItem dodo_egg;
	public static ARKSaddle saddle_small, saddle_medium, saddle_large;
	public static ARKArmorItem chitinHelm, chitinChest, chitinLegs, chitinBoots;
	public static ARKArmorItem clothHelm, clothChest, clothLegs, clothBoots;
	public static ARKArmorItem boneHelm, boneChest, boneLegs, boneBoots;
	public static ARKWeapon ironPike;
	public static ARKBlock oreSurface;
	public static Block blockNarcoBrerry;
	public static Dossier dino_book;
	public static ItemSpear	spear;
	public static ARKBlockItem item_crop_plot;
	
	public static ItemTranqGun tranq_gun;
	public static ItemCompoundBow compound_bow;
	public static ItemProjectile tranquilizer, stone_arrow, tranq_arrow, metal_arrow;
	
	public static ArmorMaterial CLOTH = EnumHelper.addArmorMaterial("CLOTH_MAT", "CLOTH_MAT", 4, new int[] {1,2,1,1}, 15);
	public static ArmorMaterial CHITIN = EnumHelper.addArmorMaterial("CHITIN_MAT", "CHITIN_MAT", 16, new int[] { 3,7,6,3 } , 10);
	public static ArmorMaterial BONE = EnumHelper.addArmorMaterial("BONE_MAT", "BONE_MAT", 40, new int[] { 3, 8, 6, 3 }, 30);
	
	public static CreativeTabs tabARK = new ARKTabs(CreativeTabs.getNextID(), "tabARKCraft");
	
	public static ARKContainerBlock smithy, pestle;
	public static ARKCropPlotBlock crop_plot;

	public enum GUI {
		SMITHY(0), PESTLE_AND_MORTAR(1), INV_DODO(2), BOOK_GUI(3), CROP_PLOT(4), TAMING_GUI(5);
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
		//stoneSpear = addWeaponThrowable("stoneSpear", ToolMaterial.STONE);
		ironPike = addWeapon("ironPike", ToolMaterial.IRON);

		// Containers
		smithy = addContainer("smithy", 0.4F, Material.wood, GUI.SMITHY.getID(), false, false, 3);
		pestle = addContainer("mortar_and_pestle", 0.4F, Material.rock, GUI.PESTLE_AND_MORTAR.getID(), false, false, 3);
		crop_plot = addCropPlotContainer("crop_plot", 0.4F, Material.wood, GUI.CROP_PLOT.getID(), false, false, 3);
		
		// Blocks
		oreSurface = addBlock(Material.rock, "oreSurface", 3.0F);
		//blockNarcoBrerry = addBlock(Material.ground, "narcoBerryBlock", 3.0F);
		blockNarcoBrerry = getRegisteredBlock("blockNarcoBerry");

		// Regular Items
		fiber = addItem("fiber");
		chitin = addItem("chitin");
		dodo_feather = addItem("dodo_feather");
		dodo_bag = addItemWithTooltip("dodo_bag", "Backpack for the Dodo");
		
		//Block Items
		item_crop_plot = addBlockItem("item_crop_plot");
		
		//Guns
		tranq_gun = addTranqGun("tranq_gun");
		compound_bow = new ItemCompoundBow("compound_bow");
			
		//Bullets
		tranquilizer = addItemProjectile("tranquilizer");
		tranq_arrow = addItemProjectile("tranq_arrow");
		stone_arrow = addItemProjectile("stone_arrow");
		metal_arrow = addItemProjectile("metal_arrow");
		
		spear = addSpearItem("spear", ToolMaterial.STONE);
		
		// Other Types of Items
		dodo_egg = addEggItem("dodo_egg");
		dodo_feces = addFecesItem("dodo_feces");
		player_feces = addFecesItem("player_feces");
		dino_book = addDossier("dossier");
		narcotics = addItemWithTooltip("narcotics", EnumChatFormatting.RED + "A Knockout of a Drink");
		saddle_small = addSaddle("saddle_small");
		saddle_medium = addSaddle("saddle_medium");
		saddle_large = addSaddle("saddle_large");
		
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

		// Handlers
		RecipeHandler.registerVanillaCraftingRecipes();
		RecipeHandler.registerPestleCraftingRecipes();
		RecipeHandler.registerSmithyCraftingRecipes();
		EntityHandler.registerModEntity(EntityExplosive.class, "Explosive Cobblestone Ball", Main.instance, 64, 10, true);
		
		EntityHandler.registerModEntity(EntitySpear.class, "Spear", Main.instance, 64, 10, true);
		EntityHandler.registerModEntity(EntityTranquilizer .class, "Tranquilizer ", Main.instance, 64, 10, true);
	//	EntityHandler.registerModEntity(EntitySimpleBullet.class, "Simple Bullet", Main.instance, 64, 10, true);
	//	EntityHandler.registerModEntity(EntitySimpleRifleAmmo.class, "Simple Rifle Ammo", Main.instance, 64, 10, true);
	//	EntityHandler.registerModEntity(EntitySimpleShotgunAmmo.class, "Simple Shotgun Ammo", Main.instance, 64, 10, true);
		
		EntityHandler.registerModEntity(EntityCobble.class, "Cobblestone Ball", Main.instance, 64, 10, true);
		EntityHandler.registerModEntity(EntityDodoEgg.class, "Dodo Egg", Main.instance, 64, 10, true);
		
		EntityHandler.registerEntityEgg(EntityRaptor.class, "raptor");
		EntityHandler.registerEntityEgg(EntityDodo.class, "dodo", BiomeGenBase.beach, BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.birchForest, BiomeGenBase.extremeHills);
		EntityHandler.registerEntityEgg(EntityBrontosaurus.class, "brontosaurus");
		//EntityHandler.registerMonster(EntityCoelacanth.class, "coelacanth", BiomeGenBase.deepOcean, BiomeGenBase.ocean, BiomeGenBase.river);
		
		KeyBindings.preInit();
		GenerationHandler.addOreToGen(oreSurface, 0); //Sets to the values in BALENCE.GEN.class
		
		// Other Stuff
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
	}
	protected static Block getRegisteredBlock(String name)
	{
		return (Block)Block.blockRegistry.getObject(new ResourceLocation(name));
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
	
	protected static ARKCropPlotBlock addCropPlotContainer(String name, float hardness, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType) {
		ARKCropPlotBlock container = new ARKCropPlotBlock(name, hardness, mat, ID);
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
	
	protected static ARKEggItem addEggItem(String name) {
		ARKEggItem i = new ARKEggItem(name);
		allItems.put(name, i);
		return i;
	}	
	protected static ARKBlockItem addBlockItem(String name) {
		ARKBlockItem i = new ARKBlockItem(name);
		allItems.put(name, i);
		return i;
	}	
	public static ItemSpear addSpearItem(String name, ToolMaterial mat) {
		ItemSpear weapon = new ItemSpear(name, mat);
		allItems.put(name, weapon);
		return weapon;
	}
	
	protected static ARKFecesItem addFecesItem(String name) {
		ARKFecesItem i = new ARKFecesItem(name);
		allItems.put(name, i);
		return i;
	}	
	
	protected static ARKFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible) {
		ARKFood f = new ARKFood(name, heal, sat, fav, alwaysEdible);
		allItems.put(name, f);
		return f;
	}
	
	protected static Dossier addDossier(String name) {
		Dossier dossier = new Dossier(name);
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
	public static ItemTranqGun addTranqGun(String name) {
		ItemTranqGun item = new ItemTranqGun(name);
		allItems.put(name, item);
		return item;
	}
	public static ItemProjectile  addItemProjectile (String name) {
		ItemProjectile item = new ItemProjectile(name);
		allItems.put(name, item);
		return item;
	}
	public static ARKWeapon addWeapon(String name, ToolMaterial mat) {
		ARKWeapon weapon = new ARKWeapon(name, mat);
		allItems.put(name, weapon);
		return weapon;
	}
	public static ARKWeaponThrowable addWeaponThrowable(String name, ToolMaterial mat) {
		ARKWeaponThrowable weapon = new ARKWeaponThrowable(name, mat);
		allItems.put(name, weapon);
		return weapon;
	}
	
	public static GlobalAdditions getInstance() { return new GlobalAdditions(); }
	public static Map<String, Block> getAllBlocks() { return allBlocks; }
	public static Map<String, Item> getAllItems() { return allItems; }
	
}
