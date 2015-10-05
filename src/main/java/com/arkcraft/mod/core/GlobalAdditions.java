package com.arkcraft.mod.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import com.arkcraft.mod.core.creativetabs.ARKTabs;
import com.arkcraft.mod.core.entity.EntityCobble;
import com.arkcraft.mod.core.entity.EntityDodoEgg;
import com.arkcraft.mod.core.entity.EntityExplosive;
import com.arkcraft.mod.core.entity.aggressive.EntityRaptor;
import com.arkcraft.mod.core.entity.passive.EntityBrontosaurus;
import com.arkcraft.mod.core.entity.passive.EntityDodo;
import com.arkcraft.mod.core.handlers.EntityHandler;
import com.arkcraft.mod.core.handlers.GuiHandler;
import com.arkcraft.mod.core.handlers.RecipeHandler;
import com.arkcraft.mod.core.items.weapons.projectiles.EntityMetalArrow;
import com.arkcraft.mod.core.items.weapons.projectiles.EntityRocketPropelledGrenade;
import com.arkcraft.mod.core.items.weapons.projectiles.EntitySimpleBullet;
import com.arkcraft.mod.core.items.weapons.projectiles.EntitySimpleRifleAmmo;
import com.arkcraft.mod.core.items.weapons.projectiles.EntitySimpleShotgunAmmo;
import com.arkcraft.mod.core.items.weapons.projectiles.EntitySpear;
import com.arkcraft.mod.core.items.weapons.projectiles.EntityStoneArrow;
import com.arkcraft.mod.core.items.weapons.projectiles.EntityTranqArrow;
import com.arkcraft.mod.core.items.weapons.projectiles.EntityTranquilizer;
import com.arkcraft.mod.core.lib.BALANCE;
import com.arkcraft.mod.core.lib.KeyBindings;

/**
 * @author Vastatio
 */
public class GlobalAdditions {

	public static CreativeTabs tabARK = new ARKTabs(CreativeTabs.getNextID(), "tabARKCraft");
	
	public enum GUI {
		SMITHY(0), PESTLE_AND_MORTAR(1), INV_DODO(2), BOOK_GUI(3), CROP_PLOT(4), TAMING_GUI(5), COMPOST_BIN(6);
		int id;
		GUI(int id) {
			this.id = id;
		}
		
		public int getID() { return id; }
	}
	
	public static void init() {

		// Handlers
		RecipeHandler.registerVanillaCraftingRecipes();
		RecipeHandler.registerPestleCraftingRecipes();
		RecipeHandler.registerSmithyCraftingRecipes();
		EntityHandler.registerModEntity(EntityExplosive.class, "Explosive Cobblestone Ball", Main.instance, 64, 10, true);
		
		EntityHandler.registerModEntity(EntitySpear.class, "Spear", Main.instance, 64, 10, true);
//		EntityHandler.registerModEntity(EntityTranqArrow.class, "Tranq Arrow", Main.instance, 64, 10, true);
//		EntityHandler.registerModEntity(EntityStoneArrow.class, "Stone Arrow", Main.instance, 64, 10, true);
//		EntityHandler.registerModEntity(EntityMetalArrow.class, "Metal Arrow", Main.instance, 64, 10, true);

		EntityHandler.registerModEntity(EntityCobble.class, "Cobblestone Ball", Main.instance, 64, 10, true);
		EntityHandler.registerModEntity(EntityDodoEgg.class, "Dodo Egg", Main.instance, 64, 10, true);
		
		EntityHandler.registerEntityEgg(EntityRaptor.class, "raptor");
		EntityHandler.registerEntityEgg(EntityDodo.class, "dodo", BiomeGenBase.beach, BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.birchForest, BiomeGenBase.extremeHills);
		EntityHandler.registerEntityEgg(EntityBrontosaurus.class, "brontosaurus");
		//EntityHandler.registerMonster(EntityCoelacanth.class, "coelacanth", BiomeGenBase.deepOcean, BiomeGenBase.ocean, BiomeGenBase.river);
		
		registerWeaponEntities();

		KeyBindings.preInit();
		
		// Other Stuff
		NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance, new GuiHandler());
	}
	
	public static void registerWeaponEntities(){
		if (BALANCE.WEAPONS.SIMPLE_PISTOL){
			EntityHandler.registerModEntity(EntitySimpleBullet.class, "Simple Bullet", Main.instance, 64, 10, true);
		}
		if (BALANCE.WEAPONS.SHOTGUN){
			EntityHandler.registerModEntity(EntitySimpleShotgunAmmo.class, "Simple Shotgun Ammo", Main.instance, 64, 10, true);		
		}
		if (BALANCE.WEAPONS.LONGNECK_RIFLE)	{
			EntityHandler.registerModEntity(EntitySimpleRifleAmmo.class, "Simple Rifle Ammo", Main.instance, 64, 10, true);
		}
		if (BALANCE.WEAPONS.TRANQ_GUN)	{
			EntityHandler.registerModEntity(EntityTranquilizer.class, "Tranquilizer", Main.instance, 64, 10, true);
		}
		if (BALANCE.WEAPONS.ROCKET_LAUNCHER)	{
			EntityHandler.registerModEntity(EntityRocketPropelledGrenade.class, "Rocket Propelled Grenade", Main.instance, 64, 10, true);
		}
		if (BALANCE.WEAPONS.CROSSBOW)	{
			EntityHandler.registerModEntity(EntityTranqArrow.class, "Tranq Arrow", Main.instance, 64, 10, true);
			EntityHandler.registerModEntity(EntityStoneArrow.class, "Stone Arrow", Main.instance, 64, 10, true);
			EntityHandler.registerModEntity(EntityMetalArrow.class, "Metal Arrow", Main.instance, 64, 10, true);
		}
	}
	
	public static GlobalAdditions getInstance() { return new GlobalAdditions(); }	
}
