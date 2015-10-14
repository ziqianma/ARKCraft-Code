package com.arkcraft.mod.common.items.weapons;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.handlers.EntityHandler;
import com.arkcraft.mod.common.items.ARKItem;
import com.arkcraft.mod.common.items.ARKSlingshot;
import com.arkcraft.mod.common.items.ARKWeapon;
import com.arkcraft.mod.common.items.weapons.bullets.ItemProjectile;
import com.arkcraft.mod.common.items.weapons.component.RangedCompCrossbow;
import com.arkcraft.mod.common.items.weapons.component.RangedCompLongneckRifle;
import com.arkcraft.mod.common.items.weapons.component.RangedCompRocketLauncher;
import com.arkcraft.mod.common.items.weapons.component.RangedCompShotgun;
import com.arkcraft.mod.common.items.weapons.component.RangedCompSimplePistol;
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
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.Map;

public class ModWeapons {
	
	public static Map<String, Block> allBlocks = new HashMap<String, Block>();
	public static Map<String, Item> allItems = new HashMap<String, Item>();
	
	public static ARKSlingshot slingshot;
	public static ARKWeapon ironPike;
	public static ItemSpear	spear;
	public static ItemTranqGun tranq_gun;
	public static ItemCompoundBow compound_bow;
	public static ItemRocketLauncher rocket_launcher;
	public static ItemProjectile tranquilizer, stone_arrow, tranq_arrow, metal_arrow;
	public static ItemProjectile simple_bullet, simple_rifle_ammo, simple_shotgun_ammo, rocket_propelled_grenade;
	public static ItemSimplePistol simple_pistol;
	public static ItemLongneckRifle longneck_rifle;
	public static ItemShotgun shotgun;	
	public static ItemCrossbow crossbow;
	public static ARKItem cobble_ball,explosive_ball;


	public static void init() {
	
		compound_bow = new ItemCompoundBow("compound_bow");
		spear = addSpearItem("spear", ToolMaterial.STONE);
		cobble_ball = addItemWithTooltip("cobble_ball", EnumChatFormatting.GOLD + "A Rocky Road to Victory");
		explosive_ball = addItemWithTooltip("explosive_ball", EnumChatFormatting.RED + "A Rocky Road to Destruction");
		slingshot = addSlingshot("slingshot");
		ironPike = addWeapon("ironPike", ToolMaterial.IRON);
		
		addGunPowderWeapons();
		registerWeaponEntities();
		registerDispenseBehavior();
		
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
			simple_pistol = addSimplePistol("simple_pistol", new RangedCompSimplePistol());
			simple_bullet = addItemProjectile("simple_bullet");
		}
		if (BALANCE.WEAPONS.LONGNECK_RIFLE) {
			longneck_rifle = addLongneckRifle("longneck_rifle", new RangedCompLongneckRifle());
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
	
	public static ItemSpear addSpearItem(String name, ToolMaterial mat) {
		ItemSpear weapon = new ItemSpear(name, mat);
		allItems.put(name, weapon);
		return weapon;
	}
	
	public static ARKItem addItemWithTooltip(String name, String... tooltips) {
		ARKItem item = new ARKItem(name, tooltips);
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
	
	public static ModWeapons getInstance() { return new ModWeapons(); }
	public static Map<String, Block> getAllBlocks() { return allBlocks; }
	public static Map<String, Item> getAllItems() { return allItems; }
}

