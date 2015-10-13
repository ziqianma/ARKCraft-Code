package com.arkcraft.mod.core.blocks;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.mod.core.GlobalAdditions.GUI;
import com.arkcraft.mod.core.blocks.ARKBlock;
import com.arkcraft.mod.core.blocks.ARKBlockSpikes;
import com.arkcraft.mod.core.blocks.ARKBush;
import com.arkcraft.mod.core.blocks.ARKContainerBlock;
import com.arkcraft.mod.core.blocks.BlockCompostBin;
import com.arkcraft.mod.core.blocks.TileEntityCompostBin;
import com.arkcraft.mod.core.handlers.GenerationHandler;

/**
 * @author wildbill22
 */
public class ModBlocks {

	public static ARKBush berryBush;
	public static ARKBlock oreSurface;
	public static Block blockNarcoBrerry;
	public static ARKBlockSpikes wooden_spikes;
	public static BlockCompostBin compost_bin;
	public static BlockInventorySmithy smithy;	
	public static BlockInventoryMP pestle;
	public static BlockInventoryCropPlot crop_plot;

	public static ModBlocks getInstance() { return new ModBlocks(); }
	public static Map<String, Block> allBlocks = new HashMap<String, Block>();
	public static Map<String, Block> getAllBlocks() { return allBlocks; }

	public static void init() {
		// world generated
		berryBush = addBush("berryBush", 0.4F);
		GenerationHandler.addOreToGen(oreSurface, 0); //Sets to the values in BALENCE.GEN.class
		
		// Blocks
		oreSurface = addBlock(Material.rock, "oreSurface", 3.0F);
		
		//blockNarcoBrerry = addBlock(Material.ground, "narcoBerryBlock", 3.0F);
		blockNarcoBrerry = getRegisteredBlock("blockNarcoBerry");
		wooden_spikes = addSpikes(Material.wood, "wooden_spikes", 3.0F);

		// Containers
		smithy = addSmithyContainer("smithy", 0.4F, Material.wood, GUI.SMITHY.getID(), false, false, 3);
		pestle = addMPContainer("mortar_and_pestle", 0.4F, Material.rock, GUI.PESTLE_AND_MORTAR.getID(), false, false, 3);
		crop_plot = addCropPlotContainer("crop_plot", 0.4F, Material.wood, GUI.CROP_PLOT.getID(), false, false, 3);

		// Tile Entities
		GameRegistry.registerTileEntity(TileInventoryCropPlot.class, "TileInventoryCropPlot");
		GameRegistry.registerTileEntity(TileInventoryMP.class, "TileInventoryMP");
		GameRegistry.registerTileEntity(TileEntityCompostBin.class, "TileEntityCompostBin");		
		GameRegistry.registerTileEntity(TileInventorySmithy.class, "TileInventorySmithy");
	}
		
	protected static Block getRegisteredBlock(String name){
		return (Block)Block.blockRegistry.getObject(new ResourceLocation(name));
	}
	
	protected static ARKBush addBush(String name, float hardness) {
		ARKBush b = new ARKBush(name, hardness);
		allBlocks.put(name, b);
		return b;
	}
	
	protected static ARKBlock addBlock(Material m, String name, float hardness) {
		ARKBlock b = new ARKBlock(m, name, hardness);
		allBlocks.put(name, b);
		return b;
	}
	
	protected static ARKBlockSpikes addSpikes(Material m, String name, float hardness) {
		ARKBlockSpikes b = new ARKBlockSpikes(m, name, hardness);
		allBlocks.put(name, b);
		return b;
	}
	
	protected static ARKContainerBlock addContainer(String name, float hardness, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType) {
		ARKContainerBlock container = new ARKContainerBlock(name, hardness, mat, ID);
		container.setRenderAsNormalBlock(renderAsNormalBlock);
		container.setOpaque(isOpaque);
		container.setRenderType(renderType);
		allBlocks.put(name, container);
		return container;
	}
	
	protected static BlockInventorySmithy addSmithyContainer(String name, float hardness, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType) {
		BlockInventorySmithy container = new BlockInventorySmithy(name, hardness, mat, ID);
		container.setRenderAsNormalBlock(renderAsNormalBlock);
		container.setOpaque(isOpaque);
		container.setRenderType(renderType);
		allBlocks.put(name, container);
		return container;
	}
	
	protected static BlockInventoryMP addMPContainer(String name, float hardness, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType) {
		BlockInventoryMP container = new BlockInventoryMP(name, hardness, mat, ID);
		container.setRenderAsNormalBlock(renderAsNormalBlock);
		container.setOpaque(isOpaque);
		container.setRenderType(renderType);
		allBlocks.put(name, container);
		return container;
	}
	
	protected static BlockInventoryCropPlot addCropPlotContainer(String name, float hardness, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType) {
		BlockInventoryCropPlot container = new BlockInventoryCropPlot(name, hardness, mat, ID);
		container.setRenderAsNormalBlock(renderAsNormalBlock);
		container.setOpaque(isOpaque);
		container.setRenderType(renderType);
		allBlocks.put(name, container);
		return container;
	}
	
	protected static BlockCompostBin addCompostBinContainer(String name, float hardness, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType) {
		BlockCompostBin container = new BlockCompostBin(name, hardness, mat, ID);
		container.setRenderAsNormalBlock(renderAsNormalBlock);
		container.setOpaque(isOpaque);
		container.setRenderType(renderType);
		allBlocks.put(name, container);
		return container;
	}
}
