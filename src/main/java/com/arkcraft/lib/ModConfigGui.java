package com.arkcraft.lib;

import com.arkcraft.module.core.ARKCraft;
import com.arkcraft.module.core.common.config.CoreConfig;
import com.arkcraft.module.item.common.config.ModuleItemConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wildbill22
 */
public class ModConfigGui extends GuiConfig
{

    // Creates the Mod1 Balance Screen (2nd Level)
    public static class Mod1BalanceEntry extends CategoryEntry
    {
        private static List<IConfigElement> getConfigElements()
        {
            List<IConfigElement> list = new ArrayList<IConfigElement>();
            // Add all the settings from MOD1_BALANCE class
            list.addAll(new ConfigElement(CoreConfig.config.getCategory(CoreConfig.CATEGORY_BALANCE)).getChildElements());
            return list;
        }

        public Mod1BalanceEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
        {
            super(owningScreen, owningEntryList, prop);
        }

        // Build the list of settings from Config.MOD2_BALANCE class
        @Override
        protected GuiScreen buildChildScreen()
        {
            return new GuiConfig(this.owningScreen, getConfigElements(), this.owningScreen.modID, CoreConfig.CATEGORY_BALANCE,
                    false, false, ARKCraft.NAME + " Balance Settings");
        }
    }

    // Creates the Mod2 Balance Screen (2nd Level)
    public static class Mod2BalanceEntry extends CategoryEntry
    {
        private static List<IConfigElement> getConfigElements()
        {
            List<IConfigElement> list = new ArrayList<IConfigElement>();
            // Add all the settings from MOD2_BALANCE class
            list.addAll(new ConfigElement(ModuleItemConfig.config.getCategory(ModuleItemConfig.CATEGORY_BALANCE)).getChildElements());
            return list;
        }

        public Mod2BalanceEntry(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement prop)
        {
            super(owningScreen, owningEntryList, prop);
        }

        // Build the list of settings from Config.MOD2_BALANCE class
        @Override
        protected GuiScreen buildChildScreen()
        {
            return new GuiConfig(this.owningScreen, getConfigElements(), this.owningScreen.modID, ModuleItemConfig.CATEGORY_BALANCE,
                    false, false, ARKCraft.NAME + " Balance Settings");
        }
    }

    // This creates the Initial Screen (1st level)
    // Displays all the entries to the screens created above that extend CategoryEntry
    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
//		list.addAll(new ConfigElement(Config.config.getCategory(Config.CATEGORY_GENERAL)).getChildElements());
        list.add(new DummyConfigElement.DummyCategoryElement("Entity & World Gen Balance", "category.arkcraft.mod1_balance", Mod1BalanceEntry.class));
        list.add(new DummyConfigElement.DummyCategoryElement("Items, Blocks, & Player Balance", "category.arkcraft.mod2_balance", Mod2BalanceEntry.class));
        return list;
    }

    public ModConfigGui(GuiScreen parentScreen)
    {
        super(parentScreen, getConfigElements(), ARKCraft.MODID, true, false, ARKCraft.NAME + " Config");
    }
}
