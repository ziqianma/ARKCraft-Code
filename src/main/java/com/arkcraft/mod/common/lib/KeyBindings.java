package com.arkcraft.mod.common.lib;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * 
 * @author wildbill22
 *
 */
public class KeyBindings {
	public static KeyBinding playerPooping, playerScoping;
	
	public static void preInit() {
		playerPooping = new KeyBinding("key.playerPoop", Keyboard.KEY_Z, "ARKCraft");
		playerScoping = new KeyBinding("key.playerScope", Keyboard.KEY_SCROLL, "ARKCraft");
		
		ClientRegistry.registerKeyBinding(playerPooping);
		ClientRegistry.registerKeyBinding(playerScoping);
	}
}
