package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class WorldSettings
{
    /** The seed for the map. */
    private final long seed;
    /** The EnumGameType. */
    private final WorldSettings.GameType theGameType;
    /** Switch for the map features. 'true' for enabled, 'false' for disabled. */
    private final boolean mapFeaturesEnabled;
    /** True if hardcore mode is enabled */
    private final boolean hardcoreEnabled;
    private final WorldType terrainType;
    /** True if Commands (cheats) are allowed. */
    private boolean commandsAllowed;
    /** True if the Bonus Chest is enabled. */
    private boolean bonusChestEnabled;
    private String worldName;
    private static final String __OBFID = "CL_00000147";

    public WorldSettings(long seedIn, WorldSettings.GameType gameType, boolean enableMapFeatures, boolean hardcoreMode, WorldType worldTypeIn)
    {
        this.worldName = "";
        this.seed = seedIn;
        this.theGameType = gameType;
        this.mapFeaturesEnabled = enableMapFeatures;
        this.hardcoreEnabled = hardcoreMode;
        this.terrainType = worldTypeIn;
    }

    public WorldSettings(WorldInfo info)
    {
        this(info.getSeed(), info.getGameType(), info.isMapFeaturesEnabled(), info.isHardcoreModeEnabled(), info.getTerrainType());
    }

    /**
     * Enables the bonus chest.
     */
    public WorldSettings enableBonusChest()
    {
        this.bonusChestEnabled = true;
        return this;
    }

    public WorldSettings setWorldName(String name)
    {
        this.worldName = name;
        return this;
    }

    /**
     * Enables Commands (cheats).
     */
    @SideOnly(Side.CLIENT)
    public WorldSettings enableCommands()
    {
        this.commandsAllowed = true;
        return this;
    }

    /**
     * Returns true if the Bonus Chest is enabled.
     */
    public boolean isBonusChestEnabled()
    {
        return this.bonusChestEnabled;
    }

    /**
     * Returns the seed for the world.
     */
    public long getSeed()
    {
        return this.seed;
    }

    /**
     * Gets the game type.
     */
    public WorldSettings.GameType getGameType()
    {
        return this.theGameType;
    }

    /**
     * Returns true if hardcore mode is enabled, otherwise false
     */
    public boolean getHardcoreEnabled()
    {
        return this.hardcoreEnabled;
    }

    /**
     * Get whether the map features (e.g. strongholds) generation is enabled or disabled.
     */
    public boolean isMapFeaturesEnabled()
    {
        return this.mapFeaturesEnabled;
    }

    public WorldType getTerrainType()
    {
        return this.terrainType;
    }

    /**
     * Returns true if Commands (cheats) are allowed.
     */
    public boolean areCommandsAllowed()
    {
        return this.commandsAllowed;
    }

    /**
     * Gets the GameType by ID
     */
    public static WorldSettings.GameType getGameTypeById(int id)
    {
        return WorldSettings.GameType.getByID(id);
    }

    public String getWorldName()
    {
        return this.worldName;
    }

    public static enum GameType
    {
        NOT_SET(-1, ""),
        SURVIVAL(0, "survival"),
        CREATIVE(1, "creative"),
        ADVENTURE(2, "adventure"),
        SPECTATOR(3, "spectator");
        int id;
        String name;

        private static final String __OBFID = "CL_00000148";

        private GameType(int typeId, String nameIn)
        {
            this.id = typeId;
            this.name = nameIn;
        }

        /**
         * Returns the ID of this game type
         */
        public int getID()
        {
            return this.id;
        }

        /**
         * Returns the name of this game type
         */
        public String getName()
        {
            return this.name;
        }

        /**
         * Configures the player capabilities based on the game type
         */
        public void configurePlayerCapabilities(PlayerCapabilities capabilities)
        {
            if (this == CREATIVE)
            {
                capabilities.allowFlying = true;
                capabilities.isCreativeMode = true;
                capabilities.disableDamage = true;
            }
            else if (this == SPECTATOR)
            {
                capabilities.allowFlying = true;
                capabilities.isCreativeMode = false;
                capabilities.disableDamage = true;
                capabilities.isFlying = true;
            }
            else
            {
                capabilities.allowFlying = false;
                capabilities.isCreativeMode = false;
                capabilities.disableDamage = false;
                capabilities.isFlying = false;
            }

            capabilities.allowEdit = !this.isAdventure();
        }

        /**
         * Returns true if this is the ADVENTURE game type
         */
        public boolean isAdventure()
        {
            return this == ADVENTURE || this == SPECTATOR;
        }

        /**
         * Returns true if this is the CREATIVE game type
         */
        public boolean isCreative()
        {
            return this == CREATIVE;
        }

        /**
         * Returns true if this is the SURVIVAL or ADVENTURE game type
         */
        public boolean isSurvivalOrAdventure()
        {
            return this == SURVIVAL || this == ADVENTURE;
        }

        /**
         * Returns the game type with the specified ID, or SURVIVAL if none found. Args: id
         */
        public static WorldSettings.GameType getByID(int idIn)
        {
            WorldSettings.GameType[] agametype = values();
            int j = agametype.length;

            for (int k = 0; k < j; ++k)
            {
                WorldSettings.GameType gametype = agametype[k];

                if (gametype.id == idIn)
                {
                    return gametype;
                }
            }

            return SURVIVAL;
        }

        /**
         * Returns the game type with the specified name, or SURVIVAL if none found. This is case sensitive. Args: name
         */
        @SideOnly(Side.CLIENT)
        public static WorldSettings.GameType getByName(String p_77142_0_)
        {
            WorldSettings.GameType[] agametype = values();
            int i = agametype.length;

            for (int j = 0; j < i; ++j)
            {
                WorldSettings.GameType gametype = agametype[j];

                if (gametype.name.equals(p_77142_0_))
                {
                    return gametype;
                }
            }

            return SURVIVAL;
        }
    }
}