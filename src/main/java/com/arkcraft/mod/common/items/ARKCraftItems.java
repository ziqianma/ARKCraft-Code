package com.arkcraft.mod.common.items;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.client.gui.book.Dossier;
import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.handlers.EntityHandler;
import com.arkcraft.mod.common.items.weapons.ItemCompoundBow;
import com.arkcraft.mod.common.items.weapons.ItemSpear;
import com.arkcraft.mod.common.items.weapons.ItemSpyGlass;
import com.arkcraft.mod.common.items.weapons.bullets.ItemProjectile;
import com.arkcraft.mod.common.items.weapons.component.ItemShooter;
import com.arkcraft.mod.common.items.weapons.component.RangedCompCrossbow;
import com.arkcraft.mod.common.items.weapons.component.RangedCompLongneckRifle;
import com.arkcraft.mod.common.items.weapons.component.RangedCompRocketLauncher;
import com.arkcraft.mod.common.items.weapons.component.RangedCompShotgun;
import com.arkcraft.mod.common.items.weapons.component.RangedCompSimplePistol;
import com.arkcraft.mod.common.items.weapons.component.RangedCompSpyGlass;
import com.arkcraft.mod.common.items.weapons.component.RangedCompTranqGun;
import com.arkcraft.mod.common.items.weapons.component.RangedComponent;
import com.arkcraft.mod.common.items.weapons.projectiles.EntityMetalArrow;
import com.arkcraft.mod.common.items.weapons.projectiles.EntityRocketPropelledGrenade;
import com.arkcraft.mod.common.items.weapons.projectiles.EntitySimpleBullet;
import com.arkcraft.mod.common.items.weapons.projectiles.EntitySimpleRifleAmmo;
import com.arkcraft.mod.common.items.weapons.projectiles.EntitySimpleShotgunAmmo;
import com.arkcraft.mod.common.items.weapons.projectiles.EntityStoneArrow;
import com.arkcraft.mod.common.items.weapons.projectiles.EntityTranqArrow;
import com.arkcraft.mod.common.items.weapons.projectiles.EntityTranquilizer;
import com.arkcraft.mod.common.items.weapons.projectiles.dispense.DispenseRocketPropelledGrenade;
import com.arkcraft.mod.common.items.weapons.projectiles.dispense.DispenseSimpleBullet;
import com.arkcraft.mod.common.items.weapons.projectiles.dispense.DispenseSimpleRifleAmmo;
import com.arkcraft.mod.common.items.weapons.projectiles.dispense.DispenseSimpleShotgunAmmo;
import com.arkcraft.mod.common.items.weapons.projectiles.dispense.DispenseTranquilizer;
import com.arkcraft.mod.common.lib.BALANCE;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wildbill22
 */
public class ARKCraftItems
{
	public static ARKFood tintoBerry, amarBerry, azulBerry, mejoBerry, narcoBerry, stimBerry, meat_raw, meat_cooked, primemeat_raw, primemeat_cooked;
	public static ARKSeedItem tintoBerrySeed, amarBerrySeed, azulBerrySeed, mejoBerrySeed, narcoBerrySeed, stimBerrySeed;
	public static ARKItem rock, fiber, chitin, narcotics, explosive_ball, dodo_bag, dodo_feather, gun_powder, spark_powder;
	public static ARKThatchItem thatch;
	public static ARKItem wood, metal, metal_ingot;
	public static ARKFecesItem dodo_feces, player_feces, fertilizer;
	public static ARKEggItem dodo_egg;
	public static ARKSaddle saddle_small, saddle_medium, saddle_large;
	public static ARKArmorItem chitinHelm, chitinChest, chitinLegs, chitinBoots;
	public static ARKArmorItem clothHelm, clothChest, clothLegs, clothBoots;
	public static ARKArmorItem boneHelm, boneChest, boneLegs, boneBoots;
	public static Dossier dino_book;
	public static ARKBushItem item_berry_bush;
	public static ARKCompostBinItem item_compost_bin;
	public static ARKSmithyItem item_smithy;
	public static ItemSpyGlass spy_glass;

	// Weapons
	public static ARKSlingshot slingshot;
	public static ARKWeapon ironPike;
	public static ItemSpear spear;
	public static ItemShooter tranq_gun;
	public static ItemCompoundBow compound_bow;
	public static ItemShooter rocket_launcher;
	public static ItemProjectile tranquilizer, stone_arrow, tranq_arrow, metal_arrow;
	public static ItemProjectile simple_bullet, simple_rifle_ammo, simple_shotgun_ammo, rocket_propelled_grenade;
	public static ItemShooter simple_pistol;
	public static ItemShooter longneck_rifle;
	public static ItemShooter shotgun;
	public static ItemShooter crossbow;

	public static ArmorMaterial CLOTH = EnumHelper.addArmorMaterial("CLOTH_MAT", "CLOTH_MAT", 4, new int[] {1,2,1,1}, 15);
	public static ArmorMaterial CHITIN = EnumHelper.addArmorMaterial("CHITIN_MAT", "CHITIN_MAT", 16, new int[] { 3,7,6,3 } , 10);
	public static ArmorMaterial BONE = EnumHelper.addArmorMaterial("BONE_MAT", "BONE_MAT", 40, new int[] { 3, 8, 6, 3 }, 30);
	
	public static ARKCraftItems getInstance() { return new ARKCraftItems(); }
	
	public static Map<String, Item> allItems = new HashMap<String, Item>();
	public static Map<String, Item> getAllItems() { return allItems; }

	public static void init() {
		// Food
		tintoBerry = addFood("tinto", 4, 0.3F, false, true, new PotionEffect(Potion.fireResistance.id, 60, 1));
		amarBerry = addFood("amar", 4, 0.3F, false, true, new PotionEffect(Potion.absorption.id, 100, 1));
		azulBerry = addFood("azul", 4, 0.3F, false, true, new PotionEffect(Potion.jump.id, 60, 1));
		mejoBerry = addFood("mejo", 4, 0.3F, false, true, new PotionEffect(Potion.resistance.id, 100, 1));
		narcoBerry = addFood("narco", 4, 0.3F, true, true, new PotionEffect(Potion.moveSpeed.id, 160, 1));
		stimBerry = addFood("stim", 4, 0.3F, true, true, new PotionEffect(Potion.heal.id, 60, 1));
		meat_raw = addFood("meat_raw", 3, 0.3F, false, false);
		meat_cooked = addFood("meat_cooked", 6, 0.9F, false, false);
		primemeat_raw = addFood("primemeat_raw", 3, 0.3F, false, false);
		primemeat_cooked = addFood("primemeat_cooked", 8, 1.2F, false, false);

		// Seeds
		tintoBerrySeed = addSeedItem("tintoBerrySeed"); 
		amarBerrySeed = addSeedItem("amarBerrySeed");
		azulBerrySeed = addSeedItem("azulBerrySeed");
		mejoBerrySeed = addSeedItem("mejoBerrySeed");
		narcoBerrySeed = addSeedItem("narcoBerrySeed");
		stimBerrySeed = addSeedItem("stimBerrySeed");

		// world generated
		
		// Weapons and tools
		rock = addItem("rock");
		explosive_ball = addItem("explosive_ball");
		slingshot = addSlingshot("slingshot");
		//stoneSpear = addWeaponThrowable("stoneSpear", ToolMaterial.STONE);
		ironPike = addWeapon("ironPike", ToolMaterial.IRON);

		// Regular Items
		fiber = addItem("fiber");
		thatch = addThatchItem("thatch");
		wood = addItem("wood");
		metal = addItem("metal");
		metal_ingot = addItem("metal_ingot");
		chitin = addItem("chitin");
		dodo_feather = addItem("dodo_feather");
		dodo_bag = addItem("dodo_bag");
		gun_powder = addItem("gun_powder");
		spark_powder = addItem("spark_powder");
		spy_glass = addSpyGlass("spy_glass", new RangedCompSpyGlass());
		
		//Block Items
		item_berry_bush = addBushItem("item_berry_bush");
		item_compost_bin = addCompostBinItem("item_compost_bin");
		item_smithy = addSmithyItem("item_smithy");
		
		//Bows
		compound_bow = new ItemCompoundBow();
		registerItem("compound_bow", compound_bow);
			
		//Bullets
	//	tranq_arrow = addItemProjectile("tranq_arrow");
	//	stone_arrow = addItemProjectile("stone_arrow");
	//	metal_arrow = addItemProjectile("metal_arrow");
		
		spear = addSpearItem("spear", ToolMaterial.STONE);
		
		// feces (2nd parameter is the seconds to decompose)
		dodo_feces = addFecesItem("dodo_feces", BALANCE.CROP_PLOT.SECONDS_FOR_SMALL_FECES_TO_DECOMPOSE);
		player_feces = addFecesItem("player_feces", BALANCE.CROP_PLOT.SECONDS_FOR_PLAYER_FECES_TO_DECOMPOSE);
		
		// Technically not feces, but used in all situations the same (currently)
		fertilizer = addFecesItem("fertilizer", BALANCE.CROP_PLOT.SECONDS_FOR_FERTILIZER_TO_DECOMPOSE);

		// Other Types of Items
		dodo_egg = addEggItem("dodo_egg");
		dino_book = addDossier("dossier");
		narcotics = addItem("narcotics");
		saddle_small = addSaddle("saddle_small");
		saddle_medium = addSaddle("saddle_medium");
		saddle_large = addSaddle("saddle_large");
				
		// Armor
		chitinHelm = addArmorItem("chitin_helm", CHITIN, "chitinArmor", 0, false);
		chitinChest = addArmorItem("chitin_chest", CHITIN, "chitinArmor", 1, false);
		chitinLegs = addArmorItem("chitin_legs", CHITIN, "chitinArmor", 2, false);
		chitinBoots = addArmorItem("chitin_boots", CHITIN, "chitinArmor", 3, false);
		clothHelm = addArmorItem("cloth_helm", CLOTH, "clothArmor", 0, false);
		clothChest = addArmorItem("cloth_chest", CLOTH, "clothArmor", 1, false);
		clothLegs = addArmorItem("cloth_legs", CLOTH, "clothArmor", 2, false);
		clothBoots = addArmorItem("cloth_boots", CLOTH, "clothArmor", 3, false);
		boneHelm = addArmorItem("bone_helm", BONE, "boneArmor", 0, true);
		boneChest = addArmorItem("bone_chest", BONE, "boneArmor", 1, true);
		boneLegs = addArmorItem("bone_legs", BONE, "boneArmor", 2, true);
		boneBoots = addArmorItem("bone_boots", BONE, "boneArmor", 3, true);
		
		registerDispenseBehavior();
		registerWeaponEntities();
		addGunPowderWeapons();
	}
	
	public static void registerWeaponEntities(){
		if (BALANCE.WEAPONS.SIMPLE_PISTOL){
			EntityHandler.registerModEntity(EntitySimpleBullet.class, "Simple Bullet", ARKCraft.instance, 64, 10, true);
		}
		if (BALANCE.WEAPONS.SHOTGUN){
			EntityHandler.registerModEntity(EntitySimpleShotgunAmmo.class, "Simple Shotgun Ammo", ARKCraft.instance, 64, 10, true);		
		}
		if (BALANCE.WEAPONS.LONGNECK_RIFLE)	{
			EntityHandler.registerModEntity(EntitySimpleRifleAmmo.class, "Simple Rifle Ammo", ARKCraft.instance, 64, 10, true);
		}
		if (BALANCE.WEAPONS.TRANQ_GUN)	{
			EntityHandler.registerModEntity(EntityTranquilizer.class, "Tranquilizer", ARKCraft.instance, 64, 10, true);
		}
		if (BALANCE.WEAPONS.ROCKET_LAUNCHER)	{
			EntityHandler.registerModEntity(EntityRocketPropelledGrenade.class, "Rocket Propelled Grenade", ARKCraft.instance, 64, 10, true);
		}
		if (BALANCE.WEAPONS.CROSSBOW)	{
			EntityHandler.registerModEntity(EntityTranqArrow.class, "Tranq Arrow", ARKCraft.instance, 64, 10, true);
			EntityHandler.registerModEntity(EntityStoneArrow.class, "Stone Arrow", ARKCraft.instance, 64, 10, true);
			EntityHandler.registerModEntity(EntityMetalArrow.class, "Metal Arrow", ARKCraft.instance, 64, 10, true);
		}
	}
	
	public static void addGunPowderWeapons(){
		if (BALANCE.WEAPONS.SIMPLE_PISTOL) {
			simple_pistol = addShooter("simple_pistol", new RangedCompSimplePistol());
			simple_bullet = addItemProjectile("simple_bullet");
		}
		if (BALANCE.WEAPONS.LONGNECK_RIFLE) {
			longneck_rifle = addShooter("longneck_rifle", new RangedCompLongneckRifle(GlobalAdditions.GUI.SCOPE.getID()));
			simple_rifle_ammo = addItemProjectile("simple_rifle_ammo");
		}
		if (BALANCE.WEAPONS.SHOTGUN) {
			shotgun = addShooter("shotgun", new RangedCompShotgun());
			simple_shotgun_ammo = addItemProjectile("simple_shotgun_ammo");
		}
		if (BALANCE.WEAPONS.TRANQ_GUN) {
			tranq_gun = addShooter("tranq_gun", new RangedCompTranqGun());
			tranquilizer = addItemProjectile("tranquilizer");
		}
		if (BALANCE.WEAPONS.ROCKET_LAUNCHER) {
			rocket_launcher = addShooter("rocket_launcher", new RangedCompRocketLauncher());
			rocket_propelled_grenade = addItemProjectile("rocket_propelled_grenade");
		}
		if (BALANCE.WEAPONS.CROSSBOW) {
			crossbow = addShooter("crossbow", new RangedCompCrossbow());
			metal_arrow = addItemProjectile("metal_arrow");
			tranq_arrow = addItemProjectile("tranq_arrow");
			stone_arrow = addItemProjectile("stone_arrow");
		}
	}

	public static void registerDispenseBehavior(){
		if (simple_bullet != null) {
			BlockDispenser.dispenseBehaviorRegistry.putObject(simple_bullet, new DispenseSimpleBullet());
		}
		if (simple_shotgun_ammo != null) {
			BlockDispenser.dispenseBehaviorRegistry.putObject(simple_shotgun_ammo, new DispenseSimpleShotgunAmmo());
		}
		if (simple_rifle_ammo != null) {
			BlockDispenser.dispenseBehaviorRegistry.putObject(simple_rifle_ammo, new DispenseSimpleRifleAmmo());
		}
		if (tranquilizer != null) {
			BlockDispenser.dispenseBehaviorRegistry.putObject(tranquilizer, new DispenseTranquilizer());
		}
		if (rocket_propelled_grenade != null) {
			BlockDispenser.dispenseBehaviorRegistry.putObject(rocket_propelled_grenade, new DispenseRocketPropelledGrenade());
		}
	}
	
	protected static ItemProjectile addItemProjectile(String name) {
		ItemProjectile i = new ItemProjectile();
		registerItem(name, i);
		return i;
	}

	protected static ItemShooter addShooter(String name, RangedComponent rangedcomponent) {
		ItemShooter i = new ItemShooter(rangedcomponent);
		registerItem(name, i);
		return i;
	}

	protected static ARKSlingshot addSlingshot(String name) {
		ARKSlingshot slingshot = new ARKSlingshot();
		registerItem(name, slingshot);
		return slingshot;
	}
	
	protected static ARKItem addItem(String name) {
		ARKItem i = new ARKItem();
		registerItem(name, i);
		return i;
	}
	
	protected static ARKThatchItem addThatchItem(String name) {
		ARKThatchItem t = new ARKThatchItem();
		registerItem(name, t);
		return t;
	}
	
	protected static ARKSeedItem addSeedItem(String name) {
		ARKSeedItem i = new ARKSeedItem();
		registerItem(name, i);
		return i;
	}
	
	protected static ARKEggItem addEggItem(String name) {
		ARKEggItem i = new ARKEggItem();
		registerItem(name, i);
		return i;
	}
	
	protected static ARKBushItem addBushItem(String name) {
		ARKBushItem i = new ARKBushItem();
		registerItem(name, i);
		return i;
	}
	
	protected static ARKCompostBinItem addCompostBinItem(String name) {
		ARKCompostBinItem i = new ARKCompostBinItem();
		registerItem(name, i);
		return i;
	}
	
	protected static ARKSmithyItem addSmithyItem(String name) {
		ARKSmithyItem i = new ARKSmithyItem();
		registerItem(name, i);
		return i;
	}
	
	protected static ItemSpyGlass addSpyGlass(String name, RangedComponent rangedcomponent) {
		ItemSpyGlass i = new ItemSpyGlass(rangedcomponent);
		registerItem(name, i);
		return i;
	}
	
	public static ItemSpear addSpearItem(String name, ToolMaterial mat) {
		ItemSpear weapon = new ItemSpear(mat);
		registerItem(name, weapon);
		return weapon;
	}
	
	protected static ARKFecesItem addFecesItem(String name, int maxDamageIn) {
		ARKFecesItem i = new ARKFecesItem();
		i.setMaxDamage(maxDamageIn);
		registerItem(name, i);
		return i;
	}	
	
	protected static ARKFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible) {
		ARKFood f = new ARKFood(heal, sat, fav, alwaysEdible);
		registerItem(name, f);
		return f;
	}
	
	protected static Dossier addDossier(String name) {
		Dossier dossier = new Dossier(name);
		registerItem(name, dossier);
		return dossier;
	}
	
	public static ARKFood addFood(String name, int heal, float sat, boolean fav, boolean alwaysEdible, PotionEffect... effect) {
		ARKFood f = new ARKFood(heal, sat, fav, alwaysEdible, effect);
		registerItem(name, f);
		return f;			
	}

	public static ARKArmorItem addArmorItem(String name, ArmorMaterial mat, String armorTexName, int type, boolean golden) {
		ARKArmorItem item = new ARKArmorItem(mat, armorTexName, type, golden);
		registerItem(name, item);
		return item;
	}

	public static ARKSaddle addSaddle(String name) {
		ARKSaddle item = new ARKSaddle();
		registerItem(name, item);
		return item;
	}

	public static ARKWeapon addWeapon(String name, ToolMaterial mat) {
		ARKWeapon weapon = new ARKWeapon(mat);
		registerItem(name, weapon);
		return weapon;
	}
	public static ARKWeaponThrowable addWeaponThrowable(String name, ToolMaterial mat)
	{
		ARKWeaponThrowable weapon = new ARKWeaponThrowable(mat);
		registerItem(name, weapon);
		return weapon;
	}

	public static void registerItem(String name, Item item)
	{
		allItems.put(name, item);
		item.setUnlocalizedName(name);
		item.setCreativeTab(GlobalAdditions.tabARK);
		GameRegistry.registerItem(item, name);
	}
}
