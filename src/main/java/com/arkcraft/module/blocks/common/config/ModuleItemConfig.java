package com.arkcraft.module.blocks.common.config;

import com.arkcraft.lib.DefaultBoolean;
import com.arkcraft.lib.DefaultDouble;
import com.arkcraft.lib.DefaultInt;
import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.ARKCraft;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.lang.reflect.Field;

/**
 * @author wildbill22
 */
public class ModuleItemConfig
{
    public static Configuration config;
    public static final String CATEGORY_GENERAL = Configuration.CATEGORY_GENERAL;
    public static final String CATEGORY_BALANCE = "balance";

    /**
     * Loads/refreshes the configuration and adds comments if there aren't any
     * {@link #init(File) init} has to be called once before using this
     */
    private static void loadConfiguration()
    {
        // Load all the Categories (a function for each just below this function)
        balanceCategoryConfiguration();

        if (config.hasChanged())
        {
            LogHelper.info("Configs: Configuration has changed, saving.");
            config.save();
        }
    }

    // Balance settings
    private static void balanceCategoryConfiguration()
    {
        ConfigCategory cat_balance = config.getCategory(CATEGORY_BALANCE);
        cat_balance.setComment("You can adjust these values to change the balancing of this mod");
        loadFields(cat_balance, ModuleItemBalance.PLANTS.class);
        loadFields(cat_balance, ModuleItemBalance.PLAYER.class);
        loadFields(cat_balance, ModuleItemBalance.WEAPONS.class);
        loadFields(cat_balance, ModuleItemBalance.CROP_PLOT.class);
        loadFields(cat_balance, ModuleItemBalance.MORTAR_AND_PESTLE.class);
        loadFields(cat_balance, ModuleItemBalance.PLAYER_CRAFTING.class);
        loadFields(cat_balance, ModuleItemBalance.COMPOST_BIN.class);
        loadFields(cat_balance, ModuleItemBalance.EXPLOSIVE_DEVICE.class);
        // TODO: Add more classes from BALANCE here when available
    }

    public static void init(File configDir)
    {
//	public static void init(File configFile) {
        File configFile = new File(configDir, ARKCraft.MODID + "_mod2.cfg");
        if (config == null)
        {
            config = new Configuration(configFile);
        }
        loadConfiguration();
    }

    /**
     * This method makes variables from a class available in the config file.
     * To use this, the given class can only contain static non-final variables,
     * which should have a '@Default*' annotation containing the default value.
     * Currently only boolean, int, and double are supported
     *
     * @param cat Config Category to put the properties inside
     * @param cls Class to go through
     * @author Maxanier
     */
    @SuppressWarnings("rawtypes")
    private static void loadFields(ConfigCategory cat, Class cls)
    {
        for (Field f : cls.getDeclaredFields())
        {
            String name = f.getName();
            Class type = f.getType();
            try
            {
                if (type == int.class)
                {
                    // Possible exception should not be caught so you can't forget a default value
                    DefaultInt a = f.getAnnotation(DefaultInt.class);
                    f.set(null, config.get(cat.getQualifiedName(), a.name(), a.value(),
                            a.comment(), a.minValue(), a.maxValue()).getInt());
                }
                else if (type == double.class)
                {
                    // Possible exception should not be caught so you can't forget a default value
                    DefaultDouble a = f.getAnnotation(DefaultDouble.class);
                    f.set(null, config.get(cat.getQualifiedName(), a.name(), a.value(),
                            a.comment(), a.minValue(), a.maxValue()).getDouble());
                }
                else if (type == boolean.class)
                {
                    DefaultBoolean a = f.getAnnotation(DefaultBoolean.class);
                    f.set(null, config.get(cat.getQualifiedName(), a.name(), a.value(), a.comment()).getBoolean());
                }
            }
            catch (NullPointerException e1)
            {
                LogHelper.error("Configs: Author probably forgot to specify a default value for " + name + " in " + cls.getCanonicalName() + e1);
                throw new Error("Please check your default values");
            }
            catch (Exception e)
            {
                LogHelper.error("Configs: Can't set " + cls.getName() + " values" + e);
                throw new Error("Please check your DracoAnimus config file");
            }
        }
    }

    @SubscribeEvent
    public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent e)
    {
        if (e.modID.equalsIgnoreCase(ARKCraft.MODID))
        {
            // Resync configs
            LogHelper.info("Configs: Configuration has changed, loading.");
            ModuleItemConfig.loadConfiguration();
        }
    }
}
