package com.arkcraft.module.blocks.common.items;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.module.blocks.common.config.ModuleItemBalance;
import com.arkcraft.module.core.GlobalAdditions;
import com.arkcraft.module.core.client.gui.book.Dossier;
import com.arkcraft.module.creature.common.entity.SaddleType;

/**
 * @author wildbill22
 */
public class ARKCraftItems
{
	public static ItemARKFood tintoBerry, amarBerry, azulBerry, mejoBerry,
			narcoBerry, stimBerry, meat_raw, meat_cooked, primemeat_raw,
			primemeat_cooked, spoiled_meat;
	public static ItemARKSeed tintoBerrySeed, amarBerrySeed, azulBerrySeed, mejoBerrySeed, narcoBerrySeed, stimBerrySeed;
	public static ItemARKBase rock, cementing_pastes, fiber, chitin, narcotics,dodo_bag, dodo_feather, gun_powder, spark_powder, hide;
	public static ItemThatch thatch;
	public static ItemARKBase wood, metal, metal_ingot, flint;
	public static ItemFeces small_feces, medium_feces, large_feces, fertilizer, player_feces;
	public static ItemDinosaurEgg dodo_egg;
	public static ItemDinosaurSaddle saddle_small, saddle_medium, saddle_large;
	public static ItemARKArmor chitinHelm, chitinChest, chitinLegs,chitinBoots;
	public static ItemARKArmor clothHelm, clothChest, clothLegs, clothBoots;
	public static ItemARKArmor hideHelm, hideChest, hideLegs, hideBoots;
	public static ItemARKArmor furHelm, furChest, furLegs, furBoots;
	public static Dossier dino_book;
	public static ItemCrystal item_crystal;
	public static ItemSpyGlass spy_glass;

	//Armor
	public static ArmorMaterial CLOTH = EnumHelper.addArmorMaterial(
			"CLOTH_MAT", "CLOTH_MAT", 4, new int[] { 1, 2, 1, 1 }, 15);
	public static ArmorMaterial CHITIN = EnumHelper.addArmorMaterial(
			"CHITIN_MAT", "CHITIN_MAT", 16, new int[] { 3, 7, 6, 3 }, 10);
	public static ArmorMaterial HIDE = EnumHelper.addArmorMaterial
			("HIDE_MAT", "HIDE_MAT", 40, new int[] { 3, 8, 6, 3 }, 30);
	public static ArmorMaterial FUR = EnumHelper.addArmorMaterial(
			"FUR_MAT", "HIDE_MAT", 40, new int[] { 3, 8, 6, 3 }, 30);

	public static ARKCraftItems getInstance()
	{
		return new ARKCraftItems();
	}

	public static Map<String, Item> allItems = new HashMap<String, Item>();

	public static Map<String, Item> getAllItems()
	{
		return allItems;
	}

	public static void init()
	{
		// Food
		tintoBerry = addFood("tinto", 4, 0.3F, false, true);
		amarBerry = addFood("amar", 4, 0.3F, false, true);
		azulBerry = addFood("azul", 4, 0.3F, false, true);
		mejoBerry = addFood("mejo", 4, 0.3F, false, true);
		narcoBerry = addFood("narco", 4, 0.3F, true, true);
		stimBerry = addFood("stim", 4, 0.3F, true, true);
		meat_raw = addFood("meat_raw", 3, 0.3F, false, false);
		meat_cooked = addFood("meat_cooked", 6, 0.9F, false, false);
		primemeat_raw = addFood("primemeat_raw", 3, 0.3F, false, false);
		primemeat_cooked = addFood("primemeat_cooked", 8, 1.2F, false, false);
		spoiled_meat = addFood("spoiled_meat", 2, 0.1F, false, false);

		// Seeds
		tintoBerrySeed = addSeedItem("tintoBerrySeed");
		amarBerrySeed = addSeedItem("amarBerrySeed");
		azulBerrySeed = addSeedItem("azulBerrySeed");
		mejoBerrySeed = addSeedItem("mejoBerrySeed");
		narcoBerrySeed = addSeedItem("narcoBerrySeed");
		stimBerrySeed = addSeedItem("stimBerrySeed");

		// world generated

		
		// Regular Items
		rock = addItem("rock");
		cementing_pastes = addItem("cementing_paste");
		fiber = addItem("fiber");
		thatch = addThatchItem("thatch");
		wood = addItem("wood");
		metal = addItem("metal");
		metal_ingot = addItem("metal_ingot");
		flint = addItem("flint");
		chitin = addItem("chitin");
		dodo_feather = addItem("dodo_feather");
		dodo_bag = addItem("dodo_bag");
		gun_powder = addItem("gun_powder");
		spark_powder = addItem("spark_powder");
		hide = addItem("hide");
		spy_glass = addSpyGlass("spy_glass");

		// feces (2nd parameter is the seconds to decompose)
		//TODO
		small_feces = addFecesItem(
				"small_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_SMALL_FECES_TO_DECOMPOSE);
		medium_feces = addFecesItem(
				"medium_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_SMALL_FECES_TO_DECOMPOSE);
		large_feces = addFecesItem(
				"large_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_SMALL_FECES_TO_DECOMPOSE);
		player_feces = addFecesItem(
				"player_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_PLAYER_FECES_TO_DECOMPOSE);

		// Technically not feces, but used in all situations the same
		// (currently)
		fertilizer = addFecesItem("fertilizer",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_FERTILIZER_TO_DECOMPOSE);

		// Other Types of Items
		dodo_egg = addEggItem("dodo_egg");
		dino_book = addDossier("dossier");
		narcotics = addItem("narcotics");
		saddle_small = addSaddle("saddle_small");
		saddle_small.setSaddleType(SaddleType.SMALL);
		saddle_medium = addSaddle("saddle_medium");
		saddle_medium.setSaddleType(SaddleType.MEDIUM);
		saddle_large = addSaddle("saddle_large");
		saddle_large.setSaddleType(SaddleType.LARGE);

		// Armor
		chitinHelm = addArmorItem("chitin_helm", CHITIN, "chitinArmor", 0,false);
		chitinChest = addArmorItem("chitin_chest", CHITIN, "chitinArmor", 1,false);
		chitinLegs = addArmorItem("chitin_legs", CHITIN, "chitinArmor", 2,false);
		chitinBoots = addArmorItem("chitin_boots", CHITIN, "chitinArmor", 3,false);
		
		clothHelm = addArmorItem("cloth_helm", CLOTH, "clothArmor", 0, false);
		clothChest = addArmorItem("cloth_chest", CLOTH, "clothArmor", 1, false);
		clothLegs = addArmorItem("cloth_legs", CLOTH, "clothArmor", 2, false);
		clothBoots = addArmorItem("cloth_boots", CLOTH, "clothArmor", 3, false);
		
		hideHelm = addArmorItem("hide_helm", HIDE, "hideArmor", 0, false);
		hideChest = addArmorItem("hide_chest", HIDE, "hideArmor", 1, false);
		hideLegs = addArmorItem("hide_legs", HIDE, "hideArmor", 2, false);
		hideBoots = addArmorItem("hide_boots", HIDE, "hideArmor", 3, false);
		
		furHelm = addArmorItem("fur_helm", FUR, "furArmor", 0, false);
		furChest = addArmorItem("fur_chest", FUR, "furArmor", 1, false);
		furLegs = addArmorItem("fur_legs", FUR, "furArmor", 2, false);
		furBoots = addArmorItem("fur_boots", FUR, "furArmor", 3, false);
	}

	protected static ItemARKBase addItem(String name)
	{
		ItemARKBase i = new ItemARKBase();
		registerItem(name, i);
		return i;
	}

	protected static ItemThatch addThatchItem(String name)
	{
		ItemThatch t = new ItemThatch();
		registerItem(name, t);
		return t;
	}

	protected static ItemARKSeed addSeedItem(String name)
	{
		ItemARKSeed i = new ItemARKSeed();
		registerItem(name, i);
		return i;
	}

	protected static ItemDinosaurEgg addEggItem(String name)
	{
		ItemDinosaurEgg i = new ItemDinosaurEgg();
		registerItem(name, i);
		return i;
	}

	protected static ItemCrystal addCrystalItem(String name)
	{
		ItemCrystal i = new ItemCrystal();
		registerItem(name, i);
		return i;
	}

	protected static ItemSpyGlass addSpyGlass(String name)
	{
		ItemSpyGlass i = new ItemSpyGlass();
		registerItem(name, i);
		return i;
	}

	protected static ItemFeces addFecesItem(String name, int maxDamageIn)
	{
		ItemFeces i = new ItemFeces();
		i.setMaxDamage(maxDamageIn);
		registerItem(name, i);
		return i;
	}

	protected static ItemARKFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible)
	{
		ItemARKFood f = new ItemARKFood(heal, sat, fav, alwaysEdible);
		registerItem(name, f);
		return f;
	}

	protected static Dossier addDossier(String name)
	{
		Dossier dossier = new Dossier(name);
		registerItem(name, dossier);
		return dossier;
	}

	public static ItemARKFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible, PotionEffect... effect)
	{
		ItemARKFood f = new ItemARKFood(heal, sat, fav, alwaysEdible, effect);
		registerItem(name, f);
		return f;
	}

	public static ItemARKArmor addArmorItem(String name, ArmorMaterial mat, String armorTexName, int type, boolean golden)
	{
		ItemARKArmor item = new ItemARKArmor(mat, armorTexName, type, golden);
		registerItem(name, item);
		return item;
	}

	public static ItemDinosaurSaddle addSaddle(String name)
	{
		ItemDinosaurSaddle item = new ItemDinosaurSaddle();
		registerItem(name, item);
		return item;
	}

	public static void registerItem(String name, Item item)
	{
		allItems.put(name, item);
		item.setUnlocalizedName(name);
		item.setCreativeTab(GlobalAdditions.tabARKMisc);
		GameRegistry.registerItem(item, name);
	}
}
