package com.arkcraft.module.crafting.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

/**
 * @author wildbill22
 */
@SideOnly(Side.CLIENT)
public class KeyBindings
{
	public static KeyBinding playerPooping, playerCrafting;

	public static void preInit()
	{
		playerPooping = new KeyBinding("key.playerPoop", Keyboard.KEY_Z, "ARKCraft");
		playerCrafting = new KeyBinding("key.playerCraft", Keyboard.KEY_V, "ARKCraft");

		ClientRegistry.registerKeyBinding(playerPooping);
		ClientRegistry.registerKeyBinding(playerCrafting);
	}
}
