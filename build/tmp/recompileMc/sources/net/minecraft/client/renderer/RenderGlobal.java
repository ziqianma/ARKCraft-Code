package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.ListChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.VboChunkFactory;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemRecord;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderGlobal implements IWorldAccess, IResourceManagerReloadListener
{
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
    private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
    private static final ResourceLocation locationForcefieldPng = new ResourceLocation("textures/misc/forcefield.png");
    /** A reference to the Minecraft object. */
    private final Minecraft mc;
    /** The RenderEngine instance used by RenderGlobal */
    private final TextureManager renderEngine;
    private final RenderManager renderManager;
    private WorldClient theWorld;
    private Set chunksToUpdate = Sets.newLinkedHashSet();
    /** List of OpenGL lists for the current render pass */
    private List renderInfos = Lists.newArrayListWithCapacity(69696);
    private ViewFrustum viewFrustum;
    /** The star GL Call list */
    private int starGLCallList = -1;
    /** OpenGL sky list */
    private int glSkyList = -1;
    /** OpenGL sky list 2 */
    private int glSkyList2 = -1;
    private VertexFormat vertexBufferFormat;
    private VertexBuffer starVBO;
    private VertexBuffer skyVBO;
    private VertexBuffer sky2VBO;
    /** counts the cloud render updates. Used with mod to stagger some updates */
    private int cloudTickCounter;
    /**
     * Stores blocks currently being broken. Key is entity ID of the thing doing the breaking. Value is a
     * DestroyBlockProgress
     */
    private final Map damagedBlocks = Maps.newHashMap();
    /** Currently playing sounds.  Type:  HashMap<ChunkCoordinates, ISound> */
    private final Map mapSoundPositions = Maps.newHashMap();
    private final TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];
    private Framebuffer entityOutlineFramebuffer;
    /** Stores the shader group for the entity_outline shader */
    private ShaderGroup entityOutlineShader;
    private double frustumUpdatePosX = Double.MIN_VALUE;
    private double frustumUpdatePosY = Double.MIN_VALUE;
    private double frustumUpdatePosZ = Double.MIN_VALUE;
    private int frustumUpdatePosChunkX = Integer.MIN_VALUE;
    private int frustumUpdatePosChunkY = Integer.MIN_VALUE;
    private int frustumUpdatePosChunkZ = Integer.MIN_VALUE;
    private double lastViewEntityX = Double.MIN_VALUE;
    private double lastViewEntityY = Double.MIN_VALUE;
    private double lastViewEntityZ = Double.MIN_VALUE;
    private double lastViewEntityPitch = Double.MIN_VALUE;
    private double lastViewEntityYaw = Double.MIN_VALUE;
    private final ChunkRenderDispatcher renderDispatcher = new ChunkRenderDispatcher();
    private ChunkRenderContainer renderContainer;
    private int renderDistanceChunks = -1;
    /** Render entities startup counter (init value=2) */
    private int renderEntitiesStartupCounter = 2;
    /** Count entities total */
    private int countEntitiesTotal;
    /** Count entities rendered */
    private int countEntitiesRendered;
    /** Count entities hidden */
    private int countEntitiesHidden;
    private boolean debugFixTerrainFrustum = false;
    private ClippingHelper debugFixedClippingHelper;
    private final Vector4f[] debugTerrainMatrix = new Vector4f[8];
    private final Vector3d debugTerrainFrustumPosition = new Vector3d();
    private boolean vboEnabled = false;
    IRenderChunkFactory renderChunkFactory;
    private double prevRenderSortX;
    private double prevRenderSortY;
    private double prevRenderSortZ;
    private boolean displayListEntitiesDirty = true;
    private static final String __OBFID = "CL_00000954";

    public RenderGlobal(Minecraft mcIn)
    {
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.renderEngine = mcIn.getTextureManager();
        this.renderEngine.bindTexture(locationForcefieldPng);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GlStateManager.bindTexture(0);
        this.updateDestroyBlockIcons();
        this.vboEnabled = OpenGlHelper.useVbo();

        if (this.vboEnabled)
        {
            this.renderContainer = new VboRenderList();
            this.renderChunkFactory = new VboChunkFactory();
        }
        else
        {
            this.renderContainer = new RenderList();
            this.renderChunkFactory = new ListChunkFactory();
        }

        this.vertexBufferFormat = new VertexFormat();
        this.vertexBufferFormat.setElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
        this.generateStars();
        this.generateSky();
        this.generateSky2();
    }

    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        this.updateDestroyBlockIcons();
    }

    private void updateDestroyBlockIcons()
    {
        TextureMap texturemap = this.mc.getTextureMapBlocks();

        for (int i = 0; i < this.destroyBlockIcons.length; ++i)
        {
            this.destroyBlockIcons[i] = texturemap.getAtlasSprite("minecraft:blocks/destroy_stage_" + i);
        }
    }

    /**
     * Creates the entity outline shader to be stored in RenderGlobal.entityOutlineShader
     */
    public void makeEntityOutlineShader()
    {
        if (OpenGlHelper.shadersSupported)
        {
            if (ShaderLinkHelper.getStaticShaderLinkHelper() == null)
            {
                ShaderLinkHelper.setNewStaticShaderLinkHelper();
            }

            ResourceLocation resourcelocation = new ResourceLocation("shaders/post/entity_outline.json");

            try
            {
                this.entityOutlineShader = new ShaderGroup(this.mc.getTextureManager(), this.mc.getResourceManager(), this.mc.getFramebuffer(), resourcelocation);
                this.entityOutlineShader.createBindFramebuffers(this.mc.displayWidth, this.mc.displayHeight);
                this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
            }
            catch (IOException ioexception)
            {
                logger.warn("Failed to load shader: " + resourcelocation, ioexception);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
            catch (JsonSyntaxException jsonsyntaxexception)
            {
                logger.warn("Failed to load shader: " + resourcelocation, jsonsyntaxexception);
                this.entityOutlineShader = null;
                this.entityOutlineFramebuffer = null;
            }
        }
        else
        {
            this.entityOutlineShader = null;
            this.entityOutlineFramebuffer = null;
        }
    }

    public void renderEntityOutlineFramebuffer()
    {
        if (this.isRenderEntityOutlines())
        {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            this.entityOutlineFramebuffer.framebufferRenderExt(this.mc.displayWidth, this.mc.displayHeight, false);
            GlStateManager.disableBlend();
        }
    }

    protected boolean isRenderEntityOutlines()
    {
        return this.entityOutlineFramebuffer != null && this.entityOutlineShader != null && this.mc.thePlayer != null && this.mc.thePlayer.isSpectator() && this.mc.gameSettings.keyBindSpectatorOutlines.isKeyDown();
    }

    private void generateSky2()
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        if (this.sky2VBO != null)
        {
            this.sky2VBO.deleteGlBuffers();
        }

        if (this.glSkyList2 >= 0)
        {
            GLAllocation.deleteDisplayLists(this.glSkyList2);
            this.glSkyList2 = -1;
        }

        if (this.vboEnabled)
        {
            this.sky2VBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(worldrenderer, -16.0F, true);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.sky2VBO.bufferData(worldrenderer.getByteBuffer(), worldrenderer.getByteIndex());
        }
        else
        {
            this.glSkyList2 = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(this.glSkyList2, GL11.GL_COMPILE);
            this.renderSky(worldrenderer, -16.0F, true);
            tessellator.draw();
            GL11.glEndList();
        }
    }

    private void generateSky()
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        if (this.skyVBO != null)
        {
            this.skyVBO.deleteGlBuffers();
        }

        if (this.glSkyList >= 0)
        {
            GLAllocation.deleteDisplayLists(this.glSkyList);
            this.glSkyList = -1;
        }

        if (this.vboEnabled)
        {
            this.skyVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderSky(worldrenderer, 16.0F, false);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.skyVBO.bufferData(worldrenderer.getByteBuffer(), worldrenderer.getByteIndex());
        }
        else
        {
            this.glSkyList = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(this.glSkyList, GL11.GL_COMPILE);
            this.renderSky(worldrenderer, 16.0F, false);
            tessellator.draw();
            GL11.glEndList();
        }
    }

    private void renderSky(WorldRenderer worldRendererIn, float p_174968_2_, boolean p_174968_3_)
    {
        boolean flag1 = true;
        boolean flag2 = true;
        worldRendererIn.startDrawingQuads();

        for (int i = -384; i <= 384; i += 64)
        {
            for (int j = -384; j <= 384; j += 64)
            {
                float f1 = (float)i;
                float f2 = (float)(i + 64);

                if (p_174968_3_)
                {
                    f2 = (float)i;
                    f1 = (float)(i + 64);
                }

                worldRendererIn.addVertex((double)f1, (double)p_174968_2_, (double)j);
                worldRendererIn.addVertex((double)f2, (double)p_174968_2_, (double)j);
                worldRendererIn.addVertex((double)f2, (double)p_174968_2_, (double)(j + 64));
                worldRendererIn.addVertex((double)f1, (double)p_174968_2_, (double)(j + 64));
            }
        }
    }

    private void generateStars()
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        if (this.starVBO != null)
        {
            this.starVBO.deleteGlBuffers();
        }

        if (this.starGLCallList >= 0)
        {
            GLAllocation.deleteDisplayLists(this.starGLCallList);
            this.starGLCallList = -1;
        }

        if (this.vboEnabled)
        {
            this.starVBO = new VertexBuffer(this.vertexBufferFormat);
            this.renderStars(worldrenderer);
            worldrenderer.finishDrawing();
            worldrenderer.reset();
            this.starVBO.bufferData(worldrenderer.getByteBuffer(), worldrenderer.getByteIndex());
        }
        else
        {
            this.starGLCallList = GLAllocation.generateDisplayLists(1);
            GlStateManager.pushMatrix();
            GL11.glNewList(this.starGLCallList, GL11.GL_COMPILE);
            this.renderStars(worldrenderer);
            tessellator.draw();
            GL11.glEndList();
            GlStateManager.popMatrix();
        }
    }

    private void renderStars(WorldRenderer worldRendererIn)
    {
        Random random = new Random(10842L);
        worldRendererIn.startDrawingQuads();

        for (int i = 0; i < 1500; ++i)
        {
            double d0 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d1 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d2 = (double)(random.nextFloat() * 2.0F - 1.0F);
            double d3 = (double)(0.15F + random.nextFloat() * 0.1F);
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;

            if (d4 < 1.0D && d4 > 0.01D)
            {
                d4 = 1.0D / Math.sqrt(d4);
                d0 *= d4;
                d1 *= d4;
                d2 *= d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = Math.sin(d8);
                double d10 = Math.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = Math.sin(d11);
                double d13 = Math.cos(d11);
                double d14 = random.nextDouble() * Math.PI * 2.0D;
                double d15 = Math.sin(d14);
                double d16 = Math.cos(d14);

                for (int j = 0; j < 4; ++j)
                {
                    double d17 = 0.0D;
                    double d18 = (double)((j & 2) - 1) * d3;
                    double d19 = (double)((j + 1 & 2) - 1) * d3;
                    double d20 = 0.0D;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0D * d13;
                    double d24 = 0.0D * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    worldRendererIn.addVertex(d5 + d25, d6 + d23, d7 + d26);
                }
            }
        }
    }

    /**
     * set null to clear
     */
    public void setWorldAndLoadRenderers(WorldClient worldClientIn)
    {
        if (this.theWorld != null)
        {
            this.theWorld.removeWorldAccess(this);
        }

        this.frustumUpdatePosX = Double.MIN_VALUE;
        this.frustumUpdatePosY = Double.MIN_VALUE;
        this.frustumUpdatePosZ = Double.MIN_VALUE;
        this.frustumUpdatePosChunkX = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkY = Integer.MIN_VALUE;
        this.frustumUpdatePosChunkZ = Integer.MIN_VALUE;
        this.renderManager.set(worldClientIn);
        this.theWorld = worldClientIn;

        if (worldClientIn != null)
        {
            worldClientIn.addWorldAccess(this);
            this.loadRenderers();
        }
    }

    /**
     * Loads all the renderers and sets up the basic settings usage
     */
    public void loadRenderers()
    {
        if (this.theWorld != null)
        {
            this.displayListEntitiesDirty = true;
            Blocks.leaves.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
            Blocks.leaves2.setGraphicsLevel(this.mc.gameSettings.fancyGraphics);
            this.renderDistanceChunks = this.mc.gameSettings.renderDistanceChunks;
            boolean flag = this.vboEnabled;
            this.vboEnabled = OpenGlHelper.useVbo();

            if (flag && !this.vboEnabled)
            {
                this.renderContainer = new RenderList();
                this.renderChunkFactory = new ListChunkFactory();
            }
            else if (!flag && this.vboEnabled)
            {
                this.renderContainer = new VboRenderList();
                this.renderChunkFactory = new VboChunkFactory();
            }

            if (flag != this.vboEnabled)
            {
                this.generateStars();
                this.generateSky();
                this.generateSky2();
            }

            if (this.viewFrustum != null)
            {
                this.viewFrustum.deleteGlResources();
            }

            this.stopChunkUpdates();
            this.viewFrustum = new ViewFrustum(this.theWorld, this.mc.gameSettings.renderDistanceChunks, this, this.renderChunkFactory);

            if (this.theWorld != null)
            {
                Entity entity = this.mc.getRenderViewEntity();

                if (entity != null)
                {
                    this.viewFrustum.updateChunkPositions(entity.posX, entity.posZ);
                }
            }

            this.renderEntitiesStartupCounter = 2;
        }
    }

    protected void stopChunkUpdates()
    {
        this.chunksToUpdate.clear();
        this.renderDispatcher.stopChunkUpdates();
    }

    public void createBindEntityOutlineFbs(int p_72720_1_, int p_72720_2_)
    {
        if (OpenGlHelper.shadersSupported)
        {
            if (this.entityOutlineShader != null)
            {
                this.entityOutlineShader.createBindFramebuffers(p_72720_1_, p_72720_2_);
            }
        }
    }

    public void renderEntities(Entity p_180446_1_, ICamera p_180446_2_, float partialTicks)
    {
        int pass = net.minecraftforge.client.MinecraftForgeClient.getRenderPass();
        if (this.renderEntitiesStartupCounter > 0)
        {
            if (pass > 0) return;
            --this.renderEntitiesStartupCounter;
        }
        else
        {
            double d0 = p_180446_1_.prevPosX + (p_180446_1_.posX - p_180446_1_.prevPosX) * (double)partialTicks;
            double d1 = p_180446_1_.prevPosY + (p_180446_1_.posY - p_180446_1_.prevPosY) * (double)partialTicks;
            double d2 = p_180446_1_.prevPosZ + (p_180446_1_.posZ - p_180446_1_.prevPosZ) * (double)partialTicks;
            this.theWorld.theProfiler.startSection("prepare");
            TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), this.mc.fontRendererObj, this.mc.getRenderViewEntity(), partialTicks);
            this.renderManager.cacheActiveRenderInfo(this.theWorld, this.mc.fontRendererObj, this.mc.getRenderViewEntity(), this.mc.pointedEntity, this.mc.gameSettings, partialTicks);
            if (pass == 0) // no indentation to shrink patch
            {
            this.countEntitiesTotal = 0;
            this.countEntitiesRendered = 0;
            this.countEntitiesHidden = 0;
            }
            Entity entity1 = this.mc.getRenderViewEntity();
            double d3 = entity1.lastTickPosX + (entity1.posX - entity1.lastTickPosX) * (double)partialTicks;
            double d4 = entity1.lastTickPosY + (entity1.posY - entity1.lastTickPosY) * (double)partialTicks;
            double d5 = entity1.lastTickPosZ + (entity1.posZ - entity1.lastTickPosZ) * (double)partialTicks;
            TileEntityRendererDispatcher.staticPlayerX = d3;
            TileEntityRendererDispatcher.staticPlayerY = d4;
            TileEntityRendererDispatcher.staticPlayerZ = d5;
            this.renderManager.setRenderPosition(d3, d4, d5);
            this.mc.entityRenderer.enableLightmap();
            this.theWorld.theProfiler.endStartSection("global");
            List list = this.theWorld.getLoadedEntityList();
            if (pass == 0) // no indentation to shrink patch
            {
            this.countEntitiesTotal = list.size();
            }
            int i;
            Entity entity2;

            for (i = 0; i < this.theWorld.weatherEffects.size(); ++i)
            {
                entity2 = (Entity)this.theWorld.weatherEffects.get(i);
                if (!entity2.shouldRenderInPass(pass)) continue;
                ++this.countEntitiesRendered;

                if (entity2.isInRangeToRender3d(d0, d1, d2))
                {
                    this.renderManager.renderEntitySimple(entity2, partialTicks);
                }
            }

            if (this.isRenderEntityOutlines())
            {
                GlStateManager.depthFunc(519);
                GlStateManager.disableFog();
                this.entityOutlineFramebuffer.framebufferClear();
                this.entityOutlineFramebuffer.bindFramebuffer(false);
                this.theWorld.theProfiler.endStartSection("entityOutlines");
                RenderHelper.disableStandardItemLighting();
                this.renderManager.setRenderOutlines(true);

                for (i = 0; i < list.size(); ++i)
                {
                    entity2 = (Entity)list.get(i);
                    if (!entity2.shouldRenderInPass(pass)) continue;
                    boolean flag = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();
                    boolean flag1 = entity2.isInRangeToRender3d(d0, d1, d2) && (entity2.ignoreFrustumCheck || p_180446_2_.isBoundingBoxInFrustum(entity2.getEntityBoundingBox()) || entity2.riddenByEntity == this.mc.thePlayer) && entity2 instanceof EntityPlayer;

                    if ((entity2 != this.mc.getRenderViewEntity() || this.mc.gameSettings.thirdPersonView != 0 || flag) && flag1)
                    {
                        this.renderManager.renderEntitySimple(entity2, partialTicks);
                    }
                }

                this.renderManager.setRenderOutlines(false);
                RenderHelper.enableStandardItemLighting();
                GlStateManager.depthMask(false);
                this.entityOutlineShader.loadShaderGroup(partialTicks);
                GlStateManager.depthMask(true);
                this.mc.getFramebuffer().bindFramebuffer(false);
                GlStateManager.enableFog();
                GlStateManager.depthFunc(515);
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
            }

            this.theWorld.theProfiler.endStartSection("entities");
            Iterator iterator = this.renderInfos.iterator();
            RenderGlobal.ContainerLocalRenderInformation containerlocalrenderinformation;

            while (iterator.hasNext())
            {
                containerlocalrenderinformation = (RenderGlobal.ContainerLocalRenderInformation)iterator.next();
                Chunk chunk = this.theWorld.getChunkFromBlockCoords(containerlocalrenderinformation.renderChunk.getPosition());
                Iterator iterator2 = chunk.getEntityLists()[containerlocalrenderinformation.renderChunk.getPosition().getY() / 16].iterator();

                while (iterator2.hasNext())
                {
                    Entity entity3 = (Entity)iterator2.next();
                    if (!entity3.shouldRenderInPass(pass)) continue;
                    boolean flag2 = this.renderManager.shouldRender(entity3, p_180446_2_, d0, d1, d2) || entity3.riddenByEntity == this.mc.thePlayer;

                    if (flag2)
                    {
                        boolean flag3 = this.mc.getRenderViewEntity() instanceof EntityLivingBase ? ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping() : false;

                        if (entity3 == this.mc.getRenderViewEntity() && this.mc.gameSettings.thirdPersonView == 0 && !flag3 || entity3.posY >= 0.0D && entity3.posY < 256.0D && !this.theWorld.isBlockLoaded(new BlockPos(entity3)))
                        {
                            continue;
                        }

                        ++this.countEntitiesRendered;
                        this.renderManager.renderEntitySimple(entity3, partialTicks);
                    }

                    if (!flag2 && entity3 instanceof EntityWitherSkull)
                    {
                        this.mc.getRenderManager().renderWitherSkull(entity3, partialTicks);
                    }
                }
            }

            this.theWorld.theProfiler.endStartSection("blockentities");
            RenderHelper.enableStandardItemLighting();
            iterator = this.renderInfos.iterator();
            TileEntity tileentity;

            while (iterator.hasNext())
            {
                containerlocalrenderinformation = (RenderGlobal.ContainerLocalRenderInformation)iterator.next();
                Iterator iterator1 = containerlocalrenderinformation.renderChunk.getCompiledChunk().getTileEntities().iterator();

                while (iterator1.hasNext())
                {
                    tileentity = (TileEntity)iterator1.next();
                    if (!tileentity.shouldRenderInPass(pass) || !p_180446_2_.isBoundingBoxInFrustum(tileentity.getRenderBoundingBox())) continue;
                    TileEntityRendererDispatcher.instance.renderTileEntity(tileentity, partialTicks, -1);
                }
            }

            this.preRenderDamagedBlocks();
            iterator = this.damagedBlocks.values().iterator();

            while (iterator.hasNext())
            {
                DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)iterator.next();
                BlockPos blockpos = destroyblockprogress.getPosition();
                tileentity = this.theWorld.getTileEntity(blockpos);

                if (tileentity instanceof TileEntityChest)
                {
                    TileEntityChest tileentitychest = (TileEntityChest)tileentity;

                    if (tileentitychest.adjacentChestXNeg != null)
                    {
                        blockpos = blockpos.offset(EnumFacing.WEST);
                        tileentity = this.theWorld.getTileEntity(blockpos);
                    }
                    else if (tileentitychest.adjacentChestZNeg != null)
                    {
                        blockpos = blockpos.offset(EnumFacing.NORTH);
                        tileentity = this.theWorld.getTileEntity(blockpos);
                    }
                }

                Block block = this.theWorld.getBlockState(blockpos).getBlock();

                if (tileentity != null && tileentity.shouldRenderInPass(pass) && tileentity.canRenderBreaking() && p_180446_2_.isBoundingBoxInFrustum(tileentity.getRenderBoundingBox()))
                {
                    TileEntityRendererDispatcher.instance.renderTileEntity(tileentity, partialTicks, destroyblockprogress.getPartialBlockDamage());
                }
            }

            this.postRenderDamagedBlocks();
            this.mc.entityRenderer.disableLightmap();
            this.mc.mcProfiler.endSection();
        }
    }

    /**
     * Gets the render info for use on the Debug screen
     */
    public String getDebugInfoRenders()
    {
        int i = this.viewFrustum.renderChunks.length;
        int j = 0;
        Iterator iterator = this.renderInfos.iterator();

        while (iterator.hasNext())
        {
            RenderGlobal.ContainerLocalRenderInformation containerlocalrenderinformation = (RenderGlobal.ContainerLocalRenderInformation)iterator.next();
            CompiledChunk compiledchunk = containerlocalrenderinformation.renderChunk.compiledChunk;

            if (compiledchunk != CompiledChunk.DUMMY && !compiledchunk.isEmpty())
            {
                ++j;
            }
        }

        return String.format("C: %d/%d %sD: %d, %s", new Object[] {Integer.valueOf(j), Integer.valueOf(i), this.mc.renderChunksMany ? "(s) " : "", Integer.valueOf(this.renderDistanceChunks), this.renderDispatcher.getDebugInfo()});
    }

    /**
     * Gets the entities info for use on the Debug screen
     */
    public String getDebugInfoEntities()
    {
        return "E: " + this.countEntitiesRendered + "/" + this.countEntitiesTotal + ", B: " + this.countEntitiesHidden + ", I: " + (this.countEntitiesTotal - this.countEntitiesHidden - this.countEntitiesRendered);
    }

    public void setupTerrain(Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator)
    {
        if (this.mc.gameSettings.renderDistanceChunks != this.renderDistanceChunks)
        {
            this.loadRenderers();
        }

        this.theWorld.theProfiler.startSection("camera");
        double d1 = viewEntity.posX - this.frustumUpdatePosX;
        double d2 = viewEntity.posY - this.frustumUpdatePosY;
        double d3 = viewEntity.posZ - this.frustumUpdatePosZ;

        if (this.frustumUpdatePosChunkX != viewEntity.chunkCoordX || this.frustumUpdatePosChunkY != viewEntity.chunkCoordY || this.frustumUpdatePosChunkZ != viewEntity.chunkCoordZ || d1 * d1 + d2 * d2 + d3 * d3 > 16.0D)
        {
            this.frustumUpdatePosX = viewEntity.posX;
            this.frustumUpdatePosY = viewEntity.posY;
            this.frustumUpdatePosZ = viewEntity.posZ;
            this.frustumUpdatePosChunkX = viewEntity.chunkCoordX;
            this.frustumUpdatePosChunkY = viewEntity.chunkCoordY;
            this.frustumUpdatePosChunkZ = viewEntity.chunkCoordZ;
            this.viewFrustum.updateChunkPositions(viewEntity.posX, viewEntity.posZ);
        }

        this.theWorld.theProfiler.endStartSection("renderlistcamera");
        double d4 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
        double d5 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
        double d6 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
        this.renderContainer.initialize(d4, d5, d6);
        this.theWorld.theProfiler.endStartSection("cull");

        if (this.debugFixedClippingHelper != null)
        {
            Frustum frustum = new Frustum(this.debugFixedClippingHelper);
            frustum.setPosition(this.debugTerrainFrustumPosition.x, this.debugTerrainFrustumPosition.y, this.debugTerrainFrustumPosition.z);
            camera = frustum;
        }

        this.mc.mcProfiler.endStartSection("culling");
        BlockPos blockpos1 = new BlockPos(d4, d5 + (double)viewEntity.getEyeHeight(), d6);
        RenderChunk renderchunk = this.viewFrustum.getRenderChunk(blockpos1);
        BlockPos blockpos = new BlockPos(MathHelper.floor_double(d4) / 16 * 16, MathHelper.floor_double(d5) / 16 * 16, MathHelper.floor_double(d6) / 16 * 16);
        this.displayListEntitiesDirty = this.displayListEntitiesDirty || !this.chunksToUpdate.isEmpty() || viewEntity.posX != this.lastViewEntityX || viewEntity.posY != this.lastViewEntityY || viewEntity.posZ != this.lastViewEntityZ || (double)viewEntity.rotationPitch != this.lastViewEntityPitch || (double)viewEntity.rotationYaw != this.lastViewEntityYaw;
        this.lastViewEntityX = viewEntity.posX;
        this.lastViewEntityY = viewEntity.posY;
        this.lastViewEntityZ = viewEntity.posZ;
        this.lastViewEntityPitch = (double)viewEntity.rotationPitch;
        this.lastViewEntityYaw = (double)viewEntity.rotationYaw;
        boolean flag1 = this.debugFixedClippingHelper != null;
        RenderGlobal.ContainerLocalRenderInformation containerlocalrenderinformation1;
        RenderChunk renderchunk3;

        if (!flag1 && this.displayListEntitiesDirty)
        {
            this.displayListEntitiesDirty = false;
            this.renderInfos = Lists.newArrayList();
            LinkedList linkedlist = Lists.newLinkedList();
            boolean flag2 = this.mc.renderChunksMany;

            if (renderchunk == null)
            {
                int j = blockpos1.getY() > 0 ? 248 : 8;

                for (int k = -this.renderDistanceChunks; k <= this.renderDistanceChunks; ++k)
                {
                    for (int l = -this.renderDistanceChunks; l <= this.renderDistanceChunks; ++l)
                    {
                        RenderChunk renderchunk1 = this.viewFrustum.getRenderChunk(new BlockPos((k << 4) + 8, j, (l << 4) + 8));

                        if (renderchunk1 != null && ((ICamera)camera).isBoundingBoxInFrustum(renderchunk1.boundingBox))
                        {
                            renderchunk1.setFrameIndex(frameCount);
                            linkedlist.add(new RenderGlobal.ContainerLocalRenderInformation(renderchunk1, (EnumFacing)null, 0, null));
                        }
                    }
                }
            }
            else
            {
                boolean flag3 = false;
                RenderGlobal.ContainerLocalRenderInformation containerlocalrenderinformation2 = new RenderGlobal.ContainerLocalRenderInformation(renderchunk, (EnumFacing)null, 0, null);
                Set set1 = this.getVisibleFacings(blockpos1);

                if (!set1.isEmpty() && set1.size() == 1)
                {
                    Vector3f vector3f = this.getViewVector(viewEntity, partialTicks);
                    EnumFacing enumfacing = EnumFacing.getFacingFromVector(vector3f.x, vector3f.y, vector3f.z).getOpposite();
                    set1.remove(enumfacing);
                }

                if (set1.isEmpty())
                {
                    flag3 = true;
                }

                if (flag3 && !playerSpectator)
                {
                    this.renderInfos.add(containerlocalrenderinformation2);
                }
                else
                {
                    if (playerSpectator && this.theWorld.getBlockState(blockpos1).getBlock().isOpaqueCube())
                    {
                        flag2 = false;
                    }

                    renderchunk.setFrameIndex(frameCount);
                    linkedlist.add(containerlocalrenderinformation2);
                }
            }

            while (!linkedlist.isEmpty())
            {
                containerlocalrenderinformation1 = (RenderGlobal.ContainerLocalRenderInformation)linkedlist.poll();
                renderchunk3 = containerlocalrenderinformation1.renderChunk;
                EnumFacing enumfacing2 = containerlocalrenderinformation1.facing;
                BlockPos blockpos2 = renderchunk3.getPosition();
                this.renderInfos.add(containerlocalrenderinformation1);
                EnumFacing[] aenumfacing = EnumFacing.values();
                int i1 = aenumfacing.length;

                for (int j1 = 0; j1 < i1; ++j1)
                {
                    EnumFacing enumfacing1 = aenumfacing[j1];
                    RenderChunk renderchunk2 = this.getRenderChunkOffset(blockpos1, blockpos2, enumfacing1);

                    if ((!flag2 || !containerlocalrenderinformation1.setFacing.contains(enumfacing1.getOpposite())) && (!flag2 || enumfacing2 == null || renderchunk3.getCompiledChunk().isVisible(enumfacing2.getOpposite(), enumfacing1)) && renderchunk2 != null && renderchunk2.setFrameIndex(frameCount) && ((ICamera)camera).isBoundingBoxInFrustum(renderchunk2.boundingBox))
                    {
                        RenderGlobal.ContainerLocalRenderInformation containerlocalrenderinformation = new RenderGlobal.ContainerLocalRenderInformation(renderchunk2, enumfacing1, containerlocalrenderinformation1.counter + 1, null);
                        containerlocalrenderinformation.setFacing.addAll(containerlocalrenderinformation1.setFacing);
                        containerlocalrenderinformation.setFacing.add(enumfacing1);
                        linkedlist.add(containerlocalrenderinformation);
                    }
                }
            }
        }

        if (this.debugFixTerrainFrustum)
        {
            this.fixTerrainFrustum(d4, d5, d6);
            this.debugFixTerrainFrustum = false;
        }

        this.renderDispatcher.clearChunkUpdates();
        Set set = this.chunksToUpdate;
        this.chunksToUpdate = Sets.newLinkedHashSet();
        Iterator iterator = this.renderInfos.iterator();

        while (iterator.hasNext())
        {
            containerlocalrenderinformation1 = (RenderGlobal.ContainerLocalRenderInformation)iterator.next();
            renderchunk3 = containerlocalrenderinformation1.renderChunk;

            if (renderchunk3.isNeedsUpdate() || renderchunk3.isCompileTaskPending() || set.contains(renderchunk3))
            {
                this.displayListEntitiesDirty = true;

                if (this.isPositionInRenderChunk(blockpos, containerlocalrenderinformation1.renderChunk))
                {
                    this.mc.mcProfiler.startSection("build near");
                    this.renderDispatcher.updateChunkNow(renderchunk3);
                    renderchunk3.setNeedsUpdate(false);
                    this.mc.mcProfiler.endSection();
                }
                else
                {
                    this.chunksToUpdate.add(renderchunk3);
                }
            }
        }

        this.chunksToUpdate.addAll(set);
        this.mc.mcProfiler.endSection();
    }

    private boolean isPositionInRenderChunk(BlockPos p_174983_1_, RenderChunk p_174983_2_)
    {
        BlockPos blockpos1 = p_174983_2_.getPosition();
        return MathHelper.abs_int(p_174983_1_.getX() - blockpos1.getX()) > 16 ? false : (MathHelper.abs_int(p_174983_1_.getY() - blockpos1.getY()) > 16 ? false : MathHelper.abs_int(p_174983_1_.getZ() - blockpos1.getZ()) <= 16);
    }

    private Set getVisibleFacings(BlockPos p_174978_1_)
    {
        VisGraph visgraph = new VisGraph();
        BlockPos blockpos1 = new BlockPos(p_174978_1_.getX() >> 4 << 4, p_174978_1_.getY() >> 4 << 4, p_174978_1_.getZ() >> 4 << 4);
        Chunk chunk = this.theWorld.getChunkFromBlockCoords(blockpos1);
        Iterator iterator = BlockPos.getAllInBoxMutable(blockpos1, blockpos1.add(15, 15, 15)).iterator();

        while (iterator.hasNext())
        {
            BlockPos.MutableBlockPos mutableblockpos = (BlockPos.MutableBlockPos)iterator.next();

            if (chunk.getBlock(mutableblockpos).isOpaqueCube())
            {
                visgraph.func_178606_a(mutableblockpos);
            }
        }

        return visgraph.func_178609_b(p_174978_1_);
    }

    private RenderChunk getRenderChunkOffset(BlockPos p_174973_1_, BlockPos p_174973_2_, EnumFacing p_174973_3_)
    {
        BlockPos blockpos2 = p_174973_2_.offset(p_174973_3_, 16);
        return MathHelper.abs_int(p_174973_1_.getX() - blockpos2.getX()) > this.renderDistanceChunks * 16 ? null : (blockpos2.getY() >= 0 && blockpos2.getY() < 256 ? (MathHelper.abs_int(p_174973_1_.getZ() - blockpos2.getZ()) > this.renderDistanceChunks * 16 ? null : this.viewFrustum.getRenderChunk(blockpos2)) : null);
    }

    private void fixTerrainFrustum(double p_174984_1_, double p_174984_3_, double p_174984_5_)
    {
        this.debugFixedClippingHelper = new ClippingHelperImpl();
        ((ClippingHelperImpl)this.debugFixedClippingHelper).init();
        Matrix4f matrix4f = new Matrix4f(this.debugFixedClippingHelper.modelviewMatrix);
        matrix4f.transpose();
        Matrix4f matrix4f1 = new Matrix4f(this.debugFixedClippingHelper.projectionMatrix);
        matrix4f1.transpose();
        Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.mul(matrix4f1, matrix4f);
        matrix4f2.invert();
        this.debugTerrainFrustumPosition.x = p_174984_1_;
        this.debugTerrainFrustumPosition.y = p_174984_3_;
        this.debugTerrainFrustumPosition.z = p_174984_5_;
        this.debugTerrainMatrix[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
        this.debugTerrainMatrix[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
        this.debugTerrainMatrix[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
        this.debugTerrainMatrix[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
        this.debugTerrainMatrix[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
        this.debugTerrainMatrix[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
        this.debugTerrainMatrix[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.debugTerrainMatrix[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);

        for (int i = 0; i < 8; ++i)
        {
            matrix4f2.transform(this.debugTerrainMatrix[i]);
            this.debugTerrainMatrix[i].x /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].y /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].z /= this.debugTerrainMatrix[i].w;
            this.debugTerrainMatrix[i].w = 1.0F;
        }
    }

    protected Vector3f getViewVector(Entity entityIn, double partialTicks)
    {
        float f = (float)((double)entityIn.prevRotationPitch + (double)(entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks);
        float f1 = (float)((double)entityIn.prevRotationYaw + (double)(entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks);

        if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 2)
        {
            f += 180.0F;
        }

        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        return new Vector3f(f3 * f4, f5, f2 * f4);
    }

    public int renderBlockLayer(EnumWorldBlockLayer blockLayerIn, double partialTicks, int pass, Entity entityIn)
    {
        RenderHelper.disableStandardItemLighting();

        if (blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT)
        {
            this.mc.mcProfiler.startSection("translucent_sort");
            double d1 = entityIn.posX - this.prevRenderSortX;
            double d2 = entityIn.posY - this.prevRenderSortY;
            double d3 = entityIn.posZ - this.prevRenderSortZ;

            if (d1 * d1 + d2 * d2 + d3 * d3 > 1.0D)
            {
                this.prevRenderSortX = entityIn.posX;
                this.prevRenderSortY = entityIn.posY;
                this.prevRenderSortZ = entityIn.posZ;
                int l = 0;
                Iterator iterator = this.renderInfos.iterator();

                while (iterator.hasNext())
                {
                    RenderGlobal.ContainerLocalRenderInformation containerlocalrenderinformation = (RenderGlobal.ContainerLocalRenderInformation)iterator.next();

                    if (containerlocalrenderinformation.renderChunk.compiledChunk.isLayerStarted(blockLayerIn) && l++ < 15)
                    {
                        this.renderDispatcher.updateTransparencyLater(containerlocalrenderinformation.renderChunk);
                    }
                }
            }

            this.mc.mcProfiler.endSection();
        }

        this.mc.mcProfiler.startSection("filterempty");
        int i1 = 0;
        boolean flag = blockLayerIn == EnumWorldBlockLayer.TRANSLUCENT;
        int j1 = flag ? this.renderInfos.size() - 1 : 0;
        int j = flag ? -1 : this.renderInfos.size();
        int k1 = flag ? -1 : 1;

        for (int k = j1; k != j; k += k1)
        {
            RenderChunk renderchunk = ((RenderGlobal.ContainerLocalRenderInformation)this.renderInfos.get(k)).renderChunk;

            if (!renderchunk.getCompiledChunk().isLayerEmpty(blockLayerIn))
            {
                ++i1;
                this.renderContainer.addRenderChunk(renderchunk, blockLayerIn);
            }
        }

        this.mc.mcProfiler.endStartSection("render_" + blockLayerIn);
        this.renderBlockLayer(blockLayerIn);
        this.mc.mcProfiler.endSection();
        return i1;
    }

    private void renderBlockLayer(EnumWorldBlockLayer blockLayerIn)
    {
        this.mc.entityRenderer.enableLightmap();

        if (OpenGlHelper.useVbo())
        {
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
            GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        }

        this.renderContainer.renderChunkLayer(blockLayerIn);

        if (OpenGlHelper.useVbo())
        {
            List list = DefaultVertexFormats.BLOCK.getElements();
            Iterator iterator = list.iterator();

            while (iterator.hasNext())
            {
                VertexFormatElement vertexformatelement = (VertexFormatElement)iterator.next();
                VertexFormatElement.EnumUsage enumusage = vertexformatelement.getUsage();
                int i = vertexformatelement.getIndex();

                switch (RenderGlobal.SwitchEnumUsage.VALUES[enumusage.ordinal()])
                {
                    case 1:
                        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
                        break;
                    case 2:
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit + i);
                        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
                        OpenGlHelper.setClientActiveTexture(OpenGlHelper.defaultTexUnit);
                        break;
                    case 3:
                        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
                        GlStateManager.resetColor();
                }
            }
        }

        this.mc.entityRenderer.disableLightmap();
    }

    private void cleanupDamagedBlocks(Iterator p_174965_1_)
    {
        while (p_174965_1_.hasNext())
        {
            DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)p_174965_1_.next();
            int i = destroyblockprogress.getCreationCloudUpdateTick();

            if (this.cloudTickCounter - i > 400)
            {
                p_174965_1_.remove();
            }
        }
    }

    public void updateClouds()
    {
        ++this.cloudTickCounter;

        if (this.cloudTickCounter % 20 == 0)
        {
            this.cleanupDamagedBlocks(this.damagedBlocks.values().iterator());
        }
    }

    private void renderSkyEnd()
    {
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.depthMask(false);
        this.renderEngine.bindTexture(locationEndSkyPng);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        for (int i = 0; i < 6; ++i)
        {
            GlStateManager.pushMatrix();

            if (i == 1)
            {
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (i == 2)
            {
                GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            }

            if (i == 3)
            {
                GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            }

            if (i == 4)
            {
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
            }

            if (i == 5)
            {
                GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
            }

            worldrenderer.startDrawingQuads();
            worldrenderer.setColorOpaque_I(2631720);
            worldrenderer.addVertexWithUV(-100.0D, -100.0D, -100.0D, 0.0D, 0.0D);
            worldrenderer.addVertexWithUV(-100.0D, -100.0D, 100.0D, 0.0D, 16.0D);
            worldrenderer.addVertexWithUV(100.0D, -100.0D, 100.0D, 16.0D, 16.0D);
            worldrenderer.addVertexWithUV(100.0D, -100.0D, -100.0D, 16.0D, 0.0D);
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }

    public void renderSky(float partialTicks, int pass)
    {
        net.minecraftforge.client.IRenderHandler renderer = this.theWorld.provider.getSkyRenderer();
        if (renderer != null)
        {
            renderer.render(partialTicks, theWorld, mc);
            return;
        }
        if (this.mc.theWorld.provider.getDimensionId() == 1)
        {
            this.renderSkyEnd();
        }
        else if (this.mc.theWorld.provider.isSurfaceWorld())
        {
            GlStateManager.disableTexture2D();
            Vec3 vec3 = this.theWorld.getSkyColor(this.mc.getRenderViewEntity(), partialTicks);
            float f1 = (float)vec3.xCoord;
            float f2 = (float)vec3.yCoord;
            float f3 = (float)vec3.zCoord;

            if (pass != 2)
            {
                float f4 = (f1 * 30.0F + f2 * 59.0F + f3 * 11.0F) / 100.0F;
                float f5 = (f1 * 30.0F + f2 * 70.0F) / 100.0F;
                float f6 = (f1 * 30.0F + f3 * 70.0F) / 100.0F;
                f1 = f4;
                f2 = f5;
                f3 = f6;
            }

            GlStateManager.color(f1, f2, f3);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.depthMask(false);
            GlStateManager.enableFog();
            GlStateManager.color(f1, f2, f3);

            if (this.vboEnabled)
            {
                this.skyVBO.bindBuffer();
                GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
                GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
                this.skyVBO.drawArrays(7);
                this.skyVBO.unbindBuffer();
                GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
            }
            else
            {
                GlStateManager.callList(this.glSkyList);
            }

            GlStateManager.disableFog();
            GlStateManager.disableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.disableStandardItemLighting();
            float[] afloat = this.theWorld.provider.calcSunriseSunsetColors(this.theWorld.getCelestialAngle(partialTicks), partialTicks);
            float f7;
            float f8;
            float f9;
            float f10;
            float f11;

            if (afloat != null)
            {
                GlStateManager.disableTexture2D();
                GlStateManager.shadeModel(7425);
                GlStateManager.pushMatrix();
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(MathHelper.sin(this.theWorld.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
                GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
                f7 = afloat[0];
                f8 = afloat[1];
                f9 = afloat[2];
                float f12;

                if (pass != 2)
                {
                    f10 = (f7 * 30.0F + f8 * 59.0F + f9 * 11.0F) / 100.0F;
                    f11 = (f7 * 30.0F + f8 * 70.0F) / 100.0F;
                    f12 = (f7 * 30.0F + f9 * 70.0F) / 100.0F;
                    f7 = f10;
                    f8 = f11;
                    f9 = f12;
                }

                worldrenderer.startDrawing(6);
                worldrenderer.setColorRGBA_F(f7, f8, f9, afloat[3]);
                worldrenderer.addVertex(0.0D, 100.0D, 0.0D);
                boolean flag = true;
                worldrenderer.setColorRGBA_F(afloat[0], afloat[1], afloat[2], 0.0F);

                for (int j = 0; j <= 16; ++j)
                {
                    f12 = (float)j * (float)Math.PI * 2.0F / 16.0F;
                    float f13 = MathHelper.sin(f12);
                    float f14 = MathHelper.cos(f12);
                    worldrenderer.addVertex((double)(f13 * 120.0F), (double)(f14 * 120.0F), (double)(-f14 * 40.0F * afloat[3]));
                }

                tessellator.draw();
                GlStateManager.popMatrix();
                GlStateManager.shadeModel(7424);
            }

            GlStateManager.enableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            GlStateManager.pushMatrix();
            f7 = 1.0F - this.theWorld.getRainStrength(partialTicks);
            f8 = 0.0F;
            f9 = 0.0F;
            f10 = 0.0F;
            GlStateManager.color(1.0F, 1.0F, 1.0F, f7);
            GlStateManager.translate(0.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.theWorld.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
            f11 = 30.0F;
            this.renderEngine.bindTexture(locationSunPng);
            worldrenderer.startDrawingQuads();
            worldrenderer.addVertexWithUV((double)(-f11), 100.0D, (double)(-f11), 0.0D, 0.0D);
            worldrenderer.addVertexWithUV((double)f11, 100.0D, (double)(-f11), 1.0D, 0.0D);
            worldrenderer.addVertexWithUV((double)f11, 100.0D, (double)f11, 1.0D, 1.0D);
            worldrenderer.addVertexWithUV((double)(-f11), 100.0D, (double)f11, 0.0D, 1.0D);
            tessellator.draw();
            f11 = 20.0F;
            this.renderEngine.bindTexture(locationMoonPhasesPng);
            int k = this.theWorld.getMoonPhase();
            int l = k % 4;
            int i1 = k / 4 % 2;
            float f15 = (float)(l + 0) / 4.0F;
            float f16 = (float)(i1 + 0) / 2.0F;
            float f17 = (float)(l + 1) / 4.0F;
            float f18 = (float)(i1 + 1) / 2.0F;
            worldrenderer.startDrawingQuads();
            worldrenderer.addVertexWithUV((double)(-f11), -100.0D, (double)f11, (double)f17, (double)f18);
            worldrenderer.addVertexWithUV((double)f11, -100.0D, (double)f11, (double)f15, (double)f18);
            worldrenderer.addVertexWithUV((double)f11, -100.0D, (double)(-f11), (double)f15, (double)f16);
            worldrenderer.addVertexWithUV((double)(-f11), -100.0D, (double)(-f11), (double)f17, (double)f16);
            tessellator.draw();
            GlStateManager.disableTexture2D();
            float f19 = this.theWorld.getStarBrightness(partialTicks) * f7;

            if (f19 > 0.0F)
            {
                GlStateManager.color(f19, f19, f19, f19);

                if (this.vboEnabled)
                {
                    this.starVBO.bindBuffer();
                    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
                    GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
                    this.starVBO.drawArrays(7);
                    this.starVBO.unbindBuffer();
                    GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
                }
                else
                {
                    GlStateManager.callList(this.starGLCallList);
                }
            }

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableFog();
            GlStateManager.popMatrix();
            GlStateManager.disableTexture2D();
            GlStateManager.color(0.0F, 0.0F, 0.0F);
            double d0 = this.mc.thePlayer.getPositionEyes(partialTicks).yCoord - this.theWorld.getHorizon();

            if (d0 < 0.0D)
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0F, 12.0F, 0.0F);

                if (this.vboEnabled)
                {
                    this.sky2VBO.bindBuffer();
                    GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
                    GL11.glVertexPointer(3, GL11.GL_FLOAT, 12, 0L);
                    this.sky2VBO.drawArrays(7);
                    this.sky2VBO.unbindBuffer();
                    GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
                }
                else
                {
                    GlStateManager.callList(this.glSkyList2);
                }

                GlStateManager.popMatrix();
                f9 = 1.0F;
                f10 = -((float)(d0 + 65.0D));
                f11 = -1.0F;
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_I(0, 255);
                worldrenderer.addVertex(-1.0D, (double)f10, 1.0D);
                worldrenderer.addVertex(1.0D, (double)f10, 1.0D);
                worldrenderer.addVertex(1.0D, -1.0D, 1.0D);
                worldrenderer.addVertex(-1.0D, -1.0D, 1.0D);
                worldrenderer.addVertex(-1.0D, -1.0D, -1.0D);
                worldrenderer.addVertex(1.0D, -1.0D, -1.0D);
                worldrenderer.addVertex(1.0D, (double)f10, -1.0D);
                worldrenderer.addVertex(-1.0D, (double)f10, -1.0D);
                worldrenderer.addVertex(1.0D, -1.0D, -1.0D);
                worldrenderer.addVertex(1.0D, -1.0D, 1.0D);
                worldrenderer.addVertex(1.0D, (double)f10, 1.0D);
                worldrenderer.addVertex(1.0D, (double)f10, -1.0D);
                worldrenderer.addVertex(-1.0D, (double)f10, -1.0D);
                worldrenderer.addVertex(-1.0D, (double)f10, 1.0D);
                worldrenderer.addVertex(-1.0D, -1.0D, 1.0D);
                worldrenderer.addVertex(-1.0D, -1.0D, -1.0D);
                worldrenderer.addVertex(-1.0D, -1.0D, -1.0D);
                worldrenderer.addVertex(-1.0D, -1.0D, 1.0D);
                worldrenderer.addVertex(1.0D, -1.0D, 1.0D);
                worldrenderer.addVertex(1.0D, -1.0D, -1.0D);
                tessellator.draw();
            }

            if (this.theWorld.provider.isSkyColored())
            {
                GlStateManager.color(f1 * 0.2F + 0.04F, f2 * 0.2F + 0.04F, f3 * 0.6F + 0.1F);
            }
            else
            {
                GlStateManager.color(f1, f2, f3);
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, -((float)(d0 - 16.0D)), 0.0F);
            GlStateManager.callList(this.glSkyList2);
            GlStateManager.popMatrix();
            GlStateManager.enableTexture2D();
            GlStateManager.depthMask(true);
        }
    }

    public void renderClouds(float p_180447_1_, int p_180447_2_)
    {
        net.minecraftforge.client.IRenderHandler renderer = this.mc.theWorld.provider.getCloudRenderer();
        if (renderer != null)
        {
            renderer.render(p_180447_1_, this.mc.theWorld, mc);
            return;
        }
        if (this.mc.theWorld.provider.isSurfaceWorld())
        {
            if (this.mc.gameSettings.fancyGraphics)
            {
                this.renderCloudsFancy(p_180447_1_, p_180447_2_);
            }
            else
            {
                GlStateManager.disableCull();
                float f1 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * (double)p_180447_1_);
                boolean flag = true;
                boolean flag1 = true;
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                this.renderEngine.bindTexture(locationCloudsPng);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                Vec3 vec3 = this.theWorld.getCloudColour(p_180447_1_);
                float f2 = (float)vec3.xCoord;
                float f3 = (float)vec3.yCoord;
                float f4 = (float)vec3.zCoord;
                float f5;

                if (p_180447_2_ != 2)
                {
                    f5 = (f2 * 30.0F + f3 * 59.0F + f4 * 11.0F) / 100.0F;
                    float f6 = (f2 * 30.0F + f3 * 70.0F) / 100.0F;
                    float f7 = (f2 * 30.0F + f4 * 70.0F) / 100.0F;
                    f2 = f5;
                    f3 = f6;
                    f4 = f7;
                }

                f5 = 4.8828125E-4F;
                double d2 = (double)((float)this.cloudTickCounter + p_180447_1_);
                double d0 = this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * (double)p_180447_1_ + d2 * 0.029999999329447746D;
                double d1 = this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * (double)p_180447_1_;
                int j = MathHelper.floor_double(d0 / 2048.0D);
                int k = MathHelper.floor_double(d1 / 2048.0D);
                d0 -= (double)(j * 2048);
                d1 -= (double)(k * 2048);
                float f8 = this.theWorld.provider.getCloudHeight() - f1 + 0.33F;
                float f9 = (float)(d0 * 4.8828125E-4D);
                float f10 = (float)(d1 * 4.8828125E-4D);
                worldrenderer.startDrawingQuads();
                worldrenderer.setColorRGBA_F(f2, f3, f4, 0.8F);

                for (int l = -256; l < 256; l += 32)
                {
                    for (int i1 = -256; i1 < 256; i1 += 32)
                    {
                        worldrenderer.addVertexWithUV((double)(l + 0), (double)f8, (double)(i1 + 32), (double)((float)(l + 0) * 4.8828125E-4F + f9), (double)((float)(i1 + 32) * 4.8828125E-4F + f10));
                        worldrenderer.addVertexWithUV((double)(l + 32), (double)f8, (double)(i1 + 32), (double)((float)(l + 32) * 4.8828125E-4F + f9), (double)((float)(i1 + 32) * 4.8828125E-4F + f10));
                        worldrenderer.addVertexWithUV((double)(l + 32), (double)f8, (double)(i1 + 0), (double)((float)(l + 32) * 4.8828125E-4F + f9), (double)((float)(i1 + 0) * 4.8828125E-4F + f10));
                        worldrenderer.addVertexWithUV((double)(l + 0), (double)f8, (double)(i1 + 0), (double)((float)(l + 0) * 4.8828125E-4F + f9), (double)((float)(i1 + 0) * 4.8828125E-4F + f10));
                    }
                }

                tessellator.draw();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableBlend();
                GlStateManager.enableCull();
            }
        }
    }

    /**
     * Checks if the given position is to be rendered with cloud fog
     */
    public boolean hasCloudFog(double p_72721_1_, double p_72721_3_, double p_72721_5_, float p_72721_7_)
    {
        return false;
    }

    private void renderCloudsFancy(float p_180445_1_, int p_180445_2_)
    {
        GlStateManager.disableCull();
        float f1 = (float)(this.mc.getRenderViewEntity().lastTickPosY + (this.mc.getRenderViewEntity().posY - this.mc.getRenderViewEntity().lastTickPosY) * (double)p_180445_1_);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f2 = 12.0F;
        float f3 = 4.0F;
        double d0 = (double)((float)this.cloudTickCounter + p_180445_1_);
        double d1 = (this.mc.getRenderViewEntity().prevPosX + (this.mc.getRenderViewEntity().posX - this.mc.getRenderViewEntity().prevPosX) * (double)p_180445_1_ + d0 * 0.029999999329447746D) / 12.0D;
        double d2 = (this.mc.getRenderViewEntity().prevPosZ + (this.mc.getRenderViewEntity().posZ - this.mc.getRenderViewEntity().prevPosZ) * (double)p_180445_1_) / 12.0D + 0.33000001311302185D;
        float f4 = this.theWorld.provider.getCloudHeight() - f1 + 0.33F;
        int j = MathHelper.floor_double(d1 / 2048.0D);
        int k = MathHelper.floor_double(d2 / 2048.0D);
        d1 -= (double)(j * 2048);
        d2 -= (double)(k * 2048);
        this.renderEngine.bindTexture(locationCloudsPng);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Vec3 vec3 = this.theWorld.getCloudColour(p_180445_1_);
        float f5 = (float)vec3.xCoord;
        float f6 = (float)vec3.yCoord;
        float f7 = (float)vec3.zCoord;
        float f8;
        float f9;
        float f10;

        if (p_180445_2_ != 2)
        {
            f8 = (f5 * 30.0F + f6 * 59.0F + f7 * 11.0F) / 100.0F;
            f9 = (f5 * 30.0F + f6 * 70.0F) / 100.0F;
            f10 = (f5 * 30.0F + f7 * 70.0F) / 100.0F;
            f5 = f8;
            f6 = f9;
            f7 = f10;
        }

        f8 = 0.00390625F;
        f9 = (float)MathHelper.floor_double(d1) * 0.00390625F;
        f10 = (float)MathHelper.floor_double(d2) * 0.00390625F;
        float f11 = (float)(d1 - (double)MathHelper.floor_double(d1));
        float f12 = (float)(d2 - (double)MathHelper.floor_double(d2));
        boolean flag = true;
        boolean flag1 = true;
        float f13 = 9.765625E-4F;
        GlStateManager.scale(12.0F, 1.0F, 12.0F);

        for (int l = 0; l < 2; ++l)
        {
            if (l == 0)
            {
                GlStateManager.colorMask(false, false, false, false);
            }
            else
            {
                switch (p_180445_2_)
                {
                    case 0:
                        GlStateManager.colorMask(false, true, true, true);
                        break;
                    case 1:
                        GlStateManager.colorMask(true, false, false, true);
                        break;
                    case 2:
                        GlStateManager.colorMask(true, true, true, true);
                }
            }

            for (int i1 = -3; i1 <= 4; ++i1)
            {
                for (int j1 = -3; j1 <= 4; ++j1)
                {
                    worldrenderer.startDrawingQuads();
                    float f14 = (float)(i1 * 8);
                    float f15 = (float)(j1 * 8);
                    float f16 = f14 - f11;
                    float f17 = f15 - f12;

                    if (f4 > -5.0F)
                    {
                        worldrenderer.setColorRGBA_F(f5 * 0.7F, f6 * 0.7F, f7 * 0.7F, 0.8F);
                        worldrenderer.setNormal(0.0F, -1.0F, 0.0F);
                        worldrenderer.addVertexWithUV((double)(f16 + 0.0F), (double)(f4 + 0.0F), (double)(f17 + 8.0F), (double)((f14 + 0.0F) * 0.00390625F + f9), (double)((f15 + 8.0F) * 0.00390625F + f10));
                        worldrenderer.addVertexWithUV((double)(f16 + 8.0F), (double)(f4 + 0.0F), (double)(f17 + 8.0F), (double)((f14 + 8.0F) * 0.00390625F + f9), (double)((f15 + 8.0F) * 0.00390625F + f10));
                        worldrenderer.addVertexWithUV((double)(f16 + 8.0F), (double)(f4 + 0.0F), (double)(f17 + 0.0F), (double)((f14 + 8.0F) * 0.00390625F + f9), (double)((f15 + 0.0F) * 0.00390625F + f10));
                        worldrenderer.addVertexWithUV((double)(f16 + 0.0F), (double)(f4 + 0.0F), (double)(f17 + 0.0F), (double)((f14 + 0.0F) * 0.00390625F + f9), (double)((f15 + 0.0F) * 0.00390625F + f10));
                    }

                    if (f4 <= 5.0F)
                    {
                        worldrenderer.setColorRGBA_F(f5, f6, f7, 0.8F);
                        worldrenderer.setNormal(0.0F, 1.0F, 0.0F);
                        worldrenderer.addVertexWithUV((double)(f16 + 0.0F), (double)(f4 + 4.0F - 9.765625E-4F), (double)(f17 + 8.0F), (double)((f14 + 0.0F) * 0.00390625F + f9), (double)((f15 + 8.0F) * 0.00390625F + f10));
                        worldrenderer.addVertexWithUV((double)(f16 + 8.0F), (double)(f4 + 4.0F - 9.765625E-4F), (double)(f17 + 8.0F), (double)((f14 + 8.0F) * 0.00390625F + f9), (double)((f15 + 8.0F) * 0.00390625F + f10));
                        worldrenderer.addVertexWithUV((double)(f16 + 8.0F), (double)(f4 + 4.0F - 9.765625E-4F), (double)(f17 + 0.0F), (double)((f14 + 8.0F) * 0.00390625F + f9), (double)((f15 + 0.0F) * 0.00390625F + f10));
                        worldrenderer.addVertexWithUV((double)(f16 + 0.0F), (double)(f4 + 4.0F - 9.765625E-4F), (double)(f17 + 0.0F), (double)((f14 + 0.0F) * 0.00390625F + f9), (double)((f15 + 0.0F) * 0.00390625F + f10));
                    }

                    worldrenderer.setColorRGBA_F(f5 * 0.9F, f6 * 0.9F, f7 * 0.9F, 0.8F);
                    int k1;

                    if (i1 > -1)
                    {
                        worldrenderer.setNormal(-1.0F, 0.0F, 0.0F);

                        for (k1 = 0; k1 < 8; ++k1)
                        {
                            worldrenderer.addVertexWithUV((double)(f16 + (float)k1 + 0.0F), (double)(f4 + 0.0F), (double)(f17 + 8.0F), (double)((f14 + (float)k1 + 0.5F) * 0.00390625F + f9), (double)((f15 + 8.0F) * 0.00390625F + f10));
                            worldrenderer.addVertexWithUV((double)(f16 + (float)k1 + 0.0F), (double)(f4 + 4.0F), (double)(f17 + 8.0F), (double)((f14 + (float)k1 + 0.5F) * 0.00390625F + f9), (double)((f15 + 8.0F) * 0.00390625F + f10));
                            worldrenderer.addVertexWithUV((double)(f16 + (float)k1 + 0.0F), (double)(f4 + 4.0F), (double)(f17 + 0.0F), (double)((f14 + (float)k1 + 0.5F) * 0.00390625F + f9), (double)((f15 + 0.0F) * 0.00390625F + f10));
                            worldrenderer.addVertexWithUV((double)(f16 + (float)k1 + 0.0F), (double)(f4 + 0.0F), (double)(f17 + 0.0F), (double)((f14 + (float)k1 + 0.5F) * 0.00390625F + f9), (double)((f15 + 0.0F) * 0.00390625F + f10));
                        }
                    }

                    if (i1 <= 1)
                    {
                        worldrenderer.setNormal(1.0F, 0.0F, 0.0F);

                        for (k1 = 0; k1 < 8; ++k1)
                        {
                            worldrenderer.addVertexWithUV((double)(f16 + (float)k1 + 1.0F - 9.765625E-4F), (double)(f4 + 0.0F), (double)(f17 + 8.0F), (double)((f14 + (float)k1 + 0.5F) * 0.00390625F + f9), (double)((f15 + 8.0F) * 0.00390625F + f10));
                            worldrenderer.addVertexWithUV((double)(f16 + (float)k1 + 1.0F - 9.765625E-4F), (double)(f4 + 4.0F), (double)(f17 + 8.0F), (double)((f14 + (float)k1 + 0.5F) * 0.00390625F + f9), (double)((f15 + 8.0F) * 0.00390625F + f10));
                            worldrenderer.addVertexWithUV((double)(f16 + (float)k1 + 1.0F - 9.765625E-4F), (double)(f4 + 4.0F), (double)(f17 + 0.0F), (double)((f14 + (float)k1 + 0.5F) * 0.00390625F + f9), (double)((f15 + 0.0F) * 0.00390625F + f10));
                            worldrenderer.addVertexWithUV((double)(f16 + (float)k1 + 1.0F - 9.765625E-4F), (double)(f4 + 0.0F), (double)(f17 + 0.0F), (double)((f14 + (float)k1 + 0.5F) * 0.00390625F + f9), (double)((f15 + 0.0F) * 0.00390625F + f10));
                        }
                    }

                    worldrenderer.setColorRGBA_F(f5 * 0.8F, f6 * 0.8F, f7 * 0.8F, 0.8F);

                    if (j1 > -1)
                    {
                        worldrenderer.setNormal(0.0F, 0.0F, -1.0F);

                        for (k1 = 0; k1 < 8; ++k1)
                        {
                            worldrenderer.addVertexWithUV((double)(f16 + 0.0F), (double)(f4 + 4.0F), (double)(f17 + (float)k1 + 0.0F), (double)((f14 + 0.0F) * 0.00390625F + f9), (double)((f15 + (float)k1 + 0.5F) * 0.00390625F + f10));
                            worldrenderer.addVertexWithUV((double)(f16 + 8.0F), (double)(f4 + 4.0F), (double)(f17 + (float)k1 + 0.0F), (double)((f14 + 8.0F) * 0.00390625F + f9), (double)((f15 + (float)k1 + 0.5F) * 0.00390625F + f10));
                            worldrenderer.addVertexWithUV((double)(f16 + 8.0F), (double)(f4 + 0.0F), (double)(f17 + (float)k1 + 0.0F), (double)((f14 + 8.0F) * 0.00390625F + f9), (double)((f15 + (float)k1 + 0.5F) * 0.00390625F + f10));
                            worldrenderer.addVertexWithUV((double)(f16 + 0.0F), (double)(f4 + 0.0F), (double)(f17 + (float)k1 + 0.0F), (double)((f14 + 0.0F) * 0.00390625F + f9), (double)((f15 + (float)k1 + 0.5F) * 0.00390625F + f10));
                        }
                    }

                    if (j1 <= 1)
                    {
                        worldrenderer.setNormal(0.0F, 0.0F, 1.0F);

                        for (k1 = 0; k1 < 8; ++k1)
                        {
                            worldrenderer.addVertexWithUV((double)(f16 + 0.0F), (double)(f4 + 4.0F), (double)(f17 + (float)k1 + 1.0F - 9.765625E-4F), (double)((f14 + 0.0F) * 0.00390625F + f9), (double)((f15 + (float)k1 + 0.5F) * 0.00390625F + f10));
                            worldrenderer.addVertexWithUV((double)(f16 + 8.0F), (double)(f4 + 4.0F), (double)(f17 + (float)k1 + 1.0F - 9.765625E-4F), (double)((f14 + 8.0F) * 0.00390625F + f9), (double)((f15 + (float)k1 + 0.5F) * 0.00390625F + f10));
                            worldrenderer.addVertexWithUV((double)(f16 + 8.0F), (double)(f4 + 0.0F), (double)(f17 + (float)k1 + 1.0F - 9.765625E-4F), (double)((f14 + 8.0F) * 0.00390625F + f9), (double)((f15 + (float)k1 + 0.5F) * 0.00390625F + f10));
                            worldrenderer.addVertexWithUV((double)(f16 + 0.0F), (double)(f4 + 0.0F), (double)(f17 + (float)k1 + 1.0F - 9.765625E-4F), (double)((f14 + 0.0F) * 0.00390625F + f9), (double)((f15 + (float)k1 + 0.5F) * 0.00390625F + f10));
                        }
                    }

                    tessellator.draw();
                }
            }
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }

    public void updateChunks(long p_174967_1_)
    {
        this.displayListEntitiesDirty |= this.renderDispatcher.runChunkUploads(p_174967_1_);
        Iterator iterator = this.chunksToUpdate.iterator();

        while (iterator.hasNext())
        {
            RenderChunk renderchunk = (RenderChunk)iterator.next();

            if (!this.renderDispatcher.updateChunkLater(renderchunk))
            {
                break;
            }

            renderchunk.setNeedsUpdate(false);
            iterator.remove();
        }
    }

    public void renderWorldBorder(Entity p_180449_1_, float p_180449_2_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        WorldBorder worldborder = this.theWorld.getWorldBorder();
        double d0 = (double)(this.mc.gameSettings.renderDistanceChunks * 16);

        if (p_180449_1_.posX >= worldborder.maxX() - d0 || p_180449_1_.posX <= worldborder.minX() + d0 || p_180449_1_.posZ >= worldborder.maxZ() - d0 || p_180449_1_.posZ <= worldborder.minZ() + d0)
        {
            double d1 = 1.0D - worldborder.getClosestDistance(p_180449_1_) / d0;
            d1 = Math.pow(d1, 4.0D);
            double d2 = p_180449_1_.lastTickPosX + (p_180449_1_.posX - p_180449_1_.lastTickPosX) * (double)p_180449_2_;
            double d3 = p_180449_1_.lastTickPosY + (p_180449_1_.posY - p_180449_1_.lastTickPosY) * (double)p_180449_2_;
            double d4 = p_180449_1_.lastTickPosZ + (p_180449_1_.posZ - p_180449_1_.lastTickPosZ) * (double)p_180449_2_;
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            this.renderEngine.bindTexture(locationForcefieldPng);
            GlStateManager.depthMask(false);
            GlStateManager.pushMatrix();
            int i = worldborder.getStatus().getID();
            float f1 = (float)(i >> 16 & 255) / 255.0F;
            float f2 = (float)(i >> 8 & 255) / 255.0F;
            float f3 = (float)(i & 255) / 255.0F;
            GlStateManager.color(f1, f2, f3, (float)d1);
            GlStateManager.doPolygonOffset(-3.0F, -3.0F);
            GlStateManager.enablePolygonOffset();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableAlpha();
            GlStateManager.disableCull();
            float f4 = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F;
            float f5 = 0.0F;
            float f6 = 0.0F;
            float f7 = 128.0F;
            worldrenderer.startDrawingQuads();
            worldrenderer.setTranslation(-d2, -d3, -d4);
            worldrenderer.markDirty();
            double d5 = Math.max((double)MathHelper.floor_double(d4 - d0), worldborder.minZ());
            double d6 = Math.min((double)MathHelper.ceiling_double_int(d4 + d0), worldborder.maxZ());
            float f8;
            double d7;
            double d8;
            float f9;

            if (d2 > worldborder.maxX() - d0)
            {
                f8 = 0.0F;

                for (d7 = d5; d7 < d6; f8 += 0.5F)
                {
                    d8 = Math.min(1.0D, d6 - d7);
                    f9 = (float)d8 * 0.5F;
                    worldrenderer.addVertexWithUV(worldborder.maxX(), 256.0D, d7, (double)(f4 + f8), (double)(f4 + 0.0F));
                    worldrenderer.addVertexWithUV(worldborder.maxX(), 256.0D, d7 + d8, (double)(f4 + f9 + f8), (double)(f4 + 0.0F));
                    worldrenderer.addVertexWithUV(worldborder.maxX(), 0.0D, d7 + d8, (double)(f4 + f9 + f8), (double)(f4 + 128.0F));
                    worldrenderer.addVertexWithUV(worldborder.maxX(), 0.0D, d7, (double)(f4 + f8), (double)(f4 + 128.0F));
                    ++d7;
                }
            }

            if (d2 < worldborder.minX() + d0)
            {
                f8 = 0.0F;

                for (d7 = d5; d7 < d6; f8 += 0.5F)
                {
                    d8 = Math.min(1.0D, d6 - d7);
                    f9 = (float)d8 * 0.5F;
                    worldrenderer.addVertexWithUV(worldborder.minX(), 256.0D, d7, (double)(f4 + f8), (double)(f4 + 0.0F));
                    worldrenderer.addVertexWithUV(worldborder.minX(), 256.0D, d7 + d8, (double)(f4 + f9 + f8), (double)(f4 + 0.0F));
                    worldrenderer.addVertexWithUV(worldborder.minX(), 0.0D, d7 + d8, (double)(f4 + f9 + f8), (double)(f4 + 128.0F));
                    worldrenderer.addVertexWithUV(worldborder.minX(), 0.0D, d7, (double)(f4 + f8), (double)(f4 + 128.0F));
                    ++d7;
                }
            }

            d5 = Math.max((double)MathHelper.floor_double(d2 - d0), worldborder.minX());
            d6 = Math.min((double)MathHelper.ceiling_double_int(d2 + d0), worldborder.maxX());

            if (d4 > worldborder.maxZ() - d0)
            {
                f8 = 0.0F;

                for (d7 = d5; d7 < d6; f8 += 0.5F)
                {
                    d8 = Math.min(1.0D, d6 - d7);
                    f9 = (float)d8 * 0.5F;
                    worldrenderer.addVertexWithUV(d7, 256.0D, worldborder.maxZ(), (double)(f4 + f8), (double)(f4 + 0.0F));
                    worldrenderer.addVertexWithUV(d7 + d8, 256.0D, worldborder.maxZ(), (double)(f4 + f9 + f8), (double)(f4 + 0.0F));
                    worldrenderer.addVertexWithUV(d7 + d8, 0.0D, worldborder.maxZ(), (double)(f4 + f9 + f8), (double)(f4 + 128.0F));
                    worldrenderer.addVertexWithUV(d7, 0.0D, worldborder.maxZ(), (double)(f4 + f8), (double)(f4 + 128.0F));
                    ++d7;
                }
            }

            if (d4 < worldborder.minZ() + d0)
            {
                f8 = 0.0F;

                for (d7 = d5; d7 < d6; f8 += 0.5F)
                {
                    d8 = Math.min(1.0D, d6 - d7);
                    f9 = (float)d8 * 0.5F;
                    worldrenderer.addVertexWithUV(d7, 256.0D, worldborder.minZ(), (double)(f4 + f8), (double)(f4 + 0.0F));
                    worldrenderer.addVertexWithUV(d7 + d8, 256.0D, worldborder.minZ(), (double)(f4 + f9 + f8), (double)(f4 + 0.0F));
                    worldrenderer.addVertexWithUV(d7 + d8, 0.0D, worldborder.minZ(), (double)(f4 + f9 + f8), (double)(f4 + 128.0F));
                    worldrenderer.addVertexWithUV(d7, 0.0D, worldborder.minZ(), (double)(f4 + f8), (double)(f4 + 128.0F));
                    ++d7;
                }
            }

            tessellator.draw();
            worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
            GlStateManager.enableCull();
            GlStateManager.disableAlpha();
            GlStateManager.doPolygonOffset(0.0F, 0.0F);
            GlStateManager.disablePolygonOffset();
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            GlStateManager.depthMask(true);
        }
    }

    private void preRenderDamagedBlocks()
    {
        GlStateManager.tryBlendFuncSeparate(774, 768, 1, 0);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.5F);
        GlStateManager.doPolygonOffset(-3.0F, -3.0F);
        GlStateManager.enablePolygonOffset();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
    }

    private void postRenderDamagedBlocks()
    {
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset(0.0F, 0.0F);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
    }

    public void drawBlockDamageTexture(Tessellator p_174981_1_, WorldRenderer p_174981_2_, Entity p_174981_3_, float p_174981_4_)
    {
        double d0 = p_174981_3_.lastTickPosX + (p_174981_3_.posX - p_174981_3_.lastTickPosX) * (double)p_174981_4_;
        double d1 = p_174981_3_.lastTickPosY + (p_174981_3_.posY - p_174981_3_.lastTickPosY) * (double)p_174981_4_;
        double d2 = p_174981_3_.lastTickPosZ + (p_174981_3_.posZ - p_174981_3_.lastTickPosZ) * (double)p_174981_4_;

        if (!this.damagedBlocks.isEmpty())
        {
            this.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            this.preRenderDamagedBlocks();
            p_174981_2_.startDrawingQuads();
            p_174981_2_.setVertexFormat(DefaultVertexFormats.BLOCK);
            p_174981_2_.setTranslation(-d0, -d1, -d2);
            p_174981_2_.markDirty();
            Iterator iterator = this.damagedBlocks.values().iterator();

            while (iterator.hasNext())
            {
                DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)iterator.next();
                BlockPos blockpos = destroyblockprogress.getPosition();
                double d3 = (double)blockpos.getX() - d0;
                double d4 = (double)blockpos.getY() - d1;
                double d5 = (double)blockpos.getZ() - d2;
                Block block = this.theWorld.getBlockState(blockpos).getBlock();
                TileEntity te = this.theWorld.getTileEntity(blockpos);
                boolean hasBreak = block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockSign || block instanceof BlockSkull;
                if (!hasBreak) hasBreak = te != null && te.canRenderBreaking();

                if (!hasBreak)
                {
                    if (d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D)
                    {
                        iterator.remove();
                    }
                    else
                    {
                        IBlockState iblockstate = this.theWorld.getBlockState(blockpos);

                        if (iblockstate.getBlock().getMaterial() != Material.air)
                        {
                            int i = destroyblockprogress.getPartialBlockDamage();
                            TextureAtlasSprite textureatlassprite = this.destroyBlockIcons[i];
                            BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
                            blockrendererdispatcher.renderBlockDamage(iblockstate, blockpos, textureatlassprite, this.theWorld);
                        }
                    }
                }
            }

            p_174981_1_.draw();
            p_174981_2_.setTranslation(0.0D, 0.0D, 0.0D);
            this.postRenderDamagedBlocks();
        }
    }

    /**
     * Draws the selection box for the player. Args: entityPlayer, rayTraceHit, i, itemStack, partialTickTime
     */
    public void drawSelectionBox(EntityPlayer p_72731_1_, MovingObjectPosition p_72731_2_, int p_72731_3_, float p_72731_4_)
    {
        if (p_72731_3_ == 0 && p_72731_2_.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
        {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(2.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            float f1 = 0.002F;
            BlockPos blockpos = p_72731_2_.getBlockPos();
            Block block = this.theWorld.getBlockState(blockpos).getBlock();

            if (block.getMaterial() != Material.air && this.theWorld.getWorldBorder().contains(blockpos))
            {
                block.setBlockBoundsBasedOnState(this.theWorld, blockpos);
                double d0 = p_72731_1_.lastTickPosX + (p_72731_1_.posX - p_72731_1_.lastTickPosX) * (double)p_72731_4_;
                double d1 = p_72731_1_.lastTickPosY + (p_72731_1_.posY - p_72731_1_.lastTickPosY) * (double)p_72731_4_;
                double d2 = p_72731_1_.lastTickPosZ + (p_72731_1_.posZ - p_72731_1_.lastTickPosZ) * (double)p_72731_4_;
                drawOutlinedBoundingBox(block.getSelectedBoundingBox(this.theWorld, blockpos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-d0, -d1, -d2), -1);
            }

            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
        }
    }

    /**
     * Draws lines for the edges of the bounding box.
     *  
     * @param boundingBox The bounding box to draw.
     */
    public static void drawOutlinedBoundingBox(AxisAlignedBB boundingBox, int p_147590_1_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawing(3);

        if (p_147590_1_ != -1)
        {
            worldrenderer.setColorOpaque_I(p_147590_1_);
        }

        worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        tessellator.draw();
        worldrenderer.startDrawing(3);

        if (p_147590_1_ != -1)
        {
            worldrenderer.setColorOpaque_I(p_147590_1_);
        }

        worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        tessellator.draw();
        worldrenderer.startDrawing(1);

        if (p_147590_1_ != -1)
        {
            worldrenderer.setColorOpaque_I(p_147590_1_);
        }

        worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
        worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
        worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
        worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
        worldrenderer.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
        worldrenderer.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
        worldrenderer.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
        worldrenderer.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
        tessellator.draw();
    }

    /**
     * Marks the blocks in the given range for update
     */
    private void markBlocksForUpdate(int p_72725_1_, int p_72725_2_, int p_72725_3_, int p_72725_4_, int p_72725_5_, int p_72725_6_)
    {
        this.viewFrustum.markBlocksForUpdate(p_72725_1_, p_72725_2_, p_72725_3_, p_72725_4_, p_72725_5_, p_72725_6_);
    }

    public void markBlockForUpdate(BlockPos pos)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        this.markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
    }

    public void notifyLightSet(BlockPos pos)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        this.markBlocksForUpdate(i - 1, j - 1, k - 1, i + 1, j + 1, k + 1);
    }

    /**
     * On the client, re-renders all blocks in this range, inclusive. On the server, does nothing. Args: min x, min y,
     * min z, max x, max y, max z
     */
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2)
    {
        this.markBlocksForUpdate(x1 - 1, y1 - 1, z1 - 1, x2 + 1, y2 + 1, z2 + 1);
    }

    public void playRecord(String recordName, BlockPos blockPosIn)
    {
        ISound isound = (ISound)this.mapSoundPositions.get(blockPosIn);

        if (isound != null)
        {
            this.mc.getSoundHandler().stopSound(isound);
            this.mapSoundPositions.remove(blockPosIn);
        }

        if (recordName != null)
        {
            ItemRecord itemrecord = ItemRecord.getRecord(recordName);
            ResourceLocation resource = null;

            if (itemrecord != null)
            {
                this.mc.ingameGUI.setRecordPlayingMessage(itemrecord.getRecordNameLocal());
                resource = itemrecord.getRecordResource(recordName);
            }

            if (resource == null) resource = new ResourceLocation(recordName);
            PositionedSoundRecord positionedsoundrecord = PositionedSoundRecord.create(resource, (float)blockPosIn.getX(), (float)blockPosIn.getY(), (float)blockPosIn.getZ());
            this.mapSoundPositions.put(blockPosIn, positionedsoundrecord);
            this.mc.getSoundHandler().playSound(positionedsoundrecord);
        }
    }

    /**
     * Plays the specified sound. Arg: soundName, x, y, z, volume, pitch
     */
    public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {}

    /**
     * Plays sound to all near players except the player reference given
     */
    public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch) {}

    public void spawnParticle(int p_180442_1_, boolean p_180442_2_, final double p_180442_3_, final double p_180442_5_, final double p_180442_7_, double p_180442_9_, double p_180442_11_, double p_180442_13_, int ... p_180442_15_)
    {
        try
        {
            this.spawnEntityFX(p_180442_1_, p_180442_2_, p_180442_3_, p_180442_5_, p_180442_7_, p_180442_9_, p_180442_11_, p_180442_13_, p_180442_15_);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
            crashreportcategory.addCrashSection("ID", Integer.valueOf(p_180442_1_));

            if (p_180442_15_ != null)
            {
                crashreportcategory.addCrashSection("Parameters", p_180442_15_);
            }

            crashreportcategory.addCrashSectionCallable("Position", new Callable()
            {
                private static final String __OBFID = "CL_00000955";
                public String call()
                {
                    return CrashReportCategory.getCoordinateInfo(p_180442_3_, p_180442_5_, p_180442_7_);
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    private void spawnParticle(EnumParticleTypes particleIn, double p_174972_2_, double p_174972_4_, double p_174972_6_, double p_174972_8_, double p_174972_10_, double p_174972_12_, int ... p_174972_14_)
    {
        this.spawnParticle(particleIn.getParticleID(), particleIn.func_179344_e(), p_174972_2_, p_174972_4_, p_174972_6_, p_174972_8_, p_174972_10_, p_174972_12_, p_174972_14_);
    }

    private EntityFX spawnEntityFX(int p_174974_1_, boolean p_174974_2_, double p_174974_3_, double p_174974_5_, double p_174974_7_, double p_174974_9_, double p_174974_11_, double p_174974_13_, int ... p_174974_15_)
    {
        if (this.mc != null && this.mc.getRenderViewEntity() != null && this.mc.effectRenderer != null)
        {
            int k = this.mc.gameSettings.particleSetting;

            if (k == 1 && this.theWorld.rand.nextInt(3) == 0)
            {
                k = 2;
            }

            double d6 = this.mc.getRenderViewEntity().posX - p_174974_3_;
            double d7 = this.mc.getRenderViewEntity().posY - p_174974_5_;
            double d8 = this.mc.getRenderViewEntity().posZ - p_174974_7_;

            if (p_174974_2_)
            {
                return this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_);
            }
            else
            {
                double d9 = 16.0D;
                return d6 * d6 + d7 * d7 + d8 * d8 > 256.0D ? null : (k > 1 ? null : this.mc.effectRenderer.spawnEffectParticle(p_174974_1_, p_174974_3_, p_174974_5_, p_174974_7_, p_174974_9_, p_174974_11_, p_174974_13_, p_174974_15_));
            }
        }
        else
        {
            return null;
        }
    }

    /**
     * Called on all IWorldAccesses when an entity is created or loaded. On client worlds, starts downloading any
     * necessary textures. On server worlds, adds the entity to the entity tracker.
     */
    public void onEntityAdded(Entity entityIn) {}

    /**
     * Called on all IWorldAccesses when an entity is unloaded or destroyed. On client worlds, releases any downloaded
     * textures. On server worlds, removes the entity from the entity tracker.
     */
    public void onEntityRemoved(Entity entityIn) {}

    /**
     * Deletes all display lists
     */
    public void deleteAllDisplayLists() {}

    public void broadcastSound(int p_180440_1_, BlockPos p_180440_2_, int p_180440_3_)
    {
        switch (p_180440_1_)
        {
            case 1013:
            case 1018:
                if (this.mc.getRenderViewEntity() != null)
                {
                    double d0 = (double)p_180440_2_.getX() - this.mc.getRenderViewEntity().posX;
                    double d1 = (double)p_180440_2_.getY() - this.mc.getRenderViewEntity().posY;
                    double d2 = (double)p_180440_2_.getZ() - this.mc.getRenderViewEntity().posZ;
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    double d4 = this.mc.getRenderViewEntity().posX;
                    double d5 = this.mc.getRenderViewEntity().posY;
                    double d6 = this.mc.getRenderViewEntity().posZ;

                    if (d3 > 0.0D)
                    {
                        d4 += d0 / d3 * 2.0D;
                        d5 += d1 / d3 * 2.0D;
                        d6 += d2 / d3 * 2.0D;
                    }

                    if (p_180440_1_ == 1013)
                    {
                        this.theWorld.playSound(d4, d5, d6, "mob.wither.spawn", 1.0F, 1.0F, false);
                    }
                    else
                    {
                        this.theWorld.playSound(d4, d5, d6, "mob.enderdragon.end", 5.0F, 1.0F, false);
                    }
                }
            default:
        }
    }

    public void playAusSFX(EntityPlayer p_180439_1_, int p_180439_2_, BlockPos blockPosIn, int p_180439_4_)
    {
        Random random = this.theWorld.rand;
        double d0;
        double d1;
        double d2;
        int l;
        int i1;
        double d3;
        double d5;
        double d7;
        double d13;

        switch (p_180439_2_)
        {
            case 1000:
                this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0F, 1.0F, false);
                break;
            case 1001:
                this.theWorld.playSoundAtPos(blockPosIn, "random.click", 1.0F, 1.2F, false);
                break;
            case 1002:
                this.theWorld.playSoundAtPos(blockPosIn, "random.bow", 1.0F, 1.2F, false);
                break;
            case 1003:
                this.theWorld.playSoundAtPos(blockPosIn, "random.door_open", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1004:
                this.theWorld.playSoundAtPos(blockPosIn, "random.fizz", 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
                break;
            case 1005:
                if (Item.getItemById(p_180439_4_) instanceof ItemRecord)
                {
                    this.theWorld.playRecord(blockPosIn, "records." + ((ItemRecord)Item.getItemById(p_180439_4_)).recordName);
                }
                else
                {
                    this.theWorld.playRecord(blockPosIn, (String)null);
                }

                break;
            case 1006:
                this.theWorld.playSoundAtPos(blockPosIn, "random.door_close", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1007:
                this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.charge", 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1008:
                this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 10.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1009:
                this.theWorld.playSoundAtPos(blockPosIn, "mob.ghast.fireball", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1010:
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.wood", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1011:
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.metal", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1012:
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.woodbreak", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1014:
                this.theWorld.playSoundAtPos(blockPosIn, "mob.wither.shoot", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1015:
                this.theWorld.playSoundAtPos(blockPosIn, "mob.bat.takeoff", 0.05F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1016:
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.infect", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1017:
                this.theWorld.playSoundAtPos(blockPosIn, "mob.zombie.unfect", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1020:
                this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_break", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1021:
                this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_use", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1022:
                this.theWorld.playSoundAtPos(blockPosIn, "random.anvil_land", 0.3F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 2000:
                int j1 = p_180439_4_ % 3 - 1;
                int k = p_180439_4_ / 3 % 3 - 1;
                d1 = (double)blockPosIn.getX() + (double)j1 * 0.6D + 0.5D;
                d2 = (double)blockPosIn.getY() + 0.5D;
                d13 = (double)blockPosIn.getZ() + (double)k * 0.6D + 0.5D;

                for (int k1 = 0; k1 < 10; ++k1)
                {
                    double d14 = random.nextDouble() * 0.2D + 0.01D;
                    double d15 = d1 + (double)j1 * 0.01D + (random.nextDouble() - 0.5D) * (double)k * 0.5D;
                    double d4 = d2 + (random.nextDouble() - 0.5D) * 0.5D;
                    double d6 = d13 + (double)k * 0.01D + (random.nextDouble() - 0.5D) * (double)j1 * 0.5D;
                    double d8 = (double)j1 * d14 + random.nextGaussian() * 0.01D;
                    double d10 = -0.03D + random.nextGaussian() * 0.01D;
                    double d12 = (double)k * d14 + random.nextGaussian() * 0.01D;
                    this.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d15, d4, d6, d8, d10, d12, new int[0]);
                }

                return;
            case 2001:
                Block block = Block.getBlockById(p_180439_4_ & 4095);

                if (block.getMaterial() != Material.air)
                {
                    this.mc.getSoundHandler().playSound(new PositionedSoundRecord(new ResourceLocation(block.stepSound.getBreakSound()), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getFrequency() * 0.8F, (float)blockPosIn.getX() + 0.5F, (float)blockPosIn.getY() + 0.5F, (float)blockPosIn.getZ() + 0.5F));
                }

                this.mc.effectRenderer.func_180533_a(blockPosIn, block.getStateFromMeta(p_180439_4_ >> 12 & 255));
                break;
            case 2002:
                d0 = (double)blockPosIn.getX();
                d1 = (double)blockPosIn.getY();
                d2 = (double)blockPosIn.getZ();

                for (l = 0; l < 8; ++l)
                {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d0, d1, d2, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, new int[] {Item.getIdFromItem(Items.potionitem), p_180439_4_});
                }

                l = Items.potionitem.getColorFromDamage(p_180439_4_);
                float f = (float)(l >> 16 & 255) / 255.0F;
                float f1 = (float)(l >> 8 & 255) / 255.0F;
                float f2 = (float)(l >> 0 & 255) / 255.0F;
                EnumParticleTypes enumparticletypes = EnumParticleTypes.SPELL;

                if (Items.potionitem.isEffectInstant(p_180439_4_))
                {
                    enumparticletypes = EnumParticleTypes.SPELL_INSTANT;
                }

                for (i1 = 0; i1 < 100; ++i1)
                {
                    d3 = random.nextDouble() * 4.0D;
                    d5 = random.nextDouble() * Math.PI * 2.0D;
                    d7 = Math.cos(d5) * d3;
                    double d9 = 0.01D + random.nextDouble() * 0.5D;
                    double d11 = Math.sin(d5) * d3;
                    EntityFX entityfx = this.spawnEntityFX(enumparticletypes.getParticleID(), enumparticletypes.func_179344_e(), d0 + d7 * 0.1D, d1 + 0.3D, d2 + d11 * 0.1D, d7, d9, d11, new int[0]);

                    if (entityfx != null)
                    {
                        float f3 = 0.75F + random.nextFloat() * 0.25F;
                        entityfx.setRBGColorF(f * f3, f1 * f3, f2 * f3);
                        entityfx.multiplyVelocity((float)d3);
                    }
                }

                this.theWorld.playSoundAtPos(blockPosIn, "game.potion.smash", 1.0F, this.theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 2003:
                d0 = (double)blockPosIn.getX() + 0.5D;
                d1 = (double)blockPosIn.getY();
                d2 = (double)blockPosIn.getZ() + 0.5D;

                for (l = 0; l < 8; ++l)
                {
                    this.spawnParticle(EnumParticleTypes.ITEM_CRACK, d0, d1, d2, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D, new int[] {Item.getIdFromItem(Items.ender_eye)});
                }

                for (d13 = 0.0D; d13 < (Math.PI * 2D); d13 += 0.15707963267948966D)
                {
                    this.spawnParticle(EnumParticleTypes.PORTAL, d0 + Math.cos(d13) * 5.0D, d1 - 0.4D, d2 + Math.sin(d13) * 5.0D, Math.cos(d13) * -5.0D, 0.0D, Math.sin(d13) * -5.0D, new int[0]);
                    this.spawnParticle(EnumParticleTypes.PORTAL, d0 + Math.cos(d13) * 5.0D, d1 - 0.4D, d2 + Math.sin(d13) * 5.0D, Math.cos(d13) * -7.0D, 0.0D, Math.sin(d13) * -7.0D, new int[0]);
                }

                return;
            case 2004:
                for (i1 = 0; i1 < 20; ++i1)
                {
                    d3 = (double)blockPosIn.getX() + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
                    d5 = (double)blockPosIn.getY() + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
                    d7 = (double)blockPosIn.getZ() + 0.5D + ((double)this.theWorld.rand.nextFloat() - 0.5D) * 2.0D;
                    this.theWorld.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d5, d7, 0.0D, 0.0D, 0.0D, new int[0]);
                    this.theWorld.spawnParticle(EnumParticleTypes.FLAME, d3, d5, d7, 0.0D, 0.0D, 0.0D, new int[0]);
                }

                return;
            case 2005:
                ItemDye.spawnBonemealParticles(this.theWorld, blockPosIn, p_180439_4_);
        }
    }

    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress)
    {
        if (progress >= 0 && progress < 10)
        {
            DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)this.damagedBlocks.get(Integer.valueOf(breakerId));

            if (destroyblockprogress == null || destroyblockprogress.getPosition().getX() != pos.getX() || destroyblockprogress.getPosition().getY() != pos.getY() || destroyblockprogress.getPosition().getZ() != pos.getZ())
            {
                destroyblockprogress = new DestroyBlockProgress(breakerId, pos);
                this.damagedBlocks.put(Integer.valueOf(breakerId), destroyblockprogress);
            }

            destroyblockprogress.setPartialBlockDamage(progress);
            destroyblockprogress.setCloudUpdateTick(this.cloudTickCounter);
        }
        else
        {
            this.damagedBlocks.remove(Integer.valueOf(breakerId));
        }
    }

    public void setDisplayListEntitiesDirty()
    {
        this.displayListEntitiesDirty = true;
    }

    @SideOnly(Side.CLIENT)
    class ContainerLocalRenderInformation
    {
        final RenderChunk renderChunk;
        final EnumFacing facing;
        final Set setFacing;
        final int counter;
        private static final String __OBFID = "CL_00002534";

        private ContainerLocalRenderInformation(RenderChunk p_i46248_2_, EnumFacing p_i46248_3_, int p_i46248_4_)
        {
            this.setFacing = EnumSet.noneOf(EnumFacing.class);
            this.renderChunk = p_i46248_2_;
            this.facing = p_i46248_3_;
            this.counter = p_i46248_4_;
        }

        ContainerLocalRenderInformation(RenderChunk p_i46249_2_, EnumFacing p_i46249_3_, int p_i46249_4_, Object p_i46249_5_)
        {
            this(p_i46249_2_, p_i46249_3_, p_i46249_4_);
        }
    }

    @SideOnly(Side.CLIENT)

    static final class SwitchEnumUsage
        {
            static final int[] VALUES = new int[VertexFormatElement.EnumUsage.values().length];
            private static final String __OBFID = "CL_00002535";

            static
            {
                try
                {
                    VALUES[VertexFormatElement.EnumUsage.POSITION.ordinal()] = 1;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    VALUES[VertexFormatElement.EnumUsage.UV.ordinal()] = 2;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    VALUES[VertexFormatElement.EnumUsage.COLOR.ordinal()] = 3;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}