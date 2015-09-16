package net.minecraft.client.network;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.GuardianSound;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiScreenDemo;
import net.minecraft.client.gui.GuiScreenRealmsProxy;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.IProgressMeter;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityPickupFX;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.client.player.inventory.LocalBlockIntercommunication;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.stream.MetadataAchievement;
import net.minecraft.client.stream.MetadataCombat;
import net.minecraft.client.stream.MetadataPlayerDeath;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.NpcMerchant;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.inventory.AnimalChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S00PacketKeepAlive;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S33PacketUpdateSign;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S40PacketDisconnect;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S46PacketSetCompressionLevel;
import net.minecraft.network.play.server.S47PacketPlayerListHeaderFooter;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.potion.PotionEffect;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.Explosion;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class NetHandlerPlayClient implements INetHandlerPlayClient
{
    private static final Logger logger = LogManager.getLogger();
    /**
     * The NetworkManager instance used to communicate with the server (used only by handlePlayerPosLook to update
     * positioning and handleJoinGame to inform the server of the client distribution/mods)
     */
    private final NetworkManager netManager;
    private final GameProfile profile;
    /**
     * Seems to be either null (integrated server) or an instance of either GuiMultiplayer (when connecting to a server)
     * or GuiScreenReamlsTOS (when connecting to MCO server)
     */
    private final GuiScreen guiScreenServer;
    /** Reference to the Minecraft instance, which many handler methods operate on */
    private Minecraft gameController;
    /** Reference to the current ClientWorld instance, which many handler methods operate on */
    private WorldClient clientWorldController;
    /**
     * True if the client has finished downloading terrain and may spawn. Set upon receipt of S08PacketPlayerPosLook,
     * reset upon respawning
     */
    private boolean doneLoadingTerrain;
    /** A mapping from player names to their respective GuiPlayerInfo (specifies the clients response time to the server) */
    private final Map playerInfoMap = Maps.newHashMap();
    public int currentServerMaxPlayers = 20;
    private boolean field_147308_k = false;
    /**
     * Just an ordinary random number generator, used to randomize audio pitch of item/orb pickup and randomize both
     * particlespawn offset and velocity
     */
    private final Random avRandomizer = new Random();
    private static final String __OBFID = "CL_00000878";

    public NetHandlerPlayClient(Minecraft mcIn, GuiScreen p_i46300_2_, NetworkManager p_i46300_3_, GameProfile p_i46300_4_)
    {
        this.gameController = mcIn;
        this.guiScreenServer = p_i46300_2_;
        this.netManager = p_i46300_3_;
        this.profile = p_i46300_4_;
    }

    /**
     * Clears the WorldClient instance associated with this NetHandlerPlayClient
     */
    public void cleanup()
    {
        this.clientWorldController = null;
    }

    /**
     * Registers some server properties (gametype,hardcore-mode,terraintype,difficulty,player limit), creates a new
     * WorldClient and sets the player initial dimension
     */
    public void handleJoinGame(S01PacketJoinGame packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.playerController = new PlayerControllerMP(this.gameController, this);
        this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.getGameType(), false, packetIn.isHardcoreMode(), packetIn.getWorldType()), net.minecraftforge.fml.common.network.handshake.NetworkDispatcher.get(getNetworkManager()).getOverrideDimension(packetIn), packetIn.getDifficulty(), this.gameController.mcProfiler);
        this.gameController.gameSettings.difficulty = packetIn.getDifficulty();
        this.gameController.loadWorld(this.clientWorldController);
        this.gameController.thePlayer.dimension = packetIn.getDimension();
        this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
        this.gameController.thePlayer.setEntityId(packetIn.getEntityId());
        this.currentServerMaxPlayers = packetIn.getMaxPlayers();
        this.gameController.thePlayer.setReducedDebug(packetIn.isReducedDebugInfo());
        this.gameController.playerController.setGameType(packetIn.getGameType());
        this.gameController.gameSettings.sendSettingsToServer();
        this.netManager.sendPacket(new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));
    }

    /**
     * Spawns an instance of the objecttype indicated by the packet and sets its position and momentum
     */
    public void handleSpawnObject(S0EPacketSpawnObject packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        double d0 = (double)packetIn.func_148997_d() / 32.0D;
        double d1 = (double)packetIn.func_148998_e() / 32.0D;
        double d2 = (double)packetIn.func_148994_f() / 32.0D;
        Object object = null;

        if (packetIn.func_148993_l() == 10)
        {
            object = EntityMinecart.func_180458_a(this.clientWorldController, d0, d1, d2, EntityMinecart.EnumMinecartType.byNetworkID(packetIn.func_149009_m()));
        }
        else if (packetIn.func_148993_l() == 90)
        {
            Entity entity = this.clientWorldController.getEntityByID(packetIn.func_149009_m());

            if (entity instanceof EntityPlayer)
            {
                object = new EntityFishHook(this.clientWorldController, d0, d1, d2, (EntityPlayer)entity);
            }

            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 60)
        {
            object = new EntityArrow(this.clientWorldController, d0, d1, d2);
        }
        else if (packetIn.func_148993_l() == 61)
        {
            object = new EntitySnowball(this.clientWorldController, d0, d1, d2);
        }
        else if (packetIn.func_148993_l() == 71)
        {
            object = new EntityItemFrame(this.clientWorldController, new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1), MathHelper.floor_double(d2)), EnumFacing.getHorizontal(packetIn.func_149009_m()));
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 77)
        {
            object = new EntityLeashKnot(this.clientWorldController, new BlockPos(MathHelper.floor_double(d0), MathHelper.floor_double(d1), MathHelper.floor_double(d2)));
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 65)
        {
            object = new EntityEnderPearl(this.clientWorldController, d0, d1, d2);
        }
        else if (packetIn.func_148993_l() == 72)
        {
            object = new EntityEnderEye(this.clientWorldController, d0, d1, d2);
        }
        else if (packetIn.func_148993_l() == 76)
        {
            object = new EntityFireworkRocket(this.clientWorldController, d0, d1, d2, (ItemStack)null);
        }
        else if (packetIn.func_148993_l() == 63)
        {
            object = new EntityLargeFireball(this.clientWorldController, d0, d1, d2, (double)packetIn.func_149010_g() / 8000.0D, (double)packetIn.func_149004_h() / 8000.0D, (double)packetIn.func_148999_i() / 8000.0D);
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 64)
        {
            object = new EntitySmallFireball(this.clientWorldController, d0, d1, d2, (double)packetIn.func_149010_g() / 8000.0D, (double)packetIn.func_149004_h() / 8000.0D, (double)packetIn.func_148999_i() / 8000.0D);
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 66)
        {
            object = new EntityWitherSkull(this.clientWorldController, d0, d1, d2, (double)packetIn.func_149010_g() / 8000.0D, (double)packetIn.func_149004_h() / 8000.0D, (double)packetIn.func_148999_i() / 8000.0D);
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 62)
        {
            object = new EntityEgg(this.clientWorldController, d0, d1, d2);
        }
        else if (packetIn.func_148993_l() == 73)
        {
            object = new EntityPotion(this.clientWorldController, d0, d1, d2, packetIn.func_149009_m());
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 75)
        {
            object = new EntityExpBottle(this.clientWorldController, d0, d1, d2);
            packetIn.func_149002_g(0);
        }
        else if (packetIn.func_148993_l() == 1)
        {
            object = new EntityBoat(this.clientWorldController, d0, d1, d2);
        }
        else if (packetIn.func_148993_l() == 50)
        {
            object = new EntityTNTPrimed(this.clientWorldController, d0, d1, d2, (EntityLivingBase)null);
        }
        else if (packetIn.func_148993_l() == 78)
        {
            object = new EntityArmorStand(this.clientWorldController, d0, d1, d2);
        }
        else if (packetIn.func_148993_l() == 51)
        {
            object = new EntityEnderCrystal(this.clientWorldController, d0, d1, d2);
        }
        else if (packetIn.func_148993_l() == 2)
        {
            object = new EntityItem(this.clientWorldController, d0, d1, d2);
        }
        else if (packetIn.func_148993_l() == 70)
        {
            object = new EntityFallingBlock(this.clientWorldController, d0, d1, d2, Block.getStateById(packetIn.func_149009_m() & 65535));
            packetIn.func_149002_g(0);
        }

        if (object != null)
        {
            ((Entity)object).serverPosX = packetIn.func_148997_d();
            ((Entity)object).serverPosY = packetIn.func_148998_e();
            ((Entity)object).serverPosZ = packetIn.func_148994_f();
            ((Entity)object).rotationPitch = (float)(packetIn.func_149008_j() * 360) / 256.0F;
            ((Entity)object).rotationYaw = (float)(packetIn.func_149006_k() * 360) / 256.0F;
            Entity[] aentity = ((Entity)object).getParts();

            if (aentity != null)
            {
                int i = packetIn.func_149001_c() - ((Entity)object).getEntityId();

                for (int j = 0; j < aentity.length; ++j)
                {
                    aentity[j].setEntityId(aentity[j].getEntityId() + i);
                }
            }

            ((Entity)object).setEntityId(packetIn.func_149001_c());
            this.clientWorldController.addEntityToWorld(packetIn.func_149001_c(), (Entity)object);

            if (packetIn.func_149009_m() > 0)
            {
                if (packetIn.func_148993_l() == 60)
                {
                    Entity entity1 = this.clientWorldController.getEntityByID(packetIn.func_149009_m());

                    if (entity1 instanceof EntityLivingBase && object instanceof EntityArrow)
                    {
                        ((EntityArrow)object).shootingEntity = entity1;
                    }
                }

                ((Entity)object).setVelocity((double)packetIn.func_149010_g() / 8000.0D, (double)packetIn.func_149004_h() / 8000.0D, (double)packetIn.func_148999_i() / 8000.0D);
            }
        }
    }

    /**
     * Spawns an experience orb and sets its value (amount of XP)
     */
    public void handleSpawnExperienceOrb(S11PacketSpawnExperienceOrb packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityXPOrb entityxporb = new EntityXPOrb(this.clientWorldController, (double)packetIn.func_148984_d() / 32.0D, (double)packetIn.func_148983_e() / 32.0D, (double)packetIn.func_148982_f() / 32.0D, packetIn.func_148986_g());
        // FORGE: BugFix MC-12013 Wrong XP orb clientside spawn position
        entityxporb.serverPosX = packetIn.func_148984_d();
        entityxporb.serverPosY = packetIn.func_148983_e();
        entityxporb.serverPosZ = packetIn.func_148982_f();
        entityxporb.rotationYaw = 0.0F;
        entityxporb.rotationPitch = 0.0F;
        entityxporb.setEntityId(packetIn.func_148985_c());
        this.clientWorldController.addEntityToWorld(packetIn.func_148985_c(), entityxporb);
    }

    /**
     * Handles globally visible entities. Used in vanilla for lightning bolts
     */
    public void handleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        double d0 = (double)packetIn.func_149051_d() / 32.0D;
        double d1 = (double)packetIn.func_149050_e() / 32.0D;
        double d2 = (double)packetIn.func_149049_f() / 32.0D;
        EntityLightningBolt entitylightningbolt = null;

        if (packetIn.func_149053_g() == 1)
        {
            entitylightningbolt = new EntityLightningBolt(this.clientWorldController, d0, d1, d2);
        }

        if (entitylightningbolt != null)
        {
            entitylightningbolt.serverPosX = packetIn.func_149051_d();
            entitylightningbolt.serverPosY = packetIn.func_149050_e();
            entitylightningbolt.serverPosZ = packetIn.func_149049_f();
            entitylightningbolt.rotationYaw = 0.0F;
            entitylightningbolt.rotationPitch = 0.0F;
            entitylightningbolt.setEntityId(packetIn.func_149052_c());
            this.clientWorldController.addWeatherEffect(entitylightningbolt);
        }
    }

    /**
     * Handles the spawning of a painting object
     */
    public void handleSpawnPainting(S10PacketSpawnPainting packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPainting entitypainting = new EntityPainting(this.clientWorldController, packetIn.func_179837_b(), packetIn.func_179836_c(), packetIn.func_148961_h());
        this.clientWorldController.addEntityToWorld(packetIn.func_148965_c(), entitypainting);
    }

    /**
     * Sets the velocity of the specified entity to the specified value
     */
    public void handleEntityVelocity(S12PacketEntityVelocity packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.func_149412_c());

        if (entity != null)
        {
            entity.setVelocity((double)packetIn.func_149411_d() / 8000.0D, (double)packetIn.func_149410_e() / 8000.0D, (double)packetIn.func_149409_f() / 8000.0D);
        }
    }

    /**
     * Invoked when the server registers new proximate objects in your watchlist or when objects in your watchlist have
     * changed -> Registers any changes locally
     */
    public void handleEntityMetadata(S1CPacketEntityMetadata packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.func_149375_d());

        if (entity != null && packetIn.func_149376_c() != null)
        {
            entity.getDataWatcher().updateWatchedObjectsFromList(packetIn.func_149376_c());
        }
    }

    /**
     * Handles the creation of a nearby player entity, sets the position and held item
     */
    public void handleSpawnPlayer(S0CPacketSpawnPlayer packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        double d0 = (double)packetIn.func_148942_f() / 32.0D;
        double d1 = (double)packetIn.func_148949_g() / 32.0D;
        double d2 = (double)packetIn.func_148946_h() / 32.0D;
        float f = (float)(packetIn.func_148941_i() * 360) / 256.0F;
        float f1 = (float)(packetIn.func_148945_j() * 360) / 256.0F;
        EntityOtherPlayerMP entityotherplayermp = new EntityOtherPlayerMP(this.gameController.theWorld, this.getPlayerInfo(packetIn.func_179819_c()).getGameProfile());
        entityotherplayermp.prevPosX = entityotherplayermp.lastTickPosX = (double)(entityotherplayermp.serverPosX = packetIn.func_148942_f());
        entityotherplayermp.prevPosY = entityotherplayermp.lastTickPosY = (double)(entityotherplayermp.serverPosY = packetIn.func_148949_g());
        entityotherplayermp.prevPosZ = entityotherplayermp.lastTickPosZ = (double)(entityotherplayermp.serverPosZ = packetIn.func_148946_h());
        int i = packetIn.func_148947_k();

        if (i == 0)
        {
            entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = null;
        }
        else
        {
            entityotherplayermp.inventory.mainInventory[entityotherplayermp.inventory.currentItem] = new ItemStack(Item.getItemById(i), 1, 0);
        }

        entityotherplayermp.setPositionAndRotation(d0, d1, d2, f, f1);
        this.clientWorldController.addEntityToWorld(packetIn.func_148943_d(), entityotherplayermp);
        List list = packetIn.func_148944_c();

        if (list != null)
        {
            entityotherplayermp.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    /**
     * Updates an entity's position and rotation as specified by the packet
     */
    public void handleEntityTeleport(S18PacketEntityTeleport packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.func_149451_c());

        if (entity != null)
        {
            entity.serverPosX = packetIn.func_149449_d();
            entity.serverPosY = packetIn.func_149448_e();
            entity.serverPosZ = packetIn.func_149446_f();
            double d0 = (double)entity.serverPosX / 32.0D;
            double d1 = (double)entity.serverPosY / 32.0D + 0.015625D;
            double d2 = (double)entity.serverPosZ / 32.0D;
            float f = (float)(packetIn.func_149450_g() * 360) / 256.0F;
            float f1 = (float)(packetIn.func_149447_h() * 360) / 256.0F;

            if (Math.abs(entity.posX - d0) < 0.03125D && Math.abs(entity.posY - d1) < 0.015625D && Math.abs(entity.posZ - d2) < 0.03125D)
            {
                entity.func_180426_a(entity.posX, entity.posY, entity.posZ, f, f1, 3, true);
            }
            else
            {
                entity.func_180426_a(d0, d1, d2, f, f1, 3, true);
            }

            entity.onGround = packetIn.func_179697_g();
        }
    }

    /**
     * Updates which hotbar slot of the player is currently selected
     */
    public void handleHeldItemChange(S09PacketHeldItemChange packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);

        if (packetIn.func_149385_c() >= 0 && packetIn.func_149385_c() < InventoryPlayer.getHotbarSize())
        {
            this.gameController.thePlayer.inventory.currentItem = packetIn.func_149385_c();
        }
    }

    /**
     * Updates the specified entity's position by the specified relative moment and absolute rotation. Note that
     * subclassing of the packet allows for the specification of a subset of this data (e.g. only rel. position, abs.
     * rotation or both).
     */
    public void handleEntityMovement(S14PacketEntity packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = packetIn.func_149065_a(this.clientWorldController);

        if (entity != null)
        {
            entity.serverPosX += packetIn.func_149062_c();
            entity.serverPosY += packetIn.func_149061_d();
            entity.serverPosZ += packetIn.func_149064_e();
            double d0 = (double)entity.serverPosX / 32.0D;
            double d1 = (double)entity.serverPosY / 32.0D;
            double d2 = (double)entity.serverPosZ / 32.0D;
            float f = packetIn.func_149060_h() ? (float)(packetIn.func_149066_f() * 360) / 256.0F : entity.rotationYaw;
            float f1 = packetIn.func_149060_h() ? (float)(packetIn.func_149063_g() * 360) / 256.0F : entity.rotationPitch;
            entity.func_180426_a(d0, d1, d2, f, f1, 3, false);
            entity.onGround = packetIn.func_179742_g();
        }
    }

    /**
     * Updates the direction in which the specified entity is looking, normally this head rotation is independent of the
     * rotation of the entity itself
     */
    public void handleEntityHeadLook(S19PacketEntityHeadLook packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = packetIn.func_149381_a(this.clientWorldController);

        if (entity != null)
        {
            float f = (float)(packetIn.func_149380_c() * 360) / 256.0F;
            entity.setRotationYawHead(f);
        }
    }

    /**
     * Locally eliminates the entities. Invoked by the server when the items are in fact destroyed, or the player is no
     * longer registered as required to monitor them. The latter  happens when distance between the player and item
     * increases beyond a certain treshold (typically the viewing distance)
     */
    public void handleDestroyEntities(S13PacketDestroyEntities packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);

        for (int i = 0; i < packetIn.func_149098_c().length; ++i)
        {
            this.clientWorldController.removeEntityFromWorld(packetIn.func_149098_c()[i]);
        }
    }

    /**
     * Handles changes in player positioning and rotation such as when travelling to a new dimension, (re)spawning,
     * mounting horses etc. Seems to immediately reply to the server with the clients post-processing perspective on the
     * player positioning
     */
    public void handlePlayerPosLook(S08PacketPlayerPosLook packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayersp = this.gameController.thePlayer;
        double d0 = packetIn.func_148932_c();
        double d1 = packetIn.func_148928_d();
        double d2 = packetIn.func_148933_e();
        float f = packetIn.func_148931_f();
        float f1 = packetIn.func_148930_g();

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X))
        {
            d0 += entityplayersp.posX;
        }
        else
        {
            entityplayersp.motionX = 0.0D;
        }

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y))
        {
            d1 += entityplayersp.posY;
        }
        else
        {
            entityplayersp.motionY = 0.0D;
        }

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Z))
        {
            d2 += entityplayersp.posZ;
        }
        else
        {
            entityplayersp.motionZ = 0.0D;
        }

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.X_ROT))
        {
            f1 += entityplayersp.rotationPitch;
        }

        if (packetIn.func_179834_f().contains(S08PacketPlayerPosLook.EnumFlags.Y_ROT))
        {
            f += entityplayersp.rotationYaw;
        }

        entityplayersp.setPositionAndRotation(d0, d1, d2, f, f1);
        this.netManager.sendPacket(new C03PacketPlayer.C06PacketPlayerPosLook(entityplayersp.posX, entityplayersp.getEntityBoundingBox().minY, entityplayersp.posZ, entityplayersp.rotationYaw, entityplayersp.rotationPitch, false));

        if (!this.doneLoadingTerrain)
        {
            this.gameController.thePlayer.prevPosX = this.gameController.thePlayer.posX;
            this.gameController.thePlayer.prevPosY = this.gameController.thePlayer.posY;
            this.gameController.thePlayer.prevPosZ = this.gameController.thePlayer.posZ;
            this.doneLoadingTerrain = true;
            this.gameController.displayGuiScreen((GuiScreen)null);
        }
    }

    /**
     * Received from the servers PlayerManager if between 1 and 64 blocks in a chunk are changed. If only one block
     * requires an update, the server sends S23PacketBlockChange and if 64 or more blocks are changed, the server sends
     * S21PacketChunkData
     */
    public void handleMultiBlockChange(S22PacketMultiBlockChange packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        S22PacketMultiBlockChange.BlockUpdateData[] ablockupdatedata = packetIn.func_179844_a();
        int i = ablockupdatedata.length;

        for (int j = 0; j < i; ++j)
        {
            S22PacketMultiBlockChange.BlockUpdateData blockupdatedata = ablockupdatedata[j];
            this.clientWorldController.func_180503_b(blockupdatedata.func_180090_a(), blockupdatedata.func_180088_c());
        }
    }

    /**
     * Updates the specified chunk with the supplied data, marks it for re-rendering and lighting recalculation
     */
    public void handleChunkData(S21PacketChunkData packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);

        if (packetIn.func_149274_i())
        {
            if (packetIn.func_149276_g() == 0)
            {
                this.clientWorldController.doPreChunk(packetIn.func_149273_e(), packetIn.func_149271_f(), false);
                return;
            }

            this.clientWorldController.doPreChunk(packetIn.func_149273_e(), packetIn.func_149271_f(), true);
        }

        this.clientWorldController.invalidateBlockReceiveRegion(packetIn.func_149273_e() << 4, 0, packetIn.func_149271_f() << 4, (packetIn.func_149273_e() << 4) + 15, 256, (packetIn.func_149271_f() << 4) + 15);
        Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(packetIn.func_149273_e(), packetIn.func_149271_f());
        chunk.fillChunk(packetIn.func_149272_d(), packetIn.func_149276_g(), packetIn.func_149274_i());
        this.clientWorldController.markBlockRangeForRenderUpdate(packetIn.func_149273_e() << 4, 0, packetIn.func_149271_f() << 4, (packetIn.func_149273_e() << 4) + 15, 256, (packetIn.func_149271_f() << 4) + 15);

        if (!packetIn.func_149274_i() || !(this.clientWorldController.provider instanceof WorldProviderSurface))
        {
            chunk.resetRelightChecks();
        }
    }

    /**
     * Updates the block and metadata and generates a blockupdate (and notify the clients)
     */
    public void handleBlockChange(S23PacketBlockChange packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.clientWorldController.func_180503_b(packetIn.func_179827_b(), packetIn.func_180728_a());
    }

    /**
     * Closes the network channel
     */
    public void handleDisconnect(S40PacketDisconnect packetIn)
    {
        this.netManager.closeChannel(packetIn.func_149165_c());
    }

    /**
     * Invoked when disconnecting, the parameter is a ChatComponent describing the reason for termination
     */
    public void onDisconnect(IChatComponent reason)
    {
        this.gameController.loadWorld((WorldClient)null);

        if (this.guiScreenServer != null)
        {
            if (this.guiScreenServer instanceof GuiScreenRealmsProxy)
            {
                this.gameController.displayGuiScreen((new DisconnectedRealmsScreen(((GuiScreenRealmsProxy)this.guiScreenServer).func_154321_a(), "disconnect.lost", reason)).getProxy());
            }
            else
            {
                this.gameController.displayGuiScreen(new GuiDisconnected(this.guiScreenServer, "disconnect.lost", reason));
            }
        }
        else
        {
            this.gameController.displayGuiScreen(new GuiDisconnected(new GuiMultiplayer(new GuiMainMenu()), "disconnect.lost", reason));
        }
    }

    public void addToSendQueue(Packet p_147297_1_)
    {
        this.netManager.sendPacket(p_147297_1_);
    }

    public void handleCollectItem(S0DPacketCollectItem packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.func_149354_c());
        Object object = (EntityLivingBase)this.clientWorldController.getEntityByID(packetIn.func_149353_d());

        if (object == null)
        {
            object = this.gameController.thePlayer;
        }

        if (entity != null)
        {
            if (entity instanceof EntityXPOrb)
            {
                this.clientWorldController.playSoundAtEntity(entity, "random.orb", 0.2F, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }
            else
            {
                this.clientWorldController.playSoundAtEntity(entity, "random.pop", 0.2F, ((this.avRandomizer.nextFloat() - this.avRandomizer.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            }

            this.gameController.effectRenderer.addEffect(new EntityPickupFX(this.clientWorldController, entity, (Entity)object, 0.5F));
            this.clientWorldController.removeEntityFromWorld(packetIn.func_149354_c());
        }
    }

    /**
     * Prints a chatmessage in the chat GUI
     */
    public void handleChat(S02PacketChat packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        net.minecraft.util.IChatComponent message = net.minecraftforge.event.ForgeEventFactory.onClientChat(packetIn.func_179841_c(), packetIn.func_148915_c());
        if (message == null) return;

        if (packetIn.func_179841_c() == 2)
        {
            this.gameController.ingameGUI.func_175188_a(message, false);
        }
        else
        {
            this.gameController.ingameGUI.getChatGUI().printChatMessage(message);
        }
    }

    /**
     * Renders a specified animation: Waking up a player, a living entity swinging its currently held item, being hurt
     * or receiving a critical hit by normal or magical means
     */
    public void handleAnimation(S0BPacketAnimation packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.func_148978_c());

        if (entity != null)
        {
            if (packetIn.func_148977_d() == 0)
            {
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                entitylivingbase.swingItem();
            }
            else if (packetIn.func_148977_d() == 1)
            {
                entity.performHurtAnimation();
            }
            else if (packetIn.func_148977_d() == 2)
            {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                entityplayer.wakeUpPlayer(false, false, false);
            }
            else if (packetIn.func_148977_d() == 4)
            {
                this.gameController.effectRenderer.func_178926_a(entity, EnumParticleTypes.CRIT);
            }
            else if (packetIn.func_148977_d() == 5)
            {
                this.gameController.effectRenderer.func_178926_a(entity, EnumParticleTypes.CRIT_MAGIC);
            }
        }
    }

    /**
     * Retrieves the player identified by the packet, puts him to sleep if possible (and flags whether all players are
     * asleep)
     */
    public void handleUseBed(S0APacketUseBed packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        packetIn.getPlayer(this.clientWorldController).trySleep(packetIn.func_179798_a());
    }

    /**
     * Spawns the mob entity at the specified location, with the specified rotation, momentum and type. Updates the
     * entities Datawatchers with the entity metadata specified in the packet
     */
    public void handleSpawnMob(S0FPacketSpawnMob packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        double d0 = (double)packetIn.func_149023_f() / 32.0D;
        double d1 = (double)packetIn.func_149034_g() / 32.0D;
        double d2 = (double)packetIn.func_149029_h() / 32.0D;
        float f = (float)(packetIn.func_149028_l() * 360) / 256.0F;
        float f1 = (float)(packetIn.func_149030_m() * 360) / 256.0F;
        EntityLivingBase entitylivingbase = (EntityLivingBase)EntityList.createEntityByID(packetIn.func_149025_e(), this.gameController.theWorld);
        if (entitylivingbase == null)
        {
            net.minecraftforge.fml.common.FMLLog.info("Server attempted to spawn an unknown entity using ID: {0} at ({1}, {2}, {3}) Skipping!", packetIn.func_149025_e(), d0, d1, d2);
            return;
        }
        entitylivingbase.serverPosX = packetIn.func_149023_f();
        entitylivingbase.serverPosY = packetIn.func_149034_g();
        entitylivingbase.serverPosZ = packetIn.func_149029_h();
        entitylivingbase.rotationYawHead = (float)(packetIn.func_149032_n() * 360) / 256.0F;
        Entity[] aentity = entitylivingbase.getParts();

        if (aentity != null)
        {
            int i = packetIn.func_149024_d() - entitylivingbase.getEntityId();

            for (int j = 0; j < aentity.length; ++j)
            {
                aentity[j].setEntityId(aentity[j].getEntityId() + i);
            }
        }

        entitylivingbase.setEntityId(packetIn.func_149024_d());
        entitylivingbase.setPositionAndRotation(d0, d1, d2, f, f1);
        entitylivingbase.motionX = (double)((float)packetIn.func_149026_i() / 8000.0F);
        entitylivingbase.motionY = (double)((float)packetIn.func_149033_j() / 8000.0F);
        entitylivingbase.motionZ = (double)((float)packetIn.func_149031_k() / 8000.0F);
        this.clientWorldController.addEntityToWorld(packetIn.func_149024_d(), entitylivingbase);
        List list = packetIn.func_149027_c();

        if (list != null)
        {
            entitylivingbase.getDataWatcher().updateWatchedObjectsFromList(list);
        }
    }

    public void handleTimeUpdate(S03PacketTimeUpdate packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.theWorld.setTotalWorldTime(packetIn.func_149366_c());
        this.gameController.theWorld.setWorldTime(packetIn.func_149365_d());
    }

    public void handleSpawnPosition(S05PacketSpawnPosition packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.thePlayer.setSpawnPoint(packetIn.func_179800_a(), true);
        this.gameController.theWorld.getWorldInfo().setSpawn(packetIn.func_179800_a());
    }

    public void handleEntityAttach(S1BPacketEntityAttach packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Object object = this.clientWorldController.getEntityByID(packetIn.func_149403_d());
        Entity entity = this.clientWorldController.getEntityByID(packetIn.func_149402_e());

        if (packetIn.func_149404_c() == 0)
        {
            boolean flag = false;

            if (packetIn.func_149403_d() == this.gameController.thePlayer.getEntityId())
            {
                object = this.gameController.thePlayer;

                if (entity instanceof EntityBoat)
                {
                    ((EntityBoat)entity).setIsBoatEmpty(false);
                }

                flag = ((Entity)object).ridingEntity == null && entity != null;
            }
            else if (entity instanceof EntityBoat)
            {
                ((EntityBoat)entity).setIsBoatEmpty(true);
            }

            if (object == null)
            {
                return;
            }

            ((Entity)object).mountEntity(entity);

            if (flag)
            {
                GameSettings gamesettings = this.gameController.gameSettings;
                this.gameController.ingameGUI.setRecordPlaying(I18n.format("mount.onboard", new Object[] {GameSettings.getKeyDisplayString(gamesettings.keyBindSneak.getKeyCode())}), false);
            }
        }
        else if (packetIn.func_149404_c() == 1 && object instanceof EntityLiving)
        {
            if (entity != null)
            {
                ((EntityLiving)object).setLeashedToEntity(entity, false);
            }
            else
            {
                ((EntityLiving)object).clearLeashed(false, false);
            }
        }
    }

    /**
     * Invokes the entities' handleUpdateHealth method which is implemented in LivingBase (hurt/death),
     * MinecartMobSpawner (spawn delay), FireworkRocket & MinecartTNT (explosion), IronGolem (throwing,...), Witch
     * (spawn particles), Zombie (villager transformation), Animal (breeding mode particles), Horse (breeding/smoke
     * particles), Sheep (...), Tameable (...), Villager (particles for breeding mode, angry and happy), Wolf (...)
     */
    public void handleEntityStatus(S19PacketEntityStatus packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = packetIn.func_149161_a(this.clientWorldController);

        if (entity != null)
        {
            if (packetIn.func_149160_c() == 21)
            {
                this.gameController.getSoundHandler().playSound(new GuardianSound((EntityGuardian)entity));
            }
            else
            {
                entity.handleHealthUpdate(packetIn.func_149160_c());
            }
        }
    }

    public void handleUpdateHealth(S06PacketUpdateHealth packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.thePlayer.setPlayerSPHealth(packetIn.getHealth());
        this.gameController.thePlayer.getFoodStats().setFoodLevel(packetIn.getFoodLevel());
        this.gameController.thePlayer.getFoodStats().setFoodSaturationLevel(packetIn.getSaturationLevel());
    }

    public void handleSetExperience(S1FPacketSetExperience packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.thePlayer.setXPStats(packetIn.func_149397_c(), packetIn.func_149396_d(), packetIn.func_149395_e());
    }

    public void handleRespawn(S07PacketRespawn packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);

        if (packetIn.func_149082_c() != this.gameController.thePlayer.dimension)
        {
            this.doneLoadingTerrain = false;
            Scoreboard scoreboard = this.clientWorldController.getScoreboard();
            this.clientWorldController = new WorldClient(this, new WorldSettings(0L, packetIn.func_149083_e(), false, this.gameController.theWorld.getWorldInfo().isHardcoreModeEnabled(), packetIn.func_149080_f()), packetIn.func_149082_c(), packetIn.func_149081_d(), this.gameController.mcProfiler);
            this.clientWorldController.setWorldScoreboard(scoreboard);
            this.gameController.loadWorld(this.clientWorldController);
            this.gameController.thePlayer.dimension = packetIn.func_149082_c();
            this.gameController.displayGuiScreen(new GuiDownloadTerrain(this));
        }

        this.gameController.setDimensionAndSpawnPlayer(packetIn.func_149082_c());
        this.gameController.playerController.setGameType(packetIn.func_149083_e());
    }

    /**
     * Initiates a new explosion (sound, particles, drop spawn) for the affected blocks indicated by the packet.
     */
    public void handleExplosion(S27PacketExplosion packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Explosion explosion = new Explosion(this.gameController.theWorld, (Entity)null, packetIn.func_149148_f(), packetIn.func_149143_g(), packetIn.func_149145_h(), packetIn.func_149146_i(), packetIn.func_149150_j());
        explosion.doExplosionB(true);
        this.gameController.thePlayer.motionX += (double)packetIn.func_149149_c();
        this.gameController.thePlayer.motionY += (double)packetIn.func_149144_d();
        this.gameController.thePlayer.motionZ += (double)packetIn.func_149147_e();
    }

    /**
     * Displays a GUI by ID. In order starting from id 0: Chest, Workbench, Furnace, Dispenser, Enchanting table,
     * Brewing stand, Villager merchant, Beacon, Anvil, Hopper, Dropper, Horse
     */
    public void handleOpenWindow(S2DPacketOpenWindow packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayersp = this.gameController.thePlayer;

        if ("minecraft:container".equals(packetIn.getGuiId()))
        {
            entityplayersp.displayGUIChest(new InventoryBasic(packetIn.getWindowTitle(), packetIn.getSlotCount()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
        else if ("minecraft:villager".equals(packetIn.getGuiId()))
        {
            entityplayersp.displayVillagerTradeGui(new NpcMerchant(entityplayersp, packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
        else if ("EntityHorse".equals(packetIn.getGuiId()))
        {
            Entity entity = this.clientWorldController.getEntityByID(packetIn.getEntityId());

            if (entity instanceof EntityHorse)
            {
                entityplayersp.displayGUIHorse((EntityHorse)entity, new AnimalChest(packetIn.getWindowTitle(), packetIn.getSlotCount()));
                entityplayersp.openContainer.windowId = packetIn.getWindowId();
            }
        }
        else if (!packetIn.hasSlots())
        {
            entityplayersp.displayGui(new LocalBlockIntercommunication(packetIn.getGuiId(), packetIn.getWindowTitle()));
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
        else
        {
            ContainerLocalMenu containerlocalmenu = new ContainerLocalMenu(packetIn.getGuiId(), packetIn.getWindowTitle(), packetIn.getSlotCount());
            entityplayersp.displayGUIChest(containerlocalmenu);
            entityplayersp.openContainer.windowId = packetIn.getWindowId();
        }
    }

    /**
     * Handles pickin up an ItemStack or dropping one in your inventory or an open (non-creative) container
     */
    public void handleSetSlot(S2FPacketSetSlot packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayersp = this.gameController.thePlayer;

        if (packetIn.func_149175_c() == -1)
        {
            entityplayersp.inventory.setItemStack(packetIn.func_149174_e());
        }
        else
        {
            boolean flag = false;

            if (this.gameController.currentScreen instanceof GuiContainerCreative)
            {
                GuiContainerCreative guicontainercreative = (GuiContainerCreative)this.gameController.currentScreen;
                flag = guicontainercreative.getSelectedTabIndex() != CreativeTabs.tabInventory.getTabIndex();
            }

            if (packetIn.func_149175_c() == 0 && packetIn.func_149173_d() >= 36 && packetIn.func_149173_d() < 45)
            {
                ItemStack itemstack = entityplayersp.inventoryContainer.getSlot(packetIn.func_149173_d()).getStack();

                if (packetIn.func_149174_e() != null && (itemstack == null || itemstack.stackSize < packetIn.func_149174_e().stackSize))
                {
                    packetIn.func_149174_e().animationsToGo = 5;
                }

                entityplayersp.inventoryContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
            }
            else if (packetIn.func_149175_c() == entityplayersp.openContainer.windowId && (packetIn.func_149175_c() != 0 || !flag))
            {
                entityplayersp.openContainer.putStackInSlot(packetIn.func_149173_d(), packetIn.func_149174_e());
            }
        }
    }

    /**
     * Verifies that the server and client are synchronized with respect to the inventory/container opened by the player
     * and confirms if it is the case.
     */
    public void handleConfirmTransaction(S32PacketConfirmTransaction packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Container container = null;
        EntityPlayerSP entityplayersp = this.gameController.thePlayer;

        if (packetIn.func_148889_c() == 0)
        {
            container = entityplayersp.inventoryContainer;
        }
        else if (packetIn.func_148889_c() == entityplayersp.openContainer.windowId)
        {
            container = entityplayersp.openContainer;
        }

        if (container != null && !packetIn.func_148888_e())
        {
            this.addToSendQueue(new C0FPacketConfirmTransaction(packetIn.func_148889_c(), packetIn.func_148890_d(), true));
        }
    }

    /**
     * Handles the placement of a specified ItemStack in a specified container/inventory slot
     */
    public void handleWindowItems(S30PacketWindowItems packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayersp = this.gameController.thePlayer;

        if (packetIn.func_148911_c() == 0)
        {
            entityplayersp.inventoryContainer.putStacksInSlots(packetIn.func_148910_d());
        }
        else if (packetIn.func_148911_c() == entityplayersp.openContainer.windowId)
        {
            entityplayersp.openContainer.putStacksInSlots(packetIn.func_148910_d());
        }
    }

    /**
     * Creates a sign in the specified location if it didn't exist and opens the GUI to edit its text
     */
    public void handleSignEditorOpen(S36PacketSignEditorOpen packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Object object = this.clientWorldController.getTileEntity(packetIn.func_179777_a());

        if (!(object instanceof TileEntitySign))
        {
            object = new TileEntitySign();
            ((TileEntity)object).setWorldObj(this.clientWorldController);
            ((TileEntity)object).setPos(packetIn.func_179777_a());
        }

        this.gameController.thePlayer.openEditSign((TileEntitySign)object);
    }

    /**
     * Updates a specified sign with the specified text lines
     */
    public void handleUpdateSign(S33PacketUpdateSign packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        boolean flag = false;

        if (this.gameController.theWorld.isBlockLoaded(packetIn.func_179704_a()))
        {
            TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.func_179704_a());

            if (tileentity instanceof TileEntitySign)
            {
                TileEntitySign tileentitysign = (TileEntitySign)tileentity;

                if (tileentitysign.getIsEditable())
                {
                    System.arraycopy(packetIn.func_180753_b(), 0, tileentitysign.signText, 0, 4);
                    tileentitysign.markDirty();
                }

                flag = true;
            }
        }

        if (!flag && this.gameController.thePlayer != null)
        {
            this.gameController.thePlayer.addChatMessage(new ChatComponentText("Unable to locate sign at " + packetIn.func_179704_a().getX() + ", " + packetIn.func_179704_a().getY() + ", " + packetIn.func_179704_a().getZ()));
        }
    }

    /**
     * Updates the NBTTagCompound metadata of instances of the following entitytypes: Mob spawners, command blocks,
     * beacons, skulls, flowerpot
     */
    public void handleUpdateTileEntity(S35PacketUpdateTileEntity packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);

        if (this.gameController.theWorld.isBlockLoaded(packetIn.func_179823_a()))
        {
            TileEntity tileentity = this.gameController.theWorld.getTileEntity(packetIn.func_179823_a());
            int i = packetIn.getTileEntityType();

            if (i == 1 && tileentity instanceof TileEntityMobSpawner || i == 2 && tileentity instanceof TileEntityCommandBlock || i == 3 && tileentity instanceof TileEntityBeacon || i == 4 && tileentity instanceof TileEntitySkull || i == 5 && tileentity instanceof TileEntityFlowerPot || i == 6 && tileentity instanceof TileEntityBanner)
            {
                tileentity.readFromNBT(packetIn.getNbtCompound());
            }
            else
            {
                tileentity.onDataPacket(netManager, packetIn);
            }
        }
    }

    /**
     * Sets the progressbar of the opened window to the specified value
     */
    public void handleWindowProperty(S31PacketWindowProperty packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayersp = this.gameController.thePlayer;

        if (entityplayersp.openContainer != null && entityplayersp.openContainer.windowId == packetIn.func_149182_c())
        {
            entityplayersp.openContainer.updateProgressBar(packetIn.func_149181_d(), packetIn.func_149180_e());
        }
    }

    public void handleEntityEquipment(S04PacketEntityEquipment packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.func_149389_d());

        if (entity != null)
        {
            entity.setCurrentItemOrArmor(packetIn.func_149388_e(), packetIn.func_149390_c());
        }
    }

    /**
     * Resets the ItemStack held in hand and closes the window that is opened
     */
    public void handleCloseWindow(S2EPacketCloseWindow packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.thePlayer.func_175159_q();
    }

    /**
     * Triggers Block.onBlockEventReceived, which is implemented in BlockPistonBase for extension/retraction, BlockNote
     * for setting the instrument (including audiovisual feedback) and in BlockContainer to set the number of players
     * accessing a (Ender)Chest
     */
    public void handleBlockAction(S24PacketBlockAction packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.theWorld.addBlockEvent(packetIn.func_179825_a(), packetIn.getBlockType(), packetIn.getData1(), packetIn.getData2());
    }

    /**
     * Updates all registered IWorldAccess instances with destroyBlockInWorldPartially
     */
    public void handleBlockBreakAnim(S25PacketBlockBreakAnim packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.theWorld.sendBlockBreakProgress(packetIn.func_148845_c(), packetIn.func_179821_b(), packetIn.func_148846_g());
    }

    public void handleMapChunkBulk(S26PacketMapChunkBulk packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);

        for (int i = 0; i < packetIn.func_149254_d(); ++i)
        {
            int j = packetIn.func_149255_a(i);
            int k = packetIn.func_149253_b(i);
            this.clientWorldController.doPreChunk(j, k, true);
            this.clientWorldController.invalidateBlockReceiveRegion(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);
            Chunk chunk = this.clientWorldController.getChunkFromChunkCoords(j, k);
            chunk.fillChunk(packetIn.func_149256_c(i), packetIn.func_179754_d(i), true);
            this.clientWorldController.markBlockRangeForRenderUpdate(j << 4, 0, k << 4, (j << 4) + 15, 256, (k << 4) + 15);

            if (!(this.clientWorldController.provider instanceof WorldProviderSurface))
            {
                chunk.resetRelightChecks();
            }
        }
    }

    public void handleChangeGameState(S2BPacketChangeGameState packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayersp = this.gameController.thePlayer;
        int i = packetIn.func_149138_c();
        float f = packetIn.func_149137_d();
        int j = MathHelper.floor_float(f + 0.5F);

        if (i >= 0 && i < S2BPacketChangeGameState.MESSAGE_NAMES.length && S2BPacketChangeGameState.MESSAGE_NAMES[i] != null)
        {
            entityplayersp.addChatComponentMessage(new ChatComponentTranslation(S2BPacketChangeGameState.MESSAGE_NAMES[i], new Object[0]));
        }

        if (i == 1)
        {
            this.clientWorldController.getWorldInfo().setRaining(true);
            this.clientWorldController.setRainStrength(0.0F);
        }
        else if (i == 2)
        {
            this.clientWorldController.getWorldInfo().setRaining(false);
            this.clientWorldController.setRainStrength(1.0F);
        }
        else if (i == 3)
        {
            this.gameController.playerController.setGameType(WorldSettings.GameType.getByID(j));
        }
        else if (i == 4)
        {
            this.gameController.displayGuiScreen(new GuiWinGame());
        }
        else if (i == 5)
        {
            GameSettings gamesettings = this.gameController.gameSettings;

            if (f == 0.0F)
            {
                this.gameController.displayGuiScreen(new GuiScreenDemo());
            }
            else if (f == 101.0F)
            {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.movement", new Object[] {GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode())}));
            }
            else if (f == 102.0F)
            {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.jump", new Object[] {GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode())}));
            }
            else if (f == 103.0F)
            {
                this.gameController.ingameGUI.getChatGUI().printChatMessage(new ChatComponentTranslation("demo.help.inventory", new Object[] {GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode())}));
            }
        }
        else if (i == 6)
        {
            this.clientWorldController.playSound(entityplayersp.posX, entityplayersp.posY + (double)entityplayersp.getEyeHeight(), entityplayersp.posZ, "random.successful_hit", 0.18F, 0.45F, false);
        }
        else if (i == 7)
        {
            this.clientWorldController.setRainStrength(f);
        }
        else if (i == 8)
        {
            this.clientWorldController.setThunderStrength(f);
        }
        else if (i == 10)
        {
            this.clientWorldController.spawnParticle(EnumParticleTypes.MOB_APPEARANCE, entityplayersp.posX, entityplayersp.posY, entityplayersp.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
            this.clientWorldController.playSound(entityplayersp.posX, entityplayersp.posY, entityplayersp.posZ, "mob.guardian.curse", 1.0F, 1.0F, false);
        }
    }

    /**
     * Updates the worlds MapStorage with the specified MapData for the specified map-identifier and invokes a
     * MapItemRenderer for it
     */
    public void handleMaps(S34PacketMaps packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        MapData mapdata = ItemMap.loadMapData(packetIn.getMapId(), this.gameController.theWorld);
        packetIn.func_179734_a(mapdata);
        this.gameController.entityRenderer.getMapItemRenderer().func_148246_a(mapdata);
    }

    public void handleEffect(S28PacketEffect packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);

        if (packetIn.isSoundServerwide())
        {
            this.gameController.theWorld.playBroadcastSound(packetIn.getSoundType(), packetIn.func_179746_d(), packetIn.getSoundData());
        }
        else
        {
            this.gameController.theWorld.playAuxSFX(packetIn.getSoundType(), packetIn.func_179746_d(), packetIn.getSoundData());
        }
    }

    /**
     * Updates the players statistics or achievements
     */
    public void handleStatistics(S37PacketStatistics packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        boolean flag = false;
        StatBase statbase;
        int i;

        for (Iterator iterator = packetIn.func_148974_c().entrySet().iterator(); iterator.hasNext(); this.gameController.thePlayer.getStatFileWriter().func_150873_a(this.gameController.thePlayer, statbase, i))
        {
            Entry entry = (Entry)iterator.next();
            statbase = (StatBase)entry.getKey();
            i = ((Integer)entry.getValue()).intValue();

            if (statbase.isAchievement() && i > 0)
            {
                if (this.field_147308_k && this.gameController.thePlayer.getStatFileWriter().readStat(statbase) == 0)
                {
                    Achievement achievement = (Achievement)statbase;
                    this.gameController.guiAchievement.displayAchievement(achievement);
                    this.gameController.getTwitchStream().func_152911_a(new MetadataAchievement(achievement), 0L);

                    if (statbase == AchievementList.openInventory)
                    {
                        this.gameController.gameSettings.showInventoryAchievementHint = false;
                        this.gameController.gameSettings.saveOptions();
                    }
                }

                flag = true;
            }
        }

        if (!this.field_147308_k && !flag && this.gameController.gameSettings.showInventoryAchievementHint)
        {
            this.gameController.guiAchievement.displayUnformattedAchievement(AchievementList.openInventory);
        }

        this.field_147308_k = true;

        if (this.gameController.currentScreen instanceof IProgressMeter)
        {
            ((IProgressMeter)this.gameController.currentScreen).doneLoading();
        }
    }

    public void handleEntityEffect(S1DPacketEntityEffect packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.func_149426_d());

        if (entity instanceof EntityLivingBase)
        {
            PotionEffect potioneffect = new PotionEffect(packetIn.func_149427_e(), packetIn.func_180755_e(), packetIn.func_149428_f(), false, packetIn.func_179707_f());
            potioneffect.setPotionDurationMax(packetIn.func_149429_c());
            ((EntityLivingBase)entity).addPotionEffect(potioneffect);
        }
    }

    public void handleCombatEvent(S42PacketCombatEvent packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.field_179775_c);
        EntityLivingBase entitylivingbase = entity instanceof EntityLivingBase ? (EntityLivingBase)entity : null;

        if (packetIn.field_179776_a == S42PacketCombatEvent.Event.END_COMBAT)
        {
            long i = (long)(1000 * packetIn.field_179772_d / 20);
            MetadataCombat metadatacombat = new MetadataCombat(this.gameController.thePlayer, entitylivingbase);
            this.gameController.getTwitchStream().func_176026_a(metadatacombat, 0L - i, 0L);
        }
        else if (packetIn.field_179776_a == S42PacketCombatEvent.Event.ENTITY_DIED)
        {
            Entity entity1 = this.clientWorldController.getEntityByID(packetIn.field_179774_b);

            if (entity1 instanceof EntityPlayer)
            {
                MetadataPlayerDeath metadataplayerdeath = new MetadataPlayerDeath((EntityPlayer)entity1, entitylivingbase);
                metadataplayerdeath.func_152807_a(packetIn.field_179773_e);
                this.gameController.getTwitchStream().func_152911_a(metadataplayerdeath, 0L);
            }
        }
    }

    public void handleServerDifficulty(S41PacketServerDifficulty packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.theWorld.getWorldInfo().setDifficulty(packetIn.func_179831_b());
        this.gameController.theWorld.getWorldInfo().setDifficultyLocked(packetIn.func_179830_a());
    }

    public void handleCamera(S43PacketCamera packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = packetIn.func_179780_a(this.clientWorldController);

        if (entity != null)
        {
            this.gameController.setRenderViewEntity(entity);
        }
    }

    public void handleWorldBorder(S44PacketWorldBorder packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        packetIn.func_179788_a(this.clientWorldController.getWorldBorder());
    }

    public void handleTitle(S45PacketTitle packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        S45PacketTitle.Type type = packetIn.func_179807_a();
        String s = null;
        String s1 = null;
        String s2 = packetIn.func_179805_b() != null ? packetIn.func_179805_b().getFormattedText() : "";

        switch (NetHandlerPlayClient.SwitchAction.field_178885_a[type.ordinal()])
        {
            case 1:
                s = s2;
                break;
            case 2:
                s1 = s2;
                break;
            case 3:
                this.gameController.ingameGUI.func_175178_a("", "", -1, -1, -1);
                this.gameController.ingameGUI.func_175177_a();
                return;
        }

        this.gameController.ingameGUI.func_175178_a(s, s1, packetIn.func_179806_c(), packetIn.func_179804_d(), packetIn.func_179803_e());
    }

    public void handleSetCompressionLevel(S46PacketSetCompressionLevel packetIn)
    {
        if (!this.netManager.isLocalChannel())
        {
            this.netManager.setCompressionTreshold(packetIn.func_179760_a());
        }
    }

    public void handlePlayerListHeaderFooter(S47PacketPlayerListHeaderFooter packetIn)
    {
        this.gameController.ingameGUI.getTabList().setHeader(packetIn.func_179700_a().getFormattedText().length() == 0 ? null : packetIn.func_179700_a());
        this.gameController.ingameGUI.getTabList().setFooter(packetIn.func_179701_b().getFormattedText().length() == 0 ? null : packetIn.func_179701_b());
    }

    public void handleRemoveEntityEffect(S1EPacketRemoveEntityEffect packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.func_149076_c());

        if (entity instanceof EntityLivingBase)
        {
            ((EntityLivingBase)entity).removePotionEffectClient(packetIn.func_149075_d());
        }
    }

    public void handlePlayerListItem(S38PacketPlayerListItem packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Iterator iterator = packetIn.func_179767_a().iterator();

        while (iterator.hasNext())
        {
            S38PacketPlayerListItem.AddPlayerData addplayerdata = (S38PacketPlayerListItem.AddPlayerData)iterator.next();

            if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.REMOVE_PLAYER)
            {
                this.playerInfoMap.remove(addplayerdata.func_179962_a().getId());
            }
            else
            {
                NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo)this.playerInfoMap.get(addplayerdata.func_179962_a().getId());

                if (packetIn.func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER)
                {
                    networkplayerinfo = new NetworkPlayerInfo(addplayerdata);
                    this.playerInfoMap.put(networkplayerinfo.getGameProfile().getId(), networkplayerinfo);
                }

                if (networkplayerinfo != null)
                {
                    switch (NetHandlerPlayClient.SwitchAction.field_178884_b[packetIn.func_179768_b().ordinal()])
                    {
                        case 1:
                            networkplayerinfo.setGameType(addplayerdata.func_179960_c());
                            networkplayerinfo.setResponseTime(addplayerdata.func_179963_b());
                            break;
                        case 2:
                            networkplayerinfo.setGameType(addplayerdata.func_179960_c());
                            break;
                        case 3:
                            networkplayerinfo.setResponseTime(addplayerdata.func_179963_b());
                            break;
                        case 4:
                            networkplayerinfo.func_178859_a(addplayerdata.func_179961_d());
                    }
                }
            }
        }
    }

    public void handleKeepAlive(S00PacketKeepAlive packetIn)
    {
        this.addToSendQueue(new C00PacketKeepAlive(packetIn.func_149134_c()));
    }

    public void handlePlayerAbilities(S39PacketPlayerAbilities packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        EntityPlayerSP entityplayersp = this.gameController.thePlayer;
        entityplayersp.capabilities.isFlying = packetIn.isFlying();
        entityplayersp.capabilities.isCreativeMode = packetIn.isCreativeMode();
        entityplayersp.capabilities.disableDamage = packetIn.isInvulnerable();
        entityplayersp.capabilities.allowFlying = packetIn.isAllowFlying();
        entityplayersp.capabilities.setFlySpeed(packetIn.getFlySpeed());
        entityplayersp.capabilities.setPlayerWalkSpeed(packetIn.getWalkSpeed());
    }

    /**
     * Displays the available command-completion options the server knows of
     */
    public void handleTabComplete(S3APacketTabComplete packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        String[] astring = packetIn.func_149630_c();

        if (this.gameController.currentScreen instanceof GuiChat)
        {
            GuiChat guichat = (GuiChat)this.gameController.currentScreen;
            guichat.onAutocompleteResponse(astring);
        }
    }

    public void handleSoundEffect(S29PacketSoundEffect packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        this.gameController.theWorld.playSound(packetIn.func_149207_d(), packetIn.func_149211_e(), packetIn.func_149210_f(), packetIn.func_149212_c(), packetIn.func_149208_g(), packetIn.func_149209_h(), false);
    }

    public void handleResourcePack(S48PacketResourcePackSend packetIn)
    {
        final String s = packetIn.func_179783_a();
        final String s1 = packetIn.func_179784_b();

        if (s.startsWith("level://"))
        {
            String s2 = s.substring("level://".length());
            File file1 = new File(this.gameController.mcDataDir, "saves");
            File file2 = new File(file1, s2);

            if (file2.isFile())
            {
                this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
                Futures.addCallback(this.gameController.getResourcePackRepository().func_177319_a(file2), new FutureCallback()
                {
                    private static final String __OBFID = "CL_00000879";
                    public void onSuccess(Object p_onSuccess_1_)
                    {
                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                    }
                    public void onFailure(Throwable p_onFailure_1_)
                    {
                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                    }
                });
            }
            else
            {
                this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
            }
        }
        else
        {
            if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() == ServerData.ServerResourceMode.ENABLED)
            {
                this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
                Futures.addCallback(this.gameController.getResourcePackRepository().func_180601_a(s, s1), new FutureCallback()
                {
                    private static final String __OBFID = "CL_00002624";
                    public void onSuccess(Object p_onSuccess_1_)
                    {
                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                    }
                    public void onFailure(Throwable p_onFailure_1_)
                    {
                        NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                    }
                });
            }
            else if (this.gameController.getCurrentServerData() != null && this.gameController.getCurrentServerData().getResourceMode() != ServerData.ServerResourceMode.PROMPT)
            {
                this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
            }
            else
            {
                this.gameController.addScheduledTask(new Runnable()
                {
                    private static final String __OBFID = "CL_00002623";
                    public void run()
                    {
                        NetHandlerPlayClient.this.gameController.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback()
                        {
                            private static final String __OBFID = "CL_00002622";
                            public void confirmClicked(boolean result, int id)
                            {
                                NetHandlerPlayClient.this.gameController = Minecraft.getMinecraft();

                                if (result)
                                {
                                    if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null)
                                    {
                                        NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.ENABLED);
                                    }

                                    NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.ACCEPTED));
                                    Futures.addCallback(NetHandlerPlayClient.this.gameController.getResourcePackRepository().func_180601_a(s, s1), new FutureCallback()
                                    {
                                        private static final String __OBFID = "CL_00002621";
                                        public void onSuccess(Object p_onSuccess_1_)
                                        {
                                            NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.SUCCESSFULLY_LOADED));
                                        }
                                        public void onFailure(Throwable p_onFailure_1_)
                                        {
                                            NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
                                        }
                                    });
                                }
                                else
                                {
                                    if (NetHandlerPlayClient.this.gameController.getCurrentServerData() != null)
                                    {
                                        NetHandlerPlayClient.this.gameController.getCurrentServerData().setResourceMode(ServerData.ServerResourceMode.DISABLED);
                                    }

                                    NetHandlerPlayClient.this.netManager.sendPacket(new C19PacketResourcePackStatus(s1, C19PacketResourcePackStatus.Action.DECLINED));
                                }

                                ServerList.func_147414_b(NetHandlerPlayClient.this.gameController.getCurrentServerData());
                                NetHandlerPlayClient.this.gameController.displayGuiScreen((GuiScreen)null);
                            }
                        }, I18n.format("multiplayer.texturePrompt.line1", new Object[0]), I18n.format("multiplayer.texturePrompt.line2", new Object[0]), 0));
                    }
                });
            }
        }
    }

    public void handleEntityNBT(S49PacketUpdateEntityNBT packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = packetIn.func_179764_a(this.clientWorldController);

        if (entity != null)
        {
            entity.func_174834_g(packetIn.func_179763_a());
        }
    }

    /**
     * Handles packets that have room for a channel specification. Vanilla implemented channels are "MC|TrList" to
     * acquire a MerchantRecipeList trades for a villager merchant, "MC|Brand" which sets the server brand? on the
     * player instance and finally "MC|RPack" which the server uses to communicate the identifier of the default server
     * resourcepack for the client to load.
     */
    public void handleCustomPayload(S3FPacketCustomPayload packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);

        if ("MC|TrList".equals(packetIn.getChannelName()))
        {
            PacketBuffer packetbuffer = packetIn.getBufferData();

            try
            {
                int i = packetbuffer.readInt();
                GuiScreen guiscreen = this.gameController.currentScreen;

                if (guiscreen != null && guiscreen instanceof GuiMerchant && i == this.gameController.thePlayer.openContainer.windowId)
                {
                    IMerchant imerchant = ((GuiMerchant)guiscreen).getMerchant();
                    MerchantRecipeList merchantrecipelist = MerchantRecipeList.readFromBuf(packetbuffer);
                    imerchant.setRecipes(merchantrecipelist);
                }
            }
            catch (IOException ioexception)
            {
                logger.error("Couldn\'t load trade info", ioexception);
            }
            finally
            {
                packetbuffer.release();
            }
        }
        else if ("MC|Brand".equals(packetIn.getChannelName()))
        {
            this.gameController.thePlayer.setClientBrand(packetIn.getBufferData().readStringFromBuffer(32767));
        }
        else if ("MC|BOpen".equals(packetIn.getChannelName()))
        {
            ItemStack itemstack = this.gameController.thePlayer.getCurrentEquippedItem();

            if (itemstack != null && itemstack.getItem() == Items.written_book)
            {
                this.gameController.displayGuiScreen(new GuiScreenBook(this.gameController.thePlayer, itemstack, false));
            }
        }
    }

    /**
     * May create a scoreboard objective, remove an objective from the scoreboard or update an objectives' displayname
     */
    public void handleScoreboardObjective(S3BPacketScoreboardObjective packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        ScoreObjective scoreobjective;

        if (packetIn.func_149338_e() == 0)
        {
            scoreobjective = scoreboard.addScoreObjective(packetIn.func_149339_c(), IScoreObjectiveCriteria.DUMMY);
            scoreobjective.setDisplayName(packetIn.func_149337_d());
            scoreobjective.setRenderType(packetIn.func_179817_d());
        }
        else
        {
            scoreobjective = scoreboard.getObjective(packetIn.func_149339_c());

            if (packetIn.func_149338_e() == 1)
            {
                scoreboard.func_96519_k(scoreobjective);
            }
            else if (packetIn.func_149338_e() == 2)
            {
                scoreobjective.setDisplayName(packetIn.func_149337_d());
                scoreobjective.setRenderType(packetIn.func_179817_d());
            }
        }
    }

    /**
     * Either updates the score with a specified value or removes the score for an objective
     */
    public void handleUpdateScore(S3CPacketUpdateScore packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.func_149321_d());

        if (packetIn.func_180751_d() == S3CPacketUpdateScore.Action.CHANGE)
        {
            Score score = scoreboard.getValueFromObjective(packetIn.func_149324_c(), scoreobjective);
            score.setScorePoints(packetIn.func_149323_e());
        }
        else if (packetIn.func_180751_d() == S3CPacketUpdateScore.Action.REMOVE)
        {
            if (StringUtils.isNullOrEmpty(packetIn.func_149321_d()))
            {
                scoreboard.removeObjectiveFromEntity(packetIn.func_149324_c(), (ScoreObjective)null);
            }
            else if (scoreobjective != null)
            {
                scoreboard.removeObjectiveFromEntity(packetIn.func_149324_c(), scoreobjective);
            }
        }
    }

    /**
     * Removes or sets the ScoreObjective to be displayed at a particular scoreboard position (list, sidebar, below
     * name)
     */
    public void handleDisplayScoreboard(S3DPacketDisplayScoreboard packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();

        if (packetIn.func_149370_d().length() == 0)
        {
            scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), (ScoreObjective)null);
        }
        else
        {
            ScoreObjective scoreobjective = scoreboard.getObjective(packetIn.func_149370_d());
            scoreboard.setObjectiveInDisplaySlot(packetIn.func_149371_c(), scoreobjective);
        }
    }

    /**
     * Updates a team managed by the scoreboard: Create/Remove the team registration, Register/Remove the player-team-
     * memberships, Set team displayname/prefix/suffix and/or whether friendly fire is enabled
     */
    public void handleTeams(S3EPacketTeams packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Scoreboard scoreboard = this.clientWorldController.getScoreboard();
        ScorePlayerTeam scoreplayerteam;

        if (packetIn.func_149307_h() == 0)
        {
            scoreplayerteam = scoreboard.createTeam(packetIn.func_149312_c());
        }
        else
        {
            scoreplayerteam = scoreboard.getTeam(packetIn.func_149312_c());
        }

        if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 2)
        {
            scoreplayerteam.setTeamName(packetIn.func_149306_d());
            scoreplayerteam.setNamePrefix(packetIn.func_149311_e());
            scoreplayerteam.setNameSuffix(packetIn.func_149309_f());
            scoreplayerteam.func_178774_a(EnumChatFormatting.func_175744_a(packetIn.func_179813_h()));
            scoreplayerteam.func_98298_a(packetIn.func_149308_i());
            Team.EnumVisible enumvisible = Team.EnumVisible.func_178824_a(packetIn.func_179814_i());

            if (enumvisible != null)
            {
                scoreplayerteam.func_178772_a(enumvisible);
            }
        }

        String s;
        Iterator iterator;

        if (packetIn.func_149307_h() == 0 || packetIn.func_149307_h() == 3)
        {
            iterator = packetIn.func_149310_g().iterator();

            while (iterator.hasNext())
            {
                s = (String)iterator.next();
                scoreboard.addPlayerToTeam(s, packetIn.func_149312_c());
            }
        }

        if (packetIn.func_149307_h() == 4)
        {
            iterator = packetIn.func_149310_g().iterator();

            while (iterator.hasNext())
            {
                s = (String)iterator.next();
                scoreboard.removePlayerFromTeam(s, scoreplayerteam);
            }
        }

        if (packetIn.func_149307_h() == 1)
        {
            scoreboard.removeTeam(scoreplayerteam);
        }
    }

    /**
     * Spawns a specified number of particles at the specified location with a randomized displacement according to
     * specified bounds
     */
    public void handleParticles(S2APacketParticles packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);

        if (packetIn.getParticleCount() == 0)
        {
            double d0 = (double)(packetIn.getParticleSpeed() * packetIn.getXOffset());
            double d2 = (double)(packetIn.getParticleSpeed() * packetIn.getYOffset());
            double d4 = (double)(packetIn.getParticleSpeed() * packetIn.getZOffset());

            try
            {
                this.clientWorldController.spawnParticle(packetIn.func_179749_a(), packetIn.func_179750_b(), packetIn.getXCoordinate(), packetIn.getYCoordinate(), packetIn.getZCoordinate(), d0, d2, d4, packetIn.getParticleArgs());
            }
            catch (Throwable throwable1)
            {
                logger.warn("Could not spawn particle effect " + packetIn.func_179749_a());
            }
        }
        else
        {
            for (int i = 0; i < packetIn.getParticleCount(); ++i)
            {
                double d1 = this.avRandomizer.nextGaussian() * (double)packetIn.getXOffset();
                double d3 = this.avRandomizer.nextGaussian() * (double)packetIn.getYOffset();
                double d5 = this.avRandomizer.nextGaussian() * (double)packetIn.getZOffset();
                double d6 = this.avRandomizer.nextGaussian() * (double)packetIn.getParticleSpeed();
                double d7 = this.avRandomizer.nextGaussian() * (double)packetIn.getParticleSpeed();
                double d8 = this.avRandomizer.nextGaussian() * (double)packetIn.getParticleSpeed();

                try
                {
                    this.clientWorldController.spawnParticle(packetIn.func_179749_a(), packetIn.func_179750_b(), packetIn.getXCoordinate() + d1, packetIn.getYCoordinate() + d3, packetIn.getZCoordinate() + d5, d6, d7, d8, packetIn.getParticleArgs());
                }
                catch (Throwable throwable)
                {
                    logger.warn("Could not spawn particle effect " + packetIn.func_179749_a());
                    return;
                }
            }
        }
    }

    /**
     * Updates en entity's attributes and their respective modifiers, which are used for speed bonusses (player
     * sprinting, animals fleeing, baby speed), weapon/tool attackDamage, hostiles followRange randomization, zombie
     * maxHealth and knockback resistance as well as reinforcement spawning chance.
     */
    public void handleEntityProperties(S20PacketEntityProperties packetIn)
    {
        PacketThreadUtil.checkThreadAndEnqueue(packetIn, this, this.gameController);
        Entity entity = this.clientWorldController.getEntityByID(packetIn.func_149442_c());

        if (entity != null)
        {
            if (!(entity instanceof EntityLivingBase))
            {
                throw new IllegalStateException("Server tried to update attributes of a non-living entity (actually: " + entity + ")");
            }
            else
            {
                BaseAttributeMap baseattributemap = ((EntityLivingBase)entity).getAttributeMap();
                Iterator iterator = packetIn.func_149441_d().iterator();

                while (iterator.hasNext())
                {
                    S20PacketEntityProperties.Snapshot snapshot = (S20PacketEntityProperties.Snapshot)iterator.next();
                    IAttributeInstance iattributeinstance = baseattributemap.getAttributeInstanceByName(snapshot.func_151409_a());

                    if (iattributeinstance == null)
                    {
                        iattributeinstance = baseattributemap.registerAttribute(new RangedAttribute((IAttribute)null, snapshot.func_151409_a(), 0.0D, 2.2250738585072014E-308D, Double.MAX_VALUE));
                    }

                    iattributeinstance.setBaseValue(snapshot.func_151410_b());
                    iattributeinstance.removeAllModifiers();
                    Iterator iterator1 = snapshot.func_151408_c().iterator();

                    while (iterator1.hasNext())
                    {
                        AttributeModifier attributemodifier = (AttributeModifier)iterator1.next();
                        iattributeinstance.applyModifier(attributemodifier);
                    }
                }
            }
        }
    }

    /**
     * Returns this the NetworkManager instance registered with this NetworkHandlerPlayClient
     */
    public NetworkManager getNetworkManager()
    {
        return this.netManager;
    }

    public Collection func_175106_d()
    {
        return this.playerInfoMap.values();
    }

    public NetworkPlayerInfo getPlayerInfo(UUID p_175102_1_)
    {
        return (NetworkPlayerInfo)this.playerInfoMap.get(p_175102_1_);
    }

    public NetworkPlayerInfo func_175104_a(String p_175104_1_)
    {
        Iterator iterator = this.playerInfoMap.values().iterator();
        NetworkPlayerInfo networkplayerinfo;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            networkplayerinfo = (NetworkPlayerInfo)iterator.next();
        }
        while (!networkplayerinfo.getGameProfile().getName().equals(p_175104_1_));

        return networkplayerinfo;
    }

    public GameProfile getGameProfile()
    {
        return this.profile;
    }

    @SideOnly(Side.CLIENT)

    static final class SwitchAction
        {
            static final int[] field_178885_a;

            static final int[] field_178884_b = new int[S38PacketPlayerListItem.Action.values().length];
            private static final String __OBFID = "CL_00002620";

            static
            {
                try
                {
                    field_178884_b[S38PacketPlayerListItem.Action.ADD_PLAYER.ordinal()] = 1;
                }
                catch (NoSuchFieldError var7)
                {
                    ;
                }

                try
                {
                    field_178884_b[S38PacketPlayerListItem.Action.UPDATE_GAME_MODE.ordinal()] = 2;
                }
                catch (NoSuchFieldError var6)
                {
                    ;
                }

                try
                {
                    field_178884_b[S38PacketPlayerListItem.Action.UPDATE_LATENCY.ordinal()] = 3;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                try
                {
                    field_178884_b[S38PacketPlayerListItem.Action.UPDATE_DISPLAY_NAME.ordinal()] = 4;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                field_178885_a = new int[S45PacketTitle.Type.values().length];

                try
                {
                    field_178885_a[S45PacketTitle.Type.TITLE.ordinal()] = 1;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_178885_a[S45PacketTitle.Type.SUBTITLE.ordinal()] = 2;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_178885_a[S45PacketTitle.Type.RESET.ordinal()] = 3;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}