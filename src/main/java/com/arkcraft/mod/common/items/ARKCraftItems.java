package com.arkcraft.mod.common.items;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.client.gui.book.Dossier;
import com.arkcraft.mod.common.items.weapons.ItemCompoundBow;
import com.arkcraft.mod.common.items.weapons.ItemCrossbow;
import com.arkcraft.mod.common.items.weapons.ItemLongneckRifle;
import com.arkcraft.mod.common.items.weapons.ItemRocketLauncher;
import com.arkcraft.mod.common.items.weapons.ItemShotgun;
import com.arkcraft.mod.common.items.weapons.ItemSimplePistol;
import com.arkcraft.mod.common.items.weapons.ItemSpear;
import com.arkcraft.mod.common.items.weapons.ItemTranqGun;
import com.arkcraft.mod.common.items.weapons.bullets.ItemProjectile;
import com.arkcraft.mod.common.items.weapons.component.RangedCompCrossbow;
import com.arkcraft.mod.common.items.weapons.component.RangedCompLongneckRifle;
import com.arkcraft.mod.common.items.weapons.component.RangedCompRocketLauncher;
import com.arkcraft.mod.common.items.weapons.component.RangedCompShotgun;
import com.arkcraft.mod.common.items.weapons.component.RangedCompSimplePistol;
import com.arkcraft.mod.common.items.weapons.component.RangedCompTranqGun;
import com.arkcraft.mod.common.items.weapons.component.RangedComponent;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @author wildbill22
 */
public class ARKCraftItems
{

	public static ARKFood tintoBerry, amarBerry, azulBerry, mejoBerry, narcoBerry, porkchop_raw, porkchop_cooked, primemeat_raw, primemeat_cooked;
	public static ARKSeedItem tintoBerrySeed, amarBerrySeed, azulBerrySeed, mejoBerrySeed, narcoBerrySeed;
	public static ARKItem cobble_ball, fiber, chitin, narcotics, explosive_ball, dodo_bag, dodo_feather, gun_powder;
	public static ARKThatchItem thatch;
	public static ARKFecesItem dodo_feces, player_feces, fertilizer;
	public static ARKEggItem dodo_egg;
	public static ARKSaddle saddle_small, saddle_medium, saddle_large;
	public static ARKArmorItem chitinHelm, chitinChest, chitinLegs, chitinBoots;
	public static ARKArmorItem clothHelm, clothChest, clothLegs, clothBoots;
	public static ARKArmorItem boneHelm, boneChest, boneLegs, boneBoots;
	public static Dossier dino_book;
	public static ARKBushItem item_berry_bush;

	// Weapons
	public static ARKSlingshot slingshot;
	public static ARKWeapon ironPike;
	public static ItemSpear spear;
	public static ItemTranqGun tranq_gun;
	public static ItemCompoundBow compound_bow;
	public static ItemRocketLauncher rocket_launcher;
	public static ItemProjectile tranquilizer, stone_arrow, tranq_arrow, metal_arrow;
	public static ItemProjectile simple_bullet, simple_rifle_ammo, simple_shotgun_ammo, rocket_propelled_grenade;
	public static ItemSimplePistol simple_pistol;
	public static ItemLongneckRifle longneck_rifle;
	public static ItemShotgun shotgun;
	public static ItemCrossbow crossbow;

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
		porkchop_raw = addFood("porkchop_raw", 3, 0.3F, false, false);
		porkchop_cooked = addFood("porkchop_cooked", 6, 0.9F, false, false);
		primemeat_raw = addFood("primemeat_raw", 3, 0.3F, false, false);
		primemeat_cooked = addFood("primemeat_cooked", 8, 1.2F, false, false);

		// Seeds
		tintoBerrySeed = addSeedItem("tintoBerrySeed"); 
		amarBerrySeed = addSeedItem("amarBerrySeed");
		azulBerrySeed = addSeedItem("azulBerrySeed");
		mejoBerrySeed = addSeedItem("mejoBerrySeed");
		narcoBerrySeed = addSeedItem("narcoBerrySeed");

		// world generated
		
		// Weapons and tools
		cobble_ball = addItemWithTooltip("cobble_ball", EnumChatFormatting.GOLD + "A Rocky Road to Victory");
		explosive_ball = addItemWithTooltip("explosive_ball", EnumChatFormatting.RED + "A Rocky Road to Destruction");
		slingshot = addSlingshot("slingshot");
		//stoneSpear = addWeaponThrowable("stoneSpear", ToolMaterial.STONE);
		ironPike = addWeapon("ironPike", ToolMaterial.IRON);
		addGunPowderWeapons();

		// Regular Items
		fiber = addItem("fiber");
		thatch = addThatchItem("thatch");
		chitin = addItem("chitin");
		dodo_feather = addItem("dodo_feather");
		dodo_bag = addItemWithTooltip("dodo_bag", "Backpack for the Dodo");
		gun_powder = addItemWithTooltip("gun_powder", "Recipe for destruction");
		
		//Block Items
		item_berry_bush = addBushItem("item_berry_bush");
		
		//Bows
		compound_bow = new ItemCompoundBow("compound_bow");
			
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
		
		registerDispenseBehavior();
	}
	
	public static void addGunPowderWeapons(){
		if (BALANCE.WEAPONS.SIMPLE_PISTOL) {
			simple_pistol = addSimplePistol("simple_pistol", new RangedCompSimplePistol());
			simple_bullet = addItemProjectile("simple_bullet");
		}
		if (BALANCE.WEAPONS.LONGNECK_RIFLE) {
			longneck_rifle = addLongneckRifle("longneck_rifle", new RangedCompLongneckRifle(null, GlobalAdditions.GUI.SCOPE.getID()));
			simple_rifle_ammo = addItemProjectile("simple_rifle_ammo");
		}
		if (BALANCE.WEAPONS.SHOTGUN) {
			shotgun = addShotgun("shotgun", new RangedCompShotgun());
			simple_shotgun_ammo = addItemProjectile("simple_shotgun_ammo");
		}
		if (BALANCE.WEAPONS.TRANQ_GUN) {
			tranq_gun = addTranqGun("tranq_gun", new RangedCompTranqGun());
			tranquilizer = addItemProjectile("tranquilizer");
		}
		if (BALANCE.WEAPONS.ROCKET_LAUNCHER) {
			rocket_launcher = addRocketLauncher("rocket_launcher", new RangedCompRocketLauncher());
			rocket_propelled_grenade = addItemProjectile("rocket_propelled_grenade");
		}
		if (BALANCE.WEAPONS.CROSSBOW) {
			crossbow = addCrossbow("crossbow", new RangedCompCrossbow());
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
		ItemProjectile i = new ItemProjectile(name);
		allItems.put(name, i);
		return i;
	}
	
	protected static ItemSimplePistol addSimplePistol(String name, RangedComponent rangedcomponent) {
		ItemSimplePistol i = new ItemSimplePistol(name, rangedcomponent);
		allItems.put(name, i);
		return i;
	}
	
	protected static ItemLongneckRifle addLongneckRifle(String name, RangedComponent rangedcomponent) {
		ItemLongneckRifle i = new ItemLongneckRifle(name, rangedcomponent);
		allItems.put(name, i);
		return i;
	}
	
	protected static ItemCrossbow addCrossbow(String name, RangedComponent rangedcomponent) {
		ItemCrossbow i = new ItemCrossbow(name, rangedcomponent);
		allItems.put(name, i);
		return i;
	}
	
	protected static ItemRocketLauncher addRocketLauncher(String name, RangedComponent rangedcomponent) {
		ItemRocketLauncher i = new ItemRocketLauncher(name, rangedcomponent);
		allItems.put(name, i);
		return i;
	}
	
	protected static ItemShotgun addShotgun(String name, RangedComponent rangedcomponent) {
		ItemShotgun i = new ItemShotgun(name, rangedcomponent);
		allItems.put(name, i);
		return i;
	}
	
	protected static ARKSlingshot addSlingshot(String name) {
		ARKSlingshot slingshot = new ARKSlingshot(name);
		allItems.put(name, slingshot);
		return slingshot;
	}
	
	protected static ARKItem addItem(String name) {
		ARKItem i = new ARKItem(name);
		allItems.put(name, i);
		return i;
	}
	
	protected static ARKThatchItem addThatchItem(String name) {
		ARKThatchItem t = new ARKThatchItem(name);
		allItems.put(name, t);
		return t;
	}
	
	protected static ARKSeedItem addSeedItem(String name) {
		ARKSeedItem i = new ARKSeedItem(name);
		allItems.put(name, i);
		return i;
	}
	
	protected static ARKEggItem addEggItem(String name) {
		ARKEggItem i = new ARKEggItem(name);
		allItems.put(name, i);
		return i;
	}
	
	protected static ARKBushItem addBushItem(String name) {
		ARKBushItem i = new ARKBushItem(name);
		allItems.put(name, i);
		return i;
	}
	
	public static ItemSpear addSpearItem(String name, ToolMaterial mat) {
		ItemSpear weapon = new ItemSpear(name, mat);
		allItems.put(name, weapon);
		return weapon;
	}
	
	protected static ARKFecesItem addFecesItem(String name, int maxDamageIn) {
		ARKFecesItem i = new ARKFecesItem(name);
		i.setMaxDamage(maxDamageIn);
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
	public static ItemTranqGun addTranqGun(String name, RangedComponent rangedcomponent) {
		ItemTranqGun item = new ItemTranqGun(name, rangedcomponent);
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
}
