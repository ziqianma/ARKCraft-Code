package com.arkcraft.mod.core.items;

import com.arkcraft.mod.core.Main;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class SoundHandler {
	public static void onEntityPlay(String name, World world, Entity entityName, float volume, float pitch) {
		world.playSoundAtEntity(entityName, (Main.MODID + ":" + name), (float) volume, (float) pitch);
	}
	
}