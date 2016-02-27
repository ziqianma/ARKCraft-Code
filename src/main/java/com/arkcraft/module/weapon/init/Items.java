package com.arkcraft.module.weapon.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.GlobalAdditions;
import com.arkcraft.module.core.common.handlers.EntityHandler;
import com.arkcraft.module.crafting.common.config.ModuleItemBalance;
import com.arkcraft.module.items.ARKCraftItems;
import com.arkcraft.module.weapon.common.entity.EntityAdvancedBullet;
import com.arkcraft.module.weapon.common.entity.EntityBallista;
import com.arkcraft.module.weapon.common.entity.EntityBallistaBolt;
import com.arkcraft.module.weapon.common.entity.EntityGrenade;
import com.arkcraft.module.weapon.common.entity.EntityMetalArrow;
import com.arkcraft.module.weapon.common.entity.EntityRocketPropelledGrenade;
import com.arkcraft.module.weapon.common.entity.EntitySimpleBullet;
import com.arkcraft.module.weapon.common.entity.EntitySimpleRifleAmmo;
import com.arkcraft.module.weapon.common.entity.EntitySimpleShotgunAmmo;
import com.arkcraft.module.weapon.common.entity.EntitySpear;
import com.arkcraft.module.weapon.common.entity.EntityStoneArrow;
import com.arkcraft.module.weapon.common.entity.EntityTranqArrow;
import com.arkcraft.module.weapon.common.entity.EntityTranquilizer;
import com.arkcraft.module.weapon.common.entity.dispense.DispenseBallistaBolt;
import com.arkcraft.module.weapon.common.entity.dispense.DispenseRocketPropelledGrenade;
import com.arkcraft.module.weapon.common.entity.dispense.DispenseSimpleBullet;
import com.arkcraft.module.weapon.common.entity.dispense.DispenseSimpleRifleAmmo;
import com.arkcraft.module.weapon.common.entity.dispense.DispenseSimpleShotgunAmmo;
import com.arkcraft.module.weapon.common.entity.dispense.DispenseTranquilizer;
import com.arkcraft.module.weapon.common.item.ItemARKWeaponBase;
import com.arkcraft.module.weapon.common.item.ItemBallista;
import com.arkcraft.module.weapon.common.item.ItemGrenade;
import com.arkcraft.module.weapon.common.item.ItemMetalHatchet;
import com.arkcraft.module.weapon.common.item.ItemMetalPick;
import com.arkcraft.module.weapon.common.item.ItemStoneHatchet;
import com.arkcraft.module.weapon.common.item.ItemStonePick;
import com.arkcraft.module.weapon.common.item.ItemWoodenClub;
import com.arkcraft.module.weapon.common.item.ammo.ItemProjectile;
import com.arkcraft.module.weapon.common.item.attachment.AttachmentType;
import com.arkcraft.module.weapon.common.item.attachment.ItemAttachment;
import com.arkcraft.module.weapon.common.item.ranged.ItemARKBow;
import com.arkcraft.module.weapon.common.item.ranged.ItemCompoundBow;
import com.arkcraft.module.weapon.common.item.ranged.ItemCrossbow;
import com.arkcraft.module.weapon.common.item.ranged.ItemFabricatedPistol;
import com.arkcraft.module.weapon.common.item.ranged.ItemLongneckRifle;
import com.arkcraft.module.weapon.common.item.ranged.ItemRangedWeapon;
import com.arkcraft.module.weapon.common.item.ranged.ItemRocketLauncher;
import com.arkcraft.module.weapon.common.item.ranged.ItemShotgun;
import com.arkcraft.module.weapon.common.item.ranged.ItemSimplePistol;
import com.arkcraft.module.weapon.common.item.ranged.ItemSlingshot;
import com.arkcraft.module.weapon.common.item.ranged.ItemSpear;

public class Items
{
	public ItemGrenade grenade;
	public ItemAttachment scope, flash_light, silencer, laser, holo_scope;
	public ItemSlingshot slingshot;
	public ItemARKWeaponBase ironPike;
	public ItemSpear spear;
	public ItemWoodenClub wooden_club;
	public ItemARKBow bow;
	public ItemCompoundBow compound_bow;
	public ItemProjectile tranquilizer, stone_arrow, tranq_arrow, metal_arrow,
			ballista_bolt, simple_bullet, simple_rifle_ammo,
			simple_shotgun_ammo, rocket_propelled_grenade, advanced_bullet;
	public ItemRangedWeapon rocket_launcher, tranq_gun, simple_pistol,
			fabricated_pistol, longneck_rifle, shotgun, crossbow;
	public ItemBallista ballista;
	public static ItemStonePick stone_pick;
	public static ItemStoneHatchet stone_hatchet;
	public static ItemMetalPick metal_pick;
	public static ItemMetalHatchet metal_hatchet;
	
	public static ToolMaterial METAL = EnumHelper.addToolMaterial("METAL_MAT",
			3, 1500, 6.0F, 0.8F, 8);
	public static ToolMaterial STONE = EnumHelper.addToolMaterial("STONE_MAT",
			2, 500, 3.5F, 0.4F, 13);

	public Items()
	{
		init();
	}

	private void init()
	{
		// weapon attachments
		scope = addItemAttachment("scope", AttachmentType.SCOPE);
		flash_light = addItemAttachment("flash_light", AttachmentType.FLASH);
		holo_scope = addItemAttachment("holo_scope", AttachmentType.HOLO_SCOPE);
		laser = addItemAttachment("laser", AttachmentType.LASER);
		silencer = addItemAttachment("silencer", AttachmentType.SILENCER);

		slingshot = addSlingshot("slingshot");
		grenade = addGrenade("grenade");
		// stoneSpear = addWeaponThrowable("stoneSpear", ToolMaterial.STONE);
		ironPike = addWeapon("ironPike", ToolMaterial.IRON);
		ballista = addBallista("ballista");
		ballista_bolt = addItemProjectile("ballista_bolt");
		
		//Tools
		metal_pick = addMetalPick("metal_pick", METAL);
		metal_hatchet = addMetalHatchet("metal_hatchet", METAL);
		stone_hatchet = addStoneHatchet("stone_hatchet", STONE);
		stone_pick = addStonePick("stone_pick", STONE);

		// Bows
		compound_bow = new ItemCompoundBow();
		registerItem("compound_bow", compound_bow);

		bow = new ItemARKBow();
		registerItem("bow", bow);

		// Bullets
		metal_arrow = addItemProjectile("metal_arrow");
		tranq_arrow = addItemProjectile("tranq_arrow");
		stone_arrow = addItemProjectile("stone_arrow");

		spear = addSpearItem("spear", ToolMaterial.WOOD);
		wooden_club = addWoodenClub("wooden_club", ToolMaterial.WOOD);

		EntityHandler.registerModEntity(EntityBallista.class, "ballista",
				ARKCraft.instance, 64, 128, false);
		EntityHandler.registerModEntity(EntityBallistaBolt.class,
				"ballistaBolt", ARKCraft.instance, 64, 20, true);
		EntityHandler.registerModEntity(EntityGrenade.class, "grenade",
				ARKCraft.instance, 64, 10, true);
		EntityHandler.registerModEntity(EntitySpear.class, "spear",
				ARKCraft.instance, 64, 10, true);

		registerDispenseBehavior();
		registerWeaponEntities();
		addRangedWeapons();
	}

	public Map<String, Item> allItems = new HashMap<String, Item>();

	public Map<String, Item> getAllItems()
	{
		return allItems;
	}

	private ItemAttachment addItemAttachment(String name, AttachmentType type)
	{
		ItemAttachment i = new ItemAttachment(name, type);
		registerItem(name, i);
		return i;
	}

	private void registerWeaponEntities()
	{
		if (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL)
		{
			EntityHandler.registerModEntity(EntitySimpleBullet.class,
					"simple_bullet", ARKCraft.instance, 16, 20, true);
		}

		if (ModuleItemBalance.WEAPONS.SHOTGUN)
		{
			EntityHandler.registerModEntity(EntitySimpleShotgunAmmo.class,
					"simple_shotgun_ammo", ARKCraft.instance, 64, 10, true);
		}

		if (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE)
		{
			EntityHandler.registerModEntity(EntitySimpleRifleAmmo.class,
					"simple_rifle_ammo", ARKCraft.instance, 64, 10, true);
			EntityHandler.registerModEntity(EntityTranquilizer.class,
					"tranquilizer_dart", ARKCraft.instance, 64, 10, true);
		}

		if (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL)
		{
			EntityHandler.registerModEntity(EntityAdvancedBullet.class,
					"advanced_bullet", ARKCraft.instance, 64, 10, true);
		}

		if (ModuleItemBalance.WEAPONS.ROCKET_LAUNCHER)
		{
			EntityHandler
					.registerModEntity(EntityRocketPropelledGrenade.class,
							"rocket_propelled_grenade", ARKCraft.instance, 64,
							10, true);
		}

		EntityHandler.registerModEntity(EntityTranqArrow.class, "tranq_arrow",
				ARKCraft.instance, 64, 10, true);
		EntityHandler.registerModEntity(EntityStoneArrow.class, "stone_arrow",
				ARKCraft.instance, 64, 10, true);
		EntityHandler.registerModEntity(EntityMetalArrow.class, "metal_arrow",
				ARKCraft.instance, 64, 10, true);
	}

	public void addRangedWeapons()
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
			crossbow.registerProjectile(metal_arrow);
			crossbow.registerProjectile(tranq_arrow);
			crossbow.registerProjectile(stone_arrow);
		}
	}

	public void registerDispenseBehavior()
	{
		if (simple_bullet != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(simple_bullet,
					new DispenseSimpleBullet());
		}
		if (simple_shotgun_ammo != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(
					simple_shotgun_ammo, new DispenseSimpleShotgunAmmo());
		}
		if (simple_rifle_ammo != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(
					simple_rifle_ammo, new DispenseSimpleRifleAmmo());
		}
		if (tranquilizer != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(tranquilizer,
					new DispenseTranquilizer());
		}
		if (rocket_propelled_grenade != null)
		{
			BlockDispenser.dispenseBehaviorRegistry.putObject(
					rocket_propelled_grenade,
					new DispenseRocketPropelledGrenade());
		}
		if (ballista != null)
		{
			DispenseBallistaBolt behavior = new DispenseBallistaBolt();
			BlockDispenser.dispenseBehaviorRegistry.putObject(ballista_bolt,
					behavior);
			BlockDispenser.dispenseBehaviorRegistry.putObject(
					ARKCraftItems.gun_powder, behavior);
		}
	}

	protected ItemProjectile addItemProjectile(String name)
	{
		ItemProjectile i = new ItemProjectile();
		registerItem(name, i);
		return i;
	}

	protected ItemRangedWeapon addShooter(ItemRangedWeapon weapon)
	{
		registerItem(weapon.getUnlocalizedName(), weapon);
		return weapon;
	}

	protected ItemSlingshot addSlingshot(String name)
	{
		ItemSlingshot slingshot = new ItemSlingshot();
		registerItem(name, slingshot);
		return slingshot;
	}

	protected ItemGrenade addGrenade(String name)
	{
		ItemGrenade slingshot = new ItemGrenade();
		registerItem(name, slingshot);
		return slingshot;
	}

	protected ItemBallista addBallista(String name)
	{
		ItemBallista i = new ItemBallista();
		registerItem(name, i);
		return i;
	}

	public ItemSpear addSpearItem(String name, ToolMaterial mat)
	{
		ItemSpear weapon = new ItemSpear(mat);
		registerItem(name, weapon);
		return weapon;
	}

	public ItemWoodenClub addWoodenClub(String name, ToolMaterial mat)
	{
		ItemWoodenClub weapon = new ItemWoodenClub(mat);
		registerItem(name, weapon);
		return weapon;
	}

	public ItemARKWeaponBase addWeapon(String name, ToolMaterial mat)
	{
		ItemARKWeaponBase weapon = new ItemARKWeaponBase(mat);
		registerItem(name, weapon);
		return weapon;
	}
	
	public ItemMetalPick addMetalPick(String name, ToolMaterial m)
	{
		ItemMetalPick i = new ItemMetalPick(m);
		registerItem(name, i);
		return i;
	}

	public ItemStonePick addStonePick(String name, ToolMaterial m)
	{
		ItemStonePick i = new ItemStonePick(m);
		registerItem(name, i);
		return i;
	}

	public ItemStoneHatchet addStoneHatchet(String name, ToolMaterial m)
	{
		ItemStoneHatchet i = new ItemStoneHatchet(m);
		registerItem(name, i);
		return i;
	}

	public ItemMetalHatchet addMetalHatchet(String name, ToolMaterial m)
	{
		ItemMetalHatchet i = new ItemMetalHatchet(m);
		registerItem(name, i);
		return i;
	}

	public void registerItem(String name, Item item)
	{
		allItems.put(name, item);
		item.setUnlocalizedName(name);
		item.setCreativeTab(GlobalAdditions.tabARKWeapons);
		GameRegistry.registerItem(item, name);
	}
}
