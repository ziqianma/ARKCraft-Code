package com.arkcraft.module.core;

import com.arkcraft.module.core.common.config.CoreConfig;
import com.arkcraft.module.core.common.event.CoreCommonEventHandler;
import com.arkcraft.module.core.common.gen.WorldGeneratorBushes;
import com.arkcraft.module.core.common.gen.island.WorldTypeIsland;
import com.arkcraft.module.core.common.proxy.CommonProxy;
import com.arkcraft.module.item.common.blocks.ARKCraftBlocks;
import com.arkcraft.module.item.common.config.ModuleItemConfig;
import com.arkcraft.module.item.common.event.ItemsCommonEventHandler;
import com.arkcraft.module.item.common.items.ARKCraftItems;
import com.arkcraft.module.item.common.items.potions.ARKCraftPotionEffects;
import com.arkcraft.module.core.common.network.ARKMessagePipeline;
import com.arkcraft.module.core.common.network.OpenPlayerCrafting;
import com.arkcraft.module.core.common.network.PlayerPoop;
import com.arkcraft.module.core.common.network.UpdateMPToCraftItem;
import com.arkcraft.module.core.common.network.UpdatePlayerCrafting;
import com.arkcraft.module.core.common.network.UpdateSmithyToCraftItem;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

@Mod(modid = ARKCraft.MODID, version = ARKCraft.VERSION, name = ARKCraft.MODID, guiFactory = "com.arkcraft.lib.ModGuiFactory", dependencies = "required-after:llibrary@[0.5.5]")
public class ARKCraft
{
    public static final String MODID = "arkcraft", VERSION = "${version}", NAME = "ARKCraft";

    @SidedProxy(clientSide = "com.arkcraft.module.core.client.proxy.ClientProxy", serverSide = "com.arkcraft.module.core.common.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance("arkcraft")
    public static ARKCraft instance;
    public static SimpleNetworkWrapper modChannel;
    public static Logger modLog;
    public ARKMessagePipeline messagePipeline;

    public static final WorldType island = new WorldTypeIsland();

    public static Map<String, Item> allItems = new HashMap<String, Item>();

    public ARKCraft()
    {
        instance = this;
        messagePipeline = new ARKMessagePipeline();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        // Create the config first, it is used below
//		Config.init(event.getSuggestedConfigurationFile());
        CoreConfig.init(event.getModConfigurationDirectory());
        FMLCommonHandler.instance().bus().register(new CoreConfig());
        ModuleItemConfig.init(event.getModConfigurationDirectory());
        FMLCommonHandler.instance().bus().register(new ModuleItemConfig());

        GameRegistry.registerWorldGenerator(new WorldGeneratorBushes(), 0);

        ARKCraftBlocks.init();
        ARKCraftItems.init();
        ARKCraftPotionEffects.init();
        GlobalAdditions.init();

        Potion[] potionTypes;

        for (Field f : Potion.class.getDeclaredFields())
        {
            f.setAccessible(true);

            try
            {
                if (f.getName().equals("potionTypes") || f.getName().equals("field_76425_a"))
                {
                    Field modfield = Field.class.getDeclaredField("modifiers");
                    modfield.setAccessible(true);
                    modfield.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                    potionTypes = (Potion[]) f.get(null);
                    final Potion[] newPotionTypes = new Potion[256];
                    System.arraycopy(potionTypes, 0, newPotionTypes, 0, potionTypes.length);
                    f.set(null, newPotionTypes);
                }
            }
            catch (Exception e)
            {
                System.err.println("(Potions!) Severe error, please report this to the mod author:");
                System.err.println(e);
            }
        }

        setupNetwork();
        modLog = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        CoreCommonEventHandler coreEventHandler = new CoreCommonEventHandler();
        MinecraftForge.EVENT_BUS.register(coreEventHandler);
        FMLCommonHandler.instance().bus().register(coreEventHandler);

        ItemsCommonEventHandler itemsEventHandler = new ItemsCommonEventHandler();
        MinecraftForge.EVENT_BUS.register(itemsEventHandler);
        FMLCommonHandler.instance().bus().register(itemsEventHandler);

        proxy.registerRenderers();
        proxy.registerWeapons();
        proxy.registerEventHandlers();
        proxy.init();
        messagePipeline.initalize();
        proxy.registerPackets(messagePipeline);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        messagePipeline.postInitialize();
    }

    public static ARKCraft instance() { return instance; }

    private void setupNetwork()
    {
        modChannel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

        int id = 0;
        // The handler (usually in the packet class), the packet class, unique id, side the packet is received on
        modChannel.registerMessage(PlayerPoop.Handler.class, PlayerPoop.class, id++, Side.SERVER);
        modChannel.registerMessage(UpdateMPToCraftItem.Handler.class, UpdateMPToCraftItem.class, id++, Side.SERVER);
        modChannel.registerMessage(UpdateSmithyToCraftItem.Handler.class, UpdateSmithyToCraftItem.class, id++, Side.SERVER);
        modChannel.registerMessage(OpenPlayerCrafting.Handler.class, OpenPlayerCrafting.class, id++, Side.SERVER);
        modChannel.registerMessage(UpdatePlayerCrafting.Handler.class, UpdatePlayerCrafting.class, id++, Side.SERVER);
    }

    public boolean isDebugger()
    {
        return "${version}".equals("${" + "version" + "}");
    }
}
