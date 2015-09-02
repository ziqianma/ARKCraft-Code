package com.arkcraft.mod.core.lib;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;

/**
 * 
 * @author wildbill22
 *
 */
public class KeyBindings {
	public static KeyBinding playerPooping;
	
	public static void preInit() {
		playerPooping = new KeyBinding("key.playerPoop", Keyboard.KEY_Z, "key.categories.gameplay");
		
		ClientRegistry.registerKeyBinding(playerPooping);
	}
}
