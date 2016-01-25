package com.arkcraft.module.core.client.proxy;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.client.event.CoreClientEventHandler;
import com.arkcraft.module.core.client.model.ModelBrontosaurus;
import com.arkcraft.module.core.client.model.ModelDodo;
import com.arkcraft.module.core.client.model.ModelRaptorNew;
import com.arkcraft.module.core.client.model.ModelSabertooth;
import com.arkcraft.module.core.client.render.RenderBrontosaurus;
import com.arkcraft.module.core.client.render.RenderDodo;
import com.arkcraft.module.core.client.render.RenderRaptor;
import com.arkcraft.module.core.client.render.RenderSabertooth;
import com.arkcraft.module.core.common.entity.aggressive.EntityRaptor;
import com.arkcraft.module.core.common.entity.aggressive.EntitySabertooth;
import com.arkcraft.module.core.common.entity.neutral.EntityBrontosaurus;
import com.arkcraft.module.core.common.entity.passive.EntityDodo;
import com.arkcraft.module.core.common.network.ARKMessagePipeline;
import com.arkcraft.module.core.common.proxy.CommonProxy;
import com.arkcraft.module.item.client.event.KeyBindings;
import com.arkcraft.module.item.client.event.Mod2ClientEventHandler;
import com.arkcraft.module.item.client.gui.overlay.GuiOverlay;
import com.arkcraft.module.item.client.gui.overlay.GuiOverlayReloading;
import com.arkcraft.module.item.client.render.RenderAdvancedBullet;
import com.arkcraft.module.item.client.render.RenderSimpleBullet;
import com.arkcraft.module.item.client.render.RenderSimpleRifleAmmo;
import com.arkcraft.module.item.client.render.RenderSimpleShotgunAmmo;
import com.arkcraft.module.item.client.render.RenderSpear;
import com.arkcraft.module.item.client.render.RenderTranquilizer;
import com.arkcraft.module.item.common.blocks.ARKCraftBlocks;
import com.arkcraft.module.item.common.config.ModuleItemBalance;
import com.arkcraft.module.item.common.entity.EntityCobble;
import com.arkcraft.module.item.common.entity.EntityDodoEgg;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityAdvancedBullet;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityBase;
import com.arkcraft.module.item.common.entity.item.projectiles.EntitySimpleBullet;
import com.arkcraft.module.item.common.entity.item.projectiles.EntitySimpleRifleAmmo;
import com.arkcraft.module.item.common.entity.item.projectiles.EntitySimpleShotgunAmmo;
import com.arkcraft.module.item.common.entity.item.projectiles.EntitySpear;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityTranquilizer;
import com.arkcraft.module.item.common.handlers.PotionEffectHandler;
import com.arkcraft.module.item.common.items.ARKCraftItems;
import com.arkcraft.module.item.common.items.ItemARKFood;
import com.arkcraft.module.item.common.items.weapons.ItemARKBow;

public class ClientProxy extends CommonProxy
{
	boolean initDone = false;
	public static ItemARKBow bow;

	@Override
	public void init()
	{
		if (initDone) { return; }

		MinecraftForge.EVENT_BUS.register(new GuiOverlay());
		MinecraftForge.EVENT_BUS.register(new GuiOverlayReloading());

		RenderingRegistry.registerEntityRenderingHandler(EntityCobble.class, new RenderSnowball(
				Minecraft.getMinecraft().getRenderManager(), ARKCraftItems.rock, Minecraft
						.getMinecraft().getRenderItem()));
		/*
		 * RenderingRegistry.registerEntityRenderingHandler(EntityTranqArrow.class
		 * , new RenderTranqArrow());
		 * RenderingRegistry.registerEntityRenderingHandler
		 * (EntityStoneArrow.class, new RenderStoneArrow());
		 * RenderingRegistry.registerEntityRenderingHandler
		 * (EntityMetalArrow.class, new RenderMetalArrow());
		 */
		RenderingRegistry.registerEntityRenderingHandler(EntityDodoEgg.class, new RenderSnowball(
				Minecraft.getMinecraft().getRenderManager(), ARKCraftItems.dodo_egg, Minecraft
						.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityBase.class, new RenderSnowball(
				Minecraft.getMinecraft().getRenderManager(), ARKCraftItems.grenade, Minecraft
						.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityRaptor.class, new RenderRaptor(
				new ModelRaptorNew(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntitySabertooth.class,
				new RenderSabertooth(new ModelSabertooth(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityDodo.class, new RenderDodo(
				new ModelDodo(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBrontosaurus.class,
				new RenderBrontosaurus(new ModelBrontosaurus(), 0.5f));
		// RenderingRegistry.registerEntityRenderingHandler(EntityTranqAmmo.class,
		// new RenderTranqAmmo());
		// RenderingRegistry.registerEntityRenderingHandler(EntitySimpleBullet.class,
		// new RenderSimpleBullet());

		GameRegistry.addSmelting(ARKCraftItems.meat_raw,
				new ItemStack(ARKCraftItems.meat_cooked, 1),
				(int) Math.floor(ItemARKFood.globalHealAmount / 2));
		GameRegistry.addSmelting(ARKCraftItems.primemeat_raw, new ItemStack(
				ARKCraftItems.primemeat_cooked, 1), (int) Math
				.floor(ItemARKFood.globalHealAmount / 2));

		ModelBakery.addVariantName(ARKCraftItems.slingshot, "arkcraft:slingshot",
				"arkcraft:slingshot_pulled");
		ModelBakery.addVariantName(ARKCraftItems.longneck_rifle, "arkcraft:longneck_rifle",
				"arkcraft:longneck_rifle_scoped", "arkcraft:longneck_rifle_scoped_reload",
				"arkcraft:longneck_rifle_reload");
		ModelBakery.addVariantName(ARKCraftItems.shotgun, "arkcraft:shotgun",
				"arkcraft:shotgun_reload");

		ModelBakery.addVariantName(ARKCraftItems.bow, "arkcraft:bow", "arkcraft:bow_pulling_0",
				"arkcraft:bow_pulling_1", "arkcraft:bow_pulling_2");

		KeyBindings.preInit();
		dossierProxy.init();
		LogHelper.info("CommonProxy: Init run finished.");
		initDone = true;
	}

	@Override
	public void registerPackets(ARKMessagePipeline pipeline)
	{
		super.registerPackets(pipeline);
	}

	@Override
	public void registerEventHandlers()
	{
		super.registerEventHandlers();

		CoreClientEventHandler mod1Eventhandler = new CoreClientEventHandler();
		FMLCommonHandler.instance().bus().register(mod1Eventhandler);
		MinecraftForge.EVENT_BUS.register(mod1Eventhandler);

		MinecraftForge.EVENT_BUS.register(new PotionEffectHandler());

		Mod2ClientEventHandler mod2Eventhandler = new Mod2ClientEventHandler();
		FMLCommonHandler.instance().bus().register(mod2Eventhandler);
		MinecraftForge.EVENT_BUS.register(mod2Eventhandler);
	}

	@Override
	public void registerWeapons()
	{
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

	/* We register the block/item textures and models here */
	@Override
	public void registerRenderers()
	{
		for (Map.Entry<String, Block> e : ARKCraftBlocks.allBlocks.entrySet())
		{
			String name = e.getKey();
			Block b = e.getValue();
			registerBlockTexture(b, name);
		}

		for (Map.Entry<String, Item> e : ARKCraftItems.allItems.entrySet())
		{
			String name = e.getKey();
			Item item = e.getValue();
			registerItemTexture(item, name);
		}
	}

	public void registerBlockTexture(final Block block, final String blockName)
	{
		registerBlockTexture(block, 0, blockName);
	}

	public void registerBlockTexture(final Block block, int meta, final String blockName)
	{
		Minecraft
				.getMinecraft()
				.getRenderItem()
				.getItemModelMesher()
				.register(Item.getItemFromBlock(block), meta,
						new ModelResourceLocation(ARKCraft.MODID + ":" + blockName, "inventory"));
	}

	public void registerItemTexture(final Item item, final String name)
	{
		registerItemTexture(item, 0, name);
	}

	public void registerItemTexture(final Item item, int meta, final String name)
	{
		Minecraft
				.getMinecraft()
				.getRenderItem()
				.getItemModelMesher()
				.register(item, meta,
						new ModelResourceLocation(ARKCraft.MODID + ":" + name, "inventory"));
		ModelBakery.addVariantName(item, ARKCraft.MODID + ":" + name);
	}

	public EntityPlayer getPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
	// public void registerSound() {
	// MinecraftForge.EVENT_BUS.register(new SoundHandler());
	// }
}
