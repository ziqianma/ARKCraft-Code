package com.arkcraft.mod.core.handler;

import java.util.Random;

import com.arkcraft.mod.core.Main;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.registry.EntityRegistry;


@SuppressWarnings({ "rawtypes", "unchecked" })
public class EntityHandler {
	
	public EntityHandler() {}
	public static void registerMonster(Class eClass, String name) {
		int entityID = EntityRegistry.findGlobalUniqueEntityId();
		Random rand = new Random(name.hashCode());
		int mainColor = rand.nextInt() * 16777215;
		int secondColor = rand.nextInt() * 16777215;
		
		EntityRegistry.registerGlobalEntityID(eClass, name, entityID);
		EntityRegistry.addSpawn(eClass, 50, 2, 4, EnumCreatureType.CREATURE, BiomeGenBase.beach, BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.birchForest, BiomeGenBase.extremeHills);
		EntityRegistry.registerModEntity(eClass, name, entityID, Main.instance(), 64, 1, true);
		EntityList.entityEggs.put(Integer.valueOf(entityID), new EntityList.EntityEggInfo(entityID, mainColor, secondColor));
	}
	
	public static void registerModEntity(Class<? extends Entity> eClass, String name, int entityID, Object mainClass, int trackRange, int updateFreq, boolean sVU) {
		EntityRegistry.registerModEntity(eClass, name, entityID, mainClass, trackRange, updateFreq, sVU);
	}
	
}
