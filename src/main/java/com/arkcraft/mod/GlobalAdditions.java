package com.arkcraft.mod;

import com.arkcraft.mod.common.ARKCraft;
import com.arkcraft.mod.common.creativetabs.ARKTabs;
import com.arkcraft.mod.common.entity.EntityCobble;
import com.arkcraft.mod.common.entity.EntityDodoEgg;
import com.arkcraft.mod.common.entity.EntityExplosive;
import com.arkcraft.mod.common.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.common.entity.neutral.EntityBrontosaurus;
import com.arkcraft.mod.common.entity.passive.EntityDodo;
import com.arkcraft.mod.common.handlers.EntityHandler;
import com.arkcraft.mod.common.handlers.GuiHandler;
import com.arkcraft.mod.common.handlers.PestleCraftingManager;
import com.arkcraft.mod.common.handlers.PlayerCraftingManager;
import com.arkcraft.mod.common.handlers.RecipeHandler;
import com.arkcraft.mod.common.handlers.SmithyCraftingManager;
import com.arkcraft.mod.common.items.weapons.projectiles.EntitySpear;
import com.arkcraft.mod.common.lib.BALANCE;
import com.arkcraft.mod.common.lib.KeyBindings;
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

/**
 * @author Vastatio
 */
public class GlobalAdditions {

	public static CreativeTabs tabARK = new ARKTabs(CreativeTabs.getNextID(), "tabARKCraft");
	
	public enum GUI {
		SMITHY(0), PESTLE_AND_MORTAR(1), INV_DODO(2), BOOK_GUI(3), CROP_PLOT(4), TAMING_GUI(5), COMPOST_BIN(6), 
		SCOPE(7), PLAYER(8), TAMED_DINO(9);
		int id;
		GUI(int id) {
			this.id = id;
		}
		
		public int getID() { return id; }
	}
	
	public static void init() {

		// Handlers
		RecipeHandler.registerVanillaCraftingRecipes();
		PestleCraftingManager.registerPestleCraftingRecipes();
		SmithyCraftingManager.registerSmithyCraftingRecipes();
		PlayerCraftingManager.registerPlayerCraftingRecipes();
		EntityHandler.registerModEntity(EntityExplosive.class, "Explosive Cobblestone Ball", ARKCraft.instance, 64, 10, true);
		
		EntityHandler.registerModEntity(EntitySpear.class, "Spear", ARKCraft.instance, 64, 10, true);
//		EntityHandler.registerModEntity(EntityTranqArrow.class, "Tranq Arrow", ARKCraft.instance, 64, 10, true);
//		EntityHandler.registerModEntity(EntityStoneArrow.class, "Stone Arrow", ARKCraft.instance, 64, 10, true);
//		EntityHandler.registerModEntity(EntityMetalArrow.class, "Metal Arrow", ARKCraft.instance, 64, 10, true);

		EntityHandler.registerModEntity(EntityCobble.class, "Cobblestone Ball", ARKCraft.instance, 64, 10, true);
		EntityHandler.registerModEntity(EntityDodoEgg.class, "Dodo Egg", ARKCraft.instance, 64, 10, true);
		
		EntityHandler.registerEntityEgg(EntityRaptor.class, "raptor", BiomeGenBase.icePlains);
		EntityHandler.registerEntityEgg(EntityDodo.class, "dodo", BiomeGenBase.beach, BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.birchForest, BiomeGenBase.extremeHills);
		EntityHandler.registerEntityEgg(EntityBrontosaurus.class, "brontosaurus");
		//EntityHandler.registerMonster(EntityCoelacanth.class, "coelacanth", BiomeGenBase.deepOcean, BiomeGenBase.ocean, BiomeGenBase.river);

		KeyBindings.preInit();
		removeTheseMCMobs();
		
		// Other Stuff
		NetworkRegistry.INSTANCE.registerGuiHandler(ARKCraft.instance, new GuiHandler());
	}
	
	// Stuff we don't want that is normally in Minecraft
	private static void removeTheseMCMobs(){
		// Don't spawn the normal Minecraft hostile mobs?
		if (!BALANCE.GEN.mcHostileMobs) {
			for (int i=0; i < BiomeGenBase.getBiomeGenArray().length; i++){
				if (BiomeGenBase.getBiomeGenArray()[i] != null){
					EntityRegistry.removeSpawn(EntityZombie.class, EnumCreatureType.MONSTER, BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntityCreeper.class, EnumCreatureType.MONSTER, BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntitySkeleton.class, EnumCreatureType.MONSTER, BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntitySpider.class, EnumCreatureType.MONSTER, BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntitySilverfish.class, EnumCreatureType.MONSTER, BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntityWitch.class, EnumCreatureType.MONSTER, BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntityEnderman.class, EnumCreatureType.MONSTER, BiomeGenBase.getBiomeGenArray()[i]);
					EntityRegistry.removeSpawn(EntityCaveSpider.class, EnumCreatureType.MONSTER, BiomeGenBase.getBiomeGenArray()[i]);
				}
			}
		}
	}
	
	public static GlobalAdditions getInstance() { return new GlobalAdditions(); }	
}
