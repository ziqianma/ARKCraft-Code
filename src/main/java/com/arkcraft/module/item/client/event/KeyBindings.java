package com.arkcraft.module.item.client.event;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

import com.arkcraft.module.core.ARKCraft;

/**
 * @author wildbill22
 */
@SideOnly(Side.CLIENT)
public class KeyBindings
{
	public static KeyBinding playerPooping, attachment, playerCrafting, reload;

	public static void preInit()
	{
		playerPooping = new KeyBinding("key.playerPoop", Keyboard.KEY_Z, "ARKCraft");
		attachment = new KeyBinding("key.attachment", Keyboard.KEY_M, "ARKCraft");
		playerCrafting = new KeyBinding("key.playerCraft", Keyboard.KEY_V, "ARKCraft");
		reload = new KeyBinding("key.arkcraft.reload", Keyboard.KEY_R, ARKCraft.NAME);

		ClientRegistry.registerKeyBinding(playerPooping);
		ClientRegistry.registerKeyBinding(attachment);
		ClientRegistry.registerKeyBinding(playerCrafting);
		ClientRegistry.registerKeyBinding(reload);
	}
}
