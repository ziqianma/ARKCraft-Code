package com.arkcraft.mod.core.proxy;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.SidedProxy;

import com.arkcraft.mod.core.GlobalAdditions;
import com.arkcraft.mod.core.Main;
import com.arkcraft.mod.core.book.proxy.DCommon;
import com.arkcraft.mod.core.entity.EntityCobble;
import com.arkcraft.mod.core.entity.EntityDodoEgg;
import com.arkcraft.mod.core.entity.EntityExplosive;
import com.arkcraft.mod.core.entity.EntityStoneSpear;
import com.arkcraft.mod.core.entity.EntityTranqAmmo;
import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.core.entity.model.ModelBrontosaurus;
import com.arkcraft.mod.core.entity.model.ModelDodo;
import com.arkcraft.mod.core.entity.model.ModelRaptor;
import com.arkcraft.mod.core.entity.passive.EntityBrontosaurus;
import com.arkcraft.mod.core.entity.passive.EntityDodo;
import com.arkcraft.mod.core.entity.render.RenderBrontosaurus;
import com.arkcraft.mod.core.entity.render.RenderDodo;
import com.arkcraft.mod.core.entity.render.RenderRaptor;
import com.arkcraft.mod.core.entity.render.RenderStoneSpear;
import com.arkcraft.mod.core.entity.test.EntitySpear;
import com.arkcraft.mod.core.entity.test.RenderSpear;
import com.arkcraft.mod.core.lib.LogHelper;

public class ClientProxy extends CommonProxy {
	
	boolean initDone = false;
	
	@SidedProxy(clientSide="com.arkcraft.mod.core.book.proxy.DClient", serverSide="com.arkcraft.mod.core.book.proxy.DCommon")
	public static DCommon dossierProxy;
	
	@Override
	public void init() {
		if(initDone) return;
		RenderingRegistry.registerEntityRenderingHandler(EntityCobble.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), GlobalAdditions.cobble_ball, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityDodoEgg.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), GlobalAdditions.dodo_egg, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityExplosive.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), GlobalAdditions.explosive_ball, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityRaptor.class, new RenderRaptor(new ModelRaptor(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityDodo.class, new RenderDodo(new ModelDodo(), 0.3F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBrontosaurus.class, new RenderBrontosaurus(new ModelBrontosaurus(), 0.5f));
	//	RenderingRegistry.registerEntityRenderingHandler(EntityTranqAmmo.class, new RenderTranqAmmo());
		RenderingRegistry.registerEntityRenderingHandler(EntityTranqAmmo.class, new RenderSnowball(Minecraft.getMinecraft().getRenderManager(), GlobalAdditions.tranq_ammo, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityStoneSpear.class, new RenderStoneSpear());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpear.class, new RenderSpear());
		ModelBakery.addVariantName(GlobalAdditions.slingshot, "arkcraft:slingshot", "arkcraft:slingshot_pulled");
		dossierProxy.init();
		LogHelper.info("CommonProxy: Init run finished.");
		initDone = true;
	}
	
	/* We register the block/item textures and models here */
	@Override
	public void registerRenderers() {
		for(Map.Entry<String, Block> e : GlobalAdditions.allBlocks.entrySet()) {
			String name = e.getKey();
			Block b = e.getValue();
			registerBlockTexture(b, name);
		}
		
		for(Map.Entry<String, Item> e : GlobalAdditions.allItems.entrySet()) {
			String name = e.getKey();
			Item item = e.getValue();
			registerItemTexture(item, name);
		}
	}
	
	public void registerBlockTexture(final Block block, final String blockName) {
		registerBlockTexture(block, 0, blockName);
	}
	
	public void registerBlockTexture(final Block block, int meta, final String blockName) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(Main.MODID + ":" + blockName, "inventory"));
	}
	
	public void registerItemTexture(final Item item, final String name) {
		registerItemTexture(item, 0, name);
	}
	
	public void registerItemTexture(final Item item, int meta, final String name) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(Main.MODID + ":" + name, "inventory"));
        ModelBakery.addVariantName(item, Main.MODID + ":" + name);
	}
	//public void registerSound() {
	//	MinecraftForge.EVENT_BUS.register(new SoundHandler());
	//}
}
