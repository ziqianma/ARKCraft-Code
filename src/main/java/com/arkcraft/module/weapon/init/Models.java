package com.arkcraft.module.weapon.init;

import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.crafting.common.config.ModuleItemBalance;
import com.arkcraft.module.items.ARKCraftItems;
import com.arkcraft.module.weapon.WeaponModule;
import com.arkcraft.module.weapon.client.render.RenderAdvancedBullet;
import com.arkcraft.module.weapon.client.render.RenderMetalArrow;
import com.arkcraft.module.weapon.client.render.RenderSimpleBullet;
import com.arkcraft.module.weapon.client.render.RenderSimpleRifleAmmo;
import com.arkcraft.module.weapon.client.render.RenderSimpleShotgunAmmo;
import com.arkcraft.module.weapon.client.render.RenderSpear;
import com.arkcraft.module.weapon.client.render.RenderStoneArrow;
import com.arkcraft.module.weapon.client.render.RenderTranqArrow;
import com.arkcraft.module.weapon.client.render.RenderTranquilizer;
import com.arkcraft.module.weapon.common.entity.EntityAdvancedBullet;
import com.arkcraft.module.weapon.common.entity.EntityGrenade;
import com.arkcraft.module.weapon.common.entity.EntityMetalArrow;
import com.arkcraft.module.weapon.common.entity.EntitySimpleBullet;
import com.arkcraft.module.weapon.common.entity.EntitySimpleRifleAmmo;
import com.arkcraft.module.weapon.common.entity.EntitySimpleShotgunAmmo;
import com.arkcraft.module.weapon.common.entity.EntitySpear;
import com.arkcraft.module.weapon.common.entity.EntityStone;
import com.arkcraft.module.weapon.common.entity.EntityStoneArrow;
import com.arkcraft.module.weapon.common.entity.EntityTranqArrow;
import com.arkcraft.module.weapon.common.entity.EntityTranquilizer;
import com.arkcraft.module.weapon.common.item.ranged.ItemRangedWeapon;

public class Models
{
	public static void init()
	{
		// Register textures for all items
		for (Entry<String, Item> i : WeaponModule.items.allItems.entrySet())
		{
			registerItemTexture(i.getValue(), 0, i.getKey());
		}

		// Register models for entities
		registerEntityModels();

		// Register variant models for all weapon items
		registerItemVariants();
	}

	private static void registerItemTexture(final Item item, int meta, String name)
	{
		if (item instanceof ItemRangedWeapon) name = "weapons/" + name;
		Minecraft
				.getMinecraft()
				.getRenderItem()
				.getItemModelMesher()
				.register(
						item,
						meta,
						new ModelResourceLocation(ARKCraft.MODID + ":" + name,
								"inventory"));
		ModelBakery.addVariantName(item, ARKCraft.MODID + ":" + name);
	}

	private static void registerEntityModels()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityStone.class,
				new RenderSnowball(Minecraft.getMinecraft().getRenderManager(),
						ARKCraftItems.rock, Minecraft.getMinecraft()
								.getRenderItem()));

		RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class,
				new RenderSnowball(Minecraft.getMinecraft().getRenderManager(),
						WeaponModule.items.grenade, Minecraft.getMinecraft()
								.getRenderItem()));

		RenderingRegistry.registerEntityRenderingHandler(
				EntityTranqArrow.class, new RenderTranqArrow());
		RenderingRegistry.registerEntityRenderingHandler(
				EntityStoneArrow.class, new RenderStoneArrow());
		RenderingRegistry.registerEntityRenderingHandler(
				EntityMetalArrow.class, new RenderMetalArrow());

		if (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL)
		{
			RenderingRegistry.registerEntityRenderingHandler(
					EntitySimpleBullet.class, new RenderSimpleBullet());
		}
		if (ModuleItemBalance.WEAPONS.SHOTGUN)
		{
			RenderingRegistry.registerEntityRenderingHandler(
					EntitySimpleShotgunAmmo.class,
					new RenderSimpleShotgunAmmo());
		}
		if (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE)
		{
			RenderingRegistry.registerEntityRenderingHandler(
					EntitySimpleRifleAmmo.class, new RenderSimpleRifleAmmo());
			RenderingRegistry.registerEntityRenderingHandler(
					EntityTranquilizer.class, new RenderTranquilizer());
		}
		if (ModuleItemBalance.WEAPONS.SPEAR)
		{
			RenderingRegistry.registerEntityRenderingHandler(EntitySpear.class,
					new RenderSpear());
		}
		if (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL)
		{
			RenderingRegistry.registerEntityRenderingHandler(
					EntityAdvancedBullet.class, new RenderAdvancedBullet());
		}
	}

	private static void registerItemVariants()
	{
		ModelBakery.addVariantName(WeaponModule.items.slingshot,
				"arkcraft:slingshot", "arkcraft:slingshot_pulled");
		ModelBakery.addVariantName(WeaponModule.items.shotgun,
				"arkcraft:weapons/shotgun", "arkcraft:weapons/shotgun_reload");
		ModelBakery.addVariantName(WeaponModule.items.longneck_rifle,
				"arkcraft:weapons/longneck_rifle",
				"arkcraft:weapons/longneck_rifle_scope",
				"arkcraft:weapons/longneck_rifle_scope_reload",
				"arkcraft:weapons/longneck_rifle_reload",
				"arkcraft:weapons/longneck_rifle_flashlight",
				"arkcraft:weapons/longneck_rifle_flashlight_reload",
				"arkcraft:weapons/longneck_rifle_laser",
				"arkcraft:weapons/longneck_rifle_laser_reload",
				"arkcraft:weapons/longneck_rifle_silencer",
				"arkcraft:weapons/longneck_rifle_silencer_reload");
		ModelBakery.addVariantName(WeaponModule.items.simple_pistol,
				"arkcraft:weapons/simple_pistol",
				"arkcraft:weapons/simple_pistol_scope",
				"arkcraft:weapons/simple_pistol_reload",
				"arkcraft:weapons/simple_pistol_scope_reload",
				"arkcraft:weapons/simple_pistol_flashlight",
				"arkcraft:weapons/simple_pistol_flashlight_reload",
				"arkcraft:weapons/simple_pistol_laser",
				"arkcraft:weapons/simple_pistol_laser_reload",
				"arkcraft:weapons/simple_pistol_silencer",
				"arkcraft:weapons/simple_pistol_silencer_reload");
		ModelBakery.addVariantName(WeaponModule.items.fabricated_pistol,
				"arkcraft:weapons/fabricated_pistol",
				"arkcraft:weapons/fabricated_pistol_scope",
				"arkcraft:weapons/fabricated_pistol_reload",
				"arkcraft:weapons/fabricated_pistol_scope_reload",
				"arkcraft:weapons/fabricated_pistol_flashlight",
				"arkcraft:weapons/fabricated_pistol_flashlight_reload",
				"arkcraft:weapons/fabricated_pistol_laser",
				"arkcraft:weapons/fabricated_pistol_laser_reload",
				"arkcraft:weapons/fabricated_pistol_silencer",
				"arkcraft:weapons/fabricated_pistol_silencer_reload",
				"arkcraft:weapons/fabricated_pistol_holo_scope",
				"arkcraft:weapons/fabricated_pistol_holo_scope_reload");

		ModelBakery.addVariantName(WeaponModule.items.bow, "arkcraft:bow",
				"arkcraft:bow_pulling_0", "arkcraft:bow_pulling_1",
				"arkcraft:bow_pulling_2");
	}
}
