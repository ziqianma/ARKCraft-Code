package com.arkcraft.module.weapon;

import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.arkcraft.module.blocks.common.config.ModuleItemBalance;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.weapon.client.event.ClientEventHandler;
import com.arkcraft.module.weapon.client.render.RenderAdvancedBullet;
import com.arkcraft.module.weapon.client.render.RenderSimpleBullet;
import com.arkcraft.module.weapon.client.render.RenderSimpleRifleAmmo;
import com.arkcraft.module.weapon.client.render.RenderSimpleShotgunAmmo;
import com.arkcraft.module.weapon.client.render.RenderSpear;
import com.arkcraft.module.weapon.client.render.RenderTranquilizer;
import com.arkcraft.module.weapon.common.entity.EntityAdvancedBullet;
import com.arkcraft.module.weapon.common.entity.EntityGrenade;
import com.arkcraft.module.weapon.common.entity.EntitySimpleBullet;
import com.arkcraft.module.weapon.common.entity.EntitySimpleRifleAmmo;
import com.arkcraft.module.weapon.common.entity.EntitySimpleShotgunAmmo;
import com.arkcraft.module.weapon.common.entity.EntitySpear;
import com.arkcraft.module.weapon.common.entity.EntityTranquilizer;
import com.arkcraft.module.weapon.common.event.CommonEventHandler;
import com.arkcraft.module.weapon.common.item.ranged.ItemRangedWeapon;
import com.arkcraft.module.weapon.init.Blocks;
import com.arkcraft.module.weapon.init.Items;

public class WeaponsModule
{
	public static Items items;
	public static Blocks blocks;

	public static void preInit()
	{
		CommonEventHandler.init();
		items = new Items();
		blocks = new Blocks();
		if (FMLCommonHandler.instance().getSide().isClient()) clientPreInit();
		else serverPreInit();
	}

	public static void init()
	{
		if (FMLCommonHandler.instance().getSide().isClient()) clientInit();
		else serverInit();
	}

	private static void clientPreInit()
	{

	}

	private static void clientInit()
	{
		ClientEventHandler.init();
		for (Entry<String, Item> i : items.allItems.entrySet())
		{
			registerItemTexture(i.getValue(), 0, i.getKey());
		}
		registerRendering();
	}

	private static void registerItemTexture(final Item item, int meta, String name)
	{
		if (item instanceof ItemRangedWeapon) name = "weapons/" + name;
		Minecraft
				.getMinecraft()
				.getRenderItem()
				.getItemModelMesher()
				.register(item, meta,
						new ModelResourceLocation(ARKCraft.MODID + ":" + name, "inventory"));
		ModelBakery.addVariantName(item, ARKCraft.MODID + ":" + name);
	}

	private static void registerRendering()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, new RenderSnowball(
				Minecraft.getMinecraft().getRenderManager(), WeaponsModule.items.grenade, Minecraft
						.getMinecraft().getRenderItem()));

		ModelBakery.addVariantName(WeaponsModule.items.slingshot, "arkcraft:slingshot",
				"arkcraft:slingshot_pulled");
		ModelBakery.addVariantName(WeaponsModule.items.shotgun, "arkcraft:weapons/shotgun",
				"arkcraft:weapons/shotgun_reload");
		ModelBakery.addVariantName(WeaponsModule.items.longneck_rifle,
				"arkcraft:weapons/longneck_rifle", "arkcraft:weapons/longneck_rifle_scope",
				"arkcraft:weapons/longneck_rifle_scope_reload",
				"arkcraft:weapons/longneck_rifle_reload",
				"arkcraft:weapons/longneck_rifle_flashlight",
				"arkcraft:weapons/longneck_rifle_flashlight_reload",
				"arkcraft:weapons/longneck_rifle_laser",
				"arkcraft:weapons/longneck_rifle_laser_reload",
				"arkcraft:weapons/longneck_rifle_silencer",
				"arkcraft:weapons/longneck_rifle_silencer_reload");
		ModelBakery.addVariantName(WeaponsModule.items.simple_pistol,
				"arkcraft:weapons/simple_pistol", "arkcraft:weapons/simple_pistol_scope",
				"arkcraft:weapons/simple_pistol_reload",
				"arkcraft:weapons/simple_pistol_scope_reload",
				"arkcraft:weapons/simple_pistol_flashlight",
				"arkcraft:weapons/simple_pistol_flashlight_reload",
				"arkcraft:weapons/simple_pistol_laser",
				"arkcraft:weapons/simple_pistol_laser_reload",
				"arkcraft:weapons/simple_pistol_silencer",
				"arkcraft:weapons/simple_pistol_silencer_reload");
		ModelBakery.addVariantName(WeaponsModule.items.fabricated_pistol,
				"arkcraft:weapons/fabricated_pistol", "arkcraft:weapons/fabricated_pistol_scope",
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

		ModelBakery.addVariantName(WeaponsModule.items.bow, "arkcraft:bow",
				"arkcraft:bow_pulling_0", "arkcraft:bow_pulling_1", "arkcraft:bow_pulling_2");

		if (ModuleItemBalance.WEAPONS.SIMPLE_PISTOL)
		{
			RenderingRegistry.registerEntityRenderingHandler(EntitySimpleBullet.class,
					new RenderSimpleBullet());
		}
		if (ModuleItemBalance.WEAPONS.SHOTGUN)
		{
			RenderingRegistry.registerEntityRenderingHandler(EntitySimpleShotgunAmmo.class,
					new RenderSimpleShotgunAmmo());
		}
		if (ModuleItemBalance.WEAPONS.LONGNECK_RIFLE)
		{
			RenderingRegistry.registerEntityRenderingHandler(EntitySimpleRifleAmmo.class,
					new RenderSimpleRifleAmmo());
			RenderingRegistry.registerEntityRenderingHandler(EntityTranquilizer.class,
					new RenderTranquilizer());
		}
		if (ModuleItemBalance.WEAPONS.SPEAR)
		{
			RenderingRegistry.registerEntityRenderingHandler(EntitySpear.class, new RenderSpear());
		}
		if (ModuleItemBalance.WEAPONS.FABRICATED_PISTOL)
		{
			RenderingRegistry.registerEntityRenderingHandler(EntityAdvancedBullet.class,
					new RenderAdvancedBullet());
		}
	}

	private static void serverPreInit()
	{

	}

	private static void serverInit()
	{

	}
}
