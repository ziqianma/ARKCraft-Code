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
import com.arkcraft.module.blocks.client.event.ItemsClientEventHandler;
import com.arkcraft.module.blocks.client.event.KeyBindings;
import com.arkcraft.module.blocks.client.event.Mod2ClientEventHandler;
import com.arkcraft.module.blocks.common.blocks.ARKCraftBlocks;
import com.arkcraft.module.blocks.common.handlers.PotionEffectHandler;
import com.arkcraft.module.blocks.common.items.ARKCraftItems;
import com.arkcraft.module.blocks.common.items.ItemARKFood;
import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.client.event.CoreClientEventHandler;
import com.arkcraft.module.core.client.gui.overlay.GuiOverlay;
import com.arkcraft.module.core.common.proxy.CommonProxy;
import com.arkcraft.module.weapon.common.entity.EntityStone;

public class ClientProxy extends CommonProxy
{
	boolean initDone = false;

	@Override
	public void init()
	{
		if (initDone) { return; }
		super.init();

		ItemsClientEventHandler.init();

		MinecraftForge.EVENT_BUS.register(new GuiOverlay());

		GameRegistry.addSmelting(ARKCraftItems.meat_raw, new ItemStack(
				ARKCraftItems.meat_cooked, 1), (int) Math
				.floor(ItemARKFood.globalHealAmount / 2));
		GameRegistry.addSmelting(ARKCraftItems.primemeat_raw, new ItemStack(
				ARKCraftItems.primemeat_cooked, 1), (int) Math
				.floor(ItemARKFood.globalHealAmount / 2));

		KeyBindings.preInit();
		dossierProxy.init();
		LogHelper.info("CommonProxy: Init run finished.");
		initDone = true;
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
		registerItemTexture(Item.getItemFromBlock(block), meta, blockName);
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
				.register(
						item,
						meta,
						new ModelResourceLocation(ARKCraft.MODID + ":" + name,
								"inventory"));
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
