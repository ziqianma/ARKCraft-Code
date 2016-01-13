package com.arkcraft.module.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import com.arkcraft.module.core.common.config.CoreBalance;
import com.arkcraft.module.core.common.creativetabs.ARKTabs;
import com.arkcraft.module.core.common.entity.aggressive.EntityRaptor;
import com.arkcraft.module.core.common.entity.aggressive.EntitySabertooth;
import com.arkcraft.module.core.common.entity.neutral.EntityBrontosaurus;
import com.arkcraft.module.core.common.entity.passive.EntityDodo;
import com.arkcraft.module.core.common.handlers.EntityHandler;
import com.arkcraft.module.core.common.handlers.GuiHandler;
import com.arkcraft.module.item.common.entity.EntityCobble;
import com.arkcraft.module.item.common.entity.EntityDodoEgg;
import com.arkcraft.module.item.common.entity.item.projectiles.EntityGrenade;
import com.arkcraft.module.item.common.entity.item.projectiles.EntitySpear;
import com.arkcraft.module.item.common.handlers.PestleCraftingManager;
import com.arkcraft.module.item.common.handlers.PlayerCraftingManager;
import com.arkcraft.module.item.common.handlers.RecipeHandler;
import com.arkcraft.module.item.common.handlers.SmithyCraftingManager;

/**
 * @author Vastatio
 */
public class GlobalAdditions
{
	public static CreativeTabs tabARK = new ARKTabs(CreativeTabs.getNextID(), "tabARKCraft");

	public enum GUI
	{
		SMITHY(0),
		PESTLE_AND_MORTAR(1),
		INV_DODO(2),
		BOOK_GUI(3),
		CROP_PLOT(4),
		TAMING_GUI(5),
		COMPOST_BIN(6),
		SCOPE(7),
		PLAYER(8),
		TAMED_DINO(9),
		FORGE_GUI(10),
		ATTACHMENT_GUI(11);
		int id;

		GUI(int id)
		{
			this.id = id;
		}

		public int getID()
		{
			return id;
		}
	}

	public static void init()
	{
		// Handlers
		RecipeHandler.registerVanillaCraftingRecipes();
		PestleCraftingManager.registerPestleCraftingRecipes();
		SmithyCraftingManager.registerSmithyCraftingRecipes();
		PlayerCraftingManager.registerPlayerCraftingRecipes();

		EntityHandler
				.registerModEntity(EntitySpear.class, "Spear", ARKCraft.instance, 64, 10, true);
		// EntityHandler.registerModEntity(EntityTranqArrow.class,
		// "Tranq Arrow", ARKCraft.instance, 64, 10, true);
		// EntityHandler.registerModEntity(EntityStoneArrow.class,
		// "Stone Arrow", ARKCraft.instance, 64, 10, true);
		// EntityHandler.registerModEntity(EntityMetalArrow.class,
		// "Metal Arrow", ARKCraft.instance, 64, 10, true);

		EntityHandler.registerModEntity(EntityCobble.class, "Cobblestone Ball", ARKCraft.instance,
				64, 10, true);
		EntityHandler.registerModEntity(EntityDodoEgg.class, "Dodo Egg", ARKCraft.instance, 64, 10,
				true);
		EntityHandler.registerModEntity(EntityGrenade.class, "Grenade", ARKCraft.instance, 64, 10,
				true);

		EntityHandler.registerEntityEgg(EntityRaptor.class, ARKCraft.MODID + ".raptor",
				BiomeGenBase.icePlains);
		EntityHandler.registerEntityEgg(EntitySabertooth.class, ARKCraft.MODID + ".saber",
				BiomeGenBase.icePlains);
		EntityHandler.registerEntityEgg(EntityDodo.class, ARKCraft.MODID + ".dodo",
				BiomeGenBase.beach, BiomeGenBase.desert, BiomeGenBase.forest,
				BiomeGenBase.birchForest, BiomeGenBase.extremeHills);
		EntityHandler.registerEntityEgg(EntityBrontosaurus.class, ARKCraft.MODID + ".brontosaurus");
		// EntityHandler.registerMonster(EntityCoelacanth.class, "coelacanth",
		// BiomeGenBase.deepOcean, BiomeGenBase.ocean, BiomeGenBase.river);
		removeTheseMCMobs();

		// Other Stuff
		NetworkRegistry.INSTANCE.registerGuiHandler(ARKCraft.instance, new GuiHandler());
	}

	// Stuff we don't want that is normally in Minecraft
	private static void removeTheseMCMobs()
	{
		// Don't spawn the normal Minecraft hostile mobs?
		if (!CoreBalance.GEN.mcHostileMobs)
		{
			for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++)
			{
				if (BiomeGenBase.getBiomeGenArray()[i] != null)
				{
					EntityRegistry.removeSpawn(EntityZombie.class, EnumCreatureType.MONSTER,
							BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntityCreeper.class, EnumCreatureType.MONSTER,
							BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntitySkeleton.class, EnumCreatureType.MONSTER,
							BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntitySpider.class, EnumCreatureType.MONSTER,
							BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntitySilverfish.class, EnumCreatureType.MONSTER,
							BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntityWitch.class, EnumCreatureType.MONSTER,
							BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntityEnderman.class, EnumCreatureType.MONSTER,
							BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntityCaveSpider.class, EnumCreatureType.MONSTER,
							BiomeGenBase.getBiomeGenArray()[i]);
				}
			}
		}
	}

	public static GlobalAdditions getInstance()
	{
		return new GlobalAdditions();
	}
}
