package com.arkcraft.mod.client.proxy;

import java.util.Map;

import net.ilexiconn.llibrary.client.render.RenderHelper;
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
import com.arkcraft.mod.client.event.Mod1ClientEventHandler;
import com.arkcraft.mod.client.model.ModelBrontosaurus;
import com.arkcraft.mod.client.model.ModelDodo;
import com.arkcraft.mod.client.model.ModelRaptorNew;
import com.arkcraft.mod.client.render.RenderBrontosaurus;
import com.arkcraft.mod.client.render.RenderDodo;
import com.arkcraft.mod.client.render.RenderRaptor;
import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.common.entity.neutral.EntityBrontosaurus;
import com.arkcraft.mod.common.entity.passive.EntityDodo;
import com.arkcraft.mod.common.proxy.CommonProxy;
import com.arkcraft.mod2.client.event.Mod2ClientEventHandler;
import com.arkcraft.mod2.client.gui.overlay.GuiOverlay;
import com.arkcraft.mod2.client.gui.overlay.GuiOverlayReloading;
import com.arkcraft.mod2.client.model.override.PlayerModelOverride;
import com.arkcraft.mod2.client.render.RenderMetalArrow;
import com.arkcraft.mod2.client.render.RenderSimpleBullet;
import com.arkcraft.mod2.client.render.RenderSimpleRifleAmmo;
import com.arkcraft.mod2.client.render.RenderSimpleShotgunAmmo;
import com.arkcraft.mod2.client.render.RenderSpear;
import com.arkcraft.mod2.client.render.RenderStoneArrow;
import com.arkcraft.mod2.client.render.RenderTranqArrow;
import com.arkcraft.mod2.client.render.RenderTranquilizer;
import com.arkcraft.mod2.common.blocks.ARKCraftBlocks;
import com.arkcraft.mod2.common.config.MOD2_BALANCE;
import com.arkcraft.mod2.common.entity.EntityCobble;
import com.arkcraft.mod2.common.entity.EntityDodoEgg;
import com.arkcraft.mod2.common.entity.item.projectiles.EntityBase;
import com.arkcraft.mod2.common.entity.item.projectiles.EntityMetalArrow;
import com.arkcraft.mod2.common.entity.item.projectiles.EntitySimpleBullet;
import com.arkcraft.mod2.common.entity.item.projectiles.EntitySimpleRifleAmmo;
import com.arkcraft.mod2.common.entity.item.projectiles.EntitySimpleShotgunAmmo;
import com.arkcraft.mod2.common.entity.item.projectiles.EntitySpear;
import com.arkcraft.mod2.common.entity.item.projectiles.EntityStoneArrow;
import com.arkcraft.mod2.common.entity.item.projectiles.EntityTranqArrow;
import com.arkcraft.mod2.common.entity.item.projectiles.EntityTranquilizer;
import com.arkcraft.mod2.common.handlers.PotionEffectHandler;
import com.arkcraft.mod2.common.items.ARKCraftItems;
import com.arkcraft.mod2.common.items.ItemARKFood;
import com.arkcraft.mod2.common.network.ARKMessagePipeline;

public class ClientProxy extends CommonProxy
{
	boolean initDone = false;
	
	@SuppressWarnings("deprecation")
	@Override
	public void init() {
		if(initDone) return;

		MinecraftForge.EVENT_BUS.register(new GuiOverlay());
		MinecraftForge.EVENT_BUS.register(new GuiOverlayReloading());

		RenderingRegistry.registerEntityRenderingHandler(EntityCobble.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ARKCraftItems.rock, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityTranqArrow.class, new RenderTranqArrow());
		RenderingRegistry.registerEntityRenderingHandler(EntityStoneArrow.class, new RenderStoneArrow());
		RenderingRegistry.registerEntityRenderingHandler(EntityMetalArrow.class, new RenderMetalArrow());


		RenderingRegistry.registerEntityRenderingHandler(EntityDodoEgg.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ARKCraftItems.dodo_egg, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityBase.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ARKCraftItems.grenade, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityRaptor.class, new RenderRaptor(new ModelRaptorNew(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityDodo.class, new RenderDodo(new ModelDodo(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBrontosaurus.class, new RenderBrontosaurus(new ModelBrontosaurus(), 0.5f));
	//	RenderingRegistry.registerEntityRenderingHandler(EntityTranqAmmo.class, new RenderTranqAmmo());
	//	RenderingRegistry.registerEntityRenderingHandler(EntitySimpleBullet.class, new RenderSimpleBullet());
		
		GameRegistry.addSmelting(ARKCraftItems.meat_raw, new ItemStack(ARKCraftItems.meat_cooked, 1), (int) Math.floor(ItemARKFood.globalHealAmount/2));
		GameRegistry.addSmelting(ARKCraftItems.primemeat_raw, new ItemStack(ARKCraftItems.primemeat_cooked, 1), (int) Math.floor(ItemARKFood.globalHealAmount/2));
		
		ModelBakery.addVariantName(ARKCraftItems.slingshot, "arkcraft:slingshot", "arkcraft:slingshot_pulled");
		ModelBakery.addVariantName(ARKCraftItems.longneck_rifle, "arkcraft:longneck_rifle", "arkcraft:longneck_rifle_scoped");

		RenderHelper.registerModelExtension(new PlayerModelOverride());

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
	public void registerEventHandlers()	{
		super.registerEventHandlers();
		
		Mod1ClientEventHandler mod1Eventhandler = new Mod1ClientEventHandler();
		FMLCommonHandler.instance().bus().register(mod1Eventhandler);
		MinecraftForge.EVENT_BUS.register(mod1Eventhandler);
		
		MinecraftForge.EVENT_BUS.register(new PotionEffectHandler());
		
		Mod2ClientEventHandler mod2Eventhandler = new Mod2ClientEventHandler();
		FMLCommonHandler.instance().bus().register(mod2Eventhandler);
		MinecraftForge.EVENT_BUS.register(mod2Eventhandler);
	}
	
	
	@Override
	public void registerWeapons(){
	if (MOD2_BALANCE.WEAPONS.SIMPLE_PISTOL){
		RenderingRegistry.registerEntityRenderingHandler(EntitySimpleBullet.class, new RenderSimpleBullet());
	}
	if (MOD2_BALANCE.WEAPONS.SHOTGUN){
		RenderingRegistry.registerEntityRenderingHandler(EntitySimpleShotgunAmmo.class, new RenderSimpleShotgunAmmo());
	}
	if (MOD2_BALANCE.WEAPONS.LONGNECK_RIFLE)	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySimpleRifleAmmo.class, new RenderSimpleRifleAmmo());
		RenderingRegistry.registerEntityRenderingHandler(EntityTranquilizer.class, new RenderTranquilizer());
	}
	if (MOD2_BALANCE.WEAPONS.SPEAR)	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySpear.class, new RenderSpear());
	}
}
	
	/* We register the block/item textures and models here */
	@Override
	public void registerRenderers() {
		for(Map.Entry<String, Block> e : ARKCraftBlocks.allBlocks.entrySet()) {
			String name = e.getKey();
			Block b = e.getValue();
			registerBlockTexture(b, name);
		}
		
		for(Map.Entry<String, Item> e : ARKCraftItems.allItems.entrySet()) {
			String name = e.getKey();
			Item item = e.getValue();
			registerItemTexture(item, name);
		}
	}
	
	public void registerBlockTexture(final Block block, final String blockName) {
		registerBlockTexture(block, 0, blockName);
	}
	
	public void registerBlockTexture(final Block block, int meta, final String blockName) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(ARKCraft.MODID + ":" + blockName, "inventory"));
	}
	
	public void registerItemTexture(final Item item, final String name) {
		registerItemTexture(item, 0, name);
	}
	
	public void registerItemTexture(final Item item, int meta, final String name) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(ARKCraft.MODID + ":" + name, "inventory"));
        ModelBakery.addVariantName(item, ARKCraft.MODID + ":" + name);
	}

	public EntityPlayer getPlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}
	//public void registerSound() {
	//	MinecraftForge.EVENT_BUS.register(new SoundHandler());
	//}
}
