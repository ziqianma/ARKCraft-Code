package com.arkcraft.module.item.common.items;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.GlobalAdditions;
import com.arkcraft.module.core.client.gui.book.Dossier;
import com.arkcraft.module.core.common.handlers.EntityHandler;
import com.arkcraft.module.item.common.config.ModuleItemBalance;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityAdvancedBullet;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityBallista;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityBallistaBolt;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityBase;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityMetalArrow;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityRocketPropelledGrenade;
import com.arkcraft.module.item.common.entity.item.projectiles.EntitySimpleBullet;
import com.arkcraft.module.item.common.entity.item.projectiles.EntitySimpleRifleAmmo;
import com.arkcraft.module.item.common.entity.item.projectiles.EntitySimpleShotgunAmmo;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityStoneArrow;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityTranqArrow;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityTranquilizer;
import com.arkcraft.module.item.common.entity.item.projectiles.dispense.DispenseBallistaBolt;
import com.arkcraft.module.item.common.entity.item.projectiles.dispense.DispenseRocketPropelledGrenade;
import com.arkcraft.module.item.common.entity.item.projectiles.dispense.DispenseSimpleBullet;
import com.arkcraft.module.item.common.entity.item.projectiles.dispense.DispenseSimpleRifleAmmo;
import com.arkcraft.module.item.common.entity.item.projectiles.dispense.DispenseSimpleShotgunAmmo;
import com.arkcraft.module.item.common.entity.item.projectiles.dispense.DispenseTranquilizer;
import com.arkcraft.module.item.common.items.itemblock.ItemBerryBush;
import com.arkcraft.module.item.common.items.itemblock.ItemCompostBin;
import com.arkcraft.module.item.common.items.itemblock.ItemCropPlot;
import com.arkcraft.module.item.common.items.itemblock.ItemMortarAndPestle;
import com.arkcraft.module.item.common.items.itemblock.ItemRefiningForge;
import com.arkcraft.module.item.common.items.itemblock.ItemSmithy;
import com.arkcraft.module.item.common.items.weapons.ItemARKBow;
import com.arkcraft.module.item.common.items.weapons.ItemCompoundBow;
import com.arkcraft.module.item.common.items.weapons.ItemSpear;
import com.arkcraft.module.item.common.items.weapons.ItemWoodenClub;
import com.arkcraft.module.item.common.items.weapons.bullets.ItemProjectile;
import com.arkcraft.module.item.common.items.weapons.ranged.ItemCrossbow;
import com.arkcraft.module.item.common.items.weapons.ranged.ItemFabricatedPistol;
import com.arkcraft.module.item.common.items.weapons.ranged.ItemLongneckRifle;
import com.arkcraft.module.item.common.items.weapons.ranged.ItemRangedWeapon;
import com.arkcraft.module.item.common.items.weapons.ranged.ItemRocketLauncher;
import com.arkcraft.module.item.common.items.weapons.ranged.ItemShotgun;
import com.arkcraft.module.item.common.items.weapons.ranged.ItemSimplePistol;

/**
 * @author wildbill22
 */
public class ARKCraftItems
{
	public static ItemARKFood tintoBerry, amarBerry, azulBerry, mejoBerry, narcoBerry, stimBerry,
			meat_raw, meat_cooked, primemeat_raw, primemeat_cooked, spoiled_meat;
	public static ItemARKSeed tintoBerrySeed, amarBerrySeed, azulBerrySeed, mejoBerrySeed,
			narcoBerrySeed, stimBerrySeed;
	public static ItemARKBase rock, cementing_pastes, fiber, chitin, narcotics, dodo_bag,
			dodo_feather, gun_powder, spark_powder, hide;
	public static ItemThatch thatch;
	public static ItemARKBase wood, metal, metal_ingot, flint;
	public static ItemFeces dodo_feces, player_feces, fertilizer;
	public static ItemDinosaurEgg dodo_egg;
	public static ItemDinosaurSaddle saddle_small, saddle_medium, saddle_large;
	public static ItemARKArmor chitinHelm, chitinChest, chitinLegs, chitinBoots;
	public static ItemARKArmor clothHelm, clothChest, clothLegs, clothBoots;
	public static ItemARKArmor hideHelm, hideChest, hideLegs, hideBoots;
	public static Dossier dino_book;
	public static ItemBerryBush item_berry_bush;
	public static ItemCompostBin item_compost_bin;
	public static ItemSmithy item_smithy;
	public static ItemCrystal item_crystal;
	public static ItemCropPlot item_crop_plot;
	public static ItemRefiningForge item_refining_forge;
	public static ItemMortarAndPestle item_mortar_and_pestle;
	public static ItemSpyGlass spy_glass;
	public static ItemGrenade grenade;
	public static ItemStonePick stone_pick;
	public static ItemStoneHatchet stone_hatchet;
	public static ItemMetalPick metal_pick;
	public static ItemMetalHatchet metal_hatchet;

	// Attachments
	public static ItemARKBase scope, flash_light;

	// Weapons
	public static ItemSlingshot slingshot;
	public static ItemARKWeaponBase ironPike;
	public static ItemSpear spear;
	public static ItemWoodenClub wooden_club;
	public static ItemARKBow bow;
	public static ItemCompoundBow compound_bow;
	public static ItemProjectile tranquilizer, stone_arrow, tranq_arrow, metal_arrow,
			ballista_bolt, simple_bullet, simple_rifle_ammo, simple_shotgun_ammo,
			rocket_propelled_grenade, advanced_bullet;
	public static ItemRangedWeapon rocket_launcher, tranq_gun, simple_pistol, fabricated_pistol,
			longneck_rifle, shotgun, crossbow;
	public static ItemBallista ballista;

	public static ArmorMaterial CLOTH = EnumHelper.addArmorMaterial("CLOTH_MAT", "CLOTH_MAT", 4,
			new int[] { 1, 2, 1, 1 }, 15);
	public static ArmorMaterial CHITIN = EnumHelper.addArmorMaterial("CHITIN_MAT", "CHITIN_MAT",
			16, new int[] { 3, 7, 6, 3 }, 10);
	public static ArmorMaterial HIDE = EnumHelper.addArmorMaterial("HIDE_MAT", "HIDE_MAT", 40,
			new int[] { 3, 8, 6, 3 }, 30);

	public static ToolMaterial METAL = EnumHelper.addToolMaterial("METAL_MAT", 3, 1500, 6.0F, 0.8F,
			8);
	public static ToolMaterial STONE = EnumHelper.addToolMaterial("STONE_MAT", 2, 500, 3.5F, 0.4F,
			13);

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

		// Weapons and tools
		rock = addItem("rock");
		scope = addItem("scope");
		flash_light = addItem("flash_light");
		cementing_pastes = addItem("cementing_paste");
		slingshot = addSlingshot("slingshot");
		grenade = addGrenade("grenade");
		// stoneSpear = addWeaponThrowable("stoneSpear", ToolMaterial.STONE);
		ironPike = addWeapon("ironPike", ToolMaterial.IRON);
		ballista = addBallista("ballista");
		ballista_bolt = addItemProjectile("ballista_bolt");

		metal_pick = addMetalPick("metal_pick", METAL);
		metal_hatchet = addMetalHatchet("metal_hatchet", METAL);
		stone_hatchet = addStoneHatchet("stone_hatchet", STONE);
		stone_pick = addStonePick("stone_pick", STONE);

		// Regular Items
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

		// Block Items
		// item_berry_bush = addBushItem("item_berry_bush");
		// item_compost_bin = addCompostBinItem("item_compost_bin");
		// item_smithy = addSmithyItem("item_smithy");
		// item_crop_plot = addCropPlot("item_crop_plot");
		// item_mortar_and_pestle =
		// addMortarAndPestle("item_mortar_and_pestle");
		// item_crystal = addCrystalItem("item_crystal");
		// item_refining_forge = addRefiningForge("item_refining_forge");

		// Bows
		compound_bow = new ItemCompoundBow();
		registerItem("compound_bow", compound_bow);

		bow = new ItemARKBow();
		registerItem("bow", bow);

		// Bullets
		// tranq_arrow = addItemProjectile("tranq_arrow");
		// stone_arrow = addItemProjectile("stone_arrow");
		// metal_arrow = addItemProjectile("metal_arrow");

		spear = addSpearItem("spear", ToolMaterial.WOOD);
		wooden_club = addWoodenClub("wooden_club", ToolMaterial.WOOD);

		// feces (2nd parameter is the seconds to decompose)
		dodo_feces = addFecesItem("dodo_feces",
				ModuleItemBalance.CROP_PLOT.SECONDS_FOR_SMALL_FECES_TO_DECOMPOSE);
		player_feces = addFecesItem("player_feces",
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
		hideHelm = addArmorItem("hide_helm", HIDE, "hideArmor", 0, true);
		hideChest = addArmorItem("hide_chest", HIDE, "hideArmor", 1, true);
		hideLegs = addArmorItem("hide_legs", HIDE, "hideArmor", 2, true);
		hideBoots = addArmorItem("hide_boots", HIDE, "hideArmor", 3, true);

		EntityHandler.registerModEntity(EntityBallista.class, "ballista", ARKCraft.instance, 64,
				128, false);
		EntityHandler.registerModEntity(EntityBallistaBolt.class, "ballistaBolt",
				ARKCraft.instance, 64, 20, true);

		EntityHandler.registerModEntity(EntityBase.class, "Entity Base", ARKCraft.instance, 64, 10,
				true);

		registerDispenseBehavior();
		registerWeaponEntities();
		addGunPowderWeapons();
	}

	public static void registerWeaponEntities()
	{
		if (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL)
		{
			EntityHandler.registerModEntity(EntitySimpleBullet.class, "Simple Bullet",
					ARKCraft.instance, 16, 20, true);
		}

		if (ModuleItemBalance.WEAPONS.SHOTGUN)
		{
			EntityHandler.registerModEntity(EntitySimpleShotgunAmmo.class, "Simple Shotgun Ammo",
					ARKCraft.instance, 64, 10, true);
		}

		if (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE)
		{
			EntityHandler.registerModEntity(EntitySimpleRifleAmmo.class, "Simple Rifle Ammo",
					ARKCraft.instance, 64, 10, true);
			EntityHandler.registerModEntity(EntityTranquilizer.class, "Tranquilizer",
					ARKCraft.instance, 64, 10, true);
		}

		if (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL)
		{
			EntityHandler.registerModEntity(EntityAdvancedBullet.class, "Advanced Bullet",
					ARKCraft.instance, 64, 10, true);
		}

		if (ModuleItemBalance.WEAPONS.ROCKET_LAUNCHER)
		{
			EntityHandler.registerModEntity(EntityRocketPropelledGrenade.class,
					"Rocket Propelled Grenade", ARKCraft.instance, 64, 10, true);
		}

		if (ModuleItemBalance.WEAPONS.CROSSBOW)
		{
			EntityHandler.registerModEntity(EntityTranqArrow.class, "Tranq Arrow",
					ARKCraft.instance, 64, 10, true);
			EntityHandler.registerModEntity(EntityStoneArrow.class, "Stone Arrow",
					ARKCraft.instance, 64, 10, true);
			EntityHandler.registerModEntity(EntityMetalArrow.class, "Metal Arrow",
					ARKCraft.instance, 64, 10, true);
			// if (MOD2_BALANCE.WEAPONS.CROSSBOW) {
			//
			// }
		}
	}

	public static void addGunPowderWeapons()
	{
		if (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE)
		{
			simple_rifle_ammo = addItemProjectile("simple_rifle_ammo");
			tranquilizer = addItemProjectile("tranquilizer");
			longneck_rifle = addShooter(new ItemLongneckRifle());
			longneck_rifle.registerProjectile(simple_rifle_ammo);
			longneck_rifle.registerProjectile(tranquilizer);
		}
		if (ModuleItemBalance.WEAPONS.SHOTGUN)
		{
			shotgun = addShooter(new ItemShotgun());
			simple_shotgun_ammo = addItemProjectile("simple_shotgun_ammo");
			shotgun.registerProjectile(simple_shotgun_ammo);
		}
		if (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL)
		{
			simple_pistol = addShooter(new ItemSimplePistol());
			simple_bullet = addItemProjectile("simple_bullet");
			simple_pistol.registerProjectile(simple_bullet);
		}
		if (ModuleItemBalance.WEAPONS.ROCKET_LAUNCHER)
		{
			rocket_launcher = addShooter(new ItemRocketLauncher());
			rocket_propelled_grenade = addItemProjectile("rocket_propelled_grenade");
			rocket_launcher.registerProjectile(rocket_propelled_grenade);
		}
		if (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL)
		{
			fabricated_pistol = addShooter(new ItemFabricatedPistol());
			advanced_bullet = addItemProjectile("advanced_bullet");
			fabricated_pistol.registerProjectile(advanced_bullet);
		}
		if (ModuleItemBalance.WEAPONS.CROSSBOW)
		{
			crossbow = addShooter(new ItemCrossbow());
			metal_arrow = addItemProjectile("metal_arrow");
			tranq_arrow = addItemProjectile("tranq_arrow");
			stone_arrow = addItemProjectile("stone_arrow");
			crossbow.registerProjectile(metal_arrow);
			crossbow.registerProjectile(tranq_arrow);
			crossbow.registerProjectile(stone_arrow);
		}
	}

	public static void registerDispenseBehavior()
	{
		if (simple_bullet != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(simple_bullet,
					new DispenseSimpleBullet());
		}
		if (simple_shotgun_ammo != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(simple_shotgun_ammo,
					new DispenseSimpleShotgunAmmo());
		}
		if (simple_rifle_ammo != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(simple_rifle_ammo,
					new DispenseSimpleRifleAmmo());
		}
		if (tranquilizer != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(tranquilizer,
					new DispenseTranquilizer());
		}
		if (rocket_propelled_grenade != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(rocket_propelled_grenade,
					new DispenseRocketPropelledGrenade());
		}
		if (ballista != null)
		{
			DispenseBallistaBolt behavior = new DispenseBallistaBolt();
			BlockDispenser.dispenseBehaviorRegistry.putObject(ballista_bolt, behavior);
			BlockDispenser.dispenseBehaviorRegistry.putObject(Items.gunpowder, behavior);
		}
	}

	protected static ItemProjectile addItemProjectile(String name)
	{
		ItemProjectile i = new ItemProjectile();
		registerItem(name, i);
		return i;
	}

	protected static ItemRangedWeapon addShooter(ItemRangedWeapon weapon)
	{
		registerItem(weapon.getUnlocalizedName(), weapon);
		return weapon;
	}

	protected static ItemSlingshot addSlingshot(String name)
	{
		ItemSlingshot slingshot = new ItemSlingshot();
		registerItem(name, slingshot);
		return slingshot;
	}

	protected static ItemGrenade addGrenade(String name)
	{
		ItemGrenade slingshot = new ItemGrenade();
		registerItem(name, slingshot);
		return slingshot;
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

	protected static ItemMetalPick addMetalPick(String name, ToolMaterial m)
	{
		ItemMetalPick i = new ItemMetalPick(m);
		registerItem(name, i);
		return i;
	}

	protected static ItemStonePick addStonePick(String name, ToolMaterial m)
	{
		ItemStonePick i = new ItemStonePick(m);
		registerItem(name, i);
		return i;
	}

	protected static ItemStoneHatchet addStoneHatchet(String name, ToolMaterial m)
	{
		ItemStoneHatchet i = new ItemStoneHatchet(m);
		registerItem(name, i);
		return i;
	}

	protected static ItemMetalHatchet addMetalHatchet(String name, ToolMaterial m)
	{
		ItemMetalHatchet i = new ItemMetalHatchet(m);
		registerItem(name, i);
		return i;
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

	// protected static ItemBerryBush addBushItem(String name)
	// {
	// ItemBerryBush i = new ItemBerryBush();
	// registerItem(name, i);
	// return i;
	// }

//	protected static ItemCompostBin addCompostBinItem(String name)
//	{
//		ItemCompostBin i = new ItemCompostBin();
//		registerItem(name, i);
//		return i;
//	}
//
//	protected static ItemSmithy addSmithyItem(String name)
//	{
//		ItemSmithy i = new ItemSmithy();
//		registerItem(name, i);
//		return i;
//	}
//
//	protected static ItemCropPlot addCropPlot(String name)
//	{
//		ItemCropPlot i = new ItemCropPlot();
//		registerItem(name, i);
//		return i;
//	}

	protected static ItemCrystal addCrystalItem(String name)
	{
		ItemCrystal i = new ItemCrystal();
		registerItem(name, i);
		return i;
	}

	protected static ItemBallista addBallista(String name)
	{
		ItemBallista i = new ItemBallista();
		registerItem(name, i);
		return i;
	}

//	protected static ItemMortarAndPestle addMortarAndPestle(String name)
//	{
//		ItemMortarAndPestle i = new ItemMortarAndPestle();
//		registerItem(name, i);
//		return i;
//	}
//
//	protected static ItemRefiningForge addRefiningForge(String name)
//	{
//		ItemRefiningForge i = new ItemRefiningForge();
//		registerItem(name, i);
//		return i;
//	}

	protected static ItemSpyGlass addSpyGlass(String name)
	{
		ItemSpyGlass i = new ItemSpyGlass();
		registerItem(name, i);
		return i;
	}

	public static ItemSpear addSpearItem(String name, ToolMaterial mat)
	{
		ItemSpear weapon = new ItemSpear(mat);
		registerItem(name, weapon);
		return weapon;
	}

	public static ItemWoodenClub addWoodenClub(String name, ToolMaterial mat)
	{
		ItemWoodenClub weapon = new ItemWoodenClub(mat);
		registerItem(name, weapon);
		return weapon;
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

	public static ItemARKWeaponBase addWeapon(String name, ToolMaterial mat)
	{
		ItemARKWeaponBase weapon = new ItemARKWeaponBase(mat);
		registerItem(name, weapon);
		return weapon;
	}

	public static ItemARKThrowableWeaponBase addWeaponThrowable(String name, ToolMaterial mat)
	{
		ItemARKThrowableWeaponBase weapon = new ItemARKThrowableWeaponBase(mat);
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
