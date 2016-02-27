package com.arkcraft.module.blocks;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.arkcraft.module.blocks.common.general.ARKContainerBlock;
import com.arkcraft.module.blocks.common.general.BlockARKBase;
import com.arkcraft.module.blocks.common.general.BlockBerryBush;
import com.arkcraft.module.blocks.common.general.BlockCompostBin;
import com.arkcraft.module.blocks.common.general.BlockCropPlot;
import com.arkcraft.module.blocks.common.general.BlockMortarAndPestle;
import com.arkcraft.module.blocks.common.general.BlockRefiningForge;
import com.arkcraft.module.blocks.common.general.BlockSmithy;
import com.arkcraft.module.blocks.common.general.BlockSpikes;
import com.arkcraft.module.blocks.common.itemblock.ItemBerryBush;
import com.arkcraft.module.blocks.common.itemblock.ItemCompostBin;
import com.arkcraft.module.blocks.common.itemblock.ItemCropPlot;
import com.arkcraft.module.blocks.common.itemblock.ItemMortarAndPestle;
import com.arkcraft.module.blocks.common.itemblock.ItemRefiningForge;
import com.arkcraft.module.blocks.common.itemblock.ItemSmithy;
import com.arkcraft.module.blocks.common.tile.TileInventoryCompostBin;
import com.arkcraft.module.blocks.common.tile.TileInventoryCropPlot;
import com.arkcraft.module.blocks.common.tile.TileInventoryForge;
import com.arkcraft.module.blocks.common.tile.TileInventoryMP;
import com.arkcraft.module.blocks.common.tile.TileInventorySmithy;
import com.arkcraft.module.core.GlobalAdditions;
import com.arkcraft.module.core.common.handlers.GenerationHandler;
import com.arkcraft.module.weapon.common.tileentity.TileFlashlight;

/**
 * @author wildbill22
 */
public class ARKCraftBlocks
{
	public static BlockBerryBush berryBush;
	public static BlockARKBase oreSurface;
	public static Block blockNarcoBerry;
	public static BlockSpikes wooden_spikes;
	public static BlockCompostBin compost_bin;
	public static BlockSmithy smithy;
	public static BlockMortarAndPestle pestle;
	public static BlockCropPlot crop_plot;
	public static BlockRefiningForge refining_forge;

	// public static BlockImprovisedExplosiveDevice block_explosive_device;
	// public static BlockExplosiveDeviceWire block_explosive_wire;

	public static ARKCraftBlocks getInstance()
	{
		return new ARKCraftBlocks();
	}

	public static Map<String, Block> allBlocks = new HashMap<String, Block>();

	public static Map<String, Block> getAllBlocks()
	{
		return allBlocks;
	}

	public static void init()
	{
		// world generated
		berryBush = (BlockBerryBush) registerBlockWithItemBlock(new BlockBerryBush(0.4F),
				ItemBerryBush.class, "berryBush");
		GenerationHandler.addOreToGen(oreSurface, 0); // Sets to the values in
														// BALENCE.GEN.class

		// block_explosive_device =
		// (BlockImprovisedExplosiveDevice)registerBlock(new
		// BlockImprovisedExplosiveDevice(null), "block_explosive_device");
		// block_explosive_wire = (BlockExplosiveDeviceWire)registerBlock(new
		// BlockExplosiveDeviceWire(), "block_explosive_wire");

		// Blocks
		oreSurface = (BlockARKBase) registerBlock(new BlockARKBase(Material.rock, 3.0F),
				"oreSurface");

		// blockNarcoBrerry = addBlock(Material.ground, "narcoBerryBlock",
		// 3.0F);
		blockNarcoBerry = getRegisteredBlock("blockNarcoBerry");

		// block_explosive_device = (BlockImprovisedExplosiveDevice)
		// registerBlockNoTab(new BlockImprovisedExplosiveDevice(),
		// "block_explosive_device");
		wooden_spikes = (BlockSpikes) registerBlock(new BlockSpikes(Material.wood, 3.0F),
				"wooden_spikes");

		// Containers
		smithy = registerSmithy("smithy", Material.wood, GlobalAdditions.GUI.SMITHY.getID(), false,
				false, 3);
		pestle = registerMortarAndPestle("mortar_and_pestle", Material.rock,
				GlobalAdditions.GUI.PESTLE_AND_MORTAR.getID(), false, false, 3);
		crop_plot = registerCropPlot("crop_plot", Material.wood,
				GlobalAdditions.GUI.CROP_PLOT.getID(), false, 3);
		compost_bin = registerCompostBin("compost_bin", Material.wood,
				GlobalAdditions.GUI.COMPOST_BIN.getID(), false, false, 3);
		refining_forge = registerRefiningForge("refining_forge", Material.rock, false,
				GlobalAdditions.GUI.FORGE_GUI.getID(), false, false, 3);

		// Tile Entities
		GameRegistry.registerTileEntity(TileInventoryCropPlot.class, "TileInventoryCropPlot");
		GameRegistry.registerTileEntity(TileInventoryMP.class, "TileInventoryMP");
		GameRegistry.registerTileEntity(TileInventoryCompostBin.class, "TileEntityCompostBin");
		GameRegistry.registerTileEntity(TileInventorySmithy.class, "TileInventorySmithy");
		GameRegistry.registerTileEntity(TileInventoryForge.class, "TileInventoryForge");
		GameRegistry.registerTileEntity(TileFlashlight.class, "TileFlashlight");

	}

	private static Block registerBlock(Block block, String name)
	{
		block.setCreativeTab(GlobalAdditions.tabARKMisc);
		return registerBlockNoTab(block, name);
	}

	private static Block registerBlockNoTab(Block block, String name)
	{
		GameRegistry.registerBlock(block, name);
		block.setUnlocalizedName(name);
		allBlocks.put(name, block);
		return block;
	}

	private static Block registerBlockWithItemBlock(Block block, Class<? extends ItemBlock> itemBlock, String name)
	{
		block.setCreativeTab(GlobalAdditions.tabARKMisc);
		GameRegistry.registerBlock(block, itemBlock, name);
		block.setUnlocalizedName(name);
		allBlocks.put(name, block);
		return block;
	}

	protected static Block getRegisteredBlock(String name)
	{
		return (Block) Block.blockRegistry.getObject(new ResourceLocation(name));
	}

	protected static ARKContainerBlock addContainer(String name, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType)
	{
		ARKContainerBlock container = new ARKContainerBlock(mat, ID);
		container.setRenderAsNormalBlock(renderAsNormalBlock);
		container.setOpaque(isOpaque);
		container.setRenderType(renderType);
		registerBlock(container, name);
		return container;
	}

	protected static BlockSmithy registerSmithy(String name, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType)
	{
		BlockSmithy container = new BlockSmithy(mat, ID);
		registerBlockWithItemBlock(container, ItemSmithy.class, name);
		return container;
	}

	protected static BlockMortarAndPestle registerMortarAndPestle(String name, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType)
	{
		BlockMortarAndPestle container = new BlockMortarAndPestle(mat, ID);
		registerBlockWithItemBlock(container, ItemMortarAndPestle.class, name);
		return container;
	}

	protected static BlockRefiningForge registerRefiningForge(String name, Material mat, boolean isBurning, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType)
	{
		BlockRefiningForge container = new BlockRefiningForge(mat, ID);
		registerBlockWithItemBlock(container, ItemRefiningForge.class, name);
		return container;
	}

	protected static BlockCropPlot registerCropPlot(String name, Material mat, int ID, boolean isOpaque, int renderType)
	{
		BlockCropPlot container = new BlockCropPlot(mat, ID);
		registerBlockWithItemBlock(container, ItemCropPlot.class, name);
		return container;
	}

	protected static BlockCompostBin registerCompostBin(String name, Material mat, int ID, boolean renderAsNormalBlock, boolean isOpaque, int renderType)
	{
		BlockCompostBin container = new BlockCompostBin(mat, ID);
		registerBlockWithItemBlock(container, ItemCompostBin.class, name);
		return container;
	}
}
