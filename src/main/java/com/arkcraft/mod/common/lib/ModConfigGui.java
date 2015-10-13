package com.arkcraft.mod.common.lib;

import java.util.ArrayList;
import java.util.List;

import com.arkcraft.mod.common.ARKCraft;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

/***
 * 
 * @author wildbill22
 *
 */
public class ModConfigGui extends GuiConfig {
	
	// Creates the Balance Screen (2nd Level)
	public static class BalanceEntry extends CategoryEntry {
		private static List<IConfigElement> getConfigElements() {
			List<IConfigElement> list = new ArrayList<IConfigElement>();
			// Add all the settings from BALANCE.PLANTS class
			list.addAll(new ConfigElement(Config.config.getCategory(Config.CATEGORY_BALANCE)).getChildElements());
			return list;
		}

		public BalanceEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop) {
			super(owningScreen, owningEntryList, prop);
		}

		// Build the list of settings from Configs.VILLAGE class
		@Override
		protected GuiScreen buildChildScreen() {
			return new GuiConfig(this.owningScreen, getConfigElements(), this.owningScreen.modID, Config.CATEGORY_BALANCE, 
					false, false, ARKCraft.NAME + " Balance Settings");
		}
	}
	
	// This creates the Initial Screen (1st level)
	// Displays all the entries to the screens created above that extend CategoryEntry
	private static List<IConfigElement> getConfigElements() {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
//		list.addAll(new ConfigElement(Config.config.getCategory(Config.CATEGORY_GENERAL)).getChildElements());
		list.add(new DummyConfigElement.DummyCategoryElement("balance", "category.arkcraft.balance", BalanceEntry.class));
		return list;
	}

	public ModConfigGui(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(), ARKCraft.MODID, true, false, ARKCraft.NAME + " Config");
	}
}
