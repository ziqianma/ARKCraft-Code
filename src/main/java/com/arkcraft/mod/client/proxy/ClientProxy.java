package com.arkcraft.mod.client.proxy;

import com.arkcraft.mod.client.model.ModelBrontosaurus;
import com.arkcraft.mod.client.model.ModelDodo;
import com.arkcraft.mod.client.model.ModelRaptor;
import com.arkcraft.mod.client.model.ModelRaptorNew;
import com.arkcraft.mod.client.render.RenderBrontosaurus;
import com.arkcraft.mod.client.render.RenderDodo;
import com.arkcraft.mod.client.render.RenderMetalArrow;
import com.arkcraft.mod.client.render.RenderRaptor;
import com.arkcraft.mod.client.render.RenderSimpleBullet;
import com.arkcraft.mod.client.render.RenderSpear;
import com.arkcraft.mod.client.render.RenderStoneArrow;
import com.arkcraft.mod.client.render.RenderTranqArrow;
import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.blocks.ARKCraftBlocks;
import com.arkcraft.mod.common.entity.EntityCobble;
import com.arkcraft.mod.common.entity.EntityDodoEgg;
import com.arkcraft.mod.common.entity.EntityExplosive;
import com.arkcraft.mod.common.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.common.entity.neutral.EntityBrontosaurus;
import com.arkcraft.mod.common.entity.passive.EntityDodo;
import com.arkcraft.mod.common.event.ClientEventHandler;
import com.arkcraft.mod.common.items.ARKCraftItems;
import com.arkcraft.mod.common.items.weapons.projectiles.EntityMetalArrow;
import com.arkcraft.mod.common.items.weapons.projectiles.EntitySimpleBullet;
import com.arkcraft.mod.common.items.weapons.projectiles.EntitySpear;
import com.arkcraft.mod.common.items.weapons.projectiles.EntityStoneArrow;
import com.arkcraft.mod.common.items.weapons.projectiles.EntityTranqArrow;
import com.arkcraft.mod.common.lib.BALANCE;
import com.arkcraft.mod.common.lib.LogHelper;
import com.arkcraft.mod.common.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.Map;

public class ClientProxy extends CommonProxy
{
	boolean initDone = false;
	
	@Override
	public void init() {
		if(initDone) return;
		RenderingRegistry.registerEntityRenderingHandler(EntityCobble.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ARKCraftItems.cobble_ball, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityTranqArrow.class, new RenderTranqArrow());
		RenderingRegistry.registerEntityRenderingHandler(EntityStoneArrow.class, new RenderStoneArrow());
		RenderingRegistry.registerEntityRenderingHandler(EntityMetalArrow.class, new RenderMetalArrow());

		RenderingRegistry.registerEntityRenderingHandler(EntityDodoEgg.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ARKCraftItems.dodo_egg, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityExplosive.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), ARKCraftItems.explosive_ball, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityRaptor.class, new RenderRaptor(new ModelRaptorNew(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityDodo.class, new RenderDodo(new ModelDodo(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBrontosaurus.class, new RenderBrontosaurus(new ModelBrontosaurus(), 0.5f));
	//	RenderingRegistry.registerEntityRenderingHandler(EntityTranqAmmo.class, new RenderTranqAmmo());
	//	RenderingRegistry.registerEntityRenderingHandler(EntitySimpleBullet.class, new RenderSimpleBullet());
		
		ModelBakery.addVariantName(ARKCraftItems.slingshot, "arkcraft:slingshot", "arkcraft:slingshot_pulled");
		dossierProxy.init();
		LogHelper.info("CommonProxy: Init run finished.");
		initDone = true;
	}

	@Override
	public void registerEventHandlers()	{
		super.registerEventHandlers();
		ClientEventHandler eventhandler = new ClientEventHandler();
		FMLCommonHandler.instance().bus().register(eventhandler);
		MinecraftForge.EVENT_BUS.register(eventhandler);
	}
	
	
	@Override
	public void registerWeapons(){
	if (BALANCE.WEAPONS.SIMPLE_PISTOL){
		RenderingRegistry.registerEntityRenderingHandler(EntitySimpleBullet.class, new RenderSimpleBullet());
	}
	if (BALANCE.WEAPONS.SHOTGUN){
	//	RenderingRegistry.registerEntityRenderingHandler(EntitySimpleShotgunAmmo.class, new RenderSimpleShotgunAmmo());
	}
	if (BALANCE.WEAPONS.LONGNECK_RIFLE)	{
	//	RenderingRegistry.registerEntityRenderingHandler(EntitySimpleRifleAmmo.class, new RenderSimpleBullet());
	}
	if (BALANCE.WEAPONS.SPEAR)	{
		RenderingRegistry.registerEntityRenderingHandler(EntitySpear.class, new RenderSpear());
	}
	if (BALANCE.WEAPONS.TRANQ_GUN)	{
	//	RenderingRegistry.registerEntityRenderingHandler(EntityTranquilizer.class, new RenderSimpleBullet());
	}
	if (BALANCE.WEAPONS.TRANQ_GUN)	{
	// RenderingRegistry.registerEntityRenderingHandler(EntityRocketPropelledGrenade.class, new RenderRocketPropelledGrenade());
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
	//public void registerSound() {
	//	MinecraftForge.EVENT_BUS.register(new SoundHandler());
	//}
}
