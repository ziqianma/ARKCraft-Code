package com.arkcraft.mod.client.event;

import com.arkcraft.mod.client.gui.GuiMainMenuOverride;
import com.arkcraft.mod.client.sound.SoundARKTheme;
import com.arkcraft.mod.common.items.ARKCraftItems;
import net.ilexiconn.llibrary.client.gui.GuiLLibraryMainMenu;
import net.ilexiconn.llibrary.common.color.EnumChatColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
	private Minecraft mc = Minecraft.getMinecraft();
	private ISound music;

	@SubscribeEvent
	public void clientTick(TickEvent.ClientTickEvent event)
	{
		if (mc.currentScreen instanceof GuiMainMenuOverride && !mc.getSoundHandler().isSoundPlaying(music))
		{
			music = new SoundARKTheme();
			mc.getSoundHandler().playSound(music);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void addTooltip(ItemTooltipEvent event)
	{
		Item item = event.itemStack.getItem();
		String tooltipLang = item.getUnlocalizedName() + ".tooltip";
		String translated = StatCollector.translateToLocal(tooltipLang);

		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && ARKCraftItems.allItems.containsValue(item) && tooltipLang != translated)
		{
			String[] lines = translated.split("NEXT");

			for (String line : lines)
			{
				for (EnumChatFormatting format : EnumChatFormatting.values())
				{
					if (!format.isFancyStyling())
					{
						line = handleColourFormat(line, format, format.getColorIndex() + "");
					}
				}

				line = handleColourFormat(line, EnumChatFormatting.ITALIC, "o");
				line = handleColourFormat(line, EnumChatFormatting.OBFUSCATED, "k");
				line = handleColourFormat(line, EnumChatFormatting.BOLD, "l");
				line = handleColourFormat(line, EnumChatFormatting.STRIKETHROUGH, "m");
				line = handleColourFormat(line, EnumChatFormatting.UNDERLINE, "n");
				line = handleColourFormat(line, EnumChatFormatting.RESET, "r");

				event.toolTip.add(1, line);
			}
		}
	}

	private String handleColourFormat(String line, EnumChatFormatting format, String colourIndex)
	{
		String value = "%" + colourIndex;

		if (line.contains(value))
        {
            line = line.replaceAll(value, "");
            line = format + line;
        }

		return line;
	}

	/*
	@SubscribeEvent
	public void onFOVUpdateEvent(FOVUpdateEvent e)
	{
		if (e.entity.isUsingItem() && e.entity.getItemInUse().getItem() instanceof IItemWeapon)
		{
			RangedComponent rc = ((IItemWeapon) e.entity.getItemInUse().getItem()).getRangedComponent();

			if (rc != null && RangedComponent.isReadyToFire(e.entity.getItemInUse()))
			{
				e.newfov = e.fov * rc.getFOVMultiplier(e.entity.getItemInUseDuration());
			}
		}
	}	*/

	@SubscribeEvent
	public void guiOpen(GuiOpenEvent event)
	{
		if (event.gui instanceof GuiMainMenu || event.gui instanceof GuiLLibraryMainMenu || event.gui instanceof GuiMainMenuOverride)
		{
			event.gui = new GuiMainMenuOverride();
		}
	}

	@SubscribeEvent
	public void playSound(PlaySoundEvent event)
	{
		if (!(event.name.contains("ark_theme")) && mc.currentScreen instanceof GuiMainMenuOverride)
		{
			event.result = null;
		}
	}
}