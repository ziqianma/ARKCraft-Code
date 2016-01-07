package com.arkcraft.module.item.common.config;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * @author wildbill22
 */
public class KeyBindings
{
    public static KeyBinding playerPooping, attachment, playerCrafting;

    public static void preInit()
    {
        playerPooping = new KeyBinding("key.playerPoop", Keyboard.KEY_Z, "ARKCraft");
        attachment = new KeyBinding("key.attachment", Keyboard.KEY_M, "ARKCraft");
        playerCrafting = new KeyBinding("key.playerCraft", Keyboard.KEY_V, "ARKCraft");

        ClientRegistry.registerKeyBinding(playerPooping);
        ClientRegistry.registerKeyBinding(attachment);
        ClientRegistry.registerKeyBinding(playerCrafting);
    }
}
