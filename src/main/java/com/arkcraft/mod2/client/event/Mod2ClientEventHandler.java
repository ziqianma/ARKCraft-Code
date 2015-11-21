package com.arkcraft.mod2.client.event;

import com.arkcraft.mod2.common.items.ARKCraftItems;

import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class Mod2ClientEventHandler
{
//	private Minecraft mc = Minecraft.getMinecraft();

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
}