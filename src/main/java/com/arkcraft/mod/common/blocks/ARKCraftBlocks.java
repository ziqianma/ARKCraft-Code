package com.arkcraft.mod.common.blocks;

import com.arkcraft.mod.GlobalAdditions;
import com.arkcraft.mod.common.handlers.GenerationHandler;
import com.arkcraft.mod.common.tile.TileInventoryCompostBin;
import com.arkcraft.mod.common.tile.TileInventoryCropPlot;
import com.arkcraft.mod.common.tile.TileInventoryMP;
import com.arkcraft.mod.common.tile.TileInventorySmithy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wildbill22
 */
public class ARKCraftBlocks
{
	public static ARKBush berryBush;
	public static ARKBlock oreSurface;
	public static Block blockNarcoBrerry;
	public static ARKBlockSpikes wooden_spikes;
	public static BlockInventoryCompostBin compost_bin;
	public static BlockInventorySmithy smithy;	
	public static BlockInventoryMP pestle;
	public static BlockInventoryCropPlot crop_plot;

	public static ARKCraftBlocks getInstance() { return new ARKCraftBlocks(); }
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
		smithy = addSmithyContainer("smithy", 0.4F, Material.wood, GlobalAdditions.GUI.SMITHY.getID(), false, false, 3);
		pestle = addMPContainer("mortar_and_pestle", 0.4F, Material.rock, GlobalAdditions.GUI.PESTLE_AND_MORTAR.getID(), false, false, 3);
		crop_plot = addCropPlotContainer("crop_plot", 0.4F, Material.wood, GlobalAdditions.GUI.CROP_PLOT.getID(), false, false, 3);
		compost_bin = addCompostBinContainer("compost_bin", 0.4F, Material.wood, GlobalAdditions.GUI.COMPOST_BIN.getID(), false, false, 3);

		// Tile Entities
		GameRegistry.registerTileEntity(TileInventoryCropPlot.class, "TileInventoryCropPlot");
		GameRegistry.registerTileEntity(TileInventoryMP.class, "TileInventoryMP");
		GameRegistry.registerTileEntity(TileInventoryCompostBin.class, "TileEntityCompostBin");
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
	//	container.setRenderAsNormalBlock(renderAsNormalBlock);
		container.setOpaque(isOpaque);
		container.setRenderType(renderType);
		allBlocks.put(name, container);
		return container;
	}
	
	protected static BlockInventoryCompostBin addCompostBinContainer(String name, float hardness, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType) {
		BlockInventoryCompostBin container = new BlockInventoryCompostBin(name, hardness, mat, ID);
		container.setRenderAsNormalBlock(renderAsNormalBlock);
		container.setOpaque(isOpaque);
		container.setRenderType(renderType);
		allBlocks.put(name, container);
		return container;
	}
}
